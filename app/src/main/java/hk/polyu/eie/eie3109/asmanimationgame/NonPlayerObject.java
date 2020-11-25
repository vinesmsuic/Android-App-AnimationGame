package hk.polyu.eie.eie3109.asmanimationgame;

import android.graphics.Bitmap;

public class NonPlayerObject extends GraphicObject {

    protected int health = 3;
    protected int exp = 3;
    private int imgWidth;
    private int imgHeight;

    public NonPlayerObject(Bitmap bitmap) {
        super(bitmap);
    }

    public NonPlayerObject(Bitmap bitmap, int health) {
        super(bitmap);
        this.health = health;
        this.exp += (health/10);
    }

    public int getExp(){return exp;}

    public void receivedDmg(int dmg) {
        health -= dmg;

    }

    public boolean isKilled(){
        if(health <= 0){
            return true;
        }
        return false;
    }

    @Override
    public void setGraphic(Bitmap bitmap) {
        super.setGraphic(bitmap);
    }

}
