package sample.Snake;

import sample.Engine.*;

public class Apple extends GameObject {

    private static final String APPLE_SIGN = "\uD83C\uDF4E";//значок яблока в UTF 16
    public boolean isAlive = true;
    Apple(int x, int y) {//конструктор вызывает родительский
        super(x, y);
    }

    public void draw(Engine game) {//метод отрисовывает яблоко на игровом поле
        game.setCellValueEx(x, y, Color.NONE, APPLE_SIGN, Color.RED, 75);//вызывается метод для прорисовки
    }
}
