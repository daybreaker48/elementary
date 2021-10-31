package com.mhd.stard.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mhd.stard.R;
import com.mhd.stard.common.vo.ScheduleData;
import com.mhd.stard.util.MHDLog;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ReCyclerScheduleAdapter extends RecyclerView.Adapter<ReCyclerScheduleAdapter.RecyclerViewHolder> {

    private ArrayList<ScheduleData> listData = new ArrayList<>();

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View holderView = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_holder_view, parent, false);
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

    public void addItem(ScheduleData data) {
        // 외부에서 item을 추가시킬 함수입니다.
        listData.add(data);
    }

    public void deleteAll() {
        // 리스트 전체삭제
        listData.clear();
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder{
        public TextView tvTime, tvMon, tvTue, tvWed, tvThu, tvFri, tvSat, tvSun;

        public RecyclerViewHolder(View view){
            super(view);

            this.tvTime = view.findViewById(R.id.tv_schedule_time_holder);
            this.tvMon = view.findViewById(R.id.tv_schedule_mon_holder);
            this.tvTue = view.findViewById(R.id.tv_schedule_tue_holder);
            this.tvWed = view.findViewById(R.id.tv_schedule_wed_holder);
            this.tvThu = view.findViewById(R.id.tv_schedule_thu_holder);
            this.tvFri = view.findViewById(R.id.tv_schedule_fri_holder);
            this.tvSat = view.findViewById(R.id.tv_schedule_sat_holder);
            this.tvSun = view.findViewById(R.id.tv_schedule_sun_holder);
        }

        void onBind(ScheduleData data) {
//            holder.tvSubject.setText(this.textSet1[position]);
//        holder.imageView.setBackgroundResource(this.imgSet[position]);
//            this.tvTime.setText(data.getTime());
//            this.tvMon.setText(data.getMon());
//            this.tvTue.setText(data.getTue());
//            this.tvWed.setText(data.getWed());
//            this.tvThu.setText(data.getThu());
//            this.tvFri.setText(data.getFri());
//            this.tvSat.setText(data.getSat());
//            this.tvSun.setText(data.getSun());
        }
    }
}
