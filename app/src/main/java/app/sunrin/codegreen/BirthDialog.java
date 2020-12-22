package app.sunrin.codegreen;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
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
public class BirthDialog {
    public static String yy,mm,dd;
    private Context context;
    public static boolean check=false;

    public BirthDialog(Context context) {
        this.context = context;
    }

    // 호출할 다이얼로그 함수를 정의한다.
    public void callFunction() {

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
                        yy=Integer.toString(year);
                        mm=Integer.toString(monthOfYear+1);
                        dd=Integer.toString(dayOfMonth);
                        check=true;
                    }
                });
        pickerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //yy, mm, dd를 뭐 넘기던가 해야지
                dlg.dismiss();
            }
        });


        //커스텀 다이얼로그 닫기
    //dlg.dismiss();



        // 커스텀 다이얼로그를 노출한다.
        dlg.show();

    }
}