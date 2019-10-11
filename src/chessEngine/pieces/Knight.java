package chessEngine.pieces;

import java.util.List;

import chessEngine.*;

import java.util.ArrayList;


public class Knight extends Chessman {


    public Knight(ChessmanType chessmanType, int row, int column) {
        super(chessmanType, row, column);
        value = 3;
        if (chessmanType == ChessmanType.WHITE) ID = "h";
        else ID = "H";
    }
    public Knight(ChessmanType chessmanType, int row, int column,int initialRow,int initialColumn) {
        super(chessmanType, row, column,initialRow,initialColumn);
        value = 3;
        if (chessmanType == ChessmanType.WHITE) ID = "h";
        else ID = "H";
    }

    @Override
    public List<Position> availableMove(Board board) {
        List<Position> KnightMovments = new ArrayList<>();
        for (int i = row - 2; i < row + 3; i++) {
            if (i < 0 || i > 7) continue;
            for (int j = column - 2; j < column + 3; j++) {
                if (j < 0 || j > 7) continue;
                if (i == row && j == column) continue;
                if (board.getChessmanAt(i, j) != null && board.getChessmanAt(i, j).chessmanType == this.chessmanType)
                    continue;
                if ((i == row + 2 && j == column - 1) || (i == row + 2 && j == column + 1) || (i == row + 1 && j == column - 2)
                        || (i == row + 1 && j == column + 2) || (i == row - 1 && j == column - 2) || (i == row - 1 && j == column + 2)
                        || (i == row - 2 && j == column - 1) || (i == row - 2 && j == column + 1)) {
                    addposition(i, j, KnightMovments);
                }
            }
        }
        return KnightMovments;
    }

    private List<Position> filteredAvailableMove(Board board) {
        return validMoves(this,board);
    }

    @Override
    public Chessman copy() {

        Chessman copy = new Knight(this.chessmanType, row, column);
        return copy;
    }

    @Override
    public void move(Position terminal, Board board) {

        List<Position> checkMove = filteredAvailableMove(board);
        Chessman unknown = board.getChessmanAt(terminal);
        if (unknown != null && unknown.chessmanType != this.chessmanType) {
            capture(terminal, board);
        }
        for (Position move : checkMove) {
            if (move.getRow() == terminal.getRow() && move.getColumn() == terminal.getColumn()) {
                this.setPosition(terminal);
                break;
            }
        }

    }

    @Override
    public void capture(Position terminal, Board board) {
        board.removePiece(board.getChessmanAt(terminal.getRow(),terminal.getColumn()));

    }


}
