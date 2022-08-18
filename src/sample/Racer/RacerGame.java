package sample.Racer;

import sample.Racer.road.*;
import sample.Engine.*;
import sample.StartMe;

import java.io.IOException;


public class RacerGame extends Engine {
    public static final int WIDTH = 64;//ширина игрового поля
    public static final int HEIGHT = 64;//высота игрового поля
    public static final int CENTER_X = WIDTH / 2;//метка для разделительное полосы
    public static final int ROADSIDE_WIDTH = 14;//размер обочины
    private RoadMarking roadMarking;//разметка дороги
    private PlayerCar player;//машина игрока
    private RoadManager roadManager;//отрисовка и передвижение объектов
    private boolean isGameStopped;//остановлена или игра
    private boolean firstStart = true;
    private FinishLine finishLine;//финишная черта
    private static final int RACE_GOAL_CARS_COUNT = 40;//количество пройденных препятствий до победы
    private ProgressBar progressBar;
    private int score;
    private String annotation = "The goal of the game is to reach the finish line.\n" +
                                "When you press \u2B06 \u2b05 \u27A1 you can\n" +
                                "change direction and speed of the car.\n" +
                                "As faster you reach the line, as more points you earn.\n" +
                                "                Enjoy your time!\n" +
                                "       SPACE - OK, ESC - main menu";

    @Override
    public void initialize() {//инициализация
        showGrid(false);//без разметки
        setScreenSize(WIDTH, HEIGHT);//размер игрового поля
        createGame();//создаем новую игру
    }

    private void createGame() {//метод создает новую игру
        roadMarking = new RoadMarking();//создаем разметку
        player = new PlayerCar();//создаем машину
        roadManager = new RoadManager();//создаем объект создающий препятствия
        finishLine = new FinishLine();//создаем финишную прямую
        progressBar = new ProgressBar(RACE_GOAL_CARS_COUNT);
        score = 3500;
        drawScene();//отрисовываем сцену
        setTurnTimer(40);//изменения каждые 40мс
        isGameStopped = false;//игра не остановлена
        setAnnotation(annotation);
    }

    private void drawScene() {//метод отрисовывает сцену
        drawField();//отрисовка игрового поля
        progressBar.draw(this);//рисуем шкалу заполнения
        finishLine.draw(this);//рисуем финишную прямую
        roadMarking.draw(this);//отрисовываем разметку
        roadManager.draw(this);//рисуем препятствия
        player.draw(this);//рисуем машину
    }

    private void drawField() {//метод отрисовывает игровое поле
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                if (x == CENTER_X) {
                    setCellColor(x, y, Color.WHITE);//отрисовка разделительной полосы
                }
                if (x < ROADSIDE_WIDTH || x >= WIDTH - ROADSIDE_WIDTH) {
                    setCellColor(x, y, Color.GREEN);//отрисовка обочины
                }
                if ((x >= ROADSIDE_WIDTH && x < CENTER_X) || (x > CENTER_X && x < WIDTH - ROADSIDE_WIDTH)) {
                    setCellColor(x, y, Color.GRAY);//отрисовка дороги
                }
            }
        }
    }

    @Override
    public void setCellColor(int x, int y, Color color) {//метод отрисовывает цвета только внутри игрового поля
        if (x >= WIDTH || y >= HEIGHT || x < 0 || y < 0) {
            return;
        } else {
            super.setCellColor(x, y, color);
        }
    }

    private void moveAll() {//метод заставляет двигаться объекты
        roadMarking.move(player.speed);//движется разметка
        player.move();//движется машина
        roadManager.move(player.speed);//передвигаем препятствия
        finishLine.move(player.speed);//финишная прямая движется
        progressBar.move(roadManager.getPassedCarsCount());//изменяетс шкала заполнения
    }

    @Override
    public void onTurn(int step) {//метод отвечает за то, что происходит за каждый момент
        if (firstStart) {
            return;
        }
        if (roadManager.checkCrush(player) == true) {//если столкновение было
            gameOver();//конец игры
            drawScene();//отрисовывем сцену
            return;//больше метод ничего не делает
        }
        if (roadManager.getPassedCarsCount() >= RACE_GOAL_CARS_COUNT) {//если счетчик пройденнхы автомобилей дошел до необход количества
            finishLine.showFinish();//показываем финишную полосу
        }
        if (finishLine.isCrossed(player)) {//если финишная черта пересечена
            win();//победа
            drawScene();//отрисовываем сцену
            return;//ничего не делаем
        }
        ;
        score -= 5;
        setScore(score);
        moveAll();//заставляем все двигаться
        roadManager.generateNewRoadObjects(this);//создаем новые препятствия
        drawScene();//отрисовываем
    }

    @Override
    public void onKeyPress(Key key) {//метод передает инф о нажатой кнопке
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

        if (key == Key.RIGHT) {
            player.setDirection(Direction.RIGHT);
        } else if (key == Key.LEFT) {
            player.setDirection(Direction.LEFT);
        }

        if (key == Key.SPACE && isGameStopped) {//если игра остановлена и нажат пробел
            createGame();//создается новая игра
        }

        if (key == Key.UP) {//если нажата клавиша вверх
            player.speed = 2;//скорость увеливается в 2 раза
        }

        if (firstStart && key == key.SPACE) {
            firstStart = false;
        }
    }

    @Override
    public void onKeyReleased(Key key) {//каждый новый кадр метод будет возвращать, что кнопка не нажата
        if (key == Key.RIGHT && player.getDirection() == Direction.RIGHT) {
            player.setDirection(Direction.NONE);
        } else if (key == Key.LEFT && player.getDirection() == Direction.LEFT) {
            player.setDirection(Direction.NONE);
        }

        if (key == Key.UP) {//если  отпущена клавиша вверх
            player.speed = 1;//то скорость становится 1
        }
    }

    private void gameOver() {//метод проигрыша
        isGameStopped = true;//игра остановлена
        showMessageDialog(Color.WHITE, "YOU LOST", Color.BLACK, 75);
        showNoteDialog(Color.GREY, "SPACE - restart, ESC - main menu", Color.BLACK, 20);
        stopTurnTimer();//таймер остановлен
        player.stop();//машина оставновлена
    }

    private void win() {//метод победы
        isGameStopped = true;//игра остановлена
        showMessageDialog(Color.WHITE, "YOU WIN", Color.BLACK, 75);//вывод сообщения
        showNoteDialog(Color.GREY, "SPACE - restart, ESC - main menu", Color.BLACK, 20);
        stopTurnTimer();//таймер остановлен
    }
}

