package com.mhd.stard.fragment;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mhd.stard.MainActivity;
import com.mhd.stard.R;
import com.mhd.stard.activity.ModifySelfActivity;
import com.mhd.stard.adapter.ReCyclerAdapter;
import com.mhd.stard.adapter.ReCyclerSelfAdapter;
import com.mhd.stard.common.MHDApplication;
import com.mhd.stard.common.vo.KidsVo;
import com.mhd.stard.common.vo.MenuVo;
import com.mhd.stard.common.vo.SelfData;
import com.mhd.stard.common.vo.SelfVo;
import com.mhd.stard.common.vo.TodoVo;
import com.mhd.stard.network.MHDNetworkInvoker;
import com.mhd.stard.util.MHDDialogUtil;
import com.mhd.stard.util.MHDLog;
import com.skydoves.powermenu.MenuAnimation;
import com.skydoves.powermenu.OnMenuItemClickListener;
import com.skydoves.powermenu.PowerMenu;
import com.skydoves.powermenu.PowerMenuItem;
import com.skydoves.progressview.ProgressView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SelfFragment extends BaseFragment {
    private RecyclerView recyclerView;
    private ReCyclerSelfAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    PowerMenu powerMenu = null;
    TextView vst_top_title, tv_self_percent, tv_tcount, tv_fcount;
    LinearLayout tv_no_data;
    String displayKid = "";
    int displayKidPosition = 0;
    int checkCount = 0;
    int totalCount = 0;
    ProgressView progressView;

    public static SelfFragment create() {
        return new SelfFragment();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_self;
    }

    public void onDialogResult(int which, int position){
        switch (which) {
            case 0: //??????
                ((MainActivity)getActivity()).startSelfModify(position);

                break;
            case 1: //??????
                MHDDialogUtil.sAlert(mContext, R.string.confirm_delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SelfVo selfVo = MHDApplication.getInstance().getMHDSvcManager().getSelfVo();
                        deleteSelf(selfVo.getMsg().get(position).getIdx());
                    }
                }, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                break;
        }
    }

    @Override
    public void inOnCreateView(View root, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //Statusbar ????????? ?????????
//        final TextView mTitle = (TextView) root.findViewById(R.id.vst_top_title);
//        LinearLayout.LayoutParams mLayoutParams = (LinearLayout.LayoutParams) mTitle.getLayoutParams();
//        mLayoutParams.topMargin = Util.getInstance().getStatusBarHeight(root.getContext());
//        mTitle.setLayoutParams(mLayoutParams);
        LinearLayout ll_top_self = (LinearLayout) root.findViewById(R.id.ll_top_self);
        tv_no_data = (LinearLayout) root.findViewById(R.id.tv_no_data);
        progressView = (ProgressView) root.findViewById(R.id.progressView);

        // vo??? ?????? ?????? ????????? ??????item ?????? ??????.
        KidsVo kidsVo = MHDApplication.getInstance().getMHDSvcManager().getKidsVo();
        MenuVo menuVo = MHDApplication.getInstance().getMHDSvcManager().getMenuVo();
        List<PowerMenuItem> kidsList = new ArrayList();
        for(int k=0; k<kidsVo.getCnt(); k++){
            kidsList.add(new PowerMenuItem(kidsVo.getMsg().get(k).getName(), k == 0 ? true : false));
        }
        tv_tcount = (TextView) root.findViewById(R.id.tv_tcount);
        tv_fcount = (TextView) root.findViewById(R.id.tv_fcount);
        tv_self_percent = (TextView) root.findViewById(R.id.tv_self_percent);
        vst_top_title = (TextView) root.findViewById(R.id.vst_top_title);
        for(int k=0; k<menuVo.getMsg().size(); k++){
            if("SE".equals(menuVo.getMsg().get(k).getMenuname())){
                // ??????????????? ????????? ????????????
                displayKid = menuVo.getMsg().get(k).getKidname();
                displayKidPosition = 0;
            }
        }
        vst_top_title.setText("[ "+displayKid+" ] ????????????");

        powerMenu = new PowerMenu.Builder(getActivity())
                .addItemList(kidsList) //
//                .addItem(new PowerMenuItem("?????????", false)) // add an item.
//                .addItem(new PowerMenuItem("?????????", false)) // aad an item list.
                .setTextSize(14)
                .setAnimation(MenuAnimation.SHOWUP_TOP_LEFT) // Animation start point (TOP | LEFT).
                .setMenuRadius(10f) // sets the corner radius.
                .setMenuShadow(10f) // sets the shadow.
                .setTextColor(ContextCompat.getColor(getActivity(), R.color.black))
                .setTextGravity(Gravity.CENTER)
                .setTextTypeface(Typeface.createFromAsset(getActivity().getAssets(), "notoregular.otf"))
                .setSelectedTextColor(Color.WHITE)
                .setMenuColor(Color.WHITE)
                .setSelectedMenuColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary))
                .setOnMenuItemClickListener(onMenuItemClickListener)
                .setDivider(new ColorDrawable(ContextCompat.getColor(getActivity(), R.color.gray))) // sets a divider.
                .setDividerHeight(1)
                .build();

