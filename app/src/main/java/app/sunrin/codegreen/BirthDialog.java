package app.sunrin.codegreen;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class BirthDialog extends AppCompatActivity {
    DatePicker datepicker;
    Button pickerBtn;
    public static String yy,mm,dd;
    public static boolean check=false;

    private Context context;

    public BirthDialog(Context context)
    {
        this.context = context;
    }

//    public void callFunction(final TextView)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_birth);
        pickerBtn = findViewById(R.id.pickerBtn);
        datepicker = findViewById(R.id.datepicker);

        datepicker.init(datepicker.getYear(), datepicker.getMonth(), datepicker.getDayOfMonth(),

                new DatePicker.OnDateChangedListener() {

                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        yy=Integer.toString(year);
                        mm=Integer.toString(monthOfYear+1);
                        dd=Integer.toString(dayOfMonth);
                        check=true;
                    }
                });
        pickerBtn.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            intent.putExtra("yy",yy);
            intent.putExtra("mm",mm);
            intent.putExtra("dd",dd);
            startActivityForResult(intent,1000);
        });
    }
}
