package org.janssen.scoreboard;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import org.janssen.scoreboard.task.FoulTask;

public class FoulDialog extends Dialog implements android.view.View.OnClickListener {

    private String authToken;
    private int teamId;
    private boolean isPositive;

    FoulDialog(final String authToken,
               final int teamId,
               final boolean isPositive,
               final Activity activity) {
        super(activity);
        this.authToken = authToken;
        this.teamId = teamId;
        this.isPositive = isPositive;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // requestWindowFeature(Window.FEATURE_NO_TITLE);
        setTitle(R.string.personal_fouls);
        setContentView(R.layout.foul_dialog);
        Button one = findViewById(R.id.one);
        Button two = findViewById(R.id.two);
        Button three = findViewById(R.id.three);
        Button four = findViewById(R.id.four);
        Button five = findViewById(R.id.five);
        Button cancel = findViewById(R.id.cancel);

        one.setOnClickListener(this);
        two.setOnClickListener(this);
        three.setOnClickListener(this);
        four.setOnClickListener(this);
        five.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.one:
                new FoulTask(authToken, teamId, 1, isPositive).execute();
                break;
            case R.id.two:
                new FoulTask(authToken, teamId, 2, isPositive).execute();
                break;
            case R.id.three:
                new FoulTask(authToken, teamId, 3, isPositive).execute();
                break;
            case R.id.four:
                new FoulTask(authToken, teamId, 4, isPositive).execute();
                break;
            case R.id.five:
                new FoulTask(authToken, teamId, 5, isPositive).execute();
                break;
            case R.id.cancel:
                break;
        }
        dismiss();
    }
}
