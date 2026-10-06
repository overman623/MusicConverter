package com.happyhouse.music.converter.help;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.happyhouse.music.converter.R;

import java.util.ArrayList;

class HowToAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    private final static String TAG = HowToAdapter.class.getName();

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textHowToTitle;
        ImageView imageHowTo;
        TextView textHowToContent;

        MyViewHolder(View view){
            super(view);
            textHowToTitle = view.findViewById(R.id.text_how_to_title);
            imageHowTo = view.findViewById(R.id.image_how_to);
            textHowToContent = view.findViewById(R.id.text_how_to_content);
        }
    }

    private ArrayList<HowToItem> howToItems;
    HowToAdapter(ArrayList<HowToItem> howToItems){
        this.howToItems = howToItems;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_how_to, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        MyViewHolder myViewHolder = (MyViewHolder) holder;
        myViewHolder.textHowToTitle.setText(howToItems.get(position).getTitle());
        Glide.with(holder.itemView).load(howToItems.get(position).getImage()).into(myViewHolder.imageHowTo);
        myViewHolder.textHowToContent.setText(howToItems.get(position).getContent());

    }

    public void removeItem(int position){
        howToItems.remove(position);
    }

    @Override
    public int getItemCount() {
        return howToItems.size();
    }


}