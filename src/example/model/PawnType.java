package example.model;

public enum PawnType {

    RED_PAWN(1), WHITE_PAWN(-1), RED_KING, WHITE_KING;

    public final int pawnWay;

    PawnType() {
        this.pawnWay = 0;
    }

    PawnType(int pawnWay) {
        this.pawnWay = pawnWay;
    }
}