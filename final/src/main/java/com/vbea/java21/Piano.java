package com.vbea.java21;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.MotionEvent;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.support.v7.app.AppCompatActivity;

import com.vbea.java21.classes.Util;
import com.vbea.java21.classes.Common;
import com.vbea.java21.classes.ExceptionHandler;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

public class Piano extends AppCompatActivity {
    private LinearLayout pianoLayout;
    private LayoutInflater mLayoutInflate;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        //setTheme(MyThemes.getTheme());
        super.onCreate(savedInstanceState);
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.piano);
        pianoLayout = findViewById(R.id.pianoLayout);
        mLayoutInflate = getLayoutInflater();
        pianoLayout.postDelayed(new Runnable() {
            public void run() {
                init();
            }
        }, 500);
    }

    public void init() {
        try {
            String json = getString(R.string.pianojson);
            JSONArray list = new JSONArray(json);
            if (list.length() > 0) {
                for (int i = 0; i < list.length(); i++) {
                    JSONObject keys = list.getJSONObject(i);
                    if (keys != null) {
                        int count = keys.getInt("count");
                        String key = keys.getString("key");
                        String tone = keys.getString("tone");
                        if (count > 0 && !Util.isNullOrEmpty(key)) {
                            //ExceptionHandler.log("piano", "count:"+count + ",key"+key+",tone"+tone);
                            switch (count) {
                                case 1:
                                    addViewBy1(key);
                                    break;
                                case 3:
                                    addViewBy3(key.split(","), tone);
                                    break;
                                case 5:
                                    addViewBy5(key.split(","), tone.split(","));
                                    break;
                                case 7:
                                    addViewBy7(key.split(","), tone.split(","));
                                    break;
                            }
                        }
                    }
                }
            }
        } catch (JSONException e) {
            ExceptionHandler.log("piano_json_error", e.toString());
        }
    }

    private void addViewBy1(String key) {
        View view;
        if (Common.PIANO_SIZE == 0) {
            view = mLayoutInflate.inflate(R.layout.piano_small1, pianoLayout, false);
        } else if (Common.PIANO_SIZE == 1) {
            view = mLayoutInflate.inflate(R.layout.piano_normal1, pianoLayout, false);
        } else {
            view = mLayoutInflate.inflate(R.layout.piano_large1, pianoLayout, false);
        }
        TextView btn1 = view.findViewById(R.id.pianoX1);
        btn1.setText(key);
        btn1.setOnTouchListener(new MyPlay(key));
        pianoLayout.addView(view);
    }

    private void addViewBy3(String[] key, String tone) {
        try {
            if (key.length == 2) {
                View view;
                if (Common.PIANO_SIZE == 0) {
                    view = mLayoutInflate.inflate(R.layout.piano_small3, pianoLayout, false);
                } else if (Common.PIANO_SIZE == 1) {
                    view = mLayoutInflate.inflate(R.layout.piano_normal3, pianoLayout, false);
                } else {
                    view = mLayoutInflate.inflate(R.layout.piano_large3, pianoLayout, false);
                }
                TextView btn1 = view.findViewById(R.id.piano3_1);
                TextView btn2 = view.findViewById(R.id.piano3_2);
                TextView btn3 = view.findViewById(R.id.piano3_c);
                btn1.setText(key[0]);
                btn1.setOnTouchListener(new MyPlay(key[0]));
                btn2.setText(key[1]);
                btn2.setOnTouchListener(new MyPlay(key[1]));
                btn3.setText(tone);
                btn3.setOnTouchListener(new MyPlay(tone));
                pianoLayout.addView(view);
            }
        } catch (Exception e) {
            ExceptionHandler.log("addview3", e.toString());
        }
    }

    private void addViewBy5(String[] key, String[] tone) {
        if (key.length == 3 && tone.length == 2) {
            View view;
            if (Common.PIANO_SIZE == 0) {
                view = mLayoutInflate.inflate(R.layout.piano_small5, pianoLayout, false);
            } else if (Common.PIANO_SIZE == 1) {
                view = mLayoutInflate.inflate(R.layout.piano_normal5, pianoLayout, false);
            } else {
                view = mLayoutInflate.inflate(R.layout.piano_large5, pianoLayout, false);
            }
            TextView btn1 = view.findViewById(R.id.piano5_1);
            TextView btn2 = view.findViewById(R.id.piano5_2);
            TextView btn3 = view.findViewById(R.id.piano5_3);
            TextView btn4 = view.findViewById(R.id.piano5_c1);
            TextView btn5 = view.findViewById(R.id.piano5_c2);
            btn1.setText(key[0]);
            btn1.setOnTouchListener(new MyPlay(key[0]));
            btn2.setText(key[1]);
            btn2.setOnTouchListener(new MyPlay(key[1]));
            btn3.setText(key[2]);
            btn3.setOnTouchListener(new MyPlay(key[2]));
            btn4.setText(tone[0]);
            btn4.setOnTouchListener(new MyPlay(tone[0]));
            btn5.setText(tone[1]);
            btn5.setOnTouchListener(new MyPlay(tone[1]));
            pianoLayout.addView(view);
        }
    }

    private void addViewBy7(String[] key, String[] tone) {
        if (key.length == 4 && tone.length == 3) {
            View view;
            if (Common.PIANO_SIZE == 0) {
                view = mLayoutInflate.inflate(R.layout.piano_small7, pianoLayout, false);
            } else if (Common.PIANO_SIZE == 1) {
                view = mLayoutInflate.inflate(R.layout.piano_normal7, pianoLayout, false);
            } else {
                view = mLayoutInflate.inflate(R.layout.piano_large7, pianoLayout, false);
            }
            TextView btn1 = view.findViewById(R.id.piano7_1);
            TextView btn2 = view.findViewById(R.id.piano7_2);
            TextView btn3 = view.findViewById(R.id.piano7_3);
            TextView btn4 = view.findViewById(R.id.piano7_4);
            TextView btn5 = view.findViewById(R.id.piano7_c1);
            TextView btn6 = view.findViewById(R.id.piano7_c2);
            TextView btn7 = view.findViewById(R.id.piano7_c3);
            btn1.setText(key[0]);
            btn1.setOnTouchListener(new MyPlay(key[0]));
            btn2.setText(key[1]);
            btn2.setOnTouchListener(new MyPlay(key[1]));
            btn3.setText(key[2]);
            btn3.setOnTouchListener(new MyPlay(key[2]));
            btn4.setText(key[3]);
            btn4.setOnTouchListener(new MyPlay(key[3]));
            btn5.setText(tone[0]);
            btn5.setOnTouchListener(new MyPlay(tone[0]));
            btn6.setText(tone[1]);
            btn6.setOnTouchListener(new MyPlay(tone[1]));
            btn7.setText(tone[2]);
            btn7.setOnTouchListener(new MyPlay(tone[2]));
            pianoLayout.addView(view);
        }
    }

    class MyPlay implements View.OnTouchListener {
        private String key;

        public MyPlay(String k) {
            key = k;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                try {
                    Common.SOUND.play(key);
                } catch (Exception e) {
                    ExceptionHandler.log("PianoTouched", e.getMessage());
                }
            }
            //if (event.getAction() == MotionEvent.ACTION_MOVE)
            //v.setFocusableInTouchMode(true);
            return false;
        }
    }

    @Override
    protected void onResume() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        super.onResume();
    }
}
