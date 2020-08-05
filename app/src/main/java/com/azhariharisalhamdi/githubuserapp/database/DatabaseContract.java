package com.azhariharisalhamdi.githubuserapp.database;

import android.net.Uri;
import android.provider.BaseColumns;

public class DatabaseContract {

    public static final String AUTHORITY = "com.azhariharisalhamdi.githubuserapp";
    private static final String SCHEME = "content";

    private DatabaseContract(){}


    public static final class UserColumns implements BaseColumns {
        public static String TABLE_NAME = "GithubUser";
        public static String USERNAME = "username";
        public static String AVATAR_URL = "avatar_url";
        public static String FOLLOWER = "follower";
        public static String FOLLOWING = "following";

        public static final Uri CONTENT_URI = new Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build();

    }
}