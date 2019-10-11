package chessEngine.pieces;

import chessEngine.*;

import java.util.ArrayList;
import java.util.List;

public class Pawn extends Chessman {


    private boolean Moved = false;



    public Pawn(ChessmanType chessmanType, int row, int column) {
        super(chessmanType, row, column);
        if (chessmanType==ChessmanType.WHITE){initialrow = 1;}
        else initialrow = 6;
        value = 1;
        if (row!=initialrow){
            Moved=true;
        }
        if (chessmanType == ChessmanType.WHITE) ID = "p";
        else ID = "P";
    }
    public Pawn(ChessmanType chessmanType, int row, int column,int initialRow,int initialColumn) {
        super(chessmanType, row, column,initialRow, initialColumn);
        if (chessmanType==ChessmanType.WHITE){initialrow = 1;}
        else initialrow = 6;
        value = 1;
        if (row!=initialrow){
            Moved=true;
        }
        if (chessmanType == ChessmanType.WHITE) ID = "p";
        else ID = "P";
    }



    public int getInitialrow() {
        return initialrow;
    }

    @Override
    public List<Position> availableMove(Board board) {
        List<Position> pawnPositions = new ArrayList<>();
        Chessman check1;
        Chessman check2;
        if (initialrow == 1) {
            check1 = board.getChessmanAt(row + 1, column);
            if (check1 == null && row + 1 < 8) {
                addposition(row + 1, column, pawnPositions);
            }
            check2 = board.getChessmanAt(row + 2, column);
            if (check1 == null && check2 == null && Moved == false && row + 2 < 8) {
                addposition(row + 2, column, pawnPositions);
            }
            check1 = board.getChessmanAt(row + 1, column - 1);
            if (check1 != null && check1.chessmanType != this.chessmanType) {
                addposition(row + 1, column - 1, pawnPositions);
            }
            check1 = board.getChessmanAt(row + 1, column + 1);
            if (check1 != null && check1.chessmanType != this.chessmanType) {
                addposition(row + 1, column + 1, pawnPositions);
            }
        }
        if (initialrow == 6) {
            check1 = board.getChessmanAt(row - 1, column);
            if (check1 == null && row - 1 >= 0) {
                addposition(row - 1, column, pawnPositions);
            }
            check2 = board.getChessmanAt(row - 2, column);
            if (check1 == null && check2 == null && Moved == false && row - 2 >= 0) {
                addposition(row - 2, column, pawnPositions);
            }
            check1 = board.getChessmanAt(row - 1, column - 1);
            if (check1 != null && check1.chessmanType != this.chessmanType) {
                addposition(row - 1, column - 1, pawnPositions);
            }
            check1 = board.getChessmanAt(row - 1, column + 1);
            if (check1 != null && check1.chessmanType != this.chessmanType) {
                addposition(row - 1, column + 1, pawnPositions);
            }
        }
        return pawnPositions;
    }

    @Override
    public Chessman copy() {

        Pawn copy = new Pawn(this.chessmanType, row, column);
        return copy;
    }

    @Override
    public void move(Position terminal, Board board) {
        Chessman opponent = board.getChessmanAt(terminal);
        List<Position> CheckMove = validMoves(this, board);
        if (opponent != null && opponent.chessmanType != this.chessmanType) {
            capture(terminal, board);
        }
        for (Position move : CheckMove) {
            if (move.getRow() == terminal.getRow() && move.getColumn() == terminal.getColumn()) {
                setPosition(terminal);
                break;
            }
        }
        if (terminal.getRow() == initialrow + 6 || terminal.getRow() == initialrow - 6) {

        }
        if (!Moved) {
            Moved = true;
        }
    }

    @Override
    public void capture(Position terminal, Board board) {

        board.removePiece(board.getChessmanAt(terminal));
    }

    public void move(Pawn pawn, Chessman chessman, Position terminal, Board board) {
        if (board.getChessmanAt(terminal) != null && board.getChessmanAt(terminal).chessmanType != this.chessmanType) {
            capture(terminal, board);
        }
        board.removePiece(pawn);
        chessman.setPosition(terminal);
        board.addPiece(chessman);
        if (!Moved) {
            Moved = true;
        }

    }
}
