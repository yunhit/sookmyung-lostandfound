package com.example.register_info;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

public class registerInfoActivity extends AppCompatActivity {

    Spinner campusSpinner, arcSpinner, ctgSpinner;
    EditText dtPlace, itName, ipTag;
    DatePicker datePicker;
    ImageView inputImage;
    Button attachBtn,rgBtn;
    ActivityResultLauncher<Intent> launcher;

    int year, month,day;
    private static final int REQUEST_CODE_PERMISSION = 100;
    private static final int REQUEST_CODE_ATTACH_FILE = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registerinfolayout);

        setTitle("분실물 같이 찾송");

        //Spinner 설정
        campusSpinner = findViewById(R.id.which_campus);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(
                this, R.array.which_campus, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        campusSpinner.setAdapter(adapter1);

        arcSpinner = findViewById(R.id.which_arc);
        ArrayAdapter<CharSequence> adapter_1campus = ArrayAdapter.createFromResource(
                this,R.array.campus1_arc, android.R.layout.simple_spinner_item);
        adapter_1campus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<CharSequence> adapter_2campus = ArrayAdapter.createFromResource(
                this,R.array.campus2_arc, android.R.layout.simple_spinner_item);
        adapter_2campus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        ArrayAdapter<CharSequence> adapter_etc = ArrayAdapter.createFromResource(
                this,R.array.etc, android.R.layout.simple_spinner_item);
        adapter_etc.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        campusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View view, int position, long id) {
                String selectedCategory = parentView.getItemAtPosition(position).toString();
                if (selectedCategory.equals("제 1캠퍼스")){
                    arcSpinner.setAdapter(adapter_1campus);
                }
                else if(selectedCategory.equals("제 2캠퍼스")){
                    arcSpinner.setAdapter(adapter_2campus);
                }
                else{
                    arcSpinner.setAdapter(adapter_etc);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ctgSpinner = findViewById(R.id.categoryList); // ctgSpinner 초기화 추가

        ArrayAdapter<CharSequence> ctgAdapter = ArrayAdapter.createFromResource(
                this, R.array.category, android.R.layout.simple_spinner_item);
        ctgAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ctgSpinner.setAdapter(ctgAdapter);

        datePicker = findViewById(R.id.datePicker);
        year = datePicker.getYear();
        month = datePicker.getMonth() + 1 ;
        day = datePicker.getDayOfMonth();



        //첨부파일 입력받기
        inputImage = findViewById(R.id.inputImage);
        attachBtn = findViewById(R.id.attachButton);
        attachBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attachFile();
            }
        });

        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK){
                    if (result.getData() != null && result.getData().getData() != null) {
                        inputImage.setImageURI(result.getData().getData());
                    }
                }
            }
        });



        rgBtn = findViewById(R.id.registerBtn);
        rgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                navigateToAfterInfoActivity();
            }
        });



    }

    private void checkPermission() {
        if(ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_CODE_PERMISSION);
        }
        else{
            attachFile();
        }
    }

    //권한이 거부됐을때
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == REQUEST_CODE_PERMISSION){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                attachFile();
            }
            else{
                Toast.makeText(this,"권한이 거부되었습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void attachFile() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        launcher.launch(intent);
    }

    private void navigateToAfterInfoActivity(){
        Intent intent = new Intent(registerInfoActivity.this, afterInfoActivity.class);

        String selectedCampus = campusSpinner.getSelectedItem().toString();
        String selectedArc = arcSpinner.getSelectedItem().toString();
        intent.putExtra("KEY_CAMPUS", selectedCampus);
        intent.putExtra("KEY_ARC",selectedArc);


        dtPlace = findViewById(R.id.detailPlace);
        String inputDPlace = dtPlace.getText().toString();
        intent.putExtra("KEY_DETAIL_PLACE",inputDPlace);


        intent.putExtra("KEY_YEAR",year);
        intent.putExtra("KEY_MONTH",month);
        intent.putExtra("KEY_DAY", day);

        String selectedCtg = ctgSpinner.getSelectedItem().toString();
        intent.putExtra("KEY_CTG",selectedCtg);


        itName = findViewById(R.id.itemName);
        String inputName = itName.getText().toString();
        intent.putExtra("KEY_ITEM_NAME",inputName);

        ipTag = findViewById(R.id.inputTag);
        String inputTag = ipTag.getText().toString();
        intent.putExtra("KEY_TAG",inputTag);

        if (inputImage != null && inputImage.getDrawable() != null){
            Uri imageUri = getImageUri(inputImage.getDrawable());
            intent.putExtra("KEY_IMAGE_URI",imageUri.toString());
        }




        startActivity(intent);
    }

    private Uri getImageUri(Drawable drawable) {
        BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
        Bitmap bitmap = bitmapDrawable.getBitmap();

        String path = MediaStore.Images.Media.insertImage(getContentResolver(),bitmap,
                "image description",null);
        return Uri.parse(path);
    }
}