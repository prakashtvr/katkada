package com.katkada.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import android.content.DialogInterface;
import android.app.AlertDialog;

import com.katkada.Fragment.HomeScreenFragment;
import com.katkada.Other.CircleTransform;
import com.katkada.Other.Config;
import com.katkada.Other.Mail;
import com.katkada.Other.MyFontTextView;
import com.katkada.Other.PreferenceHelper;
import com.katkada.Other.Reffral;
import com.katkada.Other.SessionManager;
import com.katkada.Other.TooltipWindow;
import com.katkada.R;
import com.katkada.Fragment.HomeFragment;
import com.katkada.Fragment.SettingsFragment;
//import info.androidhive.navigationdrawer.fragment.Test;
//import info.androidhive.navigationdrawer.other.CircleTransform;
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View navHeader;
    private ImageView imgNavHeaderBg, imgProfile;
    private TextView txtName, txtWebsite;
    private Toolbar toolbar;
    private FloatingActionButton fab;
    TooltipWindow tipWindow;
    SessionManager manager;
    private MyFontTextView tvExitOk, tvExitCancel;
    // urls to load navigation header background image
    // and profile image
    private static final String urlNavHeaderBg = "http://api.androidhive.info/images/nav-menu-header-bg.jpg";
    private static final String urlProfileImg = "https://lh3.googleusercontent.com/eCtE_G34M9ygdkmOpYvCag1vBARCmZwnVS6rS5t4JLzJ6QgQSBquM0nuTsCpLhYbKljoyS-txg";
    View v;
    // index to identify current nav menu item
    public static int navItemIndex = 0;
    // tags used to attach the fragments
    private static final String TAG_HOME = "home";
    private static final String TAG_PLANS_PACKS = "plans_packs";
    private static final String TAG_RATEUS = "rateus";
    private static final String TAG_CHAT = "chat";
    private static final String TAG_SHARE = "share";
    private static final String TAG_SIMDETAILS = "simdetails";
    //private static final String TAG_HOME = "home";
    // private static final String TAG_HOME = "home";
    private static final String TAG_PHOTOS = "photos";
    private static final String TAG_MOVIES = "movies";
    private static final String TAG_NOTIFICATIONS = "notifications";
    private static final String TAG_SETTINGS = "settings";
    private static final String TAG_PREFERENCES = "preferences";
    private static final String TAG_VIEWRECHARGES = "view_recharges";
    private static final String TAG_RATE_US = "rate_us";
    public static String CURRENT_TAG = TAG_HOME;
    // toolbar titles respected to selected nav menu item
    private String[] activityTitles;
    // flag to load home fragment when user presses back key
    private boolean shouldLoadHomeFragOnBackPress = true;
    private Handler mHandler;
    PreferenceHelper preferenceHelper;
    View mainview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View anchor;
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tipWindow = new TooltipWindow(MainActivity.this);
        mHandler = new Handler();
        manager = new SessionManager();
        preferenceHelper=new PreferenceHelper(MainActivity.this);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        // Navigation view header
        navHeader = navigationView.getHeaderView(0);
        txtName = (TextView) navHeader.findViewById(R.id.Profile_name);
        txtWebsite = (TextView) navHeader.findViewById(R.id.EditProfile);
        // imgNavHeaderBg = (ImageView) navHeader.findViewById(R.id.img_header_bg);
        imgProfile = (ImageView) navHeader.findViewById(R.id.img_profile);
        // load toolbar titles from string resources
        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        // load nav menu header data
        loadNavHeader();
        txtWebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Config.getConnectivityStatus(getBaseContext())) {
                    if (Config.isOnline()) {
                        startActivity(new Intent(MainActivity.this, RechargeNow.ProfileDetail.class));
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), "No Internet Detected", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    showalert();
                }
            }
        });
        // initializing navigation menu
        setUpNavigationView();
        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_HOME;
            loadHomeFragment();
        }
        tipWindow = new TooltipWindow(MainActivity.this);
    }
    /***
     * Load navigation menu header information
     * like background image, profile image
     * name, website, notifications action view (dot)
     */
    private void loadNavHeader() {
        // name, website
        if (!Login.ProgileUserName.equals("null")) {
            txtName.setText(Login.ProgileUserName);
        } else {
            // Intent i= new Intent(getApplicationContext(),EditProfile.class);
            Intent intent = new Intent(getBaseContext(), EditProfile.class);
            intent.putExtra("IS_UPDATED", "0");
            startActivity(intent);
            MainActivity.this.finish();
            // txtName.setText("Katkada");
        }
        txtWebsite.setText("Edit profile");
        //  txtName.setTextColor(Color.parseColor("#ffffff"));
        //  txtWebsite.setTextColor(Color.parseColor("#ffffff"));
        //   txtWebsite.setText("http://www.techzarinfo.com/");
        // loading header background image
        //  Glide.with(this).load(urlNavHeaderBg)
        //  .crossFade()
        //  .diskCacheStrategy(DiskCacheStrategy.ALL)
        // .into(imgNavHeaderBg);
        // Loading profile image
//        Glide.with(this).load(urlProfileImg);
//        Glide.with(this).load(R.drawable.user_image)
//                .crossFade()
//                .thumbnail(0.2f)
//                .bitmapTransform(new CircleTransform(this))
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .into(imgProfile);

        Glide.with(this).load(R.drawable.user_image).transform(new CircleTransform(this)).into(imgProfile);
        // showing dot next to notifications label
        navigationView.getMenu().getItem(3).setActionView(R.layout.menu_dot);
    }
    /***
     * chturns respected fragment that user
     * selected from navigation menu
     */
    private void loadHomeFragment() {
        // selecting appropriate nav menu item
        selectNavMenu();
        // set toolbar title
        setToolbarTitle();
        // if user select the current navigation menu again, don't do anything
        // just close the navigation drawer
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();
            // show or hide the fab button
          //  toggleFab();
            return;
        }
        // Sometimes, when fragment has huge data, screen seems hanging
        // when switching between navigation menus
        // So using runnable, the fragment is loaded with cross fade effect
        // This effect can be seen in GMail app
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments
                Fragment fragment = getHomeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };
        // If mPendingRunnable is not null, then add to the message queue
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }
        // show or hide the fab button
       // toggleFab();
        //Closing drawer on item click
        drawer.closeDrawers();
        // refresh toolbar menu
        invalidateOptionsMenu();
    }
    private Fragment getHomeFragment() {
        switch (navItemIndex) {
            case 0:
                // home
                HomeScreenFragment homeScreenFragment = new HomeScreenFragment();
                return homeScreenFragment;
            case 1:
                // photos
                if (Config.getConnectivityStatus(getBaseContext())) {
                    if (Config.isOnline()) {
                        startActivity(new Intent(MainActivity.this, PrepaidMobilePacks.class));
                    } else {
                        Toast.makeText(getApplicationContext(), "No Internet Detected", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    showalert();
                }
                // PhotosFragment photosFragment = new PhotosFragment();
                // return photosFragment;
            case 2:
                // movies fragment
                HomeScreenFragment homeScreenFragment1 = new HomeScreenFragment();
                return homeScreenFragment1;
            case 3:
                //  startActivity(new Intent(MainActivity.this, Preferences.class));
                drawer.closeDrawers();
                // notifications fragment
                // NotificationsFragment notificationsFragment = new NotificationsFragment();
                //  return notificationsFragment;
            case 4:
                // settings fragment
                SettingsFragment settingsFragment = new SettingsFragment();
                return settingsFragment;
            case 5:
                // settings fragment
                SettingsFragment settingsFragment1 = new SettingsFragment();
                return settingsFragment1;
            default:
                return new HomeFragment();
        }
    }
    private void setToolbarTitle() {
        getSupportActionBar().setTitle(activityTitles[navItemIndex]);
    }
    private void selectNavMenu() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }
    private void setUpNavigationView() {
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.nav_home:
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_HOME;
                        break;
                    case R.id.nav_photos:
                        navItemIndex = 1;
                        CURRENT_TAG = TAG_PHOTOS;
                        break;
                    case R.id.nav_view_recharges:
                        navItemIndex = 2;
                        CURRENT_TAG = TAG_VIEWRECHARGES;
                        break;
                    case R.id.nav_preferences:
//                        navItemIndex = 3;
//                        CURRENT_TAG = TAG_PREFERENCES;
                        if (Config.getConnectivityStatus(getBaseContext())) {
                            if (Config.isOnline()) {
                                startActivity(new Intent(MainActivity.this, Preferences.class));
                                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                            } else {
                                Toast.makeText(getApplicationContext(), "No Internet Detected", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            showalert();
                        }
                        break;
                    // case R.id.nav_settings:
                    //    navItemIndex = 4;
                    //CURRENT_TAG = TAG_SETTINGS;
                    //  break;
                    // case R.id.nav_simdetails:
                    //   navItemIndex = 5;
                    //   CURRENT_TAG = TAG_SIMDETAILS;
                    //    break;
                    case R.id.nav_rate_us:
                        // launch new intent instead of loading fragment
                        if (Config.getConnectivityStatus(getBaseContext())) {
                            if (Config.isOnline()) {
                                RateApp();
                            } else {
                                Toast.makeText(getApplicationContext(), "No Internet Detected", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            showalert();
                        }
                        // startActivity(new Intent(MainActivity.this, AboutUsActivity.class));
                        drawer.closeDrawers();
                        return true;
                    case R.id.nav_feedback:
                        // launch new intent instead of loading fragment
                        if (Config.getConnectivityStatus(getBaseContext())) {
                            if (Config.isOnline()) {
                                SendFeedback();
                            } else {
                                Toast.makeText(getApplicationContext(), "No Internet Detected", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            showalert();
                        }
                        // startActivity(new Intent(MainActivity.this, AboutUsActivity.class));
                        drawer.closeDrawers();
                        return true;
                    case R.id.nav_plan_request_list:
                        // launch new intent instead of loading fragment
                        if (Config.getConnectivityStatus(getBaseContext())) {
                            if (Config.isOnline()) {
                                startActivity(new Intent(MainActivity.this, PostpaidPlanRequestList.class));
                                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
                            } else {
                                Toast.makeText(getApplicationContext(), "No Internet Detected", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            showalert();
                        }
                        // startActivity(new Intent(MainActivity.this, AboutUsActivity.class));
                        drawer.closeDrawers();
                        return true;
                    case R.id.nav_share:
                        // launch new intent instead of loading fragment
                        if (Config.getConnectivityStatus(getBaseContext())) {
                            if (Config.isOnline()) {
                              //  ShareApp();
                                showRefferelDialog();
                            } else {
                                Toast.makeText(getApplicationContext(), "No Internet Detected", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            showalert();
                        }
                        drawer.closeDrawers();
                        return true;
                    case R.id.nav_logout:
                        // launch new intent instead of loading fragment
                        manager.setPreferences(MainActivity.this, "status", "0");
                        String status = manager.getPreferences(MainActivity.this, "status");
                        Log.d("status_logout", status);
                        manager.clearPreferences(MainActivity.this);
                        startActivity(new Intent(MainActivity.this, Login.class));
                        drawer.closeDrawers();
                        return true;
                    default:
                        navItemIndex = 0;
                }
                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);
                loadHomeFragment();
                return true;
            }
        });
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {
            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }
            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };
        //Setting the actionbarToggle to drawer layout
        drawer.setDrawerListener(actionBarDrawerToggle);
        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
            // MainActivity.this.finish();
            //  System.exit(0);
            return;
        }
        // This code loads home fragment when back key is pressed
        // when user is in other fragment than home
        if (shouldLoadHomeFragOnBackPress) {
            // checking if user is on other navigation menu
            // rather than home
            if (navItemIndex != 0) {
                navItemIndex = 0;
                CURRENT_TAG = TAG_HOME;
                loadHomeFragment();
                return;
            }
        }
//        new AlertDialog.Builder(this)
//                .setMessage("Are you sure you want to exit ?")
//                .setCancelable(false)
//                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int id) {
//                       finish();
//                    }
//                })
//                .setNegativeButton("No", null)
//                .show();
        openExitDialog();
        // super.onBackPressed();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        TextView tv = new TextView(MainActivity.this);
        tv.setText("Wallet:  "+preferenceHelper.getWallet());
        tv.setTextColor(getResources().getColor(R.color.white));
        tv.setOnClickListener(this);
        tv.setPadding(5, 0, 5, 0);
        tv.setTypeface(null, Typeface.BOLD);
        tv.setTextSize(14);
        menu.add(0, 0, 1, "Wallet: "+preferenceHelper.getWallet()).setActionView(tv).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        // Inflate the menu; this adds items to the action bar if it is present.
        // show menu only when home fragment is selected

        if (navItemIndex == 0) {
            getMenuInflater().inflate(R.menu.main, menu);
        }
        // when fragment is notifications, load the menu created for notifications
        if (navItemIndex == 3) {
            getMenuInflater().inflate(R.menu.notifications, menu);
        }
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_Share) {
            //    if(!tipWindow.isTooltipShown()) {
            //     tipWindow.showToolTip(v);
            //  }
            ShareApp();
            return true;
        }
        if (id == R.id.action_chat) {
            SendFeedback();
            return true;
        }
        // user is in notifications fragment
        // and selected 'Mark all as Read'
        if (id == R.id.action_mark_all_read) {
            Toast.makeText(getApplicationContext(), "All notifications marked as read!", Toast.LENGTH_LONG).show();
        }
        // user is in notifications fragment
        // and selected 'Clear All'
        if (id == R.id.action_clear_notifications) {
            Toast.makeText(getApplicationContext(), "Clear all notifications!", Toast.LENGTH_LONG).show();
        }
        return super.onOptionsItemSelected(item);
    }
    // show or hide the fab
    private void toggleFab() {
        if (navItemIndex == 0)
            fab.show();
        else
            fab.hide();
    }
    public void RateApp() {
        final AlertDialog alertDialog = new AlertDialog.Builder(
                MainActivity.this).create();
        LayoutInflater inflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View formElementsView = inflater.inflate(R.layout.rate_us, null, false);
        TextView rateus = (TextView) formElementsView.findViewById(R.id.tv_rateus);
        final RatingBar ratingBar1 = (RatingBar) formElementsView.findViewById(R.id.ratingBar);
        final TextView ratingValue = (TextView) formElementsView.findViewById(R.id.textView18);
        alertDialog.setView(formElementsView);
        ratingBar1.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                //ratingValue.setText(String.valueOf(rating));
            }
        });
        //  ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
        //    public void onRatingChanged(RatingBar ratingBar, float rating,  boolean fromUser) {
        //   ratingValue.setText(String.valueOf(rating));
//
        //}
        // });
        rateus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(android.content.Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=com.katkada&hl=en"));
                startActivity(intent);
                Toast.makeText(MainActivity.this, "Thanks for Using Katkada", Toast.LENGTH_LONG).show();
                //  Toast.makeText(getApplicationContext(), "You clicked on Submit Button", Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
            }
        });
        // Showing Alert Message
        alertDialog.show();
    }
    public void ShareApp() {
        // Toast.makeText(getApplicationContext(), "Share App!", Toast.LENGTH_LONG).show();
        final AlertDialog alertDialog = new AlertDialog.Builder(
                MainActivity.this).create();
        LayoutInflater inflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View formElementsView = inflater.inflate(R.layout.tooltip_layout, null, false);
        alertDialog.setView(formElementsView);
        // if(!tipWindow.isTooltipShown())
        //   tipWindow.showToolTip(formElementsView);
        // Showing Alert Message
        //alertDialog.show();
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        // sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
        sendIntent.setType("text/plain");
        sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Katkada");
        sendIntent.putExtra(Intent.EXTRA_TEXT, "http://www.katkada.com");
        startActivity(Intent.createChooser(sendIntent, "Choose Sharing Method"));
    }
    @Override
    public void onClick(View v) {
    }
    @Override
    protected void onDestroy() {
// TODO Auto-generated method stub
        if (tipWindow != null && tipWindow.isTooltipShown())
            tipWindow.dismissTooltip();
        super.onDestroy();
    }
    private class SendMail extends AsyncTask<String, Integer, Void> {
        private ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(MainActivity.this, "Please wait", "Sending Feedback", true, false);
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Toast.makeText(MainActivity.this, "Feedback was sent successfully.", Toast.LENGTH_LONG).show();
            progressDialog.dismiss();
        }
        protected Void doInBackground(String... params) {
            String Subject = String.valueOf(params[1]);
            String Body = String.valueOf(params[2]);
            Mail m = new Mail("prakash.techzar@gmail.com", "prakash.techzar1!");
            // String[] toArr = {"toemail@gmail", "youremail@gmail"};
            String[] toArr = {"prakash.techzar@gmail.com", "prakash.tvr@outlook.com"};
            m.setTo(toArr);
            m.setFrom("prakash.techzar@gmail.com");
            m.setSubject("Customer Feedback - " + Login.ProgileUserName + " - " + Subject);
            m.setBody("Feedback Message\n\n\n" + Body);
            try {
                if (m.send()) {
                    Toast.makeText(MainActivity.this, "Email was sent successfully.", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(MainActivity.this, "Email was not sent.", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                Log.e("MailApp", "Could not send email", e);
            }
            return null;
        }
    }
    public void SendFeedback() {
        final EditText FeedbackSubject, FeedbackBody;
        final AlertDialog alertDialog = new AlertDialog.Builder(
                MainActivity.this).create();
        LayoutInflater inflater = (LayoutInflater) getBaseContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View formElementsView = inflater.inflate(R.layout.send_feedback, null, false);
        FeedbackSubject = (EditText) formElementsView.findViewById(R.id.input_subject);
        FeedbackBody = (EditText) formElementsView.findViewById(R.id.input_body);
        final TextView send = (TextView) formElementsView.findViewById(R.id.tv_SendFeedback);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FeedbackSubject.getText().toString().isEmpty()) {
                    FeedbackSubject.setError("Subject cannot be Blank");
                } else if (FeedbackBody.getText().toString().isEmpty()) {
                    FeedbackBody.setError("Message cannot be Blank");
                } else {
                    new SendMail().execute("", FeedbackSubject.getText().toString(), FeedbackBody.getText().toString());
                    alertDialog.cancel();
                }
            }
        });
        alertDialog.setView(formElementsView);
        // Showing Alert Message
        alertDialog.show();
    }
    public void showalert() {
        android.support.v7.app.AlertDialog.Builder alertDialogBuilder = new android.support.v7.app.AlertDialog.Builder(
                getBaseContext());
        // set title
        alertDialogBuilder.setTitle("Warning !");
        // set dialog message
        alertDialogBuilder
                .setMessage("pls Check Internet Connection!")
                .setCancelable(false)
                .setPositiveButton("Setting", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                        getBaseContext().startActivity(intent);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });
        // create alert dialog
        android.support.v7.app.AlertDialog alertDialog = alertDialogBuilder.create();
        // show it
        alertDialog.show();
    }


    private void showRefferelDialog() {

        final Dialog mDialog = new Dialog(this, R.style.MyDialog);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mDialog.setContentView(R.layout.ref_code_layout);
        TextView tvReferralCode = (TextView) mDialog
                .findViewById(R.id.tvReferralCode);

        tvReferralCode.setText(preferenceHelper.getRefferalCode());

        TextView tvReferralEarn = (TextView) mDialog
                .findViewById(R.id.tvReferralEarn);
       // tvReferralEarn.setText(getString(R.string.payment_unit)
             //   + ref.getBalanceAmount());

        TextView tvRefferalBonus = (TextView) mDialog
                .findViewById(R.id.tv_refferal_bonus);
        tvRefferalBonus.setText(preferenceHelper.getRefferalAmount());

        // Button btnCancel = (Button) mDialog.findViewById(R.id.btnCancel);
        Button btnShare = (Button) mDialog.findViewById(R.id.btnShare);
        btnShare.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent
                        .putExtra(
                                android.content.Intent.EXTRA_TEXT,
                                getResources().getString(
                                        R.string.text_take_look_at)
                                        + " "
                                        + getResources().getString(
                                        R.string.app_name)
                                        + " - "
                                        + "https://play.google.com/store/apps/details?id="
                                        + getPackageName()+" \n"
                                        + getString(R.string.text_note_referral)+": "
                                        + preferenceHelper.getRefferalCode());
                startActivity(Intent.createChooser(sharingIntent,
                        getResources().getString(R.string.text_share_ref_code)));
            }
        });
        mDialog.show();
    }


    public void openExitDialog() {
        final Dialog mDialog = new Dialog(this);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mDialog.setContentView(R.layout.exit_layout);
        tvExitOk = (MyFontTextView) mDialog.findViewById(R.id.tvExitOk);
        tvExitCancel = (MyFontTextView) mDialog.findViewById(R.id.tvExitCancel);
        tvExitOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                finish();
            }
        });
        tvExitCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });
        mDialog.show();
    }



}
