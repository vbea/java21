package com.vbea.java21.ui;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.TextView;

import com.vbea.java21.BaseActivity;
import com.vbea.java21.R;
import com.vbes.util.VbeUtil;

/**
 * Created by Vbe on 2020/3/20.
 */
public class QRResultActivity extends BaseActivity {
    String result;
    public static final String QR_RESULT = "qr_result";
    @Override
    protected void before() {
        setContentView(R.layout.qr_result);
    }

    @Override
    protected void after() {
        enableBackButton();
        result = getIntent().getStringExtra(QR_RESULT);
        TextView textView = bind(R.id.txt_qr_result);
        if (!VbeUtil.isNullOrEmpty(result)) {
            textView.setText(result);
            bind(R.id.btn_qr_search).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showSearch();
                }
            });
            bind(R.id.btn_qr_copy).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    VbeUtil.addClipboard(getApplicationContext(), result);
                    toastShortMessage("已复制到剪贴板");
                }
            });
        }
    }

    private void showSearch() {
        Intent intent = new Intent(QRResultActivity.this, HtmlViewer.class);
        intent.setAction(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(result));
        VbeUtil.startActivityOptions(QRResultActivity.this, intent);
        VbeUtil.runDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 500);
    }
}
