package locksafe;

import java.util.Random;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class DeadlockSafe {
  public static void main(String[] args) throws InterruptedException {
    Account account1 = new Account();
    Account account2 = new Account();
    Thread thread1 = new TransferThread(account1, account2);
    Thread thread2 = new TransferThread(account2, account1);

    thread1.start();
    thread2.start();

    thread1.join();
    thread2.join();

    System.out.println("Account1: " + account1.getBalance());
    System.out.println("Account2: " + account2.getBalance());
  }
}

class Account {
  private int balance = 10000;
  private final Lock lock = new ReentrantLock();

  public Lock getLock() {
    return lock;
  }

  public int getBalance() {
    return balance;
  }

  public void add(int money) {
    lock.lock();
    try {
      this.balance += money;
    } finally {
      lock.unlock();
    }
  }

  public boolean takeOff(int money) {
    lock.lock();
    try {
      if (this.balance >= money) {
        this.balance -= money;
        return true;
      }
      return false;
    } finally {
      lock.unlock();
    }
  }
}

class TransferThread extends Thread {
  private final Random random = new Random();
  private final Account accountFrom;
  private final Account accountTo;

  private void takeLocks(Lock lock1, Lock lock2) throws InterruptedException {
    System.out.println(Thread.currentThread().getName() + " try lock");
    boolean lock1Taken = false;
    boolean lock2Taken = false;

    while (true) {
      try {
        lock1Taken = lock1.tryLock();
        lock2Taken = lock2.tryLock();
      } finally {
        if (lock1Taken && lock2Taken) {
          System.out.println(Thread.currentThread().getName() + " Both locks taken");
          return;
        }
        if (lock1Taken) {
          System.out.println(Thread.currentThread().getName() + " only lock1 taken. Unlocking");
          lock1.unlock();
        }
        if (lock2Taken) {
          System.out.println(Thread.currentThread().getName() + " only lock2 taken. Unlocking");
          lock2.unlock();
        }
      }
      Thread.sleep(1);
    }
  }

  public TransferThread(Account accountFrom, Account accountTo) {
    this.accountFrom = accountFrom;
    this.accountTo = accountTo;
  }

  @Override
  public void run() {
    for (int i = 0; i < 500; i++) {
      Lock lock1 = accountFrom.getLock();
      Lock lock2 = accountTo.getLock();
      try {
        takeLocks(lock1, lock2);
        int amount = random.nextInt(100);
        if (accountFrom.takeOff(amount)) {
          accountTo.add(amount);
          System.out.println(Thread.currentThread().getName() + " transferred " + amount + " units");
        }
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      } finally {
        System.out.println(Thread.currentThread().getName() + " finish task. Unlocking");
        lock1.unlock();
        lock2.unlock();
      }
    }
  }
}

