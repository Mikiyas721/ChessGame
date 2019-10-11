package netWork;

import GUI.ChessBoardUI;
import GUI.List;
import chessEngine.*;
import chessEngine.player.*;

import javax.swing.*;
import java.io.*;
import java.net.*;


public class ChessGameClient implements Runnable {
    private Socket socket = null;
    private ObjectInputStream clientScannner;
    private ObjectOutputStream clientWriter;
    private static final int port = 6547;
    InetAddress inetAddress;
    public ChessGameClient(){
        Thread thread = new Thread(this);
        thread.start();
    }
    private void accessServer(){
        try {
            clientWriter = new ObjectOutputStream(socket.getOutputStream());

            Thread.sleep(100);

            clientScannner = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,"Couldn't get stream");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        HumanNetworkPlayer humanPlayer = new HumanNetworkPlayer(ChessmanType.BLACK,clientWriter);
        NetworkPlayer networkPlayer = new NetworkPlayer(ChessmanType.WHITE,clientScannner);
        ChessGame chessGame = new ChessGame(networkPlayer,humanPlayer,BoardFormat.FLIPPED,null);
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
            String IP = JOptionPane.showInputDialog("Input the IP address of the server.\nTo get the server ip address:\n 1.Go to server command window \n 2. Type ip config \n 3. Get the Ethernet IP Address \n 4.Type the IP Address into this dialog");
            if (IP!=null) {
                socket = new Socket(IP,port);
                accessServer();
            }
            else new List();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,"Couldn't connect to server");
        }

    }
}
