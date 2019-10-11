package GUI;

import javax.swing.*;
import javax.xml.stream.Location;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import chessEngine.*;
import chessEngine.pieces.*;
import chessEngine.player.*;

public class ChessBoardUI extends JFrame implements ActionListener, MoveListener, ComputerListner {

    private List<Position> clicked = new ArrayList<>();
    private ChessGame chessGame;
    private Color LIGHT = new Color(232, 235, 239);
    private Color DARK = new Color(125, 135, 150);
    private JButton boardArray[][] = new JButton[8][8];
    private JLabel whiteArray[][] = new JLabel[8][2];
    private JLabel blackArray[][] = new JLabel[8][2];
    private JLabel label = new JLabel();

    private Board board;
    private int i = 0;
    private int j = 0;


    public ChessBoardUI(ChessGame chessGame) {
        ImagePanel panell = new ImagePanel();
        setContentPane(panell);
        setSize(800, 800);
        ImageIcon icon = new ImageIcon(getClass().getResource("/pieces/IconImage.png"));
        setIconImage(icon.getImage());
        setLocation(300, 0);
        GridLayout gridLayout = new GridLayout(8, 8);
        JPanel board = new JPanel(gridLayout);
        JPanel whiteboard = new JPanel(new GridLayout(8, 2));
        whiteboard.setOpaque(false);
        JPanel blackboard = new JPanel(new GridLayout(8, 2));
        blackboard.setOpaque(false);
        //JPanel mainPanel = new JPanel(new GridLayout(1, 3));
        whiteboard.setSize(100, 500);


        blackboard.setSize(100, 500);
        // black.setLayout(new GridLayout(2, 8));


        Container container = getContentPane();


        container.setLayout(null);

        //panel.setPreferredSize(new Dimension(container.getHeight(),container.getWidth()));
        // mainPanel.setSize(700, 700);
        board.setSize(500, 500);
        board.setLayout(gridLayout);


        for (int i = 7; i >= 0; i--) {
            for (int j = 0; j < 8; j++) {
                boardArray[i][j] = new JButton();
                boardArray[i][j].setBackground((i + j) % 2 == 0 ? DARK : LIGHT);
                boardArray[i][j].addActionListener(this);
                board.add(boardArray[i][j]);
            }
        }
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 2; j++) {
                whiteArray[i][j] = new JLabel("");
                whiteboard.add(whiteArray[i][j]);
            }
        }
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 2; j++) {
                blackArray[i][j] = new JLabel("");
                blackboard.add(blackArray[i][j]);
            }
        }  // System.out.println(container.getLocation());

        container.add(whiteboard);
        container.add(board);
        container.add(blackboard);
        container.add(label);


        board.setLocation(150, 150);
        whiteboard.setLocation(40, 150);
        blackboard.setLocation(660, 150);

        label.setLocation(150, 100);
        label.setSize(200, 50);


        this.chessGame = chessGame;
        this.chessGame.getPlayer1().addListner(this);
        this.chessGame.getPlayer2().addListner(this);
        if (chessGame.getPlayer1() instanceof ComputerPlayer) {
            ((ComputerPlayer) chessGame.getPlayer1()).addTextListner(this);
        } else if (chessGame.getPlayer2() instanceof ComputerPlayer) {
            ((ComputerPlayer) chessGame.getPlayer2()).addTextListner(this);
        }
        this.board = this.chessGame.getBoard();
        applyImageIcon();
        setVisible(true);
        this.chessGame.start();
        // this.chessGame.setGameStateListener(this);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                super.windowClosing(windowEvent);
                if ((chessGame.getPlayer1() instanceof HumanPlayer && chessGame.getPlayer2() instanceof HumanPlayer) ||
                        (chessGame.getPlayer1() instanceof ComputerPlayer || chessGame.getPlayer2() instanceof ComputerPlayer)) {
                    String[] options = {"Save", "Don't Save", "Cancel"};
                    int X = JOptionPane.showOptionDialog(null, "Do you want to save this Game?", "Chess Game", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
                    if (X == 0) {
                        chessGame.saveGame();
                        setVisible(false);
                        new GUI.List();
                    } else if (X == 1) {
                        setVisible(false);
                        new GUI.List();
                    }

                    System.out.println(X);

                    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

                } else {

                    setVisible(false);
                    new GUI.List();
                }
            }
        });
        addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent componentEvent) {
                Container container = getContentPane();
                int height = container.getHeight();
                int width = container.getWidth();
                board.setLocation((width - 500) / 2, (height - 500) / 2);
                whiteboard.setLocation(((width - 500) / 2) - 105, (height - 500) / 2);
                blackboard.setLocation(((width + 500) / 2) + 5, (height - 500) / 2);
                label.setLocation((width - 500) / 2, ((height - 500) / 2) - 50);

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


        board.setLocation((800 - 500) / 2, (800 - 500) / 2);
        whiteboard.setLocation(((800 - 500) / 2) - 105, (800 - 500) / 2);
        blackboard.setLocation(((800 + 500) / 2) + 5, (800 - 500) / 2);
        label.setLocation((800 - 500) / 2, ((800 - 500) / 2) - 50);


    }

    private Point initialLocation = null;
    private Point terminalLocation = null;
    private Position initial = null;
    private Position terminal = null;
    private boolean ispawn = false;
    private Pawn pawn = null;
    private ImageIcon initialImage;
    private Timer timer = new Timer(50, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            boolean stop = paintComponent(getGraphics());
            if (!stop){
                timer.stop();
            }
        }
    });


    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        //Chessman chessman = ;


        if (chessGame.getCurrentPlayer() instanceof HumanPlayer) {
            HumanPlayer humanPlayer = (HumanPlayer) chessGame.getCurrentPlayer();

            if (initial == null) {

                A:
                for (int i = 0; i < 8; i++) {
                    for (int j = 0; j < 8; j++) {
                        if (actionEvent.getSource() == boardArray[i][j]) {
                            Position p = new Position(i, j);
                            Chessman c = chessGame.getBoard().getChessmanAt(p);
                            if (c != null && c.getChessmanType() == chessGame.getCurrentPlayer().getChessmanType()) {
                                initial = p;
                                clicked = Chessman.validMoves(c, board);
                                if (c instanceof Pawn) {
                                    ispawn = true;
                                    pawn = (Pawn) c;
                                }

                                for (Position position : clicked) {
                                    if (boardArray[position.getRow()][position.getColumn()].getIcon() == null) {
                                        boardArray[position.getRow()][position.getColumn()].setBackground(Color.CYAN);
                                    } else
                                        boardArray[position.getRow()][position.getColumn()].setBackground(new Color(255, 40, 0));
                                }
                            }
                            break A;
                        }
                    }
                }
            } else {
                boolean canMove = false;
                A:
                for (int i = 0; i < 8; i++) {
                    for (int j = 0; j < 8; j++) {
                        if (actionEvent.getSource() == boardArray[i][j]) {
                            terminal = new Position(i, j);
                            break A;
                        }
                    }
                }
                for (Position position : clicked) {
                    if (terminal.getRow() == position.getRow() && terminal.getColumn() == position.getColumn()) {
                        canMove = true;
                    }
                }
                if (canMove && ispawn &&
                        (terminal.getRow() == (pawn.getInitialrow() + 6) || terminal.getRow() == (pawn.getInitialrow() - 6))) {
                    List<Chessman> removed = board.getRemovedChessman();

                    String text = "Choose your replacement from what you have lost\n1.Queen\n2.Bishop\n3.Rook\n4.Knight";
                    String enpassent = JOptionPane.showInputDialog(text);
                    int choice = Integer.parseInt(enpassent);
                    if (choice == 1) {
                        try {
                            Queen queen = removed.stream().
                                    filter(chessman -> chessman instanceof Queen && chessman.getChessmanType() == pawn.getChessmanType())
                                    .map(chessman -> (Queen) chessman).findAny().get();
                            Position remove = queen.getinitialPosition();
                            humanPlayer.move(initial, terminal, pawn, queen, remove);
                        } catch (NoSuchElementException e) {
                            JOptionPane.showMessageDialog(null, "You haven't lost a Queen!!Try again");
                        }
                    } else if (choice == 2) {
                        try {
                            Bishop bishop = removed.stream().
                                    filter(chessman -> chessman instanceof Bishop && chessman.getChessmanType() == pawn.getChessmanType())
                                    .map(chessman -> (Bishop) chessman).findAny().get();
                            Position remove = bishop.getinitialPosition();
                            humanPlayer.move(initial, terminal, pawn, bishop, remove);
                        } catch (NoSuchElementException e) {
                            JOptionPane.showMessageDialog(null, "You haven't lost a Bishop!!Try again");
                        }
                    } else if (choice == 3) {
                        try {
                            Rook rook = removed.stream().
                                    filter(chessman -> chessman instanceof Rook && chessman.getChessmanType() == pawn.getChessmanType())
                                    .map(chessman -> (Rook) chessman).findAny().get();
                            Position remove = rook.getinitialPosition();
                            humanPlayer.move(initial, terminal, pawn, rook, remove);
                        } catch (NoSuchElementException e) {
                            JOptionPane.showMessageDialog(null, "You haven't lost a Rook!!Try again");
                        }
                    } else if (choice == 4) {
                        try {
                            Knight knight = removed.stream().
                                    filter(chessman -> chessman instanceof Knight && chessman.getChessmanType() == pawn.getChessmanType())
                                    .map(chessman -> (Knight) chessman).findAny().get();
                            Position remove = knight.getinitialPosition();
                            humanPlayer.move(initial, terminal, pawn, knight, remove);
                        } catch (NoSuchElementException e) {
                            JOptionPane.showMessageDialog(null, "You haven't lost a Knight!!Try again");
                        }
                    }


                } else if (canMove) {
                   // System.out.println(initialLocation = boardArray[initial.getRow()][initial.getColumn()].getLocation());
                    //System.out.println(terminalLocation = boardArray[terminal.getRow()][terminal.getColumn()].getLocation());
                    initialImage = getChessmanImage(board.getChessmanAt(initial));
                    //timer.start();

                    humanPlayer.move(initial, terminal);
                }
                initial = null;
                terminal = null;
                ispawn = false;
                pawn = null;
                for (Position position : clicked) {

                    boardArray[position.getRow()][position.getColumn()].setBackground((position.getRow() + position.getColumn()) % 2 == 0 ? DARK : LIGHT);
                }
                clicked = null;
            }
        }
    }

    private ImageIcon bishopWhite = new ImageIcon(getClass().getResource("/pieces/White_Bishop.png"));
    private ImageIcon bishopBlack = new ImageIcon(getClass().getResource("/pieces/Black_Bishop.png"));
    private ImageIcon rookWhite = new ImageIcon(getClass().getResource("/pieces/White_Rook.png"));
    private ImageIcon rookBlack = new ImageIcon(getClass().getResource("/pieces/Black_Rook.png"));
    private ImageIcon knightWhite = new ImageIcon(getClass().getResource("/pieces/White_Knight.png"));
    private ImageIcon knightBlack = new ImageIcon(getClass().getResource("/pieces/Black_Knight.png"));
    private ImageIcon queenWhite = new ImageIcon(getClass().getResource("/pieces/White_Queen.png"));
    private ImageIcon queenBlack = new ImageIcon(getClass().getResource("/pieces/Black_Queen.png"));
    private ImageIcon kingWhite = new ImageIcon(getClass().getResource("/pieces/White_King.png"));
    private ImageIcon kingBlack = new ImageIcon(getClass().getResource("/pieces/Black_King.png"));
    private ImageIcon pawnWhite = new ImageIcon(getClass().getResource("/pieces/White_Pawn.png"));
    private ImageIcon pawnBlack = new ImageIcon(getClass().getResource("/pieces/Black_Pawn.png"));

    private void applyImageIcon() {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                boardArray[i][j].setIcon(null);
            }
        }
        chessGame.getBoard().getChessmen().forEach(chessman ->
                boardArray[chessman.getRow()][chessman.getColumn()].setIcon(getChessmanImage(chessman)));
    }

    private ImageIcon getChessmanImage(Chessman chessman) {
        if (chessman instanceof King)
            return chessman.getChessmanType() == ChessmanType.WHITE ? kingWhite : kingBlack;
        if (chessman instanceof Queen)
            return chessman.getChessmanType() == ChessmanType.WHITE ? queenWhite : queenBlack;
        if (chessman instanceof Bishop)
            return chessman.getChessmanType() == ChessmanType.WHITE ? bishopWhite : bishopBlack;
        if (chessman instanceof Knight)
            return chessman.getChessmanType() == ChessmanType.WHITE ? knightWhite : knightBlack;
        if (chessman instanceof Rook)
            return chessman.getChessmanType() == ChessmanType.WHITE ? rookWhite : rookBlack;
        if (chessman instanceof Pawn)
            return chessman.getChessmanType() == ChessmanType.WHITE ? pawnWhite : pawnBlack;
        throw new IllegalArgumentException("IllegalArgumentException");
    }

    public void applyRemovedImageIcon() {

        ChessmanType chessmanType = chessGame.getCurrentPlayer().getChessmanType();
        List<Chessman> removed = chessGame.getBoard().getRemovedByChessmanType(chessmanType);
        int size = removed.size();
        i = 0;
        j = 0;
        if (removed != null) {
            if (chessmanType == ChessmanType.WHITE) {
                for (int i = 0; i < 8; i++) {
                    for (int j = 0; j < 2; j++) {
                        whiteArray[i][j].setIcon(null);
                    }
                }
            } else for (int i = 0; i < 8; i++) {
                for (int j = 0; j < 2; j++) {
                    blackArray[i][j].setIcon(null);
                }
            }
            removed.stream().forEach(chessman -> {
                if (chessmanType == ChessmanType.WHITE) {
                    whiteArray[i][j].setIcon(getChessmanImage(chessman));
                } else blackArray[i][j].setIcon(getChessmanImage(chessman));
                if (j == 1) {
                    i++;
                    j = 0;
                } else {
                    j++;
                }
            });
        }
    }

    @Override
    public void hasMoved() {
        applyImageIcon();
        applyRemovedImageIcon();
        boolean check = chessGame.gameOver(chessGame.getCurrentPlayer().getChessmanType());
        if (check) {
            gameover();
        }
    }


    public void gameover() {
        JOptionPane.showMessageDialog(null, "Game over");
        setVisible(false);
        new GUI.List();

    }

    @Override
    public void setText() {
        label.setText("Computer:- Thinking");
    }

    @Override
    public void clearText() {
        label.setText("");
    }

    private class ImagePanel extends JComponent {
        private Image image;
        ImageIcon background = new ImageIcon(getClass().getResource("/pieces/Capture.PNG"));

        public ImagePanel() {
            this.image = background.getImage();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
        }
    }

    public int[] calculateDistance(Point initialLocation, Point terminalLocation) {
        int x = terminalLocation.x - initialLocation.x;
        int y = terminalLocation.y - initialLocation.y;
        int[] result = new int[2];
        result[0] = x;
        result[1] = y;
        return result;
    }

    Point point = null;

    public boolean paintComponent(Graphics graphics) {
        super.paintComponents(graphics);
        point = initialLocation;
        int[] distance = calculateDistance(initialLocation, terminalLocation);
        distance[0]=distance[0]/4;
        distance[1]=distance[1]/4;
        if (point.x == terminalLocation.x || point.y == terminalLocation.y) {
            return false;
        }
        //System.out.println(getComponentAt(point));
        initialImage.paintIcon(getComponentAt(point), graphics, point.x, point.y);

        //System.out.println(point.x+=distance[0]);
        point.x+=distance[0];
        //System.out.println(point.y+=distance[1]);
        point.y+=distance[1];
        return true;
    }
}
