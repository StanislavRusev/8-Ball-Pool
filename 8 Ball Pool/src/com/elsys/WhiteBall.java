package com.elsys;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;

public class WhiteBall extends Ball {
    public WhiteBall(BallHandler ballHandler) throws Exception
    {
        super(new Vector2D(720, 240), new Vector2D(0.0,0.0),"whiteBall", ImageIO.read(new File("./resources/whiteBall.png")), 0, ballHandler);
    }

    void setVelocity(Vector2D v) {
        this.velocity = v;
    }

}
