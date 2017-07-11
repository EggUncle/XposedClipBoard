package com.example.egguncle.xposedclipboard.hook;

import android.app.Activity;
import android.content.Intent;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Created by egguncle on 17-7-11.
 */

public class HookUtil implements IXposedHookLoadPackage {
    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        Class<?> activityClass = lpparam.classLoader.loadClass("android.app.Activity");
        XposedHelpers.findAndHookMethod(activityClass, "onActionModeStarted", ActionMode.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(final MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                Activity nowAct = (Activity) param.thisObject;
                View focusView = nowAct.getCurrentFocus();
                EditText ed = null;
                if (focusView instanceof EditText) {

                } else {
                    return;
                }
                ActionMode mode = (ActionMode) param.args[0];
                Menu menu = mode.getMenu();
                if (menu != null) {
                    XposedBridge.log("---------add clipboard-----------");
                    menu.add("剪贴板").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            if (param.thisObject instanceof Activity) {
                                Activity nowAct = (Activity) param.thisObject;
                                View focusView = nowAct.getCurrentFocus();
                                EditText ed = null;
                                if (focusView instanceof EditText) {
                                    ed = (EditText) focusView;
                                }
                                Intent intent = new Intent("com.example.egguncle.xposedtest.ProcessTextActivity");
                                intent.setType("text/plain");
                                if (ed != null) {
                                    int start = ed.getSelectionStart();
                                    int end = ed.getSelectionEnd();
                                    intent.putExtra(Intent.EXTRA_PROCESS_TEXT, ed.getText().toString());
                                    intent.putExtra("select_start", start);
                                    intent.putExtra("select_end", end);
                                }
                                nowAct.startActivityForResult(intent, 0);
                            }
                            return true;
                        }
                    });
                }
            }
        });

        XposedHelpers.findAndHookMethod(activityClass, "onActivityResult", int.class, int.class, Intent.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                XposedBridge.log("onActivityResult---");
                if (param.thisObject instanceof Activity) {
                    XposedBridge.log("Activity---");
                    Activity nowAct = (Activity) param.thisObject;
                    View focusView = nowAct.getCurrentFocus();
                    Intent data = (Intent) param.args[2];
                    if (data == null) {
                        XposedBridge.log("data---");
                        return;
                    }
                    if (focusView instanceof EditText && data instanceof Intent) {
                        XposedBridge.log("focusView---");
                        EditText e = (EditText) focusView;
                        String s = data.getStringExtra(Intent.EXTRA_PROCESS_TEXT);
                        e.setText(s);
                    } else {
                    }
                }
            }

        });
    }
}
