package com.cameraomr.android;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.cameraomr.android.com.cameraomr.db.Key;
import com.cameraomr.android.com.cameraomr.db.KeysDataSource;

public class LandingActivity extends AppCompatActivity {

    private Button mStartChecking;
    private Button mSetKey;
    private Button mAddKey;
    private TextView mActiveKeyTitle;
    private SharedPreferences sharedPref;
    private KeysDataSource datasource;

    private boolean activeKeySet;
    private String activeKeyId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        mStartChecking = (Button) findViewById(R.id.startChecking);
        mSetKey        = (Button) findViewById(R.id.setKey);
        mAddKey        = (Button) findViewById(R.id.addKey);
        mActiveKeyTitle = (TextView) findViewById(R.id.activeKeyTitle);

        mStartChecking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(activeKeySet == false)
                {
                    Toast.makeText(LandingActivity.this, "Please add/set a key first", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent cameraIntent = new Intent(LandingActivity.this, CameraActivity.class);
                startActivity(cameraIntent);

            }
        });

        mAddKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent templateChooserIntent = new Intent(LandingActivity.this, TemplateChooserActivity.class);
                startActivity(templateChooserIntent);
            }
        });

        mSetKey.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent selectKeyIntent = new Intent(LandingActivity.this, SelectKeyActivity.class);
                startActivity(selectKeyIntent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        activeKeySet = sharedPref.getBoolean(getString(R.string.active_key_set), false);
        activeKeyId   = sharedPref.getString(getString(R.string.active_key_id), "");
        if(activeKeySet == true)
        {
            datasource = new KeysDataSource(this);
            datasource.open();
            Key k = datasource.getKey(activeKeyId);
            if(k != null)
                mActiveKeyTitle.setText(k.getTitle());
        }else
        {
            mActiveKeyTitle.setText("No active key set");
        }
    }
}
