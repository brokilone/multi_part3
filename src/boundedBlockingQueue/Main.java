package boundedBlockingQueue;

import java.util.concurrent.LinkedBlockingQueue;

public class Main {
  public static void main(String[] args) throws InterruptedException {
    Store store = new Store(new LinkedBlockingQueue<>(10));

    for (int i = 0; i < 5; i++) {
      new Producer(store).start();
    }

    Thread.sleep(2000);

    for (int i = 0; i < 5; i++) {
      new Consumer(store).start();
    }
  }
}
