package com.randezvouapps.toeflspeaking;

/**
 * Created by sachira on 5/21/17.
 *
 * This is SINGLETON class is used to run the test. It is an extension of TimerTask
 * It displays the questions and counts down 15s preparation time and 45s
 * speaking time
 */

import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Handler;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Arrays;
import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Exam extends TimerTask{
    private static final Exam INSTANCE = new Exam();

    public boolean          isStopped;

    private boolean         en_beep;

    private String[]        mQuestions;
    private boolean[]       mDone;
    private Random          m_rand;
    private Date            mTime;
    private int             m_millis;
    private int             m_Elapsed;
    private int             mCount;

    private Handler         mHandler;
    private TextView        mTimerView;
    private ToneGenerator   mToneGen;

    private enum STATE {
        STATE_NOP,
        STATE_IDLE,
        STATE_PREPARE,
        STATE_SPEAK
    }

    STATE                   mSTATE;
    STATE                   mPreState;

    private Exam () {
        super();
        this.mSTATE     = STATE.STATE_IDLE;
        this.mTime      = new Date();
        this.m_rand     = new Random();
        this.m_millis   = 0;
        this.isStopped  = true;
        this.en_beep    = true;
        this.mCount     = 0;    // Number of questions displayed so far

        mToneGen        = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
    }

    public static Exam getInstance () {
        return INSTANCE;
    }

    public void loadQuestions (String[] question_list) {
        this.mQuestions = question_list;
        this.mDone      = new boolean[question_list.length];
    }

    public void setHandler (Handler handler) {
        this.mHandler = handler;
    }

    public void setTextView (TextView textview) {
        this.mTimerView = textview;
    }

    public void setBeepEnable (boolean enable) {
        this.en_beep = enable;
    }

    public String showQuestion () {
        if (mCount == mQuestions.length) {
            // All the questions have been shown
            // Clear mDone and repeat questions
            mCount = 0;
            Arrays.fill(mDone, false);
        }

        int q_num       = m_rand.nextInt(mQuestions.length);
        while (mDone[q_num])
            q_num       = m_rand.nextInt(mQuestions.length);

        mCount          += 1;
        mDone[q_num]    = true;
        mSTATE          = STATE.STATE_PREPARE;
        mTime           = new Date();
        isStopped       = false;
        return mQuestions[q_num];
    }

    public void stop () {
        mPreState       = mSTATE;
        mSTATE          = STATE.STATE_IDLE;
        isStopped       = true;
    }

    public void start () {
        mSTATE          = mPreState;
        isStopped       = false;
    }

    @Override
    public void run () {

        m_millis = (int)((new Date()).getTime() - mTime.getTime());

        switch (mSTATE) {
            case STATE_PREPARE:
                m_Elapsed = 15 - (m_millis / 1000);

                if (m_Elapsed <= 0) {
                    mSTATE = STATE.STATE_SPEAK;
                    beep();
                }
                break;
            case STATE_SPEAK:
                m_Elapsed = 60 - (m_millis / 1000);

                if (m_Elapsed <= 0) {
                    mSTATE = STATE.STATE_IDLE;
                    beep();
                }
                break;
            case STATE_IDLE:
                mTime = new Date();
                break;
            default:
                m_Elapsed = 0;
        }

        // Update UI
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                String text = "";
                if (mSTATE == STATE.STATE_PREPARE)
                    text = String.format("PREPARE\n%d", m_Elapsed);
                else if (mSTATE == STATE.STATE_SPEAK)
                    text = String.format("SPEAK\n%d", m_Elapsed);
                else if (mSTATE == STATE.STATE_IDLE)
                    text = String.format("Time Up\n%d", m_Elapsed);

                mTimerView.setText(text);
            }
        });
    }

    private void beep () {
        if (!this.en_beep)
            return;

        mToneGen.startTone(ToneGenerator.TONE_DTMF_C, 300);
    }
}
