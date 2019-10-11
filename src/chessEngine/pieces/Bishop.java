package chessEngine.pieces;

import chessEngine.*;

import java.util.ArrayList;
import java.util.List;

public class Bishop extends Chessman {


    public Bishop(ChessmanType chessmanType, int row, int column) {

        super(chessmanType, row, column);
        value = 3;
        if (chessmanType == ChessmanType.WHITE) ID = "b";
        else ID = "B";
    }
    public Bishop(ChessmanType chessmanType, int row, int column,int initialRow,int initialColumn) {

        super(chessmanType, row, column,initialRow,initialColumn);
        value = 3;
        if (chessmanType == ChessmanType.WHITE) ID = "b";
        else ID = "B";
    }

    @Override
    public List<Position> availableMove(Board board) {
        List<Position> bishopPositions = new ArrayList<>();

        checkDiagonal(bishopPositions, board, this);

        return bishopPositions;
    }

    private List<Position> filteredAvailableMove(Board board) {
      return validMoves(this,board);
    }

    @Override
    public Chessman copy() {

        Bishop copy = new Bishop(this.chessmanType, row, column);
        return copy;
    }

    @Override
    public void move(Position terminal, Board board) {
        List<Position> checkMove = filteredAvailableMove(board);
        Chessman opponent = board.getChessmanAt(terminal);
        if (opponent!=null&&opponent.chessmanType != this.chessmanType) {
            capture(terminal, board);
        }
        if (checkMove.stream().anyMatch(position -> terminal.getColumn() == position.getColumn() && terminal.getRow() == position.getRow())) {
           setPosition(terminal);

        }


    }

    @Override
    public void capture(Position terminal, Board board) {
        board.removePiece(board.getChessmanAt(terminal));
    }

}
