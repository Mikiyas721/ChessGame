package chessEngine.pieces;

import chessEngine.*;

import java.util.ArrayList;
import java.util.List;

public class Rook extends Chessman {
    public boolean hasMoved() {
        return Moved;
    }

    boolean Moved = false;


    public Rook(ChessmanType chessmanType, int row, int column) {
        super(chessmanType, row, column);
        value = 5;
        if (chessmanType == ChessmanType.WHITE) ID = "r";
        else ID = "R";

    }
    public Rook(ChessmanType chessmanType, int row, int column,int initialRow,int initialColumn) {
        super(chessmanType, row, column,initialRow,initialColumn);
        value = 5;
        if (chessmanType == ChessmanType.WHITE) ID = "r";
        else ID = "R";

    }

    @Override
    public List<Position> availableMove(Board board) {
        List<Position> rookPositions = new ArrayList<>();
        checkStraight(rookPositions, board, this);
        return rookPositions;
    }

    private List<Position> filteredAvailableMove(Board board) {
        return validMoves(this,board);
    }

    @Override
    public Chessman copy() {

        Rook copy = new Rook(this.chessmanType, row, column);
        return copy;
    }

    @Override
    public void move(Position terminal, Board board) {

        List<Position> checkMove = filteredAvailableMove(board);
        Chessman checkingcheesman = board.getChessmanAt(terminal);
        if (checkingcheesman != null && checkingcheesman.chessmanType != this.chessmanType) {
            capture(terminal, board);
        }
        if (checkMove.stream().anyMatch(Position -> terminal.getRow() == Position.getRow() && terminal.getColumn() == Position.getColumn())) {
            setPosition(terminal);
        }

        if (!Moved) {
            Moved = true;
        }
    }

    @Override
    public void capture(Position terminal, Board board) {
        board.removePiece(board.getChessmanAt(terminal));
    }


}
