package Game;

/**
 * Class to represent Mini Shogi board
 */
public class Board {

    Piece[][] board;

    final int BOARD_SIZE = 5;

    public Board(boolean initialized) {
    	board = new Piece[BOARD_SIZE][BOARD_SIZE];

        // init the board with the initial pieces 
        // gote 
        this.setPiece(new Position('a',1),new King("k"));
        this.setPiece(new Position('b',1),new GoldGeneral("g"));
        this.setPiece(new Position('c',1),new SilverGeneral("s"));
        this.setPiece(new Position('d',1),new Bishop("b"));
        this.setPiece(new Position('e',1),new Rook("r"));
        this.setPiece(new Position('a',2),new Pawn("p"));

        // Sente 
        this.setPiece(new Position('e',5),new King("K"));
        this.setPiece(new Position('d',5),new GoldGeneral("G"));
        this.setPiece(new Position('c',5),new SilverGeneral("S"));
        this.setPiece(new Position('b',5),new Bishop("B"));
        this.setPiece(new Position('a',5),new Rook("R"));
        this.setPiece(new Position('e',4),new Pawn("P"));        
    }

    public Board() {
        board = new Piece[BOARD_SIZE][BOARD_SIZE];
    }

    /* Print board */
    public String toString() {
        String[][] pieces = new String[BOARD_SIZE][BOARD_SIZE];
        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                pieces[row][col] = this.isOccupied(row, col) ? board[row][col].toString() : "";
            }
        }
        return stringifyBoard(pieces);
    }

    public boolean isOccupied(int col, int row) {
        return board[col][row] != null;
    }

    public boolean isOccupied(Position p) {
        return board[p.getRow()][p.getCol()] != null;
    }

    private String stringifyBoard(String[][] board) {
        String str = "";

        for (int row = 0; row < BOARD_SIZE; row++) {
            str += Integer.toString(5 - row) + " |";

            for (int col = 0; col < BOARD_SIZE; col++) {
                str += stringifySquare(board[row][col]);
            }
            str += System.getProperty("line.separator");
        }

        str += "    a  b  c  d  e" + System.getProperty("line.separator");

        return str;
    }

    private String stringifySquare(String sq) {
        switch (sq.length()) {
            case 0:
                return "__|";
            case 1:
                return " " + sq + "|";
            case 2:
                return sq + "|";
        }

        throw new IllegalArgumentException("Board must be an array of strings like \"\", \"P\", or \"+P\"");
    }

    public boolean isOnMap(int x_board, int y_board) {
        return x_board >= 0 && y_board >= 0 && x_board < BOARD_SIZE && y_board < BOARD_SIZE;
    }

    public boolean isOnMap(Position p) {
        return isOnMap(p.getRow(),p.getCol());
    }

    public Piece getPiece(int x ,int y) {
        return this.board[x][y];
    }
    
    public void setPiece(Position p, Piece piece) {
        board[p.getRow()][p.getCol()] = piece; 
    }

    public void removePiece(Position p) {
        board[p.getRow()][p.getCol()] = null; 
    }

    public Piece getPiece(Position p) {
        return board[p.getRow()][p.getCol()]; 
    }

    // used when checking if a player is in check 
    public Position findKing(Player p) {
        char find = ' ';
        if(p.getName().equals("Sente")) {
            find = 'K';
        }
        else if(p.getName().equals("Gote")) {
            find = 'k';
        }

        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                Piece temp = board[i][j];
                if (temp != null) {
                    if (temp.toString().charAt(0) == find) {
                        return new Position(i,j);
                    }
                }
            }
        }
        return null; 
    }
}