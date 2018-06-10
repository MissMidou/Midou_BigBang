package com.example.srct.bigbong.view.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by srct on 2018/4/19.
 */

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.MyHolder> {
    public HomeAdapter(Context context, List<CardView> cardViews) {

    }



    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class MyHolder extends RecyclerView.ViewHolder {

        private ImageView imageView;

        public MyHolder(View view) {
            super(view);
        }

    }
}
