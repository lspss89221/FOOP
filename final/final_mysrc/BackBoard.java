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
import java.io.*;


/*
color

0 = yellow / dessert 
1 = green / pasture
2 = red / hills
3 = dark green / forest
4 = grey / mountain
5 = orange / field

*/

public class BackBoard extends JPanel {

    public ArrayList<Integer> color = new ArrayList<Integer>();
    public ArrayList<Integer> number = new ArrayList<Integer>();
    public ArrayList<Point[]> point = new ArrayList<Point[]>();
    private static final long serialVersionUID = 1L;
    private final int WIDTH = 1200;
    private final int HEIGHT = 800;

    private final int W2 = WIDTH / 2;
    private final int H2 = HEIGHT / 2;
    
    private Font font = new Font("Arial", Font.BOLD, 24);
    FontMetrics metrics;

    int[] pointX = {  504, 600, 695,
                    456, 552, 647, 742,
                    456, 552, 647, 742,
                  409, 504, 599, 694, 789,                
                  409, 504, 599, 694, 789,
                361, 456, 552, 647, 742, 837,
                361, 456, 552, 647, 742, 837,
                  409, 504, 599, 694, 789,                
                  409, 504, 599, 694, 789,
                    456, 552, 647, 742,
                    456, 552, 647, 742,
                      504, 600, 695,

    };

    int[] pointY = {  181, 181, 181, 
                    206, 206, 206, 206,
                    264, 264, 264, 264,
                  288, 288, 288, 288, 288,
                  346, 346, 346, 346, 346,
                371, 371, 371, 371, 371,371,
                429, 429, 429, 429, 429,429,
                  453, 453, 453, 453, 453,
                  511, 511, 511, 511, 511,
                    536, 536, 536, 536,
                    594, 594, 594, 594,
                      619, 619, 619
    };
    int[] roadA = { 0, 0, 1, 1, 2, 2,
                    3, 4, 5, 6,
                    7, 7, 8, 8, 9, 9, 10, 10,
                    11, 12, 13, 14, 15,
                    16, 16, 17, 17, 18, 18, 19, 19, 20, 20,
                    21, 22, 23, 24, 25, 26,
                    27, 28, 28, 29, 29, 30, 30, 31, 31, 32,
                    33, 34, 35, 36, 37,
                    38, 39, 39, 40, 40, 41, 41, 42,
                    43, 44, 45, 46,
                    47, 48, 48, 49, 49, 50
    };    
    int[] roadB = { 3, 4, 4, 5, 5, 6,
                    7, 8, 9, 10,
                    11, 12, 12, 13, 13, 14, 14, 15,
                    16, 17, 18, 19, 20,
                    21, 22, 22, 23, 23, 24, 24, 25, 25, 26,
                    27, 28, 29, 30, 31, 32,
                    33, 33, 34, 34, 35, 35, 36, 36, 37, 37,
                    38, 39, 40, 41, 42,
                    43, 43, 44, 44, 45, 45, 46, 46,
                    47, 48, 49, 50,
                    51, 51, 52, 52, 53, 53
    };


    public BackBoard() {
        setPreferredSize(new Dimension(WIDTH, HEIGHT));
        initiateValue();
        Collections.shuffle(color);
        Collections.shuffle(number);
    }
    
    void initiateValue(){
        //System.out.println("I'm here.");
        for(int i=0;i<19;i++){
            Point[] tmp= new Point[6];
            point.add(tmp);
        }
        color.add(0);
        for(int i=0;i<3;i++){
            color.add(2);
            color.add(4);
        }
        
        for(int i=0;i<4;i++){
            color.add(1);
            color.add(3);
            color.add(5);
        }
        number.add(0);
        number.add(2);
        number.add(3);
        number.add(3);
        number.add(4);
        number.add(4);
        number.add(5);
        number.add(5);
        number.add(6);
        number.add(6);
        number.add(8);
        number.add(8);
        number.add(9);
        number.add(9);
        number.add(10);
        number.add(10);
        number.add(11);
        number.add(11);
        number.add(12);

    }
 
    // no s
    @Override
    public void paintComponent(Graphics g) {

        Graphics2D g2d = (Graphics2D) g;
        Point origin = new Point(600, 400);

        g2d.setStroke(new BasicStroke(4.0f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER));
        g2d.setFont(font);
        metrics = g.getFontMetrics();


        drawHexGridLoop(g2d, origin, 5, 50, 5);
        drawHarbor(g2d);
        drawShop(g2d);
        drawPlayerStat(g2d);
        drawRobber(g2d);
        drawRoadLoop(g2d);
        drawSettleAndCity(g2d);
    }

    void drawHarbor(Graphics g){

    }

    void drawShop(Graphics g){
        int x,y;
        String text1 = "Road: 1 Brick + 1 Lumber\nSettlement: 1 Brick + 1 Lumber + 1 Wool + 1 Grain\nCity: 3 Ore + 2 Grain\nDev Card: 1 Ore + 1 Wool + 1 Grain\n";
        x=350;
        y=25;

        g.setColor(new Color(0xEAC100));
        g.drawRect(330,10,520,140);
        g.setColor(new Color(0xFFF4C1));
        g.fillRect(330,10,520,140);
        
        g.setColor(new Color(0x272727));
        g.setFont(new Font("Arial", Font.BOLD, 20));
        for (String line : text1.split("\n"))
            g.drawString(line, x, y+=g.getFontMetrics().getHeight());
    }

