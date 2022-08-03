package sample.SpaceInvaders.Objects;

import sample.SpaceInvaders.Direction;

public class Boss extends EnemyShip {
    public int score = 100;
    private int frameCount = 0;

    public Boss(double x, double y) {
        super(x, y);
        this.setAnimatedView(true,ShapeMatrix.BOSS_ANIMATION_FIRST,
                ShapeMatrix.BOSS_ANIMATION_SECOND);
    }

    @Override
    public void nextFrame() {
        frameCount++;
        if (frameCount % 10 == 0 || !isAlive) {
            super.nextFrame();
        }
    }

    public Bullet fire() {
        if (this.isAlive == false) {
            return null;
        }
        if (matrix == ShapeMatrix.BOSS_ANIMATION_FIRST) {
            return new Bullet(x + 6, y + height, Direction.DOWN);
        }
        return new Bullet(x, y + height, Direction.DOWN);
    }

    public void kill() {
        if (!isAlive) {
            return;
        }
        isAlive = false;

        this.setAnimatedView(false,
                ShapeMatrix.KILL_BOSS_ANIMATION_FIRST,
                ShapeMatrix.KILL_BOSS_ANIMATION_SECOND,
                ShapeMatrix.KILL_BOSS_ANIMATION_THIRD);
    }
}