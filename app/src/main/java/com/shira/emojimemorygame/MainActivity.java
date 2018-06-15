package com.shira.emojimemorygame;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
    private MapsFragment map;

    private RecordsFragment table;
    private RadioButton radioButton;
    private SharedPreferences sharedPreferences;
    private RadioGroup radioGroup;

    private DatabaseHelper dataBase;
    private static HashSet<Record> easyRecordsSet;
    private static HashSet<Record> mediumRecordsSet;
    private static HashSet<Record> hardRecordsSet;
    private static ArrayList<Record> easyRecords;
    private static ArrayList<Record> mediumRecords;
    private static ArrayList<Record> hardRecords;

    private ArrayList<Record> arrayList;

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
                Intent intent = new Intent(MainActivity.this, MapsFragment.class);
                startActivity(intent);
            }
        });

        createDataBase();
        storeRecords();
        openTable();
        startGame();


    }

    private void createDataBase(){
        dataBase = new DatabaseHelper(this);
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

    public  ArrayList<Record> viewRecords(){
        Cursor result = dataBase.getAllData();
        if(result.getCount() == 0){
            showMessage("Error", "no records");
            return null;
        }
        arrayList = new ArrayList<>();
        while (result.moveToNext()){
            arrayList.add(new Record(result.getString(1),
                    result.getInt(2), result.getString(3),
                            result.getInt(4)));
        }
        return arrayList;
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


    private void showMessage(String title, String Message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(Message);
        builder.show();
    }

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
