package sample.Racer;

import java.util.ArrayList;
import java.util.List;
import sample.Engine.*;

public class RoadMarking {
    private List<GameObject> roadMarking = new ArrayList<>();

    public RoadMarking(){//разметка на дороге
        for(int i = -4; i<=RacerGame.HEIGHT+4;i+=8){
            roadMarking.add(new GameObject(RacerGame.CENTER_X-9,i,ShapeMatrix.ROAD_MARKING));//слева
            roadMarking.add(new GameObject(RacerGame.CENTER_X+9, i, ShapeMatrix.ROAD_MARKING));//справа
        }
    }

   public void move(int boost){//метод регулирует ускорение смены разметки
        for(GameObject item: roadMarking){
            if(item.y>=RacerGame.HEIGHT-1){//если координата разметки превысила видимую высоту
                item.y = item.y - RacerGame.HEIGHT - 8 + boost;//разметка отрисуется ниже + ускорение
            } else {//если не привысила
                item.y+=boost;//то там там же + ускорение
            }
        }
   }

   public void draw(Engine game){//метод отрисовывает разметку
        for(GameObject item:roadMarking){
            item.draw(game);
        }
   }

}
