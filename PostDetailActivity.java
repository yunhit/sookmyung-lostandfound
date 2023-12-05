package pack.mp_team5project;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Comment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class PostDetailActivity extends AppCompatActivity {

    TextView itmName, itmDate, itmTag, itmPlace, itmCtg, itmEtc, userID;
    ImageView itmImgView;
    EditText editTextComment;
    Button commentBtn;

    private RecyclerView recyclerView;
    private List<CommentModel> commentList;
    private CommentAdapter commentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String date = intent.getStringExtra("date");
        String tag = intent.getStringExtra("tag");
        String imgUrl = intent.getStringExtra("imgUrl");
        String ctg = intent.getStringExtra("ctg");
        String etc = intent.getStringExtra("etc");


        String campus = intent.getStringExtra("campus");
        String arc = intent.getStringExtra("arc");
        String dtPlace = intent.getStringExtra("dtPlace");
        String fnPlace = campus + " " + arc + " " + dtPlace;

        String uid = intent.getStringExtra("uid");
        String postUid = uid + "님의 게시물";
        String commentUid = uid + "님의 댓글";

        itmName = findViewById(R.id.itemName);
        itmDate = findViewById(R.id.itemDate);
        itmTag = findViewById(R.id.itemTag);
        itmImgView = findViewById(R.id.itemImageView);
        itmPlace = findViewById(R.id.itemPlace);
        itmCtg = findViewById(R.id.itemTag);
        itmEtc = findViewById(R.id.itemEtc);
        userID = findViewById(R.id.textUserId);

        itmName.setText(title);
        itmDate.setText(date);
        itmTag.setText(tag);
        itmPlace.setText(fnPlace);
        itmCtg.setText(ctg);
        itmEtc.setText(etc);
        userID.setText(postUid);


        Glide.with(this)
                .load(imgUrl)
                .error(R.drawable.bu)
                .into(itmImgView);

        commentList = new ArrayList<>();

        commentBtn = (Button) findViewById(R.id.buttonPostComment);
        editTextComment = findViewById(R.id.editTextComment);
        recyclerView = findViewById(R.id.recyclerViewComments);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));



        commentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String commentContent = editTextComment.getText().toString();

                if (commentList != null) {
                    CommentModel newComment = new CommentModel(commentUid, commentContent, getTimestamp());
                    commentList.add(newComment);

                    if (commentAdapter == null) {
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

    private String getTimestamp() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }
}
