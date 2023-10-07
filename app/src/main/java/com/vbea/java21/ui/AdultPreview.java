package com.vbea.java21.ui;

import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.WindowManager;

import com.vbea.java21.BaseActivity;
import com.vbea.java21.R;
import com.vbea.java21.adapter.PreviewAdapter;

/**
 * Created by Vbe on 2022/12/30.
 */
public class AdultPreview extends BaseActivity {
    @Override
    protected void before() {
        setContentView(R.layout.gallery_preview);
        View view = getWindow().getDecorView();
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN;
        view.setSystemUiVisibility(uiOptions);
    }

    @Override
    protected void after() {
        ViewPager viewPager = bind(R.id.viewpager);
        bind(R.id.gp_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAfterTransition();
            }
        });

        PreviewAdapter adapter = new PreviewAdapter(this);
        adapter.addImage(R.drawable.icon_av_01);
        adapter.addImage(R.drawable.icon_av_02);
        adapter.addImage(R.drawable.icon_av_03);
        adapter.addImage(R.drawable.icon_av_04);
        adapter.addImage(R.drawable.icon_av_05);
        adapter.addImage(R.drawable.icon_av_06);
        adapter.addImage(R.drawable.icon_av_07);
        adapter.addImage(R.drawable.icon_az_01);
        adapter.addImage(R.drawable.icon_az_02);
        adapter.addImage(R.drawable.icon_az_03);
        adapter.addImage(R.drawable.icon_az_04);
        adapter.addImage(R.drawable.icon_az_05);
        adapter.addImage(R.drawable.icon_az_06);
        adapter.addImage(R.drawable.icon_az_07);
        adapter.addImage(R.drawable.icon_az_08);
        viewPager.setAdapter(adapter);
    }
}
