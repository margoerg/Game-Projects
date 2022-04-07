/**
 * @author: Marcel Goergens 
 * @version: 10.02.2022
 */

import javax.swing.*;
import javax.imageio.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.awt.geom.*;

public class Main extends JPanel implements KeyListener
{
    private double x = 150, y = 256, vY = 0;
    private int score = 0;
    private Timer timer;
    private boolean onGround = true, isDead = false;
    private BufferedImage sprites;
    private Obstacle[] obs = new Obstacle[3];
    private Robot robot;
    private boolean robotActivated = false;

    public static void main(String[] args)
    {
        JFrame frame = new JFrame("Dinosaur Game");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);

        Main main = new Main();
        frame.add(main);
        frame.addKeyListener(main); 

        frame.setIconImage(new ImageIcon("icon.png").getImage());
        frame.pack();
        frame.setVisible(true);
    }

    public Main()
    {
        super();
        setBackground(Color.WHITE);
        setPreferredSize(new Dimension(550, 500));
        timer = new Timer(36, e -> {
                y += vY;
                if(!onGround)
                {
                    vY += 1.5;
                    if(y >= 260)
                    {
                        y = 260;
                        vY = 0;
                        onGround = true;
                    }
                }
                for(int i = 0; i < obs.length; i++)
                {
                    obs[i].move();
                    if(Math.hypot((x + 44) - (obs[i].getX() + 33), (y + 45) - (obs[i].getY() + 34)) <= 70)
                    {
                        timer.stop();
                        isDead = true;
                        repaint();
                    }
                    if(obs[i].outOfBounds())
                    {
                        obs[i].setX(400 + 300*Math.random() + obs[(((i - 1) % 3) + 3)% 3].getX());
                        obs[i].setImage(sprites);
                    }
                }
                score++;
                if(robotActivated && getRootPane().getParent().isFocusOwner())
                {
                    BufferedImage capture = robot.createScreenCapture(new Rectangle(getRootPane().getParent().getX() + 310, getRootPane().getParent().getY() +330, 1,1)); 
                    if(capture.getRGB(0, 0) < -1)
                    {
                        robot.keyPress(KeyEvent.VK_UP);
                        robot.keyRelease(KeyEvent.VK_UP);
                    }                    
                }
                repaint();
            });
        try
        {
            sprites = ImageIO.read(new File("spritesheet.png"));
            robot = new Robot();
        }catch(Exception e){e.printStackTrace();}
        reset();
    }

    public void reset()
    {
        score = 0;
        for(int i = 0; i < obs.length; i++)
        {
            obs[i] = new Obstacle(650 + 350*i, 282, sprites);
        }
        y = 260;
        isDead = false;
        onGround = true;
        vY = 0;
        timer.start();
    }

    @Override
    public void paint(Graphics g)
    {
        Graphics2D g2 = (Graphics2D)g;
        super.paint(g2);
        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Arial", Font.BOLD, 25));
        g2.drawString("Score: " + score, 20, 25);
        g2.fillRect(0, 350, getWidth(), getHeight() - 150);
        Image sprite;
        if(onGround)
        {
            if(score % 4 <= 1 )
            {
                sprite = sprites.getSubimage(185, 2, 88, 90);
            }   
            else
            {
                sprite = sprites.getSubimage(277, 2, 88, 90);
            }
        }
        else
        {
            sprite = sprites.getSubimage(1, 2, 88, 90);
        }
        g2.drawImage(sprite, (int)x, (int)y, 88, 90, null);
        //g2.setColor(Color.BLUE);
        //g2.fill(new Rectangle2D.Double(x, y, 88, 90));
        for(Obstacle o: obs)
        {
            o.paint(g2);
        }
        if(isDead)
        {
            Image dead = sprites.getSubimage(369, 0, 88, 90);
            g2.drawImage(dead, (int)x, (int)y, 87, 90, null);
        }
        if(robotActivated)
        {
            g2.drawString("Bot an", getWidth() - 100, 25);
        }
    }

    @Override
    public void keyReleased(KeyEvent e)
    {

    }

    @Override
    public void keyTyped(KeyEvent e)
    {

    }

    @Override
    public void keyPressed(KeyEvent e)
    {        
        switch(e.getKeyCode())
        {
            case KeyEvent.VK_R: reset(); break;
            case KeyEvent.VK_B: robotActivated = (!robotActivated ? true : false); break;
            default: if(onGround) {vY = -20; onGround = false;} break; 
        }
    }
}
