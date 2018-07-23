package example.model;

import example.ui.CheckersApp;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;

/**
 * A stackpane's parent will resize the stackpane within the stackpane's resizable range during layout.
 * By default the stackpane computes this range based on its content as outlined in the table below.
 * ( javafx.scene.layout Class StackPane official documentation)
 *
 * A Pawn extends StackPane
 * A pawn has a pawn type, coordinates and specific image
 */
import static example.model.Util.toMatrixPosition;
import static example.ui.CheckersApp.SQUARE_SIZE;

public class Pawn extends StackPane {

    private PawnType type;

    private Image redPawnImage;
    private Image whitePawnImage;

    private double mouseX, mouseY;
    private double oldX, oldY;

    public PawnType getType() {
        return type;
    }

    public double getOldX() {
        return oldX;
    }

    public double getOldY() {
        return oldY;
    }

    public Pawn(PawnType type, int x, int y) {
        this.type = type;
        this.redPawnImage = new Image(getClass().getResourceAsStream(CheckersApp.RED_PAWN));
        this.whitePawnImage = new Image(getClass().getResourceAsStream(CheckersApp.WHITE_PAWN));

        move(x, y);

        //we create an ImageView in order to add the specific image resource
        ImageView imageView = (type == PawnType.RED_PAWN) ? new ImageView(this.redPawnImage)
                : new ImageView(this.whitePawnImage);

        setImageView(imageView);

        setOnMouseDragged(e -> {
            relocate(e.getSceneX() - mouseX + oldX, e.getSceneY() - mouseY + oldY);
        });

        setOnMousePressed(e -> {
            mouseX = e.getSceneX();
            mouseY = e.getSceneY();
        });
    }

    /**
     * The method that make a pawn a king of it reached a specific line of the gameboard
     * @param color
     */
    public void makePawnKing(String color) {
        this.redPawnImage = new Image(getClass().getResourceAsStream(CheckersApp.RED_KING));
        this.whitePawnImage = new Image(getClass().getResourceAsStream(CheckersApp.WHITE_KING));
        this.type = (color.equals("RED_PAWN")) ? PawnType.RED_KING : PawnType.WHITE_KING;

        ImageView imageView = (type == PawnType.RED_KING) ? new ImageView(this.redPawnImage)
                : new ImageView(this.whitePawnImage);

        setImageView(imageView);
    }

    public void move(int x, int y) {
        oldX = x * SQUARE_SIZE;
        oldY = y * SQUARE_SIZE;
        relocate(oldX, oldY);
    }

    public void dontMovePawn() {
        relocate(oldX, oldY);
    }

    /**
     * Sets a specific image from the resources directory
     * @param imageView
     */
    public void setImageView(ImageView imageView){

        imageView.setX(SQUARE_SIZE * 0.4);
        imageView.setY(SQUARE_SIZE * 0.4);
        imageView.setTranslateX((SQUARE_SIZE - SQUARE_SIZE * 0.4 * 2) / 2);
        imageView.setTranslateY((SQUARE_SIZE - SQUARE_SIZE * 0.4 * 2) / 2);

        getChildren().addAll(imageView);
    }

    boolean isRedKing(int y){
        if ((y == 7) && this.type.equals(PawnType.RED_PAWN))
            return true;
        return false;
    }

    boolean isWhiteKing(int y){
        if ((y == 0) && this.type.equals(PawnType.WHITE_PAWN))
            return true;
        return false;
    }

    @Override
    public String toString() {
        return "Pawn{" +
                "type=" + type +
                "at[" + toMatrixPosition(oldX) + "][" + toMatrixPosition(oldY) + "] " +
                '}';
    }
}