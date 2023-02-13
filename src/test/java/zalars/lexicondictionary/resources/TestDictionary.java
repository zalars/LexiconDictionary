package zalars.lexicondictionary.resources;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class TestDictionary {

    static Dictionary dictionary;

    @BeforeAll
    static void initializeDictionary() {
        dictionary = new Dictionary();
    }

    @AfterAll
    static void deleteDictionary() {
        dictionary = null;
    }

    @Test
    void testLoadFrom_shouldReturnNullOrNotNullRecords() {
        try {
            Method method = Dictionary.class.getDeclaredMethod("loadFrom", String.class);
            method.setAccessible(true);
            assertAll(
                    () -> assertThat(method.invoke(dictionary, "w-r-o-n-g-F-i-l-e")).isNull(),
                    () -> assertThat(method.invoke(dictionary, "RusVocHtml.txt")).isNotNull()
            );
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testReadDefinitionOf_testReadHashOf_testReadAllWords() {
        ConcurrentMap<String, String[]> fakeRecords = new ConcurrentHashMap<>(Map.of(
                "word1", new String[] {"1", "definition1"},
                "word2", new String[] {"2", "definition2"}
        ));
        try {
            Field field = Dictionary.class.getDeclaredField("records");
            field.setAccessible(true);
            field.set(dictionary, fakeRecords);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        assertAll(
                () -> assertThat(dictionary.readDefinitionOf("word1")).isEqualTo("definition1"),
                () -> assertThat(dictionary.readHashOf("word2")).isEqualTo("2"),
                () -> assertThat(dictionary.readAllWords()).isEqualTo(Set.of("word1", "word2"))
        );
    }
}
