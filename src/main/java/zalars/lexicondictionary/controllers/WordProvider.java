package zalars.lexicondictionary.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zalars.lexicondictionary.services.Selector;

@RestController
@RequestMapping("/dictionary")
public class WordProvider {

    private final Selector selector;

    public WordProvider(Selector selector) {
        this.selector = selector;
    }

    @GetMapping("/{requestValue}")
    public String provideByLength(@PathVariable Integer requestValue) {
        if (requestValue > 0) {
            return this.selector.makeSelectionBy(requestValue);
        } else {
            return this.selector.testAvailability();  // "PASSED" или "FAILED"
        }
    }
}
