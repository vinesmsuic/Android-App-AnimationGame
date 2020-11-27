package hk.polyu.eie.eie3109.asmanimationgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class LevelSelect extends AppCompatActivity {

    ImageButton btnShop;
    ImageButton btnLevel1;
    ImageButton btnLevel2;
    ImageButton btnLevel3;
    Button btnEnd;
    TextView txtLevel1;
    TextView txtLevel2;
    TextView txtLevel3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level_select);

        SharedPreferences sharedPreferences = getSharedPreferences(Panel.PREFERENCE_NAME, Panel.MODE);
        int gameLevel1Cleared = sharedPreferences.getInt("gameLevel1Cleared", 0);
        int gameLevel2Cleared = sharedPreferences.getInt("gameLevel2Cleared", 0);
        int gameLevel3Cleared = sharedPreferences.getInt("gameLevel3Cleared", 0);


        btnShop = findViewById(R.id.btnShop);
        btnLevel1 = findViewById(R.id.btnLevel1);
        btnLevel2 = findViewById(R.id.btnLevel2);
        btnLevel3 = findViewById(R.id.btnLevel3);
        btnEnd = findViewById(R.id.btnEnd);
        txtLevel1 = findViewById(R.id.txtLevel1);
        txtLevel2 = findViewById(R.id.txtLevel2);
        txtLevel3 = findViewById(R.id.txtLevel3);

        txtLevel2.setVisibility(View.INVISIBLE);
        txtLevel3.setVisibility(View.INVISIBLE);
        btnLevel2.setVisibility(View.INVISIBLE);
        btnLevel3.setVisibility(View.INVISIBLE);
        btnEnd.setVisibility(View.INVISIBLE);

        // Hide ActionBar - https://www.codevoila.com/post/76/5-ways-to-hide-android-actionbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        if(gameLevel1Cleared > 0)
        {
            txtLevel2.setVisibility(View.VISIBLE);
            btnLevel2.setVisibility(View.VISIBLE);

            if(gameLevel2Cleared > 0)
            {
                txtLevel3.setVisibility(View.VISIBLE);
                btnLevel3.setVisibility(View.VISIBLE);

                if(gameLevel1Cleared > 0 && gameLevel2Cleared > 0 && gameLevel3Cleared > 0)
                {
                    btnEnd.setVisibility(View.VISIBLE);
                }
            }
        }


        btnShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),Shop.class);
                startActivity(i);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        });


        btnLevel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),MainActivity.class);
                i.putExtra("gameLevel",1);
                startActivity(i);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        });


        btnLevel2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),MainActivity.class);
                i.putExtra("gameLevel",2);
                startActivity(i);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        });


        btnLevel3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),MainActivity.class);
                i.putExtra("gameLevel",3);
                startActivity(i);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        });

        btnEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),Ending.class);
                startActivity(i);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        });
    }


}