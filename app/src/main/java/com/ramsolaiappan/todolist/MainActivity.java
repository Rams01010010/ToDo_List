package com.ramsolaiappan.todolist;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    ListView taskListView;
    FloatingActionButton addItemBtn;

    static ArrayList<String> todoList = new ArrayList<String>();
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("com.ramsolaiappan.todolist",MODE_PRIVATE);

        addItemBtn = (FloatingActionButton) findViewById(R.id.addItemBtn);
        taskListView = (ListView) findViewById(R.id.listView);

        todoList = new ArrayList<String>(Arrays.asList(sharedPreferences.getString("tasks","<space>").split("<space>")));
        updateTodoList();


        addItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ItemActivity.class);
                launchAddItem.launch(intent);
            }
        });

        taskListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).setTitle("Warning").setMessage("Completed ? Delete this task?").setNegativeButton("No",null).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        todoList.remove(position);
                        updateTodoList();
                    }
                }).show();
                return true;
            }
        });

    }

    ActivityResultLauncher<Intent> launchAddItem = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if(result.getResultCode() == Activity.RESULT_OK)
                    {
                        Intent data = result.getData();
                        todoList.add(data.getStringExtra("task"));
                        updateTodoList();

                        Snackbar.make(addItemBtn,"Task added", BaseTransientBottomBar.LENGTH_SHORT).show();
                    }
                }
            }
    );

    public void updateTodoList()
    {
        if(!todoList.isEmpty())
        {
            taskListView.setVisibility(View.VISIBLE);
            ((TextView) findViewById(R.id.textView3)).setVisibility(View.GONE);

            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1,todoList);
            taskListView.setAdapter(arrayAdapter);

            String tasksString = "";
            for(String item : todoList)
                tasksString += item + "<space>";

            sharedPreferences.edit().putString("tasks",tasksString).apply();
        }
        else
        {
            taskListView.setVisibility(View.GONE);
            ((TextView) findViewById(R.id.textView3)).setVisibility(View.VISIBLE);

            sharedPreferences.edit().remove("tasks").apply();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.option,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId())
        {
            case R.id.delete:
                if(!todoList.isEmpty()){
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).setTitle("Warning").setMessage("Are you sure? You want to delete all ?").setNegativeButton("No",null).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    todoList.clear();
                    updateTodoList();
                }
            }).show();}
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}