package sample.Sapper;


import sample.Engine.*;

import java.util.ArrayList;
import java.util.List;

public class Sapper extends Engine {

    private static int SIDE = 9; // размер игрового поля SIDExSIDE

    private int countClosedTiles = SIDE * SIDE;//количество закрытых ячеек
    private int countMinesOnField;//сколько мин на игрвом поле
    private int countFlags;//количество доступных флагов
    private int score;//количество очков

    private GameObject[][] gameField = new GameObject[SIDE][SIDE];// игровое поле это двумерный массив, состоящий из объектов GameObject

    private boolean isGameStopped;//игра остановлена?

    private static final String MINE = "\uD83D\uDCA3";//здесь храним кодировку на бомбу
    private static final String FLAG = "\uD83D\uDEA9";//здесь храним кодировку на флаг

    private void restart() {//метод рестарта
        isGameStopped = false;//игра не приостановлена
        countClosedTiles = SIDE * SIDE;//устанавливаем снова количество закрытых ячеек
        score = 0;//очки = 0
        setScore(score);//устанавливаем очки
        countMinesOnField = 0;//мин снова 0
        createGame();//создаем игрвовую площадку снова
    }

    private void win() {//метод победы
        isGameStopped = true;//игра остановлена
        showMessageDialog(Color.WHITE, "Вы победили", Color.BLACK, 50);//показываем диалоговое окно
    }

    private void gameOver() {//метод завершает игру
        isGameStopped = true;//игра остановлена
        showMessageDialog(Color.WHITE, "GAME OVER", Color.BLACK, 50);//показываем диалоговое окно
    }

    private void markTile(int x, int y) {//метод устанавливает флаги и сопутствующие действия
        GameObject gameObject = gameField[y][x];//объект, который будем использовать - клетка с такими координатами
        if (gameObject.isOpen || isGameStopped || countFlags == 0 && !gameObject.isFlag) {//если клетка открыта или игра остановлена или (количество свободных флагов 0 и клетка уже почена флагом)
            return;//ничего не делаем
        }
        if (!gameObject.isFlag) {//если клетка не помечена флагом
            gameObject.isFlag = true;//помечаем ее флагом
            countFlags--;//количетсво оставшихся мин -1
            setCellValue(gameObject.x, gameObject.y, FLAG);//рисуем в клетке флаг
            setCellColor(gameObject.x, gameObject.y, Color.YELLOW);//клетка становится желтой
        } else {//если клетка помечена флагом
            gameObject.isFlag = false;//убираем флаг
            countFlags++;//количетсво свободных флагов +1
            setCellValue(gameObject.x, gameObject.y, "");//убираем флаг с отрисовки
            setCellColor(gameObject.x, gameObject.y, Color.GREY);//красим клетку в оранжевый
        }
    }

    private void openTile(int x, int y) {//метод открывает ячейку с кординатами и все сопутствующие действия
        GameObject gameObject = gameField[y][x];//объект - это ячейка с координатами
        if (gameObject.isOpen || gameObject.isFlag || isGameStopped) {//если клетка уже открыта, или помечен флагом, или игра приоставновлена
            return;//ничего не делаем
        }
        gameObject.isOpen = true;//отмечаем в клетке, что она открыта
        countClosedTiles--;//если клетка открыта, то счетчик закрытых клеток -1
        setCellColor(x, y, Color.GREEN);//красим клетку в зеленый
        if (gameObject.isMine) {//если она заминирована
            setCellValueEx(gameObject.x, gameObject.y, Color.RED, MINE);//то рисуем бомбу по координатам и красим клетку в красный
            gameOver();//вызываем метод звершающий игру
            setCellValue(gameObject.x, gameObject.y, MINE);
        } else if (gameObject.countMineNeighbors == 0) { //если не заминирована и количество заминированных соседей 0
            setCellValue(gameObject.x, gameObject.y, "");//в этой клетке не пишется ничего
            List<GameObject> neighbors = getNeighbors(gameObject);//получаем всех соседей клетки
            for (GameObject neighbor : neighbors) {//для каждого соседа
                if (!neighbor.isOpen) {//если он не открыт
                    openTile(neighbor.x, neighbor.y);//открываем ячейку(снова наверх списка и рекурсия до тех пор пока кто-то из соседей не получит заминированного соседа)
                }
            }
        } else {//если ячейка не заминировна и количество заминированных соседей >0
            setCellNumber(x, y, gameObject.countMineNeighbors);//по координатам пишем количество заминированных соседей
        }
        score = score + 5;//если ячейка открыта и незаминирована + 5 очков
        setScore(score);//устанавливаем количество очков
        if (countClosedTiles == countMinesOnField) {//если число закрытых клеток равно числу мин
            win();//запускаем метод победы
        }
    }

