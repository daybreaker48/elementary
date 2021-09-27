package com.mhd.elemantary.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import java.util.ArrayList;

import com.mhd.elemantary.R;
import com.mhd.elemantary.common.vo.TodoData;
import com.mhd.elemantary.util.MHDLog;

public class ReCyclerAdapter extends RecyclerView.Adapter<ReCyclerAdapter.RecyclerViewHolder> {

    private String[] textSet1;
    private ArrayList<TodoData> listData = new ArrayList<>();

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View holderView = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_list_holder_view, parent, false);
        RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(holderView);
        MHDLog.d("dagian", "viewholder create");
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

    public void addItem(TodoData data) {
        // 외부에서 item을 추가시킬 함수입니다.
        listData.add(data);
    }

    public void deleteAll() {
        // 리스트 전체삭제
        listData.clear();
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder{
        public TextView tvSubject, tvTextbook, tvDaily;
        public CheckBox cb_todo_complete_holder;
//        public ImageView imageView;

        public RecyclerViewHolder(View view){
            super(view);

            this.tvSubject = view.findViewById(R.id.tv_todo_subject_holder);
            this.tvTextbook = view.findViewById(R.id.tv_todo_textbook_holder);
            this.tvDaily = view.findViewById(R.id.tv_todo_daily_holder);
            this.cb_todo_complete_holder = view.findViewById(R.id.cb_todo_complete_holder);
        }

        void onBind(TodoData data) {
//            holder.tvSubject.setText(this.textSet1[position]);
//        holder.imageView.setBackgroundResource(this.imgSet[position]);
            this.tvSubject.setText(data.getSubject());
            this.tvTextbook.setText(data.getTextbook());
            this.tvDaily.setText(data.getDailyProgress());
        }
    }
}
