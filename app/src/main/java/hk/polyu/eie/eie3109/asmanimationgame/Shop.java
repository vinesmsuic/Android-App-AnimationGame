package hk.polyu.eie.eie3109.asmanimationgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Shop extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        SharedPreferences sharedPreferences = getSharedPreferences(Panel.PREFERENCE_NAME, Panel.MODE);
        //Second param: Return that default value when there is no matching key.
        int level = sharedPreferences.getInt("Level", 1);
        int maxHealth = sharedPreferences.getInt("MaxHealth", 100);
        int exp = sharedPreferences.getInt("Exp", 0);
        int gold = sharedPreferences.getInt("Gold", 0);

        TextView txtGold = findViewById(R.id.txtGold);
        TextView txtHP = findViewById(R.id.txtHP);

        txtGold.setText("$: " + gold);
        txtHP.setText("HP: " + maxHealth);

        Button btnBuyHP = findViewById(R.id.buyHP);
        Button btnBuyDMG = findViewById(R.id.buyDMG);
        Button btnBuyMaxHP = findViewById(R.id.buyMaxHP);

        btnBuyHP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int health = sharedPreferences.getInt("Health", 100);
                health += 10;
                if(health > maxHealth){
                    health = maxHealth;

                }
                Panel.player.setHealth(health);
                savePreferences();
            }
        });

        btnBuyMaxHP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int maxHealth = sharedPreferences.getInt("MaxHealth", 100);
                maxHealth += 10;
                Panel.player.setMaxHealth(maxHealth);
                savePreferences();
            }
        });

        btnBuyDMG.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int attack = sharedPreferences.getInt("Attack", 1);
                attack += 1;
                Panel.player.setAttack(attack);
                savePreferences();
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

    private void savePreferences(){
        //Open the sharedPreferences editor and save
        SharedPreferences sharedPreferences = getSharedPreferences(Panel.PREFERENCE_NAME, Panel.MODE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        //Key value pairs
        editor.putInt("Level", Panel.player.getLevel());
        editor.putInt("Health", Panel.player.getHealth());
        editor.putInt("MaxHealth", Panel.player.getMaxHealth());
        editor.putInt("Attack", Panel.player.getAttack());
        editor.putInt("Exp", Panel.player.getExp());
        editor.putInt("Gold", Panel.player.getGold());
        editor.apply();
    }
}