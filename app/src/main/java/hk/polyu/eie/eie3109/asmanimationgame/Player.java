package hk.polyu.eie.eie3109.asmanimationgame;

import android.graphics.Bitmap;

public class Player{
    private int health = 100;
    private int maxHealth = 100;
    private int attack = 1;
    private int exp = 0;
    private int level = 1;
    private Bitmap bitmap;

    public Player(Bitmap bitmap){
        this.bitmap = bitmap;
    }

    public String getHealthText(){
        String msg = "HP: " + getHealth() + "/" + getMaxHealth();
        return msg;
    }

    public String getLevelText(){
        String msg = "Lv." + this.level;
        return msg;
    }

    public void setMaxHealth(int maxHealth){ this.maxHealth = maxHealth; }

    public int getMaxHealth(){return maxHealth;}

    public void setHealth(int health){ this.health = health; }

    public int getHealth(){ return health; }

    public void setAttack(int attack){ this.attack = attack; }

    public int getAttack(){ return attack;}

    public void receivedDmg(int dmg){
        health -= dmg;
    }

    public void levelUp(){
        level++;
        setAttack(getAttack()+1);
        setMaxHealth(getMaxHealth()+20);
        setHealth(getMaxHealth());
    }

    public void gainExp(int exp){
        int expNow = this.exp + exp;
        int levelRised = expNow/10;
        if(levelRised > 0){
            for(int i = 0; i < levelRised; i++) {
                levelUp();
            }
            int expLeft = expNow%10;
            this.exp = expLeft;
        }
        else {
            this.exp = expNow;
        }
    }

    public boolean isKilled(){
        if(health > 0){
            return false;
        }
        return true;
    }

    public Bitmap getGraphic(){return bitmap;}

}
