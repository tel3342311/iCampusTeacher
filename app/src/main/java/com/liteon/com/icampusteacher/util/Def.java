package com.liteon.com.icampusteacher.util;

public class Def {
	//SharePreference
	public final static String SHARE_PREFERENCE = "com.liteon.icampusguardian.PREFERENCE_FILE_KEY";
	public final static String SP_USER_TERM_READ = "com.liteon.icampusguardian.SP_USER_TERM_READ";
	public final static String SP_CURRENT_STUDENT = "com.liteon.icampusguardian.SP_CURRENT_STUDENT";
	public static final String SP_LOGIN_TOKEN = "com.liteon.icampusguardian.SP_LOGIN_TOKEN";
	public static final String SP_TARGET_CARLOS = "com.liteon.icampusguardian.SP_TARGET_CARLOS";
	public static final String SP_TARGET_STEPS = "com.liteon.icampusguardian.SP_TARGET_STEPS";
	public static final String SP_TARGET_WALKING = "com.liteon.icampusguardian.SP_TARGET_WALKING";
	public static final String SP_TARGET_RUNNING = "com.liteon.icampusguardian.SP_TARGET_RUNNING";
	public static final String SP_TARGET_CYCLING = "com.liteon.icampusguardian.SP_TARGET_CYCLING";
	public static final String SP_TARGET_SLEEPING = "com.liteon.icampusguardian.SP_TARGET_SLEEPING";
	public static final String SP_ALARM_MAP = "com.liteon.icampusguardian.SP_ALARM_MAP";
	public static final String SP_TARGET_MAP = "com.liteon.icampusguardian.SP_TARGET_MAP";
	public static final String SP_IMPROVE_PLAN = "com.liteon.icampusguardian.SP_IMPROVE_PLAN";
	public static final String SP_TEACHER_PLAN = "com.liteon.icampusguardian.SP_TEACHER_PLAN";
	public static final String SP_PHOTO_MAP = "com.liteon.icampusguardian.SP_PHOTO_MAP";
	public static final String SP_PHOTO_MAP_WATCH = "com.liteon.icampusguardian.SP_PHOTO_MAP_WATCH";
	public static final String SP_GEO_ITEM_MAP = "com.liteon.icampusguardian.SP_GEO_ITEM_MAP";
	public static final String SP_BT_WATCH_ADDRESS = "com.liteon.icampusguardian.SP_BT_WATCH_ADDRESS";
	public static final String SP_CONTACT_LIST = "com.liteon.icampusguardian.SP_BT_WATCH_ADDRESS";
	public static final String SP_STUDENT_ID = "com.liteon.icampusguardian.SP_STUDENT_ID";
	public static final String SP_STUDENT_NAME = "com.liteon.icampusguardian.SP_STUDENT_ID";

	//RET CODE there are two kind of success code
	public static final String RET_SUCCESS_1 = "SUC01";
	public static final String RET_SUCCESS_2 = "WSUC01";
	public static final String RET_ERR_01 = "ERR01";
	public static final String RET_ERR_02 = "ERR02";
    public static final String RET_ERR_16 = "ERR16";
	public static final String RET_ERR_23 = "ERR23";

	//API 01 registration
	public static final String REQUEST_USER_REGISTRATION = "ParentUserRegistration";
	public static final String KEY_ACCOUNT_NAME = "account_name";
	//API 02 login
	public static final String REQUEST_USERLOGIN = "UserLogin";
	public static final String KEY_TYPE_USERLOGIN = "user.UserLogin";
	public static final String KEY_USERNAME = "username";
	public static final String KEY_PASSWORD = "password";
	public static final String KEY_FORCELOGIN = "forcelogin";
	//API 04 Social Login
	public static final String REQUEST_FEDERATEDLOGIN = "FederatedLogin";
    public static final String KEY_SOCIAL_NAME = "name";
    public static final String KEY_SOCIAL_EMAIL = "email";
    public static final String KEY_SOCIAL_TOKEN = "token";
    public static final String KEY_SOCIAL_USERAGENT = "useragent";
    public static final String VALUE_SOCIAL_USERAGENT = "MOBILE";

