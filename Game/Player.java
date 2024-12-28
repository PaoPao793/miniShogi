package Game;

import java.util.*;

public class Player {
    private String name; 
    private ArrayList<Piece> captured;

    Player(String name) {
        this.name = name;
        captured = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public ArrayList<Piece> getCaptured() {
        return captured;
    }

    public void capturePiece(Piece p) {
        captured.add(p);
    }

    public void rmCaptured(Piece p) {
        captured.remove(p);
    }

    public void printCaptured() {
        System.out.print("\nCaptures " + name + ": ");
        for (Piece p : captured) {
            System.out.print(p + " ");
        }

        if (name.equals("Gote")) {
            System.out.println();
        }
        
    }

    public boolean isPiecePromotable(Position pos) {
        int row = pos.getRow();

        if (name.equals("Sente") && row == 4) {
            return true;
        } else {
            return (name.equals("Gote") && row == 0);
        }
    }

    public Piece capturedPieces(String name) {
        for (Piece p : captured) {
            String str = p.toString().toLowerCase();

            if (name.charAt(0) == str.charAt(0)) {
                return p;
            }
        }

        return null;
    }

    // returns an array which includes all the possible moves that a player can make 
    ArrayList<Position> allPossMoves(Board board) {
        Set<Position> set = new LinkedHashSet<>();
        ArrayList<Position> allMoves; 

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                Piece lookingAtPiece = board.getPiece(i, j);
                Position pos = new Position(i, j);

                if (lookingAtPiece == null) {
                    continue;
                }

                if (lookingAtPiece.belongsTo(this)) {
                    lookingAtPiece.possibleMoves(board, pos);
                    allMoves = lookingAtPiece.allMoves();
                    set.addAll(allMoves);
                }
            }
        }

        return new ArrayList<>(set);
    }

    void printWin(String reason) {
        System.out.println(this.name+" player wins."+reason);
    }
}
