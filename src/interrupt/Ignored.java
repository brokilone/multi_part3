package interrupt;

import java.util.concurrent.TimeUnit;

public class Ignored {
  public static void main(String[] args) {

    Thread t = new Thread(new Runnable() {
      @Override
      public void run() {
        while (true) {
          try {
            TimeUnit.SECONDS.sleep(2);
          } catch (InterruptedException e) {
            System.out.println("Исключение выбросилось");
          }

          System.out.println("Но поток продолжает работу");
        }
      }
    });

    t.start();
    System.out.println("Прерываю поток");
    t.interrupt();
  }
}

