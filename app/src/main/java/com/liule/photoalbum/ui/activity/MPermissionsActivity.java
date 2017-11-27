package com.liule.photoalbum.ui.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;


import com.liule.photoalbum.R;

import java.util.ArrayList;
import java.util.List;



/**
 * @author 刺雒
 * @time 2017/10/29
 */
public abstract class MPermissionsActivity extends AppCompatActivity {
    protected int REQUEST_CODE_PERMISSION = 0x00099;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     *检测所有的权限是否都已经授权
     *@param permissions 申请的所有权限列表
     */
    private boolean checkPermissions(String[] permissions){
        //如果当前sdk小于android6.0
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            return true;
        }

        for(String permission:permissions){
            //判断这个权限是否已经给了设备
            if(ContextCompat.checkSelfPermission(this,permission) !=
                    PackageManager.PERMISSION_GRANTED){
                return false;
            }
        }
        return true;
    }

    /**
     *获取权限集中需要申请的权限
     *@param permissions 需要申请的权限列表
     */
    private List<String> getDeniedPermissions(String[] permissions){
        List<String> needRequestPermissionsList = new ArrayList<>();
        for(String permission:permissions){
            if(ContextCompat.checkSelfPermission(this,permission) != PackageManager.PERMISSION_GRANTED
                    || ActivityCompat.shouldShowRequestPermissionRationale(this,permission)){
                //判断是否需要 向用户解释，为什么要申请该权限
                needRequestPermissionsList.add(permission);
            }
        }
        return needRequestPermissionsList;
    }

    /**
     * 请求权限
     * @param requestCode 请求返回码
     */
    protected void requestPermission(String[] permissions,int requestCode){
        this.REQUEST_CODE_PERMISSION = requestCode;
        //如果权限申请成功
        if(checkPermissions(permissions)) {
            permissionSuccess(requestCode);
        } else {
            List<String> needPermissions = getDeniedPermissions(permissions);
            //申请权限
            ActivityCompat.requestPermissions(this,needPermissions.toArray(new String[needPermissions.size()]),REQUEST_CODE_PERMISSION);
        }
    }

    /**
     * 处理权限请求回调
     *
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        Log.e("faf0",checkPermissions(permissions)+"  "+requestCode+"  " +REQUEST_CODE_PERMISSION+"sda");

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_CODE_PERMISSION){
            //如果所有权限都开启
            if(verifyPermissions(grantResults)){
                permissionSuccess(REQUEST_CODE_PERMISSION);
            }
            //如果有权限没有被授权
            else {
                permissionFail(REQUEST_CODE_PERMISSION);
                showTipsDialog();
            }
        }
    }

    /**
     * 确认所有的权限是否都已授权
     *
     */
    private boolean verifyPermissions(int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * 获取权限成功
     *
     */
    public abstract void permissionSuccess(int requestCode) ;


    /**
     * 权限获取失败
     */
    public abstract void permissionFail(int requestCode) ;



    /**
     * 显示提示对话框
     */
    private void showTipsDialog() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.prompt_message))
                .setMessage(getString(R.string.apply_permissions_msg))
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setPositiveButton(getString(R.string.sure), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startAppSettings();
                    }
                }).show();
    }

    /**
     * 启动当前应用设置页面
     *
     */
    private void startAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivity(intent);
    }

}
