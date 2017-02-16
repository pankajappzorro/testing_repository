package com.driver.cabscout.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.driver.cabscout.controler.ModelManager;
import com.driver.cabscout.model.CSPreferences;
import com.driver.cabscout.model.Constant;
import com.driver.cabscout.model.Event;
import com.driver.cabscout.model.Operations;
import com.driver.cabscout.model.Utils;
import com.facebook.CallbackManager;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.github.rahatarmanahmed.cpv.CircularProgressView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import app.driver.cabscout.R;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;

/**
 * Created by pankaj on 20/1/17.
 */

public class LoginActivity extends AppCompatActivity  {
    EditText logIn, passWord;
    TextView facebooklogin;
    Button loginbt;
    String mob;
    String password;
    Activity context;
    CallbackManager callbackManager;

    CircularProgressView progressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.setApplicationId("410517842613797");
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_login);
        context =this;
        init();

    }
    public void init() {
        callbackManager = CallbackManager.Factory.create();
        logIn = (EditText) findViewById(R.id.etLoginid);
        passWord = (EditText) findViewById(R.id.etPassword);
        facebooklogin=(TextView)findViewById(R.id.fbLogin);
        progressView = (CircularProgressView) findViewById(R.id.progressView);
        progressView.setVisibility(View.GONE);

        ButterKnife.bind(this);

    }

   @OnClick(R.id.btLogin)
    public void loginDriver() {
        mob = logIn.getText().toString();
        password = passWord.getText().toString();
        if (mob.isEmpty() || password.isEmpty()) {
            Toast.makeText(LoginActivity.this, "Plaese enter login no and passwrod", Toast.LENGTH_SHORT).show();
        }
        else if (!Utils.emailValidator(mob)){

            logIn.setError("Required");
        }
        else if (passWord.getText().toString().isEmpty()){

            passWord.setError("Required");
        }

        else {
            progressView.setVisibility(View.VISIBLE);
            ModelManager.getInstance().getLoginManager().doLogin(LoginActivity.this, Operations.loginTask(LoginActivity.this,
                    mob, password, CSPreferences.readString(LoginActivity.this, "device_token")));
        }
    }

    @OnClick(R.id.tvSignup)
    public void goSignup() {
        Intent signupIntent = new Intent(LoginActivity.this, SignUp.class);
        startActivity(signupIntent);
    }
      public void faceBookLogin(View view){

        ModelManager.getInstance().getFacebookLoginManager().doFacebookLogin(context,callbackManager);
          return;

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK)
        {
            ModelManager.getInstance().getFacebookLoginManager().getFacebookData(context);
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
        progressView.setVisibility(View.GONE);
        switch (event.getKey()) {
            case Constant.LOGIN_STATUS:
                   progressView.setVisibility(View.GONE);
                   String response = event.getValue();
                   String[] messagespli = response.split(",");
                   int id = Integer.parseInt(messagespli[messagespli.length-2]);
                   String message = messagespli[messagespli.length-1];
                   if (id>0){

                          Intent i = new Intent(LoginActivity.this, HomeScreenActivity.class);
                          CSPreferences.putString(LoginActivity.this, "login_status", "true");
                          startActivity(i);
                           }
                   else {

                           new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("Error")
                                .setContentText(message)
                                .show();
                         }
                          break;

            case Constant.FACEBOOK_LOGIN_EMPTY:
                            Intent intent = new Intent(context,LoginFacebookActivity.class);
                            startActivity(intent);
                            break;
        }
    }


}
