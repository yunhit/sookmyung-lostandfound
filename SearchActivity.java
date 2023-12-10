package pack.mp_team5project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SearchActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<PostModel> postList;
    private PostAdapter postAdapter;


    FirebaseAuth mAuth;
    DatabaseReference conditionRef = FirebaseDatabase.getInstance().getReference().child("Data");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        postList = new ArrayList<>();
        postAdapter = new PostAdapter(this, postList);
        recyclerView.setAdapter(postAdapter);


        conditionRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String postKey = snapshot.getKey();

                    Map<String, Object> firebaseDataMap = (Map<String, Object>) snapshot.getValue();
                    if(firebaseDataMap != null){
                        String title = (String) firebaseDataMap.get("inputName");
                        // Firebase 에 저장된 키가 year month day 이기 때문에
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

                        //PostModel 객체 생성 및 변수 값 대입
                        PostModel post = new PostModel(title, date, description, imageUrl, campus,
                                arc,dtPlace,ctg,etc,userEmailId,postKey, false);
                        postList.add(post);
                    }
                }
                postAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SearchActivity.this, "Failed to load posts.", Toast.LENGTH_SHORT).show();
            }
        });

        postAdapter.setOnItemClickListener(new PostAdapter.OnItemClickListener(){
            @Override
            public void onItemClick(PostModel post){
                openPostDtActivity(post);
            }

            @Override
            public void onDeleteButtonClick(Integer position) {
                String postKey = postList.get(position).getPostKey(); // 삭제할 데이터의 키 가져오기
                conditionRef.child(postKey).removeValue(); // Firebase에서 데이터 삭제
                postList.remove(position);
                postAdapter.notifyItemRemoved(position);
            }
        });



        SearchView searchView = findViewById(R.id.search_view);

        // SearchView의 이벤트 리스너 설정
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // 검색 버튼을 누를 때 호출됩니다. 여기서는 필요하지 않을 수 있습니다.
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // 검색어가 변경될 때마다 호출됩니다.
                performSearch(newText); // 검색어를 가지고 검색을 수행하는 메서드 호출
                return true;
            }
        });
    }

    private void openPostDtActivity(PostModel post) {
        Intent intent = new Intent(SearchActivity.this, PostDetailActivity.class);

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

    // 검색 메서드
    private void performSearch(String query) {
        if (postList == null || query == null) {
            return;
        }

        List<PostModel> filteredList = new ArrayList<>();

        for (PostModel post : postList) {
            // 여기서 post.getTitle(), post.getDescription() 등을 통해 검색 기준을 설정
            // 예를 들어, 제목 또는 설명에 검색어가 포함되어 있는지 확인할 수 있음
            String title = post.getTitle();
            String description = post.getDescription();
            if ((title != null && title.toLowerCase().contains(query.toLowerCase())) ||
                    (description != null && description.toLowerCase().contains(query.toLowerCase()))) {
                filteredList.add(post);
            }
        }

        // RecyclerView 에 검색된 결과 표시
        updateRecyclerView(filteredList);


    }


    private void updateRecyclerView(List<PostModel> resultList) {
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        TextView noResultTextView = findViewById(R.id.no_result_text_view);

        if (resultList.isEmpty()) {
            // 검색 결과가 없을 때 RecyclerView 감추고, 일치하는 검색어가 없음을 나타내는 TextView 보이기
            recyclerView.setVisibility(View.GONE);
            noResultTextView.setVisibility(View.VISIBLE);
            noResultTextView.setText("일치하는 검색어가 없습니다");
        } else {
            // 검색 결과가 있을 때 RecyclerView 보이고, TextView 감추기
            recyclerView.setVisibility(View.VISIBLE);
            noResultTextView.setVisibility(View.GONE);

            // RecyclerView에 검색된 결과 표시
            postAdapter = new PostAdapter(this, resultList);
            recyclerView.setAdapter(postAdapter);
        }
    }


}
