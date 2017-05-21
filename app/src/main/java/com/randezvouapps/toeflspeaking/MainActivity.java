package com.randezvouapps.toeflspeaking;

import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Timer;

public class MainActivity extends AppCompatActivity {

    /**********************************************************
     * Global Variables & UI elements                         *
     **********************************************************/
    final Handler mHandler  = new Handler();
    final Timer mExamTimer  = new Timer();

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
        Button mStartStop_btn   = (Button) findViewById(R.id.start_stop_btn);
        Button mNext_btn        = (Button) findViewById(R.id.next_btn);

        /*************************************************************
         *  Setup Events                                             *
         *************************************************************/
        mStartStop_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick (View v) {
                if (mExam.isStopped)
                    mExam.start();
                else
                    mExam.stop();
            }
        });

        mNext_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mQuestionView.setText( mExam.showQuestion() );
            }
        });

        /*************************************************************
         * Implementation                                            *
         *************************************************************/

        mExam = Exam.getInstance();
        mExam.loadQuestions(question_list);
        mExam.setHandler(mHandler);
        mExam.setTextView(mTimerView);

        mExamTimer.schedule(mExam, 0, 100);

        mQuestionView.setText( mExam.showQuestion() );
    }

    @Override
    public void onDestroy () {
        super.onDestroy();
        mExamTimer.cancel();
    }
}
