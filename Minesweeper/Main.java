/**
 * @author: Marcel Goergens
 * @version: 09.09.2021
 */

import java.io.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Main extends JPanel implements MouseListener
{
    private static final long serialVersionUID = 1L;
    private static final Dimension size = Toolkit.getDefaultToolkit().getScreenSize();
    private static int width = 9;
    private static int height = 9;

    private static int bombs = 10;
    private static int flaggen = bombs;
    private Feld[][] brett = new Feld[width][height];
    private BufferedImage sprites;
    private boolean verloren = false;
    private boolean ende = false;
    private static boolean ersterKlick = true;
    private static ImageIcon smiley;
    private static JLabel smileyLabel;
    private static JLabel flaggenLabel;
    private static JDialog dialog;

    public static void main(String[] args)
    {
        JFrame frame = new JFrame("Minesweeper");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ImageIcon icon = new ImageIcon("icon.png");
        frame.setIconImage(icon.getImage());
        frame.setLayout(new BorderLayout());

        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Spiel");
        JMenuItem item = new JMenuItem("Neustart");
        item.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
        item.getAccessibleContext().setAccessibleDescription("Neustarten");
        smiley = new ImageIcon("smiley.png");
        smileyLabel = new JLabel(smiley);
        flaggenLabel = new JLabel("" + flaggen);
        flaggenLabel.setFont(new Font("Arial", Font.BOLD, 20));
        Main main = new Main();
        item.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    main.reset();
                    ersterKlick = true;
                    main.repaint();
                }
            });
        menu.addMouseListener(main);
        menu.add(item);
        menu.addSeparator();

        ButtonGroup difficulty = new ButtonGroup();
        JRadioButtonMenuItem easy = new JRadioButtonMenuItem("Einfach");
        easy.setSelected(true);
        JRadioButtonMenuItem medium = new JRadioButtonMenuItem("Mittelschwer");
        JRadioButtonMenuItem hard = new JRadioButtonMenuItem("Schwer");
        easy.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e)
                {
                    width = 9;
                    height = 9;
                    bombs = 10;
                    flaggen = bombs;
                    ersterKlick = true;
                    main.reset();
                    main.setPreferredSize(new Dimension(width*32 + (width-1), height*32 + (height-1)));
                    frame.pack();
                    main.repaint();
                    frame.setLocation((int)(size.getWidth()/2 - frame.getWidth()/2), (int)(size.getHeight()/2 - frame.getHeight()/2));
                }
            });
        medium.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e)
                {
                    width = 16;
                    height = 16;
                    bombs = 40;
                    flaggen = bombs;
                    ersterKlick = true;
                    main.reset();
                    main.setPreferredSize(new Dimension(width*32 + (width-1), height*32 + (height-1)));
                    frame.pack();
                    main.repaint();
                    frame.setLocation((int)(size.getWidth()/2 - frame.getWidth()/2), (int)(size.getHeight()/2 - frame.getHeight()/2));
                }
            });
        hard.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e)
                {
                    width = 30;
                    height = 16;
                    bombs = 99;
                    flaggen = bombs;
                    ersterKlick = true;
                    main.reset();
                    main.setPreferredSize(new Dimension(width*32 + (width-1), height*32 + (height-1)));
                    frame.pack();
                    main.repaint();
                    frame.setLocation((int)(size.getWidth()/2 - frame.getWidth()/2), (int)(size.getHeight()/2 - frame.getHeight()/2));
                }
            });
        difficulty.add(easy);difficulty.add(medium);difficulty.add(hard);
        menu.add(easy);menu.add(medium);menu.add(hard);

        menu.addSeparator();
        JMenuItem exit = new JMenuItem("Schließen");
        exit.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    frame.dispose();
                }
            });
        exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F1, ActionEvent.CTRL_MASK));
        menu.add(exit);
        menuBar.add(menu);
        JMenu help = new JMenu("Hilfe");
        help.addMouseListener(main);
        JMenuItem tutorial = new JMenuItem("Wie man spielt");
        tutorial.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, ActionEvent.CTRL_MASK));
        tutorial.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e)
                {
                    JDialog hilfe = new JDialog(frame, "Hilfe", true);
                    hilfe.setResizable(false);
                    hilfe.setLayout(new BorderLayout());
                    JPanel panel = new JPanel();
                    JLabel text = new JLabel("It's Minesweeper. Everyone knows how to play it.");
                    panel.add(text);
                    JPanel panel2 = new JPanel();
                    JButton button = new JButton("Okay");
                    button.addActionListener(new ActionListener(){
                            public void actionPerformed(ActionEvent e)
                            {
                                hilfe.dispose();
                            }
                        });
                    panel2.add(button);
                    hilfe.add(panel, BorderLayout.NORTH);
                    hilfe.add(panel2, BorderLayout.SOUTH);
                    hilfe.pack();
                    hilfe.setLocation((int)(size.getWidth()/2 - hilfe.getWidth()/2), (int)(size.getHeight()/2 - hilfe.getHeight()/2));
                    hilfe.setVisible(true);
                }
            });

        help.add(tutorial);
        menuBar.add(help);
        frame.setJMenuBar(menuBar);

        JPanel oben = new JPanel();
        oben.setPreferredSize(new Dimension(WIDTH, 55));
        oben.add(flaggenLabel);
        oben.add(smileyLabel);

        frame.add(oben, BorderLayout.NORTH);
        frame.add(main, BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
        frame.setLocation((int)(size.getWidth()/2 - frame.getWidth()/2), (int)(size.getHeight()/2 - frame.getHeight()/2));
    }

    public Main()
    {
        super();
        setPreferredSize(new Dimension(296, 296));
        try
        {
            sprites = ImageIO.read(new File("sprites.png"));
        }catch(IOException e){}
        addMouseListener(this);
        reset();
    }

    private void reset()
    {
        brett = new Feld[width][height];
        for(int i = 0; i < bombs; i++)
        {
            int ranx = (int)(width*Math.random());
            int rany = (int)(height*Math.random()); 
            while(brett[ranx][rany] != null)
            {
                ranx = (int)(width*Math.random());
                rany = (int)(height*Math.random()); 
            }
            brett[ranx][rany] = new Feld(-1);
        }
        for(int i = 0; i < height; i++)
        {
            for(int j = 0; j < width; j++)
            {
                if(brett[j][i] == null)
                {
                    brett[j][i] = new Feld(0);
                }
            }
        }
        for(int i = 0; i < height; i++)
        {
            for(int j = 0; j < width; j++)
            {
                if(brett[j][i].getNumber() < 0)
                {
                    if(j-1 >= 0 && j-1 < width && i-1 >= 0 && i-1 < height && brett[j-1][i-1].getNumber() >= 0)
                    {
                        brett[j-1][i-1].increaseNumber();
                    }
                    if(i-1 >= 0 && i-1 < height && brett[j][i-1].getNumber() >= 0)
                    {
                        brett[j][i-1].increaseNumber();
                    }
                    if(j+1 >= 0 && j+1 < width && i-1 >= 0 && i-1 < height && brett[j+1][i-1].getNumber() >= 0)
                    {
                        brett[j+1][i-1].increaseNumber();
                    }
                    if(j-1 >= 0 && j-1 < width && brett[j-1][i].getNumber() >= 0)
                    {
                        brett[j-1][i].increaseNumber();
                    }
                    if(j+1 >= 0 && j+1 < width && brett[j+1][i].getNumber() >= 0)
                    {
                        brett[j+1][i].increaseNumber();
                    }
                    if(j-1 >= 0 && j-1 < width && i+1 >= 0 && i+1 < height && brett[j-1][i+1].getNumber() >= 0)
                    {
                        brett[j-1][i+1].increaseNumber();
                    }
                    if(i+1 >= 0 && i+1 < height && brett[j][i+1].getNumber() >= 0)
                    {
                        brett[j][i+1].increaseNumber();
                    }
                    if(j+1 >= 0 && j+1 < width && i+1 >= 0 && i+1 < height && brett[j+1][i+1].getNumber() >= 0)
                    {
                        brett[j+1][i+1].increaseNumber();
                    }
                }
            }
        }
        verloren = false;
        ende = false;
        flaggen = bombs;
        flaggenLabel.setText(""+flaggen);
        smiley = new ImageIcon("smiley.png");
        smileyLabel.setIcon(smiley);
    }

    @Override
    public void paint(Graphics g)
    {
        Graphics2D g2 = (Graphics2D)g;
        for(int i = 0; i < width - 1; i++)
        {
            g2.drawLine(33*i + 33, 0, 33*i + 33, getHeight());
        }
        for(int i = 0; i < height - 1; i++)
        {
            g2.drawLine(0, 33*i + 33, getWidth(), 33*i + 33);
        }
        for(int i = 0; i < height; i++)
        {
            for(int j = 0; j < width; j++)
            {
                if(brett[j][i].getStatus() == 0)
                {
                    BufferedImage cover = sprites.getSubimage(33, 65, 32, 32);
                    g2.drawImage(cover, 33*j + 1, 33*i + 1, null);
                }
                if(brett[j][i].getStatus() == 2)
                {
                    BufferedImage flag = sprites.getSubimage(97, 65, 32, 32);
                    g2.drawImage(flag, 33*j + 1, 33*i + 1, null);
                }
                if(brett[j][i].getNumber() < 0 && brett[j][i].getStatus() == 1)
                {
                    BufferedImage bomb = sprites.getSubimage(64, 64, 32, 32);
                    g2.drawImage(bomb, null, 33*j + 1, 33*i + 1);
                }
                if(brett[j][i].getNumber() >= 0 && brett[j][i].getStatus() == 1)
                {
                    int x = brett[j][i].getNumber() % 4;
                    int y = (brett[j][i].getNumber() - x)/4;
                    BufferedImage number = sprites.getSubimage(32*x + 1, 32*y + 1, 32, 32);
                    g2.drawImage(number, null, 33*j + 1, 33*i + 1);
                }
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e)
    {
        if(e.getX() > 0 && e.getX() < getWidth() && e.getY() > 0 && e.getY() < getHeight() && !ende)
        {
            boolean gewonnen = false;
            int x = 0;
            int y = 0;
            for(int i = 0; i < width; i++)
            {
                if(e.getX() >= 33*i && e.getX() <= 33*i + 32)
                {
                    x = i;
                }
            }
            for(int i = 0; i < height; i++)
            {
                if(e.getY() >= 33*i && e.getY() <= 33*i + 32)
                {
                    y = i;
                }
            }
            while(ersterKlick)
            {
                if(brett[x][y].getNumber() != 0)
                {
                    reset();
                }
                else
                {
                    ersterKlick = false;
                }
            }
            if(e.getButton() == MouseEvent.BUTTON3 && brett[x][y].getStatus() == 0)
            {
                brett[x][y].setStatus(2);
                flaggen--;
                flaggenLabel.setText(""+flaggen);
            }
            else if(e.getButton() == MouseEvent.BUTTON3 && brett[x][y].getStatus() == 2)
            {
                brett[x][y].setStatus(0);
                flaggen++;
                flaggenLabel.setText(""+flaggen);
            }
            else if(brett[x][y].getStatus() == 0)
            {
                reveal(x, y);
                if(brett[x][y].getNumber() < 0)
                {
                    for(int i = 0; i < height; i++)
                    {
                        for(int j = 0; j < width; j++)
                        {
                            if(brett[j][i].getStatus() != 1)
                            {
                                reveal(j, i);
                                verloren = true;
                                ende = true;
                                smiley = new ImageIcon("smiley_dead.png");
                                smileyLabel.setIcon(smiley);
                            }
                        }
                    }
                }
                gewonnen = true;
                if(!verloren)
                {
                    for(int i = 0; i < height; i++)
                    {
                        for(int j = 0; j < width; j++)
                        {
                            if(brett[j][i].getNumber() >= 0 && brett[j][i].getStatus() == 0)
                            {
                                gewonnen = false;
                                break;
                            }
                        }
                        if(!gewonnen)
                        {
                            break;
                        }
                    }
                }
            }
            if(gewonnen && !verloren)
            {
                ende = true;
                smiley = new ImageIcon("smiley_glasses.png");
                smileyLabel.setIcon(smiley);
            }
        }
        repaint();
    }

    private void reveal(int x, int y)
    {
        brett[x][y].aufdecken();
        if(brett[x][y].getNumber() == 0)
        {
            if(x-1 >= 0 && x-1 < width && y-1 >= 0 && y-1 < height && brett[x-1][y-1].getStatus() == 0)
            {
                reveal(x-1, y-1);
            }
            if(y-1 >= 0 && y-1 < height && brett[x][y-1].getStatus() == 0)
            {
                reveal(x, y-1);
            }
            if(x+1 >= 0 && x+1 < width && y-1 >= 0 && y-1 < height && brett[x+1][y-1].getStatus() == 0)
            {
                reveal(x+1, y-1);
            }
            if(x-1 >= 0 && x-1 < width && brett[x-1][y].getStatus() == 0)
            {
                reveal(x-1, y);
            }
            if(x+1 >= 0 && x+1 < width && brett[x+1][y].getStatus() == 0)
            {
                reveal(x+1, y);
            }
            if(x-1 >= 0 && x-1 < width && y+1 >= 0 && y+1 < height && brett[x-1][y+1].getStatus() == 0)
            {
                reveal(x-1, y+1);
            }
            if(y+1 >= 0 && y+1 < height && brett[x][y+1].getStatus() == 0)
            {
                reveal(x, y+1);
            }
            if(x+1 >= 0 && x+1 < width && y+1 >= 0 && y+1 < height && brett[x+1][y+1].getStatus() == 0)
            {
                reveal(x+1, y+1);
            }
        }
    }

    @Override
    public void mousePressed(MouseEvent e)
    {

    }

    @Override
    public void mouseClicked(MouseEvent e)
    {

    }

    @Override
    public void mouseEntered(MouseEvent e)
    {
        try
        {
            JMenu item = (JMenu) e.getSource();
            item.setSelected(true); 
        }catch(Exception ex){}
    }

    @Override
    public void mouseExited(MouseEvent e)
    {
        try
        {
            JMenu item = (JMenu) e.getSource();
            item.setSelected(false);
        }catch(Exception ex){}
    }
}

class Feld
{
    private int status = 0;
    private int number;

    public Feld(int pNumber)
    {
        number = pNumber;
    }

    public int getNumber()
    {
        return number;
    }

    public void setStatus(int pNewStatus)
    {
        status = pNewStatus;
    }

    public int getStatus()
    {
        return status;
    }

    public void aufdecken()
    {
        status = 1;
    }

    public void increaseNumber()
    {
        number++;
    }
}