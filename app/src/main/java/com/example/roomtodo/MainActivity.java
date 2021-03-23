package com.example.roomtodo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
//import android.widget.SearchView;
import androidx.appcompat.widget.SearchView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    EditText editText;
    Button btnAdd;
    Button btnReset;
    RecyclerView recyclerView;
    SearchView searchView;

    List<MainData> dataList = new ArrayList<>();
    LinearLayoutManager linearLayoutManager;
    RoomDB database;
    MainAdapter adapter;

    ItemTouchHelper itemTouchHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = findViewById(R.id.edit_text);
        btnAdd = findViewById(R.id.btn_add);
        btnReset = findViewById(R.id.btn_reset);
        recyclerView = findViewById(R.id.recycler_view);

        database = RoomDB.getInstance(MainActivity.this);
        dataList = database.mainDao().getAll();

        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new MainAdapter(MainActivity.this, dataList);
        recyclerView.setAdapter(adapter);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sText = editText.getText().toString().trim();
                if (!sText.equals("")) {
                    MainData data = new MainData();
                    data.setText(sText);
                    database.mainDao().insert(data);
                    dataList.clear();
                    dataList.addAll(database.mainDao().getAll());
                    adapter.notifyDataSetChanged();
                }
                editText.setText("");
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                database.mainDao().reset(dataList);
                dataList.clear();
                dataList.addAll(database.mainDao().getAll());
                adapter.notifyDataSetChanged();
            }
        });

        itemTouchHelper = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.ACTION_STATE_IDLE ) {
                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView,
                                          @NonNull RecyclerView.ViewHolder viewHolder,
                                          @NonNull RecyclerView.ViewHolder target) {
                        final int fromPos = viewHolder.getAdapterPosition();
                        final int toPos = target.getAdapterPosition();
                        adapter.notifyItemMoved(fromPos, toPos);

                        return true;
                    }

                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                    }
                }
        );
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) searchItem.getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                dataList.clear();
                dataList.addAll(database.mainDao().search(newText));
                adapter.notifyDataSetChanged();

                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }
}