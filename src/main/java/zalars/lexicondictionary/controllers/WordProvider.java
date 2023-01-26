package zalars.lexicondictionary.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import zalars.lexicondictionary.service.Dictionary;

@RestController
@RequestMapping("/dictionary")
public class WordProvider {

    private final Dictionary dictionary;

    @Autowired
    public WordProvider(Dictionary dictionary) {
        this.dictionary = dictionary;
    }

    @GetMapping("/{wordLength}")
    public String provideByLength(@PathVariable Integer wordLength) {
        return this.dictionary.getSelection(wordLength);
    }

}
