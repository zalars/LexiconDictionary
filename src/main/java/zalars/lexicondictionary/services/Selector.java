package zalars.lexicondictionary.services;

import org.springframework.stereotype.Service;
import zalars.lexicondictionary.resources.Dictionary;

import java.math.BigInteger;
import java.util.Random;

@Service
public class Selector {

    private final Dictionary dictionary;

    public Selector(Dictionary dictionary) {
        this.dictionary = dictionary.hasRecords() ? dictionary : null;
    }

    public String makeSelectionBy(Integer wordLength) {
        // отбор в массив слов нужной длины
        final String[] wordsOfSameLength;
        try {
            wordsOfSameLength = this.dictionary.readAllWords()
                                               .stream()
                                               .filter(w -> w.length() == wordLength)
                                               .toArray(String[]::new);
            // NPE, если словарь не загружен (проблема с файлом словаря), т.е. равен null
        } catch (NullPointerException e) {
            new Thread(new ShutdownWithDelayInMs(3000)).start();
            return "ERROR:3";
        }
        String source, allDerivedWithDefinitions;
        do {
            source = wordsOfSameLength[new Random().nextInt(wordsOfSameLength.length)];
            allDerivedWithDefinitions = getAllDerivedWithDefinitions(source);
        } while (allDerivedWithDefinitions.isEmpty());
        return getWordWithDefinition(source) + "@@" + allDerivedWithDefinitions;
    }

    private String getWordWithDefinition(String word) {
        return word + "@" + this.dictionary.readDefinitionOf(word);
    }

    private String getAllDerivedWithDefinitions(String source) {
        BigInteger sourceHash = new BigInteger(this.dictionary.readHashOf(source));
        StringBuilder result = new StringBuilder();
        int sourceHashSize = sourceHash.toString().length();
        for (String word : this.dictionary.readAllWords()) {
            BigInteger wordHash = new BigInteger(this.dictionary.readHashOf(word));
            if (wordHash.toString().length() > sourceHashSize || source.equals(word)) {
                continue;
            }
            if (sourceHash.remainder(wordHash).equals(BigInteger.ZERO)) {
                result.append(getWordWithDefinition(word));
                result.append("@@");
            }
        }
        return result.toString();
    }
}
