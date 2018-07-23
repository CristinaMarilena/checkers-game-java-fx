package example.model;

import static example.ui.CheckersApp.SQUARE_SIZE;

public class Util {

    /**
     * @param pixel
     * @return matrix position from Pawn pixel position
     * It's either line or column in the matrix
     */
    static int toMatrixPosition(double pixel) {
        return (int) (pixel + SQUARE_SIZE / 2) / SQUARE_SIZE;
    }
}
