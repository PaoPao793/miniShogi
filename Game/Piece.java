package Game;

import java.util.ArrayList;

public abstract class Piece {
    private String name;
    private boolean isPromoted;
    
    public Piece(String name) {
        this.name = name;
        this.isPromoted = false;
    }

    public String toString() {
        return name;
    }

    boolean isPromoted() {
        return isPromoted;
    }

    void promote() {
        isPromoted = true;
        name = "+" + name;
    }

    public abstract boolean canMove(Board board, Position start, Position end);
    public abstract boolean canPromote();
    public abstract ArrayList<Position> allMoves();
    public abstract void possibleMoves(Board board, Position start);

    boolean isGote() {
        String tempName; 
        if (name.length() != 1) {
            tempName = name.substring(1);
        } else {
            tempName = name;
        }

        if (Character.isLowerCase(tempName.charAt(0))) {
            return true;
        } 
        
        return false;
    }

    boolean belongsTo(Player player) {
        String tempName; 
        if (name.length() != 1) {
            tempName = name.substring(1);
        } else {
            tempName = name;
        }

        if (player.getName().equals("Sente") && Character.isUpperCase(tempName.charAt(0))) {
            return true;
        } else if (player.getName().equals("Gote") && Character.isLowerCase(tempName.charAt(0))) {
            return true;
        } else {
            return false;
        }
    }

    void getCaptued() {
        if (isGote()) {
            name = String.valueOf(Character.toUpperCase(name.charAt(0)));
        } else {
            name = String.valueOf(Character.toLowerCase(name.charAt(0)));
        }
    }

    void dePromote() {
        if (isPromoted) {
            isPromoted = false;
            name = name.substring(1);
        }
    }
}
    
