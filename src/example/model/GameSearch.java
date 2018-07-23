package example.model;


import java.util.LinkedList;

import static example.ui.CheckersApp.HEIGHT;
import static example.ui.CheckersApp.WIDTH;

/**
 * GameSearch class implements a barbaric algorithm that returns all the possible moves on the gameboard
 * There is a lot of room for optimization
 * The algorithm principle is that it looks for every possible move of every pawn on the gameboard and doesn't
 * look for unreachable solutions outside the board matrix
 *
 * All the moves(either step or jump) are added to a possibleMoves list
 */
public class GameSearch {

     static LinkedList<Move> getAllPossibleMoves(BoardSquare[][] board) {

         LinkedList<Move> possibleMoves = new LinkedList<>();

        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {

                if (board[i][j].getPawn() != null) {
                    possibleMoves.addAll(allStepMoves(i, j, board));
                    possibleMoves.addAll(allJumpMoves(i, j, board));
                }
            }
        }
        return possibleMoves;
    }

     static LinkedList<Move> allStepMoves(int x, int y, BoardSquare[][] board) {

        LinkedList<Move> possibleStepMoves = new LinkedList<>();

        if (board[x][y].getPawn().getType().equals(PawnType.WHITE_PAWN)) {

            if (x - 1 >= 0 && y - 1 >= 0) {
                if (!board[x - 1][y - 1].hasPawn())
                    possibleStepMoves.add(new Move(MoveType.STEP, board[x][y].getPawn()));
            }

            if (x - 1 >= 0 && y + 1 <= 7) {
                if (!board[x - 1][y + 1].hasPawn())
                    possibleStepMoves.add(new Move(MoveType.STEP, board[x][y].getPawn()));
            }
        }

        if (board[x][y].getPawn().getType().equals(PawnType.RED_PAWN)) {

            if (x + 1 <= 7 && y - 1 >= 0) {
                if (!board[x + 1][y - 1].hasPawn())
                    possibleStepMoves.add(new Move(MoveType.STEP, board[x][y].getPawn()));
            }

            if (x + 1 <= 7 && y + 1 <= 7) {
                if (!board[x + 1][y + 1].hasPawn())
                    possibleStepMoves.add(new Move(MoveType.STEP, board[x][y].getPawn()));
            }
        }

        if (board[x][y].getPawn().getType().equals(PawnType.WHITE_KING) || board[x][y].getPawn().getType().equals(PawnType.RED_KING)) {

            if (x - 1 >= 0 && y - 1 >= 0) {
                if (!board[x - 1][y - 1].hasPawn())
                    possibleStepMoves.add(new Move(MoveType.STEP, board[x][y].getPawn()));
            }

            if (x - 1 >= 0 && y + 1 <= 7) {
                if (!board[x - 1][y + 1].hasPawn())
                    possibleStepMoves.add(new Move(MoveType.STEP, board[x][y].getPawn()));
            }

            if (x - 1 >= 0 && y - 1 >= 0) {
                if (!board[x - 1][y - 1].hasPawn())
                    possibleStepMoves.add(new Move(MoveType.STEP, board[x][y].getPawn()));
            }

            if (x - 1 >= 0 && y + 1 <= 7) {
                if (!board[x - 1][y + 1].hasPawn())
                    possibleStepMoves.add(new Move(MoveType.STEP, board[x][y].getPawn()));
            }
        }
        return possibleStepMoves;
    }

     static LinkedList<Move> allJumpMoves(int x, int y, BoardSquare[][] board) {

        LinkedList<Move> possibleJumpMoves = new LinkedList<>();

        if (board[x][y].getPawn().getType().equals(PawnType.WHITE_PAWN)) {

            if (x - 2 >= 0 && y - 2 >= 0) {
                if (!board[x - 2][y - 2].hasPawn() && (board[x - 1][y - 1].hasPawn()))
                    if((board[x - 1][y - 1].getPawn().getType().equals(PawnType.RED_PAWN)
                            || board[x - 1][y - 1].getPawn().getType().equals(PawnType.RED_KING)))
                        possibleJumpMoves.add(new Move(MoveType.JUMP, board[x][y].getPawn()));
            }

            if (x - 2 >= 0 && y + 2 <= 7) {
                if (!board[x - 2][y + 2].hasPawn() && (board[x - 1][y +1].hasPawn()))
                    if((board[x - 1][y + 1].getPawn().getType().equals(PawnType.RED_PAWN)
                            || board[x - 1][y + 1].getPawn().getType().equals(PawnType.RED_KING)))
                        possibleJumpMoves.add(new Move(MoveType.JUMP, board[x][y].getPawn()));
            }
        }

        if (board[x][y].getPawn().getType().equals(PawnType.RED_PAWN)) {

            if (x + 2 <= 7 && y - 2 >= 0) {
                if (!board[x + 2][y - 2].hasPawn() && (board[x + 1][y - 1].hasPawn()))
                    if((board[x + 1][y - 1].getPawn().getType().equals(PawnType.WHITE_PAWN)
                            || board[x + 1][y - 1].getPawn().getType().equals(PawnType.WHITE_KING)))
                        possibleJumpMoves.add(new Move(MoveType.JUMP, board[x][y].getPawn()));
            }

            if (x + 2 <= 7 && y + 2 <= 7) {
                if (!board[x + 2][y + 2].hasPawn() && (board[x + 1][y + 1].hasPawn()))
                    if((board[x + 1][y + 1].getPawn().getType().equals(PawnType.WHITE_PAWN)
                            || board[x + 1][y + 1].getPawn().getType().equals(PawnType.WHITE_KING)))
                        possibleJumpMoves.add(new Move(MoveType.JUMP, board[x][y].getPawn()));
            }
        }

        if (board[x][y].getPawn().getType().equals(PawnType.WHITE_KING)) {

            if (x - 2 > -0) {
                if (y - 2 >= 0) {
                    if (!board[x - 2][y - 2].hasPawn() && (board[x - 1][y - 1].hasPawn()))
                        if((board[x - 1][y - 1].getPawn().getType().equals(PawnType.RED_PAWN)
                                || board[x - 1][y - 1].getPawn().getType().equals(PawnType.RED_KING)))
                            possibleJumpMoves.add(new Move(MoveType.JUMP, board[x][y].getPawn()));
                }

                if (y + 2 <= 7) {
                    if (!board[x - 2][y + 2].hasPawn() && (board[x - 1][y + 1].hasPawn()))
                        if((board[x - 1][y + 1].getPawn().getType().equals(PawnType.RED_PAWN)
                                || board[x - 1][y + 1].getPawn().getType().equals(PawnType.RED_KING)))
                            possibleJumpMoves.add(new Move(MoveType.JUMP, board[x][y].getPawn()));
                }
            }
        }

        if (board[x][y].getPawn().getType().equals(PawnType.RED_KING)) {

            if (x + 2 <= 7) {
                if (y - 2 >= 0) {
                    if (!board[x + 2][y - 2].hasPawn() && (board[x + 1][y - 1].hasPawn()))
                        if((board[x + 1][y - 1].getPawn().getType().equals(PawnType.WHITE_PAWN)
                                || board[x + 1][y - 1].getPawn().getType().equals(PawnType.WHITE_KING)))
                            possibleJumpMoves.add(new Move(MoveType.JUMP, board[x][y].getPawn()));
                }

                if (y + 2 <= 7) {
                    if (!board[x + 2][y + 2].hasPawn() && (board[x + 1][y + 1].hasPawn()))
                        if((board[x + 1][y + 1].getPawn().getType().equals(PawnType.WHITE_PAWN)
                                || board[x + 1][y + 1].getPawn().getType().equals(PawnType.WHITE_KING)))
                            possibleJumpMoves.add(new Move(MoveType.JUMP, board[x][y].getPawn()));
                }
            }
        }
        return possibleJumpMoves;
    }
}
