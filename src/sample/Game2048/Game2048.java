package sample.Game2048;

import sample.Engine.*;
import sample.StartMe;

import java.io.IOException;

public class Game2048 extends Engine {
    private static final int SIDE = 4;//задаем размер игрового поля
    private int[][] gameField = new int[SIDE][SIDE];//массив будет хранить состояние клетки
    private boolean isGameStopped = false;//игра остановлена?
    private int score;//очки
    private String annotation = "The goal of the game is to score 2048 points\n" +
                                "in one of the cells.\n" +
                                "When you press \u2B06 \u2b07 \u2b05 \u27A1 numbers are\n" +
                                "shifting to the edge of the field.\n" +
                                "The same numbers will summed up.\n" +
                                "Every turn random 2 or 4 will appear on empty\n" +
                                "cells. Enjoy your time!\n" +
                                "       SPACE - OK, ESC - main menu";

    public void initialize() {//инициализация
        setScreenSize(SIDE, SIDE);//создаем игровое поле
        createGame();//создание объектов игры
        setAnnotation(annotation);
        drawScene();//отрисовываем сцену
    }

    private void createGame() {//создание объектов игры
        gameField = new int[SIDE][SIDE];//создаем массив
        createNewNumber();//создаем ячейку с 2 или 4
        createNewNumber();//создаем ячейку с 2 или 4
    }

    private void drawScene() {//отрисовывает сцену
        for (int y = 0; y < SIDE; y++) {
            for (int x = 0; x < SIDE; x++) {
                setCellColoredNumber(x, y, gameField[y][x]);//каждую клетку поля закрашиваем в цвет в зависимости от значения
            }
        }
    }

    private void createNewNumber() {
        if (getMaxTileValue() == 2048) {//если максимальное число 2048
            win();//победа
        }

        int x = getRandomNumber(SIDE);
        int y = getRandomNumber(SIDE);
        int value = getRandomNumber(10);
        if (gameField[y][x] == 0) {//если ячейка пустая
            if (value == 9) {//и случайное число 9
                gameField[y][x] = 4;//то в ячейке будет число 4
            } else {
                gameField[y][x] = 2;//иначе 2 (с 90% вероятностью)
            }
        } else {//если ячейка не пустая
            createNewNumber();//начинаем заново
        }
    }

    private Color getColorByValue(int value) {//метод присваивает цвет в зависимости от значения
        Color color = null;
        switch (value) {
            case 0:
                color = Color.WHITESMOKE;
                break;
            case 2:
                color = Color.YELLOW;
                break;
            case 4:
                color = Color.ORANGE;
                break;
            case 8:
                color = Color.GREENYELLOW;
                break;
            case 16:
                color = Color.ANTIQUEWHITE;
                break;
            case 32:
                color = Color.AQUA;
                break;
            case 64:
                color = Color.AQUAMARINE;
                break;
            case 128:
                color = Color.AZURE;
                break;
            case 256:
                color = Color.BEIGE;
                break;
            case 512:
                color = Color.BISQUE;
                break;
            case 1024:
                color = Color.BLANCHEDALMOND;
                break;
            case 2048:
                color = Color.BLUEVIOLET;
        }
        return color;
    }

    private void setCellColoredNumber(int x, int y, int value) {//метод определенной клетке отрисовывает цвет и значение
        Color color = getColorByValue(value);
        if (value != 0) {//если значение в ячейке не 0, то
            setCellValueEx(x, y, color, "" + value);//пишем это значение в ячейке
        } else {//если 0
            setCellValueEx(x, y, color, "");//то не пишем
        }
    }

    private boolean compressRow(int[] row) {//метод подтверждает, что в ряду были передвижения объектов
        boolean moved = false;//было ли передвижение объектов? - нет
        int noneNull = 0;//количество не 0 ячеек в ряду
        for (int i = 0; i < row.length; i++) {
            if (row[i] != 0) {//если ячейка не 0
                if (i != noneNull) {//если индекс не равен кол-ву ненулевых ячеек(если хоть одна ячейка не пустая, будет сдвиг)
                    row[noneNull] = row[i];//сдвигаем влево
                    row[i] = 0;//нули сдвигаем вправо
                    moved = true;//был сдвиг
                }
                noneNull++;//прибавляем, что непустых ячеек +1
            }
        }
        return moved;
    }

    private boolean mergeRow(int[] row) {//метод сообщает было ли слияние в ряду
        boolean merged = false;//изначально слияния не было
        for (int i = 0; i < row.length; i++) {//для каждоого в ряду
            if (row[i] != 0) {//если число не 0
                if (i != row.length - 1) {//если индекс не выходит за рамки
                    if (row[i] == row[i + 1]) {//если два соседних числа одинаковы
                        row[i] += row[i + 1];//они складываются
                        row[i + 1] = 0;//правое число становится 0
                        merged = true;//было слияние
                        score = score + row[i];//складываем очки
                        setScore(score);
                    }
                }
            }
        }
        return merged;
    }

