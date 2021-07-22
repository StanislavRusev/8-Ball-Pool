package com.elsys;

import java.awt.*;
import java.awt.geom.Rectangle2D;

public class Ball {
    BallHandler ballHandler;
    Vector2D position;
    Vector2D velocity;
    String type;
    Image image;
    int number;
    int radius;

    public Image getImage() {
        return image;
    }

    public String getType() {
        return type;
    }

    public Ball(Vector2D position, Vector2D velocity, String type, Image image, int number, BallHandler ballHandler)
    {
        this.position = position;
        this.velocity = velocity;
        this.type = type;
        this.number = number;
        this.radius = 15;
        this.image = image.getScaledInstance(2 * radius, 2 * radius, 0);
        this.ballHandler = ballHandler;
    }

    public boolean collide(Ball b)
    {
        double xd = position.x - b.position.x;
        double yd = position.y - b.position.y;

        double sumRadius = radius + b.radius;
        double sqrRadius = sumRadius * sumRadius;

        double distSqr = (xd * xd) + (yd * yd);

        return distSqr <= sqrRadius;
    }


    public boolean DeleteBall()
    {
        velocity.x = 0;
        velocity.y = 0;
        if(this instanceof WhiteBall)
        {
            setPosition(new Vector2D(720, 240));
            Main.shouldSwap = true;
            Main.canMoveWhiteBall = true;
            return false;
        }

        if(Main.players[Main.playerTurn].getBallType().equals("None")) {}
        else if(Main.firstHit.equals(Main.players[Main.playerTurn].getBallType()))
            Main.shouldSwap = false;
        else
        {
            Main.shouldSwap = true;
            Main.canMoveWhiteBall = true;
        }

        if (Main.TurnCounter != 1)
        {
            if(!this.type.equals("blackBall"))
            {
                if(Main.players[Main.playerTurn].getBallType().equals("None"))
                {
                    Main.players[Main.playerTurn].setBallType(this.type);
                    if(this.type.equals("stripe"))
                    {
                        Main.players[1 - Main.playerTurn].setBallType("solid");
                    }
                    else
                    {
                        Main.players[1 - Main.playerTurn].setBallType("stripe");
                    }
                    Main.shouldSwap = false;
                }
                else if(!Main.players[Main.playerTurn].getBallType().equals(this.type))
                {
                    if(!Main.firstHit.equals(Main.players[Main.playerTurn].getBallType()))
                        Main.canMoveWhiteBall = true;
                    Main.shouldSwap = true;
                }
            }
            else
            {
                if(isBlackValid(Main.players[Main.playerTurn].getBallType()))
                    Main.winState = 1;
                else
                {
                    Main.winState = -1;
                    Main.swapTurns();
                }
                return false;
            }
        }
        else
        {
            if (this.type.equals("blackBall"))
            {
                setPosition(new Vector2D(210.2, 240.2));
                return false;
            }
        }
        ballHandler.removeBall(this);
        return true;
    }

    public boolean isBlackValid(String type)
    {
        if(Main.players[Main.playerTurn].getBallType().equals("None"))
            return false;
        return ballHandler.getBalls().stream().filter(val -> val.getType().equals(type)).count() == 0;
    }


    public void setPosition(Vector2D position)
    {
        this.position.x = position.x;
        this.position.y = position.y;
    }

    public boolean tick()
    {
        Vector2D temp = position.plus(velocity);

        if(temp.y < Main.top && temp.x + 14 > 420 && temp.x + 14 < 465)
            return DeleteBall();

        if(temp.y > Main.bottom && temp.x + 14 > 420 && temp.x + 14 < 465)
            return DeleteBall();

        for(int i = 0; i < 30; i++)
        {
            if(temp.y - 14 > (Main.bottom - 30) + i && temp.x - 14 < (Main.left - 20) + i)
                return DeleteBall();
            if(temp.y - 14 > (Main.bottom - 30) + i && temp.x + 14 > (Main.right + 20) - i)
                return DeleteBall();
            if(temp.y + 14 < (Main.top + 30) - i && temp.x - 14 < (Main.left - 20) + i)
                return DeleteBall();
            if(temp.y + 14 < (Main.top + 30) - i && temp.x + 14 > (Main.right + 20) - i)
                return DeleteBall();
        }

        if(temp.y < Main.top || temp.y > Main.bottom)
            velocity.y = -velocity.y;
        if(temp.x < Main.left || temp.x > Main.right)
            velocity.x = -velocity.x;

        position.add(velocity);
        double velocitySquared = velocity.times(Main.ticks * Main.ticks / 10000).dot(velocity);
        double length = velocity.length() / 100; // make into meters
        if(length <= 0)
            return false; // the ball is not moving
        double s = Math.sqrt((velocitySquared - length * Main.friction * Main.gravity - 0.25 * velocitySquared * 17 * 0.09 * length) / velocitySquared); // more physics....
        if(Double.isNaN(s))
            s = 0;
        velocity.multiply(s);
        return false;
    }

    void render(Graphics g) { g.drawImage(image,(int)position.x, (int)position.y,null,null); }

}
