package in.ac.nith.digitalportal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class HotOrNot {
	
	public static final String KEY_ROWID = "_id";
	public static final String NOTIFICATIONS = "notifications";
	
	private static final String DATABASE_NAME = "notifdb";
	private static final String DATABASE_TABLE = "notifications";
	private static final int DATABASE_VERSION = 1;
	private DbHelper ourHelper;
	private final Context ourContext;
	private SQLiteDatabase ourDatabase;
	
	private static class DbHelper extends SQLiteOpenHelper{

		public DbHelper(Context context) {
			super(context,DATABASE_NAME,null,DATABASE_VERSION);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			db.execSQL("CREATE TABLE " + DATABASE_TABLE+" (" +
			KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
			NOTIFICATIONS + " TEXT NOT NULL);"
					);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			db.execSQL("DROP TABLE IF EXISTS "+DATABASE_TABLE);
			onCreate(db);
		}
		
	}
	
		public HotOrNot(Context c){
			ourContext=c;
		}
		
		public HotOrNot open() throws SQLException{
			ourHelper=new DbHelper(ourContext);
			ourDatabase = ourHelper.getWritableDatabase();
			return this;
		}
		public void close(){
			ourHelper.close();
		}

		public long createEntry(String name) throws SQLException{
			// TODO Auto-generated method stub
			ContentValues cv = new ContentValues();
			cv.put(NOTIFICATIONS,name);
			return ourDatabase.insert(DATABASE_TABLE, null,cv);
		}

		public String[] getData() throws SQLException{
			// TODO Auto-generated method stub
			String[] columns=new String[]{KEY_ROWID,NOTIFICATIONS};
			Cursor c = ourDatabase.query(DATABASE_TABLE, columns, null, null,null, null, null); 
			c.moveToLast();
			String []result=new String[c.getCount()];
			int i=0;
			int iNotifications=c.getColumnIndex(NOTIFICATIONS);
			for(c.moveToLast();!c.isBeforeFirst();c.moveToPrevious()){
				result[i]=c.getString(iNotifications)+"\n";
				i++;
			}
			return result;
		}

		public String getName(long l) throws SQLException{
			// TODO Auto-generated method stub
			String[] columns=new String[]{KEY_ROWID,NOTIFICATIONS};
			Cursor c = ourDatabase.query(DATABASE_TABLE, columns, KEY_ROWID+"="+l, null, null, null, null);
			if(c!=null){
				c.moveToFirst();
				String name = c.getString(1);
				return name;
			}
			
			return null;
		}

		public void updateEntry(long lRow, String mName, String mHotness) throws SQLException{
			// TODO Auto-generated method stub
			ContentValues cvUpdate = new ContentValues();
			cvUpdate.put(NOTIFICATIONS, mName);
			ourDatabase.update(DATABASE_TABLE, cvUpdate, KEY_ROWID+"="+lRow,null);
			
		}

		public void delete(long lRow1) throws SQLException{
			// TODO Auto-generated method stub
			ourDatabase.delete(DATABASE_TABLE, KEY_ROWID+"="+lRow1, null);
			
		}
}
