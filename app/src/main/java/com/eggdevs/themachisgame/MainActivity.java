package com.eggdevs.themachisgame;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Layout;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

import static java.lang.Thread.sleep;

public class MainActivity extends AppCompatActivity {

    TextView tvMatches, tvExpert, tvUser, tvTitle;
    Button btnRules, btnPick;
    EditText etPick;
    String [] expertTalk = {
            "I pick ", "That's my move, ", "Here goes nothing, ", "Hold your breath, ", "Fingers crossed, "
    };

    String [] expertThink = {
            "Let me think... ", "You got that right... ", "Great move... ", "Wait for my turn... ", "Awesome dude... "
    };

    Random random, random1;

    int matchsticks = 21, expertPick;

    LayoutInflater inflater;
    AlertDialog alertDialog;
    AlertDialog.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvMatches = findViewById(R.id.tvMatches);
        tvExpert = findViewById(R.id.tvExpert);
        tvUser = findViewById(R.id.tvUser);
        btnPick = findViewById(R.id.btnPick);
        btnRules = findViewById(R.id.btnRules);
        etPick = findViewById(R.id.etPick);
        tvTitle = findViewById(R.id.tvTitle);


        showRules();

        btnRules.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showRules();
            }
        });

            btnPick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                        random = new Random();
                        random1 = new Random();

                        int userPick = Integer.parseInt(etPick.getText().toString().trim());
                    closeKeyboard();
                    tvUser.setText("" + userPick + " stick(s).");
                    tvExpert.setText("");
                    etPick.setText("");
                    matchsticks = matchsticks - userPick;
                    tvMatches.setText("" + matchsticks);
                    expertPick = 5 - userPick;

                    if (matchsticks < 6) {
                        etPick.setEnabled(false);
                    }

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            int randomIndex1 = random1.nextInt(5);
                            tvExpert.setText(expertThink[randomIndex1]);
                        }
                    },500);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            int randomIndex = random.nextInt(5);
                            tvExpert.setText(expertTalk[randomIndex] + " " +
                                    expertPick + " stick(s).");
                            Toast.makeText(MainActivity.this, "Your Turn.", Toast.LENGTH_SHORT).show();
                            tvUser.setText("");
                        }
                    }, 1000);

                    matchsticks = matchsticks - expertPick;

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            tvMatches.setText("" + matchsticks);
                        }
                    },1000);

                        gameOver();
                    }
            });

            etPick.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                    String userNum = etPick.getText().toString().trim();

                    btnPick.setEnabled(!userNum.isEmpty() &&
                            (Integer.parseInt(etPick.getText().toString().trim()) < 5 &&
                                    Integer.parseInt(etPick.getText().toString().trim()) > 0));
                    if (btnPick.isEnabled()) {

                        btnPick.setTextColor(Color.parseColor("#36770A"));
                    }
                    else {
                        btnPick.setTextColor(Color.parseColor("#ffffff"));
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        etPick.setFilters(new InputFilter[] {new InputFilter.LengthFilter(2)});
//        tvTitle.setShadowLayer(1.6f,1.5f,1.3f,Color.
//                parseColor("#ffffff"));
    }

    private void gameOver() {

            if (matchsticks == 1) {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {

                        tvExpert.setText("You are forced to pick up the last matchstick.");
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent(MainActivity.this, YouLose.class));
                                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                                finish();
                            }
                        }, 2000);
                    }
                },1500);
            }
    }

    public void showRules() {

        builder = new AlertDialog.Builder(MainActivity.this);
        inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.rules_dialog, null);
        Button btnPlay = view.findViewById(R.id.btnPlay);
        builder.setView(view);
        alertDialog = builder.create();
        alertDialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alertDialog.setCancelable(false);
        alertDialog.show();

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

    public void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
