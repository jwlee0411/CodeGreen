package app.sunrin.codegreen;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SaveDialog extends AppCompatActivity {

    private Context context;

    Button buttonConfirm;

    String recycle = "";
    DatabaseReference myRef;


    CheckBox[] checkboxes = new CheckBox[9];

    public SaveDialog(Context context) {

        this.context = context;
    }

    public void callFunction(String KANcode)
    {
        final Dialog dlg = new Dialog(context);

        // 액티비티의 타이틀바를 숨긴다.
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // 커스텀 다이얼로그의 레이아웃을 설정한다.
        dlg.setContentView(R.layout.dialog_save);
        WindowManager.LayoutParams params = dlg.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dlg.getWindow().setAttributes(params);


        if(KANcode==null)
        {
            Toast.makeText(context, "재활용 정보를 등록할 수 없는 상품입니다.", Toast.LENGTH_SHORT).show();
            dlg.dismiss();
        }
        else if(KANcode.equals("") || KANcode.equals("0000000"))
        {
            Toast.makeText(context, "재활용 정보를 등록할 수 없는 상품입니다.", Toast.LENGTH_SHORT).show();
            dlg.dismiss();
        }
        else
        {
            checkboxes[0] = dlg.findViewById(R.id.checkRecycle11);
            checkboxes[1] = dlg.findViewById(R.id.checkRecycle21);
            checkboxes[2] = dlg.findViewById(R.id.checkRecycle31);
            checkboxes[3] = dlg.findViewById(R.id.checkRecycle41);
            checkboxes[4] = dlg.findViewById(R.id.checkRecycle51);
            checkboxes[5] = dlg.findViewById(R.id.checkRecycle61);
            checkboxes[6] = dlg.findViewById(R.id.checkRecycle71);
            checkboxes[7] = dlg.findViewById(R.id.checkRecycle81);
            checkboxes[8] = dlg.findViewById(R.id.checkRecycle91);


            buttonConfirm = dlg.findViewById(R.id.buttonConfirm2);

            buttonConfirm.setOnClickListener(v -> {

                String recycle = "";
                for(int i = 0; i<9; i++)
                {
                    if(checkboxes[i].isChecked())
                    {
                        recycle = recycle + (i+1) + ",";
                    }
                }


                if(recycle.length()<1)
                {
                    Toast.makeText(context, "항목을 선택해주세요!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    recycle = recycle.substring(0, recycle.length()-1);
                    System.out.println(recycle);
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference reference = database.getReference(KANcode);
                    reference.setValue(recycle);
                    Toast.makeText(context, "등록되었습니다.", Toast.LENGTH_SHORT).show();

                    dlg.dismiss();

                }






            });

            dlg.show();

        }



    }
}