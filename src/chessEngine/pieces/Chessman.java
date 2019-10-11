package chessEngine.pieces;

import chessEngine.*;
import org.omg.PortableServer.POA;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

abstract public class Chessman {


    protected String ID;
    protected ChessmanType chessmanType;
    protected int row;

    public int getInitialrow() {
        return initialrow;
    }

    public int getInitialcolumn() {
        return initialcolumn;
    }

    protected int initialrow;
    protected int initialcolumn;
    protected int column;
    protected static int value;


    public int getValue() {
        return value;
    }

    public String getID() {
        return ID;
    }

    public Chessman(ChessmanType chessmanType, int row, int column) {
        this.chessmanType = chessmanType;
        this.row = row;
        this.column = column;
        initialrow = row;
        initialcolumn = column;

    }
    public Chessman(ChessmanType chessmanType, int row, int column,int initialRow,int initialColumn) {
        this.chessmanType = chessmanType;
        this.row = row;
        this.column = column;
        this.initialrow = row;
        this.initialcolumn = column;

    }
    public int getRow() {
        return row;
    }

    public void setInitialPosition(Position position) {
        this.initialrow = position.getRow();
        this.initialcolumn = position.getColumn();
    }

    public Position getinitialPosition() {
        Position position = new Position();
        position.setRow(initialrow);
        position.setColumn(initialcolumn);
        return position;
    }

    public Position getPosition() {
        Position position = new Position();
        position.setRow(row);
        position.setColumn(column);
        return position;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public void setPosition(Position position) {
        setRow(position.getRow());
        setColumn(position.getColumn());
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }

    public ChessmanType getChessmanType() {
        return chessmanType;
    }


    public static void checkStraight(List<Position> chesspiece, Board board, Chessman chessman) {
        for (int i = chessman.getRow() + 1; i < 8; i++)
            if (positionChecker(i, chessman.getColumn(), board, chessman, chesspiece)) break;
        for (int i = chessman.getRow() - 1; i >= 0; i--)
            if (positionChecker(i, chessman.getColumn(), board, chessman, chesspiece)) break;
        for (int j = chessman.getColumn() + 1; j < 8; j++)
            if (positionChecker(chessman.getRow(), j, board, chessman, chesspiece)) break;
        for (int j = chessman.getColumn() - 1; j >= 0; j--)
            if (positionChecker(chessman.getRow(), j, board, chessman, chesspiece)) break;

    }

    public static void checkDiagonal(List<Position> chesspiece, Board board, Chessman chessman) {
        for (int i = chessman.getRow() + 1, j = chessman.getColumn() + 1; i < 8 && j < 8; i++, j++)
            if (positionChecker(i, j, board, chessman, chesspiece)) break;
        for (int i = chessman.getRow() - 1, j = chessman.getColumn() - 1; i >= 0 && j >= 0; i--, j--)
            if (positionChecker(i, j, board, chessman, chesspiece)) break;
        for (int i = chessman.getRow() + 1, j = chessman.getColumn() - 1; i < 8 && j >= 0; i++, j--)
            if (positionChecker(i, j, board, chessman, chesspiece)) break;
        for (int i = chessman.getRow() - 1, j = chessman.getColumn() + 1; i >= 0 && j < 8; i--, j++)
            if (positionChecker(i, j, board, chessman, chesspiece)) break;
    }

    public static boolean positionChecker(int i, int j, Board board, Chessman chessman, List<Position> chesspiece) {
        boolean end = false;
        Chessman checker = board.getChessmanAt(i, j);
        if (checker == null) {
            addposition(i, j, chesspiece);
        } else {
            if (chessman.getChessmanType() != checker.getChessmanType()) {
                addposition(i, j, chesspiece);
            }
            end = true;
        }
        return end;
    }

    public static Chessman underCheck(ChessmanType chessmanType, Board board) {
        Chessman underCheck = null;
        List<Chessman> opponent = board.getOpponents(chessmanType);
        King king = board.getKing(chessmanType);
        A:
        for (Chessman chessman : opponent) {
            if (chessman instanceof Pawn) {
                if ((chessman.getRow() + 1 == king.getRow() && chessman.getColumn() + 1 == king.getColumn()) ||
                        (chessman.getRow() + 1 == king.getRow() && chessman.getColumn() - 1 == king.getColumn()) ||
                        (chessman.getRow() - 1 == king.getRow() && chessman.getColumn() + 1 == king.getColumn()) ||
                        (chessman.getRow() - 1 == king.getRow() && chessman.getColumn() - 1 == king.getColumn())) {
                    underCheck = chessman;
                    break A;
                }

            } else {
                List<Position> opponentPosition = chessman.availableMove(board);
                for (Position position : opponentPosition) {
                    if (position.getRow() == king.getRow() && position.getColumn() == king.getColumn()) {
                        underCheck = chessman;
                        break A;
                    }
                }
            }
        }
        return underCheck;
    }


    public static List<Position> validMoves(Chessman chessman, Board board) {
        Board copyBoard = board.copy();
        List<Position> valid = new ArrayList<>();
        List<Position> available = chessman.availableMove(board);
        Chessman chessmanCopy = copyBoard.getChessmanAt(chessman.getPosition());
        for (Position position : available) {
            Chessman opponent = copyBoard.getChessmanAt(position);
            chessmanCopy.setPosition(position);

            if (opponent != null && opponent.getChessmanType() != chessmanCopy.getChessmanType()) {
                copyBoard.removePiece(opponent);
            }

            Chessman checkingpiece = underCheck(chessmanCopy.getChessmanType(), copyBoard);
            if (checkingpiece == null || chessmanCopy.getChessmanType() == checkingpiece.getChessmanType()) {
                valid.add(position);
            }
        }

        return valid;
    }


    public static void addposition(int i, int j, List<Position> ChessmanMovments) {
        Position Move = new Position();
        Move.setRow(i);
        Move.setColumn(j);
        ChessmanMovments.add(Move);
    }

    abstract public List<Position> availableMove(Board board);

    abstract public void move(Position terminal, Board board);


    abstract public void capture(Position terminal, Board board);

    abstract public Chessman copy();
}

