package com.katkada.Activity;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Spinner;

import com.katkada.Fragment.BlackberryFragment;
import com.katkada.Fragment.CUGFragment;
import com.katkada.Fragment.Data_3G_4G_Fragment;
import com.katkada.Fragment.FultalktimeFragment;
import com.katkada.Fragment.GPRSFragment;
import com.katkada.Fragment.ISDFragment;
import com.katkada.Fragment.NightPacksFragment;
import com.katkada.Fragment.RoamingFragment;
import com.katkada.Fragment.SMSFragment;
import com.katkada.Fragment.TalkTimeFragment;
import com.katkada.Fragment.VoiceCallFragment;
import com.katkada.Other.JSONParser;
import com.katkada.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
public class ShowPlans extends AppCompatActivity {
    public static Spinner sp_state, sp_Operators;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    public static String OperatorID, RegionID;
    public String[] spinnerArray_Oprators;
    public final HashMap<String, String> spinnerMap_Opearators = new HashMap<String, String>();
    //  TalkTimeFragment.DisplayPlans displayPlans;
    JSONParser jsonParser = new JSONParser();
    public static String[] spinnerArray_Region;
    public final static HashMap<String, String> spinnerMap_Region = new HashMap<String, String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //    toolbar = (Toolbar) findViewById(R.id.toolbar);
        //  setSupportActionBar(toolbar);
        // getActionBar().setHomeButtonEnabled(true);
        // getActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_prepaid_mobile_packs);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_back);
        // stopButton.setVisibility(View.VISIBLE);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }
    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new TalkTimeFragment(), "TALKTIME");
        //  adapter.addFragment(new PopularFragment(), "POPULAR");
        adapter.addFragment(new FultalktimeFragment(), "FULL TALKTIME");
        adapter.addFragment(new Data_3G_4G_Fragment(), "3G/4G DATA");
        adapter.addFragment(new VoiceCallFragment(), "VIDEO CALL");
        adapter.addFragment(new SMSFragment(), "SMS");
        adapter.addFragment(new GPRSFragment(), "GPRS");
        adapter.addFragment(new RoamingFragment(), "ROAMING");
        adapter.addFragment(new NightPacksFragment(), "NIGHT PACKS");
        adapter.addFragment(new BlackberryFragment(), "BLACKBERRY");
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
//    @Override
//    public void onBackPressed() {
//       // super.onBackPressed();
//        this.finish();
//      //  return
//                ;
//        // startActivity(new Intent(getBaseContext(), MainActivity.class));
//
//    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                finish();
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        Intent i = new Intent(getApplicationContext(), MainActivity.class);
        finish();
        startActivity(i);
    }
}