package com.elsys;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

public class MouseInput extends MouseAdapter {
    BallHandler ballHandler;
    CueHandler cueHandler;
    Vector2D mouseCords;

    public MouseInput(BallHandler ballHandler, CueHandler cueHandler)
    {
        this.ballHandler = ballHandler;
        this.cueHandler = cueHandler;
        this.mouseCords = new Vector2D(720, 240);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if(!Main.canMoveWhiteBall)
            cueHandler.setMouseCords(new Vector2D(e.getX(), e.getY()));
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if(!Main.canMoveWhiteBall)
            cueHandler.setMouseCords(new Vector2D(e.getX(), e.getY()));
        else if(ballHandler.checkForMovement())
        {
            if(Main.TurnCounter == 0)
            {
                if(e.getX() > 681 && e.getX() < Main.right + 14 && e.getY() > Main.top + 14 && e.getY() < Main.bottom + 14)
                    ballHandler.getWhiteBall().setPosition(new Vector2D(e.getX() - 14, e.getY() - 14));
            }
            else if(e.getX() > Main.left + 14 && e.getX() < Main.right + 14 && e.getY() > Main.top + 14 && e.getY() < Main.bottom + 14)
                ballHandler.getWhiteBall().setPosition(new Vector2D(e.getX() - 14, e.getY() - 14));
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(!Main.canMoveWhiteBall)
        {
            mouseCords = new Vector2D(e.getX(), e.getY());
            cueHandler.setShooting(true);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(!Main.canMoveWhiteBall)
        {
            cueHandler.setShooting(false);
            double powerOfShooting;
            powerOfShooting = cueHandler.getPowerOfShooting();
            if (powerOfShooting < 1)
                powerOfShooting = 0.7;
            if (ballHandler.checkForMovement())
            {
                Main.TurnCounter++;
                ballHandler.getWhiteBall().setVelocity(new Vector2D(mouseCords.x - (ballHandler.getWhiteBall().position.x + 14), mouseCords.y - (ballHandler.getWhiteBall().position.y + 14)).normalize().multiply(powerOfShooting));
                Main.firstBallHit = true;
                Main.shouldSwap = true;
                Main.firstHit = "None";
            }
        }
    }
}
