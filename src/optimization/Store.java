package optimization;

import java.util.Queue;
import java.util.Random;

public class Store {
  private final Queue<Integer> products;
  private static final int MAX_SIZE = 10;
  private static final Random RANDOM = new Random();
  private int waitingConsumers;
  private int waitingProducers;

  public Store(Queue<Integer> products) {
    this.products = products;
  }

  public synchronized void get() {
    while (products.isEmpty()) {
      System.out.println("wait for put");
      try {
        waitingConsumers++;
        wait();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    waitingConsumers--;
    products.poll();
    System.out.println("Покупатель забрал товар. Размер очереди: " + products.size());
    if (waitingProducers > 0 && products.size() < MAX_SIZE) {
      notify();
    }
  }

  public synchronized void put() {
    while (products.size() == MAX_SIZE) {
      System.out.println("wait for get");
      try {
        waitingProducers++;
        wait();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    waitingProducers--;
    products.offer(RANDOM.nextInt(10));
    System.out.println("Производитель привез товар");
    if (waitingConsumers > 0) {
      notify();
    }

  }
}
