package optimization;

import java.util.LinkedList;

public class Main {
  public static void main(String[] args) throws InterruptedException {
    Store store = new Store(new LinkedList<>());

    for (int i = 0; i < 5; i++) {
      new Producer(store).start();
    }

    Thread.sleep(2000);

    for (int i = 0; i < 5; i++) {
      new Consumer(store).start();
    }
  }
}
