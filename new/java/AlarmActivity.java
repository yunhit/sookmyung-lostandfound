package pack.mp_team5project;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class AlarmActivity extends AppCompatActivity {

    private static final int REQUEST_ALARM_PERMISSION = 1; // Replace with your actual request code

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        if (ContextCompat.checkSelfPermission(AlarmActivity.this, Manifest.permission.POST_NOTIFICATIONS)
                == PackageManager.PERMISSION_GRANTED) {
            //
        } else {
            //권한이 없을 때 권한 요청
            ActivityCompat.requestPermissions(AlarmActivity.this, new String[]{Manifest.permission.POST_NOTIFICATIONS},
                    REQUEST_ALARM_PERMISSION);
        }
    }
}