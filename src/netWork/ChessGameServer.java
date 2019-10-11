package netWork;

import GUI.ChessBoardUI;
import GUI.NetworkUI;
import chessEngine.*;
import chessEngine.player.*;

import javax.swing.*;
import java.io.*;
import java.net.*;


public class ChessGameServer implements Runnable {
    private ServerSocket serverSocket = null;
    private ObjectInputStream serverScanner;
    private ObjectOutputStream printWriter;
    private Socket socket = null;
    private static final int port = 6547;

    public ChessGameServer() {
        try {
            serverSocket = new ServerSocket(port);

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Couldn't attach to port");
        }
      Thread thread = new Thread(this);
        thread.start();

    }

    private void serveClient() {
        try {
            printWriter = new ObjectOutputStream(socket.getOutputStream());
            Thread.sleep(100);
            serverScanner = new ObjectInputStream(socket.getInputStream());

        } catch (
                IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        HumanNetworkPlayer humanPlayer = new HumanNetworkPlayer(ChessmanType.WHITE,printWriter);
        NetworkPlayer networkPlayer = new NetworkPlayer(ChessmanType.BLACK, serverScanner);
        ChessGame chessGame = new ChessGame(humanPlayer, networkPlayer,BoardFormat.NORMAL,null);
        new ChessBoardUI(chessGame);

        if (chessGame.gameOver(humanPlayer.getChessmanType())){
            try {
                socket.close();
            } catch (IOException e) {
               JOptionPane.showMessageDialog(null,"Connection disconnected");
            }
        }
    }


    @Override
    public void run() {

        try {
            socket = serverSocket.accept();
        } catch (IOException e1) {
            JOptionPane.showMessageDialog(null, "Couldn't find host");
        }
        serveClient();


    }
}
