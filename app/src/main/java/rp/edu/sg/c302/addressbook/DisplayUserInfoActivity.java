package rp.edu.sg.c302.addressbook;

import org.json.JSONObject;

//import com.example.p11authentication.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import rp.edu.sg.c302.addressbook.R;

@SuppressLint("NewApi")
public class DisplayUserInfoActivity extends Activity {

    private static final String TAG = "DisplayUserInfoActivity";

    private String loginId;
    private String apikey;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_display_user_info);
        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment()).commit();
        }
    }

    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();
        loginId = intent.getStringExtra("loginId");
        apikey = intent.getStringExtra("apikey");
        userId = intent.getStringExtra("userId");

        //TODO 04 Request User Info using HTTPRequest and Display them
        HttpRequest request = new HttpRequest("http://10.0.2.2/c302_P08_PHP_SecureCloudAddressBook/getContactDetails.php");
        request.setMethod("POST");
        request.addData("userId", userId);
        request.execute();

        /******************************/

        try {
            String jsonString = request.getResponse();
            Log.d(TAG, "jsonString: " + jsonString);

            JSONObject jsonObj = new JSONObject(jsonString);

            EditText firstNameEditText = (EditText) findViewById(R.id.editTextFirstName);
            firstNameEditText.setText(jsonObj.getString("firstname"));

            EditText lastNameEditText = (EditText) findViewById(R.id.editTextLastName);
            lastNameEditText.setText(jsonObj.getString("lastname"));

            EditText homeEditText = (EditText) findViewById(R.id.editTextHome);
            homeEditText.setText(jsonObj.getString("home"));

            EditText mobileEditText = (EditText) findViewById(R.id.editTextMobile);
            mobileEditText.setText(jsonObj.getString("mobile"));

            EditText addressEditText = (EditText) findViewById(R.id.editTextAddress);
            addressEditText.setText(jsonObj.getString("address"));

            EditText countryEditText = (EditText) findViewById(R.id.editTextCountry);
            countryEditText.setText(jsonObj.getString("country"));

            EditText postalCodeEditText = (EditText) findViewById(R.id.editTextPostalCode);
            postalCodeEditText.setText(jsonObj.getString("postalcode"));

            EditText emailEditText = (EditText) findViewById(R.id.editTextEmail);
            emailEditText.setText(jsonObj.getString("email"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateDetailsButtonClicked(View view) {

        Log.d(TAG, "updateDetailsButtonClicked()...");

        EditText firstNameEditText = (EditText) findViewById(R.id.editTextFirstName);
        EditText lastNameEditText = (EditText) findViewById(R.id.editTextLastName);
        EditText homeEditText = (EditText) findViewById(R.id.editTextHome);
        EditText mobileEditText = (EditText) findViewById(R.id.editTextMobile);
        EditText addressEditText = (EditText) findViewById(R.id.editTextAddress);
        EditText countryEditText = (EditText) findViewById(R.id.editTextCountry);
        EditText postalCodeEditText = (EditText) findViewById(R.id.editTextPostalCode);
        EditText emailEditText = (EditText) findViewById(R.id.editTextEmail);

        //TODO 05 Update User Info using HttpRequest object
        HttpRequest request = new HttpRequest("http://10.0.2.2/c302_P08_PHP_SecureCloudAddressBook/updateContact.php");
        request.setMethod("POST");
        request.addData("userId", userId);
        request.addData("FirstName", firstNameEditText.getText().toString());
        request.addData("LastName", lastNameEditText.getText().toString());
        request.addData("Home", homeEditText.getText().toString());
        request.addData("Mobile", mobileEditText.getText().toString());
        request.addData("Address", addressEditText.getText().toString());
        request.addData("Country", countryEditText.getText().toString());
        request.addData("PostalCode", postalCodeEditText.getText().toString());
        request.addData("Email", emailEditText.getText().toString());
        request.execute();

        /******************************/
        try {
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void deleteRecordButtonClicked(View view) {

        //TODO 06 Delete User using HttpRequest object accessing removeContact.php
        Log.d(TAG, "deleteRecordButtonClicked()...");

        HttpRequest request = new HttpRequest("http://10.0.2.2/c302_P08_PHP_SecureCloudAddressBook/removeContact.php");
        request.addData("userId", userId);
        request.setMethod("POST");
        request.execute();

        /******************************/

        try {
            String jsonString = request.getResponse();
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.display_user_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(
                    R.layout.fragment_display_user_info, container, false);
            return rootView;
        }
    }

}