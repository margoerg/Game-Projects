/**
 * Marcel Goergens 
 * 16.07.2021
 */

import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;

public class Main extends JPanel implements KeyListener, ActionListener
{
    private Rakete r1, r2;
    private Stein[] steine = new Stein[30];
    private static Timer timer;
    private Integer punkte1 = 0;
    private Integer punkte2 = 0;
    private static double start;
    private boolean gameStarted = false;

    public static void main(String[] args)
    {
        JFrame frame = new JFrame();
        frame.setTitle("Space Race");
        frame.setResizable(false);
        frame.setLayout(new BorderLayout());
        ImageIcon icon= new ImageIcon("spaceship.png");
        frame.setIconImage(icon.getImage());

        JPanel up = new JPanel();
        up.setPreferredSize(new Dimension(700, 100));
        JPanel down = new JPanel();
        down.setPreferredSize(new Dimension(700, 100));
        JPanel left = new JPanel();
        left.setPreferredSize(new Dimension(100, 500));
        JPanel right = new JPanel();
        right.setPreferredSize(new Dimension(100, 500));

        Main main = new Main();
        timer = new Timer(16, main);
        frame.add(up, BorderLayout.NORTH);
        frame.add(down, BorderLayout.SOUTH);
        frame.add(left, BorderLayout.WEST);
        frame.add(right, BorderLayout.EAST);
        frame.add(main, BorderLayout.CENTER);
        frame.addKeyListener(main);

        frame.pack();
        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
        frame.setLocation((int)size.getWidth()/2 - frame.getWidth()/2, (int)size.getHeight()/2 - frame.getHeight()/2);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public Main()
    {
        super();
        setPreferredSize(new Dimension(500, 500));
        r1 = new Rakete(0.333*500, 500 - 20); 
        r2 = new Rakete(0.666*500, 500 - 20); 
        for(int i = 0; i < steine.length; i++)
        {
            steine[i] = new Stein();
        }
    }

    public void paint(Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(Color.BLACK);
        g2.fillRect(0, 0, getWidth(), getHeight());
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("", 0, 100));
        g2.drawString(punkte1.toString(), getWidth()/4 - 50, 480);
        g2.drawString(punkte2.toString(), getWidth()*3/4 - 5, 480);
        r1.paint(g2);
        r2.paint(g2);
        if(gameStarted)
        {
            double timerY = 500*((System.currentTimeMillis() - start)/30000);
            if(timerY >= 501)
            {
                timer.stop();
                r1.reset();
                r2.reset();
                steine = null;
                repaint();
                g2.setFont(new Font("", 0, 50));
                if(punkte1 > punkte2)
                {
                    g2.drawString("Winner", 30, 200);
                    g2.drawString("Loser", 320, 200);
                }
                if(punkte1 < punkte2)
                {
                    g2.drawString("Loser", 60, 200);
                    g2.drawString("Winner", 280, 200);
                }
                if(punkte1 == punkte2)
                {
                    g2.drawString("Draw", 195, 200);
                }
            }
            Rectangle2D.Double rect = new Rectangle2D.Double((getWidth()/2) - 2.5, timerY, 10, getHeight() - timerY);
            g2.fill(rect);
            if(steine != null)
            {
                for(int i = 0; i < steine.length; i++)
                {
                    steine[i].paint(g2);
                }
            }
        }
        else
        {
            Rectangle2D.Double rect = new Rectangle2D.Double((getWidth()/2) - 2.5, 0, 10, getHeight());
            g2.fill(rect);
        }
    }

    public void keyReleased(KeyEvent e)
    {
        switch(e.getExtendedKeyCode())
        {
            case KeyEvent.VK_W: r1.setV(0); break;
            case KeyEvent.VK_S: r1.setV(0); break;
            case KeyEvent.VK_UP: r2.setV(0); break;
            case KeyEvent.VK_DOWN: r2.setV(0); break;
            case KeyEvent.VK_SPACE: if(!gameStarted){start = System.currentTimeMillis(); timer.start(); gameStarted = true;}; break;
        }
    }

    public void keyPressed(KeyEvent e)
    {
        switch(e.getExtendedKeyCode())
        {
            case KeyEvent.VK_W: r1.setV(-5); break;
            case KeyEvent.VK_S: r1.setV(5); break;
            case KeyEvent.VK_UP: r2.setV(-5); break;
            case KeyEvent.VK_DOWN: r2.setV(5); break;
        }
    }

    public void keyTyped(KeyEvent e)
    {

    }

    public void actionPerformed(ActionEvent e)
    {
        r1.bewege();
        r2.bewege();
        for(int i = 0; i < steine.length; i++)
        {
            steine[i].bewege();
        }
        for(int i = 0; i < steine.length; i++)
        {
            if(Math.sqrt(Math.pow(r1.getX() - steine[i].getX(), 2) + Math.pow(r1.getY() - steine[i].getY(), 2)) <= r1.getR()+ steine[i].getR())
            {
                r1.reset();
            }
            if(Math.sqrt(Math.pow(r2.getX() - steine[i].getX(), 2) + Math.pow(r2.getY() - steine[i].getY(), 2)) <= r2.getR()+ steine[i].getR())
            {
                r2.reset();
            }
        }
        if(r1.getY() <= 20)
        {
            r1.reset();
            punkte1++;
        }
        if(r2.getY() <= 20)
        {
            r2.reset();
            punkte2++;
        }
        repaint();
    }
}
