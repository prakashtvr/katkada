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
public class ListViewAdapterPlanrequest extends BaseAdapter {
    Activity context;
    String name[];
    String email[];
    String mobile[];
    String conType[];
    String requestDtate[];
    String status[];
    String plantypeID[];
    String plan;
    public ListViewAdapterPlanrequest(Activity context, String[] name, String[] email, String[] mobile, String[] contype, String[] requestdate, String[] status, String[] plantypeID) {
        super();
        this.context = context;
        this.name = name;
        this.email = email;
        this.mobile = mobile;
        this.conType = contype;
        this.requestDtate = requestdate;
        this.status = status;
        this.plantypeID = plantypeID;
    }
    public int getCount() {
        // TODO Auto-generated method stub
        return name.length;
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
        TextView txtViewName;
        TextView txtViewEmail;
        TextView txtViewMobile;
        TextView txtViewConType;
        TextView txtViewRequestDate, txtViewStatus;
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        final ViewHolder holder;
        LayoutInflater inflater = context.getLayoutInflater();
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.row_plan_request, null);
            holder = new ViewHolder();
            holder.txtViewName = (TextView) convertView.findViewById(R.id.tv_cus_name);
            holder.txtViewEmail = (TextView) convertView.findViewById(R.id.tv_email);
            holder.txtViewMobile = (TextView) convertView.findViewById(R.id.tv_mobile);
            holder.txtViewConType = (TextView) convertView.findViewById(R.id.tv_con_type);
            holder.txtViewRequestDate = (TextView) convertView.findViewById(R.id.tv_req_date);
            holder.txtViewStatus = (TextView) convertView.findViewById(R.id.tv_status);
//            if(this.plan.equals("postpaid"))
//            {holder.txtViewBuyPlan.setText("Buy Plan");}
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.txtViewName.setText(name[position]);
        holder.txtViewEmail.setText(email[position]);
        holder.txtViewMobile.setText(mobile[position]);
        holder.txtViewConType.setText(conType[position]);
        holder.txtViewRequestDate.setText(requestDtate[position]);
        if (status[position].equals("0"))
            holder.txtViewStatus.setText("Pending");
        else {
            holder.txtViewStatus.setText("Calcelled");
        }
        holder.txtViewStatus.setOnClickListener(new View.OnClickListener() {
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