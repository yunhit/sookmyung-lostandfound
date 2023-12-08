package pack.mp_team5project;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MyWrittingFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<PostModel> postList;
    private PostAdapter postAdapter;

    private List<CommentModel> commentList;
    private CommentAdapter commentAdapter;

    View view;

    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    DatabaseReference conditionRef = FirebaseDatabase.getInstance().getReference().child("Data");

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // tablayout
        view = inflater.inflate(R.layout.mypagetab1, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        postList = new ArrayList<>();
        postAdapter = new PostAdapter(getContext(), postList);
        recyclerView.setAdapter(postAdapter);

        commentList = new ArrayList<>();

        // 현재 사용자 가져오기
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        conditionRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String postKey = snapshot.getKey();

                    //가져온 데이터를 Map으로 변환
                    Map<String, Object> firebaseDataMap = (Map<String, Object>) snapshot.getValue();

                    if(firebaseDataMap != null) {
                        String uid = (String) firebaseDataMap.get("userID");

                        // firebase uid가 현재 uid와 같은 글만 가져오기
                        if (uid != null && currentUser != null && uid.equals(currentUser.getUid())) {
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

                            int intYear = 0, intMonth = 0, intDay = 0;

                            try {
                                intYear = Integer.parseInt(year);
                                intMonth = Integer.parseInt(month);
                                intDay = Integer.parseInt(day);
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }

                            String date = String.format("%04d-%02d-%02d", intYear, intMonth, intDay);

                            //PostModel 객체 생성 및 변수 값 대입
                            PostModel post = new PostModel(title, date, description, imageUrl, campus,
                                    arc,dtPlace, ctg, etc, userEmailId, postKey);
                            postList.add(post);
                        }
                    }
                }
                postAdapter.notifyDataSetChanged();
                if (postList.isEmpty()) {
                    view.findViewById(R.id.no_result_text_view).setVisibility(View.VISIBLE);
                } else {
                    view.findViewById(R.id.no_result_text_view).setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Fragment에서 Toast는 getContext()로 해야함
                Toast.makeText(getContext(), "데이터 불러오기에 실패하였습니다", Toast.LENGTH_SHORT).show();
            }
        });

        // 해당하는 글로 이동
        postAdapter.setOnItemClickListener(new PostAdapter.OnItemClickListener(){
            @Override
            public void onItemClick(PostModel post){
                openPostDtActivity(post);
            }
        });
        // 마이페이지-내가 쓴 글 view 반환
        return view;
    }

    // 게시글로 이동
   private void openPostDtActivity(PostModel post) {
        // Fragment에서는 getActivity()로 현재 fragment가 속한 activity를 가져와야 함
        Intent intent = new Intent(getActivity(),PostDetailActivity.class);

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

}