    //API 06 Update Parent account detail.
	public static final String REQUEST_USER_UPDATE = "UserUpdate";
	public static final String KEY_PHONE_NUMBER = "mobile_number";
	//API 07 get student list
	public static final String REQUEST_GET_CHILDREN_LIST = "StudentList";
	//API 08 get student location 
	public static final String REQUEST_GET_CHILDREN_LOCATION = "StudentLocation";
	
	//API 11 Update FCM Token
	public static final String REQUEST_UPDATE_APP_TOKEN = "MobileAppTokenUpdate";
	public static final String KEY_APP_TOKEN = "appToken";
	public static final String KEY_APP_TYPE = "appType";
	public static final String KEY_APP_TYPE_ANDROID = "android";//fixed input

	//API 14 pair new device
	public static final String REQUEST_PAIR_NEW_DEVICE = "ParentUserDevicePair";
	//API 15 pair new device
	public static final String REQUEST_UNPAIR_DEVICE = "ParentUserDeviceUnPair";
	
	//API 19 get device event report
	public static final String REQUEST_GET_DEVICE_EVENT_REPORT = "DeviceEventReport";
	public static final String KEY_EMAIL = "email";
	//API 32 update child info
	public static final String REQUEST_UPDATE_CHILD_INFO = "StudentUpdate";
	public static final String KEY_STUDENT_ID = "student_id";
	public static final String KEY_NAME = "name";
	public static final String KEY_NICKNAME = "nickname";
	public static final String KEY_CLASS = "101";
	public static final String KEY_ROLL_NO = "roll_no";
	public static final String KEY_HEIGHT  = "height";
	public static final String KEY_WEIGHT  = "weight";
	public static final String KEY_DOB = "dob";
	public static final String KEY_GENDER = "gender";
	public static final String KEY_UUID = "uuid";
	//API 33 reset password
	public static final String REQUEST_PASSWORD_REST = "PasswordResetRequest";
	public static final String KEY_USER_ROLE = "user_role";
	public static final String KEY_PROFILE_NAME = "profile_name";
	public static final String KEY_MOBILE_NUMBER = "mobile_number";
	//API 36 get user detail
	public static final String REQUEST_USER_DETAIL = "UserDetails";
	//API 36 GrantTeacherAccessToSleepData
    public static final String REQUEST_GRANT_TEDETAIL = "GrantTeacherAccessToSleepData";
    public static final String REQUEST_CREATE_REMINDER = "ClassRemindersCreate";
	public static final String REQUEST_VIEW_REMINDER = "ClassRemindersList";
	public static final String REQUEST_DELETE_REMINDER = "ClassRemindersDelete";
	//EVENT ID LIST
	public static final String EVENT_ID_ENTER_SCHOOL = "1";
	public static final String EVENT_ID_LEAVE_SCHOOL = "2";
	public static final String EVENT_ID_SOS_ALERT = "13";
	public static final String EVENT_ID_SOS_REMOVE = "14";
	public static final String EVENT_ID_GPS_LOCATION = "19";
	
	//EVENT DURATION
	public static final String EVENT_DURATION_ONE_DAY = "1";
	public static final String EVENT_DURATION_WEEK = "7";
	public static final String EVENT_DURATION_MONTH = "30";
	
	//Action
	public static final String ACTION_NOTIFY = "com.liteon.icampusguardian.ACTION_NOTIFY";
	public static final String EXTRA_NOTIFY_TYPE = "com.liteon.icampusguardian.EXTRA_NOTIFY_TYPE";
	public static final String EXTRA_SOS_LOCATION = "com.liteon.icampusguardian.EXTRA_SOS_LOCATION";

