package com.mhd.stard.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mhd.stard.R;
import com.mhd.stard.adapter.ReCyclerKidsAdapter;
import com.mhd.stard.common.MHDApplication;
import com.mhd.stard.common.vo.KidsData;
import com.mhd.stard.common.vo.KidsVo;
import com.mhd.stard.util.MHDDialogUtil;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class KidsListActivity extends BaseActivity {
    RecyclerView recv_kidslist;
    private ReCyclerKidsAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    ImageView mTopRightImage;
    TextView vst_top_title;
    int kidsCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialize(R.layout.activity_kids_list);
        mContext = KidsListActivity.this;

        if(MHDApplication.getInstance().getMHDSvcManager().getKidsVo() != null) {
            KidsVo kidsVo = MHDApplication.getInstance().getMHDSvcManager().getKidsVo();
            kidsCount = kidsVo == null ? 0 : kidsVo.getCnt();
        }

        //////////////// 화면 타이틀 초기화
        vst_top_title = (TextView) findViewById(R.id.vst_top_title);
        vst_top_title.setText(R.string.title_kidslist);

        mTopRightImage = (ImageView) findViewById(R.id.vst_right_image);
        mTopRightImage.setOnClickListener(new View.OnClickListener() {//            @Override
            public void onClick(View v) {
                if(kidsCount >= 6){
                    MHDDialogUtil.sAlert(mContext, R.string.alert_kids_limit, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    });
                }else {
                    startKidsRegist();
                }
            }
        });
        AppCompatButton btn_move_stat_left = (AppCompatButton) findViewById(R.id.btn_move_stat_left);
        btn_move_stat_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        recv_kidslist = (RecyclerView) findViewById(R.id.recv_kidslist);
        recv_kidslist.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(mContext);
        recv_kidslist.setLayoutManager(layoutManager);
        adapter = new ReCyclerKidsAdapter();
        recv_kidslist.setAdapter(adapter);
        recv_kidslist.addItemDecoration(new DividerItemDecoration(recv_kidslist.getContext(), DividerItemDecoration.VERTICAL));

        adapter.setOnItemClickListener(new ReCyclerKidsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                // position을 가지고 라인을 알아낸 다음에
                // 해당 라인의 메뉴를 띄우려 했는데 이건 일단 보류. 안할 수도 있다.
                // 클릭하면 바로 수정화면으로 넘기고, 거기에 삭제버튼을 만든다.
                // 지난 데이타는 수정하지 못하게 한다.
                startKidsModify(position);
//                Toast.makeText(mContext, "modify " + position, Toast.LENGTH_SHORT).show();
            }
        }) ;

        init();
    }

    private void init(){
        adapter.deleteAll();

        // vo에 있는 아이 정보를 메뉴item 으로 삽입.
        KidsVo kidsVo = MHDApplication.getInstance().getMHDSvcManager().getKidsVo();
        for(int i=0; i<kidsVo.getCnt(); i++){
            // 각 List의 값들을 data 객체에 set 해줍니다.
            KidsData data = new KidsData();
            data.setKidsname(kidsVo.getMsg().get(i).getName());
            data.setKidsage(kidsVo.getMsg().get(i).getAge());
            data.setKidsidx(kidsVo.getMsg().get(i).getIdx());

            // 각 값이 들어간 data를 adapter에 추가합니다.
            adapter.addItem(data);
        }

        // adapter의 값이 변경되었다는 것을 알려줍니다.
        adapter.notifyDataSetChanged();

        kidsCount = kidsVo == null ? 0 : kidsVo.getCnt();
    }

    public void startKidsRegist(){
        Intent intent = new Intent(KidsListActivity.this, RegistKidsActivity.class);
        startActivityResultKids.launch(intent);
    }
    ActivityResultLauncher<Intent> startActivityResultKids = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // preference 업데이트 & 화면 갱신.
                        init();
                    }
                }
            });

    public void startKidsModify(int position){
        Intent intent = new Intent(KidsListActivity.this, ModifyKidsActivity.class);
        intent.putExtra("position", position);
        startActivityResultKidsModify.launch(intent);
    }
    ActivityResultLauncher<Intent> startActivityResultKidsModify = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        init();
                    }
                }
            });
}
