package pack.mp_team5project;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pack.mp_team5project.AlarmActivity;
import pack.mp_team5project.R;

public class FCMService extends FirebaseMessagingService {

    private static final String TAG = "FCMService";
    String title = "";
    String msg = "";
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    DatabaseReference HashRef = mRootRef.child("HashTag_Data");
    DatabaseReference HashDataRef = HashRef.child("HashTag_" + mAuth.getCurrentUser().getUid());

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        Log.d("FCM Token", token);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // FCM 메시지 수신 시 호출되는 메서드
        if (remoteMessage.getNotification() != null) {
            title = remoteMessage.getNotification().getTitle();
            msg = remoteMessage.getNotification().getBody();
            showNotification(title, msg);
        }

        if (remoteMessage.getData().size() > 0) {
            handleDataPayload(remoteMessage.getData());
        }
    }

    private void handleDataPayload(Map<String, String> data) {
        String title = data.get("title");
        String body = data.get("body");
        String hashtag = data.get("hashtag");

        // 사용자 설정 해시태그와 post의 해시태그가 일치할 경우에만 알림 표시
        getUserHashtagsAndCheckMatch(hashtag);
    }

    private void getUserHashtagsAndCheckMatch(String receivedHashtag) {
        HashDataRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<String> userHashtags = new ArrayList<>();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String hashtag = snapshot.getValue(String.class);
                    if (hashtag != null) {
                        userHashtags.add(hashtag);
                    }
                }
                if (isHashtagMatch(receivedHashtag, userHashtags)) {
                    showNotification(title, msg);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // 오류 처리
            }
        });
    }

    private boolean isHashtagMatch(String receivedHashtag, List<String> userHashtags) {
        String[] postTags = receivedHashtag.split("#");
        for (String tag : postTags) {
            if (userHashtags.contains(tag)) {
                return true;
            }
        }
        return false;
    }

    public static void sendFCMNotification(String topic, String title, String body, String hashtag) {
        RemoteMessage.Builder remoteMessageBuilder = new RemoteMessage.Builder(topic);
        Map<String, String> data = new HashMap<>();
        data.put("title", title);
        data.put("body", body);
        data.put("hashtag", hashtag);
        remoteMessageBuilder.setData(data);
        FirebaseMessaging.getInstance().send(remoteMessageBuilder.build());
    }

    //알림 클릭시 AlarmActivity로 이동하는 메소드 & 알림 형태 설정
    private void showNotification(String title, String body) {
        Intent intent = new Intent(this, AlarmActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("notification_title", title);
        intent.putExtra("notification_body", body);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.logo_img)
                .setContentTitle(title)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setVibrate(new long[]{1, 1000})
                .setContentIntent(contentIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, mBuilder.build());
    }

    // 해당 해시태그로 FirebaseMessaging의 토픽 가입 해지
    public static void unsubscribeFromFCMTopic(String hashtag) {
        FirebaseMessaging.getInstance().unsubscribeFromTopic(hashtag)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                    } else {
                    }
                });
    }
    // 해당 해시태그로 FirebaseMessaging의 토픽 가입
    public static void subscribeToFCMTopic(String hashtag) {
        FirebaseMessaging.getInstance().subscribeToTopic(hashtag)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d(TAG, "Successfully subscribed to the topic: " + hashtag);
                    } else {
                        Log.e(TAG, "Failed to subscribe to the topic: " + hashtag, task.getException());
                    }
                });
    }

}
