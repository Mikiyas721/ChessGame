package chessEngine;

import java.io.Serializable;

public class Position implements Serializable {
    private int row;
    private int column;

    public Position(int row, int column) {
        this.row = row;
        this.column = column;
    }

    public Position() {
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    @Override
    public boolean equals(Object object) {
        if (object instanceof Position) {
            Position position = (Position) object;
            return row == position.getRow() && column == position.getColumn();

        }
        return false;
    }

    @Override
    public int hashCode() {
        return row & column;
    }

    public Position flipped() {
        return new Position(7 - row, 7 - column);
    }
}
