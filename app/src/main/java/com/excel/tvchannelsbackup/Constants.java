package com.excel.tvchannelsbackup;

public class Constants {

	/*
	 * MNEMONICS
	 * 
	 * SP -> SharedPreferences
	 * SU -> SuperUser Commands
	 * DV -> Default Values
	 * NC -> Network Connections
	 * SS -> Screen Size
	 * GN -> Generic Values
	 * RC -> Activity Request Codes for on Activity Result
	 * RTC -> Activity Result Codes for on Activity Result
	 * URL -> Links
	 * JSON -> JSON 'for' messages
	 * BR -> Personalized Broadcast Receivers
	 * 
	 * */
	
	// SharedPreferences Keys
	final static String SP_SETTINGS = "settings";
	final static String SP_FIRST_ATTEMPT_YES_VALUE = "yes";
	final static String SP_FIRST_ATTEMPT_NO_VALUE = "no";
	final static String SP_FIRST_ATTEMPT_DEFAULT_VALUE = SP_FIRST_ATTEMPT_YES_VALUE;
	final static String SP_SETTINGS_FIRST_ATTEMPT_KEY = "first_attempt";
	final static String SP_SETTINGS_LANGUAGE_KEY = "language";
	final static String SP_SETTINGS_COUNTRY_KEY = "country";
	final static String SP_SETTINGS_NETWORK_CONNECTION_KEY = "network";
	final static String SP_SETTINGS_SCREEN_SIZE_KEY = "screen_size";
	final static String SP_SETTINGS_SERVER_IP_KEY = "cms_server_ip";
	final static String SP_SETTINGS_KEEP_CONFIGURATION_ACTIVITY_ON_FRONT = "keep_configuration_activity_on_front";	
	
	final static String SP_SETTINGS_NETWORK_CONNECTION_NOT_SET_VALUE = "Please select a network connection";
	final static String SP_SETTINGS_SCREEN_SIZE_NOT_SET_VALUE = "Not yet adjusted";
	
	// SU Commands
	final static String SU_SET_LOCALE_LANGUAGE = "su -f setprop persist.sys.language ";
	final static String SU_SET_LOCALE_COUNTRY  = "su -f setprop persist.sys.country ";
	
	// Default Values
	final static String DV_COUNTRY  						= "United States";
	final static String DV_COUNTRY_CODE  					= "US";
	final static String DV_LANGUAGE 						= "English";
	final static String DV_LANGUAGE_CODE 					= "en";
	final static String DV_IP_ADDRESS	 					= "Not set";
	
	// Network Connections
	final static String NC_WIFI 							= "WIFI";
	final static String NC_ETHERNET 						= "Ethernet";
	
	// Possible Screen Sizes
	final static String SS_480I						= "480i";
	final static String SS_480P						= "480p";
	final static String SS_480						= "480";
	final static String SS_576I						= "576i";
	final static String SS_576P						= "576p";
	final static String SS_576						= "576";
	final static String SS_720I						= "720i";
	final static String SS_720P						= "720p";
	final static String SS_720						= "720";
	final static String SS_1080I					= "1080i";
	final static String SS_1080P					= "1080p";
	final static String SS_1080						= "1080";
	final static String SS_1280						= "1280";
	final static String SS_1920						= "1920";
	final static String SS_SET_KEY					= "screen_size_set";
	final static String SS_SET_VALUE				= "Screen size has been set";
	final static String SS_NOT_SET_VALUE			= "Not yet adjusted";
	final static String SS_X_KEY					= "screen_size_x";
	final static String SS_Y_KEY					= "screen_size_y";
	final static String SS_WIDTH_KEY				= "screen_size_width";
	final static String SS_HEIGHT_KEY				= "screen_size_height";
	
	// Generic Values
	final static String GN_YES												= "yes";
	final static String GN_NO												= "no";
	final static String GN_ACTIVITY_TO_SERVICE_KEY							= "service_data_key";
	final static String GN_ALARM_START_FOR_SCREEN_ADJUSTMENT_VALUE			= "start_alarm";
	final static String GN_ALARM_STOP_FOR_SCREEN_ADJUSTMENT_VALUE			= "stop_alarm";
	final static String GN_BRING_CONFIGURATION_ACTIVITY_TO_FRONT_KEY_VALUE	= "bring_front_config";
	
	// Activity Request Codes for on Activity Result
	final static int RC_SET_SCREEN_SIZE										= 0;
	
	// Activity Result Codes for on Activity Result
	final static int RTC_SET_SCREEN_SIZE_SET					= 1;
	final static int RTC_SET_SCREEN_SIZE_NOT_SET				= 2;
	
	// URL
	final static String URL_WEB_SERVICE							= "appstv/webservice.php";
	
	// JSON 'for' messages
	final static String JSON_FOR_SAVE_MAC_ADDRESS				= "save_mac_address";
	
	// BR -> Personalized Broadcast Receivers
	final static String BR_RETRIEVE_CMS_SERVER_IP					= "retrieve_cms_server_ip";
	final static String BR_RECEIVE_CMS_SERVER_IP					= "receive_cms_server_ip";
	final static String BR_START_RESTORE_SERVICE					= "start_restore_service";
	final static String BR_RETURNED_CMS_SERVER_IP_KEY				= "returned_cms_server_ip";


	public static String PERMISSION_SPFS = "permission";

	public static String IS_PERMISSION_GRANTED = "is_permission_granted";
	public static String PERMISSION_GRANTED_YES = "yes";
	public static String PERMISSION_GRANTED_NO = "no";
}
