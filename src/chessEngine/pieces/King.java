package chessEngine.pieces;

import chessEngine.*;


import java.util.ArrayList;
import java.util.List;

public class King extends Chessman {
    public boolean hasMoved() {
        return Moved;
    }

    boolean Moved = false;

    public King(ChessmanType chessmanType, int row, int column) {
        super(chessmanType, row, column);
        value = 100;
        if (chessmanType == ChessmanType.WHITE) ID = "k";
        else ID = "K";


    }

    public King(ChessmanType chessmanType, int row, int column, int initialRow, int initialColumn) {
        super(chessmanType, row, column, initialRow, initialColumn);
        value = 100;
        if (chessmanType == ChessmanType.WHITE) ID = "k";
        else ID = "K";


    }

    private List<Position> filteredAvailableMove(Board board) {
        return validMoves(this, board);
    }

    @Override
    public List<Position> availableMove(Board board) {
        List<Position> kingPositions = new ArrayList<>();
        List<Chessman> avaliable = board.getFriends(chessmanType);
        Rook rook1 = null;
        Rook rook2 = null;
        for (int i = row - 1; i < row + 2; i++) {
            if (i < 0 || i > 7) continue;
            for (int j = column - 1; j < column + 2; j++) {
                if (j < 0 || j > 7) continue;
                if (i == row && j == column) continue;
                Chessman check = board.getChessmanAt(i, j);
                if (check != null && check.chessmanType == this.chessmanType) continue;
                addposition(i, j, kingPositions);
            }
        }
        for (Chessman chessman : avaliable) {
            if (chessman instanceof Rook) {
                if (chessman.getInitialcolumn() > initialcolumn) {
                    rook1 = (Rook) chessman;
                } else
                    rook2 = (Rook) chessman;
            }
        }
        if (!Moved && rook1 != null && !rook1.Moved) {
            boolean NoPiece = true;
            for (int j = column + 1; j < rook1.getColumn(); j++) {
                if (board.getChessmanAt(row, j) != null) {
                    NoPiece = false;
                    break;
                }
            }
            if (NoPiece) {
                addposition(row, column + 2, kingPositions);
            }
        }
        if (!Moved && rook2 != null && !rook2.Moved) {
            boolean NoPiece = true;
            for (int j = column - 1; j > rook2.getColumn(); j--) {
                if (board.getChessmanAt(row, j) != null) {
                    NoPiece = false;
                    break;
                }
            }
            if (NoPiece) {
                addposition(row, column - 2, kingPositions);
            }
        }
        return kingPositions;
    }

    @Override
    public void move(Position terminal, Board board) {

        List<Position> checkMove = filteredAvailableMove(board);
        Chessman unknownpiece = board.getChessmanAt(terminal);


        if (unknownpiece != null && unknownpiece.chessmanType != this.chessmanType) {
            capture(terminal, board);
        }
        for (Position move : checkMove) {

            if (move.getRow() == row && move.getColumn() == column + 2 && move.getColumn() == terminal.getColumn() && move.getRow() == terminal.getRow()) {
                castling(board, true);
            } else if (move.getRow() == row && move.getColumn() == column - 2 && move.getColumn() == terminal.getColumn() && move.getRow() == terminal.getRow()) {
                castling(board, false);
            } else if (move.getRow() == terminal.getRow() && move.getColumn() == terminal.getColumn()) {
                setPosition(terminal);
            }
        }
        if (!Moved) {
            Moved = true;
        }
    }

    @Override
    public void capture(Position terminal, Board board) {
        board.removePiece(board.getChessmanAt(terminal));
    }


    @Override
    public Chessman copy() {

        King copy = new King(this.chessmanType, row, column);
        return copy;
    }

    public void castling(Board board, boolean greater) {
        Rook ROOK = null;
        if (greater) {
            ROOK = (Rook) board.getChessmanAt(row, 7);
            setRow(row);
            setColumn(column + 2);
            ROOK.setRow(row);
            ROOK.setColumn(column - 1);
        } else {
            ROOK = (Rook) board.getChessmanAt(row, 0);
            setRow(row);
            setColumn(column - 2);
            ROOK.setRow(row);
            ROOK.setColumn(column + 1);
        }
    }
}
