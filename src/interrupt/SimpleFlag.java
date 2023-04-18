package interrupt;

import java.util.ArrayList;
import java.util.List;

public class SimpleFlag {
  public static volatile boolean stop = false;

    public static void main(String[] args) throws InterruptedException {

      InnerQueue queue = new InnerQueue();

      Thread worker = new Thread(new Runnable() {
        @Override
        public void run() {
          while (!stop) {
            Runnable task = queue.get();
            task.run();
          }
        }
      });
      worker.start();

      for (int i = 0; i < 5; i++) {
        queue.put(createTask(i));
      }

      Thread.sleep(3000);

      stop = true;
    }

    public static Runnable createTask(int number) {
      return new Runnable() {
        @Override
        public void run() {
          System.out.println("Task started: " + number);
          try {
            Thread.sleep(1000);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
          System.out.println("Task finished: " + number);
        }
      };
    }

    static class InnerQueue {
      List<Runnable> tasks = new ArrayList<>();

      public synchronized Runnable get() {
        while (tasks.isEmpty()) {
          try {
            wait();
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
        Runnable task = tasks.get(0);
        tasks.remove(task);
        return task;
      }

      public synchronized void put(Runnable task) {
        tasks.add(task);
        notify();
      }
    }
}
