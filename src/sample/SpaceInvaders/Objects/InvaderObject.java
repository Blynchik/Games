package sample.SpaceInvaders.Objects;

import sample.SpaceInvaders.Direction;
import sample.Engine.*;

public class InvaderObject {
    public double x;//координаты объекта
    public double y;
    public int[][] matrix;//матрица объекта
    public int width;//ширина матрицы
    public int height;//высота матрицы

    public InvaderObject(double x, double y){
        this.x = x;
        this.y = y;
    }

    public void setMatrix(int[][] matrix) {
        this.matrix = matrix;
        width = matrix[0].length;
        height = matrix.length;
    }

    public void draw(Engine game) {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                int colorIndex = matrix[j][i];
                game.setCellValueEx((int) x + i, (int) y + j, Color.values()[colorIndex], "");
            }
        }
    }

    public boolean isCollision(InvaderObject gameObject) {
        for (int gameObjectX = 0; gameObjectX < gameObject.width; gameObjectX++) {
            for (int gameObjectY = 0; gameObjectY < gameObject.height; gameObjectY++) {
                if (gameObject.matrix[gameObjectY][gameObjectX] > 0) {
                    if (isCollision(gameObjectX + gameObject.x, gameObjectY + gameObject.y)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean isCollision(double x, double y) {
        for (int matrixX = 0; matrixX < width; matrixX++) {
            for (int matrixY = 0; matrixY < height; matrixY++) {
                if (matrix[matrixY][matrixX] > 0
                        && matrixX + (int) this.x == (int) x
                        && matrixY + (int) this.y == (int) y) {
                    return true;
                }
            }
        }
        return false;
    }
}
