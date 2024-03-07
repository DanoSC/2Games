package com.example.a2games;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class menuAdapter extends RecyclerView.Adapter<menuAdapter.ViewHolder>{

    private ArrayList<ItemMenu> games;
    private Context mContext;

    menuAdapter(Context context, ArrayList<ItemMenu> sportsData) {
        this.games = sportsData;
        this.mContext = context;
    }
    @NonNull
    @Override
    public menuAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).
                inflate(R.layout.item_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull menuAdapter.ViewHolder holder, int position) {
        ItemMenu currentItem = games.get(position);


        Glide.with(mContext).load(currentItem.getImageResource()).into(holder.mSportsImage);
    }

    @Override
    public int getItemCount() {
        return games.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView mSportsImage;

        ViewHolder(View itemView) {
            super(itemView);

            mSportsImage = (ImageView) itemView.findViewById(R.id.item);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            ItemMenu currenItemn = games.get(position);

            switch (currenItemn.getName()){
                case "2048":
                    Intent game1= new Intent(mContext, Game2048.class);
                    mContext.startActivity(game1);
                    break;
                case "Senku":
                    Intent game2 = new Intent(mContext, GameSenku.class);
                    mContext.startActivity(game2);
                    break;
                case "Ajustes":
                    Intent ajustes = new Intent(mContext, Setings.class);
                    mContext.startActivity(ajustes);
                    break;

            }
        }

    }
}
