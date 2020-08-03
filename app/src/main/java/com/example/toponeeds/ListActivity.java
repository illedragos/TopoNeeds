package com.example.toponeeds;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.toponeeds.data.DatabaseHandler;
import com.example.toponeeds.model.Item;
import com.example.toponeeds.ui.RecyclerViewAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private List<Item> itemList;
    private DatabaseHandler databaseHandler;
    private FloatingActionButton fab;
    private AlertDialog.Builder builder;
    private AlertDialog alertDialog;
    private Button saveButton;
    private EditText topoItem;
    private EditText itemQuantity;
    private EditText itemColor;
    private EditText itemSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        fab= findViewById(R.id.fab);

        recyclerView = findViewById(R.id.recyclerview);
        databaseHandler = new DatabaseHandler(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        itemList =  new ArrayList<>();

        //Get items from db
        itemList = databaseHandler.getAllItems();

        for(Item item : itemList){
            Log.d("ACTIVITYLIST","onCreate ::.::.::.::.::"+item.getItemName());
        }

        recyclerViewAdapter = new RecyclerViewAdapter(this, itemList);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.notifyDataSetChanged();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createPopDialog();
            }
        });
    }

    private void createPopDialog() {
        builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.popup,null);

        topoItem = view.findViewById(R.id.topoItem);
        itemQuantity = view.findViewById(R.id.quantityItem);
        itemColor = view.findViewById(R.id.colorItem);
        itemSize = view.findViewById(R.id.sizeItem);

        saveButton = view.findViewById(R.id.saveButton);


        builder.setView(view);
        alertDialog = builder.create();
        alertDialog.show();


        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!topoItem.getText().toString().isEmpty()&&
                        !itemColor.getText().toString().isEmpty()&&
                        !itemQuantity.getText().toString().isEmpty()&&
                        !itemSize.getText().toString().isEmpty())
                    saveItem(v);
                else{
                    Snackbar.make(v,"Empty Filed not allowed", Snackbar.LENGTH_SHORT).show();
                }
                //Snackbar.make(v,"Empty Filed not allowed", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    private void saveItem(View view) {
        //Todo save each topoitem to db
        String newItem      = topoItem.getText().toString().trim();
        int newQuantity     = Integer.parseInt(itemQuantity.getText().toString().trim());
        String newColor     = itemColor.getText().toString().trim();
        int newSize         = Integer.parseInt(itemSize.getText().toString().trim());

        Item item = new Item(newItem,newColor,newQuantity,newSize,null);
        databaseHandler.addItem(item);

        Snackbar.make(view,"item saved",Snackbar.LENGTH_SHORT).show();

        //Todo move to next screen - details screen
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //code to be run
                alertDialog.dismiss();
                startActivity(new Intent(ListActivity.this,ListActivity.class));

            }
        },1200);//1.2 sec

    }
}
