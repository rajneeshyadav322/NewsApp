package com.example.news;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class NewsAdapter extends RecyclerView.Adapter<NewsViewHolder> {

    ArrayList<News> mNewsList;
    NewsItemClicked mListener;

    NewsAdapter(NewsItemClicked listener) {
        mNewsList = new ArrayList<News>();
        mListener = listener;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent,false);
        NewsViewHolder viewHolder = new NewsViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onItemClicked(mNewsList.get(viewHolder.getAdapterPosition()));
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        News currentItem = mNewsList.get(position);
        Glide.with(holder.itemView.getContext()).load(currentItem.urlToImage).into(holder.newsImage);
        holder.title.setText(currentItem.title);
    }

    @Override
    public int getItemCount() {
        return mNewsList.size();
    }

    void updateNews(ArrayList<News> updatedNews) {
        mNewsList.clear();
        mNewsList.addAll(updatedNews);

        notifyDataSetChanged();
    }
}
class NewsViewHolder extends RecyclerView.ViewHolder {

    public ImageView newsImage;
    public TextView title;

    public NewsViewHolder(@NonNull View itemView) {
        super(itemView);
        newsImage = itemView.findViewById(R.id.newsImage);
        title = itemView.findViewById(R.id.newsTitle);
    }
}
interface NewsItemClicked {
    void onItemClicked(News item);
}
