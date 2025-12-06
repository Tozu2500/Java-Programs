package com.tetris;

import javax.swing.SwingUtilities;

public class TetrisGame {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GameFrame frame = new GameFrame();
            frame.setVisible(true);
        });
    }

}
