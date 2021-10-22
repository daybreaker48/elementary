package com.mhd.elemantary.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mhd.elemantary.MainActivity;
import com.mhd.elemantary.R;
import com.mhd.elemantary.common.MHDApplication;
import com.mhd.elemantary.common.vo.TodoData;
import com.mhd.elemantary.network.MHDNetworkInvoker;
import com.mhd.elemantary.util.MHDLog;

public class ReCyclerAdapter extends RecyclerView.Adapter<ReCyclerAdapter.RecyclerViewHolder> {
    private ArrayList<TodoData> listData = new ArrayList<>();
    private static Context context;

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

    public void addItem(TodoData data) {
        // 외부에서 item을 추가시킬 함수입니다.
        listData.add(data);
    }

    public void deleteAll() {
        // 리스트 전체삭제
        listData.clear();
    }

    public int[] getSectionPosition() {
        int arr_size = 0;
        for(int i=0; i<getItemCount(); i++){
            if("0".equals(listData.get(i).getSection())){
                // section의 수
                arr_size++;
            }
        }
        int[] arr_section = new int[arr_size-1];
        int arr_section_idx = 0;
        for(int i=0; i<getItemCount(); i++){
            if("0".equals(listData.get(i).getSection()) && i>0){
                // 맨 앞의 것을 제외(i>0)하고 section 이 그려지는 곳이라면. 그 앞의 index를 저장
                arr_section[arr_section_idx] = i-1;
                arr_section_idx++;
            }
        }

        return arr_section;
    }

    public static class RecyclerViewHolder extends RecyclerView.ViewHolder{
        public TextView tvDetail, tvDaily, tvSection;
        public TextView tv_todo_title_holder;
        public CheckBox cbTodoComplete;

//        public ImageView imageView;

        public RecyclerViewHolder(View view){
            super(view);

            this.tvSection = view.findViewById(R.id.tv_todo_subject_section);
            this.tvDetail = view.findViewById(R.id.tv_todo_textbook_holder);
            this.tvDaily = view.findViewById(R.id.tv_todo_daily_holder);
            this.cbTodoComplete = view.findViewById(R.id.cb_todo_complete_holder);
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
            this.tvSection.setText(data.getSubject());
            this.tvSection.setVisibility(Integer.parseInt(data.getSection()));
            this.tvDetail.setText(data.getDetail());
            if(Integer.parseInt(data.getTotal()) > 0)
                this.tvDaily.setText(data.getDaily() + " page ( "+data.getRest()+" / "+data.getTotal()+" )");
            this.tv_todo_title_holder.setText(data.getTitle());
            this.cbTodoComplete.setOnCheckedChangeListener(null);
            if("Y".equals(data.getComplete())){
                this.cbTodoComplete.setChecked(true);
                this.tvDaily.setPaintFlags(this.tvDaily.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                this.tv_todo_title_holder.setPaintFlags(this.tv_todo_title_holder.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }else{
                this.cbTodoComplete.setChecked(false);
                this.tvDaily.setPaintFlags(this.tvDaily.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                this.tv_todo_title_holder.setPaintFlags(this.tv_todo_title_holder.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            }

            String tmpIdx = data.getIdx();
            this.cbTodoComplete.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
            {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
                {
                    // UI처리
                    chkBox(isChecked);
                    // data. 즉 현재의 SelfData 값을 변경시킨다.(동기화)
                    String tmpChk = isChecked == true ? "Y" : "N";
                    data.setComplete(tmpChk);

                    if(isChecked) { // 오늘 학습 완료
                        // 총페이지가 있건 없건 학습량은 계속 + 해 나간다.
                        data.setRest(String.valueOf(Integer.parseInt(data.getRest()) + Integer.parseInt(data.getDaily())));
                    } else {
                        // 총페이지가 있건 없건 학습량은 계속 - 해 나간다. 사용자가 이 정보를 볼 일은 없다.
                        data.setRest(String.valueOf(Integer.parseInt(data.getRest()) - Integer.parseInt(data.getDaily())));
                    }
                    int rest = Integer.parseInt(data.getRest());
                    int toto = Integer.parseInt(data.getTotal());
                    int oned = Integer.parseInt(data.getDaily());
                    if(rest >= toto){ // 최종 완료. 사용자에게 보여줄 때는 학습량과 총량을 맞춘다.
                        changeRest(data.getDaily() + " page ( "+toto+" / "+toto+" ) 최종 완료!!");
                    }else{
                        changeRest(data.getDaily() + " page ( "+rest+" / "+toto+" )");
                    }

                    updateTodoItem(tmpIdx, tmpChk);
                }
            });
        }

        void chkBox(boolean isChecked){
            if(isChecked){
                this.tvDaily.setPaintFlags(this.tvDaily.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                this.tv_todo_title_holder.setPaintFlags(this.tv_todo_title_holder.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }else{
                this.tvDaily.setPaintFlags(this.tvDaily.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                this.tv_todo_title_holder.setPaintFlags(this.tv_todo_title_holder.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            }
        }

        void changeRest(String newval){
            this.tvDaily.setText(newval);
        }
    }

    /**
     * update self state
     */
    public static void updateTodoItem(String idx, String tmpChk){
        try {
            Map<String, String> params = new HashMap<String, String>();
            //params.put("UUID", MHDApplication.getInstance().getMHDSvcManager().getDeviceNewUuid());
            params.put("UUMAIL", MHDApplication.getInstance().getMHDSvcManager().getUserVo().getUuMail());
            params.put("IDX", idx);
            params.put("TDCOMPLETE", tmpChk);

            MHDNetworkInvoker.getInstance().sendVolleyRequest(context, R.string.url_restapi_update_todo_check, params, ((MainActivity)context).responseListener);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            MHDLog.printException(e);
        }
    }
}
