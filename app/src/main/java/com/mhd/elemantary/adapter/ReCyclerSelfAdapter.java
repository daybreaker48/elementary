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
import android.widget.Toast;

import com.google.gson.Gson;
import com.mhd.elemantary.MainActivity;
import com.mhd.elemantary.R;
import com.mhd.elemantary.common.MHDApplication;
import com.mhd.elemantary.common.vo.SelfData;
import com.mhd.elemantary.network.MHDNetworkInvoker;
import com.mhd.elemantary.util.MHDLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ReCyclerSelfAdapter extends RecyclerView.Adapter<ReCyclerSelfAdapter.RecyclerViewHolder> {

    private ArrayList<SelfData> listData = new ArrayList<>();
    private static Context context;

    @NonNull
    @Override
    public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View holderView = LayoutInflater.from(parent.getContext()).inflate(R.layout.self_list_holder_view, parent, false);
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
    public int getItemCount() {
        // RecyclerView의 총 개수 입니다.
        return listData.size();
    }

    public void addItem(SelfData data) {
        // 외부에서 item을 추가시킬 함수입니다.
        listData.add(data);
    }

    public void deleteAll() {
        // 리스트 전체삭제
        listData.clear();
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder{
        public TextView tvSelfItem;
        public CheckBox cbSelfComplete;
//        public ImageView imageView;

        public RecyclerViewHolder(View view){
            super(view);

            this.tvSelfItem = view.findViewById(R.id.tv_self_item_holder);
            this.cbSelfComplete = view.findViewById(R.id.cb_self_complete_holder);
        }

        void onBind(final SelfData data) {
//            holder.tvSubject.setText(this.textSet1[position]);
//        holder.imageView.setBackgroundResource(this.imgSet[position]);
            this.tvSelfItem.setText(data.getSelfItem());
            this.cbSelfComplete.setOnCheckedChangeListener(null);
            if("Y".equals(data.getSelfComplete())){
                this.cbSelfComplete.setChecked(true);
                this.tvSelfItem.setPaintFlags(this.tvSelfItem.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                this.tvSelfItem.setTextColor(Color.GRAY);
            }else{
                this.cbSelfComplete.setChecked(false);
                this.tvSelfItem.setPaintFlags(this.tvSelfItem.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                this.tvSelfItem.setTextColor(Color.BLACK);
            }

            String tmpIdx = data.getSelfIdx();
            this.cbSelfComplete.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
            {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
                {
                    // UI처리
                    chkBox(isChecked);
                    // data. 즉 현재의 SelfData 값을 변경시킨다.(동기화)
                    String tmpChk = isChecked == true ? "Y" : "N";
                    data.setSelfComplete(tmpChk);
                    updateSelfItem(tmpIdx, tmpChk);
                }
            });
        }

        void chkBox(boolean isChecked){
            if(isChecked){
                this.tvSelfItem.setPaintFlags(this.tvSelfItem.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                this.tvSelfItem.setTextColor(Color.GRAY);
            }else{
                this.tvSelfItem.setPaintFlags(this.tvSelfItem.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                this.tvSelfItem.setTextColor(Color.BLACK);
            }
        }
    }

    /**
     * update self state
     */
    public static void updateSelfItem(String idx, String tmpChk){
        try {
            Map<String, String> params = new HashMap<String, String>();
            //params.put("UUID", MHDApplication.getInstance().getMHDSvcManager().getDeviceNewUuid());
            params.put("UUMAIL", MHDApplication.getInstance().getMHDSvcManager().getUserVo().getUuMail());
            params.put("IDX", idx);
            params.put("SFCOMPLETE", tmpChk);

            MHDNetworkInvoker.getInstance().sendVolleyRequest(context, R.string.url_restapi_update_self, params, ((MainActivity)context).responseListener);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            MHDLog.printException(e);
        }
    }

}
