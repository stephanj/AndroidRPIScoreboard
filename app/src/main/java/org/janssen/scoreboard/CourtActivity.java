package org.janssen.scoreboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import org.janssen.scoreboard.model.Server;
import org.janssen.scoreboard.model.types.RoomType;
import org.janssen.scoreboard.task.OnTaskListener;
import org.janssen.scoreboard.task.UserLoginTask;

/**
 * Court activity.
 *
 * Created by stephan on 18/08/13.
 */
public class CourtActivity extends ImmersiveStickyActivity implements OnTaskListener {

    private int selectedCourt;

    @Override
    public void onCreate(Bundle icicle) {

        super.onCreate(icicle);

        setContentView(R.layout.activity_court);

        final Spinner courtsSpinner = findViewById(R.id.court);
        courtsSpinner.setSelection(0);

        ArrayAdapter<CharSequence> adapterMinutesSpinner
                = ArrayAdapter.createFromResource(this, R.array.courts, android.R.layout.simple_spinner_item);
        adapterMinutesSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // Apply the adapter to the spinner
        courtsSpinner.setAdapter(adapterMinutesSpinner);
        courtsSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                selectedCourt = pos;
                Server.setCourt(selectedCourt);
                Server.setIp(RoomType.getIpForCourt(selectedCourt));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    public void startLogin(View view) {
        new UserLoginTask(this, "test", "test").execute();
    }

    @Override
    public void onTaskCompleted(String authToken) {

        if (authToken.contains("connect timed out")) {
            Toast.makeText(getApplicationContext(), "Geen connectie met scorebord", Toast.LENGTH_LONG).show();
        } else {
            Server.setToken(authToken);

            Intent intent = new Intent(getApplicationContext(), NewGameActivity.class);
            intent.putExtra(Constants.AUTH_TOKEN, authToken);
            intent.putExtra(Constants.COURT, selectedCourt);
            startActivity(intent);
        }
    }

    @Override
    public void onTaskCancelled() {
        System.out.println("Canceled");
    }
}
