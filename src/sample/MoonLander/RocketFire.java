package sample.MoonLander;

import java.util.List;
import sample.Engine.*;

public class RocketFire extends GameObject{//выхлопной огонь
    private int frameIndex;//индекс массива огня
    private List<int[][]> frames;//список массивов огня
    private boolean isVisible;//видимый или нет

    public RocketFire(List<int[][]> frameList){//конструктор
        super(0,0, frameList.get(0));
        frames = frameList;
        frameIndex = 0;
        isVisible = false;
    }

    private void nextFrame(){//кадр отображения огня
        frameIndex++;//изменение очередности из списка
        if(frameIndex>=frames.size()){//если больше списка, то сначала
            frameIndex= 0;
        }
        matrix = frames.get(frameIndex);//матрица ракеты будет полностью формироваться здесь
    }

    @Override
    public void draw(Engine game) {//отрисовка огня
        if( isVisible == false){//если он невидим
            return;//ничего не делаем
        }
        nextFrame();//новый кадр
        super.draw(game);//отрисовка
    }

    public void show(){//скрыт огонь или нет
        isVisible = true;
    }

    public void hide(){
        isVisible = false;
    }
}
