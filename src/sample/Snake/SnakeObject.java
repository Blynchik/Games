package sample.Snake;

import sample.Engine.*;

import java.util.ArrayList;
import java.util.List;

public class SnakeObject {//создаем саму змейку из игровых объектоа
    private static final String HEAD_SIGN = "\uD83D\uDC7E";//для отрисовки головы в формате UTF 16
    private static final String BODY_SIGN = "\u26AB";//для отрисовки туловища в формате UTF 16
    private List<GameObject> snakeParts = new ArrayList<GameObject>();//в списке хранятся все элементы змейки, т.к. она растет
    private Direction direction = Direction.LEFT;//направление для змейки
    public boolean isAlive = true;//змейка жива

    public SnakeObject(int x, int y) {//в конструкторе сразу добавляем три элемента
        snakeParts.add(new GameObject(x, y));
        snakeParts.add(new GameObject(x + 1, y));
        snakeParts.add(new GameObject(x + 2, y));
    }

    public void setDirection(Direction direction) {//метод устанавливает движение
        if (this.direction == Direction.LEFT && snakeParts.get(0).x == snakeParts.get(1).x) {
            return;//если текущее движение влево и оно совпадает  с введенным, и координаты Х первых двух частей змейки одинаковы
        }//ничего не делаем
        if (this.direction == Direction.RIGHT && snakeParts.get(0).x == snakeParts.get(1).x) {
            return;
        }
        if (this.direction == Direction.UP && snakeParts.get(0).y == snakeParts.get(1).y) {
            return;
        }
        if (this.direction == Direction.DOWN && snakeParts.get(0).y == snakeParts.get(1).y) {
            return;
        }

        if (this.direction == Direction.DOWN && direction == Direction.UP//если направление движения совпадает с введенным движением
                || this.direction == Direction.UP && direction == Direction.DOWN
                || this.direction == Direction.LEFT && direction == Direction.RIGHT
                || this.direction == Direction.RIGHT && direction == Direction.LEFT) {
            return;//то ничего не делаем
        }
        this.direction = direction;// если не совпадает, то меняем напрвление на введенное
    }

    public void draw(Engine game) {//метод отрисовывает змейку
        for (int i = 0; i < snakeParts.size(); i++) {
            GameObject part = snakeParts.get(i);//часть змейки - это элемент массива
            if (isAlive) {//если змейка жива
                if (i == 0) {//если это первый элемент массива
                    game.setCellValueEx(part.x, part.y, Color.NONE, HEAD_SIGN, Color.GREEN, 75);//то рисуем голову
                } else {//если это не первый элемент массива
                    game.setCellValueEx(part.x, part.y, Color.NONE, BODY_SIGN, Color.GREENYELLOW, 75);//то рисуем туловище
                }
            } else {//если змейка не жива
                if (i == 0) {//если это первый элемент массива
                    game.setCellValueEx(part.x, part.y, Color.NONE, HEAD_SIGN, Color.RED, 75);//то рисуем голову в красный
                } else {//если это не первый элемент массива
                    game.setCellValueEx(part.x, part.y, Color.NONE, BODY_SIGN, Color.RED, 75);//то рисуем туловище в красный
                }
            }
        }
    }

    public GameObject createNewHead() {//метод создает новую голову в зависимости от направления движения змейки
        GameObject head;//объекту присваивается положения в зависимости от положения головы +-1
        if (direction == Direction.LEFT) {
            head = new GameObject(snakeParts.get(0).x - 1, snakeParts.get(0).y);
        } else if (direction == Direction.UP) {
            head = new GameObject(snakeParts.get(0).x, snakeParts.get(0).y - 1);
        } else if (direction == Direction.RIGHT) {
            head = new GameObject(snakeParts.get(0).x + 1, snakeParts.get(0).y);
        } else {
            head = new GameObject(snakeParts.get(0).x, snakeParts.get(0).y + 1);
        }
        return head;
    }

    public void removeTail() {//метод удаляет последнюю клетку в змейке, когда та передвигается
        snakeParts.remove(snakeParts.size() - 1);
    }

    public void move(Apple apple) {//метод отвечает за передвижение змейки
        GameObject head = createNewHead();//создаем голову
        if (head.x >= SnakeGame.WIDTH//если положение головы вне пределов рамок игры
                || head.y >= SnakeGame.HEIGHT
                || head.x < 0
                || head.y < 0) {
            isAlive = false;//змейка мертва
            return;//ничего не делаем
        }

        if (checkCollision(head)) {//если есть столкновение
            isAlive = false;//змейка мертва
            return;//ничего не делаем
        }

        if (head.x == apple.x && head.y == apple.y) {//если положение головы совпадает с положением яблока
            apple.isAlive = false;//яблоко мертво
            snakeParts.add(0, head);//создаем новую голову
        } else {//если не совпадает с положением яблока
            snakeParts.add(0, head);//если положение головы нормальное, то добавляем голову в список элементов змейки
            removeTail();//удаляем хвост
        }
    }

    public boolean checkCollision(GameObject gameObject) {//метод проверяет столкнулась ли змейка с собой
        boolean collision = false;//по умолчанию не столкнулась
        for (GameObject snakePart : snakeParts) {//для каждой части змейки
            if ((gameObject.x == snakePart.x) && (gameObject.y == snakePart.y)) {//если координаты голов совпадают с координатами части змейки
                collision = true;//есть столкновение
            }
        }
        return collision;
    }

    public int getLength() {//метод возвращает длину змейки
        return snakeParts.size();
    }
}
