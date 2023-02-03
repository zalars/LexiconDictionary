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

    // столбцы в массиве value (в мэпе)
    private static final int HASH = 0;
    private static final int DEFINITION = 1;

    private final ConcurrentMap<String, String[]> records;  // <слово, [хэш, определение]>

    public Dictionary() {
        this.records = new ConcurrentHashMap<>(48752, 1, 1);
        loadRecords();
    }

    private void loadRecords() {
        try {
            BufferedReader fileReader = new BufferedReader(
                    new FileReader("RusVocHtml.txt", StandardCharsets.UTF_8));
            String fileLine;
            while ((fileLine = fileReader.readLine()) != null) {
                String[] singleRecord = fileLine.split("@");
                // индекс: 0 - слово, 1 - хэш, 2 - определение
                this.records.put(singleRecord[0], new String[] {singleRecord[1], singleRecord[2]});
            }
        } catch (IOException e) {
            System.err.println("ERROR: Какая-то проблема с файлом словаря (RusVocHtml.txt) - " +
                    "он должен находиться рядом с jar-файлом приложения");
        }
    }

    public boolean isLoaded() {
        return this.records.size() > 0;
    }

    public String readDefinitionOf(String word) {
        return this.records.get(word)[DEFINITION];
    }

    public String readHashOf(String word) {
        return this.records.get(word)[HASH];
    }

    public Set<String> readAllWords() {
        return this.records.keySet();
    }
}
