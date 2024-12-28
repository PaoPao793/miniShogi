package Game;

import java.util.ArrayList;

public class Bishop extends Piece {
    private static final int[][] promomoted_locations = new int[][]{{0,-1},{1,0},{0,1},{-1,0}};
    private ArrayList<Position> moves = new ArrayList<>();


    public Bishop(String name) {
        super(name);
    }

    public void possibleMoves(Board board , Position start) {
        moves.clear();
        int row = start.getRow();
        int col = start.getCol();
        int temp_row = row + 1;
        int temp_col = col - 1;
        while (temp_row < 5 && temp_col >= 0) {
            boolean obstruction = add_move(board, temp_row, temp_col);
            if(obstruction) {
                break;
            }
            temp_row++;
            temp_col--;
        }

        temp_row = row - 1;
        temp_col = col + 1;
        while (temp_col >= 0 && temp_col < 5) {
            boolean obstruction = add_move(board, temp_row, temp_col);
            if(obstruction) {
                break;
            }
            temp_row--;
            temp_col++;
        }

        temp_row = row - 1;
        temp_col = col - 1;
        while (temp_row >= 0 && temp_col >= 0) {
            boolean obstruction=add_move(board, temp_row, temp_col);
            if (obstruction) {
                break;
            }
            temp_row--;
            temp_col--;
        }

        temp_row = row + 1;
        temp_col = col + 1;
        while(temp_row < 5 && temp_col < 5) {
            boolean obstruction = add_move(board, temp_row, temp_col);
            if(obstruction) {
                break;
            }
            temp_row++;
            temp_col++;
        }

        if (isPromoted()) { // extra moves if promoted
            for(int i = 0; i < promomoted_locations.length; i++) {
                row = start.getRow() + promomoted_locations[i][0];
                col = start.getCol() + promomoted_locations[i][1];

                if (board.isOnMap(row, col)) {
                    if (board.isOccupied(row, col)) {
                        Piece end_piece = board.getPiece(row, col);
                        if(!((isGote() && end_piece.isGote()) || (isGote() == false && end_piece.isGote() == false))) {
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

    public boolean add_move(Board board, int row ,int col) {
        if (!board.isOnMap(row, col)) {
            return false;
        }

        if (board.isOccupied(row, col)) {
            Piece end_piece = board.getPiece(row, col);
            if(!((isGote() && end_piece.isGote()) || (isGote() == false && end_piece.isGote() == false))) {
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

    @Override
    public boolean canPromote() {
        return true;
    }

    public ArrayList<Position> allMoves() {
        return moves;
    }
}
