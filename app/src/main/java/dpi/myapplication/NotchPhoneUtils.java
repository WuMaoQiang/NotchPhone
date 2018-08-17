package dpi.myapplication;

import android.app.Activity;
import android.content.Context;
import android.os.SystemProperties;
import android.util.Log;
import android.view.Surface;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.lang.reflect.Method;

/**
 * @version V
 * @类描述：
 * @项目名称：MyApplicationliuhai
 * @包名称：dpi.myapplication
 * @创建人：cc
 * @创建时间：2018/7/17
 * @修改人：
 * @修改时间：
 * @修改备注：TODO
 */

public class NotchPhoneUtils {



    /**
     * 华为手机判断是不是刘海手机
     *
     * @param context
     * @return
     */
    public static boolean hasNotchAtHuawei(Context context) {
        boolean ret = false;
        try {
            ClassLoader classLoader = context.getClassLoader();
            Class HwNotchSizeUtil = classLoader.loadClass("com.huawei.android.util.HwNotchSizeUtil");
            Method get = HwNotchSizeUtil.getMethod("hasNotchInScreen");
            ret = (boolean) get.invoke(HwNotchSizeUtil);
        } catch (ClassNotFoundException e) {
            Log.e("Notch", "hasNotchAtHuawei ClassNotFoundException");
        } catch (NoSuchMethodException e) {
            Log.e("Notch", "hasNotchAtHuawei NoSuchMethodException");
        } catch (Exception e) {
            Log.e("Notch", "hasNotchAtHuawei Exception");
        } finally {
            return ret;
        }
    }


    /**
     * 华为手机获取刘海的宽高
     * int[0]值为刘海宽度 int[1]值为刘海高度
     */
    public static int[] getNotchSizeAtHuawei(Context context) {
        int[] ret = new int[]{0, 0};
        try {
            ClassLoader cl = context.getClassLoader();
            Class HwNotchSizeUtil = cl.loadClass("com.huawei.android.util.HwNotchSizeUtil");
            Method get = HwNotchSizeUtil.getMethod("getNotchSize");
            ret = (int[]) get.invoke(HwNotchSizeUtil);
        } catch (ClassNotFoundException e) {
            Log.e("Notch", "getNotchSizeAtHuawei ClassNotFoundException");
        } catch (NoSuchMethodException e) {
            Log.e("Notch", "getNotchSizeAtHuawei NoSuchMethodException");
        } catch (Exception e) {
            Log.e("Notch", "getNotchSizeAtHuawei Exception");
        } finally {
            return ret;
        }
    }


    /**
     * OPPO判断是不是刘海手机,
     * OPPO不提供接口获取刘海尺寸，目前其有刘海屏的机型尺寸规格都是统一的。不排除以后机型会有变化。
     * 刘海区域则都是宽度为324px, 高度为80px。
     *
     * @param context
     * @return
     */
    public static boolean HasNotchOPPO(Context context) {
        return context.getPackageManager().hasSystemFeature("com.oppo.feature.screen.heteromorphism");
    }


    public static final int VIVO_NOTCH = 0x00000020;//是否有刘海

    /**
     * vivo判断是不是刘海手机
     */
    public static boolean HasNotchVivo(Context context) {
        boolean ret = false;
        try {
            ClassLoader classLoader = context.getClassLoader();
            Class FtFeature = classLoader.loadClass("android.util.FtFeature");
            Method method = FtFeature.getMethod("isFeatureSupport", int.class);
            ret = (boolean) method.invoke(FtFeature, VIVO_NOTCH);
        } catch (ClassNotFoundException e) {
            Log.e("Notch", "hasNotchAtVivo ClassNotFoundException");
        } catch (NoSuchMethodException e) {
            Log.e("Notch", "hasNotchAtVivo NoSuchMethodException");
        } catch (Exception e) {
            Log.e("Notch", "hasNotchAtVivo Exception");
        } finally {
            return ret;
        }

    }

    /**
     * 小米手机判断是不是刘海手机
     *
     * @return
     */

    public static boolean HasNotchXiaoMi() {
        return SystemProperties.getInt("ro.miui.notch", 0) == 1 ? true : false;
    }

    /**
     * 小米手机获取刘海的高度
     */
    public static int getStatusBarHeight(Context context) {
        int statusBarHeight = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = context.getResources().getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }

