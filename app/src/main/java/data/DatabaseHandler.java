package data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;

import model.MyNote;

/**
 * Created by Dembla on 16-01-15.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    private final ArrayList<MyNote> wishList = new ArrayList<>();   //List to store Notes

    public DatabaseHandler(Context context) {                   //CONSTRUCTOR
        super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {  // To create database

        //create our table
        String CREATE_WISHES_TABLE = "CREATE TABLE " + Constants.TABLE_NAME +
                "(" + Constants.KEY_ID + " INTEGER PRIMARY KEY," + Constants.TITLE_NAME + " TEXT, " +
                Constants.CONTENT_NAME + " TEXT, " + Constants.DATE_NAME + " LONG);";

        db.execSQL(CREATE_WISHES_TABLE);  //run that SQL command

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {  // to upgrade database

        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_NAME);

        //Create a new one
        onCreate(db);
    }

    //delete wish method
    public  void deleteWish(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Constants.TABLE_NAME, Constants.KEY_ID + " = ? ",
                new String[]{String.valueOf(id)});

        db.close();
    }

    public void update_byID (int id, String v1, String v2) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();  //like a dictionary
        values.put(Constants.TITLE_NAME, v1);
        values.put(Constants.CONTENT_NAME, v2);
        values.put(Constants.DATE_NAME, java.lang.System.currentTimeMillis());

        db.update(Constants.TABLE_NAME, values, Constants.KEY_ID + " = " +id, null);

    }

    //add content to table
    public void addWishes(MyNote wish) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();  //like a dictionary
        values.put(Constants.TITLE_NAME, wish.getTitle());
        values.put(Constants.CONTENT_NAME, wish.getContent());
        values.put(Constants.DATE_NAME, java.lang.System.currentTimeMillis());

        db.insert(Constants.TABLE_NAME, null, values);

        Log.v("Wish Successfully", "yeah!!");

        db.close();
    }

    //get all wishes
    public ArrayList<MyNote> getWishes() {

        String selectQuery = "SELECT * FROM " + Constants.TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(Constants.TABLE_NAME, new String[]{Constants.KEY_ID, Constants.TITLE_NAME,
                        Constants.CONTENT_NAME, Constants.DATE_NAME}, null, null, null, null,
                Constants.DATE_NAME + " DESC");

        //loop through cursor  to retreive data
        if (cursor.moveToFirst()) {
            do {

                MyNote wish = new MyNote();
                wish.setTitle(cursor.getString(cursor.getColumnIndex(Constants.TITLE_NAME)));
                wish.setContent(cursor.getString(cursor.getColumnIndex(Constants.CONTENT_NAME)));

                wish.setItemId(cursor.getInt(cursor.getColumnIndex(Constants.KEY_ID)));  //for deleting

                java.text.DateFormat dateFormat = java.text.DateFormat.getDateInstance();
                String dateData = dateFormat.format(new Date(cursor.getLong(cursor.getColumnIndex(Constants.DATE_NAME))).getTime());

                wish.setRecordDate(dateData);

                wishList.add(wish);

            } while (cursor.moveToNext());
        }
        return wishList;
    }
}
