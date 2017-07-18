package rp.edu.sg.c302.addressbook;

import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class LoginActivity extends Activity {

    private static final String TAG = "LoginActivity";

    Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment()).commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Checks if user has logged in before
        if (authenticated()) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        TextView errorMsg = (TextView) findViewById(R.id.textViewLoginError);
        errorMsg.setText("Please Login");

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
        String x = preferences.getString("apikey", "");
        Log.i("String x contains >> ", "" + x);
        if (!x.equals("")){
            Log.i(">>", preferences.getString("apikey", ""));
            Intent k = new Intent(this, MainActivity.class);
            startActivity(k);
        }
    }

    public boolean authenticated() {
        Intent intent = getIntent();
        if (intent.hasExtra("apikey")) {
            Log.i(">>", "APIKEY" + intent.hasExtra("apikey"));
            return true;
        }
        Log.i(">>>", "");
        return false;
    }

//    public boolean authenticated() {
//        Intent intent = getIntent();
//        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
//        if (preferences.getString("apikey","") == null) {
//            return true;
//        }
//        return false;
//    }



    public void loginButton(View view) {
        Log.d(TAG, "loginButton()...");

        // Get the user's input
        EditText editTextUsername = (EditText) findViewById(R.id.editTextUserName);
        String userName = editTextUsername.getText().toString();
        EditText editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        String password = editTextPassword.getText().toString();
        // Check if there is network access
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {

            //TODO 01 Insert/modify code here to send a HttpRequest to doLogin.php
            HttpRequest request = new HttpRequest("http://10.0.2.2/c302_P08_PHP_SecureCloudAddressBook/doLogin.php");
            request.addData("username", editTextUsername.getText().toString());
            request.addData("password", editTextPassword.getText().toString());
            request.setMethod("POST");
            request.execute();

/******************************/
            try {
                String jsonString = request.getResponse();
                Log.d(TAG, "jsonString: " + jsonString);

                JSONObject jsonObj = (JSONObject) new JSONTokener(jsonString).nextValue();
                if (jsonObj.getBoolean("authentication")) {
                    // When authentication is successful

                    //TODO 02 Extract the id and API Key from the JSON object and assign them to the following variables
                    String apiKey = jsonObj.getString("apikey");
                    String id = jsonObj.getString("id");
                    Log.d(TAG, "userId: " + id);
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.putExtra("loginId", id);
                    intent.putExtra("apikey", apiKey);

                    SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("apikey", apiKey);
                    editor.apply();

                    System.out.println(">>" + preferences.getString("apikey", ""));
                    startActivity(intent);
                } else {
                    TextView textView = (TextView) findViewById(R.id.textViewLoginError);
                    textView.setText("Authentication failed, please login again");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            TextView textView = (TextView) findViewById(R.id.textViewLoginError);
            textView.setText("No network connection available.");
        }
    }

    public void registerButton(View view) {
        Log.d(TAG, "loginButton()...");

        // Get the user's input
        EditText editTextUsername = (EditText) findViewById(R.id.editTextUserName);
        String userName = editTextUsername.getText().toString();
        EditText editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        String password = editTextPassword.getText().toString();
        // Check if there is network access
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {

            //TODO 01.2 Insert/modify code here to send a HttpRequest to doLogin.php
            HttpRequest request = new HttpRequest("http://10.0.2.2/c302_P08_PHP_SecureCloudAddressBook/createNewUser.php");
            request.addData("username", editTextUsername.getText().toString());
            request.addData("password", editTextPassword.getText().toString());
            request.setMethod("POST");
            request.execute();

            try {
                String jsonString = request.getResponse();
                Log.d(TAG, "jsonString: " + jsonString);

                JSONObject jsonObj = (JSONObject) new JSONTokener(jsonString).nextValue();
                Toast.makeText(getApplicationContext(), "Created. You're an Admin", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            TextView textView = (TextView) findViewById(R.id.textViewLoginError);
            textView.setText("No network connection available.");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.login, menu);
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
            View rootView = inflater.inflate(R.layout.fragment_login,
                    container, false);
            return rootView;
        }
    }


}