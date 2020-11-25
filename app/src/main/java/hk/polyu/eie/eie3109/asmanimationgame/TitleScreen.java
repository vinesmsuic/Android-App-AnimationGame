package hk.polyu.eie.eie3109.asmanimationgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ConfigurationInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

public class TitleScreen extends AppCompatActivity {

    TextView txtStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_title_screen);

        // Hide ActionBar - https://www.codevoila.com/post/76/5-ways-to-hide-android-actionbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        txtStart = findViewById(R.id.txtStart);

        shineColor();

        ImageButton btnStart = findViewById(R.id.btnStart);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(i);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        startService(new Intent(this, MusicService.class));
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopService(new Intent(this, MusicService.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    public void shineColor(){ //Color change time by time
        Timer t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {
                                  int count = 0;

                                  @Override
                                  public void run() {
                                      runOnUiThread(new Runnable() {
                                          @Override
                                          public void run() {
                                              switch (count % 3) {
                                                  case 0:
                                                      txtStart.setTextColor(getResources().getColor(R.color.LightSlateGray));
                                                      break;
                                                  case 1:
                                                      txtStart.setTextColor(getResources().getColor(R.color.DarkGray));
                                                      break;
                                                  case 2:
                                                      txtStart.setTextColor(getResources().getColor(R.color.GhostWhite));
                                                      break;
                                              }
                                              count++;
                                          }
                                      });
                                  }
                              },
                0, 300);
    }
}