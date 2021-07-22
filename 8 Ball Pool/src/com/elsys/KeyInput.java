package com.elsys;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class KeyInput extends KeyAdapter {

    @Override
    public void keyPressed(KeyEvent e)
    {
        int keyCode = e.getKeyCode();
        if(Main.canMoveWhiteBall && keyCode == KeyEvent.VK_SPACE)
            Main.canMoveWhiteBall = false;
    }
}
