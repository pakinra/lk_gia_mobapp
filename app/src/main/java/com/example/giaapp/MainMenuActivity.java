package com.example.giaapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.giaapp.api.LoginResult;
import com.example.giaapp.api.NetworkService;

import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainMenuActivity extends AppCompatActivity {
    DatabaseContext _dbContext;
    SQLiteDatabase _db;
    Cursor userCursor;

    Button profileB;
    Button examsB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_menu);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        _dbContext = new DatabaseContext(getApplicationContext());

        profileB = (Button) findViewById(R.id.profileB);
        examsB = (Button) findViewById(R.id.examsB);

        profileB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenuActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });
        examsB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenuActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        Runnable updateRunnable = new Runnable() {
            @Override
            public void run() {
                _db = _dbContext.getReadableDatabase();
                userCursor =  _db.rawQuery("select property_value from app_properties where property_name = 'API-Key'", null);
                userCursor.moveToFirst();

                NetworkService.getInstance()
                        .getJSONApi()
                        .updateUserInfo(userCursor.getString(0))
                        .enqueue(new Callback<LoginResult>() {
                            @Override
                            public void onResponse(@NonNull Call<LoginResult> call, @NonNull Response<LoginResult> response) {
                                try {
                                    LoginResult loginResult = response.body();
                                    if (response.isSuccessful()) {
                                        _db.execSQL("update app_properties set " +
                                                "property_value = '"+ loginResult.getUserName() + "'" +
                                                "where property_name = 'userName'");
                                        _db.execSQL("update app_properties set " +
                                                "property_value = '"+ loginResult.getShortName() + "'" +
                                                "where property_name = 'shortName'");
                                        _db.execSQL("update app_properties set " +
                                                "property_value = '"+ loginResult.getUserCode() + "'" +
                                                "where property_name = 'userCode'");
                                        MainMenuActivity.this.runOnUiThread(new Runnable() {
                                            public void run() {
                                                Toast.makeText(MainMenuActivity.this, "Данные успешно обновлены!", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                    else {
                                        JSONObject jObjError = new JSONObject(response.errorBody().string());

                                        MainMenuActivity.this.runOnUiThread(new Runnable() {
                                            public void run() {
                                                try {
                                                    Toast.makeText(MainMenuActivity.this, jObjError.getString("response"), Toast.LENGTH_LONG).show();
                                                } catch (JSONException e) {
                                                    throw new RuntimeException(e);
                                                }
                                            }
                                        });
                                    }
                                }
                                catch (Exception ex) {

                                    MainMenuActivity.this.runOnUiThread(new Runnable() {
                                        public void run() {
                                            Toast.makeText(MainMenuActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<LoginResult> call, @NonNull Throwable t) {
                                t.printStackTrace();
                            }
                        });
            }
        };
        Thread updateThread = new Thread(updateRunnable);
        updateThread.start();
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        _db.close();
        //userCursor.close();
    }
}