package com.cameraomr.android;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.cameraomr.android.classes.Template;
import com.cameraomr.android.db.TemplatesDataSource;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class TemplateChooserActivity extends AppCompatActivity {

    private Button mTemplateButton;
    private Spinner mTemplateOptions;
    private EditText mTitle;
    private EditText mDate;
    private final List<String> mTemplateTitles = new ArrayList<String>();
    private final List<String> mTemplateIds = new ArrayList<String>();
    private String key_template_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_template_chooser);

        mTitle = (EditText) findViewById(R.id.editTextTitle);
        mDate  = (EditText) findViewById(R.id.editTextDate);
        setDefaultDate();
        setupTemplates();

        mTemplateOptions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                key_template_id = mTemplateIds.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mTemplateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTitle.getText().toString().trim().length() == 0) {
                    Toast.makeText(TemplateChooserActivity.this, "Please provide a title", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent keyIntent = new Intent(TemplateChooserActivity.this, KeyEditorActivity.class);
                keyIntent.putExtra("key_template_id", key_template_id);
                keyIntent.putExtra("key_title", mTitle.getText().toString());
                keyIntent.putExtra("key_date", mDate.getText().toString());
                startActivity(keyIntent);
            }
        });
    }

    public void setupTemplates()
    {
        TemplatesDataSource datasource = new TemplatesDataSource(this);
        datasource.open();
        List<Template> templates = datasource.getAllTemplates();
        for(Template template:templates)
        {
            String title = "" + template.getNum_answers() + " Questions, " + template.getNum_options() +" Options";
            mTemplateTitles.add(title);
            mTemplateIds.add("" + template.getId());
        }
        datasource.close();


        mTemplateButton = (Button) findViewById(R.id.nextButton);
        mTemplateOptions = (Spinner) findViewById(R.id.templateChoices);

        ArrayAdapter<String> templateAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, mTemplateTitles);
        templateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mTemplateOptions.setAdapter(templateAdapter);
    }

    public void setDefaultDate()
    {
        mDate.setInputType(InputType.TYPE_NULL);
        mDate.setFocusable(false);
        Date cDate = new Date();
        String fDate = new SimpleDateFormat("yyyy-MM-dd").format(cDate);
        mDate.setText(fDate);
    }

    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            EditText cDate = (EditText) getActivity().findViewById(R.id.editTextDate);
            cDate.setText("" + year + "-" + month + "-" + day);
        }
    }
}
