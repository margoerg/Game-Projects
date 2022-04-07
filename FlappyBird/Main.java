/**
 * author: Marcel Goergens
 * version: 01.03.2022
 */

import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.geom.*;
import java.awt.event.*;
import javax.imageio.*;
import java.io.*;

public class Main extends JPanel implements KeyListener
{
    private final int HEIGHT = 450, WIDTH = 450;
    private int x = 90;
    private int y = HEIGHT/2;
    private int v = 0;
    private boolean isRunning = true;
    private int score = 0;

    private Timer timer;
    private Obstacle[] obs;
    private Image bird;

    public static void main(String[] args)
    {
        JFrame frame = new JFrame("Flappy Bird");
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Main main = new Main();
        frame.add(main);
        frame.addKeyListener(main);
        ImageIcon icon = new ImageIcon("bird.png");
        frame.setIconImage(icon.getImage());
        frame.pack();
        frame.setVisible(true);
    }

    public Main()
    {
        super();
        setBackground(Color.CYAN);
        setPreferredSize(new Dimension(HEIGHT, WIDTH));
        obs = new Obstacle[5];
        for(int i = 0; i < obs.length; i++)
        {
            int ran = (int)((HEIGHT - 180)*Math.random());
            obs[i] = new Obstacle(600 + 300*i, ran);
        }
        try
        {
            bird = ImageIO.read(new File("bird.png"));
        }catch(IOException e){e.printStackTrace();}
        timer = new Timer(36, e -> {
                y += v;
                v++;
                if(!isRunning && y >= HEIGHT - 89)
                {
                    v = 0;
                    timer.stop();
                }
                if(isRunning)
                {
                    for(int i = 0; i < obs.length; i++)
                    {
                        obs[i].move();
                        if(obs[i].outOfBounds())
                        {
                            obs[i].setX(obs[(((i - 1) % obs.length) + obs.length)% obs.length].getX() + 300);
                        }
                    }
                    if(passes())
                    {
                        score++;
                    }
                    if(isRunning && (y >= HEIGHT - 90 || collides()))
                    {
                        for(int i = 0; i < obs.length; i++)
                        {
                            obs[i].setV(0);
                        }              
                        v = -6;
                        isRunning = false;
                    }                    
                }
                repaint();
            });
    }

    private boolean collides()
    {
        for(int i = 0; i < obs.length; i++)
        {
            if(obs[i].getX() <= x + 42 && obs[i].getX() + 45 >= x)
            {
                if(y <= obs[i].getY() || y >= obs[i].getY() + 90)
                {
                    return true;
                }
                return false;
            }
        }
        return false;
    }

    private boolean passes()
    {
        for(int i = 0; i < obs.length; i++)
        {
            if(x >= obs[i].getX() + 41 && x <= obs[i].getX()+ 49)
            {
                return true;
            }
        }
        return false;
    }

    public void reset()
    {
        for(int i = 0; i < obs.length; i++)
        {
            int ran = (int)((HEIGHT - 180)*Math.random());
            obs[i] = new Obstacle(600 + 300*i, ran);
        }
        y = HEIGHT/2;
        score = 0;
    }

    @Override
    public void paint(Graphics g)
    {
        super.paint(g);
        for(int i = 0; i < obs.length; i++)
        {
            obs[i].paint(g);
        }
        g.setColor(Color.BLACK);
        g.drawRect(-1, getHeight() - 60, getWidth() + 1, 60);        
        g.setColor(new Color(0, 128, 0));
        g.fillRect(0, getHeight() - 59, getWidth(), 59);
        g.drawImage(bird, x, y, 42, 30, null);
        g.setColor(Color.WHITE);
        g.setFont(new Font("", Font.BOLD, 50));
        java.awt.geom.Rectangle2D r = g.getFontMetrics().getStringBounds("" + score, g);
        g.drawString(""+ score, (int)((WIDTH - r.getWidth())/2), 70);
        
        if(!timer.isRunning() && !isRunning)
        {
            g.setFont(new Font("", Font.BOLD, 24));
            r = g.getFontMetrics().getStringBounds("Press any Button to reset!", g);
            g.drawString("Press any Button to reset!",(int)((WIDTH - r.getWidth())/2), 130);
        }
        if(!timer.isRunning() && isRunning)
        {
            g.setFont(new Font("", Font.BOLD, 24));
            r = g.getFontMetrics().getStringBounds("Press any Button to start!", g);
            g.drawString("Press any Button to start!",(int)((WIDTH - r.getWidth())/2), 130);
        }
    }

    @Override
    public void keyReleased(KeyEvent e)
    {
        if(isRunning && !timer.isRunning())
        {
            timer.start();
        }
        if(!isRunning && !timer.isRunning())
        {
            reset();
            isRunning = true;
            repaint();
        }
        if(isRunning)
        {
            v = -9;
        }       
    }

    @Override
    public void keyPressed(KeyEvent e)
    {

    }

    @Override
    public void keyTyped(KeyEvent e)
    {

    }

    private class Obstacle
    {
        private final int WIDTH = 45;
        private int x, y, v;

        public Obstacle(int pX, int pY)
        {
            x = pX;
            y = pY;
            v = 8;
        }

        public void paint(Graphics g)
        {
            g.setColor(Color.BLACK);
            g.drawRect(x, -1, WIDTH, y - 19);
            g.drawRect(x - 2, y - 20, WIDTH + 4, 20);
            g.drawRect(x - 2, y + 120, WIDTH + 4, 20);
            g.drawRect(x, y + 140, WIDTH, HEIGHT - y - 100);  
            
            g.setColor(new Color(0, 205, 0));
            g.fillRect(x + 1, 0, WIDTH - 1, y - 20);
            g.fillRect(x - 1, y - 19, WIDTH + 3, 19);
            g.fillRect(x - 1, y + 121, WIDTH + 3, 19);
            g.fillRect(x + 1, y + 141, WIDTH - 1, HEIGHT - y - 99);
            
            int pos = WIDTH/5 - 3;
            g.setColor(new Color(0, 230, 0));
            g.fillRect(x + pos, 0, 8, y - 20);
            g.fillRect(x - 1 + pos, y - 19, 8, 19);
            g.fillRect(x - 1 + pos, y + 121, 8, 19);
            g.fillRect(x + 1 + pos, y + 141, 8, HEIGHT - y - 99);
            
            pos = 4*WIDTH/5;
            g.setColor(new Color(0, 160, 0));
            g.fillRect(x + 1 + pos, 0, 8, y - 20);
            g.fillRect(x - 1 + pos + 4, y - 19, 8, 19);
            g.fillRect(x - 1 + pos + 4, y + 121, 8, 19);
            g.fillRect(x + 1 + pos, y + 141, 8, HEIGHT - y - 99);
            
            g.setColor(new Color(100, 255, 100));
            g.fillRect(x + 1, 0, 4, y - 20);
            g.fillRect(x, y - 19, 4, 19);
            g.fillRect(x, y + 121, 4, 19);
            g.fillRect(x + 1, y + 141, 4, HEIGHT - y - 99);
        }

        public void move()
        {
            x -= v;
        }

        public void setX(int pX)
        {
            x = pX;
        }

        public void setV(int pV)
        {
            v = pV;
        }

        public int getX()
        {
            return x;
        }

        public int getY()
        {
            return y;
        }

        public boolean outOfBounds()
        {
            return x < -WIDTH - 5;
        }
    }
}