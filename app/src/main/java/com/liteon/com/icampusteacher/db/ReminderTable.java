package com.liteon.com.icampusteacher.db;
import android.provider.BaseColumns;

public class ReminderTable {

	public ReminderTable() {}
	public static final String AUTHORITY = "com.liteon.icampusguardian";
	
	public static abstract class ReminderEntry implements BaseColumns {
		public static final String TABLE_NAME = "reminder_entry";
		public static final String COLUMN_NAME_REMINDER_ID = "reminder_id";
        public static final String COLUMN_NAME_SCHOOL_ID = "school_id";
        public static final String COLUMN_NAME_CLASS = "class";
        public static final String COLUMN_NAME_COMMENTS = "comments";
		public static final String COLUMN_NAME_CREATED_DATE = "created_date";
	}
	
}
