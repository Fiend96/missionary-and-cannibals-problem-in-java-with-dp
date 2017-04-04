package edu.neu.mcquestion;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import darkness.Drive;
import darkness.Main;
import darkness.State;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final MCView mMCView = (MCView) findViewById(R.id.mc);
        int people = Integer.parseInt(getIntent().getStringExtra("people"));
        int number = Integer.parseInt(getIntent().getStringExtra("number"));
        final List<Drive> drives = new ArrayList<>();
        State.N = people;
        State.B = number;
        final State state = new State(people, people, 1);
        new Main().execute(people, number, drives);
        if (drives.size() != 0) {
            Log.d("Main", "drives:" + drives);
            mMCView.post(new Runnable() {
                @Override
                public void run() {
                    mMCView.start(drives, state);
                }
            });
        } else {
            Toast.makeText(this, "没有解", Toast.LENGTH_SHORT).show();
        }
    }
}
