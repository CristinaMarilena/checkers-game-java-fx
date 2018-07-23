package example.model;

/**
 * Move model that has a type and a pawn
 */
public class Move {

    private MoveType type;
    private Pawn pawn;

    public Move(MoveType type) {
        this(type, null);
    }

    public Move(MoveType type, Pawn pawn) {
        this.type = type;
        this.pawn = pawn;
    }

    public MoveType getType() {
        return type;
    }

    public Pawn getPawn() {
        return pawn;
    }

    @Override
    public String toString() {
        return "Move{" +
                "type=" + type +
                ", pawn=" + pawn +
                '}';
    }
}