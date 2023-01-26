package zalars.lexicondictionary.resources;

public class Attributes {

    private final String hash;
    private final String definitions;

    public Attributes(String hash, String definitions) {
        this.hash = hash;
        this.definitions = definitions;
    }

    public String getHash() {
        return hash;
    }

    public String getDefinitions() {
        return definitions;
    }
}
