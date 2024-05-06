package com.example.giaapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity {
    DatabaseContext _dbContext;
    SQLiteDatabase _db;
    Cursor userCursor;
    Button toMainMenu;
    Button exit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        _dbContext = new DatabaseContext(getApplicationContext());
        _db = _dbContext.getReadableDatabase();

        toMainMenu = (Button) findViewById(R.id.toMainMenuB);
        toMainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, MainMenuActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });

        exit = (Button) findViewById(R.id.exitB);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _db.execSQL("delete from app_properties");

                Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });

        ArrayList<ProfileProperty> profileProperties = new ArrayList<ProfileProperty>();

        userCursor =  _db.rawQuery("select property_value from app_properties where property_name = 'userName'", null);
        userCursor.moveToFirst();
        profileProperties.add(new ProfileProperty("Имя пользователя (логин)", userCursor.getString(0)));

        userCursor =  _db.rawQuery("select property_value from app_properties where property_name = 'userCode'", null);
        userCursor.moveToFirst();
        int userCode = Integer.parseInt(userCursor.getString(0));
        if (userCode != -1) {
            if (userCode < 100) {
                profileProperties.add(new ProfileProperty("Код МСУ", Integer.toString(userCode)));
            }
            else {
                profileProperties.add(new ProfileProperty("Код ОО", Integer.toString(userCode)));
            }
        }

        userCursor =  _db.rawQuery("select property_value from app_properties where property_name = 'shortName'", null);
        userCursor.moveToFirst();
        profileProperties.add(new ProfileProperty("Наименование пользователя", userCursor.getString(0)));

        ListView productList = findViewById(R.id.userProfileLV);
        ProfilePropertyAdapter adapter = new ProfilePropertyAdapter(this, R.layout.profily_property, profileProperties);
        productList.setAdapter(adapter);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        _db.close();
        //userCursor.close();
    }
}