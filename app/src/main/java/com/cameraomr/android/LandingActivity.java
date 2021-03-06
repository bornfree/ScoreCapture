package com.cameraomr.android;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.cameraomr.android.classes.Key;
import com.cameraomr.android.db.KeysDataSource;

public class LandingActivity extends AppCompatActivity {

    private Button mStartChecking;
    private Button mSetKey;
    private Button mAddKey;
    private TextView mActiveKeyTitle;
    private SharedPreferences sharedPref;
    private KeysDataSource keydatasource;

    private boolean activeKeySet;
    private String activeKeyId;
    private Key k;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        mStartChecking = (Button) findViewById(R.id.startChecking);
        mSetKey        = (Button) findViewById(R.id.setKey);
        mAddKey        = (Button) findViewById(R.id.addKey);
        mActiveKeyTitle = (TextView) findViewById(R.id.activeKeyTitle);

        keydatasource = new KeysDataSource(this);

        mStartChecking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(activeKeySet == false || k == null)
                {
                    Toast.makeText(LandingActivity.this, "Please add/set a key first", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent cameraIntent = new Intent(LandingActivity.this, CameraActivity.class);
                cameraIntent.putExtra("key_id", activeKeyId);
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
        //resetSharedPreferences();

        keydatasource.open();

        sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        activeKeySet = sharedPref.getBoolean(getString(R.string.active_key_set), false);
        activeKeyId   = sharedPref.getString(getString(R.string.active_key_id), "");
        if(activeKeySet == true)
        {
            k = keydatasource.getKey(activeKeyId);
            if(k != null) {
                mActiveKeyTitle.setText(k.getTitle());
            }
        }else
        {
            mActiveKeyTitle.setText("No active key set");
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        keydatasource.close();
    }



    public void resetSharedPreferences()
    {
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean(getString(R.string.active_key_set), false);
        editor.commit();
    }
}
