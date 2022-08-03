package sample.Sapper;

public class GameObject { //игровой объект содержит в себе информацию о...
    public int x;//координата по оси х
    public int y;//координата по оси y
    public boolean isMine;//заминирован ли он?
    public int countMineNeighbors;//знает сколько заминированных соседей
    public boolean isOpen;//открыта ли ячейка?
    public boolean isFlag;//ячейка с флагом?

    GameObject(int x, int y, boolean isMine) {//для создания объекта необходимы данные о координатах и о минировании
        this.x = x;
        this.y = y;
        this.isMine = isMine;
    }
}
