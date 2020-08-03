package com.azhariharisalhamdi.consumerapp.database;

import android.provider.BaseColumns;

public class DatabaseContract {

    static String TABLE_NAME = "GithubUser";

    public static final class UserColumns implements BaseColumns {

        public static String USERNAME = "username";
        public static String AVATAR_URL = "avatar_url";
        public static String FOLLOWER = "follower";
        public static String FOLLOWING = "following";

    }
}