package com.esf.quagnitia.messaging_app.util;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.widget.Toast;

import com.esf.quagnitia.messaging_app.Activity.AutostartActivity;
import com.esf.quagnitia.messaging_app.Activity.MessageTabActivity;
import com.esf.quagnitia.messaging_app.Activity.SchoolActivity;
import com.esf.quagnitia.messaging_app.Storage.Preferences;

import java.util.List;

//import com.esf.quagnitia.messaging_app.Activity.WelcomeActivity;

/**
 * Created by Niki on 22-01-2019.
 */


public final class AutoStartHelper {

    private static AutoStartHelper instance;

    /***
     * Xiaomi
     */
    private final String BRAND_XIAOMI = "xiaomi";
    private final String PACKAGE_XIAOMI_MAIN = "com.miui.securitycenter";
    private final String PACKAGE_XIAOMI_COMPONENT = "com.miui.permcenter.autostart.AutoStartManagementActivity";

    /***
     * Letv
     */
    private final String BRAND_LETV = "letv";
    private final String PACKAGE_LETV_MAIN = "com.letv.android.letvsafe";
    private final String PACKAGE_LETV_COMPONENT = "com.letv.android.letvsafe.AutobootManageActivity";

    /***
     * Honor
     */
    private final String BRAND_HONOR = "honor";
    private final String PACKAGE_HONOR_MAIN = "com.huawei.systemmanager";
    private final String PACKAGE_HONOR_COMPONENT = "com.huawei.systemmanager.optimize.process.ProtectActivity";

    /**
     * Oppo
     */
    private final String BRAND_OPPO = "oppo";
    private final String PACKAGE_OPPO_MAIN = "com.coloros.safecenter";
    private final String PACKAGE_OPPO_FALLBACK = "com.oppo.safe";
    private final String PACKAGE_OPPO_COMPONENT = "com.coloros.safecenter.permission.startup.StartupAppListActivity";
    private final String PACKAGE_OPPO_COMPONENT_FALLBACK = "com.oppo.safe.permission.startup.StartupAppListActivity";
    private final String PACKAGE_OPPO_COMPONENT_FALLBACK_A = "com.coloros.safecenter.startupapp.StartupAppListActivity";

    /**
     * Vivo
     */

    private final String BRAND_VIVO = "vivo";
    private final String PACKAGE_VIVO_MAIN = "com.iqoo.secure";
    private final String PACKAGE_VIVO_FALLBACK = "com.vivo.permissionmanager";
    private final String PACKAGE_VIVO_COMPONENT = "com.iqoo.secure.ui.phoneoptimize.AddWhiteListActivity";
    private final String PACKAGE_VIVO_COMPONENT_FALLBACK = "com.vivo.permissionmanager.activity.BgStartUpManagerActivity";
    private final String PACKAGE_VIVO_COMPONENT_FALLBACK_A = "com.iqoo.secure.ui.phoneoptimize.BgStartUpManager";

    private AutoStartHelper() {
    }

    public static AutoStartHelper getInstance() {
        if (instance == null) {
            synchronized (AutoStartHelper.class) {
                if (instance == null) {
                    instance = new AutoStartHelper();
                }
            }
        }
        return instance;
    }

    private static boolean isPackageExists(Context context, String targetPackage) {
        List<ApplicationInfo> packages;
        PackageManager pm = context.getPackageManager();
        packages = pm.getInstalledApplications(0);
        for (ApplicationInfo packageInfo : packages) {
            if (packageInfo.packageName.equals(targetPackage)) {
                return true;
            }
        }
        return false;
    }

    public void getAutoStartPermission(Context context) {

        final String build_info = Build.BRAND.toLowerCase();

        switch (build_info) {

            case BRAND_XIAOMI:
                autoStartXiaomi(context);
                break;

            case BRAND_LETV:
                autoStartLetv(context);
                break;

            case BRAND_HONOR:
                autoStartHonor(context);
                break;

            case BRAND_OPPO:
                autoStartOppo(context);
                break;

            case BRAND_VIVO:
                autoStartVivo(context);
                break;

            default:
                Toast.makeText(context, "Unable to locate Autostart feature on this device.", Toast.LENGTH_SHORT).show();
                if(new Preferences(context).getString("UT").equalsIgnoreCase("admin")) {
                    Intent i = new Intent(context, SchoolActivity.class);
                    i.putExtra("FromLogin", true);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    context.startActivity(i);
                    ((AutostartActivity)context).finish();
                }else {
                    Intent i = new Intent(context, MessageTabActivity.class);
                    i.putExtra("FromLogin", true);
//                i.putExtra("schoolId",getIntent().getStringExtra("schoolId")+"");
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    context.startActivity(i);
                    ((AutostartActivity)context).finish();
                }
//                Intent i = new Intent(context, MessageListActivity.class);
//                i.putExtra("FromLogin", true);
//                i.putExtra("schoolId", new Preferences(context).getString("schoolId"));
//
//                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                context.startActivity(i);
//                ((AutostartActivity)context).finish();
                break;
        }

    }

    private void autoStartXiaomi(Context context) {
        if (isPackageExists(context, PACKAGE_XIAOMI_MAIN)) {
            try {
                startIntent(context, PACKAGE_XIAOMI_MAIN, PACKAGE_XIAOMI_COMPONENT);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void autoStartLetv(Context context) {
        if (isPackageExists(context, PACKAGE_LETV_MAIN)) {
            try {
                startIntent(context, PACKAGE_LETV_MAIN, PACKAGE_LETV_COMPONENT);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void autoStartHonor(Context context) {
        if (isPackageExists(context, PACKAGE_HONOR_MAIN)) {
            try {
                startIntent(context, PACKAGE_HONOR_MAIN, PACKAGE_HONOR_COMPONENT);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void autoStartOppo(Context context) {
        if (isPackageExists(context, PACKAGE_OPPO_MAIN) || isPackageExists(context, PACKAGE_OPPO_FALLBACK)) {
            try {
                startIntent(context, PACKAGE_OPPO_MAIN, PACKAGE_OPPO_COMPONENT);
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    startIntent(context, PACKAGE_OPPO_FALLBACK, PACKAGE_OPPO_COMPONENT_FALLBACK);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    try {
                        startIntent(context, PACKAGE_OPPO_MAIN, PACKAGE_OPPO_COMPONENT_FALLBACK_A);
                    } catch (Exception exx) {
                        exx.printStackTrace();
                    }
                }
            }
        }
    }

    private void autoStartVivo(Context context) {
        if (isPackageExists(context, PACKAGE_VIVO_MAIN) || isPackageExists(context, PACKAGE_VIVO_FALLBACK)) {
            try {
                startIntent(context, PACKAGE_VIVO_MAIN, PACKAGE_VIVO_COMPONENT);
            } catch (Exception e) {
                e.printStackTrace();
                try {
                    startIntent(context, PACKAGE_VIVO_FALLBACK, PACKAGE_VIVO_COMPONENT_FALLBACK);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    try {
                        startIntent(context, PACKAGE_VIVO_MAIN, PACKAGE_VIVO_COMPONENT_FALLBACK_A);
                    } catch (Exception exx) {
                        exx.printStackTrace();
                    }
                }
            }
        }
    }

    private void startIntent(Context context, String packageName, String componentName) throws Exception {
        try {
            Intent intent = new Intent();
            intent.setComponent(new ComponentName(packageName, componentName));
            context.startActivity(intent);
        } catch (Exception exception) {
            exception.printStackTrace();
            throw exception;
        }
    }
}