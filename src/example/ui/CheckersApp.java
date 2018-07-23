package example.ui;

import example.model.GameBoard;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class CheckersApp extends Application {

    public static final int WIDTH = 8;
    public static final int HEIGHT = 8;
    public static final String RED_PAWN = "/redpwan2_50x50.png";
    public static final String WHITE_PAWN = "/whitepawn_50x50.png";
    public static final String RED_KING = "/redking.png";
    public static final String WHITE_KING = "/whiteking.png";
    public static final String STATUS_BACK = "/statusBack.png";
    public static final int SQUARE_SIZE = 80;

    private Group boardSquaresGroup;
    private Group pieceGroup;
    private GameBoard gameBoard;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Pane root = new Pane();

        root.setPrefSize(WIDTH * SQUARE_SIZE, HEIGHT * SQUARE_SIZE + 30);
        boardSquaresGroup = new Group();
        pieceGroup = new Group();
        gameBoard = new GameBoard();

        boardSquaresGroup.setLayoutY(30);
        pieceGroup.setLayoutY(30);

        gameBoard.populateBoard(boardSquaresGroup, pieceGroup);

        root.getChildren().addAll(boardSquaresGroup, pieceGroup);
        root.getChildren().add(myMenuBar(primaryStage));

        Scene scene = new Scene(root);
        primaryStage.setTitle("CheckersApp");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    static MenuBar myMenuBar(Stage primaryStage) {
        MenuBar menuBar = new MenuBar();
        menuBar.prefWidthProperty().bind(primaryStage.widthProperty());

        // File menu - new, exit
        Menu fileMenu = new Menu("Options");
        MenuItem newMenuItem = new MenuItem("New");
        newMenuItem.setOnAction(actionEvent ->{
            System.out.println( "Restarting app!" );
            primaryStage.close();
            Platform.runLater(() -> {
                try {
                    new CheckersApp().start(new Stage());
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Cannot restart javaFX application");
                }
            });
        });

        MenuItem exitMenuItem = new MenuItem("Exit");
        exitMenuItem.setOnAction(actionEvent -> Platform.exit());

        fileMenu.getItems().addAll(newMenuItem,
                new SeparatorMenuItem(), exitMenuItem);

        menuBar.getMenus().addAll(fileMenu);
        return menuBar;
    }
}