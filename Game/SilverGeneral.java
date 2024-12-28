package Game;

import java.util.ArrayList;

public class SilverGeneral extends Piece {
    private static final int[][] lowerPossible = new int[][]{{-1,-1},{1,-1},{1,1},{-1,0},{-1,1}}; 
    private static final int[][] upperPossible = new int[][]{{1,-1},{1,1},{1,0},{-1,1},{-1,-1}};
    private static final int[][] promotedLower = new int[][]{{0,-1}, {1,0}, {-1,-1}, {0,1},{-1,1},{-1,0}};
    private static final int[][] promotedUpper = new int[][]{{1,-1},{0,-1},{1,1},{1,0},{-1,0},{0,1}};

    private ArrayList<Position> moves = new ArrayList<>();
    
    public SilverGeneral(String name) {
        super(name);
    }

    @Override
    public boolean canMove(Board board, Position start, Position end) {
        possibleMoves(board,start);
        for(Position p : moves) {
            if(end.equals(p)) {
                return true;
            }
        }
        return false;
    }

    public void possibleMoves(Board board, Position start) {
        moves.clear(); // clears moves from b4
        int[][] locations;
        if (isPromoted()) {
            if (isGote()) {
                locations = promotedLower;
            } else {
                locations = promotedUpper;
            }
        } else {
            if (this.isGote()) {
                locations = lowerPossible;
            } else {
                locations = upperPossible;
            }
        }

        for (int i = 0; i < locations.length; i++) {
            int row = start.getRow() + locations[i][0];
            int col = start.getCol() + locations[i][1];
 
            if (board.isOnMap(row, col)) {
                if (board.isOccupied(row, col)) {
                    Piece end_piece = board.getPiece(row, col);
                    if (!((isGote() && end_piece.isGote()) || (isGote() == false && end_piece.isGote() == false))) {
                        Position move_loc = new Position(row, col);
                        moves.add(move_loc);
                    }
                } else { // space was not occupied 
                    Position move_loc = new Position(row, col);
                    moves.add(move_loc);
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
