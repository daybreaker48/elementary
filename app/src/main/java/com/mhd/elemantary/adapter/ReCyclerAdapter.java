package com.mhd.elemantary.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mhd.elemantary.R;
import com.mhd.elemantary.util.MHDLog;

public class ReCyclerAdapter extends RecyclerView.Adapter<ReCyclerAdapter.RecyclerViewHolder> {

    private String[] textSet;
    private int[] imgSet;

    public ReCyclerAdapter(String[] textSet, int[] imgSet){
        this.textSet = textSet;
        this.imgSet = imgSet;
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder{
        public TextView textView;
        public ImageView imageView;

        public RecyclerViewHolder(View view){
            super(view);
            this.textView = view.findViewById(R.id.tv_text_holder);
            this.imageView = view.findViewById(R.id.iv_image_holder);
        }
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View holderView = LayoutInflater.from(parent.getContext()).inflate(R.layout.receiving_holder_view, parent, false);
        RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(holderView);
        MHDLog.d("dagian", "viewholder create");
        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        holder.textView.setText(this .textSet[position]);
        holder.imageView.setBackgroundResource(this.imgSet[position]);
    }

    @Override
    public int getItemCount() {
        return textSet.length > imgSet.length ? textSet.length : imgSet.length;
    }
}
