package boundedBlockingQueue;

import java.util.Random;
import java.util.concurrent.BlockingQueue;

public class Store {
  private final BlockingQueue<Integer> products;
  private static final Random RANDOM = new Random();

  public Store(BlockingQueue<Integer> products) {
    this.products = products;
  }

  public void get() throws InterruptedException {
    products.take();
    System.out.println("Покупатель забрал товар");
  }

  public void put() throws InterruptedException {
    products.put(RANDOM.nextInt(10));
    System.out.println("Производитель привез товар");
  }
}
