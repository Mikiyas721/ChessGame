package chessEngine.player;

import chessEngine.Board;
import chessEngine.ChessmanType;
import chessEngine.Position;
import chessEngine.pieces.Chessman;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class NetworkPlayer extends Player {
    private Board tempBoard;
    private ObjectInputStream opponentObjectInputStream;


    public NetworkPlayer(ChessmanType chessmanType, ObjectInputStream objectInputStream) {
        super(chessmanType);
        this.opponentObjectInputStream = objectInputStream;

    }

    @Override
    public void canMove(Board board) {
        tempBoard = board;
        new Thread(() -> {
            try {

                ArrayList<Position> a = (ArrayList<Position>) opponentObjectInputStream.readObject();
                Position networkInitial;
                Position networkTerminal;
                Position networkchessman;
                if (a.get(2) == null) {
                    networkInitial = a.get(0).flipped();
                    networkTerminal = a.get(1).flipped();

                    move(networkInitial, networkTerminal);
                } else {
                    networkInitial = a.get(0).flipped();
                    networkTerminal = a.get(1).flipped();
                    networkchessman = a.get(2).flipped();

                    move(networkInitial, networkTerminal, networkchessman);

                }
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }

        }).start();

    }

    public void move(Position initial, Position terminal) {
        Chessman chessman = tempBoard.getChessmanAt(initial);
        if (chessman != null && chessman.getChessmanType() == chessmanType) {
            chessman.move(terminal, tempBoard);
            fireMoveEvent();
            // tempBoard = null;
        }
    }

    public void move(Position initial, Position terminal, Position toBeAdded) {
        Chessman chessman = tempBoard.getChessmanAt(initial);
        List<Chessman> removes = tempBoard.getRemovedChessman();
        System.out.println(toBeAdded.getRow() + " " + toBeAdded.getColumn());
        for (Chessman chess : removes) {
            System.out.println(chess + " " + chess.getRow() + chess.getColumn());
        }
        Chessman chessmantoBeAdded = tempBoard.getRemovedChessman().stream().
                filter(chessman1 -> (chessman1.getinitialPosition().getRow()==toBeAdded.getRow()&&chessman1.getinitialPosition().getColumn()==toBeAdded.getColumn())).findAny().get();
        System.out.println(chessmantoBeAdded);
        chessmantoBeAdded.setPosition(terminal);
        if (chessman != null && chessman.getChessmanType() == chessmanType) {
            tempBoard.removePiece(chessman);
            tempBoard.addPiece(chessmantoBeAdded);
            fireMoveEvent();
            // tempBoard = null;
        }


    }
}
