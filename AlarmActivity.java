package pack.mp_team5project;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AlarmActivity extends AppCompatActivity {

    private static final int REQUEST_ALARM_PERMISSION = 1;

    EditText edt_tag;
    ImageButton addTag_btn;
    android.widget.Button alarmSetting_btn;
    static Boolean alarmSetting_tf;
    RecyclerView hashTag_recyclearView, post_recyclearView;
    private PostAdapter postAdapter;
    private List<PostModel> postList;
    List<String> personalHashtagList;
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference HashRef = mRootRef.child("HashTag_Data");
    DatabaseReference HashDataRef = HashRef.child("HashTag_" + mAuth.getCurrentUser().getUid());
    DatabaseReference conditionRef = FirebaseDatabase.getInstance().getReference().child("Data");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        alarmSetting_btn = findViewById(R.id.alarmSetting_btn);
        alarmSetting_tf = true;
        edt_tag = findViewById(R.id.edt_tag);
        addTag_btn = findViewById(R.id.addTag_btn);
        hashTag_recyclearView = findViewById(R.id.hashTag_recyclerView);
        post_recyclearView = findViewById(R.id.post_recyclearView);

        postList = new ArrayList<>();
        postAdapter = new PostAdapter(this, postList);
        post_recyclearView.setAdapter(postAdapter);
        personalHashtagList = new ArrayList<>();


        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        post_recyclearView.setLayoutManager(layoutManager);

        updateHashTagRecyclerView();
        updatePostRecyclerView(personalHashtagList);

        if (ContextCompat.checkSelfPermission(AlarmActivity.this, Manifest.permission.POST_NOTIFICATIONS)
                == PackageManager.PERMISSION_GRANTED) {
        } else {
            ActivityCompat.requestPermissions(AlarmActivity.this, new String[]{Manifest.permission.POST_NOTIFICATIONS},
                    REQUEST_ALARM_PERMISSION);
        }

        alarmSetting_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (alarmSetting_tf) {
                    alarmSetting_btn.setText("알림 : OFF");
                    alarmSetting_tf = false;
                } else {
                    alarmSetting_btn.setText("알림 : ON");
                    alarmSetting_tf = true;
                }
            }
        });

        addTag_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addHashTag();
                updateHashTagRecyclerView();
                updatePostRecyclerView(personalHashtagList);
            }
        });
        postAdapter.setOnItemClickListener(new PostAdapter.OnItemClickListener(){
            @Override
            public void onItemClick(PostModel post) {
                openPostDtActivity(post);
            }

            @Override
            public void onDeleteButtonClick(Integer position) {

            }
        });

        startFCMService();
    }

    //해시태그를 firebase-database에 추가
    public void addHashTag() {
        String hashtagValue = edt_tag.getText().toString();
        if (!hashtagValue.isEmpty()) {
            personalHashtagList.clear();
            // 기존 데이터를 불러옴
            HashDataRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    List<String> hashtagList = new ArrayList<>();
                    // 기존 데이터가 있으면 불러와서 리스트에 추가
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String hashtag = snapshot.getValue(String.class);
                            if (hashtag != null) {
                                hashtagList.add(hashtag);
                            }
                        }
                    }

                    // 새로운 해시태그를 리스트에 추가
                    hashtagList.add(hashtagValue);
                    // 업데이트된 리스트를 다시 파이어베이스에 저장
                    HashDataRef.setValue(hashtagList);
                    // 입력 필드 초기화
                    edt_tag.setText("");
                    updateHashTagRecyclerView();
                    updatePostRecyclerView(hashtagList);
                    FCMService.subscribeToFCMTopic(hashtagValue);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // 에러 처리
                    Toast.makeText(AlarmActivity.this, "데이터를 불러오는 중 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    //hashTag_RecyclerView에 사용자 설정해시태그 나타내기 메소드
    private void updateHashTagRecyclerView() {
        HashDataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> hashtagList = new ArrayList<>();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String hashtag = snapshot.getValue(String.class);
                        if (hashtag != null) {
                            hashtagList.add(hashtag);
                        }
                    }
                }
                // 리사이클뷰 준비
                GridLayoutManager gridLayoutManager = new GridLayoutManager(AlarmActivity.this, calculateColumnCount());
                hashTag_recyclearView.setLayoutManager(gridLayoutManager);

                // 해시태그 어댑터 생성 및 설정
                HashTagAdapter hashTagAdapter = new HashTagAdapter(AlarmActivity.this, hashtagList);
                hashTag_recyclearView.setAdapter(hashTagAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle onCancelled
            }
        });
    }


    //hashTag_RecyclerView에서 행별 열 갯수 설정
    private int calculateColumnCount() {
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int itemWidthThreshold = 300; //hashTag아이템의 크기제한 설정
        int columnCount = screenWidth / itemWidthThreshold; //행별 열갯수 설정
        return Math.max(columnCount, 1);
    }

    //사용자 설정 해시태그에 해당하는 post만 보여주는 메소드
    public void updatePostRecyclerView(List<String> hashtagList) {
        postList.clear(); // 기존 데이터를 지우고 새로운 데이터를 추가할 준비

        conditionRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String postKey = snapshot.getKey();
                    Map<String, Object> firebaseDataMap = (Map<String, Object>) snapshot.getValue();

                    if (firebaseDataMap != null) {
                        String inputTag = (String) firebaseDataMap.get("inputTag");
                        if (inputTag != null) {
                            String[] firebaseTags = inputTag.split("#");

                            boolean hasDuplicate = false;

                            // Firebase에서 가져온 해시태그와 입력한 태그 비교
                            for (String firebaseTag : firebaseTags) {
                                for (String hashTag : hashtagList) {
                                    //중복 된 값이 있으면 루프 종료
                                    if (firebaseTag.equals(hashTag)) {
                                        hasDuplicate = true;
                                        break;
                                    }
                                }
                            }

                            // 중복이 있으면 데이터 처리
                            if (hasDuplicate) {
                                // 데이터 추출
                                String title = (String) firebaseDataMap.get("inputName");
                                String year = String.valueOf(firebaseDataMap.get("year"));
                                String month = String.valueOf(firebaseDataMap.get("month"));
                                String day = String.valueOf(firebaseDataMap.get("day"));
                                String description = (String) firebaseDataMap.get("inputTag");
                                String imageUrl = (String) firebaseDataMap.get("imageUrl");
                                String campus = (String) firebaseDataMap.get("selectedCampus");
                                String arc = (String) firebaseDataMap.get("selectedArc");
                                String dtPlace = (String) firebaseDataMap.get("inputDPlace");
                                String ctg = (String) firebaseDataMap.get("selectedCtg");
                                String etc = (String) firebaseDataMap.get("rfDetail");
                                String userEmailId = (String) firebaseDataMap.get("userEmail");

                                // 기본값으로 처리
                                int intYear = 0, intMonth = 0, intDay = 0;

                                try {
                                    intYear = Integer.parseInt(year);
                                    intMonth = Integer.parseInt(month);
                                    intDay = Integer.parseInt(day);
                                } catch (NumberFormatException e) {
                                    // 정수로 변환할 수 없는 경우 처리
                                    e.printStackTrace();
                                }

                                // 날짜를 합치고 원하는 형식으로 포맷팅
                                String date = String.format("%04d-%02d-%02d", intYear, intMonth, intDay);

                                // PostModel 객체 생성 후 값 설정
                                PostModel post = new PostModel(title, date, description, imageUrl, campus, arc, dtPlace, ctg, etc, userEmailId, postKey, false);
                                // postList에 추가
                                postList.add(post);
                            }
                        }

                    }
                }
                postAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // onCancelled 처리
            }
        });
    }

    //해당 post로 이동하는 메소드
    private void openPostDtActivity(PostModel post) {
        Intent intent = new Intent(AlarmActivity.this, PostDetailActivity.class);

        intent.putExtra("title",post.getTitle());
        intent.putExtra("date",post.getDate());
        intent.putExtra("tag",post.getDescription());
        intent.putExtra("imgUrl",post.getImageUrl());
        intent.putExtra("campus",post.getCampus());
        intent.putExtra("arc",post.getArc());
        intent.putExtra("dtPlace",post.getDtPlace());
        intent.putExtra("ctg",post.getCtg());
        intent.putExtra("etc",post.getEtc());
        intent.putExtra("email",post.getUserEmailId());
        intent.putExtra("postKey",post.getPostKey());

        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updatePostRecyclerView(personalHashtagList);
    }
    private void startFCMService() {
        Intent intent = new Intent(this, FCMService.class);
        startService(intent);
    }
}
