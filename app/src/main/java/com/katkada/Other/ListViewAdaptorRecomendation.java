package com.katkada.Other;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.katkada.R;
/**
 * Created by admin on 19-10-2016.
 */
public class ListViewAdaptorRecomendation extends BaseAdapter {
    Activity context;
    String title[];
    String description[];
    String title1[];
    String description1[];
    public ListViewAdaptorRecomendation(Activity context, String[] title, String[] description, String[] title1, String[] description1) {
        super();
        this.context = context;
        this.title = title;
        this.description = description;
        this.title1 = title1;
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
        TextView txtViewTitle, tvViewMonth, tvViewPrice, tvViewMothlypack, tvViewEstimatedUsage, tvVIewTax, textViewEstimatedmonthlycost;
        TextView txtViewDescription;
        TextView txtViewTitle1;
        TextView txtViewDescription1;
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ListViewAdaptorRecomendation.ViewHolder holder;
        LayoutInflater inflater = context.getLayoutInflater();
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.row_recomendation_list, null);
            holder = new ListViewAdaptorRecomendation.ViewHolder();
            holder.txtViewTitle = (TextView) convertView.findViewById(R.id.textView8);
            holder.txtViewTitle = (TextView) convertView.findViewById(R.id.textView5);
            holder.tvViewMonth = (TextView) convertView.findViewById(R.id.textView6);
            holder.tvViewPrice = (TextView) convertView.findViewById(R.id.textView7);
            holder.txtViewDescription = (TextView) convertView.findViewById(R.id.textView9);
            holder.txtViewTitle1 = (TextView) convertView.findViewById(R.id.textView10);
            holder.txtViewDescription1 = (TextView) convertView.findViewById(R.id.textView11);
            convertView.setTag(holder);
        } else {
            holder = (ListViewAdaptorRecomendation.ViewHolder) convertView.getTag();
        }
        holder.txtViewTitle.setText(title[position]);
        holder.txtViewDescription.setText(description[position]);
        holder.txtViewTitle1.setText(title1[position]);
        holder.txtViewDescription1.setText(description1[position]);
        return convertView;
    }
}