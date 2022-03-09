package com.vbea.java21.ui;

import android.graphics.Bitmap;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.vbea.java21.BaseActivity;
import com.vbea.java21.R;
import com.vbes.util.VbeUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Vbe on 2021/12/6.
 */
public class QRGenerateActivity extends BaseActivity {

    private EditText txtGenerate;
    private ImageView imgQRCode;
    private InputMethodManager imm;

    @Override
    protected void before() {
        setContentView(R.layout.qr_creator);
    }

    @Override
    protected void after() {
        enableBackButton(R.id.toolbar);
        txtGenerate = bind(R.id.txt_generator);
        imgQRCode = bind(R.id.img_creator);
        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        bind(R.id.btn_generator).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateQRCode();
            }
        });
    }

    private void generateQRCode() {
        String content = txtGenerate.getText().toString();
        if (VbeUtil.isNullOrEmpty(content)) {
            toastShortMessage(R.string.tips_qr_creator);
            return;
        }
        Bitmap bitmap = generateBitmap(content, 500, 500);
        if (bitmap != null) {
            imgQRCode.setImageBitmap(bitmap);
        }
        if (imm != null) {
            imm.hideSoftInputFromWindow(txtGenerate.getWindowToken(), 0);
        }
    }

    private Bitmap generateBitmap(String content, int width, int height) {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        Map<EncodeHintType, String> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        try {
            BitMatrix encode = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, width, height, hints);
            int[] pixels = new int[width * height];
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    if (encode.get(j, i)) {
                        pixels[i * width + j] = 0x00000000;
                    } else {
                        pixels[i * width + j] = 0xffffffff;
                    }
                }
            }
            return Bitmap.createBitmap(pixels, 0, width, width, height, Bitmap.Config.RGB_565);
        } catch (WriterException e) {
            e.printStackTrace();
            toastShortMessage("生成失败!");
        }
        return null;
    }
}
