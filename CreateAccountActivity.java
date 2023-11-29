package pack.mp_team5project;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;


public class CreateAccountActivity extends AppCompatActivity {

    android.widget.Button createdAccount_Btn, checkedDuplicateID_Btn;
    EditText et_register_email, et_register_pw, et_register_repw;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        FirebaseApp.initializeApp(this);

        mAuth = FirebaseAuth.getInstance();

        createdAccount_Btn = findViewById(R.id.createAccount_btn);
        et_register_email = findViewById(R.id.create_IDEmail);
        et_register_pw = findViewById(R.id.create_password);
        et_register_repw = findViewById(R.id.check_password);

        createdAccount_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = et_register_email.getText().toString().trim();
                final String password = et_register_pw.getText().toString().trim();
                final String rePassword = et_register_repw.getText().toString().trim();

                if (!email.isEmpty() && !password.isEmpty() && !rePassword.isEmpty()) {
                    if (!rePassword.equals(password)) {
                        Toast.makeText(CreateAccountActivity.this, "비밀번호가 서로 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                    } else{
                        registerUser(email, password);
                    }
                } else {
                    Toast.makeText(CreateAccountActivity.this, "모든 항목을 작성해주세요", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void registerUser(String email, String password) {
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(CreateAccountActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // 회원등록 성공시
                                    FirebaseUser user = mAuth.getCurrentUser();

                                    if (user != null) {
                                        Toast.makeText(CreateAccountActivity.this, "회원 가입 성공!", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(CreateAccountActivity.this, LoginActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(CreateAccountActivity.this, "에러 발생", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    // 회원가입 실패
                                    handleRegistrationError(task.getException());
                                }
                            }
                        });
            }

    private void handleRegistrationError(Exception e) {
                if (e != null) {
                    String errorMessage = "";
                    if (e.getMessage() != null) {
                        if (e.getMessage().contains("WEAK_PASSWORD")) {
                            errorMessage += "Weak password. Please choose a stronger password.";
                        } else if (e.getMessage().contains("INVALID_EMAIL")) {
                            errorMessage += "Invalid email format. Please enter a valid email address.";
                        } else {
                            errorMessage += e.getMessage();
                        }
                    } else {
                        errorMessage += "Unknown error!";
                    }

                    Toast.makeText(CreateAccountActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(CreateAccountActivity.this, "Registration failed: Unknown error!", Toast.LENGTH_SHORT).show();
                }
            }
}
