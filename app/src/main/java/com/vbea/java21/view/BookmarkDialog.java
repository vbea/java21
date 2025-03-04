package com.vbea.java21.view;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.vbea.java21.R;
import com.vbea.java21.data.BookMark;
import com.vbes.util.VbeUtil;

/**
 * Created by Vbe on 2022/9/19.
 */
public class BookmarkDialog {
    public static void showAddDialog(Activity activity, BookMark source, final CallBack callBack) {
        showDialog(activity, "添加书签", "", source, callBack);
    }

    public static void showEditDialog(Activity activity, BookMark source, final CallBack callBack) {
        showDialog(activity, "修改书签", source.getId(), source, callBack);
    }

    public static void showDialog(Activity activity, String title, String id, BookMark source, final CallBack callBack) {
        View view = LayoutInflater.from(activity).inflate(R.layout.dialog_bookmark, null);
        EditText editTitle = view.findViewById(R.id.bookmark_edit_title);
        EditText editUrl = view.findViewById(R.id.bookmark_edit_url);
        editTitle.setText(source.getTitle());
        editUrl.setText(source.getUrl());
        new AlertDialog.Builder(activity)
        .setTitle(title).setView(view)
        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                BookMark bookMark = new BookMark();
                bookMark.setTitle(editTitle.getText().toString());
                bookMark.setUrl(editUrl.getText().toString());
                if (callBack != null) {
                    callBack.onCallback(id, bookMark);
                }
            }
        }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).show();
    }

    public interface CallBack {
        void onCallback(String id, BookMark target);
    }
}
