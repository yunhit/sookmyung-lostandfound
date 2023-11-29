package pack.mp_team5project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LoginActivity extends AppCompatActivity {


//    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
//    startActivity(intent);
//
//    finish();

    android.widget.Button login_Btn, createAccount_Btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        login_Btn = (android.widget.Button) findViewById(R.id.login_btn);

        login_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentToMain = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intentToMain);
            }
        });

        createAccount_Btn = (android.widget.Button) findViewById(R.id.createAccount_btn);

        createAccount_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentToCreateAccount = new Intent(LoginActivity.this, CreateAccountActivity.class);
                startActivity(intentToCreateAccount);
            }
        });
    }
}