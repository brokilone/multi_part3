package lock;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LiveLock {

  private final Lock lock1 = new ReentrantLock(true);
  private final Lock lock2 = new ReentrantLock(true);
  private final Random random = new Random();

  public static void main(String[] args) {
    LiveLock livelock = new LiveLock();
    new Thread(livelock::operation1, "Thread1").start();
    new Thread(livelock::operation2, "Thread2").start();
  }

  public void operation1() {
    while (true) {
      tryLock(lock1, random.nextInt(5));
      System.out.println("lock1 acquired, trying to acquire lock2.");
      try {
        Thread.sleep(random.nextInt(5));
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }

      if (tryLock(lock2)) {
        System.out.println("lock2 acquired.");
      } else {
        System.out.println("cannot acquire lock2, releasing lock1.");
        lock1.unlock();
        continue;
      }

      System.out.println("executing first operation.");
      break;
    }
    lock2.unlock();
    lock1.unlock();
  }

  public void operation2() {
    while (true) {
      tryLock(lock2, random.nextInt(5));
      System.out.println("lock2 acquired, trying to acquire lock1.");
      try {
        Thread.sleep(random.nextInt(5));
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      }

      if (tryLock(lock1)) {
        System.out.println("lock1 acquired.");
      } else {
        System.out.println("cannot acquire lock1, releasing lock2.");
        lock2.unlock();
        continue;
      }

      System.out.println("executing second operation.");
      break;
    }
    lock1.unlock();
    lock2.unlock();
  }

  private boolean tryLock(Lock lock, int i) {
    try {
      return lock.tryLock(i, TimeUnit.MILLISECONDS);
    } catch (InterruptedException e) {
      e.printStackTrace();
      return false;
    }
  }

  private boolean tryLock(Lock lock) {
    return lock.tryLock();
  }
}
