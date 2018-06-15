package com.shira.emojimemorygame;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RecordsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RecordsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecordsFragment extends DialogFragment {

    private Button button;
    private Button showButton;
    private MainActivity mainActivity;
    private boolean wait = true;

    // views values
    private final int TEXT_SIZE = 11;
    private final int PADDING_WIDTH = ((Resources.getSystem().getDisplayMetrics().widthPixels) - 80);
    private final int PADDING_HIGHT = 8;
    private final int LEFT = 1;
    private final int ADDRESS = 2;
    private final int RIGHT = 4;

    // data text values
    private static int idCounter = 0;

    private final int EASY = 0;
    private final int MEDIUM = 1;
    private final int HARD = 2;

    // table fragment members
    private int currentLevel;
    private RadioButton defaultButton;
    private View fragmentView;
    private TableLayout tableLayout;

    private TextView tvAddress;
    private int i;


    public RecordsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentView = inflater.inflate(R.layout.fragment_records, container, false);
        tableLayout = fragmentView.findViewById(R.id.table_scores);
        //tableLayout.setVisibility(View.VISIBLE);
        button = fragmentView.findViewById(R.id.button_exit);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(wait) {
                    dismiss();
                }
            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    wait = false;
                    Thread.sleep(2000);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            defaultButton.setChecked(true);
                            fragmentView.findViewById(R.id.progress_bar_table).setVisibility(View.GONE);
                            tableLayout.setVisibility(View.VISIBLE);
                            wait = true;
                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();


        defaultButton = fragmentView.findViewById(R.id.table_radio_button_easy);

        RadioGroup tableGroup = fragmentView.findViewById(R.id.radio_group_level_Table); //we are making a new radio group
        tableGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group , int checkedId) { // we are listening to the radio group on changing radio bottons
                defaultButton = group.findViewById(checkedId);

                switch (defaultButton.getId()) {
                    case R.id.table_radio_button_easy:
                        currentLevel = EASY;
                        break;

                    case R.id.table_radio_button_medium:
                        currentLevel = MEDIUM;
                        break;

                    case R.id.table_radio_button_hard:
                        currentLevel = HARD;
                        break;
                }
                tableLayout.removeAllViewsInLayout();
                mainActivity.viewRecords();
                initTable();
            }
        });
        // init table view
        return fragmentView;
    }

    private void setTextView(TextView tv, int id , int position)
    {
        tv.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        tv.setId(id);
        if(position == LEFT){
            tv.setPadding(40, PADDING_HIGHT, PADDING_WIDTH - tv.getText().length(), PADDING_HIGHT);
        }else if(position == RIGHT){
            tv.setPadding(0, PADDING_HIGHT, 40, PADDING_HIGHT);
        }else if(position == ADDRESS){
            tv.setPadding(40, PADDING_HIGHT, PADDING_WIDTH - tv.getText().length()
                    , PADDING_HIGHT);
        }else{
            tv.setPadding(0 , PADDING_HIGHT, PADDING_WIDTH - tv.getText().length(), PADDING_HIGHT);
        }

        tv.setTextSize(TEXT_SIZE);
        tv.setTextColor(Color.BLACK);
    }

    private void initTable() {
        int placementCounter = 0;
        ArrayList<Record> records = new ArrayList();
        //records.add(new Record("x", 10, "y", 1));
        //records.add(new Record("xx", 100, "y", 1));
        // add rows in table - if exist in data base
        for (i = 0; i < records.size(); i++) {
            Record record = records.get(i);

            TableRow tbrow = new TableRow(getActivity().getApplicationContext());
            tbrow.setLayoutParams(new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));

            TextView tvPlacement = new TextView(getActivity().getApplicationContext());
            TextView tvName = new TextView(getActivity().getApplicationContext());
            tvAddress = new TextView(getActivity().getApplicationContext());
            TextView tvScore = new TextView(getActivity().getApplicationContext());

            tvPlacement.setText(++placementCounter + "");
            tvName.setText(record.getPlayerName());
            tvScore.setText(record.getTime() + "");
            tvAddress.setText(record.getAddress().substring(record.getAddress().lastIndexOf(',') + 1, record.getAddress().length()));

            setTextView(tvPlacement, idCounter++, LEFT);
            setTextView(tvScore, idCounter++, 0);
            setTextView(tvAddress, idCounter++, ADDRESS);
            setTextView(tvName, idCounter++, RIGHT);

            tbrow.addView(tvPlacement);
            tbrow.addView(tvName);
            tbrow.addView(tvAddress);
            tbrow.addView(tvScore);
            // add row to table
            tableLayout.addView(tbrow, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
        }
    }

    private ArrayList<Record> getRecordsByDifficulty(){
        if(currentLevel == EASY){
            return MainActivity.getEasyRecords();
        }else if(currentLevel == MEDIUM){
            return MainActivity.getMediumRecords();
        }else{
            return MainActivity.getHardRecords();
        }
    }

/*
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    *//**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     *//*
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }*/
}
