import java.util.concurrent.Semaphore;
import java.util.Scanner;

public class laba18 {
    // Семафоры для синхронизации
    private static Semaphore sem1 = new Semaphore(1); // Семафор для первого процесса
    private static Semaphore sem2 = new Semaphore(0); // Семафор для второго процесса

    private static char sharedChar; // Общий символ

    public static void main(String[] args) {
        // Поток 1: Ожидает ввода и передает символ
        Thread process1 = new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            try {
                while (true) {
                    sem1.acquire(); // Блокируем, если второй процесс еще не вывел символ
                    System.out.print("Введите символ: ");
                    sharedChar = scanner.next().charAt(0);
                    sem2.release(); // Разрешаем второму процессу работать
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        // Поток 2: Получает символ и выводит его на экран
        Thread process2 = new Thread(() -> {
            try {
                while (true) {
                    sem2.acquire(); // Ожидаем, пока первый процесс передаст символ
                    System.out.println("Выведенный символ: " + sharedChar);
                    sem1.release(); // Разрешаем первому процессу продолжить
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        // Запуск потоков
        process1.start();
        process2.start();
    }
}
