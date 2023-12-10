package pack.mp_team5project;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


public class RegisterInfoActivity extends AppCompatActivity {

    Spinner campusSpinner, arcSpinner, ctgSpinner;
    EditText dtPlace, itName, ipTag, rfDetail;
    DatePicker datePicker;
    ImageView inputImage;
    Button attachBtn,rgBtn;
    ActivityResultLauncher<Intent> launcher;
    private FirebaseAuth mAuth;
    String email;

    int defaultYear, defaultMonth, defaultDay;
    int selectedYear, selectedMonth,selectedDay;
    private static final int REQUEST_CODE_PERMISSION = 100;
    private static final int REQUEST_CODE_ATTACH_FILE = 101;

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference conditionRef = mRootRef.child("Data");
    DatabaseReference dataRef; // 클래스 레벨에서 dataRef 선언


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_info);


        mAuth = FirebaseAuth.getInstance(); //Post data안에 현재 UID를 등록하기 위함
        FirebaseUser user =  FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            email = user.getEmail(); // user email 정보 불러오기
        }
        dataRef = conditionRef.push(); // onCreate 내에서 dataRef 초기화

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

        Calendar calendar = Calendar.getInstance();
        defaultYear = calendar.get(Calendar.YEAR);
        defaultMonth = calendar.get(Calendar.MONTH);
        defaultDay = calendar.get(Calendar.DAY_OF_MONTH);

        // 디퐅트 값(현 날짜)에서 data 수정이 없을 경우
        selectedYear = defaultYear;
        selectedMonth = defaultMonth + 1;
        selectedDay = defaultDay;

        datePicker = findViewById(R.id.datePicker);
        datePicker.init(defaultYear, defaultMonth, defaultDay, new DatePicker.OnDateChangedListener() {
            @Override
            public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                selectedYear = year;
                selectedMonth = monthOfYear + 1;
                selectedDay = dayOfMonth;
            }
        });



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

        // keyword 입력 해시태그(#) 자동 생성 기능
        ipTag = findViewById(R.id.inputTag);
        ipTag.addTextChangedListener(new TextWatcher() {
            boolean isInitialChange = true;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isInitialChange) {
                    isInitialChange = false;
                    if (count > 0 && !Character.toString(s.charAt(start)).equals("#")) {
                        ipTag.setText("#" + s.toString());
                        ipTag.setSelection(ipTag.getText().length());
                    }
                } else if (count > 0 && s.charAt(start) == ' ') {
                    String newText = s.toString().substring(0, start + 1) + "#";
                    ipTag.setText(newText);
                    ipTag.setSelection(newText.length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


    }

    private void checkPermission() {
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
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
        Intent intent = new Intent(RegisterInfoActivity.this, AfterInfoActivity.class);

        String selectedCampus = campusSpinner.getSelectedItem().toString();
        String selectedArc = arcSpinner.getSelectedItem().toString();
        intent.putExtra("KEY_CAMPUS", selectedCampus);
        intent.putExtra("KEY_ARC",selectedArc);


        dtPlace = findViewById(R.id.detailPlace);
        String inputDPlace = dtPlace.getText().toString();
        intent.putExtra("KEY_DETAIL_PLACE",inputDPlace);


        intent.putExtra("KEY_YEAR",selectedYear);
        intent.putExtra("KEY_MONTH",selectedMonth);
        intent.putExtra("KEY_DAY", selectedDay);

        String selectedCtg = ctgSpinner.getSelectedItem().toString();
        intent.putExtra("KEY_CTG",selectedCtg);


        itName = findViewById(R.id.itemName);
        String inputName = itName.getText().toString();
        intent.putExtra("KEY_ITEM_NAME",inputName);

        ipTag = findViewById(R.id.inputTag);
        String inputTag = ipTag.getText().toString();
        intent.putExtra("KEY_TAG",inputTag);

        rfDetail = findViewById(R.id.rfDetail);
        String refDetail = rfDetail.getText().toString();

        if (inputImage != null && inputImage.getDrawable() != null){
            Uri imageUri = getImageUri(inputImage.getDrawable());
            intent.putExtra("KEY_IMAGE_URI",imageUri.toString());
        }

        intent.putExtra("KEY_DATA_KEY", dataRef.getKey());
        intent.putExtra("KEY_USER_EMAIL",email);
        intent.putExtra("KEY_DATA_KEY", dataRef.getKey());

        startActivity(intent);

        // firebase Realtime Database에 data 저장
        dataRef.child("selectedCampus").setValue(selectedCampus);
        dataRef.child("selectedArc").setValue(selectedArc);
        dataRef.child("inputDPlace").setValue(inputDPlace);
        dataRef.child("year").setValue(selectedYear); // DatePicker 선택된 값으로 변수 수정
        dataRef.child("month").setValue(selectedMonth);
        dataRef.child("day").setValue(selectedDay);
        dataRef.child("selectedCtg").setValue(selectedCtg);
        dataRef.child("inputName").setValue(inputName);
        dataRef.child("inputTag").setValue(inputTag);
        dataRef.child("rfDetail").setValue(refDetail);
        dataRef.child("userID").setValue(mAuth.getCurrentUser().getUid());
        dataRef.child("userEmail").setValue(email);

        // 이미지를 Firebase Storage에 업로드
        if (inputImage != null && inputImage.getDrawable() != null) {
            Uri imageUri = getImageUri(inputImage.getDrawable());
            uploadImageToFirebase(imageUri);
        } else {
            Toast.makeText(this, "데이터가 성공적으로 저장되었습니다.", Toast.LENGTH_SHORT).show();
        }

        // FCM 메시지 전송
        FCMService.sendFCMNotification(inputTag, inputName, "설정한 해시태그를 포함하는 분실물이 등록되었어요!", inputTag);
    }

    private void uploadImageToFirebase(Uri imageUri) {
        // 이미지를 Firebase Storage에 업로드
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference imagesRef = storageRef.child("images/" + imageUri.getLastPathSegment());
        UploadTask uploadTask = imagesRef.putFile(imageUri);

        uploadTask.addOnSuccessListener(taskSnapshot -> {
            // 이미지 업로드 성공 시, 이미지의 다운로드 URL을 가져와서 데이터베이스에 저장
            imagesRef.getDownloadUrl().addOnSuccessListener(uri -> {
                String imageUrl = uri.toString();
                saveDataToDatabase(imageUrl);
            });
        }).addOnFailureListener(exception -> {
            // 이미지 업로드 실패 시 처리
            Toast.makeText(RegisterInfoActivity.this, "이미지 업로드 실패", Toast.LENGTH_SHORT).show();
        });
    }

    private void saveDataToDatabase(String imageUrl) {
        // 이미지 다운로드 URL을 데이터베이스에 저장
        dataRef.child("imageUrl").setValue(imageUrl);

        Toast.makeText(this, "데이터가 성공적으로 저장되었습니다.", Toast.LENGTH_SHORT).show();
        // 데이터 저장 후 원하는 작업을 수행하도록 처리
    }

    private Uri getImageUri(Drawable drawable) {
        BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
        Bitmap bitmap = bitmapDrawable.getBitmap();

        // 파일로 이미지 저장
        File cacheDir = this.getCacheDir();
        File imagePath = new File(cacheDir, "image.png");
        try {
            FileOutputStream fos = new FileOutputStream(imagePath);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 파일의 Uri 반환
        return Uri.fromFile(imagePath);
//        BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
//        Bitmap bitmap = bitmapDrawable.getBitmap();
//
//        String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "image description", null);
//        return Uri.parse(path);
    }
}
