package app.sunrin.codegreen;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

import java.util.Calendar;

public class BirthDialog extends AppCompatActivity{
    public static String yy,mm,dd;
    private Context context;
    public static boolean check=false;
    int age;

    SharedPreferences preferencesBirth;
    FirebaseDatabase database;
    DatabaseReference reference;

    SharedPreferences preferencesID;


    public BirthDialog(Context context) {
        this.context = context;
    }

    // 호출할 다이얼로그 함수를 정의한다.
    public void callFunction(TextView textYear, TextView textMonth, TextView textDay, TextView textAge) {

        // 커스텀 다이얼로그를 정의하기위해 Dialog클래스를 생성한다.
        final Dialog dlg = new Dialog(context);

        // 액티비티의 타이틀바를 숨긴다.
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // 커스텀 다이얼로그의 레이아웃을 설정한다.
        dlg.setContentView(R.layout.dialog_birth);
        WindowManager.LayoutParams params = dlg.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dlg.getWindow().setAttributes(params);

        Button pickerBtn = dlg.findViewById(R.id.pickerBtn);
        DatePicker datepicker = dlg.findViewById(R.id.datepicker);

        datepicker.init(datepicker.getYear(), datepicker.getMonth(), datepicker.getDayOfMonth(),

                new DatePicker.OnDateChangedListener() {

                    @Override
                    public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        age = getAge(year, monthOfYear, dayOfMonth);
                        yy=Integer.toString(year);
                        if(monthOfYear+1<10)
                        {
                            mm=Integer.toString(monthOfYear+1);
                            mm = "0" + mm;
                        }
                        else
                        {
                            mm=Integer.toString(monthOfYear+1);
                        }

                        if(dayOfMonth<10)
                        {
                            dd=Integer.toString(dayOfMonth);
                            dd = "0"+dd;
                        }
                        else
                        {
                            dd=Integer.toString(dayOfMonth);
                        }


                        check=true;
                    }
                });
        pickerBtn.setOnClickListener(view -> {
            textYear.setText(yy);
            textMonth.setText(mm);
            textDay.setText(dd);
            textAge.setText(Integer.toString(age));

            //이거 왜 안되는지 모르겠어서 SplashActivity로 이동해서 거기서 처리할 예정

//             preferencesBirth = view.getContext().getSharedPreferences("birth", 0);
//            preferencesBirth.edit().putInt("year", Integer.parseInt(yy));
//            preferencesBirth.edit().putInt("month", Integer.parseInt(mm));
//            System.out.println(Integer.parseInt(mm));
//            preferencesBirth.edit().putInt("day", Integer.parseInt(dd));
//            preferencesBirth.edit().putInt("age", age);
//            preferencesBirth.edit().apply();

            preferencesID = view.getContext().getSharedPreferences("ID", 0);
            String id = preferencesID.getString("id", "");

            database = FirebaseDatabase.getInstance();
            reference = database.getReference("user/" + id + "/userAge" );
            reference.setValue(age);

            reference = database.getReference("user/" + id + "/userYear" );
            reference.setValue(Integer.parseInt(yy));

            reference = database.getReference("user/" + id + "/userMonth" );
            reference.setValue(Integer.parseInt(mm));

            reference = database.getReference("user/" + id + "/userDay" );
            reference.setValue(Integer.parseInt(dd));


            Toast.makeText(context, "생년월일이 성공적으로 변경되었습니다!", Toast.LENGTH_SHORT).show();

            dlg.dismiss();
        });


        //커스텀 다이얼로그 닫기
    //dlg.dismiss();



        // 커스텀 다이얼로그를 노출한다.
        dlg.show();

    }


    public int getAge(int birthYear, int birthMonth, int birthDay)
    {
        Calendar current = Calendar.getInstance();
        int currentYear  = current.get(Calendar.YEAR);
        int currentMonth = current.get(Calendar.MONTH) + 1;
        int currentDay   = current.get(Calendar.DAY_OF_MONTH);

        int age = currentYear - birthYear;
        // 생일 안 지난 경우 -1
        if (birthMonth * 100 + birthDay > currentMonth * 100 + currentDay)
            age--;

        return age;
    }
}