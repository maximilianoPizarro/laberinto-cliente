package com.modelo;

import javafx.scene.image.Image;
import javafx.scene.canvas.GraphicsContext;
import javafx.geometry.Rectangle2D;

public class Sprite
{
    private Image image;
    private double positionX;
    private double positionY;    
    private double velocityX;
    private double velocityY;
    private double width;
    private double height;
    private boolean visible;
    
    public double getX() {
    	return positionX;
    }
    
    public double getY() {
    	return positionY;
    }
    
    public Sprite()
    {
        positionX = 0;
        positionY = 0;    
        velocityX = 0;
        velocityY = 0;
        //visible = false;
        width = 0;
        height = 0;
        visible = true; //si quisiera controlarlo desde lamatriz
    }

    public void setImage(Image i)
    {
        image = i;
        width = i.getWidth();
        height = i.getHeight();
    }

    public void setImage(String filename)
    {
        Image i = new Image(filename);
        setImage(i);
        
    }

    public void setPosition(double x, double y)
    {
        positionX = x;
        positionY = y;
    }

    public void setVelocity(double x, double y)
    {
        velocityX = x;
        velocityY = y;
    }

    public void addVelocity(double x, double y)
    {
        velocityX += x;
        velocityY += y;
    }

    public void update(double time)
    {
        positionX += velocityX * time;
        positionY += velocityY * time;
    }
    public void setVisible() {
    	this.visible= true;
    }
	public void setInvisible() {
		this.visible = false;
	}

	public boolean isVisible() {
			return this.visible;
	}


    public void render(GraphicsContext gc)
    {
    	if (visible)
    		gc.drawImage( image, positionX, positionY );
    }

    public Rectangle2D getBoundary()
    {
        return new Rectangle2D(positionX,positionY,width,height);
    }

    public boolean intersects(Sprite s)
    {
        return s.getBoundary().intersects( this.getBoundary() );
    }
    
    public String toString()
    {
        return " Position: [" + positionX + "," + positionY + "]" 
        + " Velocity: [" + velocityX + "," + velocityY + "]";
    }
    @Override
    public boolean equals(Object object)
    {
        boolean isEqual= false;

        if (object != null && object instanceof Sprite)
        {
            isEqual = ((this.positionX == ((Sprite) object).positionX) && (this.positionY == ((Sprite) object).positionY));
        }

        return isEqual;
    }
    
    @Override
    public int hashCode() {
        return (int) (this.positionX+this.positionY);
    }

}