/**
 * author: Marcel Goergens
 * version: 10.03.2021
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Main extends JPanel implements KeyListener
{
    private final int WIDTH = 15, HEIGHT = 15;
    private int direction;
    private boolean movable = true, gameover = false;

    private java.util.List<Body> snake;
    private Timer timer;
    private Tile food;

    public static void main(String[] args)
    {
        JFrame frame = new JFrame("Snake");
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Main main = new Main();
        frame.add(main);
        frame.addKeyListener(main);

        ImageIcon icon = new ImageIcon("icon.png");
        frame.setIconImage(icon.getImage());
        frame.pack();
        frame.setVisible(true);
    }

    public Main()
    {
        super();
        setPreferredSize(new Dimension(WIDTH*40 + (WIDTH - 1), HEIGHT*40 + (HEIGHT - 1)));
        setBackground(Color.BLACK);
        snake = new java.util.LinkedList<Body>();
        snake.add(new Body(WIDTH/2 - 1, HEIGHT/2));
        snake.add(new Body(WIDTH/2 - 2, HEIGHT/2));
        snake.add(new Body(WIDTH/2 - 3, HEIGHT/2));
        food = null;
        resetFood();
        direction = 1;
        timer = new Timer(175, e -> {
                boolean newBody = false;
                java.util.Iterator<Body> it = snake.iterator();
                Body first = null;
                if(it.hasNext())
                {
                    first = it.next();
                    first.setprevX(first.getX());
                    first.setprevY(first.getY());
                    switch(direction)
                    {
                        case 1: first.setX(first.getX() + 1); break;
                        case 2: first.setY(first.getY() + 1); break;
                        case 3: first.setX(first.getX() - 1); break;
                        case 4: first.setY(first.getY() - 1); break;
                    }
                    if(first.getX() < 0)
                    {
                        first.setX(WIDTH - 1);
                    }
                    if(first.getX() > WIDTH - 1)
                    {
                        first.setX(0);
                    }
                    if(first.getY() < 0)
                    {
                        first.setY(HEIGHT - 1);
                    }
                    if(first.getY() > HEIGHT - 1)
                    {
                        first.setY(0);
                    }
                    if(first.getX() == food.getX() && first.getY() == food.getY())
                    {
                        newBody = true;
                    }
                }
                Body next = null;
                while(it.hasNext())
                {
                    next = it.next();
                    next.setprevX(next.getX());
                    next.setprevY(next.getY());
                    next.setX(first.getprevX());
                    next.setY(first.getprevY());
                    first = next;
                }
                if(newBody && next != null)
                {
                    snake.add(new Body(next.getprevX(), next.getprevY()));
                    resetFood();
                }
                java.util.Iterator<Body> it2 = snake.iterator();
                if(it2.hasNext())
                {
                    first = it2.next();
                }
                while(it2.hasNext())
                {
                    Body next2 = it2.next();
                    if(next2.getX() == first.getX() && next2.getY() == first.getY())
                    {
                        timer.stop();
                        gameover = true;
                    }
                }
                movable = true;
                repaint();
            });
    }

    private void resetFood()
    {
        int randomX = (int)(WIDTH*Math.random());
        int randomY = (int)(HEIGHT*Math.random());
        while(isUsed(randomX, randomY))
        {
            randomX = (int)(WIDTH*Math.random());
            randomY = (int)(HEIGHT*Math.random());
        }
        food = new Tile(randomX, randomY);
    }

    private boolean isUsed(int x, int y)
    {
        for(Body b: snake)
        {
            if(b.getX() == x && b.getY() == y)
            {
                return true;
            }
        }
        return false;
    }

    private void reset()
    {
        gameover = false;
        direction = 1;
        snake = new java.util.LinkedList<Body>();
        snake.add(new Body(WIDTH/2 - 1, HEIGHT/2));
        snake.add(new Body(WIDTH/2 - 2, HEIGHT/2));
        snake.add(new Body(WIDTH/2 - 3, HEIGHT/2));
        resetFood();
    }

    @Override
    public void paint(Graphics g)
    {
        super.paint(g);
        if(!gameover)
        {
            g.setColor(Color.RED);
            java.util.Iterator<Body> it = snake.iterator();
            Body b = null;
            if(it.hasNext())
            {
                b = it.next();
                g.fillRect(41*b.getX(), 41*b.getY(), 40, 40);
            }
            g.setColor(Color.WHITE);
            while(it.hasNext())
            {            
                b = it.next();
                g.fillRect(41*b.getX(), 41*b.getY(), 40, 40);
            }
            g.setColor(Color.GREEN);
            g.fillRect(41*food.getX(), 41*food.getY(), 40, 40);
            if(!timer.isRunning())
            {
                g.setColor(Color.WHITE);
                g.setFont(new Font("", Font.BOLD, 30));
                java.awt.geom.Rectangle2D r = g.getFontMetrics().getStringBounds("Press any Button to start!", g);
                g.drawString("Press any Button to start!", (int)((WIDTH*41 - r.getWidth())/2), (int)((HEIGHT*41 - r.getHeight())/2) + 80);
            }
        }
        else
        {
            g.setColor(Color.WHITE);
            g.setFont(new Font("", Font.BOLD, 50));
            java.awt.geom.Rectangle2D r = g.getFontMetrics().getStringBounds("GAME OVER!", g);
            g.drawString("GAME OVER!", (int)((WIDTH*41 - r.getWidth())/2), (int)((HEIGHT*41 - r.getHeight())/2) + 40);
            g.setFont(new Font("", Font.BOLD, 30));
            r = g.getFontMetrics().getStringBounds("Press any Button to reset!", g);
            g.drawString("Press any Button to reset!", (int)((WIDTH*41 - r.getWidth())/2), (int)((HEIGHT*41 - r.getHeight())/2) + 70);
        }
    }

    @Override
    public void keyTyped(KeyEvent e)
    {

    }

    @Override
    public void keyReleased(KeyEvent e)
    {
        if(!gameover)
        {
            if(!timer.isRunning())
            {
                timer.start();
            }
            else
            {
                switch(e.getKeyCode())
                {
                    case KeyEvent.VK_RIGHT: if(movable && direction != 3){direction = 1; movable = false;} break;
                    case KeyEvent.VK_DOWN: if(movable && direction != 4){direction = 2; movable = false;} break;
                    case KeyEvent.VK_LEFT: if(movable && direction != 1){direction = 3; movable = false;} break;
                    case KeyEvent.VK_UP: if(movable && direction != 2){direction = 4; movable = false;} break;
                }
            }
        }
        else
        {
            reset();
            repaint();
        }
    }

    @Override
    public void keyPressed(KeyEvent e)
    {

    }

    private class Tile
    {
        private int x, y, prevx, prevy;

        public Tile(int pX, int pY)
        {
            x = pX;
            y = pY;
            prevx = 0;
            prevy = 0;
        }

        public void setX(int pX)
        {
            x = pX;
        }

        public void setY(int pY)
        {
            y = pY;
        }

        public int getX()
        {
            return x;
        }

        public int getY()
        {
            return y;
        }

        public void setprevX(int pprevX)
        {
            prevx = pprevX;
        }

        public void setprevY(int pprevY)
        {
            prevy = pprevY;
        }

        public int getprevX()
        {
            return prevx;
        }

        public int getprevY()
        {
            return prevy;
        }
    }

    private class Body extends Tile
    {
        private int prevx, prevy;

        public Body(int pX, int pY)
        {
            super(pX, pY);
            prevx = 0;
            prevy = 0;
        }

        public void setprevX(int pprevX)
        {
            prevx = pprevX;
        }

        public void setprevY(int pprevY)
        {
            prevy = pprevY;
        }

        public int getprevX()
        {
            return prevx;
        }

        public int getprevY()
        {
            return prevy;
        }
    }
}