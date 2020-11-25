package hk.polyu.eie.eie3109.asmanimationgame;

import android.graphics.Bitmap;

import java.util.Random;

public class Coordinates {
    private int x = 100;
    private int y = 0;
    private Bitmap bitmap;

    public Coordinates(Bitmap bitmap){
        this.bitmap = bitmap;
    }

    public void setX(int x){
        this.x = x - bitmap.getWidth()/2;
    }

    public int getX(){
        return x + bitmap.getWidth()/2;
    }

    public void setY(int y){
        this.y = y - bitmap.getHeight()/2;
    }

    public int getY(){
        return y + bitmap.getHeight()/2;
    }

    public void setRandomXY(){
        this.x = new Random().nextInt(1000);
        this.y = new Random().nextInt(1000);
    }
}
