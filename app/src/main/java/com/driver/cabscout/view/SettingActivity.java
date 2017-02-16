package com.driver.cabscout.view;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import app.driver.cabscout.R;
import butterknife.ButterKnife;

/**
 * Created by pankaj on 8/2/17.
 */
public class SettingActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    Spinner spinnerCountry;
    Toolbar toolbar;

        String[] country = { "United States", "Mexico", "Canada", "France", "Italy"  };

        @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        findViewIdInit();
    }
    public  void findViewIdInit()
    {
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle("SETTINGS");
        toolbar.setTitleTextColor(Color.BLACK);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        }
        // Spinner element
        spinnerCountry=(Spinner)findViewById(R.id.spCountrycode);
        // Spinner click listener
        spinnerCountry.setOnItemSelectedListener(this);

        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,country);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spinnerCountry.setAdapter(aa);

        ButterKnife.bind(this);
    }
   /* @OnClick(R.id.tvCarprofile)
    public void carProfile()
    {
        Intent intentCarprofile = new Intent(this,CarProfileActivity.class);
        startActivity(intentCarprofile);
    }*/
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        Toast.makeText(getApplicationContext(),country[position] , Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