    /**
     * 屏幕旋转会走的方法
     *
     * @param activity
     * @param isNotch
     * @param type
     * @param viewGroup
     */
    public static void onConfigurationChanged(Activity activity, Boolean isNotch, int type, ViewGroup viewGroup) {
        if (getDisplayRotation(activity) == 0) {
            if (isNotch) {
                switch (type) {
                    case 1:   //vivo
                        FrameLayout.LayoutParams lpvivo = (FrameLayout.LayoutParams) viewGroup.getLayoutParams();
                        lpvivo.topMargin = dp2px(activity, 32);
                        lpvivo.leftMargin = 0;
                        lpvivo.rightMargin = 0;
                        viewGroup.setLayoutParams(lpvivo);
                        break;
                    case 2: //HUAWEI

                        int[] sizeAtHuawei = NotchPhoneUtils.getNotchSizeAtHuawei(activity);
                        FrameLayout.LayoutParams lphuawei = (FrameLayout.LayoutParams) viewGroup.getLayoutParams();
                        lphuawei.topMargin = sizeAtHuawei[1];
                        lphuawei.leftMargin = 0;
                        lphuawei.rightMargin = 0;
                        viewGroup.setLayoutParams(lphuawei);


                        break;
                    case 3:  //OPPO  目前都为 80px
                        FrameLayout.LayoutParams lpOppo = (FrameLayout.LayoutParams) viewGroup.getLayoutParams();
                        lpOppo.topMargin = 80;
                        lpOppo.leftMargin = 0;
                        lpOppo.rightMargin = 0;
                        viewGroup.setLayoutParams(lpOppo);


                        break;
                    case 4:  //Xiaomi
                        int sizeAtXiaomi = NotchPhoneUtils.getStatusBarHeight(activity);
                        FrameLayout.LayoutParams lpXiaomi = (FrameLayout.LayoutParams) viewGroup.getLayoutParams();
                        lpXiaomi.topMargin = sizeAtXiaomi;
                        lpXiaomi.leftMargin = 0;
                        lpXiaomi.rightMargin = 0;
                        viewGroup.setLayoutParams(lpXiaomi);

                        break;
                }

            }

        } else if (getDisplayRotation(activity) == 90) {
            leftAndRightChange(activity, isNotch, type, viewGroup);
        } else if (getDisplayRotation(activity) == 180) {
        } else if (getDisplayRotation(activity) == 270) {
            leftAndRightChange(activity, isNotch, type, viewGroup);
        }


    }

    /**
     * 左右横屏都是让 leftMargin  和rightMargin 空出一个刘海的距离
     *
     * @param activity
     * @param isNotch
     * @param type
     * @param viewGroup
     */
    private static void leftAndRightChange(Activity activity, Boolean isNotch, int type, ViewGroup viewGroup) {
        if (isNotch) {
            switch (type) {
                case 1:   //vivo
                    FrameLayout.LayoutParams lpvivo = (FrameLayout.LayoutParams) viewGroup.getLayoutParams();
                    lpvivo.leftMargin = dp2px(activity, 32);
                    lpvivo.rightMargin = dp2px(activity, 32);
                    lpvivo.topMargin = 0;
                    lpvivo.bottomMargin = 0;
                    viewGroup.setLayoutParams(lpvivo);
                    break;
                case 2: //HUAWEI

                    int[] sizeAtHuawei = NotchPhoneUtils.getNotchSizeAtHuawei(activity);
                    FrameLayout.LayoutParams lphuawei = (FrameLayout.LayoutParams) viewGroup.getLayoutParams();
                    lphuawei.leftMargin = sizeAtHuawei[1];
                    lphuawei.rightMargin = sizeAtHuawei[1];
                    lphuawei.topMargin = 0;
                    lphuawei.bottomMargin = 0;
                    viewGroup.setLayoutParams(lphuawei);

                    break;
                case 3:  //OPPO  目前都为 80px
                    FrameLayout.LayoutParams lpOppo = (FrameLayout.LayoutParams) viewGroup.getLayoutParams();
                    lpOppo.leftMargin = 80;
                    lpOppo.rightMargin = 80;
                    lpOppo.topMargin = 0;
                    lpOppo.bottomMargin = 0;
                    viewGroup.setLayoutParams(lpOppo);

                    break;
                case 4:  //Xiaomi
                    int sizeAtXiaomi = NotchPhoneUtils.getStatusBarHeight(activity);
                    FrameLayout.LayoutParams lpXiaomi = (FrameLayout.LayoutParams) viewGroup.getLayoutParams();
                    lpXiaomi.leftMargin = sizeAtXiaomi;
                    lpXiaomi.rightMargin = sizeAtXiaomi;
                    lpXiaomi.topMargin = 0;
                    lpXiaomi.bottomMargin = 0;
                    viewGroup.setLayoutParams(lpXiaomi);
                    break;
            }

        }
    }

    /**
     * 获取当前屏幕旋转角度
     *
     * @param activity
     * @return 0表示是竖屏; 90表示是左横屏; 180表示是反向竖屏; 270表示是右横屏
     */
    public static int getDisplayRotation(Activity activity) {
        if (activity == null)
            return 0;

        int rotation = activity.getWindowManager().getDefaultDisplay()
                .getRotation();
        switch (rotation) {
            case Surface.ROTATION_0:
                return 0;
            case Surface.ROTATION_90:
                return 90;
            case Surface.ROTATION_180:
                return 180;
            case Surface.ROTATION_270:
                return 270;
        }
        return 0;
    }

    /**
     * px转dp
     *
     * @param context
     * @param dipValue
     */
    public static int dp2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 获取手机厂商
     *
     * @return 手机厂商   Xiaomi   HUAWEI   vivo
     */
    public static String getDeviceBrand() {
        return android.os.Build.BRAND;
    }

}
