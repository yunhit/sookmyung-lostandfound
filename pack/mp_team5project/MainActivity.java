package pack.mp_team5project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    ImageButton myPage_btn, registerPage_btn, searchPage_btn, alarmPage_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myPage_btn = (ImageButton) findViewById(R.id.myPage_btn);
        myPage_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intentToMyPage = new Intent(MainActivity.this, .class);
//                startActivity(intentToMyPage);
            }
        });

        registerPage_btn = (ImageButton) findViewById(R.id.registerPage_btn);
        registerPage_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentToRegisterPage = new Intent(MainActivity.this, RegisterInfoActivity.class);
                startActivity(intentToRegisterPage);
            }
        });

        searchPage_btn = (ImageButton) findViewById(R.id.searchPage_btn);
        searchPage_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intentToSearchPage = new Intent(MainActivity.this, .class);
//                startActivity(intentToSearchPage);
            }
        });

        alarmPage_btn = (ImageButton) findViewById(R.id.alarmPage_btn);
        alarmPage_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentToAlarmPage = new Intent(MainActivity.this, AlarmActivity.class);
                startActivity(intentToAlarmPage);
            }
        });
    }
}