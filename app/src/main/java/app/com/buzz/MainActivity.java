package app.com.buzz;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.PermissionRequest;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class MainActivity extends ActionBarActivity {

    private String mUrl = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        if (intent.hasExtra("URL")){
            mUrl = intent.getStringExtra("URL");
        }

        mUrl = "https://apprtc.appspot.com/r/" + "18907890";
        displayAlert();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void displayAlert()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you wish you buzz?").setCancelable(
                false).setPositiveButton("Yay",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        /*Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(mUrl));
                        Log.d("URL :", mUrl);

                        startActivity(i);*/

                       WebView myWebView = (WebView) findViewById(R.id.webview);

                        myWebView .getSettings().setJavaScriptEnabled(true);
                        myWebView .getSettings().setDomStorageEnabled(true);
                        //myWebView.setWebViewClient(new WebViewClient());

                        myWebView.setWebChromeClient(new WebChromeClient() {

                            @Override
                            public void onPermissionRequest(final PermissionRequest request) {
                                Log.d("URL :", "onPermissionRequest");
                                runOnUiThread(new Runnable() {
                                    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                                    @Override
                                    public void run() {
                                        Log.d("URL :" , request.getOrigin().toString());
                                        if (request.getOrigin().toString().equals("https://apprtc.appspot.com/")) {
                                            Log.d("URL :", "Trying to grant");
                                            request.grant(request.getResources());
                                        } else {
                                            Log.d("URL :", "Request denied!");
                                            request.deny();
                                        }
                                    }
                                });
                            }

                        });

                        Log.d("URL :", mUrl);
                        myWebView.loadUrl(mUrl);

                        dialog.dismiss();
                        //finish();
                    }
                }).setNegativeButton("Nay",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        finish();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

}
