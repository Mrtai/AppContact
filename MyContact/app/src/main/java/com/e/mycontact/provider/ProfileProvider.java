package com.e.mycontact.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.e.mycontact.database.ContactDatabase;
import com.e.mycontact.database.Profile;

import java.util.ArrayList;

public class ProfileProvider extends ContentProvider {
    public static final String AUTHORITY
            ="com.e.mycontact.provider.ProfileProvider";
    public static final String PROFILE_TABLE = "profile";

    public static final Uri CONTENT_URI =
            Uri.parse("content://" + AUTHORITY + "/" + PROFILE_TABLE);
    public static final int PROFILES = 1;
    public static final int PROFILES_ID = 2;
    private static final UriMatcher sURIMatcher =
            new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sURIMatcher.addURI(AUTHORITY, PROFILE_TABLE, PROFILES);
        sURIMatcher.addURI(AUTHORITY, PROFILE_TABLE + "/#",PROFILES_ID);
    }
    private ContactDatabase mDB;
    public ProfileProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = mDB.getWritableDatabase();
        int rowsDeleted = 0;

        switch (uriType) {
            case PROFILES:
                rowsDeleted = sqlDB.delete(ContactDatabase.TABLE_PROFILE,
                        selection,
                        selectionArgs);
                break;

            case PROFILES_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = sqlDB.delete(ContactDatabase.TABLE_PROFILE,
                            ContactDatabase.ID + "=" + id,
                            null);
                } else {
                    rowsDeleted = sqlDB.delete(ContactDatabase.TABLE_PROFILE,
                            ContactDatabase.ID + "=" + id
                                    + " and " + selection,
                            selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public String getType(Uri uri) {
        // TODO: Implement this to handle requests for the MIME type of the data
        // at the given URI.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int uriType = sURIMatcher.match(uri);
        Log.d("uri",uri.toString());
        SQLiteDatabase sqlDB = mDB.getWritableDatabase();

        long id = 0;
        switch (uriType) {
            case PROFILES:
                id = sqlDB.insert(ContactDatabase.TABLE_PROFILE, null, values);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse("profile" + "/" + id);
    }

    @Override
    public boolean onCreate() {
        mDB = new ContactDatabase(getContext());
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(ContactDatabase.TABLE_PROFILE);
        Log.d("uri ",uri.toString());
        int uriType = sURIMatcher.match(uri);
        Log.d("matchers",String.valueOf(uriType));
        switch (uriType) {
            case PROFILES_ID:
                queryBuilder.appendWhere(ContactDatabase.ID + "="
                        + uri.getLastPathSegment());
                break;
            case PROFILES:
                // no filter
                break;
            default:
                throw new IllegalArgumentException("Unknown URI");
        }

        Cursor cursor = queryBuilder.query(mDB.getReadableDatabase(),
                projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection,
                      String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = mDB.getWritableDatabase();
        int rowsUpdated = 0;

        switch (uriType) {
            case PROFILES:
                rowsUpdated = sqlDB.update(ContactDatabase.TABLE_CONTACT, contentValues, selection, selectionArgs);
                break;
            case PROFILES_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = sqlDB.update(ContactDatabase.TABLE_PROFILE,
                            contentValues,
                            ContactDatabase.ID + "=" + id,
                            null);
                } else {
                    rowsUpdated = sqlDB.update(ContactDatabase.TABLE_PROFILE,
                            contentValues,
                            ContactDatabase.ID + "=" + id
                                    + " and "
                                    + selection,
                            selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }

    public static ArrayList<Profile> getAllContact(Cursor cursor){
        ArrayList<Profile> profiles = new ArrayList<>();
        while (cursor.moveToNext()){
            int id = cursor.getInt(0);
            String name = cursor.getString(1);
            String phone = cursor.getString(2);
            String address = cursor.getString(3);
            String email = cursor.getString(4);
            byte[] image = cursor.getBlob(5);
            String born = cursor.getString(6);
            profiles.add(new Profile(id,name,phone,address,email,image,born));
        }
        return profiles;
    }
}
