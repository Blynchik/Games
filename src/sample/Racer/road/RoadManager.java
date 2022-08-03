package sample.Racer.road;

import sample.Engine.*;
import sample.Racer.*;

import java.util.ArrayList;
import java.util.List;

public class RoadManager {
    public static final int LEFT_BORDER = RacerGame.ROADSIDE_WIDTH;//края дороги
    public static final int RIGHT_BORDER = RacerGame.WIDTH - LEFT_BORDER;
    private static final int FIRST_LANE_POSITION = 16;//крайние позиции случайных объектоа
    private static final int FOURTH_LANE_POSITION = 44;
    private List<RoadObject> items = new ArrayList<>();//здесь хранятся объекты, которые находятся на экране
    private static final int PLAYER_CAR_DISTANCE =12;//минимальное асстояние между объектами
    private int passedCarsCount = 0;//машин проехано
    private RoadObject createRoadObject(RoadObjectType type, int x, int y) {//метод возвращает препятствие
        if (type == RoadObjectType.THORN) {
            return new Thorn(x, y);
        }else if (type == RoadObjectType.DRUNK_CAR){
            return  new MovingCar(x,y);
        } else {
            return new Car(type, x, y);
        }
    }

    public int getPassedCarsCount(){
        return passedCarsCount;
    }
    private void addRoadObject(RoadObjectType type, Engine game) {//метод выбирает случайное место на дороге и добавляет препятствие в список
        int x = game.getRandomNumber(FIRST_LANE_POSITION, FOURTH_LANE_POSITION);
        int y = -1 * RoadObject.getHeight(type);
        RoadObject obj = createRoadObject(type, x, y);
        if (obj != null && isRoadSpaceFree(obj)) {//хватает ли места для объекта
            items.add(obj);
        }
    }

    public void draw(Engine game) {//метод отрисовывает препятствия
        for (int i = 0; i < items.size(); i++) {
            items.get(i).draw(game);
        }
    }

    public void move(int boost) {//метод передвигает объекты и удаляет ненужные
        for (int i = 0; i < items.size(); i++) {
            items.get(i).move(boost + items.get(i).speed, items);
        }
        deletePassedItems();
    }

    private boolean isThornExists(){//метод проверяет, есть ли шип на экране
        for(int i = 0;i< items.size();i++){
            if(items.get(i) instanceof Thorn){
                return true;
            }
        }
        return false;
    }

    private void generateThorn(Engine game){//метод создает шип с вероятностю 10% если нет шипов
        int num = game.getRandomNumber(100);
        if(num<10 && isThornExists()==false){
            addRoadObject(RoadObjectType.THORN, game);
        }
    }

    public void generateNewRoadObjects(Engine game){//метод создает препятствия
        generateThorn(game);
        generateRegularCar(game);
        generateMovingCar(game);
    }

    private void deletePassedItems(){//метод удаляет объект, который сейчас не на экране
        for(int i = 0;i< items.size();i++){
            if(items.get(i).y>=RacerGame.HEIGHT){
                if(!(items.get(i) instanceof Thorn)){
                    passedCarsCount++;
                }
                items.remove(items.get(i));
            }
        }
    }

    public boolean checkCrush(PlayerCar playerCar){
        for(int i = 0; i< items.size();i++){
          return items.get(i).isCollision(playerCar);
        }
        return false;
    }

    private void generateRegularCar(Engine game){//метод определяет каккой вид машины выедет навстречу
        if(game.getRandomNumber(100)<30){
            int carTypeNumber = game.getRandomNumber(4);
            addRoadObject(RoadObjectType.values()[carTypeNumber], game);
        };
    }

    private boolean isRoadSpaceFree(RoadObject object){//метод проверяет достаточно ли места
        for(int i = 0; i< items.size();i++){
           if(items.get(i).isCollisionWithDistance(object, PLAYER_CAR_DISTANCE)){
               return false;
           }
        }
        return true;
    }

    private boolean isMovingCarExists(){//метод проверяет есть перемещающийя автомобиль
        for(int i = 0; i< items.size();i++){
            if(items.get(i) instanceof  MovingCar){
                return true;
            }
        }
        return false;
    }

    private void generateMovingCar(Engine game){
        if(game.getRandomNumber(100)<10&&isMovingCarExists()==false){
            addRoadObject(RoadObjectType.DRUNK_CAR, game);
        };
    }
}
