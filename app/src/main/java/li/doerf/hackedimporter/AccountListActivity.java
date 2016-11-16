package li.doerf.hackedimporter;

import android.Manifest;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.ArraySet;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static li.doerf.hackedimporter.R.id.accounts;

public class AccountListActivity extends AppCompatActivity {
    private static final int PERMISSIONS_REQUEST_GET_ACCOUNT = 1;
    private final String LOGTAG = "AccountListActivity";
    private ArrayList<String> accounts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_list);

        Button importAccounts = (Button) findViewById(R.id.importaccounts);
        importAccounts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent resultIntent = new Intent();
                resultIntent.putStringArrayListExtra("accounts", accounts);
                // TODO Add extras or a data URI to this intent as appropriate.
                setResult(Activity.RESULT_OK, resultIntent);
                finish();

            }
        });

        AccountManager manager = (AccountManager) getSystemService(ACCOUNT_SERVICE);

        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.GET_ACCOUNTS},
                    PERMISSIONS_REQUEST_GET_ACCOUNT);
            Log.d( LOGTAG, "no permission");
            return;
        }

        accounts = new ArrayList<>();

        Account[] list = manager.getAccounts();
        for ( Account account : list) {
            if ( accounts.contains( account.name) ) {
                continue;
            }

            // TODO check for right type
            accounts.add(account.name);
        }

        ArrayAdapter<String> accountAdapter =
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, accounts);

        ListView accountList = (ListView) findViewById(R.id.accounts);
        accountList.setAdapter(accountAdapter);
    }
}
