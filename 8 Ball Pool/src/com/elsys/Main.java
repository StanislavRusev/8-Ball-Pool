package com.elsys;

public class Main {
    static int left = 57;
    static int top = 60;
    static int bottom = 420;
    static int right = 813;
    static double friction = 0.02;
    static double gravity = 9.8;
    static double ticks = 300;
    static int playerTurn = 0;
    static boolean shouldSwap = false;
    static Player[] players = new Player[2];
    static int winState = 0;
    static int TurnCounter = 0;
    static boolean firstBallHit = true;
    static String firstHit = "None";
    static boolean canMoveWhiteBall = true;
    static double innateRotation = 2.37;

    public static void swapTurns()
    {
        playerTurn = 1 - playerTurn;
    }

    public static void main(String[] args) throws Exception {
        players[0] = new Player("Player1");
        players[1] = new Player("Player2");
        new Game();
    }
}
