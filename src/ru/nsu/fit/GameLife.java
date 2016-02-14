package ru.nsu.fit;
import ru.nsu.fit.pixel2d.PixelDrawer;
import ru.nsu.fit.pixel2d.vectors.Vec2d;
import ru.nsu.fit.pixel2d.vectors.Vec2dI;

import javax.swing.SwingUtilities;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.BorderFactory;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class GameLife {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }

    private static void createAndShowGUI() {
        System.out.println("Created GUI on EDT? "+
                SwingUtilities.isEventDispatchThread());
        JFrame f = new JFrame("Swing Paint Demo");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.add(new MyPanel());
        f.pack();
        f.setVisible(true);
    }
}

class MyPanel extends JPanel {

    private Vec2dI p0 = new Vec2dI(300, 300);
    private Vec2dI p1 = new Vec2dI(200, 250);
    private Vec2dI p2 = new Vec2dI(420, 104);
    private int hexDiameter = 50;
    PixelDrawer drawer = new PixelDrawer();

    public MyPanel() {
        setBorder(BorderFactory.createLineBorder(Color.black));

        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                changeCoord(e.getButton(), new Vec2dI(e.getX(), e.getY()));
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent e) {
                int b1 = MouseEvent.BUTTON1_DOWN_MASK;
                int b3 = MouseEvent.BUTTON3_DOWN_MASK;
                int button = (e.getModifiersEx() & b1) == b1 ? 1 : (e.getModifiersEx() & b3) == b3 ? 3 : 0;
                changeCoord(button, new Vec2dI(e.getX(), e.getY()));
            }
        });

    }

    private void changeCoord(int button, Vec2dI p) {
        if (button == 1) {
            repaint();
            if (!p1.eq(p)) p1 = p;
            repaint();
        } else if (button == 3) {
            repaint();
            if (!p2.eq(p)) p2 = p;
            repaint();
        }
    }

    public Dimension getPreferredSize() {
        return new Dimension(600, 600);
    }


    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        drawHexAround(g);
    }

    protected void drawHexAround(Graphics g) {
//        drawer.drawFillTriangle(g, p0, p1, p2, Color.black);
        drawer.drawFillHexagonal(g, new Vec2dI(500, 500), 200, Color.black);
        // тест на отсутствие полосочки
        drawer.drawFillTriangle(g, new Vec2dI(100, 100), new Vec2dI(150, 50), new Vec2dI(200, 100), Color.black);
        drawer.drawFillTriangle(g, new Vec2dI(100, 100), new Vec2dI(150, 150), new Vec2dI(200, 100), Color.black);
        // тест на 3 точки на одной прямой
        drawer.drawFillTriangle(g, new Vec2dI(200, 200), new Vec2dI(400, 250), new Vec2dI(600, 300), Color.black);
        // толстые линии
        drawer.drawLine(g, new Vec2dI(500, 100), p2, (int) new Vec2d(p1).lenght(), Color.black);
    }

}