package com.katkada.Other;
import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.katkada.R;
public class ListViewAdapter extends BaseAdapter {
    Activity context;
    String title[];
    String description[];
    String title1[];
    String description1[];
    String plan;
    public ListViewAdapter(Activity context, String[] title, String[] description, String[] title1, String[] description1, String plantype) {
        super();
        this.context = context;
        this.title = title;
        this.description = description;
        this.title1 = title1;
        this.plan = plantype;
        this.description1 = description1;
    }
    public int getCount() {
        // TODO Auto-generated method stub
        return title.length;
    }
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }
    private class ViewHolder {
        TextView txtViewTitle;
        TextView txtViewDescription;
        TextView txtViewTitle1;
        TextView txtViewDescription1;
        TextView txtViewMore, txtViewBuyPlan;
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        final ViewHolder holder;
        LayoutInflater inflater = context.getLayoutInflater();
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.listitem_row, null);
            holder = new ViewHolder();
            holder.txtViewTitle = (TextView) convertView.findViewById(R.id.textView1);
            holder.txtViewDescription = (TextView) convertView.findViewById(R.id.textView2);
            holder.txtViewTitle1 = (TextView) convertView.findViewById(R.id.textView3);
            holder.txtViewDescription1 = (TextView) convertView.findViewById(R.id.textView4);
            holder.txtViewBuyPlan = (TextView) convertView.findViewById(R.id.tv_buyplan);
            if (this.plan.equals("postpaid")) {
                holder.txtViewBuyPlan.setText("Buy Plan");
            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.txtViewTitle.setText(title[position]);
        holder.txtViewDescription.setText(description[position]);
        holder.txtViewTitle1.setText(title1[position]);
        holder.txtViewDescription1.setText(description1[position]);
        holder.txtViewTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //holder. txtViewDescription1.setText("welcome");
            }
        });
//        holder.txtViewBuyPlan.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                Intent intent=new Intent();
//
//                intent.putExtra("AMOUNT",holder.txtViewTitle.getText().toString());
//                getActivity().setResult(2,intent);
//                getActivity().finish();//finishing activity
//
//            }
//        });
        return convertView;
    }
}