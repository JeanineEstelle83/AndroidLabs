package com.example.androidlabs;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private EditText todoEditText;
    private Button addButton;
    private ListView todoListView;
    private TodoListAdapter todoAdapter;
    private List<TodoItem> todoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        todoEditText = findViewById(R.id.editText);
        addButton = findViewById(R.id.button);
        todoListView = findViewById(R.id.lvItems);

        // Initialize your todo list and adapter
        todoList = new ArrayList<>();
        todoAdapter = new TodoListAdapter(this, todoList);
        todoListView.setAdapter(todoAdapter);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String todoText = todoEditText.getText().toString().trim();
                boolean isUrgent =((Switch) findViewById(R.id.aSwitch)).isChecked(); /* Your logic to get the urgency state */;

                if (!TextUtils.isEmpty(todoText)) {
                    TodoItem newItem = new TodoItem(todoText, isUrgent);
                    todoList.add(newItem);
                    todoAdapter.notifyDataSetChanged();
                    todoEditText.getText().clear();
                }
            }
        });

        // Implement onItemLongClick listener for deletion
        todoListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Do you want to delete this?");
                builder.setMessage("The selected row is: " + position);
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        todoList.remove(position);
                        todoAdapter.notifyDataSetChanged();
                    }
                });
                builder.setNegativeButton("No", null);
                builder.create().show();
                return true;
            }

        });
    }

    // TodoItem class to hold todo details
    public static class TodoItem {
        private String text;
        private boolean isUrgent;

        public TodoItem(String text, boolean isUrgent) {
            this.text = text;
            this.isUrgent = isUrgent;
        }

        public String getText() {
            return text;
        }
        public void setText(String text) {
            this.text = text;
        }

        public boolean isUrgent() {
            return isUrgent;
        }
        public void setUrgent(boolean urgent) {
            isUrgent = urgent;
        }
    }

    // TodoListAdapter to manage ListView items
    public class TodoListAdapter extends BaseAdapter {
        private List<TodoItem> todoItemList;
        private LayoutInflater inflater;

        public TodoListAdapter(Context context, List<TodoItem> todoItemList) {
            this.todoItemList = todoItemList;
            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return todoItemList.size();
        }

        @Override
        public Object getItem(int position) {
            return todoItemList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.todo, parent, false);
                holder = new ViewHolder();
                holder.textView = convertView.findViewById(R.id.TextView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            TodoItem todoItem = todoItemList.get(position);
            holder.textView.setText(todoItem.getText());

            if (todoItem.isUrgent()) {
                convertView.setBackgroundColor(Color.RED);
                holder.textView.setTextColor(Color.WHITE);
            } else {
                convertView.setBackgroundColor(Color.TRANSPARENT); // Reset background
                holder.textView.setTextColor(Color.BLACK); // Reset text color
            }

            return convertView;
        }

        class ViewHolder {
            TextView textView;
        }


    }
}