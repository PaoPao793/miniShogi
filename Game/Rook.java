package Game;

import java.util.ArrayList;

public class Rook extends Piece {
    private static final int[][] promoted = new int[][]{{-1,-1},{1,-1},{1,1},{-1,1}};
    private ArrayList<Position> moves = new ArrayList<>();
    
    public Rook(String name) {
        super(name);
    }

    @Override
    public boolean canMove(Board board, Position start, Position end) {
        possibleMoves(board,start);
        for(Position p : moves) {
            if (end.equals(p)) {
                return true;
            }
        }
        return false;
    }

    public void possibleMoves(Board board , Position start) {
        moves.clear();
        int row = start.getRow();
        int col = start.getCol();
        int temp_row = row - 1;
        while (temp_row >= 0) {
            boolean obstruction = add_move(board, temp_row, col);
            if (obstruction) {
                break;
            }
            temp_row--;
        }
        
        temp_row = row + 1;
        while (temp_row < 5){
            boolean obstruction = add_move(board, temp_row, col);
            if (obstruction) {
                break;
            }
            temp_row++;
        }

        int temp_col = col - 1;
        while (temp_col >= 0) {
            boolean obstruction = add_move(board, row,temp_col);
            if (obstruction) {
                break;
            }
            temp_col--;
        }

        temp_col = col + 1;
        while (temp_col < 5) {
            boolean obstruction = add_move(board, row, temp_col);
            if (obstruction) {
                break;
            }
            temp_col++;
        }
        
        if (isPromoted()) {
            for (int i = 0; i < promoted.length; i++) {
                row = start.getRow() + promoted[i][0];
                col = start.getCol() + promoted[i][1];
                if (board.isOnMap(row, col)) {
                    if (board.isOccupied(row,col)) {
                        Piece end_piece = board.getPiece(row, col);
                        if (!((this.isGote() && end_piece.isGote())|| (this.isGote() == false && end_piece.isGote() == false))) {
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
    }

    public boolean add_move(Board board, int row, int col) {
        if(!board.isOnMap(row, col)) {
            return false;
        }
        
        if(board.isOccupied(row, col)) {
            Piece end_piece = board.getPiece(row, col);
            if(!((this.isGote() && end_piece.isGote())|| (this.isGote() == false && end_piece.isGote() == false))) {
                Position move_loc = new Position(row, col);
                moves.add(move_loc);
            }
            return true;
        } else {
            Position move_loc = new Position(row, col);
            moves.add(move_loc);
            return false;
        }
    }

    public boolean canPromote() {
        return true;
    }

    public ArrayList<Position> allMoves() {
        return moves;
    }
}
