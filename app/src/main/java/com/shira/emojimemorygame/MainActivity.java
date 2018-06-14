package com.shira.emojimemorygame;

import android.app.DatePickerDialog;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashSet;

public class MainActivity extends AppCompatActivity {

    private final static int EASY = 0;
    private final static int MEDIUM = 1;
    private final static int HARD = 2;
    private final int COL_ID = 0;
    private final int COL_NAME = 1;
    private final int COL_TIME = 2;
    private final int COL_ADDRESS = 3;
    private int gameLevel;
    private DatePickerDialog.OnDateSetListener datePicker;
    private Calendar myCalendar;

    private RecordsFragment table;
    private RadioButton radioButton;
    private SharedPreferences sharedPreferences;
    private RadioGroup radioGroup;

    private DatabaseHalper dataBase;
    private static HashSet<Record> easyRecordsSet;
    private static HashSet<Record> mediumRecordsSet;
    private static HashSet<Record> hardRecordsSet;
    private static ArrayList<Record> easyRecords;
    private static ArrayList<Record> mediumRecords;
    private static ArrayList<Record> hardRecords;

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



        findViewById(R.id.button_map).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mapIntent = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(mapIntent);
            }
        });

        createDataBase();
        storeRecords();
        openTable();
        startGame();


    }

    private void createDataBase(){
        dataBase = new DatabaseHalper(this);
    }

    public void storeRecords(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                easyRecordsSet = new HashSet<>();
                Cursor cursor = dataBase.getAllDataByDesc(EASY);

                while (cursor.getCount() > 0){

                    easyRecordsSet.add(new Record(cursor.getString(COL_NAME), cursor.getInt(COL_TIME),
                            cursor.getString(COL_ADDRESS), cursor.getInt(COL_ID)));
                }
                easyRecords = new ArrayList<>(easyRecordsSet);
                Collections.sort(easyRecords);
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                mediumRecordsSet = new HashSet<>();
                Cursor cursor = dataBase.getAllDataByAsc(MEDIUM);
                while (cursor.moveToNext()){
                    mediumRecordsSet.add(new Record(cursor.getString(COL_NAME), cursor.getInt(COL_TIME),
                            cursor.getString(COL_ADDRESS), cursor.getInt(COL_ID)));
                }
                mediumRecords = new ArrayList<>(mediumRecordsSet);
                Collections.sort(mediumRecords);
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                hardRecordsSet = new HashSet<>();
                Cursor cursor = dataBase.getAllDataByAsc(HARD);
                while (cursor.moveToNext()){
                    hardRecordsSet.add(new Record(cursor.getString(COL_NAME), cursor.getInt(COL_TIME),
                            cursor.getString(COL_ADDRESS), cursor.getInt(COL_ID)));
                }
                hardRecords = new ArrayList<>(hardRecordsSet);
                Collections.sort(hardRecords);
            }
        }).start();
    }
    private void startGame(){
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

   /* private void startRadioButtons() {
        int i = sharedPreferences.getInt(getString(R.string.button_pressed), 0); // saving in i the last selected difficulty
        if (i == EASY) { // compare i to each difficulty to find its value and bring the right radio button
            radioButton = (RadioButton) findViewById(R.id.table_radio_button_easy);
            gameLevel = EASY;
        } else if (MEDIUM == i) {
            radioButton = (RadioButton) findViewById(R.id.table_radio_button_medium);
            gameLevel = MEDIUM;
        } else {
            radioButton = (RadioButton) findViewById(R.id.table_radio_button_hard);
            gameLevel = HARD;
        }
        radioButton.setChecked(true); // check the radio button last picked by the user
        *//*radioGroup = (RadioGroup) findViewById(R.id.rg_level); //we are making a new radio group

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) { // we are listening to the radio group on changing radio bottons
                radioButton = (RadioButton) group.findViewById(checkedId);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                switch (radioButton.getId()) {
                    case R.id.table_radio_button_easy:
                        editor.putInt(getString(R.string.button_pressed), EASY);
                        gameLevel = EASY;
                        break;

                    case R.id.table_radio_button_medium:
                        editor.putInt(getString(R.string.button_pressed), MEDIUM);
                        gameLevel = MEDIUM;
                        break;

                    case R.id.table_radio_button_hard:
                        editor.putInt(getString(R.string.button_pressed), HARD);
                        gameLevel = HARD;
                        break;
                }
                editor.apply();
            }
        });*//*
    }*/

    public static ArrayList<Record> getEasyRecords() {
        return easyRecords;
    }

    public static ArrayList<Record> getMediumRecords() {
        return mediumRecords;
    }

    public static ArrayList<Record> getHardRecords() {
        return hardRecords;
    }

    public void openTable(){
        findViewById(R.id.button_record_table).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                table = new RecordsFragment();
                fragmentTransaction.add(R.id.fragment_container, table);
                fragmentTransaction.commit();

            }

        });
    }
}
