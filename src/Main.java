// This program create and edit text files

import javax.swing.*;
import java.awt.*;

public class Main extends JFrame {
    private final int ROWS = 100;
    private final int COLS = 400;
    private JTextArea textArea;

    public static void main(String[] args) {
        new Main();
    }

    public Main () {
        textArea = new JTextArea(COLS, ROWS);
        JScrollPane scroll = new JScrollPane(textArea);

        add(scroll);

        initFrame();
    }

    private void initFrame() {
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setTitle("Text Editor");
        setSize(800, 600);
//        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
