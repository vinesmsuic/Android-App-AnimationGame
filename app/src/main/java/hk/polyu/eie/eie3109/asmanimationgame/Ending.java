package hk.polyu.eie.eie3109.asmanimationgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class Ending extends AppCompatActivity {

    TextView msgEnding;
    boolean stopDisplay = false;
    ImageView imgBackground;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ending);

        // Hide ActionBar - https://www.codevoila.com/post/76/5-ways-to-hide-android-actionbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        SharedPreferences sharedPreferences = getSharedPreferences(Panel.PREFERENCE_NAME, Panel.MODE);
        int score = sharedPreferences.getInt("Score", 0);

        setText("Congratulations! \n" +
                "You Successfully helped Chen to save the world! \n\n\n" +
                "Your Final Score: " + score*10 +
                "\n\n\n\n\n Thanks for Playing! \n Click Anywhere To Continue");

        imgBackground = findViewById(R.id.imgBackground);
        imgBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopDisplay = true;
                clearProgress();
                Intent i = new Intent(getApplicationContext(),TitleScreen.class);
                startActivity(i);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        });
    }

    public void setText(final String s) {
        msgEnding = findViewById(R.id.msgPrologue);
        final int[] i = new int[1];
        i[0] = 0;
        final int length = s.length();
        final Handler handler = new Handler()
        {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                char c= s.charAt(i[0]);
                msgEnding.append(String.valueOf(c));
                i[0]++;
            }
        };

        final Timer timer = new Timer();
        TimerTask taskEverySplitSecond = new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(0);
                if (i[0] == length - 1 || stopDisplay == true) {
                    timer.cancel();
                    timer.purge();
                }
            }
        };
        timer.schedule(taskEverySplitSecond, 1, 50);
    }

    public void clearProgress(){
        //Open the sharedPreferences editor and save
        SharedPreferences sharedPreferences = getSharedPreferences(Panel.PREFERENCE_NAME, Panel.MODE);
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
}