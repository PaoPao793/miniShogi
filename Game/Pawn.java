package Game;

import java.util.ArrayList;

public class Pawn extends Piece {
    private ArrayList<Position> moves = new ArrayList<>();
    private static final int[][] promotedLower = new int[][]{{0,-1},{1,0},{0,1},{-1,1},{-1,0},{-1,-1}};
    private static final int[][] promotedUpper = new int[][]{{1,-1},{0,-1},{1,1},{1,0},{-1,0},{0,1}}; 

    public Pawn(String name) {
        super(name);
    }

    @Override
    public boolean canMove(Board board, Position start, Position end) {
        possibleMoves(board, start);
        for(Position pos : moves) {
            if (end.equals(pos)) {
                return true;
            }
        }
        return false;
    }

    public void possibleMoves(Board board,Position start) {
        moves.clear(); 
        if(!isPromoted()) {
            Position p;
            if (isGote()) {
                p = new Position(start.getRow() - 1, start.getCol());
            } else {
                p = new Position(start.getRow() + 1, start.getCol());
            }
            moves.add(p);
        } else {
            int[][] locations;
            if (isGote()) {
                locations = promotedLower;
            } else {
                locations = promotedUpper;
            }

            for (int i = 0; i < locations.length; i++) {
                int x = start.getRow() + locations[i][0];
                int y = start.getCol() + locations[i][1];
                if (board.isOnMap(x, y)) {
                    if (board.isOccupied(x, y)) {
                        Piece end_piece = board.getPiece(x, y);
                        if (!((this.isGote() && end_piece.isGote()) || (this.isGote() == false && end_piece.isGote() == false))) {
                            Position move_loc = new Position(x, y);
                            moves.add(move_loc);
                        }
                    } else { // Not occupied
                        Position move_loc = new Position(x, y);
                        moves.add(move_loc);
                    }

                }
            }
        }
    }

    public boolean canPromote() {
        return true;
    } 

    public ArrayList<Position> allMoves() {
        return moves;
    }
    
}
