package com.icesoft.tumblrget;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;


import com.icesoft.tumblrget.mail.tasks.SendMailAsyncTask;

import org.apache.commons.lang3.StringUtils;

public class MailActivity extends AppCompatActivity {
    private TextView tvSendMailTitle;
    private EditText etSendMailContent;
    private Button btnSendMail;
    private Spinner spMailType;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail);

        tvSendMailTitle = findViewById(R.id.tvSendMailTitle);
        etSendMailContent = findViewById(R.id.etSendMailContent);
        btnSendMail = findViewById(R.id.btnSendMail);
        spMailType = findViewById(R.id.spMailType);
        spMailType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int[] array = getResources().getIntArray(R.array.mail_type_value);
                if(array.length > position)
                {
                    int type = array[position];
                    System.out.println(type);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                System.out.println("Nothing");
            }
        });

        btnSendMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!StringUtils.isBlank(etSendMailContent.getText())){
                    SendMailAsyncTask task = new SendMailAsyncTask(MailActivity.this);
                    task.execute(etSendMailContent.getText().toString());
                }
            }
        });

    }
}
