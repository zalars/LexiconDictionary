package zalars.lexicondictionary.service;

import org.springframework.stereotype.Service;
import zalars.lexicondictionary.resources.Attributes;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
public class Dictionary {

    private final ConcurrentMap<String, Attributes> records;

    public Dictionary() {
        this.records = new ConcurrentHashMap<>(48752, 1, 1);
        this.populateDictionary();
    }

    private void populateDictionary() {
        try {
            BufferedReader reader = new BufferedReader(
                    new FileReader("RusVocHtml.txt", StandardCharsets.UTF_8));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] recordArray = line.split("@");
                records.put(recordArray[0], new Attributes(recordArray[1], recordArray[2]));
            }
        } catch (IOException e) {
            System.err.println("ERROR: Какая-то проблема с файлом словаря. Увы, программа не будет работать!");
            throw new RuntimeException(e);
        }
    }

    public String getSelection(Integer wordLength) {
        final String[] wordsArray = records.keySet().stream()
                                                    .filter(w -> w.length() == wordLength)
                                                    .toArray(String[]::new);
        String source;
        String derivedWithDefinitions;
        do {
            source = wordsArray[new Random().nextInt(wordsArray.length)];
            derivedWithDefinitions = getDerivedWithDefinitions(source);
        } while (derivedWithDefinitions.isEmpty());

        return getWithDefinitions(source) + "@@@" + derivedWithDefinitions;
    }

    private String getWithDefinitions(String word) {
        return word + "@" + records.get(word).getDefinitions();
    }

    private String getDerivedWithDefinitions(String sourceWord) {
        BigInteger sourceHash = new BigInteger(records.get(sourceWord).getHash());
        StringBuilder resultSet = new StringBuilder();
        int sourceHashSize = sourceHash.toString().length();

        for (ConcurrentMap.Entry<String, Attributes> entry : records.entrySet()) {
            String word = entry.getKey();
            BigInteger wordHash = new BigInteger(entry.getValue().getHash());
            if (wordHash.toString().length() > sourceHashSize || sourceWord.equals(word)) {
                continue;
            }
            if (sourceHash.remainder(wordHash).equals(BigInteger.ZERO)) {
                resultSet.append(getWithDefinitions(word));
                resultSet.append("@@");
            }
        }
        return resultSet.toString();
    }

}
