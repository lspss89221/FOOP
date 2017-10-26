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

public class MyPolygon extends Polygon {

    private static final long serialVersionUID = 1L;

    public int SIDES;

    private Point[] points;
    private Point center = new Point(0, 0);
    private int radius = 15;
    private int rotation = 90;

    public MyPolygon(Point center, int sides) {
        SIDES = sides;
        npoints = SIDES;
        xpoints = new int[SIDES];
        ypoints = new int[SIDES];
        points = new Point[SIDES];
        this.center = center;

        updatePoints();
    }

    public MyPolygon(int x, int y, int sides) {
        this(new Point(x, y),sides);
    }

    private double findAngle(double fraction) {
        return fraction * Math.PI * 2 + Math.toRadians((rotation + 180) % 360);
    }

    private Point findPoint(double angle) {
        int x = (int) (center.x + Math.cos(angle) * radius);
        int y = (int) (center.y + Math.sin(angle) * radius);

        return new Point(x, y);
    }

    protected void updatePoints() {
        for (int p = 0; p < SIDES; p++) {
            double angle = findAngle((double) p / SIDES);
            if(SIDES==4)
                angle+=0.25*Math.PI;
            Point point = findPoint(angle);
            xpoints[p] = point.x;
            ypoints[p] = point.y;
            points[p] = point;
            //System.out.printf("%d. (%d, %d)\n", p, point.x, point.y);
        }
    }
}
