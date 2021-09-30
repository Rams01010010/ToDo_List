package com.ramsolaiappan.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Calendar;
import java.util.Date;

public class ItemActivity extends AppCompatActivity {

    EditText taskET;
    ImageButton dateBtn;
    TextView dateText;
    final String NO_DATE = "No Due Date";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        dateBtn = (ImageButton) findViewById(R.id.dateBtn);
        taskET = (EditText) findViewById(R.id.taskEditText);
        dateText = (TextView) findViewById(R.id.dateTextView);

        Calendar calendar = Calendar.getInstance();
        int d = calendar.get(Calendar.DAY_OF_MONTH);
        int m = calendar.get(Calendar.MONTH);
        int y = calendar.get(Calendar.YEAR);

        dateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog  datePickerDialog = new DatePickerDialog(ItemActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String date = dayOfMonth + "/" + month + "/" + year;
                        dateText.setText(date);
                    }
                },y,m,d);
                datePickerDialog.show();
            }
        });
    }

    public void checkResult(View view)
    {
        FloatingActionButton fab = (FloatingActionButton) view;
        if(fab.getTag().equals("cancel"))
        {
            Intent intent = new Intent();
            setResult(RESULT_CANCELED,intent);
            finish();
        }
        else if(fab.getTag().equals("add"))
        {
            Intent intent = new Intent();
            String stringToSend = "";
            if(!taskET.getText().toString().equals(""))
            {
                stringToSend = taskET.getText().toString();
                if(!dateText.getText().toString().equals(NO_DATE))
                {
                    stringToSend += "  -  " + dateText.getText().toString();
                }
                intent.putExtra("task",stringToSend);
                setResult(RESULT_OK,intent);
                finish();
            }
            else
            {
                taskET.setError("Please enter a Task");
            }
        }
    }
}