	//Intent EXTRA
	public static final String EXTRA_DISABLE_USERTREM_BOTTOM = "com.liteon.icampusguardian.EXTRA_DISABLE_USERTREM_BOTTOM";
	public static final String EXTRA_GOTO_MAIN_SETTING = "com.liteon.icampusguardian.EXTRA_GOTO_MAIN_SETTING";
	public static final String EXTRA_CHOOSE_PHOTO_TYPE = "com.liteon.icampusguardian.EXTRA_GOTO_MAIN_EXTRA_CHOOSE_PHOTO_TYPE";
	public static final String EXTRA_GOTO_APP_INFO = "com.liteon.icampusguardian.EXTRA_GOTO_APP_INFO";
	//Choose photo TYPE
	public static final String EXTRA_CHOOSE_CHILD_ICON = "child_icon";
	public static final String EXTRA_CHOOSE_WATCH_ICON = "watch_icon";
	/**
	 * =================================================================
	 *
	 * Action for DataSync Service
	 *
	 * =================================================================
	 */
	public static final String ACTION_REGISTERATION_USER = "com.liteon.icampusguardian.ACTION_REGISTERATION_USER";
	public static final String ACTION_LOGIN_USER = "com.liteon.icampusguardian.ACTION_LOGIN_USER";
	public static final String ACTION_LOGOUT_USER = "com.liteon.icampusguardian.ACTION_LOGOUT_USER";
	public static final String ACTION_GET_STUDENT_LIST = "com.liteon.icampusguardian.ACTION_GET_STUDENT_LIST";
	public static final String ACTION_GET_LOCATION = "com.liteon.icampusguardian.ACTION_GET_LOCATION";
	public static final String ACTION_GET_EVENT_REPORT = "com.liteon.icampusguardian.ACTION_GET_EVENT_REPORT";
	public static final String ACTION_GET_PARENT_DETAIL = "com.liteon.icampusguardian.ACTION_GET_PARENT_DETAIL";
	public static final String ACTION_PAIR_NEW_DEVICE = "com.liteon.icampusguardian.ACTION_PAIR_NEW_DEVICE";
	public static final String ACTION_UNPAIR_DEVICE = "com.liteon.icampusguardian.ACTION_UNPAIR_NEW_DEVICE";
	public static final String ACTION_UPDATE_APP_TOKEN = "com.liteon.icampusguardian.ACTION_UPDATE_APP_TOKEN";
	public static final String ACTION_UPDATE_STUDENT_DETAIL = "com.liteon.icampusguardian.ACTION_UPDATE_STUDENT_DETAIL";
	public static final String ACTION_UPDATE_PARENT_DETAIL = "com.liteon.icampusguardian.ACTION_UPDATE_PARENT_DETAIL";
	public static final String ACTION_RESET_PASSWORD = "com.liteon.icampusguardian.ACTION_RESET_PASSWORD";

	//Action fro reponse of DataSync Service
	public static final String ACTION_ERROR_NOTIFY = "com.liteon.icampusguardian.ACTION_ERROR_NOTIFY";
	public static final String EXTRA_ERROR_MESSAGE = "com.liteon.icampusguardian.EXTRA_ERROR_MESSAGE";

	// Message types sent from the BluetoothChatService Handler
	public static final int MESSAGE_STATE_CHANGE = 1;
	public static final int MESSAGE_READ = 2;
	public static final int MESSAGE_WRITE = 3;
	public static final int MESSAGE_DEVICE_NAME = 4;
	public static final int MESSAGE_TOAST = 5;
	// Key names received from the BluetoothAgent Handler
	public static final String DEVICE_NAME = "device_name";
	public static final String TOAST = "toast";

	public static final String BT_ERR_UNABLE_TO_CONNECT = "Unable to connect device";
	public static final String EXTRA_BT_ADDR = "com.liteon.icampusguardian.EXTRA_BT_ADDR";
}
