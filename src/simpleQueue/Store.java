package simpleQueue;

import java.util.Queue;
import java.util.Random;

public class Store {
  private final Queue<Integer> products;
  private static final int MAX_SIZE = 10;
  private static final Random RANDOM = new Random();

  public Store(Queue<Integer> products) {
    this.products = products;
  }

  public synchronized void get() {
    while (products.isEmpty()) {
      System.out.println("wait for put");
      try {
        wait();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    products.poll();
    System.out.println("Покупатель забрал товар. Размер очереди: " + products.size());
    notify();
  }

  public synchronized void put() {
    while (products.size() == MAX_SIZE) {
      System.out.println("wait for get");
      try {
        wait();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    products.offer(RANDOM.nextInt(10));
    System.out.println("Производитель привез товар");
    notify();
  }
}
