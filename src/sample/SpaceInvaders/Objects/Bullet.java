package sample.SpaceInvaders.Objects;

import sample.SpaceInvaders.Direction;

public class Bullet extends InvaderObject{
    private int dy;
    public boolean isAlive = true;

    public Bullet(double x, double y, Direction direction){
        super(x,y);
        setMatrix(ShapeMatrix.BULLET);
        if(direction==Direction.UP){
            dy = -1;
        } else if(direction!=Direction.UP){
            dy = 1;
        }
    }

    public void move(){
        y +=dy;
    }

    public void kill(){
        isAlive = false;
    }
}
