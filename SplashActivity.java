package pack.mp_team5project;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 800;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // ImageView 가져오기
        ImageView splash_img = findViewById(R.id.splash_LogoImg);

        // 애니메이션 로드
        Animation splashAnimation = AnimationUtils.loadAnimation(this, R.anim.anim_splash);

        // ImageView에 애니메이션 적용
        splash_img.startAnimation(splashAnimation);

        splashAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                // 애니메이션이 시작될 때의 동작
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                // 애니메이션이 끝난 후의 동작
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }, SPLASH_TIME_OUT);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // 애니메이션이 반복될 때의 동작
            }
        });
    }
}