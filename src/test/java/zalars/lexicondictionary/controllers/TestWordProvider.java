package zalars.lexicondictionary.controllers;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import zalars.lexicondictionary.services.Selector;

@ExtendWith(MockitoExtension.class)
class TestWordProvider {

    @Mock
    private Selector selector;

    @InjectMocks
    private WordProvider provider;

    @Test
    void testProvideByLength() {
        final int someWordLength = 5;
        provider.provideByLength(someWordLength);
        Mockito.verify(selector).makeSelectionBy(someWordLength);
    }
}