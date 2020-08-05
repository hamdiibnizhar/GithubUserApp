package com.azhariharisalhamdi.githubuserapp.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import com.azhariharisalhamdi.githubuserapp.database.UserHelper;

import static com.azhariharisalhamdi.githubuserapp.database.DatabaseContract.AUTHORITY;
import static com.azhariharisalhamdi.githubuserapp.database.DatabaseContract.UserColumns.CONTENT_URI;
import static com.azhariharisalhamdi.githubuserapp.database.DatabaseContract.UserColumns.TABLE_NAME;

public class UserDataProvider extends ContentProvider {
    private static final int USER = 1;
    private static final int USER_ID = 2;
    private static final int USER_USERNAME = 3;
    private UserHelper userHelper;

    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sUriMatcher.addURI(AUTHORITY, TABLE_NAME, USER);
        sUriMatcher.addURI(AUTHORITY,
                TABLE_NAME + "/#",
                USER_ID);
        sUriMatcher.addURI(AUTHORITY,
                TABLE_NAME + "/__check_status",
                USER_USERNAME);
    }

    public UserDataProvider() {
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public boolean onCreate() {
        userHelper = UserHelper.getInstance(getContext());
        userHelper.open();
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Cursor cursor;
        switch (sUriMatcher.match(uri)) {
            case USER:
                cursor = userHelper.queryAll();
                break;
            case USER_ID:
                cursor = userHelper.queryById(uri.getLastPathSegment());
                break;
            case USER_USERNAME:
                cursor = userHelper.queryByUsername(selectionArgs[0]);
                break;
            default:
                cursor = null;
                break;
        }
        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long added;
        switch (sUriMatcher.match(uri)) {
            case USER:
                added = userHelper.insert(values);
                break;
            default:
                added = 0;
                break;
        }
        getContext().getContentResolver().notifyChange(CONTENT_URI, null);
        return Uri.parse(CONTENT_URI + "/" + added);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int deleted;
        switch (sUriMatcher.match(uri)) {
            case USER:
                deleted = userHelper.deleteByUsername(selectionArgs[0]);
                break;
            case USER_ID:
                deleted = userHelper.deleteById(uri.getLastPathSegment());
                break;
            default:
                deleted = 0;
                break;
        }
        getContext().getContentResolver().notifyChange(CONTENT_URI, null);
        return deleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int updated;
        switch (sUriMatcher.match(uri)) {
            case USER_ID:
                updated = userHelper.update(uri.getLastPathSegment(), values);
                break;
            default:
                updated = 0;
                break;
        }
        getContext().getContentResolver().notifyChange(CONTENT_URI, null);
        return updated;
    }
}
