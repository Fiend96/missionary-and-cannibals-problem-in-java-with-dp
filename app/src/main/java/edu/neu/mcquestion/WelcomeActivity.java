package edu.neu.mcquestion;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class WelcomeActivity extends AppCompatActivity {
    EditText editText;
    EditText editText2;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        editText = (EditText) findViewById(R.id.editText);
        editText2 = (EditText) findViewById(R.id.editText2);
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String people = editText.getText().toString();
                String number = editText2.getText().toString();
                if (TextUtils.isEmpty(people) || TextUtils.isEmpty(number)) {
                    new AlertDialog.Builder(WelcomeActivity.this)
                            .setTitle("错误")
                            .setMessage("传教士人数或船载量为空")
                            .show();
                    return;
                }
                Intent intent = new Intent(WelcomeActivity.this, MainActivity.class);
                intent.putExtra("people", people);
                intent.putExtra("number", number);
                startActivity(intent);
            }
        });
    }
}
