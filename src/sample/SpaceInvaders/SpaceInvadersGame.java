package sample.SpaceInvaders;

import java.util.ArrayList;
import java.util.List;
import sample.Engine.*;
import sample.SpaceInvaders.Objects.*;

public class SpaceInvadersGame extends Engine {
    public static final int WIDTH = 64;//ширина игрового поля
    public static final int HEIGHT = 64;//высота игрового поля
    private int score;//игровые очки
    private List<Star> stars;//здесь содержаться звезды
    private EnemyFleet enemyFleet;//флот
    public static final int COMPLEXITY = 5;//уровень стрельбы флота
    private List<Bullet> enemyBullets;//все пули
    private PlayerShip playerShip;//корабль игрока
    private boolean isGameStopped;//остановлена ли игра
    private int animationsCount;//отсчет анимации
    private List<Bullet> playerBullets;//пули игрока
    private static final int PLAYER_BULLETS_MAX = 1;//максимальное еоличество пуль игрока на экране

    @Override
    public void initialize() {//инициализация игры
        setScreenSize(WIDTH, HEIGHT);//задаем игровое поле
        createGame();//создаем новую игру
    }

    private void createGame() {//создание новой игры
        enemyFleet = new EnemyFleet();//создаем флот
        createStars();//создаем звезды
        enemyBullets = new ArrayList<>();//создаем пули
        playerShip = new PlayerShip();//создаем корабль
        isGameStopped = false;//игра не остановлена
        animationsCount = 0;//начало отсчета анимации
        playerBullets = new ArrayList<Bullet>();//создаем пули игрока
        score = 0;//очки
        drawScene();//отрисовываем
        setTurnTimer(40);//задаем таймер
    }

    private void drawScene() {//отрисовка объектов
        drawField();//отрисовка игрового поля
        for (int i = 0; i < enemyBullets.size(); i++) {
            enemyBullets.get(i).draw(this);//рисуем каждую пулю
        }
        enemyFleet.draw(this);
        playerShip.draw(this);
        for (int i = 0; i < playerBullets.size(); i++) {
            playerBullets.get(i).draw(this);//рисуем каждую пулю игрока
        }
    }

    private void drawField() {//отрисовка игрового поля
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                setCellValueEx(x, y, Color.BLACK, "");//красим все клетки
            }
        }
        for (int i = 0; i < stars.size(); i++) {
            stars.get(i).draw(this);//рисуем звезды
        }
    }

    private void createStars() {//создаем звезды
        stars = new ArrayList<Star>();
        for (int i = 0; i < 8; i++) {
            int x = getRandomNumber(64);
            int y = getRandomNumber(64);
            stars.add(new Star(x, y));
        }
    }

    @Override
    public void onTurn(int step) {//отрисовка того, что происходит в момент времени
        moveSpaceObjects();//передвигаем флот
        Bullet bullet = enemyFleet.fire(this);//флот открывает огонь
        if (bullet != null) {//если выстрел произошел
            enemyBullets.add(bullet);//добавляем пулю в список
        }
        check();//проверяем все элементы, ненужные удаляем
        setScore(score);//устанавливаем количество очков
        drawScene();//отрисовываем сцену
    }

    private void moveSpaceObjects() {//передвигает объекты флота
        enemyFleet.move();//флот движется

        for (int i = 0; i < enemyBullets.size(); i++) {
            enemyBullets.get(i).move();//каждая пуля движется
        }

        playerShip.move();//корабль движется

        for (int i = 0; i < playerBullets.size(); i++) {
            playerBullets.get(i).move();//каждая пуля игрока движется
        }
    }

    private void removeDeadBullets() {//удаялет лишние пули
        for (Bullet bullet : new ArrayList<>(enemyBullets)) {
            if (!bullet.isAlive || bullet.y >= HEIGHT - 1) {
                enemyBullets.remove(bullet);
            }
        }
        for (Bullet bullet : new ArrayList<>(playerBullets)) {
            if (!bullet.isAlive || bullet.y + bullet.height < 0) {
                playerBullets.remove(bullet);
            }
        }
    }

    private void check() {//проверка элементов
        playerShip.verifyHit(enemyBullets);//проверяем было ли попадание
        score += enemyFleet.verifyHit(playerBullets);// было ли попадание во флот и складываем очки
        enemyFleet.deleteHiddenShips();//удаляем взорванные корабли
        removeDeadBullets();//удаляем ненужные пули
        if (!playerShip.isAlive) {//если корабль подбит
            stopGameWithDelay();//конец игры
        }
        if (enemyFleet.getBottomBorder() >= playerShip.y) {
            playerShip.kill();//если флот достиг дна, проигрыш
        }
        if (enemyFleet.getShipsCount() == 0) {
            playerShip.win();//если не осталось кораблей, то победа
        }
    }

    private void stopGame(boolean isWin) {//метод отвечает за остановку игры
        isGameStopped = true;//игра остановлена
        stopTurnTimer();//таймер остановлен
        if (isWin == true) {//выводим сообщение или о проигрыше
            showMessageDialog(Color.WHITE, "YOU WIN", Color.GREEN, 75);
        } else {
            showMessageDialog(Color.WHITE, "YOU LOST", Color.RED, 75);
        }
    }

    private void stopGameWithDelay() {//метод проигрывает последнюю анимацию и только потом завершает игру
        animationsCount++;
        if (animationsCount >= 10) {
            stopGame(playerShip.isAlive);
        }
    }

    @Override
    public void onKeyPress(Key key) {//метод отвечает за нажатие клавиш
        if (key == Key.SPACE && isGameStopped) {
            createGame();
        } else if (key == Key.LEFT) {
            playerShip.setDirection(Direction.LEFT);
        } else if (key == Key.RIGHT) {
            playerShip.setDirection(Direction.RIGHT);
        }

        if (key == Key.SPACE) {
            Bullet bullet = playerShip.fire();
            if (bullet != null && playerBullets.size() < PLAYER_BULLETS_MAX) {
                playerBullets.add(bullet);
            }
        }
    }

    @Override
    public void onKeyReleased(Key key) {//метод отвечает за то, что произойдет если кнопка отпущена
        if (key == Key.LEFT && playerShip.getDirection() == Direction.LEFT) {
            playerShip.setDirection(Direction.UP);
        }
        if (key == Key.RIGHT && playerShip.getDirection() == Direction.RIGHT) {
            playerShip.setDirection(Direction.UP);
        }
    }

    @Override
    public void setCellValueEx(int x, int y, Color cellColor, String value) {
        if (x < 0 || x > WIDTH - 1 || y < 0 || y > HEIGHT - 1) {
            return;
        } else {
            super.setCellValueEx(x, y, cellColor, value);
        }
    }
}
