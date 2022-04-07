import java.awt.*;
import java.awt.geom.*;

public class Stein
{
    private double x, y;
    private double r = 5;
    private boolean nachRechts;
    
    public Stein()
    {
        reset();
    }
    
    public void paint(Graphics2D g)
    {
        Ellipse2D.Double kreis = new Ellipse2D.Double(x - r, y - r, 2*r, 2*r);
        g.fill(kreis);
    }
    
    public void reset()
    {
        if(Math.random() < 0.5)
        {
            x = Math.random()*(500) - 500;
            nachRechts = true;
        }
        else
        {
            x = Math.random()*(500) + 500;
            nachRechts = false;
        }
        y = Math.random()*340 + 70;
    }
    
    public void bewege()
    {
        if(nachRechts)
        {
            x += 3;
            if(x >= 500)
            {
                reset();
            }
        }
        else
        {
            x -= 3;
            if(x <= 0)
            {
                reset();
            }
        }
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
