package com.elsys;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.Vector;

public class Cue {
    Vector2D position;
    Image image;
    int radius;
    Vector2D mouseCords;
    double xDistance;
    double yDistance;
    double rotationAngle;
    double dist;
    double distX;
    double distY;
    double whatPer;
    double addX;
    double addY;
    double maxPull;
    Vector2D oldPos;
    double powerShooting;

    public Cue(Vector2D position, Image image)
    {
        this.position = position;
        this.oldPos = new Vector2D(position.x, position.y);
        this.radius = 150;
        this.image = image.getScaledInstance(2 * radius, 2 * radius, 0);
        this.mouseCords = new Vector2D(720, 240);
        this.xDistance = mouseCords.x - position.x;
        this.yDistance = mouseCords.y - position.y;
        this.rotationAngle = Math.atan2(yDistance, xDistance);

        this.dist = mouseCords.distance(new Vector2D(720, 240));
        this.distX = 720 - mouseCords.x;
        this.distY = 240 - mouseCords.y;
        this.whatPer = (15 / this.dist) * 100;
        this.addX = (this.distX * this.whatPer) / 100;
        this.addY = (this.distY * this.whatPer) / 100;
        this.maxPull = 50;
        this.powerShooting = 0;
    }

    public double getPowerShooting(){
        return powerShooting;
    }

    void tick(WhiteBall whiteBall, Vector2D mousePos, boolean shooting)
    {
        if(!shooting)
        {
            mouseCords = mousePos;
            Vector2D whiteBallPos = new Vector2D(whiteBall.position.x + 14, whiteBall.position.y + 14);
            dist = whiteBallPos.distance(mousePos);
            distX = whiteBallPos.x - mousePos.x;
            distY = whiteBallPos.y - mousePos.y;
            whatPer = (15 / dist) * 100;
            addX = (distX * whatPer) / 100;
            addY = (distY * whatPer) / 100;

            position.x = whiteBallPos.x + addX;
            position.y = whiteBallPos.y + addY;
            oldPos.x = position.x;
            oldPos.y = position.y;
        }
        else
        {
            // mousePos - current, mouseCords - old
            double bigDistance = oldPos.distance(mouseCords);
            double smallDistance = mouseCords.distance(mousePos);
            double middleDistance = oldPos.distance(mousePos);
            if(bigDistance >= middleDistance)
            {
                double per = (smallDistance / bigDistance) * 100;
                double diffX = ((oldPos.x - mouseCords.x) * per) / 100;
                double diffY = ((oldPos.y - mouseCords.y) * per) / 100;
                Vector2D newPos = new Vector2D(oldPos.x + diffX, oldPos.y + diffY);

                if(oldPos.distance(newPos) <= maxPull)
                {
                    position.x = oldPos.x + diffX;
                    position.y = oldPos.y + diffY;
                    double pur = (oldPos.distance(position) / maxPull) * 100;
                    powerShooting = ((pur * 6) / 100);
                }
            }
            else
            {
                position.x = oldPos.x;
                position.y = oldPos.y;
            }
        }
    }

    void render(Graphics g, boolean shooting)
    {
        Graphics2D g2d = (Graphics2D) g.create();

        if(!shooting)
        {
            xDistance = mouseCords.x - position.x;
            yDistance = mouseCords.y - position.y;
            rotationAngle = Math.atan2(yDistance, xDistance);
        }

        //Make a backup so that we can reset our graphics object after using it.
        AffineTransform backup = g2d.getTransform();
        //rx is the x coordinate for rotation, ry is the y coordinate for rotation, and angle
        //is the angle to rotate the image. If you want to rotate around the center of an image,
        //use the image's center x and y coordinates for rx and ry.
        AffineTransform a = AffineTransform.getRotateInstance(rotationAngle + Main.innateRotation, position.x, position.y);
        //Set our Graphics2D object to the transform
        g2d.setTransform(a);
        //Draw our image like normal
        g2d.drawImage(image, (int) position.x, (int) position.y, null);
        //Reset our graphics object so we can draw with it again.
        g2d.setTransform(backup);
    }
}
