package zalars.lexicondictionary.resources;

import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
public class Dictionary {

    private final ConcurrentMap<String, String[]> records;  // <слово, [хэш, определение]>

    public Dictionary() {
        String fileName = "RusVocHtml.txt";
        this.records = loadFrom(fileName);
    }

    private ConcurrentMap<String, String[]> loadFrom(String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName, StandardCharsets.UTF_8))) {
            ConcurrentMap<String, String[]> initialRecords =
                    new ConcurrentHashMap<>(48752, 1, 1);
            String fileLine;
            while ((fileLine = reader.readLine()) != null) {
                String[] singleRecord = fileLine.split("@");
                // индекс: 0 - слово, 1 - хэш, 2 - определение
                initialRecords.put(singleRecord[0], new String[] {singleRecord[1], singleRecord[2]});
            }
            return initialRecords;
        } catch (IOException | NullPointerException e) {
            return null;
        }
    }
    
    public boolean hasRecords() {
        return this.records != null;
    }

    public String readDefinitionOf(String word) {
        final int DEFINITION = 1;
        return this.records.get(word)[DEFINITION];
    }

    public String readHashOf(String word) {
        final int HASH = 0;
        return this.records.get(word)[HASH];
    }

    public Set<String> readAllWords() {
        return this.records.keySet();
    }
}
