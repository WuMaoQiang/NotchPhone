package dpi.myapplication;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {
    private Boolean isNotch = false;// 是否为刘海屏
    private int type;
    RelativeLayout mRlall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//状态栏黑色字体
        requestWindowFeature(Window.FEATURE_NO_TITLE);// 隐藏标题

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);// 设置全屏

        setContentView(R.layout.activity_main);

        mRlall = findViewById(R.id.rl_all);

        String deviceBrand = NotchPhoneUtils.getDeviceBrand(); //获取手机厂商
        //判断相应手机是否有刘海屏
        if ("vivo".equals(deviceBrand)) {
            isNotch = NotchPhoneUtils.HasNotchVivo(MainActivity.this);
            type = 1;
        } else if ("HUAWEI".equals(deviceBrand)) {
            isNotch = NotchPhoneUtils.hasNotchAtHuawei(MainActivity.this);
            type = 2;
        } else if ("OPPO".equals(deviceBrand)) {
            isNotch = NotchPhoneUtils.HasNotchOPPO(MainActivity.this);
            type = 3;
        } else if ("Xiaomi".equals(deviceBrand)) {
            isNotch = NotchPhoneUtils.HasNotchXiaoMi();
            type = 4;
        }

        NotchPhoneUtils.onConfigurationChanged(MainActivity.this, isNotch, type, mRlall);
    }

    //屏幕方向发生改变的回调方法
    @Override
    public void onConfigurationChanged(Configuration newConfig) {

        NotchPhoneUtils.onConfigurationChanged(MainActivity.this, isNotch, type, mRlall);
        super.onConfigurationChanged(newConfig);
    }


}
    