package com.driver.cabscout.controler;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import com.driver.cabscout.model.Constant;
import com.driver.cabscout.model.Event;
import com.driver.cabscout.model.HttpHandler;

/**
 * Created by Pankaj on 24/1/17.
 */

public class LoginManager {

    private static final String TAG = LoginManager.class.getSimpleName();

    public void doLogin(Context context, String params) {

        new ExecuteApi(context).execute(params);
    }

    private class ExecuteApi extends AsyncTask<String, String, String> {
        Context mContext;

         ExecuteApi(Context mContext) {
            this.mContext = mContext;
        }

        @Override
        protected String doInBackground(String... strings) {
            HttpHandler httpHandler = new HttpHandler();
            String response = httpHandler.makeServiceCall(strings[0]);

            Log.e(TAG, "login response--" +response);

            return response;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONObject response = jsonObject.getJSONObject("response");
                int id = Integer.parseInt(response.getString("id")); //
                String message = response.getString("message");

                    EventBus.getDefault().post(new Event(Constant.LOGIN_STATUS,String.valueOf(id)+","+message));


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
