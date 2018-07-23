package example.model;


import javafx.scene.Group;

import java.util.LinkedList;

import static example.model.Util.toMatrixPosition;
import static example.ui.CheckersApp.HEIGHT;
import static example.ui.CheckersApp.WIDTH;

/**
 * GameBoard class is created to memorize the locations of the pawns and kings before and after every move of a player
 * It also decides based on some conditionals if the moves are valid
 * The gameboard consists of 64 squares, alternating between 32 black and 32 white squares.
 */
public class GameBoard {

    private BoardSquare[][] gameboard;

    static Boolean whiteTurn;
    static Boolean isThisAJumpPawn = false;
    static Boolean atLeastOneJump = false;

    private int whitePawnsLeft;
    private int redPawnsLeft;

    public GameBoard() {
        gameboard = new BoardSquare[WIDTH][HEIGHT];
        whiteTurn = true;
    }

    /**
     * Populates the gameboard with the black&white squares and pawns
     * Each player begins the game with 12 colored pawns. (Typically, one set of pawns is white and the other red)
     * The first player is the white one
     * Players then alternate moves by changing the whiteTurn value
     *
     * @param boardSquareGroup represent the javaFX group that keeps and displays the black&white squares of the gameboard
     * @param pawnsGroup       represent the javaFX group layered on boardSquareGroup that keeps and displays the pawns of the game
     */
    public void populateBoard(Group boardSquareGroup, Group pawnsGroup) {
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {

                Pawn pawn = null;

                BoardSquare boardSquare = new BoardSquare((x + y) % 2 == 0, x, y);
                gameboard[x][y] = boardSquare;

                //initialize the red pawns and their position on the gameboard
                if (y <= 2 && (x + y) % 2 == 0) {
                    pawn = pawnFactory(PawnType.RED_PAWN, x, y, pawnsGroup);
                }

                //initialize the white pawns and their position on the gameboard
                if (y >= 5 && (x + y) % 2 == 0) {
                    pawn = pawnFactory(PawnType.WHITE_PAWN, x, y, pawnsGroup);
                }

                // We add to a boardSquare only not null pawns
                if (pawn != null) {
                    boardSquare.setPawn(pawn);
                    pawnsGroup.getChildren().add(pawn);
                }

                boardSquareGroup.getChildren().add(boardSquare);
            }
        }
        whitePawnsLeft = 12;
        redPawnsLeft = 12;
    }

    /**
     * @param type       the wanted type of the pawn(RED, WHITE)
     * @param x          initial x location
     * @param y          initial y location
     * @param pawnsGroup javaFx group that keeps all the pawns no matter the type
     * @return Pawn of a specific type(RED,WHITE) that has associated an setOnMouseReleased action
     * The setOnMouseReleased defines the implementation behind releasing a pawn on the gameboard
     */

    private Pawn pawnFactory(PawnType type, int x, int y, Group pawnsGroup) {

        Pawn pawn = new Pawn(type, x, y);

        pawn.setOnMouseReleased(e -> {
            Move move;
            isThisAJumpPawn = false;
            atLeastOneJump = false;

            //new x and y values representing the matrix values, not the pixels
            int newX = toMatrixPosition(pawn.getLayoutX());
            int newY = toMatrixPosition(pawn.getLayoutY());

            //if the new position is not 'out' of the gameboard or it is the right player's turn
            //we are looking for a possible move of the selected pawn and the new position
            //if otherwise we do not do any valid move
            if (!yourTurn(pawn) || (newX < 0 || newY < 0 || newX >= WIDTH || newY >= HEIGHT)) {
                move = new Move(MoveType.INVALID);
            } else {
                move = getPossibleMoveForPiece(pawn, newX, newY);
            }

            //this list was created for the whole purpose of determining all the valid moves on the gameboard
            LinkedList<Move> possibleMoves = GameSearch.getAllPossibleMoves(gameboard);

            //failed attempt in trying to make the player do a jump if any is available
            String player = pawn.getType().equals(PawnType.RED_PAWN) || pawn.getType().equals(PawnType.RED_KING) ? "RED" : "WHITE";
            System.out.println("Possible movable pawns for player " + pawn.getType());
            possibleMoves.forEach(possMove -> {

                if (player.equals("RED") && (possMove.getPawn().getType().equals(PawnType.RED_PAWN) ||
                        possMove.getPawn().getType().equals(PawnType.RED_KING))) System.out.println(possMove);

                if (player.equals("WHITE") && (possMove.getPawn().getType().equals(PawnType.WHITE_PAWN) ||
                        possMove.getPawn().getType().equals(PawnType.WHITE_KING))) System.out.println(possMove);

                if (possMove.getType().equals(MoveType.JUMP) && pawn.getType().equals(possMove.getPawn().getType())) {
                    if (possMove.getPawn() == pawn) {
                        isThisAJumpPawn = true;
                    }
                    atLeastOneJump = true;
                }
            });

            pawnAction(move, pawn, newX, newY, pawnsGroup);
        });

        return pawn;
    }

    /**
     * This method tries to create a Move based on the new supposed location of the pawn
     * It returns an invalid Move if the pawn tries to be moved to an occupied position or if the new position will be not
     * on the black squares
     *
     * @param pawn
     * @param newX
     * @param newY
     * @return
     */

    private Move getPossibleMoveForPiece(Pawn pawn, int newX, int newY) {
        if ((newX + newY) % 2 != 0 || gameboard[newX][newY].hasPawn() || whitePawnsLeft == 0 || redPawnsLeft == 0) {
            return new Move(MoveType.INVALID);
        }

        int x0 = toMatrixPosition(pawn.getOldX());
        int y0 = toMatrixPosition(pawn.getOldY());
        int jumpedOverPawnX = x0 + (newX - x0) / 2;
        int jumpedOverPawnY = y0 + (newY - y0) / 2;

        //this are the simple formulas to validate the new x and y coordinates of the pawn
        boolean pawnStepCondition = Math.abs(newX - x0) == 1 && newY - y0 == pawn.getType().pawnWay;
        boolean kingStepCondition = Math.abs(newX - x0) == 1 && (newY - y0 == 1 || newY - y0 == -1);
        boolean pawnJumpCondition = Math.abs(newX - x0) == 2 && newY - y0 == pawn.getType().pawnWay * 2;
        boolean kingJumpCondition = Math.abs(newX - x0) == 2 && (newY - y0 == 2 || newY - y0 == -2);

        //if the conditions are met, the pawn takes a step or a jump
        //for a jump, the move memorises the pawn the is trying to pe jumped over
        if (pawn.getType().equals(PawnType.RED_PAWN) || pawn.getType().equals(PawnType.WHITE_PAWN)
                ? pawnStepCondition : kingStepCondition) {
            return new Move(MoveType.STEP);
        } else if (pawn.getType().equals(PawnType.RED_KING) || pawn.getType().equals(PawnType.WHITE_KING)
                ? kingJumpCondition : pawnJumpCondition) {
            if (gameboard[jumpedOverPawnX][jumpedOverPawnY].hasPawn() &&
                    gameboard[jumpedOverPawnX][jumpedOverPawnY].getPawn().getType() != pawn.getType()) {
                return new Move(MoveType.JUMP, gameboard[jumpedOverPawnX][jumpedOverPawnY].getPawn());
            }
        }
        return new Move(MoveType.INVALID);
    }

    /**
     *
     * @param pawn
     * @return the turn of the players based on the pawn they select and the whiteTurn parameter
     */
    public Boolean yourTurn(Pawn pawn) {
        if (((pawn.getType().equals(PawnType.WHITE_PAWN) || pawn.getType().equals(PawnType.WHITE_KING)) && whiteTurn) ||
                (pawn.getType().equals(PawnType.RED_PAWN) || pawn.getType().equals(PawnType.RED_KING)) && !whiteTurn)
            return true;
        return false;
    }

    /**
     * Method that actually implements the specific moves of a pawn(Not a move, a step, a jump)
     * For an invalid move the pawn remains on the same square
     * @param move      The move we want to make
     * @param pawn      the selected pawn on the gameboard
     * @param newX      new xLocation
     * @param newY      new yLocation
     * @param pawnsGroup javaFx group the keeps all the pawns on the gameboard
     */
    public void pawnAction(Move move, Pawn pawn, int newX, int newY, Group pawnsGroup) {

        int initialXPosition = toMatrixPosition(pawn.getOldX());
        int initialYPosition = toMatrixPosition(pawn.getOldY());

        if (move.getType().equals(MoveType.STEP)) {
            step(pawn, newX, newY, initialXPosition, initialYPosition);
            return;
        }
        if (move.getType().equals(MoveType.JUMP)) {
            jump(pawn, newX, newY, initialXPosition, initialYPosition, move, pawnsGroup);
            return;
        }
        if (move.getType().equals(MoveType.INVALID)) {
            pawn.dontMovePawn();
            return;
        }
    }

    /**
     * For a step move, if is the case, the pawn may become king
     * Either way it takes a new position on the gameboard
     * Also, the turn of the players is changed
     * @param pawn
     * @param newX
     * @param newY
     * @param initialXPosition
     * @param initialYPosition
     */
    private void step(Pawn pawn, int newX, int newY, int initialXPosition, int initialYPosition){
        if (pawn.isRedKing(newY)) {
            pawn.makePawnKing("RED_PAWN");
        }
        if (pawn.isWhiteKing(newY)) {
            pawn.makePawnKing("WHITE_PAWN");
        }
        pawn.move(newX, newY);
        gameboard[initialXPosition][initialYPosition].setPawn(null);
        gameboard[newX][newY].setPawn(pawn);
        whiteTurn = !whiteTurn;

        System.out.println("A " + pawn.getType() + " has been moved from [" + initialXPosition + "][" + initialYPosition
                + "] to [" + newX + "][" + newY + "]");
    }

    /**
     * For a jump move, if is the case, the pawn may become king
     * Either way it takes a new position on the gameboard while it deletes the pawn over which it jumped
     * Also, the turn of the players is changed and for every jump, the other player's number of pawns goes down by one
     * @param pawn
     * @param newX
     * @param newY
     * @param initialXPosition
     * @param initialYPosition
     * @param move
     * @param pawnGroup
     */
    private void jump(Pawn pawn, int newX, int newY, int initialXPosition, int initialYPosition, Move move, Group pawnGroup){
        if (pawn.isRedKing(newY)) {
            pawn.makePawnKing("RED_PAWN");
            System.out.println("We have a red king!");
        }
        if (pawn.isWhiteKing(newY)) {
            pawn.makePawnKing("WHITE_PAWN");
            System.out.println("We have a white king!");
        }
        pawn.move(newX, newY);
        gameboard[initialXPosition][initialYPosition].setPawn(null);
        gameboard[newX][newY].setPawn(pawn);
        Pawn otherPawn = move.getPawn();
        gameboard[toMatrixPosition(otherPawn.getOldX())][toMatrixPosition(otherPawn.getOldY())].setPawn(null);
        pawnGroup.getChildren().remove(otherPawn);

        whitePawnsLeft = pawn.getType().equals(PawnType.WHITE_PAWN) || pawn.getType().equals(PawnType.WHITE_KING)
                ? --whitePawnsLeft : whitePawnsLeft;
        redPawnsLeft = pawn.getType().equals(PawnType.RED_PAWN) || pawn.getType().equals(PawnType.RED_KING)
                ? --redPawnsLeft : redPawnsLeft;

        System.out.println("WHITE PAWNS LEFT: " + whitePawnsLeft);
        System.out.println("RED PAWNS LEFT: " + redPawnsLeft);

        if (whitePawnsLeft == 0) System.out.println("White wins");
        if (redPawnsLeft == 0) System.out.println("Red wins");

        System.out.println("A " + pawn.getType() + " has jumped from [" + initialXPosition + "][" + initialYPosition
                + "] to [" + newX + "][" + newY + "]");

        whiteTurn = !whiteTurn;
    }
}
