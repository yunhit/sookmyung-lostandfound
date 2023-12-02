package pack.mp_team5project;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginSettingActivity extends AppCompatActivity {

    Button logoutbtn, unregisterbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loginsetting);

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

                        //login화면으로 이동
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

                                    //login화면으로 이동
                                    Intent intent = new Intent(LoginSettingActivity.this, LoginActivity.class);
                                    startActivity(intent);
                                    finish();
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
