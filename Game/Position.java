package Game;

public class Position {
    private char charCol; 
    private int col;  // represent the a-e (0-4)
    private int row;  // represent the 5-1 (but inverted for arr indexing)

    public Position(char charCol, int row) {
        this.charCol = charCol;
        col = charCol - 'a';
        this.row = 5 - row;
    }

    public Position(int row, int col) {
        this.row = row;
        this.col = col;
        charCol = (char) ('a' + col);
    }

    public Position(String s) {
        this(s.charAt(0), Character.getNumericValue(s.charAt(1)));
    }

    public int getCol() {
        return col;
    }

    public int getRow() {
        return row;
    }
    
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof Position)) {
            return false;
        }

        Position p = (Position) o;
        return (col == p.getCol() && row == p.getRow());
    }

    public String toString() {
        return ("" + charCol + (5 - row));
    }
}