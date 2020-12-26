package app.sunrin.codegreen;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class TrashActivity extends AppCompatActivity {
    ArrayList<ItemTrash> data = new ArrayList<>();
    boolean getValue = false;
    DatabaseReference reference;
    RecyclerView recyclerView;
    int temp;
    boolean getValue_count = false;
    int viewCount = 21;
    int trashLocationCount = 2723;
    String[] path = new String[viewCount];
    String[] locationSet;
    ProgressDialog progressDialog;

    boolean getOutLoop = false;
    int firebaseCount = 0;


    int i = 1;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trash);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("검색 중...");
        progressDialog.setMessage("검색 중입니다. 잠시만 기다려 주세요.");
        progressDialog.setCancelable(false);
        progressDialog.show();

        ItemTrash itemTrash = new ItemTrash();

        TextView textSiDo = findViewById(R.id.textSiDo);
        TextView textSiGunGu = findViewById(R.id.textSiGunGu);
        Button buttonLocationJumin = findViewById(R.id.buttonLocationJumin);

        SharedPreferences preferencesLocation = getSharedPreferences("location", 0);

        locationSet = preferencesLocation.getString("location", "").split(" ");
        System.out.println(locationSet[0]);
        textSiDo.setText(locationSet[1]);
        textSiGunGu.setText(locationSet[2]);


        buttonLocationJumin.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            if(locationSet[3].contains("동"))
            {
                intent.setData(Uri.parse("geo:0,0?q=" + locationSet[3] + " 주민센터"));
                startActivity(intent);
            }
            else
            {
                intent.setData(Uri.parse("geo:0,0?q=" + locationSet[4] + " 주민센터"));
                startActivity(intent);
            }

        });




        FirebaseDatabase database = FirebaseDatabase.getInstance();
        for(i = 1; i<trashLocationCount; i++)
        {


            int tempVal = i;


            reference = database.getReference("trash/records/" + i + "/시군구명");
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String value = snapshot.getValue(String.class);
                    System.out.println("$" + value);


                    //System.out.println(value);
                    System.out.print(value.equals(locationSet[1]) || value.equals(locationSet[2]) || value.equals(locationSet[3]));
                    if(value.equals(locationSet[1]) || value.equals(locationSet[2]) || value.equals(locationSet[3]))
                    {


                            System.out.println("이거 된거임");
                            getValue_count = true;

                            temp = 0;

                            path[0] = "trash/records/" + tempVal + "/관리구역대상지역명";
                            path[1] = "trash/records/" + tempVal + "/관리부서전화번호";
                            path[2] = "trash/records/" + tempVal + "/데이터기준일자";
                            path[3] = "trash/records/" + tempVal + "/미수거일";
                            path[4] = "trash/records/" + tempVal + "/배출장소";
                            path[5] = "trash/records/" + tempVal + "/생활쓰레기배출방법";
                            path[6] = "trash/records/" + tempVal + "/생활쓰레기배출시작시각";
                            path[7] = "trash/records/" + tempVal + "/생활쓰레기배출요일";
                            path[8] = "trash/records/" + tempVal + "/생활쓰레기배출종료시각";

                            path[9] = "trash/records/" + tempVal + "/음식물쓰레기배출방법";
                            path[10] = "trash/records/" + tempVal + "/음식물쓰레기배출시작시각";
                            path[11] = "trash/records/" + tempVal + "/음식물쓰레기배출요일";
                            path[12] = "trash/records/" + tempVal + "/음식물쓰레기배출종료시각";

                            path[13] = "trash/records/" + tempVal + "/일시적다량폐기물배출방법";
                            path[14] = "trash/records/" + tempVal + "/일시적다량폐기물배출시작시각";
                            path[15] = "trash/records/" + tempVal + "/일시적다량폐기물배출요일";
                            path[16] = "trash/records/" + tempVal + "/일시적다량폐기물배출종료시각";

                            path[17] = "trash/records/" + tempVal + "/재활용품배출방법";
                            path[18] = "trash/records/" + tempVal + "/재활용품배출시작시간";
                            path[19] = "trash/records/" + tempVal + "/재활용품배출요일";
                            path[20] = "trash/records/" + tempVal + "/재활용품배출종료시각";



                            reference = database.getReference(path[0]);
                            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    String value = snapshot.getValue(String.class);
                                    if(value==null)
                                    {

                                    }
                                    else if(value.equals("없음"))
                                    {

                                    }
                                    else
                                    {
                                        firebaseCount++;

                                        itemTrash.setRecycle(value, 0);

                                        for(int k =  1; k<viewCount; k++)
                                        {
                                            reference = database.getReference(path[k]);
                                            System.out.println("$" + path[k]);
                                            int finalK = k;
                                            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                    String value = snapshot.getValue(String.class);
                                                    System.out.println("$" + value);
                                                    itemTrash.setRecycle(value, finalK);
                                                }

                                                @Override
                                                public void onCancelled(@NonNull DatabaseError error) {

                                                }
                                            });




                                        }

                                        data.add(itemTrash);
                                        System.out.println(data.toString());


                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });






                        }





                    if(trashLocationCount-1==tempVal)
                    {

                        checkView();
                        System.out.println("ㅋㅋㅋ"+ firebaseCount);

                    }




                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });



        }








    }

    private void checkView()
    {



            Handler mHandler = new Handler();
            mHandler.postDelayed(() -> {
                if(firebaseCount>0)
                {
                    recyclerView = findViewById(R.id.recyclerView);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    ReAdapterTrash reAdapterTrash = new ReAdapterTrash(data);
                    recyclerView.setAdapter(reAdapterTrash);
                    progressDialog.dismiss();
                }
                else
                {
                    progressDialog.dismiss();
                    Toast.makeText(this, "ㄴㄴ", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(TrashActivity.this, SplashActivity.class);
                    startActivity(intent);
                    finish();

                }


            }, 1000);

    }

    private class ProgressDialog extends AlertDialog {

        public ProgressDialog(@NonNull Context context) {
            super(context);
        }

        public ProgressDialog(@NonNull Context context, int themeResId) {
            super(context, themeResId);
        }

        protected ProgressDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
            super(context, cancelable, cancelListener);
        }
    }
}
