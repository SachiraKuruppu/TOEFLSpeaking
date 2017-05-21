package com.randezvouapps.toeflspeaking;

import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.Timer;

public class MainActivity extends AppCompatActivity {

    /**********************************************************
     * Global Variables & UI elements                         *
     **********************************************************/
    final Handler mHandler  = new Handler();
    final Timer mExamTimer  = new Timer();

    private boolean mBeepEn;
    private TextView mQuestionView;
    private TextView mTimerView;
    private Exam mExam;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*************************************************************
         * Load Resources                                            *
         *************************************************************/
        Resources res = getResources();
        String[] question_list = res.getStringArray(R.array.question_list);

        /*************************************************************
         *  Load UI elements                                         *
         *************************************************************/
        mQuestionView           = (TextView) findViewById(R.id.question_tv);
        mTimerView              = (TextView) findViewById(R.id.timer_tv);
        Button mNext_btn        = (Button) findViewById(R.id.next_btn);

        /*************************************************************
         *  Setup Events                                             *
         *************************************************************/
        mNext_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mQuestionView.setText( mExam.showQuestion() );
            }
        });

        /*************************************************************
         * Implementation                                            *
         *************************************************************/
        mBeepEn = false;

        mExam = Exam.getInstance();
        mExam.loadQuestions(question_list);
        mExam.setHandler(mHandler);
        mExam.setTextView(mTimerView);
        mExam.setBeepEnable(false);

        mExamTimer.schedule(mExam, 0, 100);

        mQuestionView.setText( mExam.showQuestion() );
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_beep_tgl:
                if (mBeepEn) {
                    // Beep enabled. Disable it and set disable icon
                    item.setIcon(android.R.drawable.ic_lock_silent_mode);
                    mExam.setBeepEnable(false);
                    mBeepEn = false;
                } else {
                    // Beep disabled. Enable it and set enable icon
                    item.setIcon(android.R.drawable.ic_lock_silent_mode_off);
                    mExam.setBeepEnable(true);
                    mBeepEn = true;
                }
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy () {
        super.onDestroy();
        mExamTimer.cancel();
    }
}
