package pack.mp_team5project;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AfterInfoActivity extends AppCompatActivity {

    TextView dpLocation, dpItemName, dpCategory, dpTag, dpDate;
    ImageView dpImage , banner;

    int year, month, day;

    android.widget.Button goMainBtn;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_info);

        setTitle("분실물 같이 찾송");

        Intent intent = getIntent();

        String imageUriString = intent.getStringExtra("KEY_IMAGE_URI");
        if (imageUriString != null){
            Uri imageUri = Uri.parse(imageUriString);

            dpImage = findViewById(R.id.displayImage);
            dpImage.setImageURI(imageUri);
        }
        String itemName = intent.getStringExtra("KEY_ITEM_NAME");
        dpItemName = findViewById(R.id.displayItemName);
        dpItemName.setText(": " +itemName);

        String selectedCtg = intent.getStringExtra("KEY_CTG");
        dpCategory = findViewById(R.id.displayCategory);
        dpCategory.setText(": " + selectedCtg);

        year = intent.getIntExtra("KEY_YEAR",0);
        month = intent.getIntExtra("KEY_MONTH",0);
        day = intent.getIntExtra("KEY_DAY", 0);
        dpDate = findViewById(R.id.displayDate);
        dpDate.setText(": "+ year + "." + month + "." + day);

        String selectedCampus = intent.getStringExtra("KEY_CAMPUS");
        String selectedArc = intent.getStringExtra("KEY_ARC");
        String detailPlace = intent.getStringExtra("KEY_DETAIL_PLACE");
        dpLocation = findViewById(R.id.displayLocation);
        dpLocation.setText(": "+selectedCampus + " " + selectedArc + " " + detailPlace);

        String inputTag = intent.getStringExtra("KEY_TAG");
        dpTag = findViewById(R.id.displayTag);
        dpTag.setText(": " + inputTag);

        banner = findViewById(R.id.banner);
        banner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String adUrl ="https://direct.samsunglife.com/index.eds?cid=di:google_search_pc:main:main_brand:0_0&utm_campaign=internet&utm_medium=pc-sa&utm_source=google_search_pc&utm_content=main&utm_term=%EC%82%BC%EC%84%B1%EB%8B%A4%EC%9D%B4%EB%A0%89%ED%8A%B8%EB%B3%B4%ED%97%98&_AT=000202C800B303764E95&gad_source=1&gclid=Cj0KCQiA6vaqBhCbARIsACF9M6kzpTQ_B9nIBhHiumjFf_-eSmS2pLTkctE9b2K-7pO9b1XR9JbD5QgaAmdxEALw_wcB";

                openWebView(adUrl);
            }
        });

        goMainBtn = (android.widget.Button) findViewById(R.id.goMainBtn);
        goMainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentToAlarmPage = new Intent(AfterInfoActivity.this, MainActivity.class);
                startActivity(intentToAlarmPage);
            }
        });

    }

    private void openWebView(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }
}