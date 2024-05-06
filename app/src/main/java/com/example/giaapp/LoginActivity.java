package com.example.giaapp;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.giaapp.api.LoginModel;
import com.example.giaapp.api.LoginResult;
import com.example.giaapp.api.NetworkService;
import com.example.giaapp.api.Post;
import com.google.android.material.button.MaterialButton;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    EditText userNameET;
    EditText passwordET;
    MaterialButton loginB;
    ProgressBar progressBar;
    DatabaseContext _dbContext;
    SQLiteDatabase _db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        _dbContext = new DatabaseContext(getApplicationContext());

        userNameET = (EditText) findViewById(R.id.username);
        passwordET = (EditText) findViewById(R.id.password);
        loginB = (MaterialButton) findViewById(R.id.loginbtn);
        progressBar = (ProgressBar) findViewById(R.id.progressBar2);

        loginB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userNameET.getText().toString().trim().isEmpty()) {
                    userNameET.setText("");
                    userNameET.requestFocus();
                    Toast.makeText(LoginActivity.this, "Укажите имя пользователя!", Toast.LENGTH_SHORT).show();
                }
                if (passwordET.getText().toString().trim().isEmpty()) {
                    passwordET.setText("");
                    passwordET.requestFocus();
                    Toast.makeText(LoginActivity.this, "Укажите пароль!", Toast.LENGTH_SHORT).show();
                }
                Auth(userNameET.getText().toString().trim(), passwordET.getText().toString().trim());
            }
        });
    }

    void Auth(String userName, String password)
    {
        LoginModel model = new LoginModel();
        model.setUserName(userName);
        model.setPassword(password);

        userNameET.setEnabled(false);
        passwordET.setEnabled(false);
        loginB.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);
        NetworkService.getInstance()
                .getJSONApi()
                .userAuth(model)
                .enqueue(new Callback<LoginResult>() {
                    @Override
                    public void onResponse(@NonNull Call<LoginResult> call, @NonNull Response<LoginResult> response) {
                        try {
                            LoginResult loginResult = response.body();
                            if (response.isSuccessful()) {
                                progressBar.setVisibility(View.GONE);

                                _db = _dbContext.getReadableDatabase();
                                _db.execSQL("insert into app_properties values " +
                                        "('API-Key','" + loginResult.getApiKey() + "')" +
                                        ", ('userName','" + loginResult.getUserName() + "')" +
                                        ", ('shortName','" + loginResult.getShortName() + "')" +
                                        ", ('userCode','" + loginResult.getUserCode() + "')");

                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                startActivity(intent);
                            }
                            else {
                                userNameET.setEnabled(true);
                                passwordET.setEnabled(true);
                                loginB.setEnabled(true);
                                progressBar.setVisibility(View.GONE);
                                JSONObject jObjError = new JSONObject(response.errorBody().string());
                                Toast.makeText(LoginActivity.this, jObjError.getString("response"), Toast.LENGTH_LONG).show();
                            }
                        }
                        catch (Exception ex) {
                            userNameET.setEnabled(true);
                            passwordET.setEnabled(true);
                            loginB.setEnabled(true);
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(LoginActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<LoginResult> call, @NonNull Throwable t) {

                        //textView.append("Error occurred while getting request!");
                        Toast.makeText(LoginActivity.this, "Возникла ошибка при подключении к серверу! Проверьте подключение к сети Интернет.", Toast.LENGTH_LONG).show();
                        t.printStackTrace();
                        userNameET.setEnabled(true);
                        passwordET.setEnabled(true);
                        loginB.setEnabled(true);
                        progressBar.setVisibility(View.GONE);
                    }
                });
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        _db.close();
        //userCursor.close();
    }
}