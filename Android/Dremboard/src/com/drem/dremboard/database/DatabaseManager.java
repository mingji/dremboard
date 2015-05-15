package com.drem.dremboard.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseManager {
	private static final String mDbName = "ScienceFun.db";
	public static final int mDbVersion = 1;

	private OpenHelper mOpener;
	private SQLiteDatabase mDb;

	private Context mContext;

	public DatabaseManager(Context context) {
		this.mContext = context;
		this.mOpener = new OpenHelper(context, mDbName, null, mDbVersion);
		mDb = mOpener.getWritableDatabase();
	}

	private class OpenHelper extends SQLiteOpenHelper {

		public OpenHelper(Context context, String dbName, CursorFactory factory,
				int version) {
			super(context, dbName, null, version);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub

			db.execSQL(DbSchema.UserTable.TABLE_USER_CREATE);

			db.execSQL(DbSchema.GradeTable.TABLE_GRADE_CREATE);
			db.execSQL(DbSchema.ChapterTable.TABLE_CHAPTER_CREATE);
			db.execSQL(DbSchema.ConceptTable.TABLE_CONCEPT_CREATE);

			db.execSQL(DbSchema.VideoTable.TABLE_VIDEO_CREATE);
			db.execSQL(DbSchema.ReferenceTable.TABLE_REFERENCE_CREATE);
			db.execSQL(DbSchema.NoteTable.TABLE_NOTE_CREATE);
			db.execSQL(DbSchema.UpdateTable.TABLE_UPDATE_CREATE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub

			db.execSQL("drop table if exists " + DbSchema.UserTable.TABLE_NAME);

			db.execSQL("drop table if exists " + DbSchema.GradeTable.TABLE_NAME);
			db.execSQL("drop table if exists " + DbSchema.ChapterTable.TABLE_NAME);
			db.execSQL("drop table if exists " + DbSchema.ConceptTable.TABLE_NAME);

			db.execSQL("drop table if exists " + DbSchema.VideoTable.TABLE_NAME);
			db.execSQL("drop table if exists " + DbSchema.ReferenceTable.TABLE_NAME);
			db.execSQL("drop table if exists " + DbSchema.NoteTable.TABLE_NAME);
			db.execSQL("drop table if exists " + DbSchema.UpdateTable.TABLE_NAME);

			onCreate(db);
		}

	}
}