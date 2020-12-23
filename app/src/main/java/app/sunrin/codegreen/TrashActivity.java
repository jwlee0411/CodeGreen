package app.sunrin.codegreen;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TrashActivity extends AppCompatActivity {

    boolean getValue = false;
    DatabaseReference reference;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        for(int i = 0; i<2500; i++)
        {

            reference = database.getReference("trash/records/" + i + "/시군구명");
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    String value = snapshot.getValue(String.class);
                    if(value.equals(""))
                    {
                        //TODO
                        getValue = true;
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            //TODO 개노가다, RecyclerView 만들 예정임

//            if(getValue)
//            {
//                getValue = false;
//
//                reference = database.getReference("trash/records/" + i + "/관리구역대상지역명");
//                reference.addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        TextView textView = findViewById(R.id.);
//                        String value = snapshot.getValue(String.class);
//                        textView.setText(value);
//
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
//
//
//                reference = database.getReference("trash/records/" + i + "/관리부서전화번호");
//                reference.addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        TextView textView = findViewById(R.id.);
//                        String value = snapshot.getValue(String.class);
//                        textView.setText(value);
//
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
//                reference = database.getReference("trash/records/" + i + "/데이터기준일자");
//                reference.addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        TextView textView = findViewById(R.id.);
//                        String value = snapshot.getValue(String.class);
//                        textView.setText(value);
//
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
//                reference = database.getReference("trash/records/" + i + "/미수거일");
//                reference.addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        TextView textView = findViewById(R.id.);
//                        String value = snapshot.getValue(String.class);
//                        textView.setText(value);
//
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
//            }
        }


    }
}
