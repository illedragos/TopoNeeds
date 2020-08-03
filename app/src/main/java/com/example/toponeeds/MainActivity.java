package com.example.toponeeds;

import android.content.Intent;
import android.os.Bundle;

import com.example.toponeeds.data.DatabaseHandler;
import com.example.toponeeds.model.Item;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private Button saveButton;
    private EditText topoItem;
    private EditText itemQuantity;
    private EditText itemColor;
    private EditText itemSize;
    private DatabaseHandler databaseHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        

        
        databaseHandler = new DatabaseHandler(this);
        byPassActivity();
        //check if the item was saved
        List<Item> items = databaseHandler.getAllItems();
        for(Item item : items)
        {
            Log.d("Main","onCreate::::::::"+item.getItemName()+"::"+item.getDateItemAdded());
        }


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                createPopupDialog();

//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });
    }

    private void byPassActivity() {
        if(databaseHandler.getItemCount()>0){
            startActivity(new Intent(MainActivity.this,ListActivity.class));
            finish();
        }
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
                dialog.dismiss();
                startActivity(new Intent(MainActivity.this,ListActivity.class));
            }
        },1200);//1.2 sec

    }

    private void createPopupDialog() {

        builder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.popup,null);

        topoItem = view.findViewById(R.id.topoItem);
        itemQuantity = view.findViewById(R.id.quantityItem);
        itemColor = view.findViewById(R.id.colorItem);
        itemSize = view.findViewById(R.id.sizeItem);

        ////////////////////////////////////////\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
        saveButton = view.findViewById(R.id.saveButton);
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

//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        //code to be run
//                        dialog.dismiss();
//                    }
//                },1200);//1.2 sec
//                startActivity(new Intent(MainActivity.this,ListActivity.class));



            }
        });
        ///////////////////////////////////////////\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
        builder.setView(view);

        dialog = builder.create(); //creating our dialog object
        dialog.show(); //important state



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
