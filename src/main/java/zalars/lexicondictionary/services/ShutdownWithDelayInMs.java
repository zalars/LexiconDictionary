package zalars.lexicondictionary.services;

public class ShutdownWithDelayInMs implements Runnable {

    private final long delay;

    public ShutdownWithDelayInMs(long delay) {
        this.delay = delay;
    }

    @Override
    public void run() {
        stopProgramWith(this.delay);
    }

    private void stopProgramWith(long delay) {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException ignored) { }

        System.err.println("ERROR [в методе Dictionary.loadFrom()]: Какая-то проблема с файлом словаря" +
                " (RusVocHtml.txt) - он должен находиться рядом с jar-файлом приложения");
        System.exit(1);
    }
}
