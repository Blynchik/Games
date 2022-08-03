package sample.Racer;

import sample.Racer.road.RoadManager;

public class PlayerCar extends GameObject {
    private static int playerCarHeight = ShapeMatrix.PLAYER.length;//матрица машины игрока
    public int speed = 1;//скорость машины
    private Direction direction;//направление движения

    public PlayerCar() {
        super(RacerGame.WIDTH / 2 + 2, RacerGame.HEIGHT - playerCarHeight - 1, ShapeMatrix.PLAYER);//расположение машины при старте
    }

    public void setDirection(Direction direction){//устанавливает направление
        this.direction = direction;
    }

    public Direction getDirection(){//возвращает направление
        return direction;
    }

    public void move(){//изменяет положение машины
        if(direction==Direction.LEFT){
            x--;
        } else if(direction == Direction.RIGHT){
            x++;
        }

        if(x< RoadManager.LEFT_BORDER){//если машина выезжает за пределы дороги, то она отсается в пределах дороги
            x = RoadManager.LEFT_BORDER;
        } else if(x>RoadManager.RIGHT_BORDER-width)
            x = RoadManager.RIGHT_BORDER-width;
    }

    public void stop(){
        matrix = ShapeMatrix.PLAYER_DEAD;
    }
}
