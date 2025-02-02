import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.Condition;

public class laba17 {
    private static final Lock lock = new ReentrantLock();
    private static final Condition condition = lock.newCondition();
    private static boolean isReady = false;

    public static void main(String[] args) {
        Runnable task1 = () -> {
            try {
                lock.lock();
                while (!isReady) {
                    condition.await();  // ждём пока isReady не станет true
                }
                System.out.println(Thread.currentThread().getName() + " продолжает выполнение");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        };

        Runnable task2 = () -> {
            try {
                lock.lock();
                Thread.sleep(2000);  // работает
                isReady = true;
                condition.signal();  // уведомляем первый поток
                System.out.println(Thread.currentThread().getName() + " уведомил поток");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        };

        Thread thread1 = new Thread(task1);
        Thread thread2 = new Thread(task2);
        thread1.start();
        thread2.start();
    }
}
