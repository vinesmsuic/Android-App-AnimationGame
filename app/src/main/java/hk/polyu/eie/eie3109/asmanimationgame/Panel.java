package hk.polyu.eie.eie3109.asmanimationgame;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;

public class Panel extends SurfaceView implements SurfaceHolder.Callback {

    public static int MODE = Context.MODE_PRIVATE;
    public static final String PREFERENCE_NAME = "SaveSetting";
    public Player player;

    private Bitmap bmp;
    private Bitmap bmpHurt;
    private GameThread thread;
    private ArrayList<NonPlayerObject> npcs = new ArrayList<NonPlayerObject>();
    private Bitmap bg;
    private int score = 0;
    Context context = null;
    private int gameLevel = 0;
    private int killCount = 0;
    private int requireKills = new Random().nextInt(5) + 20;

    private int gameLevel1Cleared = 0;
    private int gameLevel2Cleared = 0;
    private int gameLevel3Cleared = 0;


    public Panel(Context context) {
        super(context);
        this.context = context;
        getHolder().addCallback(this);

        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.m_puppet);
        bmpHurt = BitmapFactory.decodeResource(getResources(), R.drawable.m_puppet_dead);
        bg = BitmapFactory.decodeResource(getResources(), R.drawable.bg_demonic_crop);
        player = new Player(resizeImage(BitmapFactory.decodeResource(getResources(), R.drawable.chen_fighter),500,500));

        thread = new GameThread(getHolder(), this);

