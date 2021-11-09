package com.mhd.stard.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.mhd.stard.MainActivity;
import com.mhd.stard.R;
import com.mhd.stard.common.MHDApplication;
import com.mhd.stard.common.vo.TodoData;
import com.mhd.stard.common.vo.TodoEndData;
import com.mhd.stard.network.MHDNetworkInvoker;
import com.mhd.stard.util.MHDDialogUtil;
import com.mhd.stard.util.MHDLog;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ReCyclerEndAdapter extends RecyclerView.Adapter<ReCyclerEndAdapter.RecyclerViewHolder> {
    private ArrayList<TodoEndData> listData = new ArrayList<>();
    private static Context context;
    private String queryDays = "";
    public static String sectionDivide = "";
    public static int[] detailBack = {R.drawable.detail_background,R.drawable.detail_background_1,R.drawable.detail_background_2,R.drawable.detail_background_3,R.drawable.detail_background_4};
    public static int detailIdx = 0;
    public static int dataCount = 0;

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
        View holderView = LayoutInflater.from(parent.getContext()).inflate(R.layout.todoend_list_holder_view, parent, false);
        RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(holderView);
        context = parent.getContext();
        dataCount = getItemCount();

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

    public void addItem(TodoEndData data) {
        // 외부에서 item을 추가시킬 함수입니다.
        listData.add(data);
    }

    public void deleteAll() {
        // 리스트 전체삭제
        listData.clear();
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder{
        public TextView tvSection;
        public TextView tv_todo_title_holder, tv_todo_terms_holder;
        public String tmpYN = "";

        public RecyclerViewHolder(View view){
            super(view);

            this.tvSection = view.findViewById(R.id.tv_todo_subject_section);
            this.tv_todo_title_holder = view.findViewById(R.id.tv_todo_title_holder);
            this.tv_todo_terms_holder = view.findViewById(R.id.tv_todo_terms_holder);

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

        void onBind(TodoEndData data) {
            if(Integer.parseInt(data.getSection()) == 0){ // 섹션을 표시할 때. 이때가 컬러를 바꿔야 할 때.
                if(detailIdx == 4) detailIdx = 0;
                else detailIdx++;
            }
            sectionDivide = data.getSubject();
            this.tvSection.setText(data.getSubject());
            this.tvSection.setVisibility(Integer.parseInt(data.getSection()));
            this.tv_todo_title_holder.setText(data.getTitle());
            this.tv_todo_terms_holder.setText(data.getStart().substring(0,10) + " ~ " + data.getTdenddate().substring(0,10));
        }
    }
}
