package app.sunrin.codegreen;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.util.prefs.Preferences;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    protected void onResume() // 활성 상태일 경우 바코드를 스캔함.
    {
        super.onResume();
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setCaptureActivity(CustomScannerActivity.class); //CustomScannerActivity + activity_custom_scanner을 통해 바코드 스캔 창을 띄우고 바코드를 스캔함.
        integrator.initiateScan();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) { // 바코드가 스캔되면
        super.onActivityResult(requestCode, resultCode, intent);
        Log.d("onActivityResult", "onActivityResult: .");
        if (resultCode == Activity.RESULT_OK) {
            IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
            String re = scanResult.getContents();
            String message = re;
            Log.d("onActivityResult", "onActivityResult: ." + re);

            //Toast.makeText(this, re, Toast.LENGTH_LONG).show(); // 바코드에 인식된 숫자 Toast로 띄우기

            SharedPreferences preferences = getSharedPreferences("BarcodeResult", 0);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("result", re);
            editor.commit();

            Intent intent1 = new Intent(getApplicationContext(), AfterScanActivity.class);
            startActivity(intent1); //새 창(상품정보 띄울 창) 띄우기
            finish();
        }
    }
}