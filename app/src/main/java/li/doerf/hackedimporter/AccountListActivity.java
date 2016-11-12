package li.doerf.hackedimporter;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class AccountListActivity extends AppCompatActivity {
    private final String LOGTAG = "AccountListActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_list);

        Button clickMeButton = (Button) findViewById(R.id.clickme);
        clickMeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AccountManager manager = (AccountManager) getSystemService(ACCOUNT_SERVICE);

                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    Log.d( LOGTAG, "no permission");
                    return;
                }

                Account[] list = manager.getAccounts();
                for ( Account account : list) {
                    Log.d(LOGTAG, "account: " + account.name );
                    Log.d(LOGTAG, "account: " + account.type );
                    Log.d(LOGTAG, "account: " + account.toString() );
                }
            }
        });
    }
}
