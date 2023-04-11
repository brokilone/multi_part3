package interrupt;

import java.util.ArrayList;
import java.util.List;

public class SimpleFlag {

    public static void main(String[] args) throws InterruptedException {

      InnerQueue queue = new InnerQueue();

      Thread worker = new Thread(new Runnable() {
        @Override
        public void run() {
          while (!Thread.currentThread().isInterrupted()) {
            Runnable task = null;
            try {
              task = queue.get();
              task.run();
            } catch (InterruptedException e) {
              Thread.currentThread().interrupt();
            }

          }
        }
      });
      worker.start();

      for (int i = 0; i < 5; i++) {
        queue.put(createTask(i));
      }

      Thread.sleep(3000);

      worker.interrupt();
    }

    public static Runnable createTask(int number) {
      return new Runnable() {
        @Override
        public void run() {
          System.out.println("Task started: " + number);
          try {
            Thread.sleep(1000);
          } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
          }
          System.out.println("Task finished: " + number);
        }
      };
    }

    static class InnerQueue {
      List<Runnable> tasks = new ArrayList<>();

      public synchronized Runnable get() throws InterruptedException {
        while (tasks.isEmpty()) {
            wait();

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