    void drawRobber(Graphics g){
        for(int i=0;i<19;i++){
            int sumX = 0;
            int sumY = 0;
            for(int j=0;j<6;j++){
                sumX += (int)point.get(i)[j].getX();
                sumY += (int)point.get(i)[j].getY();
            }
            sumX = sumX/6-8;
            sumY = sumY/6-30;

            g.setColor(new Color(0x000000));
            g.fillOval(sumX,sumY,20,20);
        }
    }


    void drawPlayerStat(Graphics g){
        int x,y;
        String text1 = "Player A Status\nPlayer A Status\nPlayer A Status\nPlayer A Status\n";
        g.setColor(new Color(0xCE0000));
        g.setFont(new Font("Arial", Font.BOLD, 20));
        //g.drawString(text1, 100, 100);
        x=100;
        y=100;
        for (String line : text1.split("\n")){
            g.drawString(line, x, y);
            y+=g.getFontMetrics().getHeight();
        }
        String text2 = "Player B Status";
        g.setColor(new Color(0xCE0000));
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString(text2, 100, 500);
        
        String text3 = "Player C Status";
        g.setColor(new Color(0xCE0000));
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString(text3, 900, 100);
        
        String text4 = "Player D Status";
        g.setColor(new Color(0xCE0000));
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString(text4, 900, 500);
           
    }

    /*
    blue:   0x0000C6
    red:    0xEA0000
    purple: 0x6F00D2
    black:  0x000000
    */
    void drawSettleAndCity(Graphics g){
        Random rnd = new Random();
        for(int i=0;i<54;i++){
            if(rnd.nextInt(100)>50){
                MyPolygon triangle = new MyPolygon(pointX[i], pointY[i], 3);    
                g.setColor(new Color(0x0000C6));
                g.fillPolygon(triangle);
            }
        }
        for(int i=0;i<54;i++){
            if(rnd.nextInt(100)>50){
                MyPolygon rectangle = new MyPolygon(pointX[i], pointY[i], 4);    
                g.setColor(new Color(0x0000C6));
                g.fillPolygon(rectangle);
            }
        }
    }



    void drawRoadLoop(Graphics g){
        for(int i=0;i<pointX.length;i++){
            Graphics2D g2 = (Graphics2D) g;
            String text = String.format("%d",i);
            g2.setColor(new Color(0x2828FF));
            g2.setFont(new Font("Arial", Font.BOLD, 12));
            g2.drawString(text, pointX[i], pointY[i]);
        
        }

        for(int i=0;i<72;i++){
            Graphics2D g2 = (Graphics2D) g;
            String text = String.format("%d",i);
            g2.setColor(new Color(0xCE0000));
            g2.setFont(new Font("Arial", Font.BOLD, 12));
            g2.drawString(text, (pointX[roadA[i]]+pointX[roadB[i]])/2, (pointY[roadA[i]]+pointY[roadB[i]])/2);
        }
        //TODO


        for(int i=0;i<72;i++){
            //TODO
            Graphics2D g2 = (Graphics2D) g;
            g2.setStroke(new BasicStroke(5));
            g2.setColor(new Color(0x000093));
            g2.drawLine(pointX[roadA[i]],pointY[roadA[i]],pointX[roadB[i]],pointY[roadB[i]]);
        }
    }

    void drawHexGridLoop(Graphics g, Point origin, int size, int radius, int padding) {
        double ang30 = Math.toRadians(30);
        double xOff = Math.cos(ang30) * (radius + padding);
        double yOff = Math.sin(ang30) * (radius + padding);
        int half = size / 2;
        int index = 0;
        for (int row = 0; row < size; row++) {
            int cols = size - java.lang.Math.abs(row - half);

            for (int col = 0; col < cols; col++) {
                int xLbl = row < half ? col - row : col - half;
                int yLbl = row - half;
                int x = (int) (origin.x + xOff * (col * 2 + 1 - cols));
                int y = (int) (origin.y + yOff * (row - half) * 3);
                
                int colorValue = color.get(index);
                drawHex(g, x, y, radius, colorValue,index);
                index++;
            }
        }
    }

    private int convert2Color(int color){
        switch(color){
            case 0: return 0xEAC100;
            case 1: return 0x00BB00;
            case 2: return 0x930000;
            case 3: return 0x006030;
            case 4: return 0x6C6C6C;
            case 5: return 0xFF9224;
            default: return 0x000000;
        }
    }

    void drawHex(Graphics g, int x, int y, int r, int color, int index) {
        Hexagon hex = new Hexagon(x, y, r);
        int hexColor = convert2Color(color);
        
        Point[] tmp = new Point[6]; 
        for(int i=0;i<6;i++)
           tmp[i] = new Point(hex.xpoints[i],hex.ypoints[i]);
        point.remove(index);
        point.add(index,tmp);   

        int numberValue = number.get(index);
        String text = String.format("%d",numberValue);
        
        int w = metrics.stringWidth(text);
        int h = metrics.getHeight();

        g.setColor(new Color(hexColor));
        g.fillPolygon(hex);
        g.setColor(new Color(0xFFDD88));
        g.drawPolygon(hex);
        g.setColor(new Color(0xFFFFFF));
        g.drawString(text, x - w/2, y + h/2);
    }
    
}