        setFocusable(true);
        spawnRandomMonster(true);
        loadPreferences();
    }

    public Panel(Context context, int gameLevel) {
        super(context);
        this.context = context;
        getHolder().addCallback(this);

        this.gameLevel = gameLevel;

        switch (gameLevel)
        {
            case 1:
                bmp = BitmapFactory.decodeResource(getResources(), R.drawable.m_slime);
                bmpHurt = BitmapFactory.decodeResource(getResources(), R.drawable.m_slime_dead);
                bg = BitmapFactory.decodeResource(getResources(), R.drawable.bg_grass_croped);
                break;
            case 2:
                bmp = BitmapFactory.decodeResource(getResources(), R.drawable.m_bat);
                bmpHurt = BitmapFactory.decodeResource(getResources(), R.drawable.m_bat_dead);
                bg = BitmapFactory.decodeResource(getResources(), R.drawable.bg_demonic_crop);
                break;
            case 3:
                bmp = BitmapFactory.decodeResource(getResources(), R.drawable.m_puppet);
                bmpHurt = BitmapFactory.decodeResource(getResources(), R.drawable.m_puppet_dead);
                bg = BitmapFactory.decodeResource(getResources(), R.drawable.bg_boss_crop);
                //I want to
                break;
                //free
        }
        player = new Player(resizeImage(BitmapFactory.decodeResource(getResources(), R.drawable.chen_fighter),500,500));
        player.setXLocation(400);

        thread = new GameThread(getHolder(), this);

        setFocusable(true);
        spawnRandomMonster(true);
        loadPreferences();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Coordinates coords;
        int x, y;
        canvas.drawColor(Color.WHITE);
        //bg = resizeImage(bg, canvas.getWidth(), canvas.getHeight());
        canvas.drawBitmap(bg,0,0,null);


        //https://stackoverflow.com/questions/2655402/android-canvas-drawtext
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);
        paint.setTextSize(60);
        canvas.drawText(player.getHealthText(), 10, 90, paint);
        canvas.drawText(player.getLevelText(), 900, 90, paint);
        canvas.drawText("Score: "+score*10, 400, 90, paint);

        canvas.drawBitmap(player.getGraphic(), player.getXLocation(), getHeight()-(player.getGraphic().getHeight()/2)-100, null);

        if(killCount > requireKills){
            thread.setRunning(false);

            canvas.drawText("CLEAR!", getWidth()/2-100, getHeight()/2, paint);
            TimerTask task = new TimerTask() {
                public void run() {
                    gameLevelClear();
                }
            };
            Timer timer = new Timer("Timer");
            timer.schedule(task, 2000);

        }

        if(player.isKilled()){
            thread.setRunning(false);

            canvas.drawText("GAME OVER", getWidth()/2-100, getHeight()/2, paint);
            TimerTask task = new TimerTask() {
                public void run() {
                    gameOver();
                }
            };
            Timer timer = new Timer("Timer");
            timer.schedule(task, 2000);

        }

        for(GraphicObject graphic: npcs){
            coords = graphic.getCoordinates();
            x = coords.getX();
            y = coords.getY();
            canvas.drawBitmap(graphic.getGraphic(), x, y, null);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        synchronized (thread.getSurfaceHolder()) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                int x = (int) event.getX();
                int y = (int) event.getY();

                player.setXLocation(x-250);

                int killedNpc = -1;

                if(npcs.size() > 0)
                {
                    //Check if there is overlap of event X
                    for (NonPlayerObject n : npcs){
                        int gX = n.getCoordinates().getX();
                        int gY = n.getCoordinates().getY();
                        int gW = n.getGraphic().getWidth();
                        int gH = n.getGraphic().getHeight();

                        if(gX <= x && x <= (gX + gW) && gY <= y && y <= (gY + gH)){
                            score+=1;
                            n.receivedDmg(player.getAttack());

                            TimerTask task = new TimerTask() {
                                public void run() {
                                    //Do something...
                                    n.setGraphic(bmpHurt);
                                    TimerTask taskHit = new TimerTask() {
                                        public void run() {
                                            //Do something...
                                            n.setGraphic(bmp);
                                        }
                                    };
                                    Timer timer = new Timer("Timer");
                                    timer.schedule(taskHit, 100);
                                }
                            };
                            Timer timer = new Timer("Timer");
                            timer.schedule(task, 50);

                            if(npcs.size() < 2)
                            {
                                int backups = new Random().nextInt(10); // 10% Calling Backup Monsters
                                if(backups == 1){
                                        spawnRandomMonster(true);
                                }
                            }

                            if(n.isKilled()){
                                //npcs.remove(n); //Causing java.util.ConcurrentModificationException
                                killedNpc = npcs.indexOf(n);
                                player.gainExp(n.getExp());
                                player.setGold(player.getGold() + 5);
                                score+=5;
                                if(npcs.size() < 2)
                                {
                                    spawnRandomMonster(false);
                                }
                            }
                            if(killedNpc != -1)
                            {
                                killCount++;
                                npcs.remove(killedNpc);
                            }
                            return true;
                        }
                    }
                }
                else //Spawn 1 monster
                {
                    spawnRandomMonster(false);
                }

            }
            return true;
        }
    }

    public void spawnRandomMonster(boolean longDelay){

        int delay = 0;
        if(longDelay){
            delay = (new Random().nextInt(5) + 1)*1000;
        }
        else
        {
            delay = 1000;
        }

        TimerTask taskHit = new TimerTask() {
            public void run() {
                //Create new graphic
                NonPlayerObject monster = new NonPlayerObject(bmp);
                monster.getCoordinates().setRandomXY();
                monster.setHealth((new Random().nextInt(3) + 1)* gameLevel + player.getLevel()) ;
                monster.setAttack(20*gameLevel - player.getLevel() + new Random().nextInt(5));
                npcs.add(monster);
            }
        };
        Timer timer = new Timer("Timer");
        timer.schedule(taskHit, delay);

    }

    public void updateMovement() {
        Coordinates coord;
        Movement movement;

        int x, y;

        for(NonPlayerObject n : npcs) {
            coord = n.getCoordinates();
            movement = n.getMovement();

            x = (movement.getXDirection()==Movement.X_DIRECTION_RIGHT)?coord.getX()+movement.getXSpeed():coord.getX()-movement.getXSpeed();
            //check x if reaches border
            if(x < 0) {
                movement.toggleXDirection();
                coord.setX(-x);
            } else if(x + n.getGraphic().getWidth() > getWidth()) {
                movement.toggleXDirection();
                coord.setX(x + getWidth() - (x+n.getGraphic().getWidth()));
            } else {
                coord.setX(x);
            }

            y = (movement.getYDirection()==Movement.Y_DIRECTION_DOWN)?coord.getY()+movement.getYSpeed():coord.getY()-movement.getYSpeed();
            //check y if reaches border
            if(y < 0) { //Collide top border
                movement.toggleYDirection();
                coord.setY(-y);
            } else if(y + n.getGraphic().getHeight() > getHeight()) { //Collide bottom border
                movement.toggleYDirection();
                coord.setY(y + getHeight() - (y+n.getGraphic().getHeight()));
                if(x > player.getXLocation()-50 && x < (player.getXLocation()+(player.getGraphic().getWidth()/3)))
                    player.setHealth(player.getHealth() - n.getAttack());

            } else {
                coord.setY(y);
            }
        }
    }

    public void gameLevelClear(){
        savePreferences();
        ((Activity)context).startActivity(new Intent(context, LevelSelect.class));
        ((Activity)context).finish();
    }

    public void gameOver(){
        clearStatus();
        savePreferences();
        ((Activity)context).startActivity(new Intent(context, TitleScreen.class));
        ((Activity)context).finish();
    }

    private void savePreferences(){
        //Open the sharedPreferences editor and save
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, MODE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        switch (gameLevel){
            case 1:
                editor.putInt("gameLevel1Cleared", gameLevel1Cleared+1);
                break;
            case 2:
                editor.putInt("gameLevel2Cleared", gameLevel2Cleared+1);
                break;
            case 3:
                editor.putInt("gameLevel3Cleared", gameLevel3Cleared+1);
                break;
        }

        //Key value pairs
        editor.putInt("Level", player.getLevel());
        editor.putInt("Health", player.getHealth());
        editor.putInt("MaxHealth", player.getMaxHealth());
        editor.putInt("Attack", player.getAttack());
        editor.putInt("Score", score);
        editor.putInt("Exp", player.getExp());
        editor.putInt("Gold", player.getGold());
        editor.apply();
    }

    private void loadPreferences(){
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, MODE);

        //Second param: Return that default value when there is no matching key.
        int attack = sharedPreferences.getInt("Attack", 1);
        int level = sharedPreferences.getInt("Level", 1);
        int health = sharedPreferences.getInt("Health", 100);
        int maxHealth = sharedPreferences.getInt("MaxHealth", 100);
        int exp = sharedPreferences.getInt("Exp", 0);
        int gold = sharedPreferences.getInt("Gold", 0);

        player.setAttack(attack);
        player.setHealth(health);
        player.setMaxHealth(maxHealth);
        player.setLevel(level);
        player.setExp(exp);
        player.setGold(gold);


        score = sharedPreferences.getInt("Score", 0);
        gameLevel1Cleared = sharedPreferences.getInt("gameLevel1Cleared", 0);
        gameLevel2Cleared = sharedPreferences.getInt("gameLevel2Cleared", 0);
        gameLevel3Cleared = sharedPreferences.getInt("gameLevel3Cleared", 0);

    }

    public void clearStatus(){
        score = 0;
        player.setExp(0);
        player.setGold(0);
        player.setMaxHealth(100);
        player.setHealth(100);
        player.setAttack(1);
        player.setLevel(1);


        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, MODE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        //Key value pairs
        editor.putInt("gameLevel1Cleared", 0);
        editor.putInt("gameLevel2Cleared", 0);
        editor.putInt("gameLevel3Cleared", 0);
        editor.putInt("Level", 1);
        editor.putInt("Health", 100);
        editor.putInt("MaxHealth", 100);
        editor.putInt("Attack", 1);
        editor.putInt("Score", 0);
        editor.putInt("Exp", 0);
        editor.putInt("Gold", 0);
        editor.apply();
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {
        boolean retry = true;
        thread.setRunning(false);
        while (retry){
            try {
                thread.join();
                retry = false;
            } catch (InterruptedException e) {
                //keep trying here
            }
        }
    }

    //https://stackoverflow.com/questions/10765300/android-setting-background-on-canvas-using-png-file
    public Bitmap resizeImage(Bitmap image,int maxWidth, int maxHeight)
    {
        Bitmap resizedImage = null;
        try {
            int imageHeight = image.getHeight();


            if (imageHeight > maxHeight)
                imageHeight = maxHeight;
            int imageWidth = (imageHeight * image.getWidth()) / image.getHeight();

            if (imageWidth > maxWidth) {
                imageWidth = maxWidth;
                imageHeight = (imageWidth * image.getHeight()) / image.getWidth();
            }

            if (imageHeight > maxHeight)
                imageHeight = maxHeight;
            if (imageWidth > maxWidth)
                imageWidth = maxWidth;


            resizedImage = Bitmap.createScaledBitmap(image, imageWidth,
                    imageHeight, true);
        } catch (OutOfMemoryError e) {

            e.printStackTrace();
        }catch(NullPointerException e)
        {
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return resizedImage;
    }
}
