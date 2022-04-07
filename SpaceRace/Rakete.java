import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;
import javax.imageio.*;
import java.io.*;

public class Rakete
{
    private double x, y, v;
    private double startX, startY;
    private double r = 20;

    public Rakete(double px, double py)
    {
        x = px;
        y = py;
        startX = x;
        startY = y;
        v = 0;
    }

    public void paint(Graphics2D g)
    {
        //Ellipse2D.Double kreis = new Ellipse2D.Double(x - r, y - r, 2*r - 3, 2*r - 3);
        //g.fill(kreis);
        try
        {
            Image bild = ImageIO.read(new File("spaceship.png"));
            g.drawImage(bild, (int)(x - r - 9), (int)(y - r - 9), null);
        } catch(Exception e){}
    }   

    public void bewege()
    {
        y += v;
        if(y >= 500 - r)
        {
            y = 500 - r;
        }
    }

    public void setV(double pV)
    {
        v = pV;
    }

    public void reset()
    {
        x = startX;
        y = startY;
    }

    public double getX()
    {
        return x;
    }

    public double getY()
    {
        return y;
    }

    public double getR()
    {
        return r;
    }
}
