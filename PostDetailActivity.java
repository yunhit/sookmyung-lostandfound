package pack.mp_team5project;


import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;


public class PostDetailActivity extends AppCompatActivity {

    TextView itmName, itmDate, itmTag, itmPlace, itmCtg, itmEtc, userID;
    ImageView itmImgView;

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
        String messageUid = uid + "님의 게시물";

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
        userID.setText(messageUid);


        Glide.with(this)
                .load(imgUrl)
                .error(R.drawable.bu)
                .into(itmImgView);


    }
}
