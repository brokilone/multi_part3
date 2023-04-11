package lock;

public class Deadlock {
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
  public int getBalance() {
    return balance;
  }

  private int balance = 10000;

  public void add(int money) {
    this.balance += money;
  }

  public boolean takeOff(int money) {
    if (this.balance >= money) {
      this.balance -= money;
      return true;
    }
    return false;
  }
}

class TransferThread extends Thread {
  private final Account accountFrom;
  private final Account accountTo;

  public TransferThread(Account accountFrom, Account accountTo) {
    this.accountFrom = accountFrom;
    this.accountTo = accountTo;
  }


  @Override
  public void run() {
    System.out.println("Thread  " + Thread.currentThread().getName() + " started");
    for (int i = 0; i < 2000; i++) {
      synchronized (accountFrom) {
        synchronized (accountTo) {
          if (accountFrom.takeOff(10)) {
            accountTo.add(10);
          }
        }
      }
    }
    System.out.println("Thread  " + Thread.currentThread().getName() + " finished");
  }
}

