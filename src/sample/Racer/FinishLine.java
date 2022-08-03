package sample.Racer;

public class FinishLine extends GameObject {

    private boolean isVisible = false;

    public FinishLine(){
        super(RacerGame.ROADSIDE_WIDTH,-1*ShapeMatrix.FINISH_LINE.length,ShapeMatrix.FINISH_LINE);//финишная линия находится выше экрана
    }

    public void showFinish(){
        isVisible = true;//видима
    }

    public void move(int boost){
        if(isVisible==false){//как только финишная примая станет видима
            return;
        }
        y+=boost;//она начнет движение
    }

    public boolean isCrossed(PlayerCar car){//если машина мересекла черту
        if(car.y<y){
            return true;//то метка пройдена
        }
        return false;
    }
}
