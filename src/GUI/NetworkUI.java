package GUI;


import netWork.ChessGameClient;
import netWork.ChessGameServer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;


public class NetworkUI extends JFrame {

    public NetworkUI() {
        ImagePanel imagePanel = new ImagePanel();
        setContentPane(imagePanel);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);
        setSize(400, 400);
        setLocation(400, 100);

        Container container = getContentPane();
        container.setLayout(new FlowLayout());
        JButton create = new JButton("Create");
        create.setOpaque(false);
        create.setBorderPainted(false);
        create.setContentAreaFilled(false);
        create.addActionListener(new CreateListener());

        JButton join = new JButton("Join");
        join.setOpaque(false);
        join.setBorderPainted(false);
        join.setContentAreaFilled(false);
        join.addActionListener(new JoinListener());


        container.add(create);
        container.add(join);
        setVisible(true);
        addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent windowEvent) {

            }

            @Override
            public void windowClosing(WindowEvent windowEvent) {
                new List();
                setDefaultCloseOperation(DISPOSE_ON_CLOSE);
            }

            @Override
            public void windowClosed(WindowEvent windowEvent) {

            }

            @Override
            public void windowIconified(WindowEvent windowEvent) {

            }

            @Override
            public void windowDeiconified(WindowEvent windowEvent) {

            }

            @Override
            public void windowActivated(WindowEvent windowEvent) {

            }

            @Override
            public void windowDeactivated(WindowEvent windowEvent) {

            }
        });
    }

    private class CreateListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {
            ChessGameServer server = new ChessGameServer();
            setVisible(false);

        }
    }

    private class JoinListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent actionEvent) {

            ChessGameClient client = new ChessGameClient();
            setVisible(false);

        }
    }

    private class ImagePanel extends JComponent {
        private Image image;
        ImageIcon background = new ImageIcon(getClass().getResource("/pieces/Network.PNG"));

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
