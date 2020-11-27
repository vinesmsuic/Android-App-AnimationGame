package hk.polyu.eie.eie3109.asmanimationgame;

import java.util.Random;

public class Movement {
    public static final int X_DIRECTION_RIGHT = 1;
    public static final int X_DIRECTION_LEFT = -1;
    public static final int Y_DIRECTION_DOWN = 1;
    public static final int Y_DIRECTION_UP = -1;

    private int randomNum = new Random().nextInt(3) + 18;

    //x and y speed should be the same otherwise the movement looks so weird
    private int xSpeed = randomNum;
    private int ySpeed = randomNum;


    private int xDirection = getRandomDirection();
    private int yDirection = getRandomDirection();

    public void setXYSpeed(int x, int y){
        this.xSpeed = x;
        this.ySpeed = y;
    }

    public void setDirections(int xDirection, int yDirection){
        this.xDirection = xDirection;
        this.yDirection = yDirection;
    }

    private int getRandomDirection(){
        //Set random Direction
        int randomIndex = new Random().nextInt( 2 )+1;
        return randomIndex-1;
    }

    public void toggleXDirection() {
        if (xDirection == X_DIRECTION_RIGHT) {
            xDirection = X_DIRECTION_LEFT;
        } else {
            xDirection = X_DIRECTION_RIGHT;
        }
    }

    public void toggleYDirection(){
        if(yDirection == Y_DIRECTION_UP){
            yDirection = Y_DIRECTION_DOWN;
        }
        else {
            yDirection = Y_DIRECTION_UP;
        }
    }

    public int getXSpeed(){
        return xSpeed;
    }

    public int getYSpeed(){
        return ySpeed;
    }

    public int getXDirection(){
        return xDirection;
    }

    public int getYDirection(){
        return yDirection;
    }

}
