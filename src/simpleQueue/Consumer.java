package simpleQueue;

public class Consumer extends Thread {
  private Store store;

  public Consumer(Store store) {
    this.store = store;
  }

  @Override
  public void run() {
    while (true) {
      try {
        Thread.sleep(1000);
        store.get();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }
}
