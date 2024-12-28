package Game;

import java.util.ArrayList;

public class GoldGeneral extends Piece {
    private static final int[][] lowerPossible = new int[][]{{0,-1},{1,0},{0,1},{-1,1},{-1,0},{-1,-1}}; // back, right, diag forw right, forw, diag forw left, left
    private static final int[][] upperPossible = new int[][]{{1,-1},{0,-1},{1,1},{1,0},{-1,0},{0,1}}; 
    private ArrayList<Position> moves = new ArrayList<>();
    
    public GoldGeneral(String name) {
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
        moves.clear(); 
        int[][] locations;
        if (isGote()) {
            locations = lowerPossible;
        } else {
            locations = upperPossible;
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
                } else { // Not occupied 
                    Position move_loc = new Position(row, col);
                    moves.add(move_loc);
                }
            }
        }
    }

    public boolean canPromote() {
        return false;
    }

    public ArrayList<Position> allMoves(){
        return this.moves;
    }
}
