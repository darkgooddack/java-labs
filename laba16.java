import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.*;

abstract class BaseAI extends Thread {
    protected volatile boolean isActive = true;
    protected Object object;

    public BaseAI(Object object) {
        this.object = object;
    }

    public void run() {
        while (isActive) {
            update();
            try {
                Thread.sleep(1000); // Интервал обновления интеллекта
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public abstract void update();
    public void stopAI() {
        isActive = false;
    }
}

interface IBehaviour {
    void behave(long timeElapsed);
}

class Developer implements IBehaviour {
    private int x, y;
    private int speed = 5;

    public Developer() {
        this.x = (int) (Math.random() * 500);
        this.y = (int) (Math.random() * 500);
    }

    @Override
    public void behave(long timeElapsed) {
        // Двигается случайным образом
        if (timeElapsed % 10 == 0) {
            x += Math.random() > 0.5 ? speed : -speed; // если больше 0.5, то движется назад
            y += Math.random() > 0.5 ? speed : -speed;
        }
    }

    public void render(Graphics g) {
        g.setColor(Color.BLUE);
        g.fillRect(x, y, 10, 10);
    }
}

class Manager implements IBehaviour {
    private int x, y;
    private int radius = 100;
    private int angle = 0;
    private int centerX = 250, centerY = 250;

    public Manager() {
        this.x = centerX;
        this.y = centerY - radius;
    }

    @Override
    public void behave(long timeElapsed) {
        // Двигается по окружности
        angle = (angle + 5) % 360;
        x = centerX + (int) (radius * Math.cos(Math.toRadians(angle)));
        y = centerY + (int) (radius * Math.sin(Math.toRadians(angle)));
    }

    public void render(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect(x, y, 10, 10);
    }
}

class Habitat {
    private List<IBehaviour> entities = new ArrayList<>();
    private int width, height;
    private boolean isSimulating = false;
    private long startTime;

    public Habitat(int width, int height) {
        this.width = width;
        this.height = height;
    }

    public void update(long timeElapsed) {
        for (IBehaviour entity : entities) {
            entity.behave(timeElapsed);
        }
    }

    public void addEntity(IBehaviour entity) {
        entities.add(entity);
    }

    public void removeEntities() {
        entities.clear();
    }

    public List<IBehaviour> getEntities() {
        return entities;
    }

    public void startSimulation() {
        this.isSimulating = true;
        this.startTime = System.currentTimeMillis();
    }

    public void stopSimulation() {
        this.isSimulating = false;
        removeEntities();
    }

    // Добавляем публичный геттер для переменной isSimulating
    public boolean isSimulating() {
        return isSimulating;
    }

    // Возвращаем время симуляции
    public long getSimulationTime() {
        return System.currentTimeMillis() - startTime;
    }
}


public class laba16 {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Simulation");
        SimulationPanel panel = new SimulationPanel();
        frame.add(panel);
        frame.setSize(600, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
