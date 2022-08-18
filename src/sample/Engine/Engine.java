package sample.Engine;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import javafx.scene.paint.Color;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Random;

public class Engine extends Application {//implements GameScreen {

    private int width;            //ширина игрового поля (состоит из клеток)
    private int height;           //высота игрового поля (состоит из клеток)
    private int timerStep = 0;

    private boolean showCoordinates = false;//по умолчанию не показываем координаты
    private boolean showGrid = true;//показываем разметку?
    private boolean isMessageShown = false;//сообщение показано?
    private boolean isAnnotationShown = true;
    private boolean isNoteShown = false;
    private boolean showTV = true; //показываем интерактивный экран?

    private StackPane cells[][];  // панель объектов (в данном случае двумерное поле из объектов StackPane),StackPane - панель, которая может отображать что-то слоями в качестве стека
    private Stage primaryStage;   //окно содержащее все элементы
    private Stage annotationStage;
    private Text scoreText;       //переменная, куда сохраняются заработанные очки
    private Pane root;            //корневая панель
    private Pane annotationPanel;
    private Timeline timeline = new Timeline();//создаем временную шкалу для проигрывания анимации
    private TextFlow dialogContainer;//макет для создания текстовых потоков
    private TextFlow noteContainer;
    private TextFlow annotationContainer;
    private String annotation;

    private static Random random = new Random();//новый объект класса Random(случайное число)
    private static int cellSize;  //размер стороны клетки, из которых состоит игровое поле

    public Engine() {//конструктор
    }

    public void setAnnotation(String annotation) {
        this.annotation = annotation;
    }

    public void initialize() {//инициализация
    }

    private void createScorePanel() {//метод создает панель с выводом набранных очков
        this.scoreText.setFont(Font.font("Verdana", FontWeight.BOLD, 16.0D));//тексту задается шрифт, толщина, размер
        this.scoreText.setFill(this.toFXColor(sample.Engine.Color.BLACK));//цвет текста становится цветом из нашего списка
        StackPane scorePane = new StackPane(new Node[]{this.scoreText});//создаем новую панель объектов, которая состоит из массива объектов Node, в этом массиве только текст с количеством очков
        scorePane.setBorder(new Border(new BorderStroke[]{new BorderStroke(this.toFXColor(sample.Engine.Color.BLACK), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)}));
        //ставим границы для панели с очками. Границы состоят из обводки(BorderStroke), которая в свою очередь несет в себе информацию о цвете обводки, стиле обводки (это сплошная линия (SOLID)), кривизне углов (EMPTY - квадратные) и толщине границы(DEFAULT - по умолчанию)
        scorePane.setLayoutY((double) (this.height * cellSize + 110 + 6));//высота панели количество клеток*на их сторону + место для экрана + 6 обводка
        int scoreHeight = 20;
        Rectangle rectangle;
        if (this.showGrid) {//если разметка включена
            rectangle = new Rectangle((double) ((this.width * cellSize - 1) / 2), (double) scoreHeight, this.toFXColor(sample.Engine.Color.WHITE));
            //инициализируем новый четырехуголник со строной в двое меньше клетки -1 для для разметки, высотой текста и цветом из списка
            scorePane.setLayoutX((double) (125 + (this.width * cellSize - 1) / 4));//ширина панели 125(для экрана)+ ширина в четверо меньше клетки -1 для разметки
        } else {//если разметка выключена
            rectangle = new Rectangle((double) (this.width * cellSize / 2), (double) scoreHeight, this.toFXColor(sample.Engine.Color.WHITE));
            //инициализируем новый четырехуголник со строной в двое меньше клетки, высотой текста и цветом из списка
            scorePane.setLayoutX((double) (124 + this.width * cellSize / 4));//ширина панели 125(для экрана)+ ширина в четверо меньше клетки
        }
        scorePane.getChildren().add(0, rectangle);//добавляем в панель этот прямоугольник
        this.root.getChildren().add(scorePane);//добавляем эту панель в корневую панель
    }

