	package com.example.androidlabs;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private EditText todoEditText;
    private Button addButton;
    private ListView todoListView;
    private TodoListAdapter todoAdapter;
    private List<TodoItem> todoList;
    private MyOpener databaseHelper;


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
        databaseHelper = new MyOpener(this);

        // Load saved todos from the database when the app opens
        loadTodosFromDatabase();

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String todoText = todoEditText.getText().toString().trim();
                boolean isUrgent =((Switch) findViewById(R.id.aSwitch)).isChecked(); /* Your logic to get the urgency state */;

                if (!TextUtils.isEmpty(todoText)) {
                    TodoItem newItem = new TodoItem(todoText, isUrgent);


                    SQLiteDatabase db = databaseHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put(MyOpener.COLUMN_TEXT, todoText);
                    values.put(MyOpener.COLUMN_URGENCY, isUrgent ? 1 : 0);
                    db.insert(MyOpener.TABLE_TODO, null, values);
                    long newRowId = db.insert(MyOpener.TABLE_TODO, null, values);

                    db.close();
                    if (newRowId != -1) {
                        // Data added successfully
                        Toast.makeText(MainActivity.this, "Data added to the database", Toast.LENGTH_SHORT).show();
                        todoList.add(newItem);
                    } else {
                        // Insert failed
                        Toast.makeText(MainActivity.this, "Failed to add data to the database", Toast.LENGTH_SHORT).show();
                    }
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

                        // Remove from the database
                        // Remove from the database
                        SQLiteDatabase db = databaseHelper.getWritableDatabase();
                        int deletedRows = db.delete(
                                MyOpener.TABLE_TODO,
                                MyOpener.COLUMN_TEXT + " = ?",
                                new String[]{todoList.get(position).getText()}
                        );
                        db.close();

                        if (deletedRows > 0) {
                            // Data deleted successfully
                            Toast.makeText(MainActivity.this, "Data deleted from the database", Toast.LENGTH_SHORT).show();
                        } else {
                            // Deletion failed
                            Toast.makeText(MainActivity.this, "Failed to delete data from the database", Toast.LENGTH_SHORT).show();
                        }
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

    private void loadTodosFromDatabase() {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.query(MyOpener.TABLE_TODO, null, null, null, null, null, null);

        // Process the Cursor to load todos
        if (cursor != null) {
            int textColumnIndex = cursor.getColumnIndex(MyOpener.COLUMN_TEXT);
            int urgencyColumnIndex = cursor.getColumnIndex(MyOpener.COLUMN_URGENCY);

            while (cursor.moveToNext()) {
                if (textColumnIndex != -1 && urgencyColumnIndex != -1) {
                    String text = cursor.getString(textColumnIndex);
                    int urgency = cursor.getInt(urgencyColumnIndex);

                    TodoItem todo = new TodoItem(text, urgency == 1);
                    todoList.add(todo);
                } else {
                    // Handle situation when column index is not found
                    Log.e("TodoLoadError", "Column index not found");
                }
            }

            cursor.close();
            db.close();
            todoAdapter.notifyDataSetChanged();
        }
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
    private void printCursor(Cursor cursor) {
        if (cursor != null) {
            Log.d("CursorInfo", "Database Version: " + databaseHelper.getReadableDatabase().getVersion());
            Log.d("CursorInfo", "Number of columns in Cursor: " + cursor.getColumnCount());
            String[] columnNames = cursor.getColumnNames();
            Log.d("CursorInfo", "Column names: " + Arrays.toString(columnNames));
            Log.d("CursorInfo", "Number of results in Cursor: " + cursor.getCount());

            if (cursor.moveToFirst()) {
                do {
                    StringBuilder row = new StringBuilder();
                    for (String columnName : columnNames) {
                        int columnIndex = cursor.getColumnIndex(columnName);
                        if (columnIndex > -1) {
                            String columnValue = cursor.getString(columnIndex);
                            row.append(columnName).append(": ").append(columnValue).append(", ");
                        } else {
                            row.append(columnName).append(": ").append("Column not found").append(", ");
                            // Handle the situation where the column index is not found
                        }
                    }
                    Log.d("CursorInfo", "Row: " + row.toString());
                } while (cursor.moveToNext());
            }
        }
    }
}

