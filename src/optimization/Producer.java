package optimization;

public class Producer extends Thread {
  private final Store store;

  public Producer(Store store) {
    this.store = store;
  }

  @Override
  public void run() {
    while (true) {
      try {
        Thread.sleep(1000);
        store.put();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }
}
