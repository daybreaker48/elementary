package com.mhd.stard.adapter;

import android.content.Context;
import android.graphics.Color;
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
import com.mhd.stard.common.vo.SelfData;
import com.mhd.stard.network.MHDNetworkInvoker;
import com.mhd.stard.util.MHDLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ReCyclerSelfAdapter extends RecyclerView.Adapter<ReCyclerSelfAdapter.RecyclerViewHolder> {
    private ArrayList<SelfData> listData = new ArrayList<>();
    private static Context context;

    public interface OnItemClickListener {
        void onItemClick(View v, int position);
    }
    public interface OnItemLongClickListener {
        void onItemLongClick(View v, int position);
    }
    //// 리스너 객체 참조를 저장하는 변수
    private static ReCyclerSelfAdapter.OnItemClickListener mListener = null;
    private static ReCyclerAdapter.OnItemLongClickListener mLongListener = null;

    //// OnItemClickListener 리스너 객체 참조를 어댑터에 전달하는 메서드
    public void setOnItemClickListener(ReCyclerSelfAdapter.OnItemClickListener listener) {
        this.mListener = listener;
    }
    public void setOnItemLongClickListener(ReCyclerAdapter.OnItemLongClickListener listener) {
        this.mLongListener = listener;
    }

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
    public int getItemViewType(int position) {
        return position;
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

        public RecyclerViewHolder(View view){
            super(view);

            this.tvSelfItem = view.findViewById(R.id.tv_self_item_holder);
            this.cbSelfComplete = view.findViewById(R.id.cb_self_complete_holder);

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
            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int pos = getAdapterPosition() ;
                    if (pos != RecyclerView.NO_POSITION) {
                        // 리스너 객체의 메서드 호출.
                        if (mLongListener != null) {
                            mLongListener.onItemLongClick(v, pos);
                        }
                    }

                    return true;
                }
            });
        }

        void onBind(final SelfData data) {
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

                    // 달성율 표시. 이것은 SelfFragment에서 해야할 일.
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
