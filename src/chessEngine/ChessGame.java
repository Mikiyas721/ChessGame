package chessEngine;

import chessEngine.pieces.Chessman;
import chessEngine.pieces.King;
import chessEngine.player.ComputerPlayer;
import chessEngine.player.HumanPlayer;
import chessEngine.player.Player;

import javax.swing.*;
import java.io.*;

public class ChessGame implements MoveListener {
    private Player player1;
    private Player player2;
    private Player currentPlayer;
    private Player waitingPlayer;

    public Player getWaitingPlayer() {
        return waitingPlayer;
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }


    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    private Board board;

    public ChessGame(Player player1, Player player2, BoardFormat boardFormat, String saved) {
        this.player1 = player1;
        this.player2 = player2;
        player1.addListner(this);
        player2.addListner(this);
        board = new Board();
        if (saved != null) {
            loadGame(saved);
        } else if (boardFormat == BoardFormat.NORMAL) {
            board.setNormalInitialPosition();
            currentPlayer = player1;
            waitingPlayer = player2;
        } else {
            board.setFlippedInitialPosition();
            currentPlayer = player1;
            waitingPlayer = player2;
        }
    }


    public void loadGame(String saved) {
        String read[] = saved.split("-");
        board.loadBoard(read[0]);
        if (read[1].charAt(0) == 'W') {
            currentPlayer = player1;
            waitingPlayer = player2;

        } else if (read[1].charAt(0) == 'B') {
            currentPlayer = player2;
            waitingPlayer = player1;
        }

    }

    public void saveGame() {
        String save = board.toString();
        save += '-';
        if (currentPlayer.getChessmanType() == ChessmanType.WHITE) {
            save += 'W';
        } else save += 'B';
        save += ';';
        if (currentPlayer instanceof ComputerPlayer) {
            save += 'C';
            save += ((ComputerPlayer) currentPlayer).getDepth();
        } else if (waitingPlayer instanceof ComputerPlayer) {
            save += 'C';
            save+=((ComputerPlayer) waitingPlayer).getDepth();
        } else if (currentPlayer instanceof HumanPlayer && waitingPlayer instanceof HumanPlayer) {
            save += 'H';
        }
        boolean result = new File("saved").mkdir();
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("saved\\saved.txt"))) {
            bufferedWriter.write(save);
            bufferedWriter.flush();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Couldn't save game");
        }
    }

    public boolean start() {
        if (currentPlayer != null) {
            currentPlayer.canMove(board);
            return true;
        }
        return false;
    }

    @Override
    public void hasMoved() {
        King king = board.getKing(waitingPlayer.getChessmanType());
       /* boolean check = gameOver(waitingPlayer.getChessmanType());
        if (check) {
            gameStateListner.gameover();

        }*/  {

            currentPlayer = currentPlayer == player1 ? player2 : player1;
            waitingPlayer = waitingPlayer == player1 ? player2 : player1;
            currentPlayer.canMove(board);
        }
    }

    public boolean gameOver(ChessmanType chessmanType) {
        boolean gameOver = false;

        return isCheckMate(chessmanType) || isStaleMate(chessmanType);
    }

    public boolean isCheckMate(ChessmanType chessmanType) {
        return !(Chessman.underCheck(chessmanType, board) == null) && Chessman.validMoves(board.getKing(chessmanType), board).isEmpty() && !board.valid(chessmanType);

    }

    public boolean isStaleMate(ChessmanType chessmanType) {
        return (board.getFriends(chessmanType).size() == 1) && (Chessman.underCheck(chessmanType, board) == null) && (Chessman.validMoves(board.getKing(chessmanType), board).isEmpty());

    }


}
