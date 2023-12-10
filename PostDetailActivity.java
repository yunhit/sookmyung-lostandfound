package pack.mp_team5project;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class PostDetailActivity extends AppCompatActivity {

    TextView itmName, itmDate, itmTag, itmPlace, itmCtg, itmEtc, userEmailId;
    ImageView itmImgView;
    EditText editTextComment;
    Button commentBtn;
    String postKey, commentUserEmail;

    private RecyclerView recyclerView;
    private List<CommentModel> commentList;
    private CommentAdapter commentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);


        Intent intent = getIntent();
        postKey = intent.getStringExtra("postKey");

        String title = intent.getStringExtra("title");
        String date = intent.getStringExtra("date");
        String tag = intent.getStringExtra("tag");
        String imgUrl = intent.getStringExtra("imgUrl");
        String ctg = intent.getStringExtra("ctg");
        String etc = intent.getStringExtra("etc");
        String email = intent.getStringExtra("email");
        String postEmail = email + "님의 게시물";


        String campus = intent.getStringExtra("campus");
        String arc = intent.getStringExtra("arc");
        String dtPlace = intent.getStringExtra("dtPlace");
        String fnPlace = campus + " " + arc + " " + dtPlace;



        itmName = findViewById(R.id.itemName);
        itmDate = findViewById(R.id.itemDate);
        itmTag = findViewById(R.id.itemTag);
        itmImgView = findViewById(R.id.itemImageView);
        itmPlace = findViewById(R.id.itemPlace);
        itmCtg = findViewById(R.id.itemCtg);
        itmEtc = findViewById(R.id.itemEtc);
        userEmailId = findViewById(R.id.textUserEmailId);


        itmName.setText(title);
        itmDate.setText(date);
        itmTag.setText(tag);
        itmPlace.setText(fnPlace);
        itmCtg.setText(ctg);
        itmEtc.setText(etc);
        userEmailId.setText(postEmail);


        Glide.with(this)
                .load(imgUrl)
                .error(R.drawable.bu)
                .into(itmImgView);

        commentList = new ArrayList<>();


        editTextComment = findViewById(R.id.editTextComment);
        recyclerView = findViewById(R.id.recyclerViewComments);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        loadComments();

        commentBtn = (Button) findViewById(R.id.buttonPostComment);
        commentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUser user =  FirebaseAuth.getInstance().getCurrentUser();
                if(user != null){
                    commentUserEmail = user.getEmail(); // user email 정보 불러오기
                }
                String commentContent = editTextComment.getText().toString();
                String commentAuthor = commentUserEmail;

                if(!TextUtils.isEmpty(commentContent)) {
                    String commentId = FirebaseDatabase.getInstance().getReference()
                            .child("comments").push().getKey();

                    Map<String, Object> commentData = new HashMap<>();
                    commentData.put("author",commentAuthor);
                    commentData.put("commentContent", commentContent);
                    commentData.put("timeStamp",getTimestamp());
                    commentData.put("targetPostKey",postKey);

                    FirebaseDatabase.getInstance().getReference().child("comments")
                            .child(commentId).setValue(commentData);

                    CommentModel newComment = new CommentModel(commentAuthor, commentContent, getTimestamp(), postKey);
                    commentList.add(newComment);

                    if(commentAdapter == null){
                        commentAdapter = new CommentAdapter(PostDetailActivity.this, commentList);
                        recyclerView.setAdapter(commentAdapter);
                    } else {
                        commentAdapter.notifyDataSetChanged();
                    }

                    editTextComment.setText("");

                }


            }
        });




    }

    private void loadComments() {
        CommentLoader.loadCommentsForPost(postKey, new CommentLoader.CommentLoadListener() {
            @Override
            public void onCommentsLoaded(List<CommentModel> comments) {
                // 댓글 있는 경우
                commentList.addAll(comments);

                if(commentAdapter == null) {
                    commentAdapter = new CommentAdapter(PostDetailActivity.this,commentList);
                    recyclerView.setAdapter(commentAdapter);
                } else {
                    commentAdapter.notifyDataSetChanged();
                }


            }

            @Override
            public void onNoComments() {
                // 불러올 댓글 없는 경우
                Toast.makeText(PostDetailActivity.this,"댓글을 작성해보세요!",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onError(DatabaseError databaseError) {
                Toast.makeText(PostDetailActivity.this,"Failed to loasd comments",Toast.LENGTH_SHORT).show();

            }


        });
    }


    private String getTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }
}
