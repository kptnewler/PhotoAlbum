package com.liule.photoalbum.ui.dialog;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * @author 刺雒
 * @time 2017/10/30 
 */

public class LoadingProgressDialog extends ProgressDialog{
    public LoadingProgressDialog(Context context,String title,String text) {
        super(context);
        this.setTitle(title);
        this.setMessage(text);
        setProgress(ProgressDialog.STYLE_HORIZONTAL);
    }
}
