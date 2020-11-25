package hk.polyu.eie.eie3109.asmanimationgame;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

public class MusicService extends Service {
    public MusicService() {
    }

    private static final String TAG = null;
    MediaPlayer player;

    @Override
    public void onCreate() {
        super.onCreate();


        player = MediaPlayer.create(this, R.raw.withered_leaf);
        //setting loop play to true
        //this will make the music continuously playing
        player.setLooping(true);

        player.setVolume(100,100);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //staring the player
        player.start();

        //start sticky means service will be explicity started and stopped
        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        //stopping the player when service is destroyed
        player.stop();

        //Release resources
        player.release();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}