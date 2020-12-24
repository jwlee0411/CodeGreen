package app.sunrin.codegreen;


import android.app.Dialog;
import android.content.Context;
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

public class AddDialog extends AppCompatActivity {

    private Context context;
    TextInputLayout textProductCategory, textBarcodeNumber, textProductName;
    TextInputEditText editProductCategory, editBarcodeNumber, editProductName;
    Button buttonConfirm;

    String recycle = "";
    DatabaseReference myRef;


    CheckBox[] checkboxes = new CheckBox[9];

    public AddDialog(Context context) {

        this.context = context;
    }

    public void callFunction()
    {
        final Dialog dlg = new Dialog(context);

        // 액티비티의 타이틀바를 숨긴다.
        dlg.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // 커스텀 다이얼로그의 레이아웃을 설정한다.
        dlg.setContentView(R.layout.dialog_add);
        WindowManager.LayoutParams params = dlg.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dlg.getWindow().setAttributes(params);

        textProductCategory = dlg.findViewById(R.id.textProductCategory);
        textBarcodeNumber = dlg.findViewById(R.id.textBarcodeNumber);
        textProductName = dlg.findViewById(R.id.textProductName);

        editProductCategory = dlg.findViewById(R.id.editProductCategory);
        editBarcodeNumber = dlg.findViewById(R.id.editBarcodeNumber);
        editProductName = dlg.findViewById(R.id.editProductName);

        buttonConfirm = dlg.findViewById(R.id.buttonConfirm);

        checkboxes[0] = dlg.findViewById(R.id.checkRecycle1);
        checkboxes[1] = dlg.findViewById(R.id.checkRecycle2);
        checkboxes[2] = dlg.findViewById(R.id.checkRecycle3);
        checkboxes[3] = dlg.findViewById(R.id.checkRecycle4);
        checkboxes[4] = dlg.findViewById(R.id.checkRecycle5);
        checkboxes[5] = dlg.findViewById(R.id.checkRecycle6);
        checkboxes[6] = dlg.findViewById(R.id.checkRecycle7);
        checkboxes[7] = dlg.findViewById(R.id.checkRecycle8);
        checkboxes[8] = dlg.findViewById(R.id.checkRecycle9);



        buttonConfirm.setOnClickListener(v -> {
            if(editProductName.getText().toString().length()<=0)
            {
                textProductName.setError("상품명을 입력하세요.");
            }
            if(editBarcodeNumber.getText().toString().length()<=0)
            {
                textBarcodeNumber.setError("바코드 번호를 입력하세요.");
            }
            if(editProductCategory.getText().toString().length()<=0) {
                textProductCategory.setError("상품 카테고리를 입력하세요.");
            }
            if(editProductCategory.getText().toString().length()>0&&editBarcodeNumber.getText().toString().length()>0&&editProductName.getText().toString().length()>0)
            {
                FirebaseDatabase database = FirebaseDatabase.getInstance();



                myRef = database.getReference("product/" + editBarcodeNumber.getText().toString() + "/name");
                myRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String value = snapshot.getValue(String.class);
                        if(value==null)
                        {


                            for(int i = 0; i<9; i++)
                            {
                                if(checkboxes[i].isChecked())
                                {
                                    recycle = recycle + (i + 1) + ",";
                                }
                            }

                            if(recycle.equals(""))
                            {
                                Toast.makeText(context, "재활용 정보를 등록해주세요.", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {

                                myRef = database.getReference("product/"+ editBarcodeNumber.getText().toString() + "/category");
                                myRef.setValue(editProductCategory.getText().toString());

                                myRef = database.getReference("product/"+ editBarcodeNumber.getText().toString() + "/name");
                                myRef.setValue(editProductName.getText().toString());

                                myRef = database.getReference("product/"+ editBarcodeNumber.getText().toString() + "/recycle");
                                myRef.setValue(recycle.substring(0, recycle.length()-1));
                                Toast.makeText(context, "등록되었습니다.", Toast.LENGTH_SHORT).show();
                                dlg.dismiss();
                            }


                        }
                        else
                        {
                            dlg.dismiss();
                            Toast.makeText(context, "이미 등록된 상품입니다.", Toast.LENGTH_SHORT).show();
                        }
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });



            }





        });

        dlg.show();



    }
}