    @Override
    public void onKeyPress(Key key) {//метод реагирует на нажатие клавиши
        if (isGameStopped) {//если игра остановлена
            if (key == Key.SPACE) {//если нажата клавиша пробел
                isGameStopped = false;//продолжаем игру
                score = 0; //очки обнуляются
                setScore(score);
                createGame();//создаем новую игру
                drawScene();//отрисовываем сцену
            } else {//если не нажата пробел
                return;//игнорируем
            }
        }

        if (key == Key.ESCAPE) {
            try {
                Runtime.getRuntime().exec(StartMe.ENGINE_COMP);
                Runtime.getRuntime().exec(StartMe.START_ME_COMP);
                Runtime.getRuntime().exec(StartMe.START_ME_START);
                System.exit(0);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        if (canUserMove() == false) {//если игрок не может двигаться
            gameOver();//конец игры
            return;
        } else if (key == Key.LEFT) {//если нажато влево
            moveLeft();//передвигаем все влево и т.д.
        } else if (key == Key.RIGHT) {
            moveRight();
        } else if (key == Key.UP) {
            moveUp();
        } else if (key == Key.DOWN) {
            moveDown();
        } else {
            return;
        }
        drawScene();
    }

    private void moveLeft() {//метод сдвигает не пустые ячейки в левую часть игрового поля
        boolean needNewNum = false;//нужно ли добавить новое число?, т.к. при сдвиге и слиянии по правилам появляется новое число
        for (int i = 0; i < SIDE; i++) {// для всех ячеек в ряду
            if (compressRow(gameField[i])) {//если сдиг влево удался
                needNewNum = true;//нужна еще одна цифра
            }
            if (mergeRow(gameField[i])) {//если было слияние
                needNewNum = true;//нужна еще одна цифра
            }
            if (compressRow(gameField[i])) {//если было слияние, то нужно еще раз сдвинуть влево
                needNewNum = true;//нужна еще одна цифра
            }
        }
        if (needNewNum) {//если цифра нужна
            createNewNumber();//добавляем ее 1 раз
        }
    }

    private void moveRight() {//метод сдвигает ячейки вправо
        rotateClockwise();//2 раза повернем по часовой стрелке
        rotateClockwise();
        moveLeft();//переместим и сольем все ячейки слевой стороны
        rotateClockwise();//повернем еще 2 раза, чтобы они оказались справа
        rotateClockwise();
    }

    private void moveUp() {//метод сдвигает ячейки вверх
        rotateClockwise();
        rotateClockwise();
        rotateClockwise();
        moveLeft();
        rotateClockwise();
    }

    private void moveDown() {//метод сдвигает ячейки вниз
        rotateClockwise();
        moveLeft();
        rotateClockwise();
        rotateClockwise();
        rotateClockwise();
    }

    private void rotateClockwise() {//метод поворачивает матрицу игрового поля на 90 градусов
        int[][] result = new int[SIDE][SIDE];//создаем матрицу, где будут происходить все изменения
        for (int y = 0; y < SIDE; y++) {
            for (int x = 0; x < SIDE; x++) {
                result[y][x] = gameField[SIDE - x - 1][y];//копируем значения из основной матрицы согласно условию
            }
        }
        gameField = result;//новая матрица становится основной
    }

    private int getMaxTileValue() {//метод возвращает самое большое число в массиве
        int max = 0;
        for (int y = 0; y < SIDE; y++) {
            for (int x = 0; x < SIDE; x++) {
                if (gameField[y][x] > max) {
                    max = gameField[y][x];
                }
            }
        }
        return max;
    }

    private void win() {//метод выводит на экран сообщение о победе
        isGameStopped = true;//игра остановлена
        showMessageDialog(Color.WHITE, "YOU WIN", Color.BLACK, 75);//сообщение
        showNoteDialog(Color.GREY, "SPACE - restart, ESC - main menu", Color.BLACK, 20);
    }

    private boolean canUserMove() {//метод проверяет возможность хода
        for (int y = 0; y < SIDE; y++) {
            for (int x = 0; x < SIDE; x++) {
                if (gameField[y][x] == 0) {//если есть пустые ячейки
                    return true;
                } else if ((y < SIDE - 1) && gameField[y][x] == gameField[y + 1][x]) {//если есть клетки, у которых соседи такие же числа
                    return true;
                } else if ((x < SIDE - 1) && gameField[y][x] == gameField[y][x + 1]) {
                    return true;
                }
            }
        }
        return false;
    }

    private void gameOver() {//метод проигрыша
        isGameStopped = true;
        showMessageDialog(Color.WHITE, "YOU LOST", Color.BLACK, 75);
        showNoteDialog(Color.GREY, "SPACE - restart, ESC - main menu", Color.BLACK, 20);
    }
}
