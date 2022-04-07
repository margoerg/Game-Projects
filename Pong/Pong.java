
/**
 * Marcel Goergens
 * 09.07.2021 
 */

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.sound.sampled.*;

public class Pong extends JPanel implements ActionListener, KeyListener
{    
    private int seitenlaenge = 20;
    private int Kugelx = 395;
    private int Kugely = 300;
    private int Kugelv[] = {(int)(Math.random()*10) + 5, (int)(Math.random()*8) + 3};
    private int playerx = 80;
    private int playery = 300;
    private int playerv = 0;
    private int cpux = 720;
    private int cpuy = Kugely;
    private int cpuv = 0;
    private Integer pPunkte = 0;
    private Integer cpuPunkte = 0;
    private Timer timer = new Timer(16, this);

    public Pong()
    {
        super();
        this.setPreferredSize(new Dimension(800, 600));
        this.setBackground(Color.BLACK);
    }

    public static void main(String[] args)
    {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setTitle("Pong");
        frame.setLayout(new BorderLayout());
        ImageIcon im1= new ImageIcon("Icon.png");
        frame.setIconImage(im1.getImage());

        JPanel oben = new JPanel();
        oben.setPreferredSize(new Dimension(1000, 100));
        JPanel links = new JPanel();
        links.setPreferredSize(new Dimension(100,600));
        JPanel rechts = new JPanel();
        rechts.setPreferredSize(new Dimension(100,600));
        JPanel unten = new JPanel();
        unten.setPreferredSize(new Dimension(1000, 100));
        Pong pong = new Pong();

        frame.add(oben, BorderLayout.NORTH);
        frame.add(links, BorderLayout.WEST);
        frame.add(rechts, BorderLayout.EAST);
        frame.add(unten, BorderLayout.SOUTH);
        frame.add(pong, BorderLayout.CENTER);

        frame.addKeyListener(pong); 
        frame.pack();
        frame.setVisible(true);    
    }

    public void paint(Graphics g)
    {
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        g.setColor(Color.WHITE);
        for(int i = 0; i<10; i++)
        {
            g.fillRect(395, 60*i + 10, 20, 35);
        }
        if(timer.isRunning())
        {
            g.fillRect(Kugelx, Kugely, seitenlaenge, seitenlaenge);
        }
        g.fillRect(playerx-10, playery-50, 20, 100);
        g.fillRect(cpux-10, cpuy-50, 20, 100);
        g.setFont(new Font("", Font.BOLD, 100));
        String links;
        if(pPunkte < 10)
        {
            links = "0" + pPunkte;
        }
        else
        {
            links = pPunkte.toString();
        }
        String rechts;
        if(cpuPunkte < 10)
        {
            rechts = "0" + cpuPunkte;
        }
        else
        {
            rechts = cpuPunkte.toString();
        }
        g.drawString(links, 270, 90);
        g.drawString(rechts, 430, 90);
    }

    public void actionPerformed(ActionEvent e)
    {
        Kugelx += Kugelv[0];
        Kugely += Kugelv[1];
        if(Kugely >= this.getHeight()-seitenlaenge)
        {
            Kugelv[1] = -Kugelv[1];
        }
        if(Kugely <= seitenlaenge/2)
        {
            Kugelv[1] = -Kugelv[1];
        }
        if((Kugelx >= this.getWidth()-110) && (Kugely-seitenlaenge/2 >= cpuy-70 && Kugely-seitenlaenge/2 <= cpuy+50) && (!(Kugelx>cpux+10)))
        {
            Kugelv[0] = -Kugelv[0];
            playSound("paddle.wav");
            if(cpuv < 0)
            {
                Kugelv[1] -= 5;
            }
            if(cpuv > 0)
            {
                Kugelv[1] += 5;
            }
        }
        else if((Kugelx <= 90) && (Kugely-seitenlaenge/2 >= playery-70 && Kugely-seitenlaenge/2 <= playery+50) && (!(Kugelx<playerx-10)))
        {
            Kugelv[0] = -Kugelv[0];  
            playSound("paddle.wav");
            if(playerv < 0)
            {
                Kugelv[1] -= 5;
            }
            if(playerv > 0)
            {
                Kugelv[1] += 5;
            }
        }
        else if(Kugelx <= 30)
        {
            Kugelx = 400-seitenlaenge/2;
            Kugely = 300-seitenlaenge/2;
            Kugelv[0] = (int)(Math.random()*12) + 7;
            if(Math.random() < 0.5)
            {
                Kugelv[1] = (int)(Math.random()*8) + 3;
            }
            else
            {
                Kugelv[1] = -(int)(Math.random()*8) + 3;
            }
            cpuPunkte++;
        }
        else if(Kugelx >= this.getWidth()-30)
        {
            Kugelx = 400-seitenlaenge/2;
            Kugely = 300-seitenlaenge/2;
            Kugelv[0] = -((int)(Math.random()*12) + 7);
            if(Math.random() < 0.5)
            {
                Kugelv[1] = (int)(Math.random()*8) + 3;
            }
            else
            {
                Kugelv[1] = -(int)(Math.random()*8) + 3;
            }
            pPunkte++;
        }
        playery += playerv;
        if(playery < 50)
        {
            playery = 50;
        }
        if(playery > this.getHeight()-50)
        {
            playery = this.getHeight()-50;
        }
        if(Kugelv[0] > 0)
        {
            if(cpuy < Kugely)
            {
                cpuv = 5;
            }
            else
            {
                cpuv = -5;                
            }
            cpuy += cpuv;
        }
        if(cpuy <= 50)
        {
            cpuy = 50;
        }
        if(cpuy >= this.getHeight()-50)
        {
            cpuy = this.getHeight()-50;
        }
        repaint();
    }

    public void keyReleased(KeyEvent e)
    {
        playerv = 0;
        switch(e.getKeyCode())
        {
            case KeyEvent.VK_SPACE: timer.start(); break;
            case KeyEvent.VK_ESCAPE: System.exit(0); break;
        }
    }

    public void keyPressed(KeyEvent e)
    {
        switch(e.getKeyCode())
        {
            case KeyEvent.VK_UP: playerv = -15; break;
            case KeyEvent.VK_DOWN: playerv = 15; break;
        }
    }

    public void keyTyped(KeyEvent e)
    {
        switch(e.getKeyCode())
        {
            case KeyEvent.VK_UP: playerv = -15; break;
            case KeyEvent.VK_DOWN: playerv = 15; break;
        }
    }
    
    public static synchronized void playSound(final String url) {
        new Thread(new Runnable() {
                public void run() {
                    try {
                        Clip clip = AudioSystem.getClip();
                        AudioInputStream inputStream = AudioSystem.getAudioInputStream(
                                Pong.class.getResourceAsStream(url));
                        clip.open(inputStream);
                        clip.start(); 
                    } catch (Exception e) {
                        System.err.println(e.getMessage());
                    }
                }
            }).start();
    }
}
