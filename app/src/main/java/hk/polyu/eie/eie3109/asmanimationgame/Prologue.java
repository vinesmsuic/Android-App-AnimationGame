package hk.polyu.eie.eie3109.asmanimationgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class Prologue extends AppCompatActivity {

    TextView msgPrologue;
    boolean stopDisplay = false;
    ImageView imgBackground;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prologue);

        // Hide ActionBar - https://www.codevoila.com/post/76/5-ways-to-hide-android-actionbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        setText("Chen played too much video games recently. " +
                "Now she even dreamed in RPG game. " +
                "Please help Chen to save the world. \n\n" +
                "P.S. All progress will be cleaned if there is GAMEOVER. \n\n" +
                "Press anywhere to continue");

        imgBackground = findViewById(R.id.imgBackground);
        imgBackground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopDisplay = true;
                Intent i = new Intent(getApplicationContext(),LevelSelect.class);
                startActivity(i);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        });
    }



    public void setText(final String s) {
        msgPrologue = findViewById(R.id.msgPrologue);
        final int[] i = new int[1];
        i[0] = 0;
        final int length = s.length();
        final Handler handler = new Handler()
        {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                char c= s.charAt(i[0]);
                msgPrologue.append(String.valueOf(c));
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
}