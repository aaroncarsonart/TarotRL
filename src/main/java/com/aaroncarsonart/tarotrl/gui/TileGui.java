package com.aaroncarsonart.tarotrl.gui;

import javax.swing.*;
import java.awt.*;

/**
 * The goal is to have a rigid, tiled grid of panels to display characters on,
 * each with their own FG and BG color, which respond to mouse events.
 */
public class TileGui {


    public void setup() {
        JFrame frame = new JFrame("My Tile Gui");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Container contentPane = frame.getContentPane();
        contentPane.setBackground(Color.DARK_GRAY);
        contentPane.setForeground(Color.LIGHT_GRAY);

        frame.pack();
        frame.setSize(800, 400);
        //createGameDisplay();
        frame.setResizable(false);
        frame.setLocation(100, 100);
        frame.setVisible(true);


    }


    public static void main(String[] args) {
        TileGui tileGui = new TileGui();
        tileGui.setup();
    }
}
