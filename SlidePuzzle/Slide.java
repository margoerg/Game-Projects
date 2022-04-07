import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class Slide extends JPanel implements KeyListener
{
    private static JLabel gewonnen;
    private boolean gestartet = false;
    private static Integer[][] board = {{1, 2, 3, 4},
            {5, 6, 7, 8},
            {9, 10, 11, 12},
            {13, 14, 15, 0}};
    private BufferedImage img;

    public Slide()
    {
        super();      
        this.setPreferredSize(new Dimension(403, 403));
        try
        {
            img = ImageIO.read(new File("bild.jpg"));
        }catch(Exception e){}
    }

    public static void main(String[] args)
    {
        JFrame frame = new JFrame("Slider");
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        Slide s = new Slide();
        JPanel oben = new JPanel();
        oben.setPreferredSize(new Dimension(603, 100));

        JPanel unten = new JPanel();
        unten.setPreferredSize(new Dimension(603, 100));
        gewonnen = new JLabel("GELOEST!");
        gewonnen.setFont(new Font("Bold", 1, 30));
        gewonnen.setVisible(false);
        unten.add(gewonnen);

        JPanel links = new JPanel();
        links.setPreferredSize(new Dimension(100, 403));

        JPanel rechts = new JPanel();
        rechts.setPreferredSize(new Dimension(100, 403));

        frame.add(oben, BorderLayout.NORTH);
        frame.add(unten, BorderLayout.SOUTH);
        frame.add(links, BorderLayout.WEST);
        frame.add(rechts, BorderLayout.EAST);
        frame.add(s, BorderLayout.CENTER);
        frame.addKeyListener(s);
        frame.pack();
        frame.setVisible(true);
    }

    public void paint(Graphics g)
    {
        g.setColor(new Color(173, 216, 230));
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        for(int i = 0; i < 4; i++)
        {
            for(int j = 0; j < 4; j++)
            {
                if(board[i][j] != 0)
                {/**
                    g.setColor(new Color(0, 100, 0));
                    g.fillRect(j*100 + j, i*100 + i, 100, 100);
                    g.setColor(Color.WHITE);
                    g.setFont(new Font("Bold", 1, 30));
                    if(board[i][j]<10)
                    {
                    g.drawString(board[i][j].toString(), j*100+44, i*100+57);
                    }
                    else
                    {
                    g.drawString(board[i][j].toString(), j*100+35, i*100+57);
                    }
                     **/
                    BufferedImage teil = null;                   
                    int x = (board[i][j]-1)%4;
                    int y = (board[i][j]-1-x)/4;
                    teil = img.getSubimage(100*x, 100*y, 100, 100);
                    g.drawImage(teil, j*100 + j, i*100 + i, null);
                }
            }
        }
    }

    public void keyReleased(KeyEvent e)
    {
        boolean fertig = false;
        switch(e.getKeyCode())
        {
            case KeyEvent.VK_UP:
            if(gestartet){
                for(int i = 0; i < 4; i++)
                {
                    for(int j = 0; j < 4; j++)
                    {
                        if(board[i][j] == 0 && i !=3)
                        {
                            board[i][j] = board[i+1][j];
                            board[i+1][j] = 0;
                            fertig = true;
                            break;
                        }
                    }
                    if(fertig)
                    {
                        break;
                    }
                }}break;

            case KeyEvent.VK_DOWN:
            if(gestartet){
                for(int i = 0; i < 4; i++)
                {
                    for(int j = 0; j < 4; j++)
                    {
                        if(board[i][j] == 0 && i != 0)
                        {
                            board[i][j] = board[i-1][j];
                            board[i-1][j] = 0;
                            break;
                        }
                    }
                }}break;

            case KeyEvent.VK_LEFT:
            if(gestartet){
                for(int i = 0; i < 4; i++)
                {
                    for(int j = 0; j < 4; j++)
                    {
                        if(board[i][j] == 0 && j !=3)
                        {
                            board[i][j] = board[i][j+1];
                            board[i][j+1] = 0;
                            break;
                        }
                    }
                }}break;

            case KeyEvent.VK_RIGHT:
            if(gestartet){
                for(int i = 0; i < 4; i++)
                {
                    for(int j = 0; j < 4; j++)
                    {
                        if(board[i][j] == 0 && j !=0)
                        {
                            board[i][j] = board[i][j-1];
                            board[i][j-1] = 0;
                            break;
                        }
                    }
                }}break;

            case KeyEvent.VK_SPACE: if(!gestartet){this.shuffle(); gewonnen.setVisible(false); gestartet = true;};break;
        }
        repaint();
        if(geloest() && gestartet)
        {
            gestartet = false;
            gewonnen.setVisible(true);
        }
    }

    public void keyTyped(KeyEvent e)
    {

    }

    public void keyPressed(KeyEvent e)
    {

    }

    public void shuffle()
    {
        ArrayList<Integer> zahlen = new ArrayList<Integer>();
        for(int i = 0; i < 16; i++)
        {
            zahlen.add(i, i);
        }

        for(int i = 0; i < 4; i++)
        {
            for(int j = 0; j < 4; j++)
            {
                int ran = (int)(Math.random()*zahlen.size());
                board[i][j] = zahlen.get(ran);
                zahlen.remove(ran);
                zahlen.trimToSize();
            }
        }

        if(!this.isSolvable(this.convert(board)))
        {
            shuffle();
        }
    }

    public boolean geloest()
    {
        for(int i = 0; i < 4; i++)
        {
            if(i<3)
            {
                for(int j = 0; j < 4; j++)
                {
                    if(board[i][j] != 4*i + j + 1)
                    {
                        return false;
                    }
                }
            }
            else
            {
                for(int j = 0; j < 3; j++)
                {
                    if(board[i][j] != 4*i + j + 1)
                    {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public boolean isSolvable(int[] puzzle)
    {
        int parity = 0;
        int gridWidth = (int) Math.sqrt(puzzle.length);
        int row = 0; // the current row we are on
        int blankRow = 0; // the row with the blank tile

        for (int i = 0; i < puzzle.length; i++)
        {
            if (i % gridWidth == 0) { // advance to next row
                row++;
            }
            if (puzzle[i] == 0) { // the blank tile
                blankRow = row; // save the row on which encountered
                continue;
            }
            for (int j = i + 1; j < puzzle.length; j++)
            {
                if (puzzle[i] > puzzle[j] && puzzle[j] != 0)
                {
                    parity++;
                }
            }
        }

        if (gridWidth % 2 == 0) { // even grid
            if (blankRow % 2 == 0) { // blank on odd row; counting from bottom
                return parity % 2 == 0;
            } else { // blank on even row; counting from bottom
                return parity % 2 != 0;
            }
        } else { // odd grid
            return parity % 2 == 0;
        }
    }

    public int[] convert(Integer[][] arr)
    {
        int[] out = new int[16];

        for(int i = 0; i < 4; i++)
        {
            for(int j = 0; j < 4; j++)
            {
                out[i*4 + j] = arr[i][j];
            }
        }

        return out;
    }
}
