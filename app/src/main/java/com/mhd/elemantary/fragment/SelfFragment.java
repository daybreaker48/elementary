package com.mhd.elemantary.fragment;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
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
import com.mhd.elemantary.MainActivity;
import com.mhd.elemantary.R;
import com.mhd.elemantary.adapter.ReCyclerSelfAdapter;
import com.mhd.elemantary.common.MHDApplication;
import com.mhd.elemantary.common.vo.KidsVo;
import com.mhd.elemantary.common.vo.MenuVo;
import com.mhd.elemantary.common.vo.SelfData;
import com.mhd.elemantary.common.vo.SelfVo;
import com.mhd.elemantary.network.MHDNetworkInvoker;
import com.mhd.elemantary.util.MHDLog;
import com.skydoves.powermenu.MenuAnimation;
import com.skydoves.powermenu.OnMenuItemClickListener;
import com.skydoves.powermenu.PowerMenu;
import com.skydoves.powermenu.PowerMenuItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SelfFragment extends BaseFragment {
    private RecyclerView recyclerView;
    private ReCyclerSelfAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    PowerMenu powerMenu = null;
    TextView vst_top_title, tv_no_data;
    String displayKid = "";
    int displayKidPosition = 0;

    public static SelfFragment create() {
        return new SelfFragment();
    }

    @Override
    public int getLayoutResId() {
        return R.layout.fragment_self;
    }

    @Override
    public void inOnCreateView(View root, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //Statusbar 아래로 내리기
//        final TextView mTitle = (TextView) root.findViewById(R.id.vst_top_title);
//        LinearLayout.LayoutParams mLayoutParams = (LinearLayout.LayoutParams) mTitle.getLayoutParams();
//        mLayoutParams.topMargin = Util.getInstance().getStatusBarHeight(root.getContext());
//        mTitle.setLayoutParams(mLayoutParams);
        LinearLayout ll_top_self = (LinearLayout) root.findViewById(R.id.ll_top_self);
        tv_no_data = (TextView) root.findViewById(R.id.tv_no_data);

        // vo에 있는 아이 정보를 메뉴item 으로 삽입.
        KidsVo kidsVo = MHDApplication.getInstance().getMHDSvcManager().getKidsVo();
        MenuVo menuVo = MHDApplication.getInstance().getMHDSvcManager().getMenuVo();
        List<PowerMenuItem> kidsList = new ArrayList();
        for(int k=0; k<kidsVo.getCnt(); k++){
            kidsList.add(new PowerMenuItem(kidsVo.getMsg().get(k).getName(), k == 0 ? true : false));
        }
        vst_top_title = (TextView) root.findViewById(R.id.vst_top_title);
        for(int k=0; k<menuVo.getMsg().size(); k++){
            if("SE".equals(menuVo.getMsg().get(k).getMenuname())){
                // 해당메뉴에 설정된 아이정보
                displayKid = menuVo.getMsg().get(k).getKidname();
                displayKidPosition = 0;
            }
        }
        vst_top_title.setText("["+displayKid+"] 습관");

        powerMenu = new PowerMenu.Builder(getActivity())
                .addItemList(kidsList) //
//                .addItem(new PowerMenuItem("한다인", false)) // add an item.
//                .addItem(new PowerMenuItem("한지인", false)) // aad an item list.
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
////                .addItem(new PowerMenuItem("한다인", false)) // add an item.
////                .addItem(new PowerMenuItem("한지인", false)) // aad an item list.
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

        querySelf();
    }

    @Override
    public void onResume() {
        super.onResume();

        // 기존에 선택됐던 item
        if(powerMenu != null) {
            int sp = powerMenu.getSelectedPosition() == -1 ? 0 : powerMenu.getSelectedPosition();
            KidsVo kidsVo = MHDApplication.getInstance().getMHDSvcManager().getKidsVo();
            List<PowerMenuItem> kidsList = new ArrayList();
            for (int k = 0; k < kidsVo.getCnt(); k++) {
                kidsList.add(new PowerMenuItem(kidsVo.getMsg().get(k).getName(), k == sp ? true : false));
            }

            powerMenu = new PowerMenu.Builder(getActivity())
                    .addItemList(kidsList) //
                    //                .addItem(new PowerMenuItem("한다인", false)) // add an item.
                    //                .addItem(new PowerMenuItem("한지인", false)) // aad an item list.
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
            vst_top_title.setText("["+displayKid+"] 습관");
            powerMenu.setSelectedPosition(position); // change selected item
            displayKidPosition = position;
            // MenuVo 정보를 갱신
            MenuVo menuVo = MHDApplication.getInstance().getMHDSvcManager().getMenuVo();
            for(int k=0; k<menuVo.getMsg().size(); k++){
                if("SE".equals(menuVo.getMsg().get(k).getMenuname())){
                    // 해당메뉴에 설정된 아이정보
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
//            // editor 내용 초기화.
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
     * BaseActivity에서 상속받지 못하기 때문에 parent Activity에서 받아서 현재 fragment의 function을 호출하도록 처리
     */
    public boolean networkResponseProcess(String nvMsg, int nvCnt, String result) {
        SelfVo selfVo = null;
        adapter.deleteAll();

        tv_no_data.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);

        if (nvCnt == 0) {
            // 정보가 없으면 비정상
            Toast.makeText(mContext, nvMsg, Toast.LENGTH_SHORT).show();
        } else {
            // self 정보를 받아옴.
            Gson gson = new Gson();
            selfVo = gson.fromJson(result, SelfVo.class);
            MHDApplication.getInstance().getMHDSvcManager().setSelfVo(null);
            MHDApplication.getInstance().getMHDSvcManager().setSelfVo(selfVo);
        }
        for(int i=0; i<nvCnt; i++){
            // 각 List의 값들을 data 객체에 set 해줍니다.
            SelfData data = new SelfData();
            data.setSelfIdx(selfVo.getMsg().get(i).getIdx());
            data.setSelfItem(selfVo.getMsg().get(i).getTbtitle());
            data.setSelfComplete(selfVo.getMsg().get(i).getSfcomplete());

            // 각 값이 들어간 data를 adapter에 추가합니다.
            adapter.addItem(data);
        }

        // adapter의 값이 변경되었다는 것을 알려줍니다.
        adapter.notifyDataSetChanged();

        return true;
    }
    public boolean networkResponseProcess_update(String nvMsg, int nvCnt, String result) {
        if (nvCnt == 0) {
            // 정보가 없으면 비정상. update 실패.
            Toast.makeText(mContext, "fail", Toast.LENGTH_SHORT).show();
        } else {// update 성공.
            Toast.makeText(mContext, "success", Toast.LENGTH_SHORT).show();
        }

        return true;
    }

    public void noData(String nvApiParam) {
        if(nvApiParam.equals(mContext.getString(R.string.restapi_query_self))) {
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
//                .addItem(new PowerMenuItem("한다인", false)) // add an item.
//                .addItem(new PowerMenuItem("한지인", false)) // aad an item list.
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
}
