package com.cameraomr.android;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.cameraomr.android.com.cameraomr.db.Key;
import com.cameraomr.android.com.cameraomr.db.KeysDataSource;
import com.cameraomr.android.com.cameraomr.db.Template;
import com.cameraomr.android.com.cameraomr.db.TemplatesDataSource;

import java.util.List;

public class SelectKeyActivity extends AppCompatActivity {

    private ListView mKeyList;
    private KeysDataSource datasource;
    private TemplatesDataSource tdatasource;
    private KeysAdapter kAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_key);

        mKeyList = (ListView) findViewById(R.id.keyListView);

        datasource = new KeysDataSource(this);
        datasource.open();

        tdatasource = new TemplatesDataSource(this);
        tdatasource.open();

        List<Key> keys = datasource.getAllKeys();
        for(Key k:keys)
        {
            Template t = tdatasource.getTemplate(Long.toString(k.getTemplate_id()));
            String title = "" + t.getNum_answers() + " Questions, " + t.getNum_options() + " Options";
            k.setTemplate_title(title);
        }

        kAdapter = new KeysAdapter(keys, SelectKeyActivity.this);
        mKeyList.setAdapter(kAdapter);
    }

    @Override
    protected void onResume() {
        datasource.open();
        tdatasource.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        datasource.close();
        tdatasource.close();
        super.onPause();
    }

    public void setActiveKey(View v) {
        long lid = (Long)v.getTag();
        String id =Long.toString(lid);
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(getString(R.string.active_key_set), true);
        editor.putString(getString(R.string.active_key_id), id);
        editor.commit();

        Toast.makeText(SelectKeyActivity.this, "Active key changed", Toast.LENGTH_LONG).show();
        Intent landingIntent = new Intent(SelectKeyActivity.this, LandingActivity.class);
        landingIntent.setFlags(landingIntent.getFlags() | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(landingIntent);
    }

    public void deleteKey(View v) {
        long lid = (Long) v.getTag();
        String id = Long.toString(lid);
        datasource.deleteKeyById(id);

        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String activeKeyId   = sharedPref.getString(getString(R.string.active_key_id), "");
        if(activeKeyId == id || datasource.getAllKeys().size() == 0) {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean(getString(R.string.active_key_set), false);
            editor.putString(getString(R.string.active_key_id), "");
            editor.commit();
        }


        finish();
        startActivity(getIntent());
    }


}
