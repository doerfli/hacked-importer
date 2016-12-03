package li.doerf.hackedimporter;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.common.collect.Maps;

import java.util.ArrayList;
import java.util.Map;

public class AccountListActivity extends AppCompatActivity {
    private static final int PERMISSIONS_REQUEST_GET_ACCOUNT = 1;
    private final String LOGTAG = "AccountListActivity";
    private ArrayList<String> accounts;
    private static Map<String,String> myRecognizedAccounts;

    static {
        myRecognizedAccounts = Maps.newHashMap();
        myRecognizedAccounts.put( "com.google", "Google");
        myRecognizedAccounts.put( "com.facebook.auth.login", "Facebook");
        myRecognizedAccounts.put( "com.twitter.android.auth.login", "Twitter");
        myRecognizedAccounts.put( "com.linkedin.android", "Linkedin");
        myRecognizedAccounts.put( "com.reddit.account", "Reddit");
        myRecognizedAccounts.put( "com.booking.dcl", "Booking");
        myRecognizedAccounts.put( "com.tripadvisor.tripadvisor", "Tripadvisor");
        myRecognizedAccounts.put( "com.evernote", "Evernote");
        myRecognizedAccounts.put( "com.getpebble.android.basalt", "Pebble");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_list);

        Button importAccounts = (Button) findViewById(R.id.searchaccounts);
        importAccounts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ( getCallingActivity() == null ) {
                    Toast.makeText(getApplicationContext(), "Please call this app from Hacked?", Toast.LENGTH_LONG).show();
                    return;
                }

                Intent resultIntent = new Intent();
                resultIntent.putStringArrayListExtra("accounts", accounts);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();

            }
        });

        AccountManager manager = (AccountManager) getSystemService(ACCOUNT_SERVICE);

        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
            Log.v(LOGTAG, "permission READ_PHONE_STATE denied");
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.GET_ACCOUNTS)) {
                Log.v(LOGTAG, "show permission rationale");
                final Activity thisActivity = this;
                new AlertDialog.Builder(this)
                        .setTitle( getString( R.string.dialog_permission_request_title))
                        .setMessage(getString( R.string.dialog_permission_request_GET_ACCOUNT))
                        .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                ActivityCompat.requestPermissions( thisActivity,
                                        new String[]{Manifest.permission.GET_ACCOUNTS},
                                        PERMISSIONS_REQUEST_GET_ACCOUNT);
                            }
                        }).show();
            } else {
                Log.i(LOGTAG, "request permission READ_PHONE_STATE");
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.GET_ACCOUNTS},
                        PERMISSIONS_REQUEST_GET_ACCOUNT);
            }
        }

        Log.d(LOGTAG, "permission granted");

        accounts = new ArrayList<>();

        Account[] list = manager.getAccounts();
        for ( Account account : list) {
            Log.d(LOGTAG, "type: " + account.type + " - account name: " + account.name);
            if ( accounts.contains( account.name) ) {
                continue;
            }

            if ( ! myRecognizedAccounts.containsKey(account.type) ) {
                Log.i( LOGTAG, "unrecognized type: " + account.type);
                continue;
            }

            Log.i( LOGTAG, "recognized type: " + account.type);
            accounts.add(account.name);
        }

        if ( accounts.size() == 0 ) {
            accounts.add(getString(R.string.no_accounts_found));
        }

        ArrayAdapter<String> accountAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, accounts);

        ListView accountList = (ListView) findViewById(R.id.accounts);
        accountList.setAdapter(accountAdapter);
    }
}
