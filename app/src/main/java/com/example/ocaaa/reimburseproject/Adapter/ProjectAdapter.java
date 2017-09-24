package com.example.ocaaa.reimburseproject.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.ocaaa.reimburseproject.Model.Project;
import com.example.ocaaa.reimburseproject.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Asus on 6/19/2017.
 */

public class ProjectAdapter extends ArrayAdapter<Project> {

    public ProjectAdapter(Context context, List<Project> object) {
        super(context, 0, object);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null)
            convertView = LayoutInflater.from(getContext()).
                    inflate(R.layout.list_project,parent,false);

        Project current = getItem(position);

        TextView title = (TextView)convertView.findViewById(R.id.tvProjectName);
        title.setText("Project ID #"+current.getId());

        TextView date = (TextView)convertView.findViewById(R.id.tvProjectDate);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date d = null;
        try {
            d = sdf.parse(current.getDate());
            sdf.applyPattern("d MMMM, y");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        date.setText(sdf.format(d));

        TextView name = (TextView)convertView.findViewById(R.id.tvProjectName);
        name.setText(current.getProject_name());

        TextView details = (TextView) convertView.findViewById(R.id.tvProjectDetails);
        details.setText(current.getDetails());

        return convertView;
    }
}
