package hk.polyu.eie.eie3109.asmanimationgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Shop extends AppCompatActivity {

    private MediaPlayer soundPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        SharedPreferences sharedPreferences = getSharedPreferences(Panel.PREFERENCE_NAME, Panel.MODE);
        //Second param: Return that default value when there is no matching key.
        int health = sharedPreferences.getInt("Health", 100);
        int gold = sharedPreferences.getInt("Gold", 0);
        TextView txtGold = findViewById(R.id.txtGold);
        TextView txtHP = findViewById(R.id.txtHP);
        TextView txtSystem = findViewById(R.id.txtSystem);
        txtSystem.setText("");

        txtGold.setText("$: " + gold);
        txtHP.setText("HP: " + health);

        Button btnBuyHP = findViewById(R.id.buyHP);
        Button btnBuyDMG = findViewById(R.id.buyDMG);
        Button btnBuyMaxHP = findViewById(R.id.buyMaxHP);
        Button btnBack = findViewById(R.id.btnBack);

        // Hide ActionBar - https://www.codevoila.com/post/76/5-ways-to-hide-android-actionbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        btnBuyHP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int health = sharedPreferences.getInt("Health", 100);
                int gold = sharedPreferences.getInt("Gold", 0);
                int maxHealth = sharedPreferences.getInt("MaxHealth", 100);

                playSound();

                if(gold - 10 >= 0)
                {
                    if(health == maxHealth)
                    {
                        //Toast.makeText(Shop.this, "Your HP is full Already!", Toast.LENGTH_SHORT).show();
                        txtSystem.setText("Your HP is full Already!");
                    }
                    else
                    {
                        health += 10;
                        if(health > maxHealth){
                            health = maxHealth;

                        }
                        gold -= 10;

                        txtGold.setText("$: " + gold);
                        txtHP.setText("HP: " + health);

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putInt("Health", health);
                        editor.putInt("Gold", gold);
                        editor.apply();

                        txtSystem.setText("HP is now " + health);
                        //Toast.makeText(Shop.this, "HP is now " + health, Toast.LENGTH_SHORT).show();
                    }

                }
                else
                {
                    txtSystem.setText("You don't have enough gold! \nYour current HP is: " + health);
                    //Toast.makeText(Shop.this, "You don't have enough gold! \nYour current HP is: " + health, Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnBuyMaxHP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int maxHealth = sharedPreferences.getInt("MaxHealth", 100);
                int gold = sharedPreferences.getInt("Gold", 0);

                playSound();

                if(gold - 30 >= 0)
                {
                    maxHealth += 10;
                    gold -= 30;

                    txtGold.setText("$: " + gold);
                    txtHP.setText("HP: " + maxHealth);

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("MaxHealth", maxHealth);
                    editor.putInt("Gold", gold);
                    editor.apply();

                    txtSystem.setText("MaxHP is now " + maxHealth);
                    //Toast.makeText(Shop.this, "MaxHP is now " + maxHealth, Toast.LENGTH_SHORT).show();
                }
                else
                {
                    txtSystem.setText("You don't have enough gold! \nYour current MaxHP is: " + maxHealth);
                    //Toast.makeText(Shop.this, "You don't have enough gold! \nYour current MaxHP is: " + maxHealth, Toast.LENGTH_SHORT).show();
                }

            }
        });

        btnBuyDMG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int attack = sharedPreferences.getInt("Attack", 1);
                int gold = sharedPreferences.getInt("Gold", 0);

                playSound();

                if(gold -50 >= 0)
                {
                    attack += 1;
                    gold -= 50;

                    txtGold.setText("$: " + gold);

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("Attack", attack);
                    editor.putInt("Gold", gold);
                    editor.apply();

                    txtSystem.setText("DMG is now " + attack);
                    //Toast.makeText(Shop.this, "DMG is now " + attack, Toast.LENGTH_SHORT).show();
                }
                else
                {
                    txtSystem.setText("You don't have enough gold! \nYour current attack is: " + attack);
                    //Toast.makeText(Shop.this, "You don't have enough gold! \nYour current attack is: " + attack, Toast.LENGTH_SHORT).show();
                }

            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),LevelSelect.class);
                startActivity(i);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(getApplicationContext(),LevelSelect.class);
        startActivity(i);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        finish();
    }

    public void playSound(){
        if(soundPlayer == null)
        {
            soundPlayer = MediaPlayer.create(this, R.raw.coin);
            soundPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    if(soundPlayer != null) {
                        soundPlayer.release();
                        soundPlayer = null;
                    }
                }
            });
        }
        soundPlayer.start();
    }

}