package GUI;

import chessEngine.BoardFormat;
import chessEngine.ChessGame;
import chessEngine.ChessmanType;
import chessEngine.player.ComputerPlayer;
import chessEngine.player.HumanPlayer;
import chessEngine.player.Player;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class List extends JFrame {
    public List() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocation(300, 30);
        setSize(700, 700);
        setIconImage(new ImageIcon(getClass().getResource("/pieces/IconImage.png")).getImage());

        ImagePanel panel = new ImagePanel();
        setContentPane(panel);


        Container container = getContentPane();
        container.setLayout(null);
        // container.add(panel);

        //container.setLayout(new GridLayout());


        // JPanel panel = new JPanel();


        //panel.add(new JLabel(background, JLabel.CENTER));

        //label.add(panel);
        JButton button0 = new JButton("Resume");
        button0.setSize(100, 25);

        button0.setOpaque(false);
        button0.setContentAreaFilled(false);
        container.add(button0);
        button0.addActionListener(new Resume());

        JButton button1 = new JButton("One player");
        button1.setSize(100, 25);

        button1.setOpaque(false);
        button1.setContentAreaFilled(false);
        container.add(button1);
        button1.addActionListener(new OnePlayer());

        JButton button2 = new JButton("Two player");
        button2.setSize(100, 25);

        button2.setOpaque(false);
        button2.setContentAreaFilled(false);
        container.add(button2);
        button2.addActionListener(new TwoPlayer());

        JButton button3 = new JButton("Network Player");
        button3.setSize(150, 25);

        button3.setOpaque(false);
        button3.setContentAreaFilled(false);
        container.add(button3);
        button3.addActionListener(new MultiPlayer());
        setVisible(true);
        addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent componentEvent) {
                Container container = getContentPane();
                int height = container.getHeight() - 100;
                int width = container.getWidth();

                button0.setLocation((width - 100) / 2, height / 5);
                button1.setLocation(((width - 100) / 2), (int) (height * 0.4) + 25);
                button2.setLocation(((width - 100) / 2), (int) (height * 0.6) + 50);
                button3.setLocation((width - 150) / 2, (int) (height * 0.8) + 75);

            }

            @Override
            public void componentMoved(ComponentEvent componentEvent) {

            }

            @Override
            public void componentShown(ComponentEvent componentEvent) {

            }

            @Override
            public void componentHidden(ComponentEvent componentEvent) {

            }
        });
        button0.setLocation((700 - 100) / 2, 700 / 5);
        button1.setLocation(((700- 100) / 2), (int) (700 * 0.4) + 25);
        button2.setLocation(((700- 100) / 2), (int) (700 * 0.6) + 50);
        button3.setLocation((700 - 150) / 2, (int) (700 * 0.8) + 75);

    }

    public static void main(String[] args) {
        new List();

    }

    private class Resume implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            String saved = null;
            try {
                FileReader fileReader = new FileReader("saved\\saved.txt");
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                saved = bufferedReader.readLine();
                if (saved != null) {
                    String read[] = saved.split(";");
                    if (read[1].charAt(0) == 'C') {
                        Player humanplayer1 = new HumanPlayer(ChessmanType.WHITE);
                        Player computerPlayer = new ComputerPlayer(ChessmanType.BLACK, read[1].charAt(1));
                        ChessGame chessGame = new ChessGame(humanplayer1, computerPlayer, BoardFormat.NORMAL, read[0]);
                        new ChessBoardUI(chessGame);
                        setVisible(false);

                    } else {
                        Player humanplayer1 = new HumanPlayer(ChessmanType.WHITE);
                        Player humanPlayer2 = new HumanPlayer(ChessmanType.BLACK);
                        ChessGame chessGame = new ChessGame(humanplayer1, humanPlayer2, BoardFormat.NORMAL, read[0]);
                        new ChessBoardUI(chessGame);
                        setVisible(false);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "No saved Game");
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private class TwoPlayer implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {


            Player humanplayer1 = new HumanPlayer(ChessmanType.WHITE);
            Player humanPlayer2 = new HumanPlayer(ChessmanType.BLACK);
            ChessGame chessGame = new ChessGame(humanplayer1, humanPlayer2, BoardFormat.NORMAL, null);
            new ChessBoardUI(chessGame);
            setVisible(false);

        }
    }

    private class OnePlayer implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            int depth;
            String[] options = {"Easy", "Medium", "Hard"};
            depth = JOptionPane.showOptionDialog(null, "Choose the level that you want", "Chess Game", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
            Player humanPlayer2 = null;
            if (depth == 0) {
                humanPlayer2 = new ComputerPlayer(ChessmanType.BLACK, 1);
                Player humanPlayer1 = new HumanPlayer(ChessmanType.WHITE);
                ChessGame chessGame = new ChessGame(humanPlayer1, humanPlayer2, BoardFormat.NORMAL, null);
                new ChessBoardUI(chessGame);
                setVisible(false);
            } else if (depth == 1) {
                humanPlayer2 = new ComputerPlayer(ChessmanType.BLACK, 2);
                Player humanPlayer1 = new HumanPlayer(ChessmanType.WHITE);
                ChessGame chessGame = new ChessGame(humanPlayer1, humanPlayer2, BoardFormat.NORMAL, null);
                new ChessBoardUI(chessGame);
                setVisible(false);

            } else if (depth == 2) {
                humanPlayer2 = new ComputerPlayer(ChessmanType.BLACK, 3);
                Player humanPlayer1 = new HumanPlayer(ChessmanType.WHITE);
                ChessGame chessGame = new ChessGame(humanPlayer1, humanPlayer2, BoardFormat.NORMAL, null);
                new ChessBoardUI(chessGame);
                setVisible(false);

            }

        }
    }

    private class MultiPlayer implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            new NetworkUI();
            setVisible(false);
        }
    }

    ImagePanel panel = new ImagePanel();

    private class ImagePanel extends JComponent {
        private Image image;
        ImageIcon background = new ImageIcon(getClass().getResource("/pieces/chess-competition-1024.jpg"));

        public ImagePanel() {
            this.image = background.getImage();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
