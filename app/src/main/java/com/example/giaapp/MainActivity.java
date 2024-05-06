package com.example.giaapp;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    DatabaseContext _dbContext;
    SQLiteDatabase _db;
    Cursor userCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        _dbContext = new DatabaseContext(getApplicationContext());
    }

    @Override
    public void onResume() {
        super.onResume();
        _db = _dbContext.getReadableDatabase();

        //_db.execSQL("delete from app_properties");

        userCursor =  _db.rawQuery("select * from app_properties where property_name = 'API-Key'", null);

        if (userCursor.getCount() != 1)
        {
            _db.execSQL("delete from app_properties where property_name = 'API-Key'");
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
        else {
            Intent intent = new Intent(this, MainMenuActivity.class);
            startActivity(intent);
        }

        //TextView tv = findViewById(R.id.textView);
        //tv.setText(Integer.toString(userCursor.getCount()));
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        _db.close();
        //userCursor.close();
    }
}