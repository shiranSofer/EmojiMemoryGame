package com.shira.emojimemorygame;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class LevelActivity extends AppCompatActivity {

    private Intent gameIntent;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_level);

        bundle = getIntent().getExtras();
        Button easyLevel = (Button)findViewById(R.id.button_easy);
        Button mediumLevel = (Button)findViewById(R.id.button_medium);
        Button hardLevel = (Button)findViewById(R.id.button_hard);

        ((TextView)findViewById(R.id.text_view_welcome_level_screen)).setText("Welcome " + bundle.getString("player_name"));
        ((TextView)findViewById(R.id.text_view_age_level_screen)).setText("Your age is:  " + bundle.getInt("player_age"));

        if(bundle.getBoolean("player_birthday"))
            findViewById(R.id.text_view_birthday_note_level_screen).setVisibility(View.VISIBLE);

        gameIntent = new Intent(LevelActivity.this, GameActivity.class);

        easyLevel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                gameIntent.putExtra("level",1);
                gameIntent.putExtra("player_name", bundle.getString("player_name"));
                startActivity(gameIntent);
            }
        });

        mediumLevel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameIntent.putExtra("level",2);
                gameIntent.putExtra("player_name", bundle.getString("player_name"));
                startActivity(gameIntent);
            }
        });

        hardLevel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gameIntent.putExtra("level",3);
                gameIntent.putExtra("player_name", bundle.getString("player_name"));
                startActivity(gameIntent);
            }
        });
    }

}
