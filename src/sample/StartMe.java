package sample;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.StageStyle;
import sample.Engine.*;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class StartMe extends Engine {
    private final double HEIGHT = 500.0;
    private final double WIDTH = 500.00;
    private final double GAP = 40;
    private AnchorPane buttonList;
    private Pane root;
    private Stage primaryStage;
    private List<Button> buttons;
    public static final String JAVA_FX_LIB;
    public static final String GAME_ROOT;

    private static final String GAME2048_COMP;
    private static final String GAME_SAPPER_COMP;
    private static final String GAME_SNAKE_COMP;
    private static final String GAME_RACER_COMP;
    private static final String GAME_MOONLANDER_COMP;
    private static final String GAME_SPACE_INVADER_COMP;

    private static final String GAME2048_START;
    private static final String GAME_SAPPER_START;
    private static final String GAME_SNAKE_START;
    private static final String GAME_RACER_START;
    private static final String GAME_MOONLANDER_START;
    private static final String GAME_SPACE_INVADERS_GAME_START;

    public static final String START_ME_START;

    public static final String ENGINE_COMP;
    public static final String START_ME_COMP;

    public StartMe() {//конструктор
    }

    static {
        Path path = Paths.get("Games.iml");
        String s = path.toAbsolutePath().toString().substring(0,(path.toAbsolutePath().toString().length()-9));
        GAME_ROOT = s;
        JAVA_FX_LIB = s + "\\Frames\\javafx-sdk-18.0.2\\lib";
        GAME2048_COMP = "javac --module-path \"" + JAVA_FX_LIB + "\" --add-modules ALL-MODULE-PATH -d " + GAME_ROOT + "sample/Game2048/*.java";
        GAME_SAPPER_COMP = "javac --module-path \"" + JAVA_FX_LIB + "\" --add-modules ALL-MODULE-PATH -d " + GAME_ROOT + "sample/Sapper/*.java";
        GAME_SNAKE_COMP = "javac --module-path \"" + JAVA_FX_LIB + "\" --add-modules ALL-MODULE-PATH -d " + GAME_ROOT + " sample/Snake/*.java";
        GAME_RACER_COMP = "javac --module-path \"" + JAVA_FX_LIB + "\" --add-modules ALL-MODULE-PATH -d " + GAME_ROOT + " sample/Racer/*.java";
        GAME_MOONLANDER_COMP = "javac --module-path \"" + JAVA_FX_LIB + "\" --add-modules ALL-MODULE-PATH -d " + GAME_ROOT + " sample/MoonLander/*.java";
        GAME_SPACE_INVADER_COMP = "javac --module-path \"" + JAVA_FX_LIB + "\" --add-modules ALL-MODULE-PATH -d " + GAME_ROOT + " sample/SpaceInvaders/*.java";

        GAME2048_START = "java --module-path \"" + JAVA_FX_LIB + "\" --add-modules ALL-MODULE-PATH -classpath out/production/Engine sample.Game2048.Game2048";
        GAME_SAPPER_START = "java --module-path \"" + JAVA_FX_LIB + "\" --add-modules ALL-MODULE-PATH -classpath out/production/Engine sample.Sapper.Sapper";
        GAME_SNAKE_START = "java --module-path \"" + JAVA_FX_LIB + "\" --add-modules ALL-MODULE-PATH -classpath out/production/Engine sample.Snake.SnakeGame";
        GAME_RACER_START = "java --module-path \"" + JAVA_FX_LIB + "\" --add-modules ALL-MODULE-PATH -classpath out/production/Engine sample.Racer.RacerGame";
        GAME_MOONLANDER_START = "java --module-path \"" + JAVA_FX_LIB + "\" --add-modules ALL-MODULE-PATH -classpath out/production/Engine sample.MoonLander.MoonLanderGame";
        GAME_SPACE_INVADERS_GAME_START = "java --module-path \"" + JAVA_FX_LIB + "\" --add-modules ALL-MODULE-PATH -classpath out/production/Engine sample.SpaceInvaders.SpaceInvadersGame";

        START_ME_START = "java --module-path \"" + JAVA_FX_LIB + "\" --add-modules ALL-MODULE-PATH -classpath out/production/Engine sample.StartMe";

        ENGINE_COMP = "javac --module-path \"" + JAVA_FX_LIB + "\" --add-modules ALL-MODULE-PATH -d out/production/Engine sample/Engine/*.java";
        START_ME_COMP = "javac --module-path \"" + JAVA_FX_LIB + "\" --add-modules ALL-MODULE-PATH -d out/production/Engine sample/StartMe.java";
    }

    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage; //создаем главное окно
        Scene scene = new Scene(this.createContent());
        primaryStage.setTitle("Главное меню");
        primaryStage.setResizable(false);//нельзя растягивать и сжимать окно
        primaryStage.initStyle(StageStyle.TRANSPARENT);//устанавливаем стиль окна с прозрачным фоном и без декораций
        scene.setFill(Color.TRANSPARENT);//устанавливаем стиль окна с прозрачным фоном и без декораций
        primaryStage.setScene(scene);//помещаем в окно сцену(контейнер со всем)
        primaryStage.show();//делаем видимым окно

        buttons.get(0).setOnAction(new EventHandler<javafx.event.ActionEvent>() {
            @Override
            public void handle(javafx.event.ActionEvent actionEvent) {
                try {
                    Runtime.getRuntime().exec(ENGINE_COMP);
                    Runtime.getRuntime().exec(GAME2048_COMP);
                    Runtime.getRuntime().exec(GAME2048_START);
                    System.exit(0);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        buttons.get(1).setOnAction(new EventHandler<javafx.event.ActionEvent>() {
            @Override
            public void handle(javafx.event.ActionEvent actionEvent) {
                try {
                    Runtime.getRuntime().exec(ENGINE_COMP);
                    Runtime.getRuntime().exec(GAME_SAPPER_COMP);
                    Runtime.getRuntime().exec(GAME_SAPPER_START);
                    System.exit(0);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        buttons.get(2).setOnAction(new EventHandler<javafx.event.ActionEvent>() {
            @Override
            public void handle(javafx.event.ActionEvent actionEvent) {
                try {
                    Runtime.getRuntime().exec(ENGINE_COMP);
                    Runtime.getRuntime().exec(GAME_SNAKE_COMP);
                    Runtime.getRuntime().exec(GAME_SNAKE_START);
                    System.exit(0);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        buttons.get(3).setOnAction(new EventHandler<javafx.event.ActionEvent>() {
            @Override
            public void handle(javafx.event.ActionEvent actionEvent) {
                try {
                    Runtime.getRuntime().exec(ENGINE_COMP);
                    Runtime.getRuntime().exec(GAME_RACER_COMP);
                    Runtime.getRuntime().exec(GAME_RACER_START);
                    System.exit(0);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        buttons.get(4).setOnAction(new EventHandler<javafx.event.ActionEvent>() {
            @Override
            public void handle(javafx.event.ActionEvent actionEvent) {
                try {
                    Runtime.getRuntime().exec(ENGINE_COMP);
                    Runtime.getRuntime().exec(GAME_MOONLANDER_COMP);
                    Runtime.getRuntime().exec(GAME_MOONLANDER_START);
                    System.exit(0);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        buttons.get(5).setOnAction(new EventHandler<javafx.event.ActionEvent>() {
            @Override
            public void handle(javafx.event.ActionEvent actionEvent) {
                try {
                    Runtime.getRuntime().exec(ENGINE_COMP);
                    Runtime.getRuntime().exec(GAME_SPACE_INVADER_COMP);
                    Runtime.getRuntime().exec(GAME_SPACE_INVADERS_GAME_START);
                    System.exit(0);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        buttons.get(6).setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                System.exit(0);
            }
        });
    }


    private void createBorderImage() {//метод, который рисует рамку(интерактивный экран)
        InputStream inputStream = Engine.class.getResourceAsStream("screenNew.png");//в входящий поток помещаем картинку по адресу, метод класса преобразует адрес в поток
        Image image = new Image(inputStream);//создаем новое изображение с URL inputStream
        ImageView imageView = new ImageView(image);//ImageView - обеспечивает отображение изображений Image
        imageView.setFitWidth((double) (WIDTH + 250));//устанавливаем ширину, в рамках которой загруженное изображение может меняться
        imageView.setFitHeight((double) (HEIGHT + 110 + 140));//устанавливаем высоту, в рамках которой загруженное изображение может меняться
        this.root.getChildren().add(imageView);//в корневую панель добавляем загруженную картинку
    }

    private Parent createContent() { //Parent обрабатывает все операции графики сцены, метод вернет готовую корневую панель
        root = new Pane(); //инициализируем корневую панель
        this.root.setPrefSize((double) (WIDTH + 250), (double) (HEIGHT + 110 + 140));//устанавливаем предпочтительную ширину и высоту для корневой панели
        createBackImage();
        this.createBorderImage();//создаем рисунок для рамки
        this.createButtons();//создаем кнопки
        //createPathFields();
        root.setBackground(new Background(new BackgroundFill[]{new BackgroundFill(Color.TRANSPARENT, CornerRadii.EMPTY, Insets.EMPTY)}));
        //делаем, чтобы фон корневой панели не было видно
        return this.root;//возвращаем корневую панель
    }

    private void createButtons() {
        buttons = new ArrayList<>();
        buttonList = new AnchorPane();
        buttons.add(0, new Button("\u2795 2048"));
        buttons.add(1, new Button("\uD83D\uDCA3 Sapper"));
        buttons.add(2, new Button("\uD83D\uDC0D Snake"));
        buttons.add(3, new Button("\uD83D\uDE97 Racer"));
        buttons.add(4, new Button("\uD83D\uDE80 Moon Lander"));
        buttons.add(5, new Button("\uD83D\uDC7D Space Invaders"));
        buttons.add(6, new Button("\uD83D\uDEAA Exit"));

        int i = 0;
        for (Button button : buttons) {
            AnchorPane.setRightAnchor(button, WIDTH / 2.0 + 50);
            AnchorPane.setLeftAnchor(button, WIDTH / 2.0 + 50);
            AnchorPane.setTopAnchor(button, (HEIGHT + 110 + 140) / 2.0 - (double) buttons.size() * GAP / 2.0 + i * GAP);
            buttonList.getChildren().add(button);
            i++;
        }
        root.getChildren().add(buttonList);
    }

    private void createBackImage() {//метод, который рисует рамку(интерактивный экран)
        InputStream inputStream = Engine.class.getResourceAsStream("back.png");//в входящий поток помещаем картинку по адресу, метод класса преобразует адрес в поток
        Image image = new Image(inputStream);//создаем новое изображение с URL inputStream
        ImageView imageView = new ImageView(image);//ImageView - обеспечивает отображение изображений Image
        imageView.setLayoutX(11);
        imageView.setLayoutY(0);
        imageView.setFitWidth((double) (WIDTH + 240));//устанавливаем ширину, в рамках которой загруженное изображение может меняться
        imageView.setFitHeight((double) (HEIGHT + 250));//устанавливаем высоту, в рамках которой загруженное изображение может меняться
        this.root.getChildren().add(imageView);//в корневую панель добавляем загруженную картинку
    }

    /*private void createPathFields() {
        Text path = new Text("Path to JavaFx:");
        path.setLayoutX(115);
        path.setLayoutY(545);
        path.setFont(Font.font("verdana", FontWeight.NORMAL, FontPosture.REGULAR, 15));
        path.setFill(Color.ORANGERED);
        path.setStroke(Color.BLACK);
        path.setStrokeWidth(0.5);
        TextField javaFXPath = new TextField(JAVA_FX_LIB);
        javaFXPath.setMinWidth(350);
        javaFXPath.setLayoutX(230);
        javaFXPath.setLayoutY(530);

        Text gamePath = new Text("Path to Game:");
        gamePath.setLayoutX(120);
        gamePath.setLayoutY(585);
        gamePath.setFont(Font.font("verdana", FontWeight.NORMAL, FontPosture.REGULAR, 15));
        gamePath.setFill(Color.ORANGERED);
        gamePath.setStroke(Color.BLACK);
        gamePath.setStrokeWidth(0.5);
        TextField gameRoot = new TextField(GAME_ROOT);
        gameRoot.setMinWidth(350);
        gameRoot.setLayoutX(230);
        gameRoot.setLayoutY(570);

        root.getChildren().add(path);
        root.getChildren().add(gamePath);
        root.getChildren().add(javaFXPath);
        root.getChildren().add(gameRoot);
    }*/
}

