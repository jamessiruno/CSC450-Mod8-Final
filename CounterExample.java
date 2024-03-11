public class CounterExample {
    public static void main(String[] args) {
        Counter counter = new Counter();

        Thread thread1 = new Thread(() -> counter.countUp(20), "CounterUp");
        Thread thread2 = new Thread(() -> counter.countDown(20), "CounterDown");

        thread1.start();
        thread2.start();
    }
}

class Counter {
    private final Object lock = new Object();
    private boolean upFinished = false;

    public void countUp(int target) {
        synchronized (lock) {
            try {
                for (int i = 1; i <= target; i++) {
                    System.out.println(Thread.currentThread().getName() + ": " + i);
                    Thread.sleep(500);
                    if (i == target) {
                        upFinished = true;
                        lock.notify(); // Notify countDown thread
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void countDown(int target) {
        synchronized (lock) {
            try {
                while (!upFinished) {
                    lock.wait(); // Wait for countUp thread to finish
                }
                for (int i = target; i >= 0; i--) {
                    System.out.println(Thread.currentThread().getName() + ": " + i);
                    Thread.sleep(500);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
