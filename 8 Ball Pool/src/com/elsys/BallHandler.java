package com.elsys;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;

public class BallHandler {
    private LinkedList<Ball> balls;

    public BallHandler() throws Exception
    {
        balls = new LinkedList<>();
        setupBalls();
    }

    public void removeBall(Ball ball){
        balls.remove(ball);
    }

    public int getTotalBalls()
    {
        return this.balls.size();
    }

    public boolean checkForMovement()
    {
        for(Ball ball : balls)
        {
            if(ball.velocity.x == 0 && ball.velocity.y == 0)
                continue;
            return false;
        }
        return true;
    }

    public void collideBalls(Ball a, Ball b)
    {
        // every ball will have a mass of 1.0d
        Vector2D delta = a.position.minus(b.position);
        double totalRadius = a.radius + b.radius;
        double dist = delta.dot(delta);

        if(dist > totalRadius * totalRadius)
            return; // balls not colliding

        double deltaLength = delta.length();

        // minimum translation distance to push balls apart after intersecting
        Vector2D mtd;
        if(deltaLength != 0.0d)
        {
            mtd = delta.times((totalRadius - deltaLength)/deltaLength);
        }
        else // special case. balls are exactly on top of each other. we do not want to divide by zero
        {
            deltaLength = totalRadius - 1.0d;
            delta = new Vector2D(totalRadius, 0.0d);

            mtd = delta.times((totalRadius - deltaLength)/deltaLength);
        }

        // push-pull them apart based off their mass
        a.position.add(mtd.times(1.0d / 2.0d));
        b.position.subtract(mtd.times(1.0d / 2.0d));

        Vector2D impactSpeed = a.velocity.minus(b.velocity);
        double k = impactSpeed.dot(mtd.normalize());

        // balls intersecting but moving away from each other anyways
        if(k > 0.0d)
            return;

        // collision impulse
        double i = (- (1.0d + 0.85d) * k) / 2.0d;
        Vector2D impulse = mtd.times(i);

        // change of momentum
        a.velocity.add(impulse.times(1.0d));
        b.velocity.subtract(impulse.times(1.0d));
    }

    public void tick()
    {
        for(int i = 0; i < balls.size(); i++)
        {
            for(int j = i + 1; j < balls.size(); j++)
            {
                if (balls.get(i).collide(balls.get(j)))
                {
                    if(balls.get(i) instanceof WhiteBall && Main.canMoveWhiteBall && checkForMovement()) continue;
                    if(Main.firstBallHit && balls.get(i) instanceof WhiteBall)
                    {
                        Main.firstHit = balls.get(j).type;
                        if(!balls.get(j).type.equals(Main.players[Main.playerTurn].getBallType()) && !Main.players[Main.playerTurn].getBallType().equals("None"))
                        {
                            Main.shouldSwap = true;
                            Main.canMoveWhiteBall = true;
                        }
                        Main.firstBallHit = false;
                    }
                    collideBalls(balls.get(i), balls.get(j));
                }
            }
        }
        for(int i = 0; i < getTotalBalls(); i++)
            if(balls.get(i).tick()) i--;

        if(checkForMovement() && Main.shouldSwap)
        {
            Main.swapTurns();
            Main.shouldSwap = false;
        }
    }

    public LinkedList<Ball> getBalls() { return balls; }

    public void render(Graphics g)
    {
        for(Ball ball : balls)
            ball.render(g);
    }

    void setupBalls() throws Exception
    {
        WhiteBall whiteBall = new WhiteBall(this);
        balls.add(whiteBall);
        balls.add(new Ball(new Vector2D(150, 180), new Vector2D(0, 0),"stripe", ImageIO.read(new File("./resources/ball 11.png")), 11, this));
        balls.add(new Ball(new Vector2D(150, 210), new Vector2D(0, 0),"solid", ImageIO.read(new File("./resources/ball 2.png")), 2, this));
        balls.add(new Ball(new Vector2D(150, 240), new Vector2D(0, 0),"stripe", ImageIO.read(new File("./resources/ball 13.png")), 13, this));
        balls.add(new Ball(new Vector2D(150, 270), new Vector2D(0, 0),"solid", ImageIO.read(new File("./resources/ball 4.png")), 4, this));
        balls.add(new Ball(new Vector2D(150, 300), new Vector2D(0, 0),"solid", ImageIO.read(new File("./resources/ball 5.png")), 5, this));
        balls.add(new Ball(new Vector2D(180, 195), new Vector2D(0, 0),"solid", ImageIO.read(new File("./resources/ball 6.png")), 6, this));
        balls.add(new Ball(new Vector2D(180, 225), new Vector2D(0, 0),"stripe", ImageIO.read(new File("./resources/ball 10.png")), 10, this));
        balls.add(new Ball(new Vector2D(180, 255), new Vector2D(0, 0),"solid", ImageIO.read(new File("./resources/ball 3.png")), 3, this));
        balls.add(new Ball(new Vector2D(180, 285), new Vector2D(0, 0),"stripe", ImageIO.read(new File("./resources/ball 14.png")), 14, this));
        balls.add(new Ball(new Vector2D(210, 210), new Vector2D(0, 0),"stripe", ImageIO.read(new File("./resources/ball 15.png")), 15, this));
        balls.add(new Ball(new Vector2D(210, 270), new Vector2D(0, 0),"solid", ImageIO.read(new File("./resources/ball 1.png")), 1, this));
        balls.add(new Ball(new Vector2D(240, 225), new Vector2D(0, 0),"solid", ImageIO.read(new File("./resources/ball 7.png")), 7, this));
        balls.add(new Ball(new Vector2D(240, 255), new Vector2D(0, 0),"stripe", ImageIO.read(new File("./resources/ball 12.png")), 12, this));
        balls.add(new Ball(new Vector2D(270, 240), new Vector2D(0, 0),"stripe", ImageIO.read(new File("./resources/ball 9.png")), 9, this));
        balls.add(new Ball(new Vector2D(210, 240), new Vector2D(0, 0),"blackBall", ImageIO.read(new File("./resources/ball 8.png")), 8, this));
    }

    public WhiteBall getWhiteBall(){ return (WhiteBall) this.balls.getFirst(); }

}
