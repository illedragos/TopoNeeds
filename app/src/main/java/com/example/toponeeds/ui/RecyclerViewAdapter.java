package com.example.toponeeds.ui;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.toponeeds.ListActivity;
import com.example.toponeeds.R;
import com.example.toponeeds.data.DatabaseHandler;
import com.example.toponeeds.model.Item;
import com.google.android.material.snackbar.Snackbar;

import java.text.MessageFormat;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private Context context;
    private List<Item> itemList;

    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private LayoutInflater inflater;

    public RecyclerViewAdapter(Context context, List<Item> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.list_row,parent,false);

        return new ViewHolder(view, context);
    }

    //@SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {

        Item item = itemList.get(position); //object Item
        holder.itemName.setText(MessageFormat.format("Articol: {0}", item.getItemName()));
        holder.itemQuantity.setText(MessageFormat.format("Cantitate: {0}", String.valueOf(item.getItemQuantity())));
        holder.itemColor.setText(MessageFormat.format("Coloare: {0}", item.getItemColor()));
        holder.itemSize.setText(MessageFormat.format("Marime: {0}", String.valueOf(item.getItemSize())));
        holder.itemDate.setText(MessageFormat.format("Adaugat la: {0}", item.getDateItemAdded()));
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView itemName;
        public TextView itemQuantity;
        public TextView itemColor;
        public TextView itemSize;
        public TextView itemDate;
        public Button editButton;
        public Button deleteButton;
        public int id;
        public ViewHolder(@NonNull View itemView, Context ctx) {
            super(itemView);
            context = ctx;

            itemName = itemView.findViewById(R.id.item_name);
            itemQuantity = itemView.findViewById(R.id.item_quantity);
            itemColor = itemView.findViewById(R.id.item_color);
            itemSize = itemView.findViewById(R.id.item_size);
            itemDate = itemView.findViewById(R.id.item_date);

            editButton = itemView.findViewById(R.id.editButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);

            editButton.setOnClickListener(this);
            deleteButton.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            //int position = getAdapterPosition();
            int position=getAdapterPosition();
            Item item = itemList.get(position);

            switch (v.getId()){
                case R.id.editButton:
                    editItem(item);
                    break;
                case R.id.deleteButton:
                    deleteItem(item.getId());
                    break;
                default:
                    break;
            }


        }

        private void editItem(final Item item) {
            //final int adpt = getAdapterPosition();

            //final Item item = itemList.get(adpt);


            builder = new AlertDialog.Builder(context);
            inflater = LayoutInflater.from(context);
            final View view = inflater.inflate(R.layout.popup,null);

            final EditText topoItem;
            final EditText itemQuantity;
            final EditText itemColor;
            final EditText itemSize;
            Button saveButton;
            TextView title;

            topoItem = view.findViewById(R.id.topoItem);
            itemQuantity = view.findViewById(R.id.quantityItem);
            itemColor = view.findViewById(R.id.colorItem);
            itemSize = view.findViewById(R.id.sizeItem);
            title=view.findViewById(R.id.title);

            topoItem.setText(item.getItemName());
            itemQuantity.setText(String.valueOf(item.getItemQuantity()));
            itemColor.setText(String.valueOf(item.getItemColor()));
            itemSize.setText(String.valueOf(item.getItemSize()));
            saveButton = view.findViewById(R.id.saveButton);
            saveButton.setText("update");



            ////////////////////////////////////////\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
            builder.setView(view);

            dialog = builder.create(); //creating our dialog object
            dialog.show();


            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseHandler databaseHandler = new DatabaseHandler(context);





                    if(topoItem.getText().toString().isEmpty())
                        item.setItemName("NULL");
                    else
                        item.setItemName(topoItem.getText().toString());

                    if(itemColor.getText().toString().isEmpty())
                        item.setItemColor("NULL");
                    else
                        item.setItemColor(itemColor.getText().toString());

                    if(itemQuantity.getText().toString().isEmpty())
                        item.setItemQuantity(-1);
                    else
                        item.setItemQuantity(Integer.parseInt(itemQuantity.getText().toString()));

                    if(itemSize.getText().toString().isEmpty())
                        item.setItemSize(-1);
                    else
                        item.setItemSize(Integer.parseInt(itemSize.getText().toString()));

                    Log.d("Rapid","id="+id);
                    Log.d("Rapid","item-id="+item.getId());
                    Log.d("Rapid","item-name="+item.getItemName());
                    Log.d("Rapid","item-color="+item.getItemColor());
                    Log.d("Rapid","item-quantity="+item.getItemQuantity());
                    Log.d("Rapid","item-size="+item.getItemSize());
                    Log.d("Rapid","item-date="+item.getDateItemAdded());
                    Log.d("Rapid","adapter-id="+getAdapterPosition());


                    Snackbar.make(v,"Item Updated",Snackbar.LENGTH_SHORT).show();
                    databaseHandler.updateItem(item);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            notifyItemChanged(getAdapterPosition(), item); //important!
                            dialog.dismiss();

                        }
                    },1200);//1.2 sec





//                    databaseHandler.updateItem(newItem);
//                    notifyItemChanged(getAdapterPosition(),newItem); //important!





                }
            });

            //important state




            //DatabaseHandler db = new DatabaseHandler(context);
            //Item item1 = new Item(id,"TV","negru",888,444,null);
            //db.updateItem(item1);
            //dialog.dismiss();

        }

        private void deleteItem(final int id) {

            builder = new AlertDialog.Builder(context);

            inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.confirmation_pop,null);

            Button noButton = view.findViewById(R.id.conf_no_button);
            Button yesButton = view.findViewById(R.id.conf_yes_button);

            builder.setView(view);
            dialog = builder.create();
            dialog.show();

            yesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    DatabaseHandler db = new DatabaseHandler(context);
                    db.deleteItem(id);
                    itemList.remove(getAdapterPosition());
                    notifyItemRemoved(getAdapterPosition());

                    dialog.dismiss();

                }
            });

            noButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });





        }
    }
}

