package sample.Racer;

import sample.Engine.*;

public class GameObject {
    public int x;//координата массива
    public int y;//координата массива
    public int width;//ширина массива
    public int height;//высота массива
    public int[][] matrix;//массив

    public GameObject(int x, int y, int[][] matrix){//конструктор
        height = matrix.length;
        width = matrix[0].length;
        this.matrix = matrix;
        this.x = x;
        this.y = y;
    }

    public GameObject(int x, int y){//конструктор для статичных объекто
        this.x = x;
        this.y = y;
    }

    public void draw(Engine game){//метод создает объект
        for(int y = 0; y< matrix.length;y++){//покрасятся тоолько заполненные клетки массива
            for(int x = 0; x<matrix[0].length; x++){
                game.setCellColor(this.x+x,this.y+y,Color.values()[matrix[y][x]]);
            }
        }
    }

    private boolean isCollision(int x, int y) {//метод возвращает было ли пересечение с другим объектом
        for (int matrixX = 0; matrixX < width; matrixX++) {
            for (int matrixY = 0; matrixY < height; matrixY++) {
                if (matrix[matrixY][matrixX] != 0 && matrixX + this.x == x && matrixY + this.y == y) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isCollision(GameObject gameObject) {//метод возвращает было ли пересечение с другим объектом
        if (!isCollisionPossible(gameObject)) {
            return false;
        }

        for (int carX = 0; carX < gameObject.width; carX++) {
            for (int carY = 0; carY < gameObject.height; carY++) {
                if (gameObject.matrix[carY][carX] != 0) {
                    if (isCollision(carX + gameObject.x, carY + gameObject.y)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean isCollisionPossible(GameObject otherGameObject) {//возможно ли столкновение
        if (x > otherGameObject.x + otherGameObject.width || x + width < otherGameObject.x) {
            return false;
        }

        if (y > otherGameObject.y + otherGameObject.height || y + height < otherGameObject.y) {
            return false;
        }
        return true;
    }
}
