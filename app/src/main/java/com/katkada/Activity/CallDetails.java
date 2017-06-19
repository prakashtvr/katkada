package com.katkada.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.katkada.Database.DBHelper;
import com.katkada.Fragment.CUGFragment;
import com.katkada.Fragment.FultalktimeFragment;
import com.katkada.Fragment.ISDFragment;
import com.katkada.Fragment.NightPacksFragment;
import com.katkada.Fragment.RoamingFragment;
import com.katkada.Fragment.SMSFragment;
import com.katkada.Fragment.TalkTimeFragment;
import com.katkada.Other.CircularProgressBarDrawable;
import com.katkada.R;

import java.util.ArrayList;
import java.util.List;
public class CallDetails extends AppCompatActivity {
    TextView tv_data, tv_sms;
    public SeekBar bar;
    private ProgressBar progBar, progress1, progress2;
    private TextView text, tv, tv_calls, tv_prog_Calls;
    private Handler mHandler = new Handler();
    private int mProgressStatus = 0;
    int remainigpro = 100;
    DBHelper db;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    float totalcalls = 0, SecsPerCalls = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.arrow_left);
        setContentView(R.layout.activity_call_details);
        progBar = (ProgressBar) findViewById(R.id.progressBar);
        progress1 = (ProgressBar) findViewById(R.id.progressBar2);
        progress2 = (ProgressBar) findViewById(R.id.progressBar4);
        text = (TextView) findViewById(R.id.textView39);
        tv = (TextView) findViewById(R.id.textView37);
        tv_prog_Calls = (TextView) findViewById(R.id.tv_dataused);
        // tv_calls = (TextView)findViewById(R.id.text_calls);
        tv.setText("");
        LoadCallDetails();
        dosomething();
        tv_data = (TextView) findViewById(R.id.tv_datadetail);
        tv_sms = (TextView) findViewById(R.id.tv_smsdetail);
        tv_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(getBaseContext(), DataDetails.class));
            }
        });
        tv_sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(getBaseContext(), SMSDetails.class));
            }
        });
        viewPager = (ViewPager) findViewById(R.id.viewpager1);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs1);
        tabLayout.setupWithViewPager(viewPager);
    }
    private void setupViewPager(ViewPager viewPager) {
        CallDetails.ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new TalkTimeFragment(), "TALKTIME");
//        //  adapter.addFragment(new PopularFragment(), "POPULAR");
        adapter.addFragment(new FultalktimeFragment(), "FULL TALKTIME");
//        adapter.addFragment(new Data_3G_4G_Fragment(), "3G/4G DATA");
//        adapter.addFragment(new VoiceCallFragment(), "VIDEO CALL");
        adapter.addFragment(new SMSFragment(), "SMS");
//        adapter.addFragment(new GPRSFragment(), "GPRS");
        adapter.addFragment(new RoamingFragment(), "ROAMING");
        adapter.addFragment(new NightPacksFragment(), "NIGHT PACKS");
//        adapter.addFragment(new BlackberryFragment(), "BLACKBERRY");
        adapter.addFragment(new CUGFragment(), "CUG");
        adapter.addFragment(new ISDFragment(), "ISD");
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
        CircularProgressBarDrawable drawable = new CircularProgressBarDrawable();
        drawable.setColors(new int[]{0xffff0000, 0xffff00a8, 0xffb400ff, 0xff2400ff, 0xff008aff,
                0xff00ffe4, 0xff00ff60, 0xff0cff00, 0xffa8ff00, 0xffffc600, 0xffff3600, 0xffff0000});
        // progBar.setProgressDrawable(drawable);
        progBar.setMax(100);
        progress1.setMax(100);
        progress2.setMax(100);
        progress2.setProgress(80);
        progBar.setProgress(20);
        new Thread(new Runnable() {
            public void run() {
                final int presentage = 0;
                while (mProgressStatus <= totalcalls) {
                    mProgressStatus += 1;
                    // Update the progress bar
                    mHandler.post(new Runnable() {
                        public void run() {
                            progBar.setProgress(mProgressStatus);
                            progress1.setProgress(mProgressStatus);
                            progress2.setProgress(remainigpro--);
                            //text.setText(""+mProgressStatus+" Cals");
                            text.setText(String.valueOf((int) totalcalls) + " Calls Used");
                        }
                    });
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        // progBar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#C0D000"), android.graphics.PorterDuff.Mode.SRC_ATOP);
        // progBar.setProgress(mProgressStatus);
    }
    public void LoadCallDetails() {
        db = new DBHelper(this, "ZnSoftech.db", null, 2);
        Cursor c = db.getData();
        if (c.getCount() > 0) {
            c.moveToFirst();
            do {
                String number = c.getString(0);
                String date = c.getString(1);
                String time = c.getString(2);
                String duration = c.getString(3);
                String type = c.getString(4);
                if (type.equals("OUTGOING")) {
                    SecsPerCalls = Float.parseFloat(duration.toString());
                    totalcalls += (float) Math.ceil(SecsPerCalls / 60);
                    tv_prog_Calls.setText(String.valueOf((int) totalcalls) + " Calls Used");
                    tv.setText(String.valueOf((int) totalcalls) + " Calls Used");
                }
                // tv_calls.append("Number:"+number+"\nDate:"+date+"\nTime:"+time+"\nDuration:"+duration+"\nCall Type:"+type+"\n\n");
            } while (c.moveToNext());
        } else {
            tv.setText("No Incoming and Outgoing call history exists!!!");
        }
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
