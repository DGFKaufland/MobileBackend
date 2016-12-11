package de.kaufland.ksilence.api;

public class Api {

    public static final String VERSION              = "/v1";
    public static final String API_KEY              = "AIzaSyBf43T1cL6ahr5JlelB2EiNw3Y_tbHiy7A";

    public static class Path {
        public static final String API              = "api"                 + VERSION;
        public static final String MESSAGES         = API                   + "/messages";
        public static final String CONTACTS         = API                   + "/contacts";
        public static final String CONTACT          = CONTACTS              + "/{id}";
        public static final String NOTIFICATIONS    = API                   + "/notifications";
        public static final String NOTIFICATION     = NOTIFICATIONS         + "/{id}";
        public static final String DEPARTMENTS      = API                   + "/departments";
    }

}
