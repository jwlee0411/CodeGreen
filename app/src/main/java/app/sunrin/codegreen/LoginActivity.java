package app.sunrin.codegreen;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    String[][] finalValue;
    String[] newValue;
    int idIndex;
    int i = 0;

    FirebaseDatabase database;
    DatabaseReference myRef;
    SharedPreferences preferencesGetLogined, preferencesSaveAll, preferencesID, preferencesPW;

    TextInputLayout id_Layout, pw_Layout;
    TextInputEditText id_ET, pw_ET;
    MaterialButton sign_in_btn, sign_up_btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        preferencesGetLogined = getSharedPreferences("getLogined", 0);
        preferencesSaveAll = getSharedPreferences("saveAll", 0);
        preferencesID = getSharedPreferences("ID", 0);
        preferencesPW = getSharedPreferences("PW", 0);
        Boolean getLogined = this.preferencesGetLogined.getBoolean("getLogined", false);


        //TODO:자동로그인 사용 중지(오류 해결을 위해)

        if(getLogined)
        {
            Toast.makeText(LoginActivity.this, "자동 로그인 되었습니다!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(), SplashActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        else
        {
            id_Layout = findViewById(R.id.TextInput_ID);
            pw_Layout = findViewById(R.id.TextInputChangePassword);
            id_ET = findViewById(R.id.edit_id);
            pw_ET = findViewById(R.id.editPassword);
            sign_in_btn = findViewById(R.id.signInBtn);
            sign_up_btn = findViewById(R.id.signUpBtn);


            database = FirebaseDatabase.getInstance();


            int maxVal = 8;
            finalValue = new String[10000][8];

            //TODO : 로그인 관련 알고리즘 갈아엎어야 함.
            myRef = database.getReference("user");


            myRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    String valueAll = snapshot.getValue().toString();
                    valueAll = valueAll.replace("{", "").replace("}", "")
                            .replace("userSex=", "")
                            .replace("userYear=", "")
                            .replace("userMonth=", "")
                            .replace("userPW=", "")
                            .replace("userDay=", "")
                            .replace("userID=", "")
                            .replace("value=", "")
                            .replace("userAge=", "");
                    String[] newValue = valueAll.split(", ");
                    System.out.println(valueAll + "★");
                    //System.out.println(newValue[0] + newValue[1] + newValue[2]);

                    for(int j = 0; j<newValue.length; j++)
                    {
                        finalValue[i][j] = newValue[j];
                        System.out.println(newValue[j]);
                    }
                    i++;

                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


//            myRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//                    String valueAll = dataSnapshot.getValue().toString();
//
//
//                    String value = dataSnapshot.getValue().toString();
//                    System.out.println(value);
//                    value = value.substring(0, value.length()-2);
//                    value = value.replace("}, ", "☆");
//                    //굳이 별로 바꾼 이유는 split에서 정규식을 사용하기 때문에 이렇게 안 하면 에러가 발생하기 때문
//                    //자세한 것은 이 링크 참고 : https://mytory.net/archives/285
//                    System.out.println(value);
//
//
//                    newValue = value.split("☆");
//                    System.out.println(newValue.length);
//
//
//                    finalValue = new String[newValue.length][3];
//
//                    for(int i = 0; i<newValue.length; i++)
//                    {
//                        finalValue[i][0] = newValue[i].substring(newValue[i].indexOf("userID=")+7, newValue[i].lastIndexOf(", "));
//                        finalValue[i][1] = newValue[i].substring(newValue[i].indexOf("userPW=")+7, newValue[i].indexOf(", "));
//                        finalValue[i][2] = newValue[i].substring(newValue[i].indexOf("value=")+6);
//
//                        System.out.println("가" + finalValue[i][0]);
//                        System.out.println("나" + finalValue[i][1]);
//                        System.out.println("다" + finalValue[i][2]);
//
//                    }
//
//
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error)
//                {
//                    System.out.println("ERROR");
//
//                }
//            });


            sign_in_btn.setOnClickListener(v -> {
                String input_id = id_ET.getText().toString().replaceAll(" ", "");
                String input_pw = pw_ET.getText().toString().replaceAll(" ", "");  // 공백 처리


                boolean idExist = false;
                int id = 5;
                int pw = 3;

                for(int k = 0; k<i; k++)
                {
                    if(finalValue[k][id].equals(input_id)){
                        idExist = true;
                        idIndex = k;
                        break;
                    }

                }



                if (!idExist)
                {   // ID가 존재하지 않을 때
                    id_Layout.setError("'" + input_id + "' is not existed");
                }
                else
                {
                    id_Layout.setErrorEnabled(false);
                    if (finalValue[idIndex][pw].equals(input_pw))
                    {  // 두 String 비교
                        pw_Layout.setErrorEnabled(false);
                        Toast.makeText(LoginActivity.this, "환영합니다!", Toast.LENGTH_SHORT).show();
                        this.preferencesGetLogined.edit().putBoolean("getLogined", true).commit();


                        SharedPreferences.Editor editorID = preferencesID.edit();
                        editorID.putString("id", input_id);
                        editorID.commit();

                        SharedPreferences.Editor editorPW = preferencesPW.edit();
                        editorPW.putString("pw", input_pw);
                        editorPW.commit();

                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myRef = database.getReference("user/"+input_id+"/value");
                        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                SharedPreferences.Editor editorSaveAll = preferencesSaveAll.edit();
                                editorSaveAll.putString("saveAll", snapshot.getValue(String.class));
                                editorSaveAll.commit();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });


                        Intent intent = new Intent(LoginActivity.this, SplashActivity.class);
                        startActivity(intent);
                        finish();



                    }
                    else{
                        pw_Layout.setError("비밀번호가 다릅니다.");
                    }
                }
                if (input_id.length() <= 0)
                    id_Layout.setError("아이디를 입력해주세요.");
                if (input_pw.length() <= 0)
                    pw_Layout.setError("비밀번호를 입력해주세요.");
            });

            // sign up 버튼 누르면 화면 이동
            sign_up_btn.setOnClickListener(v -> {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
                finish();
            });
        }



    }
}