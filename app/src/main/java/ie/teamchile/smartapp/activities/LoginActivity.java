package ie.teamchile.smartapp.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Map;

import ie.teamchile.smartapp.R;
import ie.teamchile.smartapp.api.SmartApiClient;
import ie.teamchile.smartapp.model.BaseModel;
import ie.teamchile.smartapp.model.PostingData;
import ie.teamchile.smartapp.util.NotKeys;
import ie.teamchile.smartapp.util.SharedPrefs;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class LoginActivity extends AppCompatActivity {
    private SharedPreferences prefs;
    private SharedPrefs prefsUtil = new SharedPrefs();
    private String username, password;
    private Button btnLogin;
    private TextView tvUsername, tvPassword, tvAbout;
    private Intent intent;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = (Button) findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new ButtonClick());
        tvUsername = (TextView) findViewById(R.id.et_username);
        tvPassword = (TextView) findViewById(R.id.et_password);
        tvAbout = (TextView) findViewById(R.id.tv_about);
        tvAbout.setOnClickListener(new ButtonClick());

        tvUsername.setText(NotKeys.USERNAME);
        tvPassword.setText(NotKeys.PASSWORD);
    }

    @Override
    protected void onResume() {
        super.onResume();
        BaseModel.getInstance().deleteInstance();
        System.gc();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        BaseModel.getInstance().deleteInstance();
        BaseModel.getInstance().setLoginStatus(false);
        System.gc();
        finish();
    }

    private void getSharedPrefs() {
        Log.d("prefs", "getSharedPrefs called");
        prefs = this.getSharedPreferences("SMART", MODE_PRIVATE);
        Map<String, ?> prefsMap = prefs.getAll();
        for(Map.Entry<String,?> entry : prefsMap.entrySet()){
            Log.d("prefs", "key = " + entry.getKey());
            if(entry.getKey().contains("appointment_post")){
                Log.d("prefs", "get key = " + prefs.getString(entry.getKey(), ""));
                prefsUtil.postAppointment(
                        prefsUtil.getObjectFromString(
                                prefs.getString(entry.getKey(), "")),
                        this, entry.getKey());
            }
        }
    }

    private void doRetrofit() {
        PostingData login = new PostingData();
        login.postLogin(username, password);

        SmartApiClient.getUnAuthorizedApiClient().postLogin(login, new Callback<BaseModel>() {
            @Override
            public void success(BaseModel baseModel, Response response) {
                prefsUtil.deletePrefs(LoginActivity.this, "appts_got");
                BaseModel.getInstance().setLogin(baseModel.getLogin());
                BaseModel.getInstance().setLoginStatus(true);
                pd.dismiss();
                getSharedPrefs();
                intent = new Intent(LoginActivity.this, QuickMenuActivity.class);
                startActivity(intent);
            }

            @Override
            public void failure(RetrofitError error) {
                if (error.getResponse() != null) {
                    BaseModel body = (BaseModel) error.getBodyAs(BaseModel.class);
                    Log.d("Retrofit", "retro error = " + body.getError().getError());
                    Toast.makeText(LoginActivity.this, body.getError().getError(), Toast.LENGTH_LONG).show();
                }
                pd.dismiss();
            }
        });
    }

    private void getCredentials() {
        username = tvUsername.getText().toString();
        password = tvPassword.getText().toString();
    }

    private class ButtonClick implements View.OnClickListener {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_login:
                    pd = new ProgressDialog(LoginActivity.this);
                    pd.setMessage("Logging In");
                    pd.setCanceledOnTouchOutside(false);
                    pd.setCancelable(false);
                    pd.show();
                    getCredentials();
                    doRetrofit();
                    break;
                case R.id.tv_about:
                    Intent intent = new Intent(LoginActivity.this, AboutActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(intent);
                    break;
            }
        }
    }
}
