package com.example.experiments.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import java.util.HashMap;

public class ExperimentProvider extends ContentProvider {

    //поля для контент провайдера
    static final String PROVIDER_NAME = "com.example.contentproviderdemo.Experiment1Prov";
    static final String URL = "content://" + PROVIDER_NAME + "/experiments1";
    public static final Uri CONTENT_URI = Uri.parse(URL);

    //поля для базы данных
    public static final String ID = "_id";
    public static final String DATE_MEASUREMENT = "date_measurement";
    public static final String START_TIME = "start_time";
    public static final String END_TIME = "end_time";
    public static final String TIME_SPENT = "time_spent";
    public static final String NAME = "name";
    public static final String EQUIPMENT = "equipment";
    public static final String LOCATION = "location";
    public static final String GREASE_MASS = "grease_mass";
    public static final String GREASE_OUTPUT = "grease_output";

    //целые константы для URI
    static final int EXPERIMENTS = 1;
    static final int EXPERIMENTS_ID = 2;

    DBHelper dbHelper;

    private static HashMap<String, String> ExpertMap;
    static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "experiments1", EXPERIMENTS);
        uriMatcher.addURI(PROVIDER_NAME, "experiments1/№", EXPERIMENTS_ID);
    }

    //инфо для базы данных
    private SQLiteDatabase database;
    static final String DATABASE_NAME = "experiment";
    static final String TABLE_NAME = "obtainingSamples";
    static final int DATABASE_VERSION = 1;
    static final String CREATE_TABLE = " CREATE TABLE " + TABLE_NAME
            + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
            + " date_measurement TEXT NOT NULL, " + " start_time TEXT NOT NULL,"
            + " end_time TEXT NOT NULL," + " time_spent TEXT NOT NULL,"
            + " name TEXT NOT NULL," + " equipment TEXT NOT NULL,"
            + " location TEXT NOT NULL," + " grease_mass TEXT NOT NULL," + " grease_output TEXT NOT NULL);";

    public static class DBHelper extends SQLiteOpenHelper {

        public DBHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(DBHelper.class.getName(),
                    "Upgrading database from version " + oldVersion
                            + " to " + newVersion + ". Old data will be destroyed");
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }

    @Override
    public boolean onCreate() {
        Context context = getContext();
        dbHelper = new DBHelper(context);
        database = dbHelper.getWritableDatabase();
        return database != null;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        //имя таблицы для запроса
        queryBuilder.setTables(TABLE_NAME);

        switch (uriMatcher.match(uri)) {
            case EXPERIMENTS:
                queryBuilder.setProjectionMap(ExpertMap);
                break;
            case EXPERIMENTS_ID:
                queryBuilder.appendWhere(ID + "=" +
                        uri.getLastPathSegment());
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        if (sortOrder == null || sortOrder == "") {
            //по умолчанию сортировка по имени
            sortOrder = NAME;
        }
        Cursor cursor = queryBuilder.query(database, projection,
                selection, selectionArgs, null, null, sortOrder);
        //регистрируем курсов
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long row = database.insert(TABLE_NAME, "", values);
        if (row > 0) { //если запись успешна добавлена
            Uri newUri = ContentUris.withAppendedId(CONTENT_URI, row);
            getContext().getContentResolver().notifyChange(newUri, null);
            return newUri;
        }
        throw new SQLException("Fail to add a new record into " + uri);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int count = 0;
        switch (uriMatcher.match(uri)) {
            case EXPERIMENTS:
                count = database.update(TABLE_NAME, values, selection, selectionArgs);
                break;
            case EXPERIMENTS_ID:
                count = database.update(TABLE_NAME, values, ID + " = "
                        + uri.getLastPathSegment()
                        + (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' :
                        ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count = 0;
        switch (uriMatcher.match(uri)) {
            case EXPERIMENTS:
                count = database.delete(TABLE_NAME, selection, selectionArgs);
                break;
            case EXPERIMENTS_ID:
                String id = uri.getLastPathSegment();
                count = database.delete(TABLE_NAME,
                        ID + " = " + id + (!TextUtils.isEmpty(selection) ?
                                " AND (" + selection + ')' : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public String getType(Uri  uri) {
        switch (uriMatcher.match(uri)) {
            case EXPERIMENTS:
                return "vnd.android.cursor.dir/vnd.example.experiments1";
            case EXPERIMENTS_ID:
                return "vnd.android.cursor.item/vnd.example.experiments1";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }
}