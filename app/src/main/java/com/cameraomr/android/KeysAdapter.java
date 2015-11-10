package com.cameraomr.android;

/**
 * Created by harsha on 9/11/15.
 */
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.cameraomr.android.com.cameraomr.db.Key;
import com.cameraomr.android.com.cameraomr.db.Template;

public class KeysAdapter extends BaseAdapter {

    private List<Key> keys;
    private Context mContext;

    public KeysAdapter(List<Key> data, Context context) {
        keys = data;
        mContext = context;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return keys.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return keys.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return keys.indexOf(getItem(position));
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        View v = convertView;
        if (v == null) {
            LayoutInflater mInflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = mInflater.inflate(R.layout.component_key_details, null);
        }

        final TextView keyTitle = (TextView) v.findViewById(R.id.keyTitle);
        final TextView keyTemplateTitle  = (TextView) v.findViewById(R.id.keyTemplateTitle);
        final TextView keyDate  = (TextView) v.findViewById(R.id.keyDate);
        final TextView keyId = (Button) v.findViewById(R.id.keyIdButton);
        final TextView keyId2 = (Button) v.findViewById(R.id.keyIdButton2);

        Key key = (Key) getItem(position);

        keyTitle.setText(key.getTitle());
        keyTemplateTitle.setText(key.getTemplate_title());
        keyDate.setText(key.getDate());
        keyId.setTag(Long.valueOf(key.getId()));
        keyId2.setTag(Long.valueOf(key.getId()));

        return v;
    }

}
