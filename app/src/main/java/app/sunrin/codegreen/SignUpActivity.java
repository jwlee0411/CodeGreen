package app.sunrin.codegreen;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Debug;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.Collection;
import java.util.Iterator;

import org.apache.commons.lang3.StringUtils;

public class SignUpActivity extends AppCompatActivity {

    String[][] finalValue;
    String[] newValue;

    FirebaseDatabase database;
    DatabaseReference myRef;


    public static SignUpActivity signUpActivity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);


        final TextInputLayout id_Layout = findViewById(R.id.TextInput_ID);
        final TextInputLayout pw_Layout = findViewById(R.id.TextInput_PW);
        final TextInputEditText id_ET = findViewById(R.id.text_ID);
        final TextInputEditText pw_ET = findViewById(R.id.text_PW);

        MaterialButton makeBtn = findViewById(R.id.makeBtn);

        signUpActivity = SignUpActivity.this;



        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("user");
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String value = dataSnapshot.getValue().toString();
                System.out.println(value);
                value = value.substring(0, value.length()-2);
                value = value.replace("}, ", "☆");
                //굳이 별로 바꾼 이유는 split에서 정규식을 사용하기 때문에 이렇게 안 하면 에러가 발생하기 때문
                //자세한 것은 이 링크 참고 : https://mytory.net/archives/285
                System.out.println(value);


                newValue = value.split("☆");
                System.out.println(newValue.length);


                finalValue = new String[newValue.length][3];

                for(int i = 0; i<newValue.length; i++)
                {
                    finalValue[i][0] = newValue[i].substring(newValue[i].indexOf("D=")+2, newValue[i].lastIndexOf(", "));
                    finalValue[i][1] = newValue[i].substring(newValue[i].indexOf("W=")+2, newValue[i].indexOf(", "));
                    finalValue[i][2] = newValue[i].substring(newValue[i].indexOf("e=")+2);

                    System.out.println("가" + finalValue[i][0]);
                    System.out.println("나" + finalValue[i][1]);
                    System.out.println("다" + finalValue[i][2]);

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                System.out.println("ERROR");

            }
        });



        makeBtn.setOnClickListener(v -> {
            String input_id = id_ET.getText().toString().replace(" ", "");
            String input_pw = pw_ET.getText().toString().replace(" ", "");


            boolean passwordExist = false;
            for(int i = 0; i<newValue.length; i++)
            {
                if(finalValue[i][0].equals(input_id))
                {
                    passwordExist = true;
                    break;
                }

            }
            // 입력한 ID에 따른 비밀번호 존재 여부 확인
            if (!passwordExist) { // 없을 때
                id_Layout.setErrorEnabled(false);
                if (input_pw.length() >= 8) {


                    long time = System.currentTimeMillis();
                    String strTime = Long.toString(time);
                    myRef = database.getReference("user/" + input_id + "/userID");
                    myRef.setValue(input_id);

                    myRef = database.getReference("user/" + input_id + "/userPW");
                    myRef.setValue(input_pw);

                    myRef = database.getReference("user/" + input_id + "/value");
                    myRef.setValue("");

                    Toast.makeText(SignUpActivity.this, "새 아이디를 생성했습니다!", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
            else
            {
                id_Layout.setError("이미 존재하는 아이디입니다.");
            }
            // 에러 처리
            if (input_id.length() <= 0)
                id_Layout.setError("아이디를 입력해주세요.");
            if (input_pw.length() <= 0)
                pw_Layout.setError("비밀번호를 입력해주세요.");
            else if (input_pw.length() < 8)
                pw_Layout.setError("비밀번호는 8자 이상입니다.");
            else
                pw_Layout.setErrorEnabled(false);
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}