    private Key getKey(KeyCode code) {//возвращает название нажатой кнопки
        String keyName = (String) Arrays.stream(Key.values()).map(Enum::name).filter((name) -> { //получаем название клавиши из потока, который состоит из массива, в котором мапа (список-имя), но нужно найти только по имени
            return code.name().equalsIgnoreCase(name);//возвращаем навание клавиши игнорируя регистр
        }).findFirst().orElse(Key.UNKNOWN.name());//извлекаем из потока первый элемент(если он есть, иначе неизвестную кнопку)
        return Key.valueOf(keyName);//возвращаем кнопку, которая приравнена к имени из списка
    }

    private Color toFXColor(sample.Engine.Color color) {//метод, который превращает цвет из нашего списка в цвет javafx
        if (color == sample.Engine.Color.NONE) {//если наш цвет отсутсвует,
            return Color.TRANSPARENT;//то сделать цвет прозрачным
        } else {
            return color != null ? Color.valueOf(color.name()) : Color.BLACK;//иначе если наш цвет - null, то цвет = это наш цвет из списка, иначе черный
        }
    }

    private void createBorderImage() {//метод, который рисует рамку(интерактивный экран)
        InputStream inputStream = Engine.class.getResourceAsStream("screen1.png");//в входящий поток помещаем картинку по адресу, метод класса преобразует адрес в поток
        Image image = new Image(inputStream);//создаем новое изображение с URL inputStream
        ImageView imageView = new ImageView(image);//ImageView - обеспечивает отображение изображений Image
        imageView.setFitWidth((double) (this.width * cellSize + 250));//устанавливаем ширину, в рамках которой загруженное изображение может меняться
        imageView.setFitHeight((double) (this.height * cellSize + 110 + 140));//устанавливаем высоту, в рамках которой загруженное изображение может меняться
        this.root.getChildren().add(imageView);//в корневую панель добавляем загруженную картинку
    }

    private Parent createContent() { //Parent обрабатывает все операции графики сцены, метод вернет готовую корневую панель с клетками
        this.root = new Pane(); //инициализируем корневую панель
        this.root.setPrefSize((double) (this.width * cellSize + 250), (double) (this.height * cellSize + 110 + 140));//устанавливаем предпочтительную ширину и высоту для корневой панели (количество клеток*их сторону + что-то(видимо, необходимо для рамки))
        this.createBorderImage();//создаем рисунок для рамки

        for (int y = 0; y < this.height; ++y) { //цикл, который добавляет готовые клетки в корневую панель
            for (int x = 0; x < this.width; ++x) {
                ObservableList<Node> children = this.cells[y][x].getChildren();//создаем список, который позволяет слушателям отслеживать изменения, список заполняется такой конструкцией, которая позволит потом создать кнопку для каждой панели(клетки)
                Rectangle cell; //создаем фигуру клетка
                if (this.showGrid && children.size() > 0) { //если разметка (решетка на игровом поле) включена и список объектов не пуст
                    cell = (Rectangle) children.get(0); //клетка становится НУЛЕВОЙ панелью объектов StackPane
                    cell.setWidth((double) cellSize - 1); //устанвливаем клетке ширину(-1, наверное для отображения разметки)
                    cell.setHeight((double) cellSize - 1); //устанвливаем высоту
                    cell.setStroke(this.toFXColor(sample.Engine.Color.BLACK)); // устанвливаем цвет разметки из списка цветов
                }

                if (this.showCoordinates && children.size() > 2) {//если показываем координаты и список объектов больше 2
                    Text coordinate = (Text) children.get(2); //то объект StackPane становится текстом с координатами 3 ячейки
                    coordinate.setFont(Font.font((double) cellSize * 0.15D)); //устанвливаем для текста с координатами шрифт размером 0.15 от стороны клетки
                    StackPane.setAlignment(coordinate, Pos.TOP_LEFT); //выравниваем StackPane (текста с координатами) в верхний левый угол, в пределах высоты и ширины
                    coordinate.setText(x + " - " + y); //уставливаем координаты
                }

                if (children.size() > 0) {//список объектов не пустой
                    cell = (Rectangle) children.get(0);//клетка становится НУЛЕВОЙ панелью объектов StackPane
                    cell.setWidth((double) cellSize);//устанвливаем клетке ширину
                    cell.setHeight((double) cellSize);//устанвливаем высоту
                    this.cells[y][x].setLayoutX((double) (x * cellSize + 125));//устанавливаем координаты для клетки x
                    this.cells[y][x].setLayoutY((double) (y * cellSize + 110));//устанавливаем координаты для клетки y
                    this.root.getChildren().add(this.cells[y][x]);//добавляем клетку в корневую панель
                }
            }
        }
        this.createScorePanel();//создаем панель с очками
        this.timeline.setCycleCount(-1);//анимация не проигрывается
        return this.root;//возвращаем корневую панель
    }

    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage; //создаем главное окно
        this.scoreText = new Text("Score: 0"); //создаем текст с очками
        this.initialize();
        Scene scene = new Scene(this.createContent());// Scene - контейнер для всех объектов Node, создаем такой, заполняем контейнер контентом с помощью метода

