package pack.mp_team5project;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CreateAccountActivity extends AppCompatActivity {

    Button createdAccount_Btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        createdAccount_Btn = (Button) findViewById(R.id.createdAccount_btn);

        createdAccount_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentToCreatedAccount = new Intent(CreateAccountActivity.this, LoginActivity.class);
                startActivity(intentToCreatedAccount);
            }
        });
    }
}