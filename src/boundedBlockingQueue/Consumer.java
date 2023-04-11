package boundedBlockingQueue;

public class Consumer extends Thread {
  private final Store store;

  public Consumer(Store store) {
    this.store = store;
  }

  @Override
  public void run() {
    while (true) {
      try {
        Thread.sleep(2000);
        store.get();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }
}
