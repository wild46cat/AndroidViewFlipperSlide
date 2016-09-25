package com.example.wuxueyou.viewflipperslide;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.ViewFlipper;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    private final String TAG = MainActivity.class.getSimpleName();
    private final int TIME_INTERVAL = 5000;
    private ViewFlipper flipper1;
    private ViewFlipper flipper2;
    private TextView textView;
    private List<View> dots;

    private int oldX;
    private int newX;
    private boolean flag;
    private long currentNumber;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            flipper1.showNext();
            if (flag) {
                flipper2.setInAnimation(MainActivity.this,R.anim.slide_in_right);
                flipper2.setOutAnimation(MainActivity.this,R.anim.slide_out_left);
                flipper2.showNext();
                currentNumber++;
                setNumberText(currentNumber);
            }
            handler.sendMessageDelayed(new Message(), TIME_INTERVAL);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        flipper1 = (ViewFlipper) this.findViewById(R.id.flipper1);
        flipper2 = (ViewFlipper) this.findViewById(R.id.flipper2);
        textView = (TextView) this.findViewById(R.id.textview);
        dots = new ArrayList<View>();
        dots.add(this.findViewById(R.id.dot_1));
        dots.add(this.findViewById(R.id.dot_2));
        dots.add(this.findViewById(R.id.dot_3));
        dots.add(this.findViewById(R.id.dot_4));

        oldX = 0;
        newX = 0;
        flag = true;
        currentNumber = flipper2.getChildCount()*10000000L;
        setNumberText(currentNumber);

        handler.sendMessageDelayed(new Message(), TIME_INTERVAL);

        flipper2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        flag = false;
                        oldX = (int) event.getX();
                        break;
                    case MotionEvent.ACTION_UP:
                        flag = true;
                        newX = (int) event.getX();
                        if (oldX > newX) {
                            flipper2.setInAnimation(MainActivity.this,R.anim.slide_in_right);
                            flipper2.setOutAnimation(MainActivity.this,R.anim.slide_out_left);
                            flipper2.showNext();
                            currentNumber++;
                            setNumberText(currentNumber);
                        } else if (oldX < newX) {
                            flipper2.setInAnimation(MainActivity.this,R.anim.slide_in_left);
                            flipper2.setOutAnimation(MainActivity.this,R.anim.slide_out_right);
                            flipper2.showPrevious();
                            currentNumber--;
                            setNumberText(currentNumber);
                        } else if (oldX == newX) {
                        }
                        break;
                }
                return true;
            }
        });
    }

    public void setNumberText(long currentNumber) {
        int current = (int)Math.abs(currentNumber % flipper2.getChildCount());
        textView.setText(String.valueOf(current));
        for (int i = 0; i < dots.size(); i++) {
            dots.get(i).setBackgroundResource(R.drawable.dot_normal);
        }
        dots.get(current).setBackgroundResource(R.drawable.dot_focused);
    }
}
