package com.liteon.com.icampusteacher.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.liteon.com.icampusteacher.db.AccountTable.AccountEntry;
import com.liteon.com.icampusteacher.db.ChildLocationTable.ChildLocationEntry;
import com.liteon.com.icampusteacher.db.ChildTable.ChildEntry;
import com.liteon.com.icampusteacher.db.EventListTable.EventListEntry;
import com.liteon.com.icampusteacher.db.WearableTable.WearableEntry;
import com.liteon.com.icampusteacher.util.JSONResponse;
import com.liteon.com.icampusteacher.util.JSONResponse.Parent;
import com.liteon.com.icampusteacher.util.JSONResponse.Student;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {
    private static final String TAG = DBHelper.class.getSimpleName();
	private static DBHelper mInstance = null;
	public static final int DATABASE_VERSION = 3;
	public static final String DATABASE_NAME = "data.db";
	private static final String TEXT_TYPE = " TEXT";
	private static final String INTEGER_TYPE = " INTEGER";
	private static final String COMMA_SEP = ",";

	// ACCOUNT
	public static final String SQL_QUERY_ALL_ACCOUNT_DATA = "SELECT * FROM " + AccountEntry.TABLE_NAME;
	private static final String SQL_CREATE_ACCOUNT_TABLE = "CREATE TABLE " + AccountEntry.TABLE_NAME + " ("
			+ AccountEntry.COLUMN_NAME_USER_NAME + TEXT_TYPE + " PRIMARY KEY" + COMMA_SEP
			+ AccountEntry.COLUMN_NAME_PASSWORD + TEXT_TYPE + COMMA_SEP
            + AccountEntry.COLUMN_NAME_UUID + TEXT_TYPE + COMMA_SEP
            + AccountEntry.COLUMN_NAME_ROLE_TYPE + TEXT_TYPE + COMMA_SEP
			+ AccountEntry.COLUMN_NAME_ACCOUNT_NAME + TEXT_TYPE + COMMA_SEP
            + AccountEntry.COLUMN_NAME_MOBILE_NUMBER + TEXT_TYPE + COMMA_SEP
            + AccountEntry.COLUMN_NAME_GIVEN_NAME + TEXT_TYPE + COMMA_SEP
            + AccountEntry.COLUMN_NAME_NICK_NAME + TEXT_TYPE + COMMA_SEP
			+ AccountEntry.COLUMN_NAME_GENDER + TEXT_TYPE + COMMA_SEP
            + AccountEntry.COLUMN_NAME_HEIGHT + INTEGER_TYPE + COMMA_SEP
            + AccountEntry.COLUMN_NAME_WEIGHT + INTEGER_TYPE + COMMA_SEP
            + AccountEntry.COLUMN_NAME_DOB + TEXT_TYPE + COMMA_SEP
            + AccountEntry.COLUMN_NAME_TOKEN + TEXT_TYPE + " )";
	private static final String SQL_DELETE_ACCOUNT_TABLE = "DROP TABLE IF EXISTS " + AccountEntry.TABLE_NAME;

	// Child data
	public static final String SQL_QUERY_ALL_CHILDREN_DATA = "SELECT * FROM " + ChildEntry.TABLE_NAME;
	private static final String SQL_CREATE_CHILDREN_TABLE = "CREATE TABLE " + ChildEntry.TABLE_NAME + " ("
            + ChildEntry.COLUMN_NAME_STUDENT_ID + TEXT_TYPE + " PRIMARY KEY" + COMMA_SEP
            + ChildEntry.COLUMN_NAME_UUID + TEXT_TYPE + COMMA_SEP
            + ChildEntry.COLUMN_NAME_GIVEN_NAME + TEXT_TYPE + COMMA_SEP
            + ChildEntry.COLUMN_NAME_NICK_NAME + TEXT_TYPE + COMMA_SEP
			+ ChildEntry.COLUMN_NAME_GENDER + TEXT_TYPE + COMMA_SEP
            + ChildEntry.COLUMN_NAME_HEIGHT + INTEGER_TYPE + COMMA_SEP
            + ChildEntry.COLUMN_NAME_WEIGHT + INTEGER_TYPE + COMMA_SEP
            + ChildEntry.COLUMN_NAME_CLASS + TEXT_TYPE + COMMA_SEP
            + ChildEntry.COLUMN_NAME_ROLL_NO + TEXT_TYPE + COMMA_SEP
			+ ChildEntry.COLUMN_NAME_IS_DELETED +  INTEGER_TYPE + COMMA_SEP
            + ChildEntry.COLUMN_NAME_DOB + TEXT_TYPE + COMMA_SEP
			+ ChildEntry.COLUMN_NAME_GRADE + TEXT_TYPE + COMMA_SEP
			+ ChildEntry.COLUMN_NAME_REGISTARTION_NO + TEXT_TYPE + COMMA_SEP
			+ ChildEntry.COLUMN_NAME_EMERGENCY_CONTACT + TEXT_TYPE +" )";
	private static final String SQL_DELETE_CHILD_TABLE = "DROP TABLE IF EXISTS " + ChildEntry.TABLE_NAME;
	
	// Event list data
	public static final String SQL_QUERY_ALL_EVENT_DATA = "SELECT * FROM " + EventListEntry.TABLE_NAME;
	private static final String SQL_CREATE_EVENT_TABLE = "CREATE TABLE " + EventListEntry.TABLE_NAME + " ("
			+ EventListEntry.COLUMN_NAME_UUID + TEXT_TYPE + " PRIMARY KEY" + COMMA_SEP
			+ EventListEntry.COLUMN_NAME_EVENT_ID + TEXT_TYPE + COMMA_SEP + EventListEntry.COLUMN_NAME_EVENT_SUBSCRIBE
			+ TEXT_TYPE + " )";
	private static final String SQL_DELETE_EVENT_TABLE = "DROP TABLE IF EXISTS " + EventListEntry.TABLE_NAME;

	public static DBHelper getInstance(Context ctx) {
		if (mInstance == null) {
			mInstance = new DBHelper(ctx.getApplicationContext());
		}
		return mInstance;
	}

	// child location data
	public static final String SQL_QUERY_CHILD_LOCATION_DATA = "SELECT * FROM " + ChildLocationEntry.TABLE_NAME;
	private static final String SQL_CREATE_CHILD_LOCAITON_TABLE = "CREATE TABLE " + ChildLocationEntry.TABLE_NAME + " ("
			+ ChildLocationEntry.COLUMN_NAME_STUDENTID + TEXT_TYPE + " PRIMARY KEY" + COMMA_SEP
			+ ChildLocationEntry.COLUMN_NAME_LATITUDE + TEXT_TYPE + COMMA_SEP
            + ChildLocationEntry.COLUMN_NAME_LONGITUDE + TEXT_TYPE + COMMA_SEP
            + ChildLocationEntry.COLUMN_NAME_UPDATE_TIME + INTEGER_TYPE + " )";
	private static final String SQL_DELETE_CHILD_LOCATION_TABLE = "DROP TABLE IF EXISTS " + ChildLocationEntry.TABLE_NAME;

	// Reminder data
    public static final String SQL_QUERY_REMINDER_DATA = "SELECT * FROM " + ReminderTable.ReminderEntry.TABLE_NAME;
    private static final String SQL_CREATE_REMINDER_TABLE = "CREATE TABLE " + ReminderTable.ReminderEntry.TABLE_NAME + " ("
            + ReminderTable.ReminderEntry.COLUMN_NAME_REMINDER_ID + TEXT_TYPE + " PRIMARY KEY" + COMMA_SEP
			+ ReminderTable.ReminderEntry.COLUMN_NAME_SCHOOL_ID + INTEGER_TYPE + COMMA_SEP
            + ReminderTable.ReminderEntry.COLUMN_NAME_COMMENTS + TEXT_TYPE + COMMA_SEP
            + ReminderTable.ReminderEntry.COLUMN_NAME_CREATED_DATE + TEXT_TYPE + COMMA_SEP
            + ReminderTable.ReminderEntry.COLUMN_NAME_CLASS + TEXT_TYPE + " )";
    private static final String SQL_DELETE_REMINDER_TABLE = "DROP TABLE IF EXISTS " + ReminderTable.ReminderEntry.TABLE_NAME;

    private DBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(SQL_CREATE_ACCOUNT_TABLE);
		db.execSQL(SQL_CREATE_CHILDREN_TABLE);
		db.execSQL(SQL_CREATE_EVENT_TABLE);
		db.execSQL(SQL_CREATE_CHILD_LOCAITON_TABLE);
		db.execSQL(SQL_CREATE_REMINDER_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(SQL_DELETE_ACCOUNT_TABLE);
		db.execSQL(SQL_DELETE_CHILD_TABLE);
		db.execSQL(SQL_DELETE_EVENT_TABLE);
		db.execSQL(SQL_DELETE_CHILD_LOCATION_TABLE);
		db.execSQL(SQL_DELETE_REMINDER_TABLE);
		onCreate(db);
	}
	
	public Parent getParentByToken(SQLiteDatabase db, String token) {
		Parent item = new Parent();
		Cursor cursor = db.rawQuery(SQL_QUERY_ALL_ACCOUNT_DATA, null);
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			do {
				item.setAccount_name(cursor.getString(cursor.getColumnIndex(AccountEntry.COLUMN_NAME_ACCOUNT_NAME)));
				item.setPassword(cursor.getString(cursor.getColumnIndex(AccountEntry.COLUMN_NAME_PASSWORD)));
				item.setGiven_name(cursor.getString(cursor.getColumnIndex(AccountEntry.COLUMN_NAME_GIVEN_NAME)));
				item.setUsername(cursor.getString(cursor.getColumnIndex(AccountEntry.COLUMN_NAME_USER_NAME)));
				item.setMobile_number(cursor.getString(cursor.getColumnIndex(AccountEntry.COLUMN_NAME_MOBILE_NUMBER)));
				item.setToken(token);
			} while (cursor.moveToNext());
			cursor.close();
			db.close();
		}
		return item;
	}
	
	public String getAccountToken(SQLiteDatabase db, String name) {
		Cursor c = db.query(AccountEntry.TABLE_NAME, new String[] { AccountEntry.COLUMN_NAME_TOKEN }, "username =?",
				new String[] { name }, null, null, null, null);
		if (c.moveToFirst()) // if the row exist then return the id
			return c.getString(c.getColumnIndex(AccountEntry.COLUMN_NAME_TOKEN));
		return "";
	}

	public void clearAccountToken(SQLiteDatabase db, String token) {
		ContentValues cv = new ContentValues();
		cv.put(AccountEntry.COLUMN_NAME_TOKEN, "");
		db.update(AccountEntry.TABLE_NAME, cv, "token=?", new String [] { token });
		db.close();
	}
	public void updateAccountToken(SQLiteDatabase db, String user, String token) {
        ContentValues cv = new ContentValues();
        cv.put(AccountEntry.COLUMN_NAME_TOKEN, token);
        db.update(AccountEntry.TABLE_NAME, cv, "username=?", new String [] { user });
        db.close();
    }
	public long insertAccount(SQLiteDatabase db, ContentValues value) {

		long ret = db.insert(AccountEntry.TABLE_NAME, "", value);
		db.close();
		return ret;

	}

	public long deleteAccount(SQLiteDatabase db) {
		long ret = db.delete(AccountEntry.TABLE_NAME, null, null);
		db.close();
		return ret;
	}

    public void updateAccount(SQLiteDatabase db, Parent parent) {
        ContentValues cv = new ContentValues();
        String[] args = new String[]{parent.getUsername()};
        cv.put(AccountEntry.COLUMN_NAME_ACCOUNT_NAME, parent.getAccount_name());
        cv.put(AccountEntry.COLUMN_NAME_MOBILE_NUMBER, parent.getMobile_number());
        cv.put(AccountEntry.COLUMN_NAME_PASSWORD, parent.getPassword());
        db.update(AccountEntry.TABLE_NAME, cv, "username=?", args);
    }

	public void insertChildList(SQLiteDatabase db, List<Student> childList) {
		for (Student item : childList) {
			ContentValues cv = new ContentValues();
			cv.put(ChildEntry.COLUMN_NAME_UUID, item.getUuid());
			cv.put(ChildEntry.COLUMN_NAME_GIVEN_NAME, item.getName());
			cv.put(ChildEntry.COLUMN_NAME_NICK_NAME, item.getNickname());
			cv.put(ChildEntry.COLUMN_NAME_GENDER, item.getGender());
			cv.put(ChildEntry.COLUMN_NAME_DOB, item.getDob());
			cv.put(ChildEntry.COLUMN_NAME_HEIGHT, item.getHeight());
			cv.put(ChildEntry.COLUMN_NAME_WEIGHT, item.getWeight());
			cv.put(ChildEntry.COLUMN_NAME_ROLL_NO, item.getRoll_no());
			cv.put(ChildEntry.COLUMN_NAME_CLASS, item.get_class());
			cv.put(ChildEntry.COLUMN_NAME_STUDENT_ID, item.getStudent_id());
			cv.put(ChildEntry.COLUMN_NAME_GRADE, item.getGrade());
			cv.put(ChildEntry.COLUMN_NAME_REGISTARTION_NO, item.getRegistartion_no());
			cv.put(ChildEntry.COLUMN_NAME_EMERGENCY_CONTACT, item.getEmergency_contact());

            long ret = db.insert(ChildEntry.TABLE_NAME, null, cv);
            Log.d(TAG, "insert " + item.getNickname() + "RET is " + ret);
		}
		db.close();
	}

	public void updateChildByStudentId(SQLiteDatabase db, Student item) {
        String studentID = item.getStudent_id();
	    ContentValues cv = new ContentValues();
        cv.put(ChildEntry.COLUMN_NAME_UUID, item.getUuid());
        cv.put(ChildEntry.COLUMN_NAME_GIVEN_NAME, item.getName());
        cv.put(ChildEntry.COLUMN_NAME_NICK_NAME, item.getNickname());
        cv.put(ChildEntry.COLUMN_NAME_GENDER, item.getGender());
        cv.put(ChildEntry.COLUMN_NAME_DOB, item.getDob());
        cv.put(ChildEntry.COLUMN_NAME_HEIGHT, item.getHeight());
        cv.put(ChildEntry.COLUMN_NAME_WEIGHT, item.getWeight());
        cv.put(ChildEntry.COLUMN_NAME_ROLL_NO, item.getRoll_no());
        cv.put(ChildEntry.COLUMN_NAME_CLASS, item.get_class());
		cv.put(ChildEntry.COLUMN_NAME_GRADE, item.getGrade());
		cv.put(ChildEntry.COLUMN_NAME_REGISTARTION_NO, item.getRegistartion_no());
		cv.put(ChildEntry.COLUMN_NAME_EMERGENCY_CONTACT, item.getEmergency_contact());

		int ret = db.update(ChildEntry.TABLE_NAME, cv, "student_id=?", new String[] { studentID });
    }

    public Student getChildByStudentID(SQLiteDatabase db, String studentId) {
        Cursor cursor = db.query(ChildEntry.TABLE_NAME, null, "student_id=?",
                new String[] { studentId }, null, null, null, null);
        if (cursor.moveToFirst()) {
            Student item = new Student();
            item.setUuid(cursor.getString(cursor.getColumnIndex(ChildEntry.COLUMN_NAME_UUID)));
            item.setName(cursor.getString(cursor.getColumnIndex(ChildEntry.COLUMN_NAME_GIVEN_NAME)));
            item.setNickname(cursor.getString(cursor.getColumnIndex(ChildEntry.COLUMN_NAME_NICK_NAME)));
            item.setGender(cursor.getString(cursor.getColumnIndex(ChildEntry.COLUMN_NAME_GENDER)));
            item.setDob(cursor.getString(cursor.getColumnIndex(ChildEntry.COLUMN_NAME_DOB)));
            item.setHeight(Integer.toString(cursor.getInt(cursor.getColumnIndex(ChildEntry.COLUMN_NAME_HEIGHT))));
            item.setWeight(Integer.toString(cursor.getInt(cursor.getColumnIndex(ChildEntry.COLUMN_NAME_WEIGHT))));
            item.setRoll_no(Integer.parseInt(cursor.getString(cursor.getColumnIndex(ChildEntry.COLUMN_NAME_ROLL_NO))));
            item.set_class(cursor.getString(cursor.getColumnIndex(ChildEntry.COLUMN_NAME_CLASS)));
            item.setStudent_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex(ChildEntry.COLUMN_NAME_STUDENT_ID))));
            item.setGrade(cursor.getString(cursor.getColumnIndex(ChildEntry.COLUMN_NAME_GRADE)));
            item.setRegistartion_no(cursor.getString(cursor.getColumnIndex(ChildEntry.COLUMN_NAME_REGISTARTION_NO)));
            item.setEmergency_contact(cursor.getString(cursor.getColumnIndex(ChildEntry.COLUMN_NAME_EMERGENCY_CONTACT)));
            return item;
        }
        return null;
    }

    public void deleteChildByStudentID(SQLiteDatabase db, String studentId) {
		long ret = db.delete(ChildEntry.TABLE_NAME, ChildEntry.COLUMN_NAME_STUDENT_ID + "=" + studentId, null);
		db.close();
		return ;
	}

	public void clearChildList(SQLiteDatabase db) {
		db.execSQL("delete from "+ ChildEntry.TABLE_NAME);
		db.close();
	}

	public boolean isChildExist(SQLiteDatabase db, String studentId) {
		if (TextUtils.isEmpty(studentId)) {
			return false;
		}
		Cursor c = db.query(ChildEntry.TABLE_NAME, null, "student_id =?", new String[] { studentId }, null, null, null, null);
		if (c.moveToFirst()) { // if the row exist then return the id
			c.close();
			return true;
		}
		c.close();
		return false;
	}

	public List<Student> queryChildList(SQLiteDatabase db) {
		List<Student> list = new ArrayList<>();
		Cursor cursor = db.rawQuery(SQL_QUERY_ALL_CHILDREN_DATA, null);
		if (cursor.getCount() > 0) {
			cursor.moveToFirst();
			do {
				if (cursor.getInt(cursor.getColumnIndex(ChildEntry.COLUMN_NAME_IS_DELETED)) != 0) {
					continue;
				}
				Student item = new Student();
				item.setUuid(cursor.getString(cursor.getColumnIndex(ChildEntry.COLUMN_NAME_UUID)));
				item.setName(cursor.getString(cursor.getColumnIndex(ChildEntry.COLUMN_NAME_GIVEN_NAME)));
				item.setNickname(cursor.getString(cursor.getColumnIndex(ChildEntry.COLUMN_NAME_NICK_NAME)));
				item.setGender(cursor.getString(cursor.getColumnIndex(ChildEntry.COLUMN_NAME_GENDER)));
				item.setDob(cursor.getString(cursor.getColumnIndex(ChildEntry.COLUMN_NAME_DOB)));
				item.setHeight(Integer.toString(cursor.getInt(cursor.getColumnIndex(ChildEntry.COLUMN_NAME_HEIGHT))));
				item.setWeight(Integer.toString(cursor.getInt(cursor.getColumnIndex(ChildEntry.COLUMN_NAME_WEIGHT))));
				item.setRoll_no(Integer.parseInt(cursor.getString(cursor.getColumnIndex(ChildEntry.COLUMN_NAME_ROLL_NO))));
				item.set_class(cursor.getString(cursor.getColumnIndex(ChildEntry.COLUMN_NAME_CLASS)));
				item.setStudent_id(Integer.parseInt(cursor.getString(cursor.getColumnIndex(ChildEntry.COLUMN_NAME_STUDENT_ID))));
				item.setGrade(cursor.getString(cursor.getColumnIndex(ChildEntry.COLUMN_NAME_GRADE)));
                item.setRegistartion_no(cursor.getString(cursor.getColumnIndex(ChildEntry.COLUMN_NAME_REGISTARTION_NO)));
                item.setEmergency_contact(cursor.getString(cursor.getColumnIndex(ChildEntry.COLUMN_NAME_EMERGENCY_CONTACT)));
				list.add(item);
			} while (cursor.moveToNext());
			cursor.close();
			db.close();
		}
		return list;
	}

    public void insertChild(SQLiteDatabase db, Student item) {
        ContentValues cv = new ContentValues();
        cv.put(ChildEntry.COLUMN_NAME_UUID, item.getUuid());
        cv.put(ChildEntry.COLUMN_NAME_NICK_NAME, item.getNickname());
        cv.put(ChildEntry.COLUMN_NAME_ROLL_NO, item.getRoll_no());
        cv.put(ChildEntry.COLUMN_NAME_STUDENT_ID, item.getStudent_id());
		cv.put(ChildEntry.COLUMN_NAME_GENDER, item.getGender());
		cv.put(ChildEntry.COLUMN_NAME_DOB, item.getDob());
		cv.put(ChildEntry.COLUMN_NAME_HEIGHT, item.getHeight());
		cv.put(ChildEntry.COLUMN_NAME_WEIGHT, item.getWeight());

        long ret = db.insert(ChildEntry.TABLE_NAME, null, cv);
        Log.d(TAG, "insert " + item.getNickname() + "RET is " + ret);
    }
	
	public void updateChildData(SQLiteDatabase db, Student student) {
		ContentValues cv = new ContentValues();
		String[] args = new String[]{student.getStudent_id()};
		cv.put(ChildEntry.COLUMN_NAME_UUID, student.getUuid());
		cv.put(ChildEntry.COLUMN_NAME_IS_DELETED, student.getIsDelete());
		db.update(ChildEntry.TABLE_NAME, cv, "student_id=?", args);
	}

    public void clearReminderList(SQLiteDatabase db) {
        db.execSQL("delete from "+ ReminderTable.ReminderEntry.TABLE_NAME);
        db.close();
    }

	public List<JSONResponse.Contents> queryReminderData(SQLiteDatabase db) {
        List<JSONResponse.Contents> list = new ArrayList<>();
        Cursor cursor = db.rawQuery(SQL_QUERY_REMINDER_DATA, null);
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                JSONResponse.Contents item = new JSONResponse.Contents();
                item.setReminder_id(cursor.getString(cursor.getColumnIndex(ReminderTable.ReminderEntry.COLUMN_NAME_REMINDER_ID)));
                item.setClass_no(cursor.getString(cursor.getColumnIndex(ReminderTable.ReminderEntry.COLUMN_NAME_CLASS)));
                item.setComments(cursor.getString(cursor.getColumnIndex(ReminderTable.ReminderEntry.COLUMN_NAME_COMMENTS)));
                item.setCreated_date(cursor.getString(cursor.getColumnIndex(ReminderTable.ReminderEntry.COLUMN_NAME_CREATED_DATE)));
                item.setSchool_id(cursor.getInt(cursor.getColumnIndex(ReminderTable.ReminderEntry.COLUMN_NAME_SCHOOL_ID)));
                list.add(item);
            } while (cursor.moveToNext());
            cursor.close();
            db.close();
        }
        return list;
    }

    public void deleteReminderById(SQLiteDatabase db, String reminderId) {
        long ret = db.delete(ReminderTable.ReminderEntry.TABLE_NAME, ReminderTable.ReminderEntry.COLUMN_NAME_REMINDER_ID + "=" + reminderId, null);
        Log.d(TAG, "Delete Reminder id " + reminderId + " ,Ret :" + ret);
        db.close();
        return ;
    }

    public void insertReminderList(SQLiteDatabase db, List<JSONResponse.Contents> reminderList) {
        for (JSONResponse.Contents item : reminderList) {
            ContentValues cv = new ContentValues();
            cv.put(ReminderTable.ReminderEntry.COLUMN_NAME_REMINDER_ID, item.getReminder_id());
            cv.put(ReminderTable.ReminderEntry.COLUMN_NAME_CLASS, item.getClass_no());
            cv.put(ReminderTable.ReminderEntry.COLUMN_NAME_COMMENTS, item.getComments());
            cv.put(ReminderTable.ReminderEntry.COLUMN_NAME_CREATED_DATE, item.getCreated_date());
            cv.put(ReminderTable.ReminderEntry.COLUMN_NAME_SCHOOL_ID, item.getSchool_id());

            long ret = db.insert(ReminderTable.ReminderEntry.TABLE_NAME, null, cv);
            Log.d(TAG, "insert Reminder id " + item.getReminder_id() + "RET is " + ret);
        }
        db.close();
    }

    public void updateReminderById(SQLiteDatabase db, JSONResponse.Contents item) {
        String reminderId = item.getReminder_id();
        ContentValues cv = new ContentValues();
        cv.put(ReminderTable.ReminderEntry.COLUMN_NAME_CLASS, item.getClass_no());
        cv.put(ReminderTable.ReminderEntry.COLUMN_NAME_COMMENTS, item.getComments());
        cv.put(ReminderTable.ReminderEntry.COLUMN_NAME_CREATED_DATE, item.getCreated_date());
        cv.put(ReminderTable.ReminderEntry.COLUMN_NAME_SCHOOL_ID, item.getSchool_id());

        int ret = db.update(ReminderTable.ReminderEntry.TABLE_NAME, cv, "reminder_id=?", new String[] { reminderId });
        Log.d(TAG, "Update Reminder id " + item.getReminder_id() + "RET is " + ret);
    }

    public String getBlueToothAddrByStudentId(SQLiteDatabase db, String student_id) {
        Cursor c = db.query(WearableEntry.TABLE_NAME, new String[] { WearableEntry.COLUMN_NAME_ADDR}, "student_id =?",
                new String[] { student_id }, null, null, null, null);
        if (c.moveToFirst()) // if the row exist then return the id
            return c.getString(c.getColumnIndex(WearableEntry.COLUMN_NAME_ADDR));
        return "";
	}

    public boolean isWearableExist(SQLiteDatabase db, String uuid) {
        if (TextUtils.isEmpty(uuid)) {
            return false;
        }
        Cursor c = db.query(WearableEntry.TABLE_NAME, null, "uuid =?", new String[] { uuid }, null, null, null, null);
        if (c.moveToFirst()) { // if the row exist then return the id
            c.close();
            return true;
        }
        c.close();
        return false;
    }

	private void createDummyData(SQLiteDatabase db) {

	    //Child Data
        List<Student> studentList = new ArrayList<>();
		Student item = new Student();
		//student 1
		item.setUuid("");
		item.setName("Name");
		item.setNickname("Pink");
		item.setGender("FeMale");
		item.setDob("1995-01-01");
		item.setHeight("150");
		item.setWeight("40");
		item.setRoll_no(11);
		item.set_class("1");
		item.setStudent_id(1);
		studentList.add(item);

		//student 2
        item = new Student();
		item.setUuid("");
		item.setName("Name");
		item.setNickname("Gibert");
		item.setGender("Male");
		item.setDob("1996-03-01");
		item.setHeight("140");
		item.setWeight("40");
		item.setRoll_no(12);
		item.set_class("2");
		item.setStudent_id(2);
        studentList.add(item);

		insertChildList(db, studentList);

		//Parent Data
        ContentValues cv = new ContentValues();
        cv.put(AccountEntry.COLUMN_NAME_USER_NAME, "admin3@parent.com");
        cv.put(AccountEntry.COLUMN_NAME_PASSWORD, "password");
        cv.put(AccountEntry.COLUMN_NAME_TOKEN, "E8C33BCCC8A1E1627B28B65B0B4DE829");
        cv.put(AccountEntry.COLUMN_NAME_ACCOUNT_NAME, "LO Parent User");
        cv.put(AccountEntry.COLUMN_NAME_MOBILE_NUMBER, "9030008893");
        insertAccount(getWritableDatabase(), cv);

        //location Data child 1
        cv.clear();
        cv.put(ChildLocationEntry.COLUMN_NAME_STUDENTID, "1");
        cv.put(ChildLocationEntry.COLUMN_NAME_LATITUDE, "25.029600");
        cv.put(ChildLocationEntry.COLUMN_NAME_LONGITUDE, "121.533260");
        cv.put(ChildLocationEntry.COLUMN_NAME_UPDATE_TIME, Calendar.getInstance().getTimeInMillis());
        insertChildLocation(getWritableDatabase(), cv);
        //location Data child 2
        cv.clear();
        cv.put(ChildLocationEntry.COLUMN_NAME_STUDENTID, "2");
        cv.put(ChildLocationEntry.COLUMN_NAME_LATITUDE, "25.039594");
        cv.put(ChildLocationEntry.COLUMN_NAME_LONGITUDE, "121.559538");
        cv.put(ChildLocationEntry.COLUMN_NAME_UPDATE_TIME, Calendar.getInstance().getTimeInMillis());
        insertChildLocation(getWritableDatabase(), cv);
	}

    public void insertChildLocation(SQLiteDatabase db, ContentValues cv) {
        db.insert(ChildLocationEntry.TABLE_NAME, null, cv);
        db.close();
	}

	public LatLng getChildLocationByID(SQLiteDatabase db, String studentID) {
        Cursor c = db.query(ChildLocationEntry.TABLE_NAME, new String[] { ChildLocationEntry.COLUMN_NAME_LATITUDE, ChildLocationEntry.COLUMN_NAME_LONGITUDE}, "student_id =?",
                new String[] { studentID }, null, null, null, null);
        if (c.moveToFirst()) {// if the row exist then return the id
            String lat = c.getString(c.getColumnIndex(ChildLocationEntry.COLUMN_NAME_LATITUDE));
            String lng = c.getString(c.getColumnIndex(ChildLocationEntry.COLUMN_NAME_LONGITUDE));
            LatLng location = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
            return location;
        }
        return null;
    }

    public void deletaAll(SQLiteDatabase db) {
        db.delete(AccountEntry.TABLE_NAME, null, null);
        db.delete(ChildEntry.TABLE_NAME, null, null);
        db.delete(WearableEntry.TABLE_NAME, null, null);
        db.delete(ChildLocationEntry.TABLE_NAME, null, null);
        db.delete(WearableEntry.TABLE_NAME, null, null);
        db.close();
    }
}
