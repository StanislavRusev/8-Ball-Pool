package com.elsys;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;

public class CueHandler {
    private Cue cue;
    private BallHandler ballHandler;
    private Vector2D mousePos;
    private boolean shooting;

    public CueHandler(Vector2D position, BallHandler ballHandler) throws Exception
    {
        this.cue = new Cue(new Vector2D(720, 240), ImageIO.read(new File("./resources/cue.png")));
        this.ballHandler = ballHandler;
        this.mousePos = position;
        this.shooting = false;
    }

    public void setMouseCords(Vector2D mousePos) {
        this.mousePos = mousePos;
    }

    public void setShooting(boolean shooting){
        this.shooting = shooting;
    }

    public double getPowerOfShooting(){
        return cue.getPowerShooting();
    }

    public void tick(){
        cue.tick(ballHandler.getWhiteBall(), mousePos, shooting);
    }

    public void render(Graphics g)
    {
        if(ballHandler.checkForMovement() && !Main.canMoveWhiteBall)
            cue.render(g, shooting);
    }
}
