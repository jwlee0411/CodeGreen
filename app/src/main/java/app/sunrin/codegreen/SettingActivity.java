package app.sunrin.codegreen;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.Calendar;

public class SettingActivity extends AppCompatActivity{

    SharedPreferences preferences;
    SharedPreferences preferencesBirth;
    SharedPreferences preferencesID;
    SharedPreferences preferencesPW;
    SharedPreferences preferencesSex;

    TextView textYear;
    TextView textMonth;
    TextView textDay;
    TextView textAge;

    SharedPreferences.Editor editPW;

    String id, pw;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        preferencesBirth = getSharedPreferences("birth", 0);
        preferencesID = getSharedPreferences("ID", 0);
        preferencesSex = getSharedPreferences("sex", 0);
        preferencesPW = getSharedPreferences("PW", 0);


        textYear = findViewById(R.id.textYear);
        textMonth = findViewById(R.id.textMonth);
        textDay = findViewById(R.id.textDay);
        textAge = findViewById(R.id.textAge);

       preferences = getSharedPreferences("getLogined", 0);

       id = preferencesID.getString("id", "");
       pw = preferencesPW.getString("pw", "");
       editPW = preferencesPW.edit();
       System.out.println(id);

       TextView myID = findViewById(R.id.textView3); //TODO : 레이아웃 파일 받으면 id 바꾸기
       myID.setText(id);

        Button buttonLogout = findViewById(R.id.buttonNewLogout);
        buttonLogout.setOnClickListener(v -> {



                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("로그아웃 하시겠습니까?");
                    builder.setMessage("로그아웃을 하게 되면 다시 로그인을 하셔야 합니다.");
                    builder.setPositiveButton("예",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(getApplicationContext(),"로그아웃 되었습니다.",Toast.LENGTH_LONG).show();
                                    preferences.edit().putBoolean("getLogined", false).commit();
                                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);


                                }
                            });
                    builder.setNegativeButton("아니오",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            });
                    builder.show();

        });

        Button buttonChangeBirth = findViewById(R.id.buttonChangeBirth);
        buttonChangeBirth.setOnClickListener(v -> {
            BirthDialog birthDialog = new BirthDialog(SettingActivity.this);

            birthDialog.callFunction(textYear, textMonth, textDay, textAge);

        });

        Button buttonChangePassword = findViewById(R.id.buttonChangePassword);
        buttonChangePassword.setOnClickListener(v -> {
            TextInputLayout textInputLayout = findViewById(R.id.TextInputChangePassword);

            TextInputEditText textInputEditText = findViewById(R.id.editPassword);

            String input_pw = textInputEditText.getText().toString().replace(" ", "");

            if (input_pw.length() >= 8) {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("user/" + id + "/userPW");
                myRef.setValue(input_pw);
                editPW.putString("pw", input_pw).commit();
                Toast.makeText(SettingActivity.this, "비밀번호가 성공적으로 변경되었습니다.", Toast.LENGTH_SHORT).show();

            }
            else
            {
               //Toast.makeText(this, "비밀번호는 8자 이상입니다.", Toast.LENGTH_SHORT).show();
                textInputLayout.setError("비밀번호는 8자 이상입니다.");
            }

        });


        RadioGroup radioGroup = findViewById(R.id.radioGroupSetting);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {


                if(checkedId==R.id.radioButtonSetting)
                {
                    preferencesSex.edit().putBoolean("sex", true).commit();
                    FirebaseDatabase.getInstance().getReference("user/"+id+"/userSex").setValue(true);
                    Toast.makeText(SettingActivity.this, "성별이 성공적으로 변경되었습니다.", Toast.LENGTH_SHORT).show();
                }
                else if(checkedId==R.id.radioButtonSetting2)
                {
                    preferencesSex.edit().putBoolean("sex", false).commit();
                    FirebaseDatabase.getInstance().getReference("user/"+id+"/userSex").setValue(false);
                    Toast.makeText(SettingActivity.this, "성별이 성공적으로 변경되었습니다.", Toast.LENGTH_SHORT).show();
                }


            }
        });

        Button buttonQuit = findViewById(R.id.buttonQuit);
        buttonQuit.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("정말로 탈퇴하시겠습니까?");
            builder.setMessage("탈퇴 시 모든 개인정보가 삭제되며, 이 작업은 되돌릴 수 없습니다.");
            builder.setPositiveButton("예",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            preferences.edit().putBoolean("getLogined", false).commit();
                            Toast.makeText(getApplicationContext(),"회원 탈퇴가 완료되었습니다.",Toast.LENGTH_LONG).show();



                            SharedPreferences preferences = getSharedPreferences("ID", 0);
                            String id = preferences.getString("id", "");


                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference myRef = database.getReference("user/"+id);
                            myRef.removeValue();

                            getSharedPreferences("ID", 0).edit().clear();
                            getSharedPreferences("PW", 0).edit().clear();

                            SharedPreferences dataSave = getSharedPreferences("saveAll", 0);
                            dataSave.edit().clear();



                            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);

                        }
                    });
            builder.setNegativeButton("아니오",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
            builder.show();

        });


    }








}