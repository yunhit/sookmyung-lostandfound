package pack.mp_team5project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.FirebaseApp;

public class LoginActivity extends AppCompatActivity {

    android.widget.Button login_Btn, createAccount_Btn;
    EditText et_login_emailID, et_login_password;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        FirebaseApp.initializeApp(this);

        mAuth = FirebaseAuth.getInstance();

        login_Btn = (android.widget.Button) findViewById(R.id.login_btn);

        et_login_emailID = findViewById(R.id.login_emailId);
        et_login_password = findViewById(R.id.login_password);

        login_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String emailID = et_login_emailID.getText().toString().trim();
                final String password = et_login_password.getText().toString().trim();
                if (!emailID.isEmpty() && !password.isEmpty()) {
                    loginUser(emailID, password);
                } else {
                    Toast.makeText(LoginActivity.this, "모든 항목을 작성해주세요", Toast.LENGTH_SHORT).show();
                }
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

    private void loginUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // 로그인 성공
                            FirebaseUser user = mAuth.getCurrentUser();
                            if (user != null) {
                                Toast.makeText(LoginActivity.this, "로그인 성공!", Toast.LENGTH_SHORT).show();
                                Intent intentToMain = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intentToMain);
                            } else {
                                Toast.makeText(LoginActivity.this, "에러 발생", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            // 로그인 실패
                            handleLoginError(task.getException());
                        }
                    }
                });
    }

    private void handleLoginError(Exception e) {
        if (e != null) {
            String errorMessage = "";
            if (e.getMessage() != null) {
                if (e.getMessage().contains("INVALID_EMAIL")) {
                    errorMessage += "Invalid email format. Please enter a valid email address.";
                } else if (e.getMessage().contains("USER_NOT_FOUND") || e.getMessage().contains("WRONG_PASSWORD")) {
                    errorMessage += "Invalid email or password. Please check your credentials and try again.";
                } else {
                    errorMessage += e.getMessage();
                }
            } else {
                errorMessage += "Unknown error!";
            }

            Toast.makeText(LoginActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(LoginActivity.this, "Login failed: Unknown error!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            Intent intentToMain = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intentToMain);
        }
    }

}