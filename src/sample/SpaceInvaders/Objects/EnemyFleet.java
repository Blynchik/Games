package sample.SpaceInvaders.Objects;

import sample.SpaceInvaders.Direction;
import sample.SpaceInvaders.*;
import sample.Engine.*;

import java.util.ArrayList;
import java.util.List;

public class EnemyFleet {
    private static final int ROWS_COUNT = 3;//ряды флота
    private static final int COLUMNS_COUNT = 10;//столбцы
    private static final int STEP = ShapeMatrix.ENEMY.length + 1;
    private List<EnemyShip> ships;
    private Direction direction = Direction.RIGHT;

    public EnemyFleet() {
        createShips();
    }

    private void createShips() {
        ships = new ArrayList<EnemyShip>();
        for (int x = 0; x < COLUMNS_COUNT; x++) {
            for (int y = 0; y < ROWS_COUNT; y++) {
                ships.add(new EnemyShip(x * STEP, y * STEP + 12));
            }
        }
        ships.add(new Boss(STEP * COLUMNS_COUNT / 2 - ShapeMatrix.KILL_BOSS_ANIMATION_FIRST.length / 2 - 1, 5));

    }

    public void draw(Engine game) {
        for (int i = 0; i < ships.size(); i++) {
            ships.get(i).draw(game);
        }
    }

    private double getLeftBorder() {
        double min = Double.MAX_VALUE;
        for (int i = 0; i < ships.size(); i++) {
            if (ships.get(i).x < min) {
                min = ships.get(i).x;
            }
        }
        return min;
    }

    private double getRightBorder() {
        double max = Double.MIN_VALUE;
        for (int i = 0; i < ships.size(); i++) {
            if (ships.get(i).x + ships.get(i).width > max) {
                max = ships.get(i).x + ships.get(i).width;
            }
        }
        return max;
    }

    private double getSpeed() {
        double s = 3.0 / ships.size();
        if (s > 2.0) {
            return 2.0;
        }
        return s;
    }

    public void move() {
        if (ships.size() == 0) {
            return;
        } else if (direction == Direction.LEFT && getLeftBorder() < 0) {
            direction = Direction.RIGHT;
            for (int i = 0; i < ships.size(); i++) {
                ships.get(i).move(Direction.DOWN, getSpeed());
            }
        } else if (direction == Direction.RIGHT && getRightBorder() > SpaceInvadersGame.WIDTH) {
            direction = Direction.LEFT;
        } else {
            for (int i = 0; i < ships.size(); i++) {
                ships.get(i).move(direction, getSpeed());
            }
        }
    }

    public Bullet fire(Engine game) {
        if (ships.isEmpty()) {
            return null;
        }
        if (game.getRandomNumber(100 / SpaceInvadersGame.COMPLEXITY) > 0) {
            return null;
        }
        return ships.get(game.getRandomNumber(ships.size())).fire();
    }

    public int verifyHit(List<Bullet> bullets) {
        if(bullets.isEmpty()){
            return 0;
        }
        int score = 0;
        for (EnemyShip ship : ships) {
            for (Bullet bullet : bullets) {
                if (ship.isCollision(bullet)) {
                    if (ship.isAlive) {
                        if (bullet.isAlive) {
                            ship.kill();
                            bullet.kill();
                            score +=ship.score;
                        }
                    }
                }
            }
        }
        return score;
    }

    public void deleteHiddenShips() {
        for (EnemyShip ship : new ArrayList<>(ships)) {
            if (ship.isVisible() == false) {
                ships.remove(ship);
            }
        }
    }

    public double getBottomBorder(){
        double max = Double.MIN_VALUE;
        for(EnemyShip ship: ships){
            if(ship.y+ ship.height>max){
                max = ship.y+ship.height;
            }
        }
        return max;
    }

    public int getShipsCount(){
        return ships.size();
    }

}
