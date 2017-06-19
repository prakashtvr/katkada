package com.katkada.Other;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.katkada.Activity.RechargeNow;
import com.katkada.Fragment.HomeScreenFragment;
import com.katkada.R;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
/**
 * Created by prakash on 23-11-2016.
 */
public class ListViewAdaptorPlanDetails extends BaseAdapter //implements View.OnClickListener, LoadImageTask.Listener
{
    Activity context;
    String strOperator[];
    String strPLanName[];
    String strValidity[];
    String strPrice[];
    String estimate[];
    String IMAGE_URL[];
    ListViewAdaptorPlanDetails.ViewHolder holder1;
    //String Url="http://katkada.com/test/assets/public//images/operator/operator_5.png";
    HomeScreenFragment fragment = new HomeScreenFragment();
    public ListViewAdaptorPlanDetails(Activity con, String[] OperatorName, String[] PlanName, String[] PlanValidity, String[] PlanPrice, String[] estimete, String[] IMAGE_URL) {
        super();
        this.context = con;
        this.strOperator = OperatorName;
        this.strPLanName = PlanName;
        this.strValidity = PlanValidity;
        this.strPrice = PlanPrice;
        this.estimate = estimete;
        this.IMAGE_URL = IMAGE_URL;
    }
    public int getCount() {
        // TODO Auto-generated method stub
        return strPLanName.length;
    }
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }
//    @Override
//    public void onClick(View v) {
//
//    }
//
//    @Override
//    public void onImageLoaded(Bitmap bitmap) {
//
//       //mImageView.setImageBitmap(bitmap);
//    }
//
//    @Override
//    public void onError() {
//        Toast.makeText(this.context, "Error Loading Image !", Toast.LENGTH_SHORT).show();
//    }
    public static class ViewHolder {
        TextView tvOperator;
        TextView tvPlanName, OptName;
        TextView tvValidity;
        TextView tvPrice, tvestimatemontcost;
        TextView tvbuyplan;
        TextView tvtxtViewMore;
        RelativeLayout rlMore;
        public static ImageView mImageView;
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        final ListViewAdaptorPlanDetails.ViewHolder holder;
        LayoutInflater inflater = context.getLayoutInflater();
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.row_plan_details, null);
            holder = new ListViewAdaptorPlanDetails.ViewHolder();
            holder.tvValidity = (TextView) convertView.findViewById(R.id.textView49);
            holder.tvPlanName = (TextView) convertView.findViewById(R.id.textView46);
            holder.tvOperator = (TextView) convertView.findViewById(R.id.textView53);
            holder.tvPrice = (TextView) convertView.findViewById(R.id.textView52);
            holder.tvtxtViewMore = (TextView) convertView.findViewById(R.id.textView50);
            holder.rlMore = (RelativeLayout) convertView.findViewById(R.id.layoutViewMore);
            holder.tvbuyplan = (TextView) convertView.findViewById(R.id.textView62);
            holder.tvestimatemontcost = (TextView) convertView.findViewById(R.id.textView60);
            holder.OptName = (TextView) convertView.findViewById(R.id.opt_name);
             holder.mImageView = (ImageView)  convertView.findViewById(R.id.imageView16);
            convertView.setTag(holder);
        } else {
            holder = (ListViewAdaptorPlanDetails.ViewHolder) convertView.getTag();
        }
        holder.rlMore.setVisibility(View.GONE);
        holder.tvOperator.setText(strOperator[position]);
        holder.tvPlanName.setText(strPLanName[position]);
        holder.tvValidity.setText(strValidity[position]);
        holder.tvPrice.setText(strPrice[position]);
        holder.tvestimatemontcost.setText(estimate[position]);
        holder.OptName.setText(IMAGE_URL[position]);
        Picasso.with(context).load("http://www.katkada.com/assets/public/images/calc_operator/operator_"+IMAGE_URL[position]+".png").into(holder.mImageView);
       // new ImageDownloaderTask(holder.mImageView).execute("http://www.katkada.com/assets/public/images/calc_operator/operator_3.png");
      /* StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        try {
            URL url = new URL("http://www.katkada.com/assets/public/images/calc_operator/operator_3.png");
            holder.mImageView.setImageBitmap(BitmapFactory.decodeStream((InputStream)url.getContent()));
        } catch (IOException e) {
            Log.e("PR\rakash", e.getMessage());
        }*/
        //new LoadImageTask(this).execute(Url);
        holder.rlMore.setVisibility(View.GONE);
        holder.tvtxtViewMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (holder.rlMore.getVisibility() == View.GONE) {
                    holder.rlMore.setVisibility(View.VISIBLE);
                    holder.tvtxtViewMore.setText("Less");
                } else {
                    holder.tvtxtViewMore.setText("More");
                    holder.rlMore.setVisibility(View.GONE);
                }
            }
        });
        holder.tvbuyplan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Intent intent=new Intent();
                //  intent.putExtra("AMOUNT",holder.tvPrice.getText().toString());
                Intent i = new Intent(context, RechargeNow.class);
                i.putExtra("Selected_Value", holder.tvPrice.getText().toString());
                context.finish();
                context.startActivity(i);
//                Log.d("Value", "onClick: "+holder.tvPrice.getText().toString());
//              Intent i= new Intent(context, RechargeNow.class);
//                i.putExtra("AMOUNT1",holder.tvPrice.getText().toString());
//                //context. startActivityForResult(i, 3);
//            context.startActivity(i);
                //finish();//finishing activity
            }
        });
        return convertView;
    }
    class ImageDownloaderTask extends AsyncTask<String, Void, Bitmap> {

        private final WeakReference<ImageView> imageViewReference;

        public ImageDownloaderTask(ImageView imageView) {
            imageViewReference = new WeakReference<ImageView>(imageView);
        }

        @Override
        protected Bitmap doInBackground(String... params) {

            return downloadBitmap(params[0]);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (isCancelled()) {
                bitmap = null;
            }

            if (imageViewReference != null) {
                ImageView imageView = imageViewReference.get();
                if (imageView != null) {
                    if (bitmap != null) {
                        imageView.setImageBitmap(bitmap);
                    } else {
                        Drawable placeholder = null;
                        imageView.setImageDrawable(placeholder);
                    }
                }
            }
        }

        private Bitmap downloadBitmap(String url) {
            HttpURLConnection urlConnection = null;
            try {
                URL uri = new URL(url);
                urlConnection = (HttpURLConnection) uri.openConnection();

                final int responseCode = urlConnection.getResponseCode();
                if (responseCode != HttpURLConnection.HTTP_OK) {
                    return null;
                }

                InputStream inputStream = urlConnection.getInputStream();
                if (inputStream != null) {
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    return bitmap;
                }
            } catch (Exception e) {
                urlConnection.disconnect();
                Log.w("ImageDownloader", "Errore durante il download da " + url);
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            return null;
        }
    }
}