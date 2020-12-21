package app.sunrin.codegreen;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.util.ArrayList;
import java.util.Scanner;
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
    public void onBackPressed() {
        startActivity(new Intent(MainActivity.this, SplashActivity.class));
        finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) { // 바코드가 스캔되면
        super.onActivityResult(requestCode, resultCode, intent);
        Log.d("onActivityResult", "onActivityResult: .");
        if (resultCode == Activity.RESULT_OK) {
            IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
            String re = scanResult.getContents();
            System.out.println(re);


            char tmp;
            boolean output = true;	// 결과값을 저장할 변수, 참/거짓밖에 없기 때문에 boolean으로 선언

            for(int i = 0 ; i < re.length() ; i++) {
                tmp = re.charAt(i);	//한글자씩 검사하기 위해서 char형 변수인 tmp에 임시저장
                if(!('0' <= tmp &&  tmp <= '9')) {	//문자가 0 ~ 9 사이가 아닐경우
                    output = false;	//output을 false로 바꾼다.
                }
            }

            if(output)
            {

                SharedPreferences preferences = getSharedPreferences("BarcodeResult", 0);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("result", re);
                editor.commit();

                Intent intent1 = new Intent(getApplicationContext(), AfterScanActivity.class);
                startActivity(intent1); //새 창(상품정보 띄울 창) 띄우기
                finish();

            }
            else
            {
                Toast.makeText(getApplicationContext(), "올바른 바코드가 아닙니다.", Toast.LENGTH_SHORT).show();
            }

        }
        else
        {
            startActivity(new Intent(MainActivity.this, SplashActivity.class));
            finish();
        }
    }
}