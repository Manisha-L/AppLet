package com.W9221214.AppLet.utils;

public class Constants {

    public interface InternetType {
        int INTERNET_NONE = -1;
        int INTERNET_WIFI = 0;
        int INTERNET_3G = 1;
    }

    public interface NotificationsChannels {
        String DEFAULT_CHANNEL_ID = "APPLET_NOTIFICATION";
    }

    public interface TypesList {
        String TYPE_LIST_KEY = "TYPE_LIST_KEY";
        int ALL = 0;
        int FILTERED = 1;
        int SEARCH = 2;
    }

    public interface BundleKeys {
        String BUNDLE_EXTRA = "BUNDLE_EXTRA";
        String APPLET_OBJECT_KEY = "APPLET_OBJECT_KEY";
        String FILTERED_PARAMS_KEY = "FILTERED_PARAMS_KEY";
        String BUNDLE_CURRENCY_KEY = "BUNDLE_CURRENCY_KEY";
        String SEARCH_PARAM_KEY = "SEARCH_PARAM_KEY";
    }

    public interface Status {
        String OCCUPIED = "OCCUPIED";
        String AVAILABLE = "AVAILABLE";
    }

    public interface Currencies {

        String POUND = "POUND";
    }

    public interface MapsCodes {
        int ERROR_DIALOG_REQUEST = 9001;
        int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 9002;
        int PERMISSIONS_REQUEST_ENABLE_GPS = 9003;
        String MAPVIEW_BUNDLE_KEY = "MapViewBundleKey";
    }

    public interface PrefesKeys {
        String PREFS_KEY = "SHARED_PREFERENCES_KEY";
        String CURRENCY_KEY = "CURRENCY_KEY";
    }
}
