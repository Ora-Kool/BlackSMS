package com.etingemabian.blacksms;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SENTHelper  extends SQLiteOpenHelper{
	public static final String DATABASE_NAME = "sentdb";
	public static final String TABLE_NAME = "SENT";
	public static final int DATABASE_VERSION = 1;
	public static final String SENT_MESSAGE = "current";
	public static final String DATE = "date";
	public static final String SENDER = "sender";
	public static final String CREATION_STRING = "CREATE TABLE "+ TABLE_NAME +" ( _id integer primary key autoincrement, "+
	SENDER+ " VARCHAR, "+SENT_MESSAGE+ " VARCHAR, "+ DATE+ " DATE)";

	public SENTHelper(Context context){
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATION_STRING);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
		db.execSQL("DROP TABLE IF EXIST "+ TABLE_NAME);
		this.onCreate(db);
	}
	

}
