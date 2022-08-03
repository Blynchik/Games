package sample.SpaceInvaders.Objects;

import sample.Engine.*;

public class Star extends InvaderObject{
    private static final String STAR_SIGN = "\u2605";//символ звезды

    public Star(double x, double y){
        super(x,y);
    }

    public void draw(Engine game){
        game.setCellValueEx((int)x,(int)y, Color.NONE,STAR_SIGN,Color.WHITE,100);//рисуем звезды
    }
}
