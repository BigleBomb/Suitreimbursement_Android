package com.example.ocaaa.reimburseproject.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ocaaa.reimburseproject.Model.Reimburse;
import com.example.ocaaa.reimburseproject.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Asus on 7/9/2017.
 */

public class ReimburseAdapter extends ArrayAdapter<Reimburse>{

    public ReimburseAdapter(Context context, List<Reimburse> object) {
        super(context, 0, object);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null)
            convertView = LayoutInflater.from(getContext()).
                    inflate(R.layout.list_reimburse,parent,false);

        Reimburse current = getItem(position);

        LinearLayout linearLayout = (LinearLayout) convertView.findViewById(R.id.reimburseCardHeader);
        linearLayout.setBackgroundColor(getContext().getResources().getColor(current.getHeaderColor()));

        TextView title = (TextView)convertView.findViewById(R.id.tvReimburseCategory);
        title.setText(getContext().getResources().getString(current.getCategoryText()));

        TextView details = (TextView) convertView.findViewById(R.id.tvReimburseDetail);
        details.setText(current.getDetails());

        TextView date = (TextView)convertView.findViewById(R.id.tvReimburseDate);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date d = null;
        try {
            d = sdf.parse(current.getDate());
            sdf.applyPattern("d MMMM, y");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        date.setText(sdf.format(d));

        int no = current.getCost();
        String str = String.format("%,d", no).replace(",", ".");
        TextView cost = (TextView)convertView.findViewById(R.id.tvReimburseCost);
        cost.setText("Rp. "+str);

        ImageView image = (ImageView) convertView.findViewById(R.id.ivReimbursePic);
        image.setImageResource(current.getIcon());

        TextView status = (TextView) convertView.findViewById(R.id.tvReimburseStatus);
        status.setText(getContext().getResources().getString(current.getStatusText()));
        status.setTextColor(status.getResources().getColor(current.getColor()));

        return convertView;
    }
}
