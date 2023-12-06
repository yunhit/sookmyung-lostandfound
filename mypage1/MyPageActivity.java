
package pack.mp_team5project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MyPageActivity extends AppCompatActivity {

    TextView MyIDText;
    Fragment fragment1, fragment2;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mypage);

        //현재 사용자 아이디로 text바꾸기
        MyIDText = findViewById(R.id.MyID);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        String userEmail = user.getEmail();
        MyIDText.setText("\n\n"+userEmail);

        //tablayout
        fragment1 = new MyWrittingFragment();
        fragment2 = new MyAttFragment();

        getSupportFragmentManager().beginTransaction().add(R.id.mainLayout,fragment1).commit();
        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);

        tabs.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();

                Fragment selected = null;

                switch (position) {
                    case 0: selected = fragment1; break;
                    case 1: selected = fragment2; break;
                    default: ;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.mainLayout, selected).commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    //로그인설정 클릭시
    public void LoginSettingClick(View view) {
        Intent intent = new Intent(this, LoginSettingActivity.class);
        startActivity(intent);
    }
}