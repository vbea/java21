package com.vbea.java21.ui;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.vbea.java21.BaseActivity;
import com.vbea.java21.R;
import com.vbes.util.GalleryUtil;
import com.vbes.util.VbeUtil;
import com.vbes.util.media.MimeType;

import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.ZXingView;
//import cn.bingoogolapple.qrcode.zbar.ZBarView;

/**
 * Created by Vbe on 2020/3/19.
 */
public class QRScannerActivity extends BaseActivity implements QRCodeView.Delegate {
    //private ZBarView zBarView;
    private ZXingView zBarView;
    @Override
    protected void before() {
        setContentView(R.layout.qr_scanner);
        zBarView = bind(R.id.zBarview);
    }

    @Override
    protected void after() {
        enableBackButton();
        zBarView.setDelegate(this);
        bind(R.id.qr_gallery).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GalleryUtil.from(QRScannerActivity.this).choose(MimeType.ofImage()).theme(MyThemes.getTheme()).thumbnailScale(0.85f).forResult(120);
            }
        });
    }

    private void init() {
        if (VbeUtil.hasAllPermissions(this, Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            zBarView.startCamera();
            zBarView.startSpot();
            //mHandler.sendEmptyMessageDelayed(1, 200);
        } else {
            VbeUtil.requestPermission(this, 100, Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE);
        }
    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(100);
    }

    private void showResult(String result) {
        if (result.contains("://")) {
            if (result.contains("http://") || result.contains("https://") || result.contains("vbea://")) {
                Intent intent = new Intent(QRScannerActivity.this, HtmlViewer.class);
                intent.setAction(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(result));
                startActivity(intent);
            } else {
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                    intent.setData(Uri.parse(result));
                    startActivity(intent);
                } catch (Exception e) {
                    Intent intent = new Intent(QRScannerActivity.this, QRResultActivity.class);
                    intent.putExtra(QRResultActivity.QR_RESULT, result);
                    startActivity(intent);
                }
            }
        } else {
            Intent intent = new Intent(QRScannerActivity.this, QRResultActivity.class);
            intent.putExtra(QRResultActivity.QR_RESULT, result);
            startActivity(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        init();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 120) {
            zBarView.decodeQRCode(GalleryUtil.obtainPathResult(data).get(0));
        }
    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        if (VbeUtil.isNullOrEmpty(result)) {
            toastShortMessage("无法识别");
            zBarView.startSpot();
            return;
        }
        vibrate();
        showResult(result);
                /*showLoading(R.string.waiting);
                Message msg = new Message();
                msg.what = 3;
                msg.obj = result;
                mHandler.sendMessageDelayed(msg, 1000);*/
    }

    @Override
    public void onCameraAmbientBrightnessChanged(boolean isDark) {

    }

    @Override
    public void onScanQRCodeOpenCameraError() {

    }

    @Override
    protected void onStart() {
        super.onStart();
        init();
    }

    @Override
    protected void onStop() {
        zBarView.stopCamera();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        zBarView.onDestroy();
        //mHandler.removeCallbacksAndMessages(null);
        super.onDestroy();
    }

    /*@SuppressLint("HandlerLeak")
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    qrLayer.setVisibility(View.GONE);
                    break;
                case 3:
                    hideLoading();
                    showResult(msg.obj.toString());
                    break;
            }
            super.handleMessage(msg);
        }
    };*/
}
