package com.example.a2games;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.TypedArray;
import android.os.Bundle;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class GamesMenu extends AppCompatActivity {
        private RecyclerView mRecyclerView;
        private ArrayList<ItemMenu> items;
        private menuAdapter mAdapter;

        @Override
        protected void onCreate (Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_games_menu);


            mRecyclerView = findViewById(R.id.recyclerView);
            mRecyclerView.setLayoutManager(new GridLayoutManager(this,2));

            items = new ArrayList<>();


            // Initialize the adapter and set it to the RecyclerView.
            mAdapter = new menuAdapter(this, items);
            mRecyclerView.setAdapter(mAdapter);

            // Get the data.
            initializeData();
        }


        /**
         * Initialize the sports data from resources.
         */
        private void initializeData () {
            String[] names = new String[3];
            names[0] = "2048";
            names[1] = "Senku";
            names[2] = "Ajustes";
            TypedArray itemImage = getResources().obtainTypedArray(R.array.itemsImage);

            for(int i=0;i < names.length;i++){
                ItemMenu item = new ItemMenu(names[i],itemImage.getResourceId(i,0));
                items.add(item);
            }
            itemImage.recycle();
            mAdapter.notifyDataSetChanged();
        }

}