//        ll_top_self.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                KidsVo kidsVo = MHDApplication.getInstance().getMHDSvcManager().getKidsVo();
//                List<PowerMenuItem> kidsList = new ArrayList();
//                for(int k=0; k<kidsVo.getCnt(); k++){
//                    kidsList.add(new PowerMenuItem(kidsVo.getMsg().get(k).getName(), k == 0 ? true : false));
//                }
//                powerMenu = new PowerMenu.Builder(getActivity())
//                        .addItemList(kidsList) //
////                .addItem(new PowerMenuItem("?????????", false)) // add an item.
////                .addItem(new PowerMenuItem("?????????", false)) // aad an item list.
//                        .setTextSize(14)
//                        .setAnimation(MenuAnimation.SHOWUP_TOP_LEFT) // Animation start point (TOP | LEFT).
//                        .setMenuRadius(10f) // sets the corner radius.
//                        .setMenuShadow(10f) // sets the shadow.
//                        .setTextColor(ContextCompat.getColor(getActivity(), R.color.black))
//                        .setTextGravity(Gravity.CENTER)
//                        .setTextTypeface(Typeface.createFromAsset(getActivity().getAssets(), "notoregular.otf"))
//                        .setSelectedTextColor(Color.WHITE)
//                        .setMenuColor(Color.WHITE)
//                        .setSelectedMenuColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary))
//                        .setOnMenuItemClickListener(onMenuItemClickListener)
//                        .setDivider(new ColorDrawable(ContextCompat.getColor(getActivity(), R.color.gray))) // sets a divider.
//                        .setDividerHeight(1)
//                        .build();
//
//                powerMenu.showAsDropDown(v);
//            }
//        });

        recyclerView = (RecyclerView) root.findViewById(R.id.recv_receiving);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ReCyclerSelfAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(recyclerView.getContext(), DividerItemDecoration.VERTICAL));

        adapter.setOnItemClickListener(new ReCyclerSelfAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                // position??? ????????? ????????? ????????? ?????????
                // ?????? ????????? ????????? ????????? ????????? ?????? ?????? ??????. ?????? ?????? ??????.
                // ???????????? ?????? ?????????????????? ?????????, ????????? ??????????????? ?????????.
                // ?????? ???????????? ???????????? ????????? ??????.
                    ((MainActivity)getActivity()).startSelfModify(position);
            }
        }) ;
        adapter.setOnItemLongClickListener(new ReCyclerAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(View v, int position) {
                // ?????? ????????? ?????????.
                Bundle args = new Bundle();
                args.putInt("position", position);
                args.putString("from", "self");
                DialogFragment newFragment = new MenuDialogFragment();
                newFragment.setArguments(args);
                newFragment.show(getChildFragmentManager(), "Dialog");
            }
        });

        querySelf();
    }

    @Override
    public void onResume() {
        super.onResume();

        // ????????? ???????????? item
        if(powerMenu != null) {
            int sp = powerMenu.getSelectedPosition() == -1 ? 0 : powerMenu.getSelectedPosition();
            KidsVo kidsVo = MHDApplication.getInstance().getMHDSvcManager().getKidsVo();
            List<PowerMenuItem> kidsList = new ArrayList();
            for (int k = 0; k < kidsVo.getCnt(); k++) {
                kidsList.add(new PowerMenuItem(kidsVo.getMsg().get(k).getName(), k == sp ? true : false));
            }

            powerMenu = new PowerMenu.Builder(getActivity())
                    .addItemList(kidsList) //
                    //                .addItem(new PowerMenuItem("?????????", false)) // add an item.
                    //                .addItem(new PowerMenuItem("?????????", false)) // aad an item list.
                    .setTextSize(14)
                    .setAnimation(MenuAnimation.SHOWUP_TOP_LEFT) // Animation start point (TOP | LEFT).
                    .setMenuRadius(10f) // sets the corner radius.
                    .setMenuShadow(10f) // sets the shadow.
                    .setTextColor(ContextCompat.getColor(getActivity(), R.color.black))
                    .setTextGravity(Gravity.CENTER)
                    .setTextTypeface(Typeface.createFromAsset(getActivity().getAssets(), "notoregular.otf"))
                    .setSelectedTextColor(Color.WHITE)
                    .setMenuColor(Color.WHITE)
                    .setSelectedMenuColor(ContextCompat.getColor(getActivity(), R.color.colorPrimary))
                    .setOnMenuItemClickListener(onMenuItemClickListener)
                    .setDivider(new ColorDrawable(ContextCompat.getColor(getActivity(), R.color.gray))) // sets a divider.
                    .setDividerHeight(1)
                    .setHeaderView(null)
                    .setFooterView(null)
                    .build();
        }
    }

    private OnMenuItemClickListener<PowerMenuItem> onMenuItemClickListener = new OnMenuItemClickListener<PowerMenuItem>() {
        @Override
        public void onItemClick(int position, PowerMenuItem item) {
            displayKid = item.getTitle().toString();
            vst_top_title.setText("[ "+displayKid+" ] ????????????");
            powerMenu.setSelectedPosition(position); // change selected item
            displayKidPosition = position;
            // MenuVo ????????? ??????
            MenuVo menuVo = MHDApplication.getInstance().getMHDSvcManager().getMenuVo();
            for(int k=0; k<menuVo.getMsg().size(); k++){
                if("SE".equals(menuVo.getMsg().get(k).getMenuname())){
                    // ??????????????? ????????? ????????????
                    menuVo.getMsg().get(k).setKidname(displayKid);
                    querySelf();
                    break;
                }
            }

            powerMenu.dismiss();
        }
    };

    @Override
    public void batchFunction(String api) {
//        if(api.equals(getString(R.string.api_editor_clear))) {
//            // editor ?????? ?????????.
//            editor.clearAllContents();
//        }
    }

    /**
     * query self
     */
    public void querySelf(){
        try {
            Map<String, String> params = new HashMap<String, String>();
            //params.put("UUID", MHDApplication.getInstance().getMHDSvcManager().getDeviceNewUuid());
            params.put("UUMAIL", MHDApplication.getInstance().getMHDSvcManager().getUserVo().getUuMail());
            params.put("TKNAME", displayKid);

            MHDNetworkInvoker.getInstance().sendVolleyRequest(((MainActivity)getActivity()), R.string.url_restapi_query_self, params, ((MainActivity)getActivity()).responseListener);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            MHDLog.printException(e);
        }
    }
    /**
     * BaseActivity?????? ???????????? ????????? ????????? parent Activity?????? ????????? ?????? fragment??? function??? ??????????????? ??????
     */
    public boolean networkResponseProcess(String nvMsg, int nvCnt, String result) {
        SelfVo selfVo = null;
        adapter.deleteAll();

        tv_no_data.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);

        if (nvCnt == 0) {
            // ????????? ????????? ?????????
            Toast.makeText(mContext, nvMsg, Toast.LENGTH_SHORT).show();
        } else {
            // self ????????? ?????????.
            Gson gson = new Gson();
            selfVo = gson.fromJson(result, SelfVo.class);
            MHDApplication.getInstance().getMHDSvcManager().setSelfVo(null);
            MHDApplication.getInstance().getMHDSvcManager().setSelfVo(selfVo);
        }

        int cCount = 0;
        for(int i=0; i<nvCnt; i++){
            // ??? List??? ????????? data ????????? set ????????????.
            SelfData data = new SelfData();
            data.setSelfIdx(selfVo.getMsg().get(i).getIdx());
            data.setSelfItem(selfVo.getMsg().get(i).getTbtitle());
            data.setSelfComplete(selfVo.getMsg().get(i).getSfcomplete());
            if("Y".equals(selfVo.getMsg().get(i).getSfcomplete())){
                cCount++;
            }

            // ??? ?????? ????????? data??? adapter??? ???????????????.
            adapter.addItem(data);
        }
        checkCount = cCount;
        totalCount = nvCnt;
        calcComplete(cCount, totalCount, false);

        // adapter??? ?????? ?????????????????? ?????? ???????????????.
        adapter.notifyDataSetChanged();

        return true;
    }
    public boolean networkResponseProcess_update(String nvMsg, int nvCnt, String result) {
        String checkValue = "";
        try{
            JSONObject nvJsonObject = new JSONObject(result);
            // ????????????. ????????? ????????? ??? M, S(Success) ?????? ???.
            String returnMsg = nvJsonObject.getString("msg");
            JSONObject nvLastString = new JSONObject(returnMsg);
            checkValue = nvLastString.getString("complete"); // ?????? ??????

        } catch (Exception e) {
            MHDLog.printException(e);
        } finally {
            if("N".equals(checkValue)) { //????????? -
                checkCount--;
                calcComplete(0, totalCount, false);
            }else if("Y".equals(checkValue)) { //????????? +
                checkCount++;
                calcComplete(0, totalCount, true);
            }
        }

        return true;
    }
    private void calcComplete(int cCount, int tCount, boolean checked){
        if(cCount == 0){ // ??????????????? ????????? ????????????
            tv_tcount.setText(tCount + "??? ???");
            tv_fcount.setText(checkCount + "??? ??????");
            progressView.setProgress(Math.round((checkCount*100)/tCount));
            progressView.setLabelText(Math.round((checkCount*100)/tCount) + "%");
//            tv_self_percent.setText(Math.round((checkCount*100)/tCount) + "%\n??????");
        }else{ // ?????? ??????
            tv_tcount.setText(tCount + "??? ???");
            tv_fcount.setText(cCount + "??? ??????");
            progressView.setProgress(Math.round((cCount*100)/tCount));
            progressView.setLabelText(Math.round((cCount*100)/tCount) + "%");
//            tv_self_percent.setText(Math.round((cCount*100)/tCount) + "%\n??????");
        }
    }

    public void noData(String nvApiParam) {
        if(nvApiParam.equals(mContext.getString(R.string.restapi_query_self))) {
            tv_self_percent.setText("0%\n??????");
            tv_no_data.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }

    public void showPMenu(){
        KidsVo kidsVo = MHDApplication.getInstance().getMHDSvcManager().getKidsVo();
        List<PowerMenuItem> kidsList = new ArrayList();
        for(int k=0; k<kidsVo.getCnt(); k++){
            kidsList.add(new PowerMenuItem(kidsVo.getMsg().get(k).getName(), k == 0 ? true : false));
        }
        powerMenu = new PowerMenu.Builder(getActivity())
                .addItemList(kidsList) //
//                .addItem(new PowerMenuItem("?????????", false)) // add an item.
//                .addItem(new PowerMenuItem("?????????", false)) // aad an item list.
                .setTextSize(14)
                .setAnimation(MenuAnimation.SHOWUP_TOP_LEFT) // Animation start point (TOP | LEFT).
                .setMenuRadius(10f) // sets the corner radius.
                .setMenuShadow(10f) // sets the shadow.
                .setTextColor(ContextCompat.getColor(getActivity(), R.color.black))
                .setTextGravity(Gravity.CENTER)
                .setTextTypeface(Typeface.createFromAsset(getActivity().getAssets(), "notoregular.otf"))
                .setSelectedTextColor(Color.WHITE)
                .setMenuColor(Color.WHITE)
                .setSelectedMenuColor(ContextCompat.getColor(getActivity(), R.color.powermenu_select))
                .setOnMenuItemClickListener(onMenuItemClickListener)
                .setDivider(new ColorDrawable(ContextCompat.getColor(getActivity(), R.color.gray))) // sets a divider.
                .setDividerHeight(1)
                .setHeaderView(null)
                .setFooterView(null)
                .build();

        powerMenu.setSelectedPosition(displayKidPosition);
        powerMenu.showAsDropDown(vst_top_title, 600, 0);
    }


    private void deleteSelf(String sIndex){
        // db index ??? ????????? ???????????? ?????? ?????? ??????
        try {
            Map<String, String> params = new HashMap<String, String>();
            //params.put("UUID", MHDApplication.getInstance().getMHDSvcManager().getDeviceNewUuid());
            params.put("UUMAIL", MHDApplication.getInstance().getMHDSvcManager().getUserVo().getUuMail());
            params.put("IDX", sIndex);

            MHDNetworkInvoker.getInstance().sendVolleyRequest(mContext, R.string.url_restapi_delete_self, params, ((MainActivity)getActivity()).responseListener);
        } catch (Exception e) {
            MHDLog.printException(e);
        }
    }
}
