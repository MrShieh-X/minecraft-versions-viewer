package com.mrshiehx.minecraft_versions_viewer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class VersionsAdapter extends ArrayAdapter {

    public VersionsAdapter(Context context, int resource, List<VersionItem> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        VersionItem versionItem = (VersionItem) getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.version_item, null);

        TextView number = (TextView) view.findViewById(R.id.number);
        TextView versionName = (TextView) view.findViewById(R.id.version_name);
        TextView releaseDate = (TextView) view.findViewById(R.id.release_date);

        number.setText(versionItem.getNumber());
        versionName.setText(versionItem.getVersionName());
        releaseDate.setText(versionItem.getReleaseDate());


        return view;
    }
}