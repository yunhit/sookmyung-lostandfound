package pack.mp_team5project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

public class MyCommentFragment extends Fragment {

    private RecyclerView recyclerView;
    private List<PostModel> postList;
    private PostAdapter postAdapter;

    private List<CommentModel> commentList;
    private CommentAdapter commentAdapter;

    View view;

    PostModel post;

    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    DatabaseReference conditionRef = FirebaseDatabase.getInstance().getReference().child("Data");
    DatabaseReference commentsRef = FirebaseDatabase.getInstance().getReference().child("comments");

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // tablayout
        view = inflater.inflate(R.layout.mypagetab2, container, false);

        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        postList = new ArrayList<>();
        postAdapter = new PostAdapter(getContext(), postList);
        recyclerView.setAdapter(postAdapter);

        commentList = new ArrayList<>();
        commentAdapter = new CommentAdapter(getContext(), commentList);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        String userEmailId = mAuth.getCurrentUser().getEmail();

        commentsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                commentList.clear();
                postList.clear();

                for (DataSnapshot commentSnapshot : dataSnapshot.getChildren()) {
                    CommentModel comment = commentSnapshot.getValue(CommentModel.class);

                    if (comment != null) {
                        String author = comment.getAuthor();

                        if (author != null && currentUser != null && author.equals(userEmailId)) {
                            String postKey = comment.getTargetPostKey();

                            // 해당 댓글의 postKey를 사용하여 게시글 정보를 가져옴
                            conditionRef.child(postKey).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot postSnapshot) {
                                    post = postSnapshot.getValue(PostModel.class);
                                    if (post != null) {
                                        post.setTitle(postSnapshot.child("inputName").getValue(String.class));

                                        post.setDescription(postSnapshot.child("inputTag").getValue(String.class));
                                        post.setImageUrl(postSnapshot.child("imageUrl").getValue(String.class));

                                        post.setCampus(postSnapshot.child("selectedCampus").getValue(String.class));
                                        post.setArc(postSnapshot.child("selectedArc").getValue(String.class));
                                        post.setDtPlace(postSnapshot.child("inputDPlace").getValue(String.class));
                                        post.setCtg(postSnapshot.child("selectedCtg").getValue(String.class));
                                        post.setEtc(postSnapshot.child("rfDetail").getValue(String.class));

                                        String postKey = conditionRef.child(comment.getTargetPostKey()).getKey();
                                        post.setPostKey(postKey);

                                        Long year = postSnapshot.child("year").getValue(Long.class);
                                        Long month = postSnapshot.child("month").getValue(Long.class);
                                        Long day = postSnapshot.child("day").getValue(Long.class);

                                        String date = String.format("%04d-%02d-%02d", year, month, day);
                                        post.setDate(date);

                                        post.setUserEmailId(postSnapshot.child("userEmail").getValue(String.class));

                                        postList.add(post);
                                        postAdapter.notifyDataSetChanged();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    Toast.makeText(getContext(), "데이터 불러오기에 실패하였습니다", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }
                }

                if (postList.isEmpty()) {
                    view.findViewById(R.id.no_result_text_view).setVisibility(View.VISIBLE);
                } else {
                    view.findViewById(R.id.no_result_text_view).setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "데이터 불러오기에 실패하였습니다", Toast.LENGTH_SHORT).show();
            }
        });

        // 해당하는 글로 이동
        postAdapter.setOnItemClickListener(new PostAdapter.OnItemClickListener(){
            @Override
            public void onItemClick(PostModel post){
                openPostDtActivity(post);
            }

            @Override
            public void onDeleteButtonClick(Integer position) {
                // 사용X
            }
        });

        return view;
    }


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