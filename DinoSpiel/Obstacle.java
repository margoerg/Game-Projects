import java.awt.*;
import java.awt.geom.*;
import javax.imageio.*;
import java.io.*;
import java.awt.image.*;

public class Obstacle
{
    private double x, y, v;
    private Image sprite;
    
    public Obstacle(double pX, double pY, BufferedImage sprites)
    {
        x = pX;
        y = pY;
        v = -12;
        setImage(sprites);
    }
    
    public void paint(Graphics2D g2)
    {
        g2.drawImage(sprite, (int)x, (int)y, 66, 68, null);
        //g2.setColor(Color.RED);
        //g2.fill(new Rectangle2D.Double(x, y, 66, 68));
    }
    
    public void move()
    {
        x += v;
        if(v > -15)        
        {
            v -= 0.01;
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
    
    public void setX(double pX)
    {
        x = pX;
    }
    
    public boolean outOfBounds()
    {
        return x <= -76;
    }
    
    public void setImage(BufferedImage sprites)
    {
        int ran = (int)(3*Math.random());
        switch(ran)
        {
            case 0: sprite = sprites.getSubimage(798, 1, 66, 68); break;
            case 1: sprite = sprites.getSubimage(869, 1, 66, 68); break;
            case 2: sprite = sprites.getSubimage(940, 1, 66, 68); break;
        }
    }
}
