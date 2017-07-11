package com.example.egguncle.xposedclipboard.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.egguncle.xposedclipboard.R;

public class ProcessTextActivity extends AppCompatActivity {
    private EditText edProcess;
    private Button btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_process_text);
        edProcess = (EditText) findViewById(R.id.ed_process);
        btn = (Button) findViewById(R.id.btn);
        checkText(getIntent());
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra(Intent.EXTRA_PROCESS_TEXT, edProcess.getText().toString());
                setResult(RESULT_OK, intent);
                finish();
            }
        });

    }

    private void checkText(Intent intent){
        CharSequence txt=intent.getCharSequenceExtra(Intent.EXTRA_PROCESS_TEXT);
        int start=intent.getIntExtra("select_start",0);
        int end=intent.getIntExtra("select_end",0);
        Toast.makeText(this,"----"+txt+"----", Toast.LENGTH_SHORT).show();
        edProcess.setText(txt);
        edProcess.setSelection(start,end);
    }
}
