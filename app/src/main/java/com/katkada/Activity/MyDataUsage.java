package com.katkada.Activity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.katkada.GetDataUsage.Fragment.ProcessListFragment;
import com.katkada.R;
public class MyDataUsage extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_data_usage);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.frame_root, new ProcessListFragment()).commit();
    }
}
