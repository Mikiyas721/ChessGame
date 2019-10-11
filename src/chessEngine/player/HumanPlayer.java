package chessEngine.player;

import chessEngine.Board;
import chessEngine.ChessmanType;
import chessEngine.Position;
import chessEngine.pieces.Chessman;
import chessEngine.pieces.Pawn;

public class HumanPlayer extends Player {
    protected Board tempBoard;

    public HumanPlayer(ChessmanType chessmanType) {
        super(chessmanType);
    }

    @Override
    public void canMove(Board board) {
        tempBoard = board;
    }

    public void move(Position initial, Position terminal) {
        Chessman chessman = tempBoard.getChessmanAt(initial);
        if (chessman != null && chessman.getChessmanType() == chessmanType) {
            chessman.move(terminal,tempBoard);
            fireMoveEvent();
            tempBoard = null;
        }


    }
    public void move(Position initial, Position terminal,Pawn pawn, Chessman toBeAdded,Position removed) {
        Pawn pawn1 = (Pawn)tempBoard.getChessmanAt(initial);
        if (pawn1 != null && pawn1.getChessmanType() == chessmanType) {
            pawn1.move(pawn,toBeAdded,terminal,tempBoard);
            fireMoveEvent();
            tempBoard = null;
        }
    }



}
