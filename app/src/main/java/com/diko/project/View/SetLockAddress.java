package com.diko.project.View;

import android.content.Intent;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.diko.basemodule.Essential.BaseTemplate.BaseActivity;
import com.diko.project.Controller.LockSetController;
import com.diko.project.Controller.LoginController;
import com.diko.project.Manager.InterfaceManger;
import com.diko.project.R;
import com.diko.project.Utils.RetrofitUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by jie on 2018/4/11.
 */

public class SetLockAddress extends BaseActivity {
    private TextView back;//返回控件
    private TextView textView6;//设置门锁地址文字
    private EditText lock_address;//填写门锁地址
    private TextView finish;//确定门锁地址
    private String lockKey;//相对应门锁的密钥
    private String address;//相对应的门锁地址
    private String account;//这个应用程序的账号。
    @Override
    public int getLayoutId() {
        return R.layout.activity_set_lock_address;
    }

    @Override
    public void initViews() {
        back = findView(R.id.back);
        textView6 = findView(R.id.textView6);
        lock_address = findView(R.id.lock_address);
        finish = findView(R.id.finish);
    }

    @Override
    public void initListener() {
        back.setOnClickListener(this);
        finish.setOnClickListener(this);
    }

    @Override
    public void initData() {
        SharedPreferences userInformation = getSharedPreferences("UserInformation", MODE_PRIVATE);
        account = userInformation.getString("account","");
        Intent i = getIntent();
        lockKey = i.getStringExtra("lockKey");
    }

    @Override
    public void processClick(View v) {
        switch (v.getId()) {
            case R.id.back:
                finish();
                break;
            case R.id.finish:
                finish_click();
                break;
            default:
                break;
        }
    }

    private void finish_click() {
        address = lock_address.getText().toString().trim();
        if (!TextUtils.isEmpty(address)){
//            Log.e("finish_click: ",address+" "+lockKey+" "+account);
            review_address();
        }else{
            showToast("请填入地址");
        }
    }

    private void review_address() {
            List<MultipartBody.Part> parts = null;
            Map<String, RequestBody> params = new HashMap<>();
            params.put("account", RetrofitUtils.convertToRequestBody(account));
            params.put("lockKey", RetrofitUtils.convertToRequestBody(lockKey));
            params.put("address", RetrofitUtils.convertToRequestBody(address));
            LockSetController.SetLockAddress(params, parts, new InterfaceManger.OnRequestListener() {
                @Override
                public void onSuccess(Object success) {
                    showToast("修改地址成功");
                    finish();
                }

                @Override
                public void onError(String error) {
                    Toast.makeText(SetLockAddress.this, error, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onComplete() {

                }
            });
    }
}
