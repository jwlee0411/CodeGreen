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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_scan);
        SharedPreferences preferences = getSharedPreferences("BarcodeResult", 0);
        String result = preferences.getString("result", "");
        System.out.println("■");
        System.out.println(result);
        Toast.makeText(this, result, Toast.LENGTH_LONG).show();
        Button button = findViewById(R.id.button);
        button.setText(result);
    }
}
