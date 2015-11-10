package com.cameraomr.android;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.cameraomr.android.com.cameraomr.db.Key;
import com.cameraomr.android.com.cameraomr.db.KeysDataSource;
import com.cameraomr.android.com.cameraomr.db.Template;
import com.cameraomr.android.com.cameraomr.db.TemplatesDataSource;

import java.util.ArrayList;

public class KeyEditorActivity extends AppCompatActivity {


    private LinearLayout mKeyRows;
    private String mKeyTitle;
    private String mKeyDate;
    private String mKeyTemplateId;
    private Button mSave;
    private String mKeyAnswers = "";
    private Template template;

    private KeysDataSource datasource;
    private SharedPreferences sharedPref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_key_editor);

        datasource = new KeysDataSource(this);
        datasource.open();
        sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        mKeyRows = (LinearLayout) findViewById(R.id.keyRows);
        mSave = (Button) findViewById(R.id.saveKey);

        mKeyTemplateId = getIntent().getStringExtra("key_template_id");
        mKeyTitle = getIntent().getStringExtra("key_title");
        mKeyDate = getIntent().getStringExtra("key_date");
        addKeyTemplate();

        mSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i=0 ; i< template.getNum_answers(); i++)
                {
                    LinearLayout keyRow = (LinearLayout) mKeyRows.getChildAt(i);
                    RadioGroup rGroup = (RadioGroup) keyRow.findViewById(R.id.keyOptions);
                    int radioButtonID = rGroup.getCheckedRadioButtonId();
                    if(radioButtonID == -1)
                    {
                        mKeyAnswers += "_;";
                    }else
                    {
                        RadioButton radioButton = (RadioButton) rGroup.findViewById(radioButtonID);
                        mKeyAnswers += (radioButton.getText() + ";");
                    }

                }

                //Toast.makeText(KeyEditorActivity.this, mKeyTitle + mKeyDate + mKeyAnswers, Toast.LENGTH_LONG).show();
                Key key = datasource.createKey(mKeyTemplateId, mKeyTitle, mKeyDate, mKeyAnswers);

                // LandingActivity needs to know these details
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putBoolean(getString(R.string.active_key_set), true);
                editor.putString(getString(R.string.active_key_id), Long.toString(key.getId()));
                editor.commit();

                Intent landingIntent = new Intent(KeyEditorActivity.this, LandingActivity.class);
                landingIntent.setFlags(landingIntent.getFlags() | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(landingIntent);
            }
        });

    }

    @Override
    protected void onResume() {
        datasource.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        datasource.close();
        super.onPause();
    }



    public void addKeyTemplate()
    {
        TemplatesDataSource datasource = new TemplatesDataSource(this);
        datasource.open();
        template = datasource.getTemplate(mKeyTemplateId);
        datasource.close();

        ArrayList<KeyRow> keyRowList = new ArrayList<KeyRow>();
        LayoutInflater inflator = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        for(int i=0 ; i< template.getNum_answers(); i++)
        {
            LinearLayout keyRow = (LinearLayout) inflator.inflate(R.layout.component_option_row, null);
            if(i%2 != 0) {
                keyRow.setBackgroundColor(0xFFD8E5E7);
            }
            TextView serialNumber = (TextView) keyRow.findViewById(R.id.serialNumber);
            serialNumber.setText(Integer.toString(i+1));
            mKeyRows.addView(keyRow);
        }
    }



}

//        for(int i=0 ; i< key_length; i++)
//        {
//            KeyRow kRow = new KeyRow();
//            kRow.setserialNumber(i+"");
//            keyRowList.add(kRow);
//        }

//KeysAdapter kAdapter = new KeysAdapter(keyRowList, getApplicationContext());
//mKeyRows.setAdapter(kAdapter);

//<ListView
//android:layout_width="match_parent"
//        android:layout_height="0dp"
//        android:layout_weight="9"
//        android:id="@+id/keyRows"
//        android:layout_gravity="center_horizontal" android:layout_below="@+id/textView7" />