package com.mhd.elemantary.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import com.mhd.elemantary.MainActivity;
import com.mhd.elemantary.R;
import com.mhd.elemantary.common.vo.TodoData;
import com.mhd.elemantary.util.MHDLog;

public class ReCyclerAdapter extends RecyclerView.Adapter<ReCyclerAdapter.RecyclerViewHolder> {

    private String[] textSet1;
    private ArrayList<TodoData> listData = new ArrayList<>();

    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }
    // 리스너 객체 참조를 저장하는 변수
    private static OnItemClickListener mListener = null;

    // OnItemClickListener 리스너 객체 참조를 어댑터에 전달하는 메서드
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mListener = listener ;
    }

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
        public TextView tvSubject, tvDetail, tvDaily;
        public TextView tv_todo_publisher_holder, tv_todo_title_holder;
        public CheckBox cb_todo_complete_holder;
//        public ImageView imageView;

        public RecyclerViewHolder(View view){
            super(view);

            this.tvSubject = view.findViewById(R.id.tv_todo_subject_holder);
            this.tvDetail = view.findViewById(R.id.tv_todo_textbook_holder);
            this.tvDaily = view.findViewById(R.id.tv_todo_daily_holder);
            this.cb_todo_complete_holder = view.findViewById(R.id.cb_todo_complete_holder);

            this.tv_todo_publisher_holder = view.findViewById(R.id.tv_todo_publisher_holder);
            this.tv_todo_title_holder = view.findViewById(R.id.tv_todo_title_holder);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition() ;
                    if (pos != RecyclerView.NO_POSITION) {
                        // 리스너 객체의 메서드 호출.
                        if (mListener != null) {
                            mListener.onItemClick(v, pos) ;
                            // 여기서 메뉴를 띄워야 하는 듯
                        }
                    }
                }
            });
        }

        void onBind(TodoData data) {
//            holder.tvSubject.setText(this.textSet1[position]);
//        holder.imageView.setBackgroundResource(this.imgSet[position]);
            this.tvSubject.setText(data.getSubject());
            this.tvDetail.setText(data.getDetail());
            this.tvDaily.setText("하루학습 : " + data.getDaily());
            String temp = data.getPublisher();
            this.tv_todo_publisher_holder.setText(data.getPublisher());
            this.tv_todo_title_holder.setText(data.getTitle());
        }
    }
}
