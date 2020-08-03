package com.azhariharisalhamdi.githubuserapp.helper;


import android.database.Cursor;

import com.azhariharisalhamdi.githubuserapp.database.DatabaseContract;
import com.azhariharisalhamdi.githubuserapp.models.User;

import java.util.ArrayList;

public class MappingHelper {

    public static ArrayList<User> mapCursorToArrayList(Cursor userCursor) {
        ArrayList<User> UserList = new ArrayList<>();

        while (userCursor.moveToNext()) {
            int id = userCursor.getInt(userCursor.getColumnIndexOrThrow(DatabaseContract.UserColumns._ID));
            String username = userCursor.getString(userCursor.getColumnIndexOrThrow(DatabaseContract.UserColumns.USERNAME));
            String avatar_url = userCursor.getString(userCursor.getColumnIndexOrThrow(DatabaseContract.UserColumns.AVATAR_URL));
            String follower = userCursor.getString(userCursor.getColumnIndexOrThrow(DatabaseContract.UserColumns.FOLLOWER));
            String following = userCursor.getString(userCursor.getColumnIndexOrThrow(DatabaseContract.UserColumns.FOLLOWING));
            UserList.add(new User(id, username, avatar_url, Integer.parseInt(follower), Integer.parseInt(following)));
        }

        return UserList;
    }
}
