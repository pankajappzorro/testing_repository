package com.driver.cabscout.model;

import android.content.Context;
import android.util.Log;

import org.json.JSONObject;

/**
 * Created by rishav on 17/1/17.
 */

public class Operations {
    private static final String TAG = Operations.class.getSimpleName();

    public static String getCabCompaniesTask(Context context) {
        String params = Config.cab_companies_url;
        //  Log.e(TAG, "cab_companies list--"+params);
        return params;
    }

    public static String registrationTask(Context context, String email, String cab_id, String drivername, String password, String devicetoken,
                                          String drivNo, String drivLicence, String drivCity, String drivState, String drivZip) {

        String params = Config.signUp_url + "&email=" + email + "&company_id=" + cab_id +
                "&name=" + drivername + "&password=" + password + "&device_token=" + devicetoken + "&device_type=A" + "&mobile=" + drivNo + "&driver_license=A" + drivLicence + "&city=" + drivCity +
                "&state=" + drivState + "&zip=" + drivZip;
        Log.e("Signup url", "registration params---" + params);

        return params;
    }

    // Login operation below
    public static String loginTask(Context context, String email, String password, String deviceToken) {
        String params = Config.login_url + "&email=" + email + "&password=" + password + "&device_token=" + deviceToken;

        Log.e(TAG, "login parameters--" + params);
        return params;
    }

    public static String facebookLoginTask(Context context, String fb_id) {
        String params = Config.facebook_login_verify_url + fb_id;
        Log.e(TAG, "login_verify params-- " + params);
        return params;
    }

    public static String fbLoginParams(Context context, String company_id, String email, String name,
                                       String token, String mobile, String imageUrl, String fb_id) {
        /*String params = Config.fb_login_url+company_id+"&email="+email+"&password=WJBJvfHTRNT"+"&name="+name+"&device_token="+token+"&device_type=A"+"&mobile="+mobile+"&profileImage="+imageUrl+"&facebook_id="+fb_id;*/

        try {
            JSONObject postDataParams = new JSONObject();

            postDataParams.put("company_id", company_id);
            postDataParams.put("email", email);
            postDataParams.put("password", "welcomeUser");
            postDataParams.put("name", name);
            postDataParams.put("device_token", token);
            postDataParams.put("device_type", "A");
            postDataParams.put("mobile", mobile);
            postDataParams.put("profileImage", imageUrl);
            postDataParams.put("facebook_id", fb_id);
            String params = null;
            try {
                params = Utils.getPostDataString(postDataParams);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return params;

        } catch (Exception e) {
            e.printStackTrace();
        }
        //     Log.e(TAG, "fb_login params-- "+params);return params;} catch (JSONException e) {e.printStackTrace();}return null;}
        return null;
    }
}