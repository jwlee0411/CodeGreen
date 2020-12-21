package app.sunrin.codegreen;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class JuminActivity extends AppCompatActivity {

    String value;
    String[] newValue;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextView textViewJumin = findViewById(R.id.textViewJumin);
        Button buttonLocationJumin = findViewById(R.id.buttonLocationJumin);
        Button buttonContactJumin = findViewById(R.id.buttonContactJumin);

        SharedPreferences preferences = getSharedPreferences("location", 0);
        String location = preferences.getString("location", "");
        System.out.println(location);
        location = location.substring(location.indexOf("구 ")+2, location.indexOf("동")+1);
        System.out.println(location);
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("center/"+location);

        String finalLocation = location;
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                value = snapshot.getValue(String.class);
                System.out.println(value);

                if(value==null)
                {
                    Toast.makeText(JuminActivity.this, "주민센터 정보가 없습니다!", Toast.LENGTH_LONG).show();
                }
                else
                {
                    newValue = value.split("★");

                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("geo:0,0?q=" + finalLocation + "주민센터"));
                    startActivity(intent);
                }




            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
