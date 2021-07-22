package com.elsys;

import java.awt.*;

public class Player {
    String name;
    private String ballType;

    Player(String name)
    {
        this.name = name;
        this.ballType = "None";
    }

    public void render(Graphics g, BallHandler h, int x, int y, int player)
    {
        g.drawString(this.name, x,y);

        int stripeCounter = (int) h.getBalls().stream().filter(val -> val.getType().equals("stripe")).count();
        int solidCounter = (int) h.getBalls().stream().filter(val -> val.getType().equals("solid")).count();

        int i = (player == 0) ? -30 : 30;
        x = (player == 0) ? x + 30 : x - 3;
        for(Ball ball : h.getBalls())
        {
            if(ball.getType().equals(this.ballType))
            {
                g.drawImage(ball.getImage(), x + i, y + 11, null);
                if(player == 0)
                    i += 36;
                else
                    i -= 36;
            }
            else if(ball.getType().equals("blackBall"))
            {
                if(this.ballType.equals("stripe") && stripeCounter == 0 || this.ballType.equals("solid") && solidCounter == 0)
                    g.drawImage(ball.getImage(), x + i, y + 11, null);
            }
        }
    }

    String getBallType() { return this.ballType; }

    void setBallType(String ballType) { this.ballType = ballType; }
}
