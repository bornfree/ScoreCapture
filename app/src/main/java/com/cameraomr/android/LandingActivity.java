package com.cameraomr.android;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LandingActivity extends AppCompatActivity {

    private Button mStartChecking;
    private Button mSetKey;
    private Button mAddKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        mStartChecking = (Button) findViewById(R.id.startChecking);
        mSetKey        = (Button) findViewById(R.id.setKey);
        mAddKey        = (Button) findViewById(R.id.addKey);

        mStartChecking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(getApplicationContext(), CameraActivity.class);
                startActivity(cameraIntent);

            }
        });

    }
}
