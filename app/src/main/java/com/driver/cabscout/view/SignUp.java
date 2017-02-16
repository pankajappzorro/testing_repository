package com.driver.cabscout.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.driver.cabscout.controler.ModelManager;
import com.driver.cabscout.model.Constant;
import com.driver.cabscout.model.Event;
import com.driver.cabscout.model.Operations;
import com.driver.cabscout.model.Utils;
import com.github.rahatarmanahmed.cpv.CircularProgressView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import app.driver.cabscout.R;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by pankaj on 21/1/17.
 */
public class SignUp extends AppCompatActivity {
    Toolbar toolbar;
    TextView txtSignup;
    Context context;
    ImageView driverPic;
    Activity activity = this;
    EditText etDrivername, etDrivstate, etDriEmail, etDrivNo, etDrivPasswrd, etReDrivPassword, etDrivLicence, etDrivCity, etDrivZip;
    String drivername, driEmail, drivNo, drivPasswrd, redrivPasswrd, drivLicence, drivCity, drivState, drivZip,devicetoken, cabid;
    CircularProgressView progressView;
    CheckBox termsCheckBox, policyCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.driversignup);
        initViews();
        //Top toolbar
    }

    public void initViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("CREATE ACCOUNT");
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);  // Butterknife Click Listener
        context = this;
        progressView = (CircularProgressView) findViewById(R.id.progressView);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        /*devicetoken = FirebaseInstanceId.getInstance().getToken();
        cabid = getIntent().getStringExtra("cab_id");*/

        etDrivername = (EditText) findViewById(R.id.edtdribername);
        etDriEmail = (EditText) findViewById(R.id.etDriEmail);
        etDrivNo =   (EditText) findViewById(R.id.etDrivNo);
        etDrivPasswrd = (EditText) findViewById(R.id.etDrivPasswrd);
        etReDrivPassword = (EditText) findViewById(R.id.etReDrivPassword);
        etDrivLicence = (EditText) findViewById(R.id.etDrivLicence);
        etDrivCity = (EditText) findViewById(R.id.etDrivCity);
        etDrivstate = (EditText) findViewById(R.id.etDrivstate);
        etDrivZip = (EditText) findViewById(R.id.etDrivstate);
        driverPic = (ImageView) findViewById(R.id.ciDriver);
        termsCheckBox = (CheckBox) findViewById(R.id.termsCheckbox);
        policyCheckBox = (CheckBox) findViewById(R.id.privacyCheckbox);
    }

    @OnClick(R.id.tvAlreadyacc)
    public void login() {
        Intent logIntent = new Intent(SignUp.this, LoginActivity.class);
        startActivity(logIntent);
    }

    @OnClick(R.id.btSubmit)
    public void signup() {
        // get String
        drivername = etDrivername.getText().toString().trim();
        driEmail = etDriEmail.getText().toString().trim();
        drivNo = etDrivNo.getText().toString().trim();
        drivPasswrd = etDrivPasswrd.getText().toString().trim();
        redrivPasswrd = etReDrivPassword.getText().toString().trim();
        drivLicence = etDrivLicence.getText().toString().trim();
        drivCity = etDrivCity.getText().toString().trim();
        drivState = etDrivstate.getText().toString().trim();
        drivZip = etDrivZip.getText().toString().trim();

       if (etDrivername.getText().toString().isEmpty()){

           etDrivername.setError("Required");

        }
       else if (!drivPasswrd.equals(redrivPasswrd)) {

            Toast.makeText(this, "Password didn't match", Toast.LENGTH_SHORT).show();

        } else if (!Utils.emailValidator(driEmail)) {

                etDriEmail.setError("Required vaild Email id");

        } else if (!termsCheckBox.isChecked() || !policyCheckBox.isChecked()) {

            Toast.makeText(this, "You must be agree to all terms and conditions", Toast.LENGTH_SHORT).show();
        }
        else if (drivNo.isEmpty()||drivNo.length()<=10){

           etDrivNo.setError("Required");
        }
        else {
           progressView.setVisibility(View.VISIBLE);
           ModelManager.getInstance().getRegistrationManager().registerUser(getApplicationContext(),
                    Operations.registrationTask(getApplicationContext(), driEmail, cabid, drivername, redrivPasswrd, "dhddhfd", drivNo, drivLicence, drivCity, drivState, drivZip));
        }
    }
    @Subscribe
    public void onEvent(Event event) {
        switch (event.getKey()) {
            case Constant.SIGNUPRESPONSE:

                progressView.setVisibility(View.GONE);

                String responsemessage = event.getValue();
                String[] messagesplit= responsemessage.split(",");
                int id = Integer.parseInt(messagesplit[messagesplit.length-2]);
                String message = messagesplit[messagesplit.length-1];
                if (id>0){
                    new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Success")
                            .setContentText(message)
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    Intent i = new Intent(context,LoginActivity.class);
                                    startActivity(i);
                                    finish();
                                }
                            })
                            .show();
                }
                else {
                    new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Error")
                            .setContentText(message)
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    Intent i = new Intent(context,SignUp.class);
                                    startActivity(i);
                                }
                            })
                            .show();
                }
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                overridePendingTransition(android.R.anim.fade_out, android.R.anim.fade_in);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
