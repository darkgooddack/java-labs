import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Circus {
    private final Lock lock = new ReentrantLock(); // мьютекс
    private final Condition lionTurn = lock.newCondition(); // условия для появления животных
    private final Condition tigerTurn = lock.newCondition();

    private int lionsLeft;
    private int tigersLeft;
    private boolean lastWasLion = false; //первым выходит лев

    // конструктор класса
    public Circus(int lions, int tigers) {
        this.lionsLeft = lions;
        this.tigersLeft = tigers;
    }

    public void enterLion(int id) throws InterruptedException {
        lock.lock();
        try {
            while (lastWasLion) {  //  последний был лев, пыпускаем тигра
                lionTurn.await();
            }
            System.out.println("Лев " + id + " вышел на арену");
            lastWasLion = true;
            lionsLeft--;
            tigerTurn.signal(); // будим тигра
        } finally {
            lock.unlock();
        }
    }

    public void enterTiger(int id) throws InterruptedException {
        lock.lock();
        try {
            while (!lastWasLion) {  // последний был тигр, выпускаем льва
                tigerTurn.await();
            }
            System.out.println("Тигр " + id + " вышел на арену");
            lastWasLion = false;
            tigersLeft--;
            lionTurn.signal(); // Будим льва
        } finally {
            lock.unlock();
        }
    }
}

public class laba19 {
    public static void main(String[] args) {
        int n = 5; // львы
        int m = n - 1; // тигры
        Circus circus = new Circus(n, m); // передаём их в конструктор класса

        Thread[] threads = new Thread[n + m]; // на весь зоопарк создаём потоки

        for (int i = 0; i < n; i++) {
            final int id = i + 1;
            threads[i] = new Thread(() -> {
                try {
                    circus.enterLion(id);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

        for (int i = 0; i < m; i++) {
            final int id = i + 1;
            threads[n + i] = new Thread(() -> {
                try {
                    circus.enterTiger(id);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

        // перемешиваем потоки для случайного порядка запуска
        java.util.Collections.shuffle(java.util.Arrays.asList(threads));

        // запускаем потоки
        for (Thread thread : threads) {
            thread.start();
        }

        // завершение потоков
        for (Thread thread : threads) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
