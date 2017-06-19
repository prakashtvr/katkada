package com.katkada.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.katkada.Fragment.Data_3G_4G_Fragment;
import com.katkada.Fragment.GPRSFragment;
import com.katkada.Other.CircularProgressBarDrawable;
import com.katkada.R;

import java.util.ArrayList;
import java.util.List;
public class DataDetails extends AppCompatActivity {
    TextView tv_call, tv_sms;
    public SeekBar bar;
    private ProgressBar progBar, progress1, progress2;
    private TextView text, tvMyDataUsage, tv_progresstext, tv_DataHeading;
    private Handler mHandler = new Handler();
    private int mProgressStatus = 0;
    int remainigpro = 100;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_left);
        setContentView(R.layout.activity_data_details);
        tv_call = (TextView) findViewById(R.id.tv_calldetail);
        progBar = (ProgressBar) findViewById(R.id.progressBar);
        progress1 = (ProgressBar) findViewById(R.id.progressBar2);
        progress2 = (ProgressBar) findViewById(R.id.progressBar4);
        text = (TextView) findViewById(R.id.textView39);
        tv_sms = (TextView) findViewById(R.id.tv_smsdetail);
        tvMyDataUsage = (TextView) findViewById(R.id.tv_mydatausage);
        tv_DataHeading = (TextView) findViewById(R.id.textView37);
        tv_progresstext = (TextView) findViewById(R.id.tv_dataused);
        tvMyDataUsage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(getBaseContext(), MyDataUsage.class));
            }
        });
        tv_sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(getBaseContext(), SMSDetails.class));
            }
        });
        tv_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(getBaseContext(), CallDetails.class));
            }
        });
        viewPager = (ViewPager) findViewById(R.id.viewpager1);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs1);
        tabLayout.setupWithViewPager(viewPager);
        dosomething();
    }
    private void setupViewPager(ViewPager viewPager) {
        DataDetails.ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
//        adapter.addFragment(new TalkTimeFragment(), "TALKTIME");
//        //  adapter.addFragment(new PopularFragment(), "POPULAR");
//        adapter.addFragment(new FultalktimeFragment(), "FULL TALKTIME");
        adapter.addFragment(new Data_3G_4G_Fragment(), "3G/4G DATA");
//        adapter.addFragment(new VoiceCallFragment(), "VIDEO CALL");
        //adapter.addFragment(new SMSFragment(), "SMS");
        adapter.addFragment(new GPRSFragment(), "2G Data");
//        adapter.addFragment(new RoamingFragment(), "ROAMING");
//        adapter.addFragment(new NightPacksFragment(), "NIGHT PACKS");
//        adapter.addFragment(new BlackberryFragment(), "BLACKBERRY");
//        adapter.addFragment(new CUGFragment(), "CUG");
//        adapter.addFragment(new ISDFragment(), "ISD");
        viewPager.setAdapter(adapter);
        // PrepaidMobilePacks.ViewPagerAdapter adapter = new PrepaidMobilePacks.ViewPagerAdapter( getSupportFragmentManager());
        // adapter.addFragment(new RecomendationFragment(), "RECOMENDATION");
        // adapter.addFragment(new Recharge(), "RECHARGE");
        // adapter.addFragment(new RecomendationFragment(), "THREE");
        // viewPager.setAdapter(adapter);
    }
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();
        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }
        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }
        @Override
        public int getCount() {
            return mFragmentList.size();
        }
        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }
        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
    public void dosomething() {
        final long sizeInMb = (android.net.TrafficStats.getTotalRxBytes() + android.net.TrafficStats.getTotalTxBytes()) / (1024 * 1024);
        CircularProgressBarDrawable drawable = new CircularProgressBarDrawable();
        drawable.setColors(new int[]{0xffff0000, 0xffff00a8, 0xffb400ff, 0xff2400ff, 0xff008aff,
                0xff00ffe4, 0xff00ff60, 0xff0cff00, 0xffa8ff00, 0xffffc600, 0xffff3600, 0xffff0000});
        // progBar.setProgressDrawable(drawable);
        progBar.setMax(1000);
        progress1.setMax(5000);
        progress2.setMax(5000);
        progress2.setProgress(80);
        progBar.setProgress(20);
        new Thread(new Runnable() {
            public void run() {
                final int presentage = 0;
                while (mProgressStatus < sizeInMb) {
                    mProgressStatus += 1;
                    // Update the progress bar
                    mHandler.post(new Runnable() {
                        public void run() {
                            progBar.setProgress(mProgressStatus);
                            progress1.setProgress(mProgressStatus);
                            progress2.setProgress(remainigpro--);
                            text.setText("" + mProgressStatus + " MB");
                            tv_DataHeading.setText("" + mProgressStatus + " MB");
                            tv_progresstext.setText("" + mProgressStatus + " MB");
                        }
                    });
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        // progBar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#C0D000"), android.graphics.PorterDuff.Mode.SRC_ATOP);
        // progBar.setProgress(mProgressStatus);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
