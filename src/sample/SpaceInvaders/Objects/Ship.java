package sample.SpaceInvaders.Objects;

import sample.Engine.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Ship extends InvaderObject {
    public boolean isAlive = true;
    private List<int[][]> frames;
    private int frameIndex;
    private boolean loopAnimation = false;

    public Ship(double x, double y) {
        super(x, y);
    }

    public void setStaticView(int[][] viewFrame) {
        setMatrix(viewFrame);
        frames = new ArrayList<int[][]>();
        frames.add(viewFrame);
        frameIndex = 0;
    }

    public Bullet fire() {
        return null;
    }

    public void kill() {
        isAlive = false;
    }

    public void nextFrame() {
        frameIndex++;

        if (frameIndex >= frames.size()) {
            if (loopAnimation) {
                frameIndex = 0;
            } else {
                return;
            }
        }

        matrix = frames.get(frameIndex);
    }

    @Override
    public void draw(Engine game) {
        super.draw(game);
        nextFrame();
    }

    public boolean isVisible() {
        if (!isAlive && frameIndex >= frames.size()) {
            return false;
        }
        return true;
    }

    public void setAnimatedView(boolean isLoopAnimation, int[][]... viewFrames) {
        this.setMatrix(viewFrames[0]);

        frames = Arrays.asList(viewFrames);
        frameIndex = 0;
        loopAnimation = isLoopAnimation;
    }
}
