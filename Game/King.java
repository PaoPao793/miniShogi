package Game;

import java.util.*;


public class King extends Piece {
    private static final int[][] possibleLocations = new int[][]{{-1,-1},{0,-1},{1,-1},{1,0},{1,1},{0,1},{-1,1},{-1,0}};
    private ArrayList<Position> moves = new ArrayList<>();

    public King(String name) {
        super(name);
    }

    @Override
    public boolean canMove(Board board, Position start, Position end) {
        possibleMoves(board,start);
        for (Position p : moves) {
            if (end.equals(p)) {
                return true;
            }
        }
        return false;
    }

    public void possibleMoves(Board board,Position start) {
        moves.clear(); // clears stuff from b4
        for(int i = 0; i < possibleLocations.length; i++) {
            int x = start.getRow() + possibleLocations[i][0];
            int y = start.getCol() + possibleLocations[i][1];
            if(board.isOnMap(x,y)) {
                if (board.isOccupied(x,y)) {
                    Piece end_piece = board.getPiece(x,y);
                    if (!((isGote() && end_piece.isGote())|| (isGote() == false && end_piece.isGote() == false))) {
                        Position move_loc = new Position(x,y);
                        moves.add(move_loc);

                    }
                } else { // Not occupied 
                    Position move_loc = new Position(x,y);
                    moves.add(move_loc);
                }
            }
        }
    }

    public boolean canPromote() {
        return false;
    }

    public ArrayList<Position> allMoves() {
        return this.moves;
    }
}
