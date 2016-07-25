package com.etingemabian.blacksms;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {
	public  static final int DATABASE_VERSION = 1;
	public static final  String DATABASE_NAME = "smsdb";
	public static final  String TABLE_NAME = "SMSINBOX";
	public  static final String ADDRESS = "address";
	public static final String BODY = "body";
	public static final String DATE = "date";
	public static final  String SENT = "sent";
	public static  final String String_create = "CREATE TABLE "+TABLE_NAME+" (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
			+ADDRESS+" VARCHAR, "+ BODY+ " VARCHAR, "+ DATE+" DATE)";

	
	public DbHelper(Context context) {
		super(context, DATABASE_NAME, null,  DATABASE_VERSION);
		
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(String_create);
		
	
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXIST"+ TABLE_NAME);
		
	
		this.onCreate(db);
	}
	
	
	
	 
	}


