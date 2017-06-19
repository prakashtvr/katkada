package com.katkada.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.katkada.Other.TooltipWindow;
import com.katkada.R;

import android.view.View.OnClickListener;
public class Share extends AppCompatActivity {
    TooltipWindow tipWindow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);
        tipWindow = new TooltipWindow(Share.this);
        TextView textView = (TextView) findViewById(R.id.anchor_view);
        textView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View anchor) {
                if (!tipWindow.isTooltipShown())
                    tipWindow.showToolTip(anchor);
            }
        });
    }
    @Override
    protected void onDestroy() {
// TODO Auto-generated method stub
        if (tipWindow != null && tipWindow.isTooltipShown())
            tipWindow.dismissTooltip();
        super.onDestroy();
    }
}