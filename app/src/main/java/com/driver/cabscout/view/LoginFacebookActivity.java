package com.driver.cabscout.view;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.driver.cabscout.controler.CabCompaniesManager;
import com.driver.cabscout.controler.ModelManager;
import com.driver.cabscout.model.CSPreferences;
import com.driver.cabscout.model.Config;
import com.driver.cabscout.model.Constant;
import com.driver.cabscout.model.Event;
import com.driver.cabscout.model.Operations;
import com.driver.cabscout.model.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import app.driver.cabscout.R;

public class LoginFacebookActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = LoginFacebookActivity.class.getSimpleName();
    Activity activity = this;
    EditText editCompany, editName, editEmail, editPhone;
    String name, email, phone, company;
    TextView textRegister;
    LinearLayout linearLayout;
    TextInputLayout textInputCompany;
    boolean isCompany;
    String company_id, device_token, profile_pic, fb_id;
    Dialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_facebook);

        initViews();
    }

    public void initViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Verify Profile");
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        device_token = CSPreferences.readString(activity, "device_token");

        if (device_token.isEmpty()) {
           // device_token = FirebaseInstanceId.getInstance().getToken();
        }



        editCompany = (EditText) findViewById(R.id.editCompanyName);
        editName = (EditText) findViewById(R.id.editName);
        editEmail = (EditText) findViewById(R.id.editEmail);
        editPhone = (EditText) findViewById(R.id.editPhone);
        textRegister = (TextView) findViewById(R.id.textRegister);
        textRegister.setOnClickListener(this);

        textInputCompany = (TextInputLayout) findViewById(R.id.textInputCompanyName);

        editCompany.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    for (Map.Entry<Integer, String> entry : CabCompaniesManager.cabCompaniesList.entrySet()) {
                        Log.e(TAG, entry.getValue());

                        if (editCompany.getText().toString().isEmpty()) {
                            textInputCompany.setError("Please enter the company code");
                        } else if (!editCompany.getText().toString().equals(entry.getValue())) {
                            textInputCompany.setError("Please enter the valid company code");
                        } else {
                            textInputCompany.setErrorEnabled(false);
                        }
                    }
                } else {

                    textInputCompany.setErrorEnabled(false);
                }
            }
        });

        linearLayout = (LinearLayout) findViewById(R.id.activity_login_facebook);

        name = CSPreferences.readString(this, "user_name");
        email = CSPreferences.readString(this, "user_email");

        editName.setText(name);

        if (!email.isEmpty())
            editEmail.setText(email);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.textRegister:
                name = editName.getText().toString().trim();
                email = editEmail.getText().toString().trim();
                phone = editPhone.getText().toString().trim();
                company = editCompany.getText().toString().trim();
                profile_pic = CSPreferences.readString(activity, "profile_pic");

                fb_id = CSPreferences.readString(activity, "fb_id");

                if (company.isEmpty() || name.isEmpty() || email.isEmpty() || phone.isEmpty()) {
                    Utils.makeSnackBar(activity, linearLayout, "Please fill all the details");
                    return;
                } else if (!Utils.emailValidator(email)) {
                    Utils.makeSnackBar(activity, linearLayout, "Please enter the valid email address");
                }

                for (Map.Entry<Integer, String> entry : CabCompaniesManager.cabCompaniesList.entrySet()) {
                    if (CabCompaniesManager.cabCompaniesList.containsValue(company)) {
                        company_id = String.valueOf(entry.getKey());
                        isCompany = true;

                    }
                    else {
                        isCompany = false;
                    }
                }

                if (isCompany) {
                    dialog.show();

                    try {
                        ModelManager.getInstance().getFacebookLoginManager().registerUser(activity, Config.fb_login_url, Operations.fbLoginParams(activity,
                                company_id, email, URLEncoder.encode(name, "utf-8"), "kddfgf", phone, profile_pic, fb_id));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    Utils.makeSnackBar(activity, linearLayout, "Please enter the valid company code");
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

    @Subscribe
    public void onEvent(Event event) {
        dialog.dismiss();
        switch (event.getKey()) {
            case Constant.FACEBOOK_LOGIN_SUCCESS:
                Toast.makeText(activity, "Logged-in successfully", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(activity, HomeScreenActivity.class);
                CSPreferences.putString(activity, "login_status", "true");
                startActivity(i);
                finish();
                break;

            case Constant.ALREADY_REGISTERED:
                Utils.makeSnackBar(activity, linearLayout, "Sorry, this email is already in use. Please try with another email.");

                break;

            case Constant.SERVER_ERROR:
                Utils.makeSnackBar(activity, linearLayout, "Sorry, server error occurred. Please try after sometime.");

                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
                alertDialog.setMessage("Are you sure you want to cancel? \nYour information will not be saved.");
                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                });

                alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create().show();

                break;
        }
        return true;
    }
}
