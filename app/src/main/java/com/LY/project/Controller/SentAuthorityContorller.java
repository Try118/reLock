package com.LY.project.Controller;

import android.content.Context;
import android.util.Log;

import com.LY.project.Manager.InterfaceManger;
import com.LY.project.R;
import com.LY.project.Utils.RetrofitUtils;

import org.json.JSONObject;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by jie on 2018/4/14.
 */

public class SentAuthorityContorller {

    private static Context context;

    public SentAuthorityContorller(Context context) {
        this.context = context;
    }
    /**
     * 授权门锁
     */
    public static void GiveLock(Map<String, RequestBody> map, List<MultipartBody.Part> parts, final InterfaceManger.OnRequestListener listener) {
        Call<ResponseBody> call = RetrofitUtils.getInstance().GiveLock(map, parts);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (listener == null) {
                    return;
                }
                if (!response.isSuccessful() || response == null) {
                    listener.onError(context.getString(R.string.server_error) + response.code());
                    return;
                }
                try {
                    String body = response.body().string();
                    JSONObject jsonObject = new JSONObject(body);
                    Log.e("onResponse", body);

                    int code = jsonObject.getInt("code");
                    Log.e("onResponse", String.valueOf(code));
                    if (code == 1) {
                        listener.onSuccess(body);
                    } else {
                        listener.onError(context.getString(R.string.impower_failed));
                    }
                } catch (Exception e) {
                    listener.onError(e.toString());
                    e.printStackTrace();
                }
                listener.onComplete();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (listener == null) {
                    return;
                }
                Log.e("onFailure", t.toString());
                if (t.toString().contains("ConnectException")) {
                    listener.onError(context.getString(R.string.no_internet));
                } else {
                    listener.onError(context.getString(R.string.network_anomaly));
                }
                listener.onComplete();
            }
        });
    }
}
