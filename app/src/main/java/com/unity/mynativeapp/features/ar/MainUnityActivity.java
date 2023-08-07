package com.unity.mynativeapp.features.ar;

import android.content.Intent;
import android.os.Bundle;
import com.company.product.OverrideUnityActivity;


public class MainUnityActivity extends OverrideUnityActivity {
    @Override
    protected void showMainActivity(String setToColor) {

    }

    // Setup activity layout
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        handleIntent(intent);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
        setIntent(intent);
    }

    void handleIntent(Intent intent) {
        if(intent == null || intent.getExtras() == null) return;

        if(intent.getExtras().containsKey("doQuit"))
            if(mUnityPlayer != null) {
                finish();
            }
    }



}
