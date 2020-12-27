package app.sunrin.codegreen;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.Interpreter;
import org.tensorflow.lite.support.common.FileUtil;
import org.tensorflow.lite.support.common.TensorOperator;
import org.tensorflow.lite.support.common.TensorProcessor;
import org.tensorflow.lite.support.common.ops.NormalizeOp;
import org.tensorflow.lite.support.image.ImageProcessor;
import org.tensorflow.lite.support.image.TensorImage;
import org.tensorflow.lite.support.image.ops.ResizeOp;
import org.tensorflow.lite.support.image.ops.ResizeWithCropOrPadOp;
import org.tensorflow.lite.support.label.TensorLabel;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class NewScannerActivity extends AppCompatActivity {

    protected Interpreter tflite;
    private MappedByteBuffer tfliteModel;
    private TensorImage inputImageBuffer;
    private  int imageSizeX;
    private  int imageSizeY;
    private TensorBuffer outputProbabilityBuffer;
    private TensorProcessor probabilityProcessor;
    private static final float IMAGE_MEAN = 0.0f;
    private static final float IMAGE_STD = 1.0f;
    private static final float PROBABILITY_MEAN = 0.0f;
    private static final float PROBABILITY_STD = 255.0f;
    private Bitmap bitmap;
    private List<String> labels;
    ImageView imageView;
    Uri imageuri;
    Button buclassify;
    TextView classitext;
    String saveData = "";
    ArrayList<Item> data = new ArrayList<>(); //분리배출 방법의 recyclerView에 넣는 arraylist

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_scanner);
        imageView=(ImageView)findViewById(R.id.img);
        buclassify=(Button)findViewById(R.id.classify);
        classitext=(TextView)findViewById(R.id.classifytext);

        imageView.setOnClickListener(v -> {
            data = new ArrayList<>();
            saveData = "";
            Intent intent=new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(Intent.createChooser(intent,"Select Picture"),12);
        });

        try{
            tflite=new Interpreter(loadmodelfile(this));
        }catch (Exception e) {
            e.printStackTrace();
        }

        buclassify.setOnClickListener(v -> {



        });



    }

    private TensorImage loadImage(final Bitmap bitmap) {
        // Loads bitmap into a TensorImage.

        try {
            inputImageBuffer.load(bitmap);
        }
        catch (NullPointerException e)
        {
            Toast.makeText(this, "이미지를 등록해주세요!", Toast.LENGTH_LONG).show();
        }


        // Creates processor for the TensorImage.
        int cropSize = Math.min(bitmap.getWidth(), bitmap.getHeight());
        // TODO(b/143564309): Fuse ops inside ImageProcessor.
        ImageProcessor imageProcessor =
                new ImageProcessor.Builder()
                        .add(new ResizeWithCropOrPadOp(cropSize, cropSize))
                        .add(new ResizeOp(imageSizeX, imageSizeY, ResizeOp.ResizeMethod.NEAREST_NEIGHBOR))
                        .add(getPreprocessNormalizeOp())
                        .build();
        return imageProcessor.process(inputImageBuffer);
    }

    private MappedByteBuffer loadmodelfile(Activity activity) throws IOException {
        AssetFileDescriptor fileDescriptor=activity.getAssets().openFd("newmodel.tflite");
        FileInputStream inputStream=new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel=inputStream.getChannel();
        long startoffset = fileDescriptor.getStartOffset();
        long declaredLength=fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY,startoffset,declaredLength);
    }

    private TensorOperator getPreprocessNormalizeOp() {
        return new NormalizeOp(IMAGE_MEAN, IMAGE_STD);
    }
    private TensorOperator getPostprocessNormalizeOp(){
        return new NormalizeOp(PROBABILITY_MEAN, PROBABILITY_STD);
    }

    private void showresult(){

        try{
            labels = FileUtil.loadLabels(this,"newdict.txt");
        }catch (Exception e){
            e.printStackTrace();
        }
        Map<String, Float> labeledProbability =
                new TensorLabel(labels, probabilityProcessor.process(outputProbabilityBuffer))
                        .getMapWithFloatValue();
        float maxValueInMap =(Collections.max(labeledProbability.values()));

        for (Map.Entry<String, Float> entry : labeledProbability.entrySet()) {
            if (entry.getValue()==maxValueInMap) {
                classitext.setText(entry.getKey());
                setRecycle();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==12 && resultCode==RESULT_OK && data!=null) {
            imageuri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageuri);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }


            int imageTensorIndex = 0;
            int[] imageShape = tflite.getInputTensor(imageTensorIndex).shape(); // {1, height, width, 3}
            imageSizeY = imageShape[1];
            imageSizeX = imageShape[2];
            DataType imageDataType = tflite.getInputTensor(imageTensorIndex).dataType();

            int probabilityTensorIndex = 0;
            int[] probabilityShape =
                    tflite.getOutputTensor(probabilityTensorIndex).shape(); // {1, NUM_CLASSES}
            DataType probabilityDataType = tflite.getOutputTensor(probabilityTensorIndex).dataType();

            inputImageBuffer = new TensorImage(imageDataType);
            outputProbabilityBuffer = TensorBuffer.createFixedSize(probabilityShape, probabilityDataType);
            probabilityProcessor = new TensorProcessor.Builder().add(getPostprocessNormalizeOp()).build();

            if(bitmap==null)
            {
                Toast.makeText(this, "이미지를 등록해주세요!", Toast.LENGTH_LONG).show();
            }
            else
            {
                inputImageBuffer = loadImage(bitmap);
                tflite.run(inputImageBuffer.getBuffer(),outputProbabilityBuffer.getBuffer().rewind());

                showresult();
            }




        }
    }

    private void setRecycle()
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference reference = database.getReference("ai/" + classitext.getText().toString());
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String value = snapshot.getValue(String.class);
                if(value==null)
                {
                    Toast.makeText(NewScannerActivity.this, "재활용 정보가 없습니다!", Toast.LENGTH_SHORT).show();
                    RecyclerView recyclerView = findViewById(R.id.recycle_new);
                    recyclerView.setVisibility(View.INVISIBLE);

                    FloatingActionButton floatingActionButton = findViewById(R.id.floatingActionButton2);
                    floatingActionButton.setVisibility(View.INVISIBLE);
                }
                else
                {



                    if(value.contains("1"))
                    {
                        addPlastic();
                        saveData = saveData + "1-";
                    }
                    if(value.contains("2"))
                    {
                        addPaper();
                        saveData = saveData + "2-";
                    }
                    if(value.contains("3"))
                    {
                        addVinyl();
                        saveData = saveData + "3-";
                    }
                    if(value.contains("4"))
                    {
                        addCan();
                        saveData = saveData + "4-";
                    }
                    if(value.contains("5"))
                    {
                        addStrph();
                        saveData = saveData + "5-";
                    }
                    if(value.contains("6"))
                    {
                        addPet();
                        saveData = saveData + "6-";
                    }
                    if(value.contains("7"))
                    {
                        addGlass();
                        saveData = saveData + "7-";
                    }
                    if(value.contains("8"))
                    {
                        addTrash();
                        saveData = saveData + "8-";
                    }
                    if(value.contains("9"))
                    {
                        addElec();
                        saveData = saveData + "9-";
                    }


                    FloatingActionButton floatingActionButton = findViewById(R.id.floatingActionButton2);
                    floatingActionButton.setVisibility(View.VISIBLE);
                    floatingActionButton.setOnClickListener(v -> {
                        SharedPreferences preferencesSaveAll = getSharedPreferences("saveAll", 0);
                        String saveData = preferencesSaveAll.getString("saveAll", "");
                        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                        Calendar calendar = Calendar.getInstance();
                        String currentDate = format.format(calendar.getTime());

                        saveData = saveData + "★@" + classitext.getText().toString() + "@@" + currentDate  + "@@" + NewScannerActivity.this.saveData;



                        SharedPreferences sharedPreferences = getSharedPreferences("ID", 0);

                        String user_id = sharedPreferences.getString("id", "");

                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        DatabaseReference myRef = database.getReference("user/" + user_id + "/value");
                        myRef.setValue(saveData);

                        SharedPreferences.Editor editor = preferencesSaveAll.edit();
                        editor.putString("saveAll", saveData).commit();


                        Toast.makeText(getApplicationContext(), "저장되었습니다.", Toast.LENGTH_LONG).show();
                        floatingActionButton.setVisibility(View.INVISIBLE);

                    });



                    RecyclerView recyclerView = findViewById(R.id.recycle_new);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    ReAdapter reAdapter = new ReAdapter(data);
                    recyclerView.setAdapter(reAdapter);
                    recyclerView.setVisibility(View.VISIBLE);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
    public Item addItem(Drawable img, String title, String way) {
        Item item = new Item();
        item.setRecycle_img(img);
        item.setRecycle_class(title);
        item.setRecycle_how(way);
        return item;
    }

    // data arraylist에 데이터를 넣는 함수
    //플라스틱 1,종이 2,비닐 3,캔 4,스티로폼 5,페트병 6,유리 7,일반쓰레기 8, 전자제품 9
    public void addPlastic() {
        data.add(addItem(getResources().getDrawable(R.drawable.pp), "플라스틱", "플라스틱의 이물질을 제거 후 버려주세요."));
        saveData = saveData + "1-";
    }

    public void addPaper() {
        data.add(addItem(getResources().getDrawable(R.drawable.paper), "종이", "종이에 붙어 있는 테이프 등 종이가 아닌 재질을 제거 후 버려주세요."));
        saveData = saveData + "2-";
    }

    public void addVinyl() {
        data.add(addItem(getResources().getDrawable(R.drawable.vinyl), "비닐", "비닐의 이물질을 제거 후 버려주세요"));
        saveData = saveData + "3-";
    }

    public void addCan() {
        data.add(addItem(getResources().getDrawable(R.drawable.can), "캔", "캔의 내용물을 제거 후 버려주세요"));
        saveData = saveData + "4-";
    }

    public void addStrph() {
        data.add(addItem(getResources().getDrawable(R.drawable.ps), "스티로폼", "스티로폼의 이물질을 제거후 이물질이 없는 상태로 버려주세요"));
        saveData = saveData + "5-";
    }

    public void addPet() {
        data.add(addItem(getResources().getDrawable(R.drawable.pete), "페트병", "페트병 안의 내용물을 제거한 뒤 페트병에 붙어 있는 비닐과 뚜껑을 제거 후 분리수거 합니다."));
        saveData = saveData + "6-";
    }

    public void addTrash() {
        data.add(addItem(getResources().getDrawable(R.drawable.normal_black), "일반쓰레기", "종량제 봉투에 담아 버려주세요."));
        saveData = saveData + "8-";
    }

    public void addGlass() {
        data.add(addItem(getResources().getDrawable(R.drawable.glass), "유리", "공병의 경우 이물질을 제거 후 버려주세요."));
        saveData = saveData + "7-";
    }

    public void addElec() {
        data.add(addItem(getResources().getDrawable(R.drawable.bolt_black), "전자제품", "한 면의 길이가 1m 미만인 소형가전은 재활용품 배출시 함께 배출, 1m 이상인 대형가전은 대형폐가전 무상방문수거 서비스를 이용해 배출"));
        saveData = saveData + "9 - ";
    }
}

