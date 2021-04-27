package com.vbea.java21.web;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;

import com.vbea.java21.classes.Common;
import com.vbea.java21.classes.ExceptionHandler;
import com.vbea.java21.classes.Util;
import com.vbea.java21.ui.HtmlViewer;
import com.vbes.util.view.MyAlertDialog;

import java.io.File;

/**
 * Created by Vbe on 2021/3/11.
 */
public class WebUploadUtil {

    private Activity activity;
    private Uri cameraUri;
    private String[] headItems = {"拍照", "相册"};
    private ValueCallback<Uri> mUploadMessage;
    private ValueCallback<Uri[]> mUploadMessages;

    public static final int CHOOSER_FILE = 501;
    public static final int CHOOSE_CAMERA = 502;

    public WebUploadUtil(Activity act) {
        activity = act;
    }

    public void selectImage() {
        MyAlertDialog dialogBuild = new MyAlertDialog(activity);
        dialogBuild.setItems(headItems, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                dialog.dismiss();
                switch (item) {
                    case 0:
                        if (Util.isAndroidN()) {
                            if (!Util.hasAllPermissions(activity, Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE))
                                Util.requestPermission(activity, 1001, Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                            else
                                startCamera();
                        } else
                            startCamera();
                        break;
                    case 1:
                        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                        intent.setType("image/*");
                        Intent intent2 = Intent.createChooser(intent, "选择图片");
                        activity.startActivityForResult(intent2, CHOOSER_FILE);
                        break;
                }
            }
        });
        dialogBuild.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface d) {
                if (mUploadMessages != null) {
                    //Uri[] uris = new Uri[1];
                    //uris[0] = Uri.parse("");
                    mUploadMessages.onReceiveValue(null);
                    mUploadMessages = null;
                } else if (mUploadMessage != null) {
                    mUploadMessage.onReceiveValue(Uri.parse(""));
                    mUploadMessage = null;
                }
            }
        });
        dialogBuild.show();
    }

    public void startCamera() {
        try {
            Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File file = new File(Common.getTempImagePath(activity));
            cameraUri = FileProvider.getUriForFile(activity, Common.FileProvider, file);
            intent1.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri);
            activity.startActivityForResult(intent1, CHOOSE_CAMERA);
        } catch (Exception e) {
            ExceptionHandler.log("startCamera_1", e.toString());
        }
    }

    public void onFileChoose(ValueCallback<Uri[]> filePathCallback, WebChromeClient.FileChooserParams fileChooserParams) {
        if (mUploadMessages != null)
            mUploadMessages.onReceiveValue(null);
        mUploadMessages = filePathCallback;
        String type = "*/*";
        if (fileChooserParams != null && fileChooserParams.getAcceptTypes() != null && fileChooserParams.getAcceptTypes().length > 0)
            type = fileChooserParams.getAcceptTypes()[0];
        type = Util.isNullOrEmpty(type) ? "*/*" : type;
        if (type.equals("image/*"))
            selectImage();
        else {
            Intent i = new Intent(Intent.ACTION_GET_CONTENT);
            i.addCategory(Intent.CATEGORY_OPENABLE);
            i.setType(type);
            activity.startActivityForResult(Intent.createChooser(i, "文件上传"), CHOOSER_FILE);
        }
    }

    public void onReceiveValue(Uri uri) {
        if (uri == null)
            uri = Uri.parse("");
        if (mUploadMessage != null)
            mUploadMessage.onReceiveValue(uri);
        if (mUploadMessages != null)
            mUploadMessages.onReceiveValue(new Uri[]{uri});
        mUploadMessage = null;
        mUploadMessages = null;
    }

    public void chooseCamera() {
        if (mUploadMessage != null) {
            File f = new File(Common.getTempImagePath(activity));
            if (!f.exists())
                cameraUri = Uri.parse("");
            else {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, f.getAbsolutePath());
                values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
                activity.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            }
            onReceiveValue(cameraUri);
        }
    }

    public void chooseFile(Intent data) {
        if (mUploadMessage != null && data != null)
            onReceiveValue(data.getData());
    }
}
