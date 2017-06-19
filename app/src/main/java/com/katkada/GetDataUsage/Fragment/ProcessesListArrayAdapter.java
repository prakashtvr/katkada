package com.katkada.GetDataUsage.Fragment;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.net.TrafficStats;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.katkada.R;

import java.util.List;
/**
 * Created by prakash on 30-12-2016.
 */
public class ProcessesListArrayAdapter extends
        ArrayAdapter<ActivityManager.RunningAppProcessInfo> {
    public ProcessesListArrayAdapter(Context context, int resource,
                                     List<ActivityManager.RunningAppProcessInfo> mScanResults) {
        super(context, resource, mScanResults);
        this.context = context;
        this.listOfProducts = mScanResults;
    }
    /**
     * The context.
     */
    private final Context context;
    /**
     * The list of recordings.
     */
    private final List<ActivityManager.RunningAppProcessInfo> listOfProducts;
    /**
     * The Class ViewHolder.
     */
    private class ViewHolder {
        /**
         * The quanitity.
         */
        TextView processName, UID, sent, recieved;
    }
    @SuppressLint("NewApi")
    @Override
    public View getView(final int productIndex, View convertView,
                        ViewGroup parent) {
        /** The holder. */
        final ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.process_list_item, parent,
                    false);
            holder = new ViewHolder();
            holder.processName = (TextView) convertView
                    .findViewById(R.id.app_package_name);
            holder.UID = (TextView) convertView.findViewById(R.id.UID);
            holder.sent = (TextView) convertView.findViewById(R.id.send);
            holder.recieved = (TextView) convertView
                    .findViewById(R.id.recieved);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.processName
                .setText(listOfProducts.get(productIndex).processName);
        holder.UID.setText("UID : " + listOfProducts.get(productIndex).uid);
        holder.sent.setText("Sent : "
                + bytesHoomanRedable(TrafficStats.getUidTxBytes(listOfProducts
                .get(productIndex).uid), true));
        holder.recieved.setText("Recieve : "
                + bytesHoomanRedable(TrafficStats.getUidRxBytes(listOfProducts
                .get(productIndex).uid), true));
        holder.sent.setSelected(true);
        holder.recieved.setSelected(true);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DELETE);
                intent.setData(Uri.parse(listOfProducts.get(productIndex).processName));
                context.startActivity(intent);
            }
        });
        return convertView;
    }
    /**
     * Bytes hooman redable.
     *
     * @param bytes      the bytes
     * @param showInByte the show in byte
     * @return the string
     */
    public static String bytesHoomanRedable(long bytes, boolean showInByte) {
        if (!showInByte) {
            long bits = bytes * 8;
            long Kbit = 1024;
            long Mbit = Kbit * 1024;
            long Gbit = Mbit * 1024;
            if (bits < Kbit)
                return String.valueOf(bits) + " bit";
            if (bits > Kbit && bits < Mbit)
                return String.valueOf(bits / Kbit) + " Kilobit";
            if (bits > Mbit && bits < Gbit)
                return String.valueOf(bits / Mbit) + " Megabit";
            if (bits > Gbit)
                return String.valueOf(bits / Gbit) + " Gigabit";
            return "???";
        } else {
            long bits = bytes;
            long Kbit = 1024;
            long Mbit = Kbit * 1024;
            long Gbit = Mbit * 1024;
            if (bits < Kbit)
                return String.valueOf(bits) + " Byte";
            if (bits > Kbit && bits < Mbit)
                return String.valueOf(bits / Kbit) + " KiloByte";
            if (bits > Mbit && bits < Gbit)
                return String.valueOf(bits / Mbit) + " MegaByte";
            if (bits > Gbit)
                return String.valueOf(bits / Gbit) + " GigaByte";
            return "???";
        }
    }
}
