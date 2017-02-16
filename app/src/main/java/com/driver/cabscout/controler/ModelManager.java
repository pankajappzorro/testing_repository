package com.driver.cabscout.controler;

public class ModelManager {

    private CabCompaniesManager cabCompaniesManager;
   private RegistrationManager registrationManager;
   private LoginManager loginManager;
    private FacebookLoginManager facebookLoginManager;
   /* private SearchAddressManager searchAddressManager;
    private LocationDirectionManager locationDirectionManager;*/
    private static ModelManager modelManager;


    private ModelManager() {
        cabCompaniesManager = new CabCompaniesManager();
         registrationManager = new RegistrationManager();
       /*searchAddressManager = new SearchAddressManager();*/
        loginManager = new LoginManager();
        facebookLoginManager = new FacebookLoginManager();
       // locationDirectionManager = new LocationDirectionManager();
    }

    public static ModelManager getInstance() {
        if (modelManager == null)
            return modelManager = new ModelManager();
        else
            return modelManager;
    }

    public FacebookLoginManager getFacebookLoginManager() {
        return facebookLoginManager;
    }

    public CabCompaniesManager getCabCompaniesManager() {
        return cabCompaniesManager;
    }

    public RegistrationManager getRegistrationManager() {
        return registrationManager;
    }

   public LoginManager getLoginManager() {
        return loginManager;
    }

    /*public SearchAddressManager getSearchAddressManager() {
        return searchAddressManager;
    }

    public LocationDirectionManager getLocationDirectionManager() {
        return locationDirectionManager;
    }*/
}