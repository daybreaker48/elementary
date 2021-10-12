package com.mhd.elemantary.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.mhd.elemantary.MainActivity;
import com.mhd.elemantary.R;
import com.mhd.elemantary.activity.RegistTodoActivity;
import com.mhd.elemantary.common.ClickCallbackListener;
import com.mhd.elemantary.common.MHDApplication;
import com.mhd.elemantary.common.MHDSvcManager;
import com.mhd.elemantary.common.vo.SelfData;
import com.mhd.elemantary.common.vo.SubjectListData;
import com.mhd.elemantary.common.vo.SubjectVo;
import com.mhd.elemantary.network.MHDNetworkInvoker;
import com.mhd.elemantary.util.MHDLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ReCyclerSubjectAdapter extends RecyclerView.Adapter<ReCyclerSubjectAdapter.RecyclerViewHolder> {

    private ArrayList<SubjectListData> listData = new ArrayList<>();
    private static Context context;
    private ClickCallbackListener callbackListener;

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View holderView = LayoutInflater.from(parent.getContext()).inflate(R.layout.subject_list_holder_view, parent, false);
        RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(holderView);
        context = parent.getContext();

        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        // Item을 하나, 하나 보여주는(bind 되는) 함수입니다.
        holder.onBind(listData.get(position), callbackListener);
    }

    @Override
    public int getItemCount() {
        // RecyclerView의 총 개수 입니다.
        return listData.size();
    }

    public void addItem(SubjectListData data) {
        // 외부에서 item을 추가시킬 함수입니다.
        listData.add(data);
    }

    public void deleteAll() {
        // 리스트 전체삭제
        listData.clear();
    }

    public void setCallbackListener(ClickCallbackListener callbackListener) {
        this.callbackListener = callbackListener;
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView tvSubjectItem;
        private ClickCallbackListener callbackListener;

//        public ImageView imageView;

        public RecyclerViewHolder(View view){
            super(view);

            this.tvSubjectItem = view.findViewById(R.id.tv_subject_item_holder);
            // 아이템 클릭 이벤트 처리.
            view.setOnClickListener(this);
        }

        void onBind(final SubjectListData data, ClickCallbackListener callbackListener) {
//            holder.tvSubject.setText(this.textSet1[position]);
//        holder.imageView.setBackgroundResource(this.imgSet[position]);
            this.tvSubjectItem.setText(data.getSubject());
            this.callbackListener = callbackListener;
        }

        @Override
        public void onClick(View view) {
            view.setBackgroundResource(R.color.mdtp_light_gray);
            callbackListener.callBack(getAdapterPosition());
        }
    }

}
