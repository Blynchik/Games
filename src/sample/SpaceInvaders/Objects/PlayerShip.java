package sample.SpaceInvaders.Objects;

import sample.SpaceInvaders.Direction;
import sample.SpaceInvaders.SpaceInvadersGame;

import java.util.List;

public class PlayerShip extends Ship {
    private Direction direction = Direction.UP;

    public PlayerShip() {
        super(SpaceInvadersGame.WIDTH / 2.0, SpaceInvadersGame.HEIGHT - ShapeMatrix.PLAYER.length - 1);
        setStaticView(ShapeMatrix.PLAYER);
    }

    public void setDirection(Direction newDirection) {
        if (newDirection != Direction.DOWN) {
            this.direction = newDirection;
        }
    }

    public Direction getDirection(){
        return direction;
    }

    public void verifyHit(List<Bullet> bullets) {//метод проверяет было ли попадание
        if (bullets.isEmpty()) {//если пуль нет, ничего не делаем
            return;
        }
        if (isAlive) {
            for (Bullet bullet : bullets) {
                if (isAlive && bullet.isAlive && isCollision(bullet)) {
                    kill();
                    bullet.kill();
                    break;
                }
            }
        }
    }

    @Override
    public void kill() {
        if(!isAlive){
            return;
        } else {
            isAlive = false;
            setAnimatedView(false, ShapeMatrix.KILL_PLAYER_ANIMATION_FIRST,ShapeMatrix.KILL_PLAYER_ANIMATION_SECOND, ShapeMatrix.KILL_PLAYER_ANIMATION_THIRD, ShapeMatrix.DEAD_PLAYER);
        }
    }

    public void move(){
        if(isAlive==false){
            return;
        } else if(direction==Direction.LEFT){
            x--;
        } else if(direction==Direction.RIGHT){
            x++;
        }

        if(x<0){
            x=0;
        }

        if(x+width>SpaceInvadersGame.WIDTH){
            x = SpaceInvadersGame.WIDTH-width;
        }
    }

    @Override
    public Bullet fire() {
        if(!isAlive){
        } else {
            return new Bullet(x+2,y-ShapeMatrix.BULLET.length,Direction.UP);
        }
        return null;
    }

    public void win(){
        setStaticView(ShapeMatrix.WIN_PLAYER);
    }
}
