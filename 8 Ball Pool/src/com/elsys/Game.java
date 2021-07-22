package com.elsys;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferStrategy;
import java.io.File;

public class Game extends Canvas implements Runnable{
    private boolean isRunning = false;
    private Thread thread;
    private Image board;
    private BallHandler ballHandler;
    private CueHandler cueHandler;
    private Font UIFont;
    private Image imageUI;

    Game() throws Exception
    {
        new Window("Billiard", 914, 646, this);
        this.board = ImageIO.read(new File("./resources/board.png"));
        this.ballHandler = new BallHandler();
        this.cueHandler = new CueHandler(new Vector2D(720, 240), ballHandler);
        this.addMouseListener(new MouseInput(ballHandler, cueHandler));
        this.addMouseMotionListener(new MouseInput(ballHandler, cueHandler));
        this.addKeyListener(new KeyInput());
        this.UIFont = new Font("TimesRoman", Font.BOLD, 15);
        this.imageUI = ImageIO.read(new File("./resources/UIFrame.png"));
        start();
    }

    private void start()
    {
        isRunning = true;
        thread = new Thread(this);
        thread.start();
    }

    private void stop() throws Exception
    {
        isRunning = false;
        thread.join();
    }

    @Override
    public void run()
    {
        this.requestFocus();
        long lastTime = System.nanoTime();
        double amountOfTicks = Main.ticks;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        long timer = System.currentTimeMillis();
        int frames = 0;

        while(isRunning) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while(delta >= 1) {
                tick();
                delta--;
            }
            render();
            frames++;

            if(System.currentTimeMillis() - timer > 1000) {
                timer += 1000;
                frames = 0;
            }
        }

        try {
            stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void tick()
    {
        if (Main.winState == 0)
        {
            ballHandler.tick();
            cueHandler.tick();
        }
    }

    public void render()
    {
        BufferStrategy bs = this.getBufferStrategy();

        if(bs == null)
        {
            this.createBufferStrategy(3); // render at 2 more frames in advance for clean rendering
            return;
        }

        Graphics g = bs.getDrawGraphics();
        ////////////////////////////////

        if(Main.winState != 0)
        {
            g.setColor(Color.black);
            g.fillRect(0,0, 900, 646);
            g.setColor(Color.green);
            g.setFont(new Font("TimesRoman", Font.BOLD, 60));
            if(Main.winState == -1)
                Main.winState = 1;
            String str = Main.players[Main.playerTurn].name + " has won the game!";
            g.drawString(str, 65, 323);
        }
        else
        {
            g.setColor(new Color(24,34,52,255));
            g.fillRect(0, 0, 900, 646);
            g.drawImage(board, 0, 0, null, null);
            ballHandler.render(g);
            cueHandler.render(g);
            g.setFont(UIFont);
            g.drawImage(imageUI, 0,Main.bottom + 88, null, null);
            g.setColor(Color.white);
            Main.players[0].render(g, ballHandler, Main.left - 30, 540, 0);
            Main.players[1].render(g, ballHandler, Main.right, 540, 1);
            g.drawString(Main.players[Main.playerTurn].name, (Main.right + Main.left - 35) / 2, 525);
            if(Main.canMoveWhiteBall && ballHandler.checkForMovement())
            {
                g.drawString("You can move the white ball", Main.left + 280, 545);
                g.drawString("Press space when you are ready", Main.left + 270, 565);
            }
        }
        ////////////////////////////////
        g.dispose();
        bs.show();
    }
}