        scene.setOnMouseClicked((event) -> { //задает событие если кнопка нажата и отпущена для всего контенйера объектов
            if (isMessageShown) {
                this.isMessageShown = false;//то не показываем сообщение
                this.dialogContainer.setVisible(false);//не показываем диалоговое окно
                isNoteShown = false;
                noteContainer.setVisible(false);
            }
            if (isAnnotationShown) {
                isAnnotationShown = false;
                annotationContainer.setVisible(false);
            }

            if (cellSize != 0) {//если сторона клетки не ноль
                switch (event.getButton()) {//если изменено состояние события (нажата кнопка мыши) на...
                    case PRIMARY://... нажата и отпущена левая кнопка мыши (основная)
                        if (this.showTV) {//если показан интерактивный экран (по условию true)
                            this.onMouseLeftClick((int) (event.getX() - 125.0D) / cellSize, (int) (event.getY() - 110.0D) / cellSize);//делаем действие для левой кнопки мыши и получаем координаты щелчка Х и У скорректированные на размер интерактивного экрана и поделенные на количество клеток игрового поля, то есть получаем координаты клетки, где произошел щелчок
                        } else {//если интерактивный экран выключен
                            this.onMouseLeftClick((int) event.getX() / cellSize, (int) event.getY() / cellSize);//делаем действие для левой кнопки мыши и получаем координаты клетки, где произошел щелчок, без корректировки на интерактивный экран
                        }
                        break;

                    case SECONDARY://... нажата и отпущена правая кнопка мыши (вторичная)
                        if (this.showTV) {//если показан интерактивный экран (по условию true)
                            this.onMouseRightClick((int) (event.getX() - 125.0D) / cellSize, (int) (event.getY() - 110.0D) / cellSize);//делаем действие для правой кнопки мыши и получаем координаты щелчка Х и У скорректированные на размер интерактивного экрана и поделенные на количество клеток игрового поля, то есть получаем координаты клетки, где произошел щелчок
                        } else {//если интерактивный экран выключен
                            this.onMouseRightClick((int) event.getX() / cellSize, (int) event.getY() / cellSize);//делаем действие для правой кнопки мыши и получаем  координаты клетки, где произошел щелчок, без корректировки на интерактивный экран
                        }
                }
            }
        });
        scene.setOnKeyReleased((event) -> { //в данной сцене(в основном контейнере) определяем функцию, если кнопка нажата и отпущена
            if (!this.isMessageShown) {//если сообщение не показано
                this.onKeyReleased(this.getKey(event.getCode()));//то кнопка нажата
            }
        });
        scene.setOnKeyPressed((event) -> {//в данной сцене(в основном контейнере) опредееляем функцию, если кнопка нажата, но не отпущена
            if (this.isMessageShown || isAnnotationShown) {//если сообщение показано
                if (event.getCode().name().equalsIgnoreCase(Key.SPACE.name())) {//если нажата кнопка соответсвующая пробелу из нашего списка
                    if (isMessageShown) {
                        this.isMessageShown = false;//то не показываем сообщение
                        this.dialogContainer.setVisible(false);//не показываем диалоговое окно
                        isNoteShown = false;
                        noteContainer.setVisible(false);
                    }
                    if (isAnnotationShown) {
                        isAnnotationShown = false;
                        annotationContainer.setVisible(false);
                    }
                }
                this.onKeyPress(this.getKey(event.getCode()));//кнопка нажата и не отпущена
            } else {//если сообщение показано
                this.onKeyPress(this.getKey(event.getCode()));//то кнопка нажата и не отпущена
            }
        });

