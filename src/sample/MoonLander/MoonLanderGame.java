package sample.MoonLander;

import sample.Engine.*;
import sample.StartMe;

import java.io.IOException;

public class MoonLanderGame extends Engine {
    public static final int WIDTH = 64;//высота
    public static final int HEIGHT = 64;//ширина
    private Rocket rocket;//ракета
    private GameObject landscape;//ландшафт
    private boolean isUpPressed;//вверх нажата?
    private boolean isLeftPressed;//влево нажата?
    private boolean isRightPressed;//вправо нажата?
    private GameObject platform;//посадочная площадка
    private boolean isGameStopped;//игра остановлена?
    private boolean firstStart = true;
    private int score;//очки
    private String annotation = "The goal of the game is to land on the plain\n" +
                                "When you press \u2B06 \u2b05 \u27A1 you can\n" +
                                "change direction and speed of the rocket.\n" +
                                "As faster you land, as more points you earn.\n" +
                                "                Enjoy your time!\n" +
                                "       SPACE - OK, ESC - main menu";

    @Override
    public void initialize() {//метод инициализирует игру
        setScreenSize(WIDTH, HEIGHT);//устанавливаем размер игрвого поля
        createGame();//создаем необходимые объекты
        showGrid(false);//показать сетку?
    }

    private void createGame() {//метож создает и отрисовывает игровые объекты
        createGameObjects();//создаем ракету и ландшафт
        drawScene();//отрисовываем объекты
        setTurnTimer(50);//время отображения экрана
        isLeftPressed = false;//при создании игры кнопки по условию не нажаты
        isRightPressed = false;
        isUpPressed = false;
        isGameStopped = false;//игра не остановлена
        score = 1000;//с самого начала 1000 очков
        setAnnotation(annotation);
    }

    private void drawScene() {//метод отрисовывает сцену
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                setCellColor(x, y, Color.BLACK);//каждая клетка - черная
            }
        }
        rocket.draw(this);//отрисовываем ракету в данной игре
        landscape.draw(this);//отрисовываем ланшафт
    }

    private void createGameObjects() {//метод содержит в себе положение объектов
        rocket = new Rocket(WIDTH / 2, 0);//создаем ракету
        landscape = new GameObject(0, 25, ShapeMatrix.LANDSCAPE);//создаем ландшафт
        platform = new GameObject(23, MoonLanderGame.HEIGHT - 1, ShapeMatrix.PLATFORM);//создаем платформу
    }

    @Override
    public void onTurn(int step) {//метод содержит в себе то, что происходи за промежуток времени
        if (firstStart) {
            return;
        } else {
            rocket.move(isUpPressed, isLeftPressed, isRightPressed);//ракета получает новые координаты
            if (score > 0) {
                score--;
            }
            check();
            setScore(score);
            drawScene();//отрисовываем сцену
        }
    }

    @Override
    public void setCellColor(int x, int y, Color color) {
        if (x < 0 || y < 0 || x > WIDTH - 1 || y > HEIGHT - 1) {//если ракета за пределами игрового поля
            return;//ничего не отрисовываем
        } else {
            super.setCellColor(x, y, color);
        }
    }

    @Override
    public void onKeyPress(Key key) {//метод определяет нажатие клавиш
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

        if (key == Key.UP) {//если нажата вверх
            isUpPressed = true;
        } else if (key == Key.LEFT) {//если нажата влево
            isLeftPressed = true;
            isRightPressed = false;
        } else if (key == Key.RIGHT) {//если нажата вправо
            isRightPressed = true;
            isLeftPressed = false;
        } else if (isGameStopped == true && key == Key.SPACE) {
            createGame();
        } else if (firstStart && key == key.SPACE) {
            firstStart = false;
        } else {//если нажато что-то другое или ничего
            return;
        }
    }

    @Override
    public void onKeyReleased(Key key) {//метод определяет что произойдет при отпускании клавиши
        if (key == Key.UP) {
            isUpPressed = false;
        } else if (key == Key.LEFT) {
            isLeftPressed = false;
        } else if (key == Key.RIGHT) {
            isRightPressed = false;
        } else {
            return;
        }
    }

    private void check() {//метод проверяет было ли столкновение
        if (rocket.isCollision(platform) && rocket.isStopped()) {
            win();//если было соприкосновение с платформой и ракета остановилась то победа
        } else if (rocket.isCollision(landscape)) {//если соприкосновение с ландшафтом
            gameOver();//проигрыш
        }
    }

    private void win() {//метод определяет поведение при победе
        rocket.land();//ракета приземлилась
        isGameStopped = true;//игра остановлена
        showMessageDialog(Color.WHITE, "YOU WIN", Color.BLACK, 75);//показываем сообщение о победе
        showNoteDialog(Color.GREY, "SPACE - restart, ESC - main menu", Color.BLACK, 20);
        stopTurnTimer();//остановить таймер хода
    }

    private void gameOver() {//метод завершает игру
        rocket.crash();//замена текстуры на разбившуюся ракету
        score = 0;
        isGameStopped = true;//игра остановлена
        showMessageDialog(Color.WHITE, "YOU LOST", Color.BLACK, 75);//вывод сообщения
        showNoteDialog(Color.GREY, "SPACE - restart, ESC - main menu", Color.BLACK, 20);
        stopTurnTimer();//таймер остановлен
    }

}
