package com.driver.cabscout.model;

/**
 * Created by pankaj on 18/1/17.
 */

public class Config {
    static final  String base_url = "http://35.162.151.221/driver_api.php?action=";
    static  final String login_url =base_url+"driver_login&device_type=A";
    static  final String signUp_url = base_url+"driver_register";
    static  final String forget_password = "";
    static final String user_detail = "";
    static final String verify_email ="";
    public static final String facebook_login_verify_url = base_url+"driverDetailFacebookId&facebook_id=";
    public static final String cab_companies_url = base_url + "company_list";
  public   static final String fb_login_url = base_url+"customer_register";

}
