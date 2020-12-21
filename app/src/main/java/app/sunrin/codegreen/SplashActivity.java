package app.sunrin.codegreen;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity  extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceStare)
    {
        super.onCreate(savedInstanceStare);
        setContentView(R.layout.activity_splash);
        // 어플리케이션을 열면 처음에 실행됨.(SpalshActivity -> MainActivity로 실행되는 구조)


        Button buttonBarcode = findViewById(R.id.buttonBarcode);
        Button buttonPhoto = findViewById(R.id.buttonPhoto);
        Button buttonRecent = findViewById(R.id.buttonRecent);
        Button buttonLogout = findViewById(R.id.buttonLogout);
        Button buttonSetting = findViewById(R.id.buttonSetting);

        buttonBarcode.setOnClickListener(v ->
        {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        });

        buttonPhoto.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), MainActivity.class))); //TODO

        buttonRecent.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), RecentActivity.class)));

        buttonLogout.setOnClickListener(v -> {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        buttonSetting.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(), SettingActivity.class)));



    }


}


