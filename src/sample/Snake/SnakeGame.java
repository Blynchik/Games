package sample.Snake;

import sample.Engine.*;
import sample.StartMe;

import java.io.IOException;

public class SnakeGame extends Engine {

    private Apple apple;//это яблоко
    private SnakeObject snake;//это змейка
    private int turnDelay;//время хода
    private boolean isGameStopped;//игра остановлена
    static boolean firstStart = true;
    private static final int GOAL = 28;//яблок до победы
    private int score;//количество очков
    private String annotation = "The goal of the game is to eat apples.\n" +
                                "When you press \u2B06 \u2b07 \u2b05 \u27A1 you can\n" +
                                "change snake move direction.\n" +
                                "Each apple increase snake's speed.\n" +
                                "                Enjoy your time!\n" +
                                "       SPACE - OK, ESC - main menu";

    public static final int WIDTH = 15;//ширина игрового поля
    public static final int HEIGHT = 15;//высота игрового поля

    public void initialize() {//инициализируем игру
        setScreenSize(WIDTH, HEIGHT);//устанавливаем размеры игрового поля
        createGame();//создаем все необходимое для создания игры
    }

    private void createGame() {//создаем все необходмое для создания игры
        score = 0;//при создании очков 0
        turnDelay = 300;//время хода 300 мс
        setTurnTimer(turnDelay);//устанавливаем это время
        snake = new SnakeObject(WIDTH / 2, HEIGHT / 2);//задаем координаты змейке
        createNewApple();//создаем яблоко по координатам
        isGameStopped = false;//игра не остановлена
        setScore(score);//устанавливаем количество очков
        setAnnotation(annotation);
        drawScene();//отрисовываем сцену
    }

    private void drawScene() {//метод отрисовывает все происходящее на экране за ход
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                setCellValueEx(x, y, Color.LIGHTYELLOW, "");//заполняем каждую клетку фона цветом и необходимым знаком
            }
        }
        snake.draw(this);//рисуем змейку в игру
        apple.draw(this);//рисуем яблоко в сцене
    }

    public void onTurn(int step) {//метод определяет, что происходит за ход
        if (firstStart == true) {
            return;
        } else {
            snake.move(apple);//змейка передвигается
            if (apple.isAlive == false) {//если яблоко съели
                createNewApple();//создается новое яблоко
                score += 5;//количество очков +5
                setScore(score);//устанавливаем количество очков
                turnDelay -= 10;//ускоряем игру, уменьшая задержу
                setTurnTimer(turnDelay);//устанавливаем задержку
            }
            if (snake.isAlive == false) {//если змейка мертва
                gameOver();//завершаем игру
            }
            if (snake.getLength() > GOAL) {//если длина змейки больше макисмального количества очков
                win();//победа
            }
            drawScene();//отрисовываем сцену
        }
    }

    public void onKeyPress(Key key) {//метод определяет движение змейки в зависимости от нажатой клавиши
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
        if(key == Key.SPACE && isGameStopped == true){//если нажат пробел и игра остановлена
            createGame();//начинаем новую игру
        }

        if (key == Key.DOWN) {//если нажата клавиша вниз
            snake.setDirection(Direction.DOWN);//змейка меняет направление на вниз
        } else if (key == Key.UP) {//и т.д.
            snake.setDirection(Direction.UP);
        } else if (key == Key.RIGHT) {
            snake.setDirection(Direction.RIGHT);
        } else if(key == Key.LEFT){
            snake.setDirection(Direction.LEFT);
        }

        if (firstStart && key == key.SPACE) {
            firstStart = false;
        }
    }

    private void createNewApple() {//метод создает яблоко
        Apple newApple;//создаем объект яблоко
        do {
            int x = getRandomNumber(WIDTH);//координаты
            int y = getRandomNumber(HEIGHT);//...
            newApple = new Apple(x, y);//новое яблоко по координатам
        }while(snake.checkCollision(newApple));//выполняем пункты выше до тех пор пока яблоко непоявится на координатах вне тела змейки
        apple = newApple;//присваиваем объект игровому яблоку
    }

    private void gameOver() {//метод завершает игру
        stopTurnTimer();//останавливаем таймер
        isGameStopped = true;//игра остановлена
        showMessageDialog(Color.WHITE, "GAME OVER", Color.BLACK, 75);//выводим сообщение
        showNoteDialog(Color.GREY, "SPACE - restart, ESC - main menu", Color.BLACK, 20);
    }

    private void win() {//метод завершает игру при победе
        stopTurnTimer();//останавливаем таймер
        isGameStopped = true;//игра остановлена
        showMessageDialog(Color.WHITE, "YOU WIN", Color.BLACK, 75);//выводим сообщение
        showNoteDialog(Color.GREY, "SPACE - restart, ESC - main menu", Color.BLACK, 20);
    }
}