    private List<GameObject> getNeighbors(GameObject gameObject) {//метод определяет соседей клетки
        List<GameObject> result = new ArrayList<>();//создаем список
        for (int y = gameObject.y - 1; y <= gameObject.y + 1; y++) {//определяем координаты сосдей по оси y, всегда на +-1 от текущей клетки
            for (int x = gameObject.x - 1; x <= gameObject.x + 1; x++) {//определяем координаты сосдей по оси x, всегда на +-1 от текущей клетки
                if (y < 0 || y >= SIDE) {//если ось y выходит за рамки игрового поля
                    continue;//то проскакиваем
                }
                if (x < 0 || x >= SIDE) {//если ось x выходит за рамки игрового поля
                    continue;//то проскакиваем
                }
                if (gameField[y][x] == gameObject) {//если ячейка с координатами сам объект
                    continue;//то проскакиваем
                }
                result.add(gameField[y][x]);//в остальных случаях добавляем в список соседей
            }
        }
        return result;//возвращаем список соседей
    }

    private void countMineNeighbors() {//метод считает количество заминированных соседей у клетки
        for (int y = 0; y < SIDE; y++) {
            for (int x = 0; x < SIDE; x++) {
                GameObject gameObject = gameField[y][x];//объект - это ячейка на игровом поле
                if (!gameObject.isMine) {//если объект не заминирован
                    List<GameObject> neighbors = getNeighbors(gameObject);//получаем всех соседей клетки
                    for (GameObject neighbor : neighbors) {//для всех соседей
                        if (neighbor.isMine) {//если сосед заминирован
                            gameObject.countMineNeighbors++;//то количество заминированных соседей у незаминированной ячейки + 1
                        }
                    }
                }
            }
        }
    }

    private void createGame() {//создаем игру
        for (int y = 0; y < SIDE; y++) {
            for (int x = 0; x < SIDE; x++) {
                boolean isMine = getRandomNumber(10) < 1;//получаем целое число от 0 до 10(не включая),если число 0, то ячейка заминирована
                if (isMine) {//если ячейка заминирована
                    countMinesOnField++;//заминированных ячеек + 1
                }
                gameField[y][x] = new GameObject(x, y, isMine);//получаем координаты каждого объекта и заминирован ли он
                setCellColor(x, y, Color.GREY);//закрышиваем каждый объект по координатам
                setCellValue(x, y, "");
            }
        }
        countMineNeighbors();//считаем заминированных соседей
        countFlags = countMinesOnField;//количество доступных флагов равно количеству оставшихся мин
    }

    public void initialize() { // инициализируем игру...
        setScreenSize(SIDE, SIDE); //задаем игровое поле
        createGame();//заполняем игровое поле объектами
        isGameStopped = false;
    }

    public void onMouseLeftClick(int x, int y) {
        if (isGameStopped) {//если игра остановлена
            restart();//запускаем метод рестарт, если нажата ЛКМ
            return;
        }
        openTile(x, y);
    }//если происходит клик ЛКМ, то открываем клетку по координатам

    public void onMouseRightClick(int x, int y) {//если клик ПКМ, то вызываем этот метод(отрисовка флага) по координатам
        markTile(x, y);
    }//по ПКМ устанавливаем флаги по координатам
}

