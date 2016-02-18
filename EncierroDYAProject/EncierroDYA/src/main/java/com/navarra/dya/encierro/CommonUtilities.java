package com.navarra.dya.encierro;

import android.content.Context;
import android.content.Intent;

/**
 * Created by
 * @author Paula Remirez Ruiz
 * @version 2.0 (beta)
 */
public final class CommonUtilities {
	
	// The server registration url
    static final String unregister_gcm_server = "http://www.doyang.es/gcm_server_php/unregister.php";
    static final String url_new_injured = "http://www.doyang.es/android_connect/new_injured.php";
    static final String url_injured_details = "http://www.doyang.es/android_connect/get_injured_details.php";
    static final String url_update_injured = "http://www.doyang.es/android_connect/update_injured.php";
    static final String url_delete_injured = "http://www.doyang.es/android_connect/delete_injured.php";
    static final String url_login_details = "http://www.doyang.es/android_connect/get_login_details.php";
    static final String url_all_injured = "http://www.doyang.es/android_connect/get_all_injured.php";
    static final String url_stand_injured = "http://www.doyang.es/android_connect/get_stand_injured.php";
    static final String url_one_injured = "http://www.doyang.es/android_connect/get_one_injured.php";
    static final String asignInjuredResource = "http://www.doyang.es/android_connect/asignInjuredResource.php";
    static final String updateInjuredHealthCenter = "http://www.doyang.es/android_connect/updateInjuredHealthCenter.php";
    static final String updateUser = "http://www.doyang.es/android_connect/updateUser.php";
    static final String is_test = "http://www.doyang.es/android_connect/isTest.php";
    static final String updateGPS= "http://www.doyang.es/android_connect/updateGPS.php";
    static final String getStands = "http://www.doyang.es/android_connect/getStands.php";
    static final String sendSMS = "http://www.doyang.es/android_connect/sendSMS.php";

    // JSON Node names
    static final String TAG = "DYA encierro";
    static final String TAG_USERNAME = "username";
    static final String TAG_USER = "user";
    static final String TAG_PASS= "password";
    static final String TAG_SUCCESS = "success";
    static final String TAG_INJURED = "injured";
    static final String TAG_ID = "id";
    static final String TAG_GENDER = "gender";
    static final String TAG_OLD = "old";
    static final String TAG_STAND = "stand";
    static final String TAG_AMBULANCE = "ambulance";
    static final String TAG_HEALTH_CENTER = "health_center";
    static final String TAG_CONSCIOUS = "conscious";
    static final String TAG_GORING_HEAD = "goring_head";
    static final String TAG_GORING_BACK = "goring_back";
    static final String TAG_GORING_CHEST = "goring_chest";
    static final String TAG_GORING_ARMS = "goring_arms";
    static final String TAG_GORING_LEGS = "goring_legs";
    static final String TAG_BREAK_HEAD = "break_head";
    static final String TAG_BREAK_CHEST = "break_chest";
    static final String TAG_BREAK_ARMS = "break_arms";
    static final String TAG_BREAK_LEGS = "break_legs";
    static final String TAG_TCE = "tce";
    static final String TAG_HEMORRHAGES = "hemorrhages";
    static final String TAG_BRUISES = "bruises";
    static final String TAG_POLICONTUSION = "policontusion";
    static final String TAG_FACE = "face";
    static final String TAG_TRIAGE = "triage";
    static final String TAG_DEAD = "dead";
    static final String TAG_ID_INJURED = "id_injured";
    static final String TAG_TYPE_LIST_INJURED = "TAG_TYPE_LIST_INJURED";
    static final String TAG_TRANSFER = "transfer";
    static final String TAG_NOTES = "notes";
    static final String TAG_USERID = "userId";
    static final String TAG_GLASGOW = "glasgow";
    static final String TAG_BLOOD_PRESSURE = "blood_presure";
    static final String TAG_PENETRATING_INJURY_TX = "penetrating_injury_tx";
    static final String TAG_PENETRATING_INJURY_ABDOMEN = "penetrating_injury_abdomen";
    static final String TAG_TRACHEAL_INTUBATION = "tracheal_intubation";
    static final String TAG_RESOURCE = "resource";
    static final String TAG_GPS="gps_position";
    static final String TAG_STATUS="status";
    static final String TAG_CONTUSION_HEAD = "contusion_head";
    static final String TAG_CONTUSION_BACK = "contusion_back";
    static final String TAG_CONTUSION_CHEST = "contusion_chest";
    static final String TAG_CONTUSION_ARMS = "contusion_arms";
    static final String TAG_CONTUSION_LEGS = "contusion_legs";
    static final String TAG_CONTUSION_DEFORMITY_ARMS = "contusion_deformity_arms";
    static final String TAG_CONTUSION_DEFORMITY_LEGS = "contusion_deformity_legs";
    static final String TAG_CUTS = "cuts";
    static final String TAG_MESSAGE = "message";

    // Google project id
    static final String PROJECT_ID = "977528864095"; //941483838070 --> pauremirez@gmail.com

    /**
     * Tag used on log messages.
     */
    //static final int MAX_ATTEMPTS = 5;
    static final String DISPLAY_MESSAGE_ACTION = "com.navarra.dya.encierro.DISPLAY_MESSAGE";
    static final String EXTRA_MESSAGE = "message";

    static String registrationStatus = "No registrado";
    static String broadcastMessage = "Sin mensajes";

    public CommonUtilities(){

    }

    /**
     * Notifies UI to display a message.
     * This method is defined in the common helper because it's used both by
     * the UI and the background service.
     *
     * @param context application's context.
     * @param message message to be displayed.
     */
    public static void displayMessage(Context context, String message) {
        Intent intent = new Intent(DISPLAY_MESSAGE_ACTION);
        intent.putExtra(EXTRA_MESSAGE, message);
        context.sendBroadcast(intent);
    }


}
