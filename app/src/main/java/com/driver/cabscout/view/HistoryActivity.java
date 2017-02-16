package com.driver.cabscout.view;

import android.annotation.TargetApi;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.CalendarView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

import app.driver.cabscout.R;
import butterknife.ButterKnife;
/**
 * Created by pankaj on 25/1/17.
 */
public class HistoryActivity extends AppCompatActivity {

  //  public EditText startDate, endDate;
  CalendarView calHistory;
    TextView calDate;
    private DatePickerDialog fromDatePickerDialog;
    private DatePickerDialog toDatePickerDialog;
    private SimpleDateFormat dateFormatter;
    Toolbar toolbar;


    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
        findViewbyId();

        CalendarView view = new CalendarView(this);
        setContentView(view);

        view.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView arg0, int year, int month,
                                            int date) {

                Toast.makeText(getApplicationContext(),date+ "/"+month+"/"+year,4000).show();
            }
        });
        /*Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                startDate.setText(dateFormatter.format(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        toDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                endDate.setText(dateFormatter.format(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        */
    }

    @TargetApi(Build.VERSION_CODES.N)
    public void findViewbyId() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Trips History");
        toolbar.setTitleTextColor(Color.BLACK);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
        }
       /* startDate = (EditText) findViewById(R.id.etStartdate);
        startDate.requestFocus();
        endDate = (EditText) findViewById(R.id.etEnddate);
        endDate.requestFocus();*/

       /*   SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
      startDate.setText(sdf.format(new Date()));
        endDate.setText(sdf.format(new Date()));*/
        ButterKnife.bind(this);
        calHistory= (CalendarView)findViewById(R.id.calHistory);

        calDate = (TextView) findViewById(R.id.dateTv);
    }

   /* @OnClick(R.id.etStartdate)
    public void selectstartDate() {
        fromDatePickerDialog.show();
    }

    @OnClick(R.id.etEnddate)
    public void selectEndDate() {
        toDatePickerDialog.show();
    }
    @OnClick(R.id.btHistory)
    public void findHistory()
    {
       // Find Button Enquiry code here which is display on list
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
