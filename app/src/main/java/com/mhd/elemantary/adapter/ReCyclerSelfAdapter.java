package com.mhd.elemantary.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.mhd.elemantary.R;
import com.mhd.elemantary.common.vo.SelfData;
import com.mhd.elemantary.common.vo.TodoData;
import com.mhd.elemantary.util.MHDLog;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ReCyclerSelfAdapter extends RecyclerView.Adapter<ReCyclerSelfAdapter.RecyclerViewHolder> {

    private ArrayList<SelfData> listData = new ArrayList<>();

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View holderView = LayoutInflater.from(parent.getContext()).inflate(R.layout.self_list_holder_view, parent, false);
        RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(holderView);
        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        // Item을 하나, 하나 보여주는(bind 되는) 함수입니다.
        holder.onBind(listData.get(position));
    }

    @Override
    public int getItemCount() {
        // RecyclerView의 총 개수 입니다.
        return listData.size();
    }

    public void addItem(SelfData data) {
        // 외부에서 item을 추가시킬 함수입니다.
        listData.add(data);
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder{
        public TextView tvSelfItem;
        public CheckBox cb_self_complete_holder;
//        public ImageView imageView;

        public RecyclerViewHolder(View view){
            super(view);

            this.tvSelfItem = view.findViewById(R.id.tv_self_item_holder);
            this.cb_self_complete_holder = view.findViewById(R.id.cb_self_complete_holder);
        }

        void onBind(SelfData data) {
//            holder.tvSubject.setText(this.textSet1[position]);
//        holder.imageView.setBackgroundResource(this.imgSet[position]);
            this.tvSelfItem.setText(data.getSelfItem());
        }
    }
}
