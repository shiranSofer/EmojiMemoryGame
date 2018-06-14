package com.shira.emojimemorygame;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class EndActivity extends AppCompatActivity {

    private RecordsFragment table;
    private MainActivity mainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_end);
        findViewById(R.id.end_record_table).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainActivity = new MainActivity();
                mainActivity.storeRecords();
                openTable();
            }
        });
    }



    public void openTable(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        table = new RecordsFragment();
        fragmentTransaction.add(R.id.fragment_container, table);
        fragmentTransaction.commit();

    }
}
