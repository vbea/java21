package com.vbea.java21.bridge;

import com.vbea.java21.BaseActivity;
import com.vbea.java21.R;
import com.vbea.java21.ui.AdultPreview;
import com.vbes.util.VbeUtil;

/**
 * Created by Vbe on 2022/12/30.
 */
public class Spam1 extends BaseActivity {
    @Override
    protected void before() {
        setContentView(R.layout.welcome);
    }

    @Override
    protected void after() {
        VbeUtil.runDelayed(new Runnable() {
            @Override
            public void run() {
                VbeUtil.startActivityOptions(Spam1.this, AdultPreview.class);
            }
        }, 2000);
    }
}
