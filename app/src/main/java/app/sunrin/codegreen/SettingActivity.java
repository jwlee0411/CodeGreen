package app.sunrin.codegreen;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SettingActivity extends AppCompatActivity{

    SharedPreferences preferences;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

       preferences = getSharedPreferences("getLogined", 0);

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
            Intent picker = new Intent(getApplicationContext(), BirthDialog.class);
            startActivityForResult(picker,1000);

        });

        Button buttonChangePassword = findViewById(R.id.buttonChangePassword);
        buttonChangePassword.setOnClickListener(v -> {

        });


        Button buttonChangeSex = findViewById(R.id.buttonChangeSex);
        buttonChangeSex.setOnClickListener(v -> {

        });

        Button buttonQuit = findViewById(R.id.buttonQuit);
        buttonQuit.setOnClickListener(v -> {
            show();

        });


    }

    private void show()
    {
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
    }
}