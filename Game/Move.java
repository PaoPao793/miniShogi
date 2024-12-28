package Game;

public class Move {
    private Position startPosition;
    private Position endPosition;
    private boolean isDrop;
    private String droppedPiece; // Used if isDrop is true. This should be the piece symbol.
    private boolean promote;

    // Constructor for a regular move
    public Move(Position startPosition, Position endPosition, boolean promote) {
        this.startPosition = startPosition;
        this.endPosition = endPosition;
        this.isDrop = false; 
        this.promote = promote;
    }

    // Constructor for a drop move
    public Move(String droppedPiece, Position endPosition) {
        this.droppedPiece = droppedPiece;
        this.endPosition = endPosition;
        isDrop = true; // This is a drop move
        promote = false; // Promotion doesn't apply to drops
    }

    // Getters
    public Position getStartPosition() {
        return startPosition;
    }

    public Position getEndPosition() {
        return endPosition;
    }

    public boolean isDrop() {
        return isDrop;
    }

    public String getDroppedPiece() {
        return droppedPiece;
    }

    public boolean isPromote() {
        return promote;
    }

    // String representation for debugging
    @Override
    public String toString() {
        if (isDrop) {
            return "Drop " + droppedPiece + " to " + endPosition;
        } else {
            return "Move from " + startPosition + " to " + endPosition + (promote ? " with promotion" : "");
        }
    }
}
