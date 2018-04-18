package com.shira.emojimemorygame;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private DatePickerDialog.OnDateSetListener datePicker;
    private Calendar myCalendar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myCalendar = Calendar.getInstance();
        datePicker = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                ((Button)findViewById(R.id.button_player_age)).setText(myCalendar.get(Calendar.DAY_OF_MONTH) + "/"
                        + myCalendar.get(Calendar.MONTH) +"/" + myCalendar.get(Calendar.YEAR));
            }

        };

        findViewById(R.id.button_player_age).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(MainActivity.this, datePicker, myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        findViewById(R.id.button_enter_main_screen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String playerName = ((EditText)findViewById(R.id.edit_text_player_name)).getText().toString();

                final int year = myCalendar.get(Calendar.YEAR);
                final int month = myCalendar.get(Calendar.MONTH) + 1;
                final int day = myCalendar.get(Calendar.DAY_OF_MONTH);

                Calendar calendar = Calendar.getInstance();
                int currentMonth = calendar.get(Calendar.MONTH) + 1;
                int currentDay = calendar.get(Calendar.DAY_OF_MONTH);
                int currentYear = calendar.get(Calendar.YEAR);
                int playerAge = currentYear - year;
                Boolean itsHisBirthday = false;

                if(month == currentMonth && day == currentDay)
                    itsHisBirthday = true;

                Intent playerInfoIntent = new Intent(MainActivity.this, LevelActivity.class);
                playerInfoIntent.putExtra("player_name", playerName);
                playerInfoIntent.putExtra("player_age", playerAge);
                playerInfoIntent.putExtra("player_birthday", itsHisBirthday);
                startActivity(playerInfoIntent);
            }
        });
    }
}
