package hk.polyu.eie.eie3109.asmanimationgame;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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

public class Panel extends SurfaceView implements SurfaceHolder.Callback {

    private Bitmap bmp;
    private GameThread thread;
    private ArrayList<NonPlayerObject> npcs = new ArrayList<NonPlayerObject>();
    public Player player;
    private Bitmap bg;
    private int score = 0;
    Context context = null;

    public Panel(Context context) {
        super(context);
        this.context = context;
        getHolder().addCallback(this);

        bmp = BitmapFactory.decodeResource(getResources(), R.drawable.m_puppet);
        bg = BitmapFactory.decodeResource(getResources(), R.drawable.bg_demonic_crop);
        player = new Player(resizeImage(BitmapFactory.decodeResource(getResources(), R.drawable.chen_fighter),500,500));

        thread = new GameThread(getHolder(), this);

        setFocusable(true);
        spawnRandomMonster(true);
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
        canvas.drawText(player.getHealthText(), 10, 75, paint);
        canvas.drawText(player.getLevelText(), 900, 75, paint);
        canvas.drawText("Score: "+score*10, 450, 75, paint);

        canvas.drawBitmap(player.getGraphic(), getWidth()/2-(player.getGraphic().getWidth()/2), getHeight()-(player.getGraphic().getHeight()/2)-100, null);

        if(player.isKilled()){
            thread.setRunning(false);

            canvas.drawText("GAME OVER", getWidth()/2-200, getHeight()/2, paint);
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
                            n.receivedDmg(1);

                            TimerTask task = new TimerTask() {
                                public void run() {
                                    //Do something...
                                    n.setGraphic(BitmapFactory.decodeResource(getResources(), R.drawable.m_puppet_dead));
                                    TimerTask taskHit = new TimerTask() {
                                        public void run() {
                                            //Do something...
                                            n.setGraphic(BitmapFactory.decodeResource(getResources(), R.drawable.m_puppet));
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
                                score+=5;
                                if(npcs.size() < 2)
                                {
                                    spawnRandomMonster(false);
                                }
                            }
                            if(killedNpc != -1)
                            {
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

        for(GraphicObject graphic: npcs) {
            coord = graphic.getCoordinates();
            movement = graphic.getMovement();

            x = (movement.getXDirection()==Movement.X_DIRECTION_RIGHT)?coord.getX()+movement.getXSpeed():coord.getX()-movement.getXSpeed();
            //check x if reaches border
            if(x < 0) {
                movement.toggleXDirection();
                coord.setX(-x);
            } else if(x + graphic.getGraphic().getWidth() > getWidth()) {
                movement.toggleXDirection();
                coord.setX(x + getWidth() - (x+graphic.getGraphic().getWidth()));
            } else {
                coord.setX(x);
            }

            y = (movement.getYDirection()==Movement.Y_DIRECTION_DOWN)?coord.getY()+movement.getYSpeed():coord.getY()-movement.getYSpeed();
            //check y if reaches border
            if(y < 0) { //Collide top border
                movement.toggleYDirection();
                coord.setY(-y);
            } else if(y + graphic.getGraphic().getHeight() > getHeight()) { //Collide bottom border
                movement.toggleYDirection();
                coord.setY(y + getHeight() - (y+graphic.getGraphic().getHeight()));
                //if(x > getWidth()/2-(player.getGraphic().getWidth()/2) && x < getWidth()/2+(player.getGraphic().getWidth()/2))
                    player.setHealth(player.getHealth()-100);

            } else {
                coord.setY(y);
            }
        }
    }

    public void gameOver(){
        ((Activity)context).startActivity(new Intent(context, TitleScreen.class));
        ((Activity)context).finish();
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
            int imageWidth = (imageHeight * image.getWidth())
                    / image.getHeight();

            if (imageWidth > maxWidth) {
                imageWidth = maxWidth;
                imageHeight = (imageWidth * image.getHeight())
                        / image.getWidth();
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
