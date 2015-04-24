package app.com.buzz;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

public class CallStateHandler extends BroadcastReceiver {
    private static String last6Nums = null;
    private static boolean bIncoming = false;
    private static boolean bOutgoing = false;
    private static boolean bAnswerForIncoming = false;
    private static boolean bDisplayed = false;



    @Override
    public void onReceive(final Context context, Intent intent) {
        String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
        String msg = "Phone state changed to " + state;
        int LAST_N_NUMS = 7;
        TelephonyManager mTelMgr = (TelephonyManager)context.getSystemService(context.TELEPHONY_SERVICE);


        if (intent.getAction().equals(Intent.ACTION_NEW_OUTGOING_CALL)) {
            // get phone number from bundle
            String outgoingNumber = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
            Log.d("URL : ", "OutgoingNumber number - " + outgoingNumber);


            last6Nums = (outgoingNumber == null || outgoingNumber.length() < LAST_N_NUMS) ?
                    outgoingNumber : outgoingNumber.substring(outgoingNumber.length() - LAST_N_NUMS);
            Log.d("URL : ", "Computed last 6 OutgoingNumber number - " + last6Nums);

            setResultData(outgoingNumber);
            launchVideo(context, last6Nums);
            bOutgoing = true;
            return;
        }

        /*if (intent.getAction().equals(TelephonyManager.ACTION_PRECISE_CALL_STATE_CHANGED)) {
            Log.d("CallStateHandler: ", "HURRAHHHH ! state - " + state);
        }*/

            Log.d("CallStateHandler: ", "state - " + state);
        if (TelephonyManager.EXTRA_STATE_RINGING.equals(state)) {
            String incomingNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
            msg += ". Incoming number is " + incomingNumber;
            Log.d("URL : ", "Incoming number - " + incomingNumber);

            last6Nums = (incomingNumber == null || incomingNumber.length() < LAST_N_NUMS) ?
                    incomingNumber : incomingNumber.substring(incomingNumber.length() - LAST_N_NUMS);
            Log.d("URL : ", "COMPUTED last 6 Incoming number - " + last6Nums);

            bIncoming = true;

        }
        if (TelephonyManager.EXTRA_STATE_OFFHOOK.equals(state)) {
            if (bIncoming)
                bAnswerForIncoming = true;

            Log.d("CallStateHandler: ", "bIncoming - " + bIncoming);
            Log.d("CallStateHandler: ", "bAnswerForIncoming - " + bAnswerForIncoming);


            if (bAnswerForIncoming && !bDisplayed) {
                Log.d("CallStateHandler: ", "Launching Activity - ");

                /*AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setMessage("Halo !")
                        .setTitle("Buzz To Video Service ?")
                        .setPositiveButton("Yay ", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent i = new Intent(Intent.ACTION_VIEW);
                                i.setData(Uri.parse(url));
                                context.startActivity(i);
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("Nay", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                         });
                // Create the AlertDialog
                AlertDialog dialog = builder.create();
                dialog.show();*/
                launchVideo(context, last6Nums);
                bDisplayed = true;
            } else if (bOutgoing && !bDisplayed) {
                launchVideo(context, last6Nums);
            }

        }
        if (TelephonyManager.EXTRA_STATE_IDLE.equals(state)) {
            if (!bIncoming) {
                String msisdn = mTelMgr.getLine1Number();
                last6Nums = (msisdn == null || msisdn.length() < LAST_N_NUMS) ?
                        msisdn : msisdn.substring(msisdn.length() - LAST_N_NUMS);
            }

            bIncoming = bAnswerForIncoming = bOutgoing = bDisplayed = false;
            Log.d("CallStateHandler: ", "last6Nums -- " + last6Nums);
        }

        //Toast.makeText(context, msg, Toast.LENGTH_LONG).show();

    }

    private void launchVideo(Context context, String last6Nums) {
        //final String url = "https://apprtc.appspot.com/r/" + last6Nums + "?audio=false";
        final String url = "https://apprtc.appspot.com/r/" + last6Nums;
        Log.d("URL :", "last6Nums - " + last6Nums);
        Intent i = new Intent(context, MainActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        i.putExtra("URL", url);
        context.startActivity(i);
    }
}
