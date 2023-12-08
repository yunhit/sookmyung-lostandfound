package pack.mp_team5project;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginSettingActivity extends AppCompatActivity {

    TextView MyIDText;
    Button logoutbtn, unregisterbtn;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginsetting);

        //아이디에 현재 사용자 아이디 출력
        MyIDText = findViewById(R.id.nowID);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        String userEmail = user.getEmail();
        MyIDText.setText(userEmail);

        //로그아웃 버튼 클릭시
        logoutbtn = findViewById(R.id.logout_btn);
        logoutbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //로그아웃 확인 안내창 + 확인 안내창 클릭시 이벤트
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginSettingActivity.this);
                builder.setTitle("로그아웃");
                builder.setMessage("로그아웃 하시겠습니까?");

                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(LoginSettingActivity.this, "로그아웃", Toast.LENGTH_SHORT).show();
                   	// 로그아웃
                        mAuth.signOut();

                        // 로그아웃 후 동작 정의
                        Intent intent = new Intent(LoginSettingActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(LoginSettingActivity.this, "취소", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.create().show();
            }
        });

        //회원탈퇴 버튼 클릭시
        unregisterbtn = findViewById(R.id.unregister_btn);
        unregisterbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginSettingActivity.this);
                builder.setTitle("회원탈퇴");
                builder.setMessage("정말 탈퇴하시겠습니까?");

                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginSettingActivity.this);
                        builder.setTitle("회원탈퇴");
                        builder.setMessage("탈퇴 확인 문구를 정확하게 입력해주세요.\n(확인 문구: 탈퇴)");

                        // EditText로 확인문구 입력받기
                        final EditText input = new EditText(LoginSettingActivity.this);
                        builder.setView(input);
                        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface confirmDialog, int confirmWhich) {
                                String userInput = input.getText().toString();

                                //userInput과 확인문구 비교
                                if (userInput.equals("탈퇴")) {
                                    Toast.makeText(LoginSettingActivity.this, "탈퇴했습니다", Toast.LENGTH_SHORT).show();

                                    // Firebase Authentication에서 현재 로그인된 사용자 가져오기
                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                    // Firebase 사용자 삭제
                                    if (user != null) {
                                        String userUid = user.getUid();

                                        // 사용자 UID와 관련된 데이터베이스에서의 데이터 삭제
                                        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();
                                        databaseRef.child("Data").orderByChild("userID").equalTo(userUid)
                                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                            // 각 데이터 삭제
                                                            snapshot.getRef().removeValue();
                                                        }

                                                        // Firebase Authentication에서 사용자 삭제
                                                        user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                if (task.isSuccessful()) {
                                                                    // 사용자 삭제 성공
                                                                    Toast.makeText(LoginSettingActivity.this, "탈퇴했습니다", Toast.LENGTH_SHORT).show();

                                                                    // login 화면으로 이동
                                                                    Intent intent = new Intent(LoginSettingActivity.this, LoginActivity.class);
                                                                    startActivity(intent);
                                                                    finish();
                                                                } else {
                                                                    // 사용자 삭제 실패
                                                                    Toast.makeText(LoginSettingActivity.this, "탈퇴에 실패했습니다", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }
                                                        });
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                                        // 데이터베이스에서의 데이터 삭제 실패
                                                        Toast.makeText(LoginSettingActivity.this, "데이터 삭제에 실패했습니다", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    }
                                } else { //확인 문구 불일치
                                    Toast.makeText(LoginSettingActivity.this, "확인 문구가 일치하지 않습니다", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface confirmDialog, int confirmWhich) {
                                Toast.makeText(LoginSettingActivity.this, "취소", Toast.LENGTH_SHORT).show();
                            }
                        });
                        builder.show();
                    }
                });

                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(LoginSettingActivity.this, "취소", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();
            }
        });
    }
}
