package hk.polyu.eie.eie3109.asmanimationgame;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class GameThread2 extends Thread{

    private SurfaceHolder surfaceHolder;
    private Panel panel;
    private boolean run = false;

    public GameThread2(SurfaceHolder surfaceHolder, Panel panel){
        this.surfaceHolder = surfaceHolder;
        this.panel = panel;
    }

    public void setRunning(boolean run){
        this.run = run;
    }

    public SurfaceHolder getSurfaceHolder(){
        return surfaceHolder;
    }

    @SuppressLint("WrongCall")
    @Override
    public void run(){
        super.run();
        //Canvas c;
        while (run){
            //c = null;
            try {
                //c = surfaceHolder.lockCanvas();
                synchronized (surfaceHolder){
                    panel.updateMovement();
                    //panel.onDraw(c);
                }
                sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                //if(c != null){
                    //surfaceHolder.unlockCanvasAndPost(c);
                //}
            }
        }
    }
}