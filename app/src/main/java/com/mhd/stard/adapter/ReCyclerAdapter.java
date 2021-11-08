package com.mhd.stard.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.mhd.stard.MainActivity;
import com.mhd.stard.R;
import com.mhd.stard.common.MHDApplication;
import com.mhd.stard.common.vo.TodoData;
import com.mhd.stard.network.MHDNetworkInvoker;
import com.mhd.stard.util.MHDDialogUtil;
import com.mhd.stard.util.MHDLog;

public class ReCyclerAdapter extends RecyclerView.Adapter<ReCyclerAdapter.RecyclerViewHolder> {
    private ArrayList<TodoData> listData = new ArrayList<>();
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
        View holderView = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_list_holder_view, parent, false);
        RecyclerViewHolder recyclerViewHolder = new RecyclerViewHolder(holderView);
        context = parent.getContext();
        dataCount = getItemCount();

        return recyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolder holder, int position) {
        // Item을 하나, 하나 보여주는(bind 되는) 함수입니다.
        holder.onBind(listData.get(position), queryDays);
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

    public void setQueryDays(String tdate) {
        this.queryDays = tdate;
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
        public String tmpYN = "";

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

        void onBind(TodoData data, String tdate) {
            if(Integer.parseInt(data.getSection()) == 0){ // 섹션을 표시할 때. 이때가 컬러를 바꿔야 할 때.
                if(detailIdx == 4) detailIdx = 0;
                else detailIdx++;
            }
            sectionDivide = data.getSubject();
            this.tvDetail.setBackgroundResource(detailBack[detailIdx]);
            this.tvSection.setText(data.getSubject());
            this.tvSection.setVisibility(Integer.parseInt(data.getSection()));
            this.tvDetail.setText(data.getDetail());
            if(Integer.parseInt(data.getTotal()) > 0)
                this.tvDaily.setText(data.getDaily() + " page ( "+data.getRest()+" / "+data.getTotal()+" )");
            else
                this.tvDaily.setText(data.getDaily() + " page");
            this.tv_todo_title_holder.setText(data.getTitle());
            this.cbTodoComplete.setOnCheckedChangeListener(null);
            // 과거나 현재의의 데이타라면 그 날짜의 로그를 체크한다.
            if(checkFuture(tdate) < 2) {
                String tmp = data.getTdpc();
                if ("0".equals(data.getTdpc())) { // 로그가 없다는 것.
                    this.cbTodoComplete.setChecked(false);
                    this.tvDaily.setPaintFlags(this.tvDaily.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                    this.tv_todo_title_holder.setPaintFlags(this.tv_todo_title_holder.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                } else { // 해당날짜 완료로그가 있다는 것. 이 경우는 체크해제를 못하도록 막아야하지 않을까...?
                    this.cbTodoComplete.setChecked(true);
                    this.tvDaily.setPaintFlags(this.tvDaily.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    this.tv_todo_title_holder.setPaintFlags(this.tv_todo_title_holder.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                }
            }else { // 미래. 체크할 수가 없기에 모든 상태가 체크해제 상태여야 한다.
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
                    if("N".equals(tmpYN)){
                        // 이 경우는 최종 완료에서 취소한 경우 밖에 없다. 그런 경우는 다시 리스너로 들어와도 그냥 처리 없이 나가야 한다.
                        tmpYN = "Y";
                        return;
                    }
                    if(isChecked && Integer.parseInt(data.getTotal())>0 && (Integer.parseInt(data.getRest()) + Integer.parseInt(data.getDaily()) >= Integer.parseInt(data.getTotal()))){
                        tmpYN = "Y";
                        // 최종 완료가 되는거라면.
                        MHDDialogUtil.sAlert(context, R.string.confirm_end, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
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
                                if(toto > 0) { // 총페이지가 입력되어 있을 때. 누적학습량과 총 페이지를 보여준다.
                                    if (rest >= toto) { // 최종 완료. 사용자에게 보여줄 때는 학습량과 총량을 맞춘다.
                                        changeRest(data.getDaily() + " page ( " + toto + " / " + toto + " ) 최종 완료!!");
                                        updateTodoItem(tmpIdx, tmpChk, rest, "Y", checkFuture(tdate), tdate, data.getKid(), dataCount);
                                    } else {
                                        changeRest(data.getDaily() + " page ( " + rest + " / " + toto + " )");
                                        updateTodoItem(tmpIdx, tmpChk, rest, "N", checkFuture(tdate), tdate, data.getKid(), dataCount);
                                    }
                                } else {
                                    // 총페이지가 업을 때는 누적학습량을 보여줘야 하는가? 계산은 이미 했으니 db에 보내야지.UI는 취소선 처리
                                    updateTodoItem(tmpIdx, tmpChk, rest, "N", checkFuture(tdate), tdate, data.getKid(), dataCount);
                                }
                                return;
                            }
                        }, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                tmpYN = "N";
                                buttonView.setChecked(false);
                                return;
                            }
                        });
                    }else{
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
                        if(toto > 0) { // 총페이지가 입력되어 있을 때. 누적학습량과 총 페이지를 보여준다.
                            if (rest >= toto) { // 최종 완료. 사용자에게 보여줄 때는 학습량과 총량을 맞춘다.
                                changeRest(data.getDaily() + " page ( " + toto + " / " + toto + " ) 최종 완료!!");
                                updateTodoItem(tmpIdx, tmpChk, rest, "Y", checkFuture(tdate), tdate, data.getKid(), dataCount);
                            } else {
                                changeRest(data.getDaily() + " page ( " + rest + " / " + toto + " )");
                                updateTodoItem(tmpIdx, tmpChk, rest, "N", checkFuture(tdate), tdate, data.getKid(), dataCount);
                            }
                        } else {
                            // 총페이지가 업을 때는 누적학습량을 보여줘야 하는가? 계산은 이미 했으니 db에 보내야지.UI는 취소선 처리
                            updateTodoItem(tmpIdx, tmpChk, rest, "N", checkFuture(tdate), tdate, data.getKid(), dataCount);
                        }
                    }
                }
            });

            // 조회 날짜가 미래라면 모든 체크박스 비활성화
            this.cbTodoComplete.setEnabled(checkFuture(tdate) < 2);
            // 조회 날짜가 과거이고, 완료 or 삭제된 건은 체크박스 비활성화
            MHDLog.d("cbTodoComplete", "checkFuture(tdate)" + checkFuture(tdate));
            MHDLog.d("cbTodoComplete", "checkFuture(tdate)" + data.getUse());
            MHDLog.d("cbTodoComplete", "checkFuture(tdate)" + data.getEnd());
            if(checkFuture(tdate) == 0 && ("N".equals(data.getUse()) || "Y".equals(data.getEnd()))){
                this.cbTodoComplete.setEnabled(false);
            }
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
    public static void updateTodoItem(String idx, String tmpChk, int rest, String end, int past, String tdate, String tkid, int dCount){
        try {
            Map<String, String> params = new HashMap<String, String>();
            //params.put("UUID", MHDApplication.getInstance().getMHDSvcManager().getDeviceNewUuid());
            params.put("UUMAIL", MHDApplication.getInstance().getMHDSvcManager().getUserVo().getUuMail());
            params.put("IDX", idx);
            params.put("TDCOMPLETE", tmpChk);
            params.put("TDREST", String.valueOf(rest));
            params.put("TDEND", end);
            params.put("TDPAST", String.valueOf(past));
            params.put("TDPASTDAY", tdate);
            params.put("TDKID", tkid);
            params.put("TDCOUNT", String.valueOf(dCount));

            MHDNetworkInvoker.getInstance().sendVolleyRequest(context, R.string.url_restapi_update_todo_check, params, ((MainActivity)context).responseListener);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            MHDLog.printException(e);
        }
    }

    public static int checkFuture(String rdate) {
        Calendar cal = Calendar.getInstance();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String tdate = df.format(cal.getTime());
        Date rdate_d = null;
        Date tdate_d = null;
        try {
            rdate_d = df.parse(rdate);
            tdate_d = df.parse(tdate);
        } catch(ParseException e) {
            e.printStackTrace();
        }

        int compare = rdate_d.compareTo(tdate_d);
        if (compare > 0) { // 미래
            return 2;
        } else if (compare < 0) { // 과거
            return 0;
        } else {
            return 1;
        }
    }
}
