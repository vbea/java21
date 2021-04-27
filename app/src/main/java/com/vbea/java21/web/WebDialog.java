package com.vbea.java21.web;

import android.app.Activity;
import android.content.DialogInterface;
import android.net.http.SslCertificate;
import android.net.http.SslError;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.HttpAuthHandler;
import android.webkit.SslErrorHandler;
import android.widget.EditText;
import android.widget.TextView;

import com.vbea.java21.R;
import com.vbea.java21.ui.HtmlViewer;
import com.vbes.util.VbeUtil;
import com.vbes.util.view.MyAlertDialog;

/**
 * Created by Vbe on 2021/1/18.
 */
public class WebDialog {

    private static MyAlertDialog certDialog;

    public static void showSecurityDialog(Activity activity, SslErrorHandler h, SslError e) {
        String message = "该网站证书无效，确认继续？";
        switch (e.getPrimaryError()) {
            case SslError.SSL_EXPIRED:
                message = "该网站证书已过期，确定继续？";
                break;
            case SslError.SSL_IDMISMATCH:
                message = "该网站的名称与证书上的名称不一致，确定继续？";
                break;
            case SslError.SSL_UNTRUSTED:
                message = "该网站证书不被信任，确认继续？";
                break;
            case SslError.SSL_DATE_INVALID:
                message = "该网站证书日期无效，确认继续？";
                break;
            case SslError.SSL_INVALID:
                message = "该网站证书不可用，确认继续？";
                break;
        }
        showSecurityDialog(activity, message, e.getCertificate(), h);
    }

    private static void showSecurityDialog(Activity activity, final String msg, final SslCertificate sert, final SslErrorHandler handle) {
        if (certDialog == null) {
            certDialog = new MyAlertDialog(activity);
            certDialog.setTitle("安全警告");
            certDialog.setMessage(msg);
            certDialog.setCancelable(false);
            certDialog.setPositiveButton("继续", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface d, int s) {
                    handle.proceed();
                }
            });
            certDialog.setNegativeButton("取消", null);
            certDialog.setNeutralButton("查看证书", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface d, int s) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("颁发给：");
                    sb.append(sert.getIssuedTo().getCName());
                    sb.append("\n");
                    sb.append("颁发者：");
                    sb.append(sert.getIssuedBy().getCName());
                    sb.append("\n有效期：");
                    sb.append(sert.getValidNotBefore());
                    sb.append("\n至");
                    sb.append(sert.getValidNotAfter());
                    VbeUtil.showResultDialog(activity, sb.toString(), "安全证书", "确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface d, int s) {
                            showSecurityDialog(activity, msg, sert, handle);
                        }
                    });
                }
            });
            certDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialogInterface) {
                    certDialog = null;
                }
            });
            certDialog.show();
        }
    }

    public static void showWebAuthDialog(Activity activity, String host, final HttpAuthHandler httpAuthHandler) {
        LayoutInflater inflater = activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.web_auth_dialog, null, false);
        EditText txtUsername = view.findViewById(R.id.auth_username);
        EditText txtPassword = view.findViewById(R.id.auth_password);
        txtUsername.setText("");
        txtPassword.setText("");
        MyAlertDialog alertDialog = new MyAlertDialog(activity);
        alertDialog.setTitle(host+"请求用户名和密码");
        alertDialog.setView(view);
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (httpAuthHandler != null) {
                    httpAuthHandler.proceed(txtUsername.getText().toString(), txtPassword.getText().toString());
                }
            }
        });
        alertDialog.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (httpAuthHandler != null) {
                    httpAuthHandler.cancel();
                }
            }
        });
        alertDialog.show();
    }

}
