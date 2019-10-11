package chessEngine.pieces;

import chessEngine.*;

import java.util.ArrayList;
import java.util.List;

public class Queen extends Chessman {


    public Queen(ChessmanType chessmanType, int row, int column) {
        super(chessmanType, row, column);
        value = 9;
        if (chessmanType == ChessmanType.WHITE) ID = "q";
        else ID = "Q";
    }
    public Queen(ChessmanType chessmanType, int row, int column,int initialRow,int initialColumn) {
        super(chessmanType, row, column,initialRow,initialColumn);
        value = 9;
        if (chessmanType == ChessmanType.WHITE) ID = "q";
        else ID = "Q";
    }

    @Override
    public List<Position> availableMove(Board board) {
        List<Position> queenPositions = new ArrayList<>();
        //Why doesn't Queen work
        checkStraight(queenPositions, board, this);
        checkDiagonal(queenPositions, board, this);
        return queenPositions;
    }

    private List<Position> filteredAvailableMove(Board board) {
        return validMoves(this,board);
    }
    @Override
    public Chessman copy() {

        Queen copy = new Queen(this.chessmanType, row, column);
        return copy;
    }

    @Override
    public void move(Position terminal, Board board) {

        List<Position> checkMove = filteredAvailableMove(board);
        Chessman checkingcheesman = board.getChessmanAt(terminal);
        if (checkingcheesman != null && checkingcheesman.chessmanType != this.chessmanType) {
            capture(terminal, board);
        }
        //if(checkMove.stream().anyMatch(terminal.getRow() == row && terminal.getColumn() == column))
        for (Position move : checkMove) {
            if (move.getRow() == terminal.getRow() && move.getColumn() == terminal.getColumn()) {
                setPosition(terminal);
                //What to do with duplicate code?
            }
        }

    }

    @Override
    public void capture(Position terminal, Board board) {
        board.removePiece(board.getChessmanAt(terminal));
    }


}
