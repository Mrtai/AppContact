package com.e.mycontact.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import com.e.mycontact.database.ContactDatabase;

public class ContactProvider extends ContentProvider {

    private static final String AUTHORITY
            ="com.e.mycontact.provider.ContactProvider";
    public static final int CONTACT = 100;
    public static final int CONTACT_ID = 110;
    private static final String CONTACT_TABLE = "Contacts";
    public static final Uri CONTENT_URI =
            Uri.parse("content://" + AUTHORITY + "/" + CONTACT_TABLE);
    public static final int CONTACTS = 1;
    public static final int CONTACTS_ID = 2;
    private static final UriMatcher sURIMatcher =
            new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sURIMatcher.addURI(AUTHORITY, CONTACT_TABLE, CONTACTS);
        sURIMatcher.addURI(AUTHORITY, CONTACT_TABLE + "/#",CONTACTS_ID);
    }

    private ContactDatabase mDB;
    public ContactProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = mDB.getWritableDatabase();
        int rowsDeleted = 0;

        switch (uriType) {
            case CONTACT:
                rowsDeleted = sqlDB.delete(ContactDatabase.TABLE_CONTACT,
                        selection,
                        selectionArgs);
                break;

            case CONTACT_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = sqlDB.delete(ContactDatabase.TABLE_CONTACT,
                            ContactDatabase.ID + "=" + id,
                            null);
                } else {
                    rowsDeleted = sqlDB.delete(ContactDatabase.TABLE_CONTACT,
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

        SQLiteDatabase sqlDB = mDB.getWritableDatabase();

        long id = 0;
        switch (uriType) {
            case CONTACT:
                id = sqlDB.insert(ContactDatabase.TABLE_CONTACT, null, values);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse("contact" + "/" + id);
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
        queryBuilder.setTables(ContactDatabase.TABLE_CONTACT);

        int uriType = sURIMatcher.match(uri);
        switch (uriType) {
            case CONTACT_ID:
                queryBuilder.appendWhere(ContactDatabase.ID + "="
                        + uri.getLastPathSegment());
                break;
            case CONTACT:
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
            case CONTACT:
                rowsUpdated = sqlDB.update(ContactDatabase.TABLE_CONTACT, contentValues, selection, selectionArgs);
                break;
            case CONTACT_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = sqlDB.update(ContactDatabase.TABLE_CONTACT,
                            contentValues,
                            ContactDatabase.ID + "=" + id,
                            null);
                } else {
                    rowsUpdated = sqlDB.update(ContactDatabase.TABLE_CONTACT,
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
}