        primaryStage.setTitle("Blynchik Games");//задаем название окна
        primaryStage.setResizable(false);//нельзя растягивать и сжимать окно

        if (this.showTV) {//если показан интерактивный экран
            primaryStage.initStyle(StageStyle.TRANSPARENT);//устанавливаем стиль окна с прозрачным фоном и без декораций
            scene.setFill(Color.TRANSPARENT);//заполняем сцену прозрачным цветом
        }

        if (isAnnotationShown == true) {
            showAnnotationDialog(sample.Engine.Color.WHITE, annotation, sample.Engine.Color.BLACK, 20);
            this.annotationContainer.setVisible(true);//сделать видимым
        }

        primaryStage.setScene(scene);//помещаем в окно сцену(контейнер со всем)
        primaryStage.show();//делаем видимым окно
        this.timeline.playFromStart();//проигрывание дорожки анимации
    }

    public void setScreenSize(int width, int height) { //метод устанавливает размер игрового поля
        //принимая желаемые значения ширины и высоты
        if (width < 3) {               //если желаемая ширина поля меньше 3 клеток
            this.width = 3;            //то  установить ширину поля 3 клетки
        } else if (width > 100) {      //иначе если желаемая ширина поля больше 100 клеток
            this.width = 100;          //то установить ширину поля 100 клеток
        } else {                       //иначе (если оба условия не подходят, то есть кол-во клеток больше 3, но меньше 100)
            this.width = width;        //то ширина будет соответсовать желаемой
        }

        if (height < 3) {              //если желаемая высота поля меньше 3 клеток
            this.height = 3;           //то  установить высоту поля 3 клетки
        } else if (height > 100) {     //иначе если желаемая высота поля больше 100 клеток
            this.height = 100;         //то установить высоту поля 100 клеток
        } else {                       //иначе (если оба условия не подходят, то есть кол-во клеток = больше 3, но меньше 100)
            this.height = height;      //то высота будет соответсовать желаемой
        }

        if (800 / this.width < 600 / this.height) { //если деление на ширину меньше деления на высоту
            cellSize = 800 / this.width;       //то сторона клетки будет такой
        } else {                          //если наоборот
            cellSize = 600 / this.height;      //то такой
        }                                 //800 и 600 скорее всего подобраны под отображение экрана 3Х4
        //это нужно если игровое поле будет не квадратным, чтобы клетки разместились по полю в рамках одного экрана

        this.cells = new StackPane[this.height][this.width]; //инициализация массива, что его размеры будут такими высота Х ширина

        for (int y = 0; y < this.height; ++y) { //массив заполняется объектами StackPane
            for (int x = 0; x < this.width; ++x) {
                this.cells[y][x] = new StackPane(new Node[]{new Rectangle(), new Text(), new Text()});
            }                                   //каждая панель StackPane должна состоять из массива с объектами Node(кнопки, метки и др.)
        }                                       //массив из Node состоит из прямоугольника, текста и текста
    }

    public void showMessageDialog(sample.Engine.Color cellColor, String message, sample.Engine.Color textColor, int textSize) {//метод добавляет в корневую панель дилоговое окно
        if (this.dialogContainer == null) {//если контейнер с текстом пустой
            this.dialogContainer = new TextFlow();//помещаем в контейнер текст
            this.root.getChildren().add(this.dialogContainer);//контейнер добавляем в основную панель
        }

        this.dialogContainer.getChildren().clear();//очищаем контейнер
        Text messageText = new Text();//создаем новый текст
        messageText.setFont(Font.font("Verdana", FontWeight.BOLD, (double) textSize));//для текста устанавливаем шрифт, жирность, размер
        messageText.setText(message);//устанавливает текст
        double preferredWidth = messageText.getLayoutBounds().getWidth();//предпочитаемая ширина = ширина панели для текста
        messageText.setFill(this.toFXColor(textColor));//красим текст в цвет из списка
        this.dialogContainer.setLayoutX((this.root.getWidth() - preferredWidth) / 2.0D);//ширина панели для диалогового окна (ширина основной панели - ширина текста)/2
        this.dialogContainer.setLayoutY(this.root.getHeight() / 2.0D - 30.0D);//высота диалогового окна(высота основной панели/2 - 30)
        this.dialogContainer.setBackground(new Background(new BackgroundFill[]{new BackgroundFill(this.toFXColor(cellColor), CornerRadii.EMPTY, Insets.EMPTY)}));
        //в диалоговом окне фон - это фон, заполняемый массивом из цвета из списка, кривизне углов(квадратные), внутренние смещения(нет)
        this.dialogContainer.setVisible(true);//сделать видимым
        this.dialogContainer.getChildren().add(messageText);//добавляем в контейнер текст
        this.isMessageShown = true;//текст становится видимым
    }

    public void showNoteDialog(sample.Engine.Color cellColor, String message, sample.Engine.Color textColor, int textSize) {//метод добавляет в корневую панель дилоговое окно
        if (this.noteContainer == null) {//если контейнер с текстом пустой
            this.noteContainer = new TextFlow();//помещаем в контейнер текст
            this.root.getChildren().add(this.noteContainer);//контейнер добавляем в основную панель
        }

        this.noteContainer.getChildren().clear();//очищаем контейнер
        Text messageText = new Text();//создаем новый текст
        messageText.setFont(Font.font("Verdana", FontWeight.BOLD, (double) textSize));//для текста устанавливаем шрифт, жирность, размер
        messageText.setText(message);//устанавливает текст
        double preferredWidth = messageText.getLayoutBounds().getWidth();//предпочитаемая ширина = ширина панели для текста
        messageText.setFill(this.toFXColor(textColor));//красим текст в цвет из списка
        this.noteContainer.setLayoutX((this.root.getWidth() - preferredWidth) / 2.0D);//ширина панели для диалогового окна (ширина основной панели - ширина текста)/2
        this.noteContainer.setLayoutY(this.root.getHeight() / 2.0D + 150.0D);//высота диалогового окна(высота основной панели/2 - 30)
        this.noteContainer.setBackground(new Background(new BackgroundFill[]{new BackgroundFill(this.toFXColor(cellColor), CornerRadii.EMPTY, Insets.EMPTY)}));
        //в диалоговом окне фон - это фон, заполняемый массивом из цвета из списка, кривизне углов(квадратные), внутренние смещения(нет)
        this.noteContainer.setVisible(true);//сделать видимым
        this.noteContainer.getChildren().add(messageText);//добавляем в контейнер текст
        this.isNoteShown = true;//текст становится видимым
    }

    public void showAnnotationDialog(sample.Engine.Color cellColor, String message, sample.Engine.Color textColor, int textSize) {//метод добавляет в корневую панель дилоговое окно
        if (this.annotationContainer == null) {//если контейнер с текстом пустой
            this.annotationContainer = new TextFlow();//помещаем в контейнер текст
            this.root.getChildren().add(this.annotationContainer);//контейнер добавляем в основную панель
        }

        this.annotationContainer.getChildren().clear();//очищаем контейнер
        Text messageText = new Text();//создаем новый текст
        messageText.setFont(Font.font("Verdana", FontWeight.BOLD, (double) textSize));//для текста устанавливаем шрифт, жирность, размер
        setAnnotation(message);
        messageText.setText(annotation);//устанавливает текст
        messageText.setFill(this.toFXColor(textColor));//красим текст в цвет из списка
        this.annotationContainer.setLayoutX(140);//ширина панели для диалогового окна (ширина основной панели - ширина текста)/2
        this.annotationContainer.setLayoutY((this.height * cellSize + 140) / 2);//высота диалогового окна(высота основной панели/2 - 30)
        this.annotationContainer.setBackground(new Background(new BackgroundFill[]{new BackgroundFill(this.toFXColor(cellColor), CornerRadii.EMPTY, Insets.EMPTY)}));
        this.annotationContainer.setBorder(new Border(new BorderStroke[]{new BorderStroke(this.toFXColor(sample.Engine.Color.BLACK), BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)}));
        //в диалоговом окне фон - это фон, заполняемый массивом из цвета из списка, кривизне углов(квадратные), внутренние смещения(нет)
        this.annotationContainer.getChildren().add(messageText);//добавляем в контейнер текст
    }

    public void setCellValue(int x, int y, String value) {//метод присвивает тексту в клетке какое-то значение
        ObservableList<Node> children = this.cells[y][x].getChildren();//получаем список того, что вообще есть в клетке
        if (children.size() > 1) {//если в списке больше одного элементов
            Text text = (Text) children.get(1);//то берем второй (внутри лежит массив с Node[]{Rectangle, Text, Text}), см. setScreenSize в конце
            if (text.getText().equals(value)) {//если текст такой же, как и входной
                return;//то все оставляем так же
            }
            if (value.length() <= 4) {//если длина строки меньше 4
                double fontSize = (double) cellSize * 0.4D;//размер шрифта 40% от стороны клетки
                text.setFont(Font.font(fontSize));//устанавливаем размер
            } else {//если длина строки больше 4
                int fontSize = cellSize / value.length();//размер шрифта такой
                text.setFont(Font.font((double) fontSize));//устанавливаем размер шрифта
            }
            text.setText(value);//устанавливаем значение текста
        }
    }

    public void setCellColor(int x, int y, sample.Engine.Color color) { //метод устанвливает цвет, где вызывется
        if (color != null && color != sample.Engine.Color.NONE) { // если цвет отсутствует, то
            ObservableList<Node> children = this.cells[y][x].getChildren();//создаем список, который позволяет слушателям отслеживать изменения, список заполняется такой конструкцией, которая позволит потом создать кнопку для каждой панели
            if (children.size() > 0 && !Color.valueOf(color.name()).equals(((Rectangle) children.get(0)).getFill())) {
                //если список не пустой и Цвет не будет цветом, который соответсвует цвету НУЛЕВОЙ панели из списка, сделанной прямоугольником и заполненной каким-то цветом
                ((Rectangle) children.get(0)).setFill(Color.valueOf(color.name()));
                //НУЛЕВАЯ панель из списка, сделанная прямоугольником, заполняется цветом такого имени
            }
        }
    }

    public void setCellValueEx(int x, int y, sample.Engine.Color cellColor, String value) {//перегрузка метода, который полностью может отрисовать текст, клетку и т.д
        this.setCellValue(x, y, value);//устанвливает текст
        this.setCellColor(x, y, cellColor);//устанавливает цвет клетки
    }

    public void setCellValueEx(int x, int y, sample.Engine.Color cellColor, String value, sample.Engine.Color textColor) {//перегрузка метода, который полностью может отрисовать текст, клетку и т.д
        this.setCellValueEx(x, y, cellColor, value);//устанавливает цвет клетки и текст
        this.setCellTextColor(x, y, textColor);//устанавливает цвет текста
    }

    public void setCellValueEx(int x, int y, sample.Engine.Color cellColor, String value, sample.Engine.Color textColor, int textSize) {//перегрузка метода, который полностью может отрисовать текст, клетку и т.д
        this.setCellValueEx(x, y, cellColor, value, textColor);//устанавливает цвет клетки, текст, цвет текста
        this.setCellTextSize(x, y, textSize);//устанавливает размер текста
    }

    public void setCellTextColor(int x, int y, sample.Engine.Color color) {//метод устанавливает цвет текста
        ObservableList<Node> children = this.cells[y][x].getChildren();//получаем список всего, что есть в клетке по данным координатам
        if (children.size() > 1) {//если в клетке больше 1 элемента
            Text text = (Text) children.get(1);//то берем второй (внутри лежит массив с Node[]{Rectangle, Text, Text})
            if (!this.toFXColor(color).equals(text.getFill())) {//если введенный цвет не соответсвует текущему цвету
                text.setFill(this.toFXColor(color));//красим текст цветом из списка
            }
        }
    }

    public void setCellTextSize(int x, int y, int size) {//метод уставливает размер шрифта в клетке
        ObservableList<Node> children = this.cells[y][x].getChildren();//получаем список всего, что находится в клетке
        if (children.size() > 1) {//если в списке больше 1 элемента
            Text text = (Text) children.get(1);//то берем второй (внутри лежит массив с Node[]{Rectangle, Text, Text})
            size = size > 100 ? 100 : size;//если размер >100, устанавливаем 100, иначе введенный размер(в процентах от клетки)
            double fontSize = (double) cellSize * ((double) size / 100.0D);//размер текста сторона клетки умноженная на размер в процентах от 100%
            if (!Font.font(fontSize).equals(text.getFont())) {//если размер текста не соответсвует уже уставновленному размеру
                text.setFont(Font.font(fontSize));//установить введенный размер
            }
        }
    }

    public void setCellNumber(int x, int y, int value) {
        this.setCellValue(x, y, String.valueOf(value));
    }//метод вписывает в клетку цифру

    public void setTurnTimer(int timeMs) {//метод-таймер устанавливает время хода
        this.timeline.stop();//временная шкала останавливается
        KeyFrame frame = new KeyFrame(Duration.millis((double) timeMs), (event) -> {//ключевой кадр состоит из длительности в миллисекундах и события и значений кадра
            if (!this.isMessageShown) {//если сообщение показано
                this.onTurn(++this.timerStep);//то вызываем метод, в котором описывается, что происходит за ход, идет осчет времени хода
            }
        }, new KeyValue[0]);//значений кадра для интерполяции
        this.timeline.getKeyFrames().clear();//для текущей временной шкалы очищаются все ключевые кадры
        this.timeline.getKeyFrames().add(frame);//для текущей временной шкалы добавляется текущий кадр
        this.timeline.play();//временная шкала запускается
    }

    public void setScore(int score) {//метод устанавливает значение очков
        this.scoreText.setText("Score: " + score);//устанвливаем текст для панели
    }

    public void showGrid(boolean isShow) {//метод, который говорит нужна разметка или нет
        this.showGrid = isShow;
    }

    public int getRandomNumber(int max) {//метод генерирует случайное число от 0 до max не включая
        return random.nextInt(max);//получаем число int random в пределах от 0 до max
    }

    public int getRandomNumber(int min, int max) {//метод генерирует число в промежутке
        return random.nextInt(max - min) + min;
    }

    public void stopTurnTimer() {//шкала времени остановлена
        this.timeline.stop();
    }

    public void onMouseLeftClick(int x, int y) {//метод реагирует на нажатие левой кнопки мыши с координатами
    }

    public void onMouseRightClick(int x, int y) {//метод регирует на нажатие правой кнопки мыши с координатами
    }

    public void onKeyReleased(Key key) {//метод регистрирует кнопку из нашего списка, что она нажата и отпущена
    }

    public void onKeyPress(Key key) {//метод регистрирует кнопку из нашего списка, что она нажата и не отпущена
    }

    public void onTurn(int step) {//метод определяет, что происходит за ход
    }
}
















