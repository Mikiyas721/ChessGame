package chessEngine.player;

import chessEngine.Board;
import chessEngine.ChessmanType;
import chessEngine.MoveListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import chessEngine.*;
import chessEngine.pieces.Chessman;

abstract public class Player {
    protected ChessmanType chessmanType;
    protected List<MoveListener> moveListeners = new ArrayList<>();

    protected void fireMoveEvent() {
        moveListeners.forEach(MoveListener::hasMoved);

    }

    public Player(ChessmanType chessmanType) {
        this.chessmanType = chessmanType;
    }

    public ChessmanType getChessmanType() {
        return chessmanType;
    }

    public void addListner(MoveListener moveListener) {
        moveListeners.add(moveListener);
    }

    abstract public void canMove(Board board);



}
