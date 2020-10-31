package app.sunrin.codegreen;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class AfterScanActivity extends AppCompatActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        setContentView(R.layout.activity_splash);
        SharedPreferences preferences = getSharedPreferences("BarcodeResult", 0);
        String result = preferences.getString("result", "");
        System.out.println("■");
        System.out.println(result);
        Button button = findViewById(R.id.button);
        button.setText(result);
    }
}
