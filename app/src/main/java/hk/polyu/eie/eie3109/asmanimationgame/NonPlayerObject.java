package hk.polyu.eie.eie3109.asmanimationgame;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class NonPlayerObject extends GraphicObject {

    protected int health = 3;
    protected int exp = 1;
    protected int monsterType = 0;
    protected int attack = 10;

    public NonPlayerObject(Bitmap bitmap) {
        super(bitmap);
    }

    public NonPlayerObject(Bitmap bitmap, int health, int attack) {
        super(bitmap);
        this.attack = attack;
        this.health = health;
        this.exp += (health/10);
    }

    public void setHealth(int health){
        this.health = health;
    }

    public void setMonsterType(int type){
        monsterType = type;
    }

    public int getAttack(){return attack;}

    public void setAttack(int attack){this.attack = attack;}

    public int getMonsterType(){
        return monsterType;
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
