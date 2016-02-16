package com.klemstinegroup.wub2;

import java.io.File;

import org.tmatesoft.sqljet.core.SqlJetException;
import org.tmatesoft.sqljet.core.SqlJetTransactionMode;
import org.tmatesoft.sqljet.core.table.ISqlJetCursor;
import org.tmatesoft.sqljet.core.table.ISqlJetTable;
import org.tmatesoft.sqljet.core.table.SqlJetDb;

public class SQLDatabase {

	static final String DB_NAME = "Q:\\wub.sql";

	static String trackTable = "CREATE TABLE tracks (key INTEGER PRIMARY KEY AUTOINCREMENT, filename TEXT,artist TEXT, album TEXT, title TEXT, genre TEXT, bitrate INTEGER,sample_rate INTEGER,seconds INTEGER,index TEXT)";

	// String createFirstNameIndexQuery = "CREATE INDEX " + FULL_NAME_INDEX + "
	// ON " + TABLE_NAME + "(" + FIRST_NAME_FIELD + "," + SECOND_NAME_FIELD +
	// ")";
	// String createDateIndexQuery = "CREATE INDEX " + DOB_INDEX + " ON " +
	// TABLE_NAME + "(" + DOB_FIELD + ")";

	public static void delete() {
		File dbFile = new File(DB_NAME);
		dbFile.delete();

	}

	public static void create() throws SqlJetException {
		File dbFile = new File(DB_NAME);
		if (!dbFile.exists()) {
			SqlJetDb db = SqlJetDb.open(dbFile, true);
			db.getOptions().setAutovacuum(true);
			db.beginTransaction(SqlJetTransactionMode.WRITE);
			try {
				db.getOptions().setUserVersion(1);
			} finally {
				db.commit();
			}

			db.createTable(trackTable);
			// db.createIndex(fileIndex);
			db.close();
		}

	}

	public static void main(String[] args) {
		 delete();
		 try {
		 create();
		 } catch (SqlJetException e) {
		 // TODO Auto-generated catch block
		 e.printStackTrace();
		 }

//		try {
//			SqlJetDb db = SqlJetDb.open(new File(DB_NAME), true);
//			ISqlJetTable table = db.getTable("tracks");
//
//			// getting all rows in table, sorted by PK.
//			System.out.println();
//
//			db.beginTransaction(SqlJetTransactionMode.READ_ONLY);
//			ISqlJetCursor cursor;
//			cursor = table.open();
//			if (!cursor.eof()) {
//				do {
//					System.out.println(cursor.getRowId() + " : " + cursor.getString("title"));
//				} while (cursor.next());
//				
//			}
//			cursor.close();
//			db.commit();
//		} catch (SqlJetException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

}