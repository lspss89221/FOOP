import java.awt.*;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Stroke;

import javax.swing.*;
import javax.swing.JFrame;
import javax.swing.JPanel;

import java.awt.event.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.event.MouseInputAdapter;


import java.util.*;
import java.lang.*;
import java.*;

public class GUI{
  
    JFrame frame;
    static BackBoard bLayer;

    public GUI(){
        frame = new JFrame();
        frame.setPreferredSize(new Dimension(1200, 800));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        bLayer = new BackBoard();
        //Create and set up the content pane.
        frame.setContentPane(bLayer);
        
        //Display the window.
        frame.pack();
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                GUI windows = new GUI();
                windows.frame.setVisible(true);
            }
        });

        String[] names = new String[4];
        names[0] = "Mark";
        names[1] = "Kevin";
        names[2] = "Jennie";
        names[3] = "Wombat";

        Game game = new Game(4, names);

        while(!game.isWin())
        {
            game.playGame();
            bLayer.repaint();
        }
    }
}

