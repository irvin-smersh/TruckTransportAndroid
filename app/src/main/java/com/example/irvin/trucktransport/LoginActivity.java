package com.example.irvin.trucktransport;


import android.app.ProgressDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.irvin.trucktransport.app.AppConfig;
import com.example.irvin.trucktransport.controllers.VozacController;
import com.example.irvin.trucktransport.dataaccess.DatabaseHelper;
import com.example.irvin.trucktransport.enums.QueryType;
import com.example.irvin.trucktransport.listeners.DataDownloadedListener;
import com.example.irvin.trucktransport.model.QueryBundle;
import com.example.irvin.trucktransport.model.ResultBundle;
import com.example.irvin.trucktransport.model.UserResponse;
import com.example.irvin.trucktransport.networking.VolleyTask;
import com.example.irvin.trucktransport.utils.SessionManager;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class LoginActivity extends AppCompatActivity implements DataDownloadedListener {

    private static final String TAG = LoginActivity.class.getSimpleName();
    private Button btnLogin;
    private TextInputLayout usernameWrapper;
    private TextInputLayout passwordWrapper;
    private EditText txtUsername;
    private EditText txtPassword;
    private ProgressDialog pDialog;
    private SessionManager session;
    private VozacController vozacAccess;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameWrapper  = (TextInputLayout) findViewById(R.id.usernameWrapper);
        passwordWrapper  = (TextInputLayout) findViewById(R.id.passwordWrapper);
        txtUsername = (EditText)findViewById(R.id.txtUsername);
        txtPassword = (EditText)findViewById(R.id.txtPassword);
        btnLogin = (Button) findViewById(R.id.btnLogin);
        usernameWrapper.setHint("Username");
        passwordWrapper.setHint("Password");

        vozacAccess = new VozacController(this);
        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        //userAccess = new UserController(this);
        // Session manager
        session = new SessionManager(this);
        //session.setLogin(false, "");
/*
        DatabaseHelper db_helper = new DatabaseHelper(this);
        SQLiteDatabase db = db_helper.getWritableDatabase();
        db_helper.onUpgrade(db,2,3);
*/

        // Check if user is already logged in or not
        if(session.isLoggedIn()){
            Intent intent = new Intent(LoginActivity.this, NavigationActivity.class);
            startActivity(intent);
            finish();
            overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = txtUsername.getText().toString().trim();
                String password = txtPassword.getText().toString().trim();
                if (!validateUsername(username)) {
                    usernameWrapper.setError("Not a valid username!");
                } else if (!validatePassword(password)) {
                    passwordWrapper.setError("Not a valid password!");
                    //}else if(!mBound) {
                    //    Toast.makeText(getApplicationContext(), "GPS Error!!!", Toast.LENGTH_LONG).show();
                } else {
                    usernameWrapper.setErrorEnabled(false);
                    passwordWrapper.setErrorEnabled(false);
                    //LatLng coordinates = mService.getLatLng();
                    //Log.d(TAG, "DUZINA: " + coordinates.latitude + " - SIRINA: " + coordinates.longitude);
                    Map<String, String> params = new HashMap<>();
                    params.put("username", username);
                    params.put("password", password);
                    //params.put("from", "supervisor");
                    //params.put("latitude", String.valueOf(coordinates.latitude));
                    //params.put("longitude", String.valueOf(coordinates.longitude));
                    QueryBundle queryBundle = new QueryBundle(AppConfig.URL_LOGIN, QueryType.LOGIN, params);
                    VolleyTask.addQuery(getApplicationContext(), LoginActivity.this, queryBundle);
                    pDialog.setMessage("Logging in ...");
                    showDialog();
                }
            }
        });

    }

    private void showDialog() {
        if (!pDialog.isShowing())
                pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
    public boolean validatePassword(String password) {
        return password.length() >= 5;
    }

    public boolean validateUsername(String username) {
        return username.length() >= 5;
    }

    @Override
    public void dataDownloaded(QueryType queryType, ResultBundle object) {
        hideDialog();
        String userResponseString = object.getResult().toString();

        try {
            JSONObject jObj = new JSONObject(userResponseString);
            String response = jObj.toString();
            UserResponse userResponse = new Gson().fromJson(response, UserResponse.class);

            userResponse.isError();
            if (!userResponse.isError()){
                session.setLogin(true, userResponse.getUser().getVozac_id());
                vozacAccess.addVozac(userResponse.getUser());

                Intent intent = new Intent(getApplicationContext(), NavigationActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            }else{
                Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
            }

        }catch (JSONException e){
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Json error. " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onErrorLoading(QueryType queryType, ResultBundle object) {
        Log.i(TAG, "Error " + object.getResult());
    }

    @Override
    public void onBackPressed() {
        Intent a = new Intent(Intent.ACTION_MAIN);
        a.addCategory(Intent.CATEGORY_HOME);
        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(a);
    }
}

