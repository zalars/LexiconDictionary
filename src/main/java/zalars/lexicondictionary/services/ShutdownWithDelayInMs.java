package zalars.lexicondictionary.services;

public class ShutdownWithDelayInMs implements Runnable {

    private final long delay;

    public ShutdownWithDelayInMs(long delay) {
        this.delay = delay;
    }

    @Override
    public void run() {
        stopProgramWithDelay(this.delay);
    }

    private void stopProgramWithDelay(long delay) {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException ignored) {  }
        throw new RuntimeException("Проблема с файлом словаря [метод Dictionary.loadRecords()]");
    }
}
