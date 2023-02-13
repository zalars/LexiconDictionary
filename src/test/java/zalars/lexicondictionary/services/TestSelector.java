package zalars.lexicondictionary.services;

import org.junit.jupiter.api.Test;
import zalars.lexicondictionary.resources.Dictionary;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.*;

class TestSelector {

    private static final Dictionary dictionary = new Dictionary();
    private static Selector selector;

    @Test
    void testMakeSelectionBy_whenDictionaryIsNotNull_shouldReturnString() {
        // модель соответствия букв простым числам (для хэша): г-2, н-3, р-5, т-7, у-11
        ConcurrentMap<String, String[]> fakeRecords = new ConcurrentHashMap<>(Map.of(
                "грунт", new String[] {"2310", "определение-грунт"},
                "гурт", new String[] {"770", "определение-гурт"},
                "тур", new String[] {"385", "определение-тур"},
                "нут", new String[] {"231", "определение-нут"}
        ));
        setSelectorAndDictionaryBy(fakeRecords);
        String expected = "грунт@определение-грунт@@гурт@определение-гурт@@" +
                "тур@определение-тур@@нут@определение-нут@@";
        String actual = selector.makeSelectionBy(5);
        Set<String> expectedSet = Arrays.stream(expected.split("@@")).collect(Collectors.toSet());
        Set<String> actualSet = Arrays.stream(actual.split("@@")).collect(Collectors.toSet());
        assertThat(actualSet).isEqualTo(expectedSet);
    }

    @Test
    void testMakeSelectionBy_whenDictionaryIsNull_shouldReturnErrorString() {
        setSelectorAndDictionaryBy(null);
        assertThat(selector.makeSelectionBy(5)).isEqualTo("ERROR:3");
    }

    private void setSelectorAndDictionaryBy(ConcurrentMap<String, String[]> records) {
        try {
            Field field = Dictionary.class.getDeclaredField("records");
            field.setAccessible(true);
            field.set(dictionary, records);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        selector = new Selector(dictionary);
    }
}
