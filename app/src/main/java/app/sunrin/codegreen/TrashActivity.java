package app.sunrin.codegreen;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
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
    int getValue_count = 0;
    int viewCount = 21;
    int trashLocationCount = 2725;
    String[] path = new String[viewCount];
    String[] locationSet;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("검색 중...");
        progressDialog.setMessage("검색 중입니다. 잠시만 기다려 주세요.");
        progressDialog.setCancelable(false);
        progressDialog.show();

        TextView textSiDo = findViewById(R.id.textSiDo);
        TextView textSiGunGu = findViewById(R.id.textSiGunGu);
        Button buttonLocationJumin = findViewById(R.id.buttonLocationJumin);

        SharedPreferences preferencesLocation = getSharedPreferences("location", 0);

        locationSet = preferencesLocation.getString("location", "").split(" ");
        textSiDo.setText(locationSet[1]);
        textSiGunGu.setText(locationSet[2]);


        buttonLocationJumin.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setData(Uri.parse("geo:0,0?q=" + locationSet[2] + "주민센터"));
            startActivity(intent);
        });




        FirebaseDatabase database = FirebaseDatabase.getInstance();
        for(int i = 1; i<trashLocationCount; i++)
        {

            reference = database.getReference("trash/records/" + i + "/시군구명");
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String value = snapshot.getValue(String.class);
                    if(value.contains(locationSet[1]) || value.contains(locationSet[2]))//TODO
                    {
                        getValue = true;
                        getValue_count++;
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            if(getValue)
            {

                ItemTrash itemTrash = new ItemTrash();

                temp = 0;

                path[0] = "trash/records/" + i + "/관리구역대상지역명";
                path[1] = "trash/records/" + i + "/관리부서전화번호";
                path[2] = "trash/records/" + i + "/데이터기준일자";
                path[3] = "trash/records/" + i + "/미수거일";
                path[4] = "trash/records/" + i + "/배출장소";
                path[5] = "trash/records/" + i + "/생활쓰레기배출방법";
                path[6] = "trash/records/" + i + "/생활쓰레기배출시작시각";
                path[7] = "trash/records/" + i + "/생활쓰레기배출요일";
                path[8] = "trash/records/" + i + "/생활쓰레기배출종료시각";

                path[9] = "trash/records/" + i + "/음식물쓰레기배출방법";
                path[10] = "trash/records/" + i + "/음식물쓰레기배출시작시각";
                path[11] = "trash/records/" + i + "/음식물쓰레기배출요일";
                path[12] = "trash/records/" + i + "/음식물쓰레기배출종료시각";

                path[13] = "trash/records/" + i + "/일시적다량폐기물배출방법";
                path[14] = "trash/records/" + i + "/일시적다량폐기물배출시작시각";
                path[15] = "trash/records/" + i + "/일시적다량폐기물배출요일";
                path[16] = "trash/records/" + i + "/일시적다량폐기물배출종료시각";

                path[17] = "trash/records/" + i + "/재활용품배출방법";
                path[18] = "trash/records/" + i + "/재활용품배출시작시간";
                path[19] = "trash/records/" + i + "/재활용품배출요일";
                path[20] = "trash/records/" + i + "/재활용품배출종료시각";


                for(int k = 0; k<viewCount; k++)
                {
                    reference = database.getReference(path[k]);
                    int finalK = k;
                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String value = snapshot.getValue(String.class);
                            itemTrash.setRecycle(value, finalK);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }


                data.add(itemTrash);
                getValue = false;
            }
        }


        progressDialog.dismiss();

        if(getValue_count==0)
        {
            Toast.makeText(this, "재활용 관련 정보가 없습니다!", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(this, SplashActivity.class);
            startActivity(intent);
            finish();
        }
        else
        {
            recyclerView = findViewById(R.id.recyclerView);
            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            ReAdapterTrash reAdapterTrash = new ReAdapterTrash(data);
            recyclerView.setAdapter(reAdapterTrash);
        }

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
