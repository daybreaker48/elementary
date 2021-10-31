package com.mhd.stard.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mhd.stard.R;
import com.mhd.stard.common.vo.KidsData;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ReCyclerKidsAdapter extends RecyclerView.Adapter<ReCyclerKidsAdapter.RecyclerViewHolder> {
    private ArrayList<KidsData> listData = new ArrayList<>();
    private static Context context;

    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }
    // 리스너 객체 참조를 저장하는 변수
    private static ReCyclerKidsAdapter.OnItemClickListener mListener = null;

    // OnItemClickListener 리스너 객체 참조를 어댑터에 전달하는 메서드
    public void setOnItemClickListener(ReCyclerKidsAdapter.OnItemClickListener listener) {
        this.mListener = listener ;
    }

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View holderView = LayoutInflater.from(parent.getContext()).inflate(R.layout.kids_list_holder_view, parent, false);
        RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(holderView);
        context = parent.getContext();

        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        // Item을 하나, 하나 보여주는(bind 되는) 함수입니다.
        holder.onBind(listData.get(position));
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        // RecyclerView의 총 개수 입니다.
        return listData.size();
    }

    public void addItem(KidsData data) {
        // 외부에서 item을 추가시킬 함수입니다.
        listData.add(data);
    }

    public void deleteAll() {
        // 리스트 전체삭제
        listData.clear();
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder{
        public TextView tv_kids_item_holder;

        public RecyclerViewHolder(View view){
            super(view);

            this.tv_kids_item_holder = view.findViewById(R.id.tv_kids_item_holder);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition() ;
                    if (pos != RecyclerView.NO_POSITION) {
                        // 리스너 객체의 메서드 호출.
                        if (mListener != null) {
                            mListener.onItemClick(v, pos) ;
                        }
                    }
                }
            });
        }

        void onBind(final KidsData data) {
            this.tv_kids_item_holder.setText(data.getKidsname()+" / ("+data.getKidsage()+"세)");

            String tmpIdx = data.getKidsidx();
        }
    }
}
