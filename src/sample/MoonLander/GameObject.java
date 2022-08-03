package sample.MoonLander;

import sample.Engine.*;

public class GameObject {
    public double x;//коодината х
    public double y;//координата у
    public int[][] matrix;//положение каждой точки ракеты на игровом поле
    public int width;//ширина массива объекта
    public int height;//высота массива объекта

    public GameObject(double x, double y, int[][] matrix){//конструктор
        this.x = x;
        this.y = y;
        this.matrix = matrix;
        this.width = matrix[0].length;
        this.height = matrix.length;
    }

    public void draw(Engine game){//метод отрисовывает ракету
        if(matrix == null){//если массив ракеты пустой
            return;//ничего не делаем
        }

        for(int i = 0; i< width; i++){//для каждой клетки массива ракеты
            for(int j = 0; j< height;j++){
                int colorIndex = matrix[j][i];//получаем значение, хранимое в массиве ракеты
                game.setCellColor((int)x+i, (int)y+j, Color.values()[colorIndex]);//красим в цвет соответсвующий индексу из списка цветов
            }//т.к. отсчет элементов массива начинается с левого-верхнего угла, то к координатам клетки игровогор поля нужно прибавить координату клетки из массива самой ракеты
        }
    }
}
