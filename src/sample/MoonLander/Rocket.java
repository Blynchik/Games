package sample.MoonLander;

import java.util.ArrayList;
import java.util.List;
import sample.Engine.*;

public class Rocket extends GameObject {
    private double speedY = 0;//скорость падения по оси у
    private double speedX = 0;//скорость по оси x
    private double boost = 0.05;//ускорение падения
    private double slowdown = boost / 10;//замедление поворота по бокам
    private RocketFire downFire;//огонь вниз
    private RocketFire leftFire;//огонь влево
    private RocketFire rightFire;//огонь вправо

    public Rocket(double x, double y) {
        super(x, y, ShapeMatrix.ROCKET);
        List<int[][]> down = new ArrayList<>();//список массив огня
        down.add(ShapeMatrix.FIRE_DOWN_1);
        down.add(ShapeMatrix.FIRE_DOWN_2);
        down.add(ShapeMatrix.FIRE_DOWN_3);
        downFire = new RocketFire(down);
        List<int[][]>left = new ArrayList<>();
        left.add(ShapeMatrix.FIRE_SIDE_1);
        left.add(ShapeMatrix.FIRE_SIDE_2);
        leftFire = new RocketFire(left);
        List<int[][]> right = new ArrayList<>();
        right.add(ShapeMatrix.FIRE_SIDE_1);
        right.add(ShapeMatrix.FIRE_SIDE_2);
        rightFire = new RocketFire(right);
    }//конструктор ракеты

    public void move(boolean isUpPressed, boolean isLeftPressed, boolean isRightPressed) {//метод отвечает за координаты отрисовки ракеты
        if (isUpPressed) {//если нажата вверх
            speedY -= boost;//скорость падения уменьшается на ускорение
        } else {//если не нажата
            speedY += boost;//скорость падения увеличивается на ускорение
        }
        y += speedY;//положение ракеты изменяется в зависимости от скорости

        if (isLeftPressed) {//если нажата влево
            speedX -= boost;//скорость уменьшается растет
        } else if (isRightPressed) {//если нажата вправо
            speedX += boost;//скорость увеличивается
        } else if (speedX > slowdown) {//если скорость вправо больше замедления вправо
            speedX -= slowdown;//скорость вправо уменьшается
        } else if (speedX < -slowdown) {//если скорость влево меньше скорости замедления влево
            speedX += slowdown;//скорость влево уменьшается
        } else {//если скорость по оси х такая же как замедление
            speedX = 0;//скорость по оси х = 0
        }
        x += speedX;//положение ракеты по оси х в зависимости от скорости
        checkBorders();//проверяем границы
        switchFire(isUpPressed, isLeftPressed, isRightPressed);//рисуем огонь
    }

    private void checkBorders() {//метод проверяет, не вышла ли за границы экрана ракета
        if (x < 0) {//если положение ракеты по оси х меньше нуля
            x = 0;//останавливаемся
            speedX = 0;
        }
        if (x + width > MoonLanderGame.WIDTH) {//если положение маасива ракеты больше ширины экрана
            x = MoonLanderGame.WIDTH - width;//останавливается
            speedX = 0;
        }
        if (y < 0) {//если положение ракеты по оси y меньше нуля
            y = 0;//останавливаемся
            speedY = 0;
        }
    }

    public boolean isStopped() {//метод проверяет находится ли ракета в неподвижнос состоянии относительно оси у
        boolean isStopped = speedY < 10 * boost ? true : false;//если скорость падения меньше 10 ускорений, то да, иначе нет
        return isStopped;
    }

    public boolean isCollision(GameObject object){//метод проверяет было ли пересечение с объектом
        int transparent = Color.NONE.ordinal();//в списке цветов 0

        for (int matrixX = 0; matrixX < width; matrixX++) {
            for (int matrixY = 0; matrixY < height; matrixY++) {
                int objectX = matrixX + (int) x - (int) object.x;//сравниваем каждую точку ракеты с числом в массиве
                int objectY = matrixY + (int) y - (int) object.y;

                if (objectX < 0 || objectX >= object.width || objectY < 0 || objectY >= object.height) {
                    continue;
                }

                if (matrix[matrixY][matrixX] != transparent && object.matrix[objectY][objectX] != transparent) {
                    return true;
                    //если число каждой точки ракеты не 0 и число каждой точки объекта не 0
                    //то было столкновение
                }
            }
        }
        return false;
    }

    public void land(){//метод будет размещать ракету чуть выше, что бы не наезжать при посадке на другие текстуры
        y-=1;
    }

    public void crash(){//метод заменяет текстуру ракеты на разбившуюся ракету
        matrix = ShapeMatrix.ROCKET_CRASH;
    }

    private void switchFire(boolean isUpPressed, boolean isLeftPressed, boolean isRightPressed){//переключает огонь
        if(isUpPressed == true){//если нажата вверх
            downFire.x = x+width/2;//огонь отрисуется массив ракеты от края + половина ширины(центр ракеты)
            downFire.y = y+height;//нижняя точка ракеты
            downFire.show();//отрисовываем
        } else {//если не нажата
            downFire.hide();//ничего
        }
        if(isLeftPressed){
            leftFire.x = x+width;
            leftFire.y = y+height;
            leftFire.show();
        } else {
            leftFire.hide();
        }
        if(isRightPressed == true){
            rightFire.x = x-ShapeMatrix.FIRE_SIDE_1[0].length;
            rightFire.y = y+height;
            rightFire.show();
        } else {
            rightFire.hide();
        }
    }

    @Override
    public void draw(Engine game) {//метод отрисует ракету и огонь
        super.draw(game);
        downFire.draw(game);
        leftFire.draw(game);
        rightFire.draw(game);
    }
}
