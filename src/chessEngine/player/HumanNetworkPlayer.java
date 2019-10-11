package chessEngine.player;

import chessEngine.*;
import chessEngine.pieces.Chessman;
import chessEngine.pieces.Pawn;

import javax.swing.*;
import java.io.*;
import java.util.*;

public class HumanNetworkPlayer extends HumanPlayer {
    private ObjectOutputStream opponentObjectOutputStream;

    public HumanNetworkPlayer(ChessmanType chessmanType, ObjectOutputStream opponentObjectOutputStream) {
        super(chessmanType);
        this.opponentObjectOutputStream = opponentObjectOutputStream;
    }

    @Override
    public void move(Position initial, Position terminal) {
        Chessman chessman = tempBoard.getChessmanAt(initial);
        Chessman opponent = tempBoard.getChessmanAt(terminal);
        if (chessman != null && chessman.getChessmanType() == chessmanType) {
            chessman.move(terminal, tempBoard);

            ArrayList<Position> a = new ArrayList<>();

            try {
                a.add(initial);
                a.add(terminal);
                a.add(null);
                opponentObjectOutputStream.writeObject(a);

                fireMoveEvent();
                tempBoard = null;
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Couldn't write position");
            }
        }
    }

    @Override
    public void move(Position initial, Position terminal, Pawn pawn, Chessman toBeAdded, Position removed) {
        Pawn pawn1 = (Pawn) tempBoard.getChessmanAt(initial);
        Chessman opponent = tempBoard.getChessmanAt(terminal);
        if (pawn1 != null && pawn1.getChessmanType() == chessmanType) {
            pawn1.move(pawn, toBeAdded, terminal, tempBoard);
            ArrayList<Position> a = new ArrayList<>();
            try {
                a.add(initial);
                a.add(terminal);
                a.add(removed);
                opponentObjectOutputStream.writeObject(a);

                fireMoveEvent();
                tempBoard = null;
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Couldn't write position");
            }
        }



    }
}



