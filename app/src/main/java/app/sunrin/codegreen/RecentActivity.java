package app.sunrin.codegreen;


import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class RecentActivity extends AppCompatActivity {

    //여기가 새로 추가한 거니까 지우지 말 것
    FirebaseDatabase database;
    DatabaseReference myRef;
    String[][] finalValue;
    String dataAll;
    String[] tempData;

    int totalLength = 0;

    int userSex = 0;
    int userYear = 1;
    int userMonth = 2;
    int userPW = 3;
    int userDay = 4;
    int userID = 5;
    int value = 6;
    int userAge = 7;


    int intPhotoLink = 0;
    int intProductName = 1;
    int intProductCategory = 2;
    int intScanDate = 3;
    int intKANcode = 4;
    int intRecycle = 5;
    int userNum;


    int[][] totalData = {
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, //총 남자
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, //10대 남자
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, //20대
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, //30대
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, //40대
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, //50대
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, //60대
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, //기타 나이

            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, //총 여자
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, //10대 여자
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, //20대
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, //30대
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, //40대
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, //50대
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, //60대
            {0, 0, 0, 0, 0, 0, 0, 0, 0, 0}, //기타 나이


    }; //총 쓰레기 수 + 재활용 관련 정보(1~9)


    //메인화면(카메라로 바코드 스캔하는 곳)에서 설정 버튼 누르면 들어오는 화면임

//    PieChart pieChart;


    //TODO: 로그인 방식 변경으로 소스코드 수정 필요
    String[] data;
    String[][] newData;

    int[] recycleCategory = {0, 0, 0, 0, 0, 0, 0, 0, 0};

    int[][] recycle;
    //각 카테고리별로 몇 개를 버렸는지
    //플라스틱 1,종이 2,비닐 3,캔 4,스티로폼 5,페트병 6,유리 7,일반쓰레기 8, 전자제품 9

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent);


        //sharedpreference : 이름 saveAll, 키 saveAll에 String형으로 모든 데이터를 때려박음
        SharedPreferences dataSave = getSharedPreferences("saveAll", 0);
        dataAll = dataSave.getString("saveAll", "");
        System.out.println("dataAll");
        System.out.println(dataAll);


        dataAll = dataAll.substring(1);


        //데이터를 차곡차곡 배열로 바꿔줄거에염♥
        //처음에 있는 별을 없애야 제대로 작동되므로 위에서 substring을 해줬어영
        data = dataAll.split("★");


        //newData[i][0] => 상품 사진 링크
        //newData[i][1] => 상품명
        //newData[i][2] => 상품 카테고리
        //newData[i][3] => 스캔한 날짜
        //newData[i][4] => KAN코드
        //newData[i][5] => 분리배출방법(하이픈으로 연결되어 있음)




        //전체 상품 정보
        newData = new String[data.length][intRecycle + 1];
        System.out.println("data.length");
        System.out.println(data.length);
//
//        for(int i = 0; i<data.length; i++)
//        {
//            System.out.println("data[i]");
//            System.out.println(data[i]);
//            String[] tempData = data[i].split("@");
//            System.out.println("newData[i][j]");
//            for(int j = 0; j<6; j++)
//            {
//                newData[i][j] = tempData[j];
//
//                System.out.println(newData[i][j]);
//            }
//        }
//
//        Calendar today = Calendar.getInstance ( );
//        SimpleDateFormat Format = new SimpleDateFormat("YYYY-MM");
//        Date date = today.getTime ( );
//        String thisMonth = Format.format(date);
//
//        //전체 상품의 재활용 정보
//        //혹시 필요할 수도 있을까봐 남겨둠
//        for(int i = 0; i<data.length; i++)
//        {
//            if(newData[i][intScanDate].contains(thisMonth)) {
//                for (int j = 1; j <= 9; j++) {
//                    if (newData[i][intRecycle].contains(Integer.toString(j))) {
//                        recycleCategory[j - 1]++;
//                    }
//                }
//            }
//        }
//
//
//
//        //전체 재활용 정보(일자별)
//        recycle = new int[data.length][12];
//        for(int i = 0; i<data.length; i++)
//        {
//            String getYear = newData[i][intScanDate].substring(0, 4);
//            String getMonth = newData[i][intScanDate].substring(5, 7);
//            String getDay = newData[i][intScanDate].substring(8, 10);
//
//            recycle[i][0] = Integer.parseInt(getYear);
//            recycle[i][1] = Integer.parseInt(getMonth);
//            recycle[i][2] = Integer.parseInt(getDay);
//
//            System.out.println("recycle[i][0]");
//
//            System.out.println(recycle[i][0]);
//            System.out.println("recycle[i][1]");
//            System.out.println(recycle[i][1]);
//            System.out.println("recycle[i][2]");
//            System.out.println(recycle[i][2]);
//
//            System.out.println("recycle[i][j+2]");
//            for(int j = 1; j<=9; j++)
//            {
//                if(newData[i][intRecycle].contains(Integer.toString(j)))
//                {
//                    recycle[i][j+2] = 1;
//
//                }
//                else
//                {
//                    recycle[i][j+2] = 0;
//                }
//
//                System.out.println(recycle[i][j+2]);
//
//
//            }
//        }


        //TODO : 모든 유저의 재활용 정보 불러오기


        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("user");

        DatabaseReference referenceUserNum = database.getReference("userNum");

        referenceUserNum.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userNum = snapshot.getValue(Integer.class);
                int maxVal = 8;

                finalValue = new String[10000][maxVal];


                myRef = database.getReference("user");


                myRef.addChildEventListener(new ChildEventListener() {
                    @Override
                    public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                        String valueAll = snapshot.getValue().toString();
                        valueAll = valueAll.replace("{", "").replace("}", "")
                                .replace("userSex=", "")
                                .replace("userYear=", "")
                                .replace("userMonth=", "")
                                .replace("userPW=", "")
                                .replace("userDay=", "")
                                .replace("userID=", "")
                                .replace("value=", "")
                                .replace("userAge=", "");
                        String[] newValue = valueAll.split(", ");
                        System.out.println(valueAll + "★");
                        //System.out.println(newValue[0] + newValue[1] + newValue[2]);

                        for (int j = 0; j < newValue.length; j++) {
                            finalValue[totalLength][j] = newValue[j];
                            System.out.println(newValue[j]);
                        }
                        totalLength++;

                        System.out.println("::::" + userNum);
                        System.out.println(":::::" + totalLength);

                        if(totalLength == userNum)
                        {
                            for (int i = 0; i < totalLength; i++) {
                                System.out.println(Arrays.toString(finalValue[i]));
                                if(!finalValue[i][value].contains("★"))
                                {

                                }
                                else {
                                    dataAll = finalValue[i][value].substring(2);


                                    //데이터를 차곡차곡 배열로 바꿔줄거에염♥
                                    //처음에 있는 별을 없애야 제대로 작동되므로 위에서 substring을 해줬어영
                                    data = dataAll.split("★");

                                    newData = new String[data.length][intRecycle + 1];
                                    System.out.println("data.length");
                                    System.out.println(data.length);

                                    for (int j = 0; j < data.length; j++) {
                                        System.out.println(data[j]);
                                        tempData = data[j].split("@");
                                        checkWhoTrash(i);
                                    }

                                    //플라스틱 1,종이 2,비닐 3,캔 4,스티로폼 5,페트병 6,유리 7,일반쓰레기 8, 전자제품 9
                                }

                            }


                            showArray();


                        }




                    }

                    @Override
                    public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                    }

                    @Override
                    public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });







    }


//        setPieChart();


  private void showArray()
  {
      System.out.println(totalData[0][0]);
      System.out.println(totalData[8][0]);
  }

  private void checkWhoTrash(int i)
  {
      if (finalValue[i][userSex].equals("true")) {
          System.out.println("boy");
          totalData[0][0]++;
          if (tempData[5].contains("1")) totalData[0][1]++;
          if (tempData[5].contains("2")) totalData[0][2]++;
          if (tempData[5].contains("3")) totalData[0][3]++;
          if (tempData[5].contains("4")) totalData[0][4]++;
          if (tempData[5].contains("5")) totalData[0][5]++;
          if (tempData[5].contains("6")) totalData[0][6]++;
          if (tempData[5].contains("7")) totalData[0][7]++;
          if (tempData[5].contains("8")) totalData[0][8]++;
          if (tempData[5].contains("9")) totalData[0][9]++;


          if (Integer.parseInt(finalValue[i][userAge]) >= 70) {
              totalData[7][0]++;
              if (tempData[5].contains("1")) totalData[7][1]++;
              if (tempData[5].contains("2")) totalData[7][2]++;
              if (tempData[5].contains("3")) totalData[7][3]++;
              if (tempData[5].contains("4")) totalData[7][4]++;
              if (tempData[5].contains("5")) totalData[7][5]++;
              if (tempData[5].contains("6")) totalData[7][6]++;
              if (tempData[5].contains("7")) totalData[7][7]++;
              if (tempData[5].contains("8")) totalData[7][8]++;
              if (tempData[5].contains("9")) totalData[7][9]++;
          } else if (Integer.parseInt(finalValue[i][userAge]) >= 60) {
              totalData[6][0]++;
              if (tempData[5].contains("1")) totalData[6][1]++;
              if (tempData[5].contains("2")) totalData[6][2]++;
              if (tempData[5].contains("3")) totalData[6][3]++;
              if (tempData[5].contains("4")) totalData[6][4]++;
              if (tempData[5].contains("5")) totalData[6][5]++;
              if (tempData[5].contains("6")) totalData[6][6]++;
              if (tempData[5].contains("7")) totalData[6][7]++;
              if (tempData[5].contains("8")) totalData[6][8]++;
              if (tempData[5].contains("9")) totalData[6][9]++;
          } else if (Integer.parseInt(finalValue[i][userAge]) >= 50) {
              totalData[5][0]++;
              if (tempData[5].contains("1")) totalData[5][1]++;
              if (tempData[5].contains("2")) totalData[5][2]++;
              if (tempData[5].contains("3")) totalData[5][3]++;
              if (tempData[5].contains("4")) totalData[5][4]++;
              if (tempData[5].contains("5")) totalData[5][5]++;
              if (tempData[5].contains("6")) totalData[5][6]++;
              if (tempData[5].contains("7")) totalData[5][7]++;
              if (tempData[5].contains("8")) totalData[5][8]++;
              if (tempData[5].contains("9")) totalData[5][9]++;
          } else if (Integer.parseInt(finalValue[i][userAge]) >= 40) {
              totalData[4][0]++;
              if (tempData[5].contains("1")) totalData[4][1]++;
              if (tempData[5].contains("2")) totalData[4][2]++;
              if (tempData[5].contains("3")) totalData[4][3]++;
              if (tempData[5].contains("4")) totalData[4][4]++;
              if (tempData[5].contains("5")) totalData[4][5]++;
              if (tempData[5].contains("6")) totalData[4][6]++;
              if (tempData[5].contains("7")) totalData[4][7]++;
              if (tempData[5].contains("8")) totalData[4][8]++;
              if (tempData[5].contains("9")) totalData[4][9]++;
          } else if (Integer.parseInt(finalValue[i][userAge]) >= 30) {
              totalData[3][0]++;
              if (tempData[5].contains("1")) totalData[3][1]++;
              if (tempData[5].contains("2")) totalData[3][2]++;
              if (tempData[5].contains("3")) totalData[3][3]++;
              if (tempData[5].contains("4")) totalData[3][4]++;
              if (tempData[5].contains("5")) totalData[3][5]++;
              if (tempData[5].contains("6")) totalData[3][6]++;
              if (tempData[5].contains("7")) totalData[3][7]++;
              if (tempData[5].contains("8")) totalData[3][8]++;
              if (tempData[5].contains("9")) totalData[3][9]++;
          } else if (Integer.parseInt(finalValue[i][userAge]) >= 20) {
              totalData[2][0]++;
              if (tempData[5].contains("1")) totalData[2][1]++;
              if (tempData[5].contains("2")) totalData[2][2]++;
              if (tempData[5].contains("3")) totalData[2][3]++;
              if (tempData[5].contains("4")) totalData[2][4]++;
              if (tempData[5].contains("5")) totalData[2][5]++;
              if (tempData[5].contains("6")) totalData[2][6]++;
              if (tempData[5].contains("7")) totalData[2][7]++;
              if (tempData[5].contains("8")) totalData[2][8]++;
              if (tempData[5].contains("9")) totalData[2][9]++;
          } else if (Integer.parseInt(finalValue[i][userAge]) >= 10) {
              totalData[1][0]++;
              if (tempData[5].contains("1")) totalData[1][1]++;
              if (tempData[5].contains("2")) totalData[1][2]++;
              if (tempData[5].contains("3")) totalData[1][3]++;
              if (tempData[5].contains("4")) totalData[1][4]++;
              if (tempData[5].contains("5")) totalData[1][5]++;
              if (tempData[5].contains("6")) totalData[1][6]++;
              if (tempData[5].contains("7")) totalData[1][7]++;
              if (tempData[5].contains("8")) totalData[1][8]++;
              if (tempData[5].contains("9")) totalData[1][9]++;
          } else {
              totalData[7][0]++;
              if (tempData[5].contains("1")) totalData[7][1]++;
              if (tempData[5].contains("2")) totalData[7][2]++;
              if (tempData[5].contains("3")) totalData[7][3]++;
              if (tempData[5].contains("4")) totalData[7][4]++;
              if (tempData[5].contains("5")) totalData[7][5]++;
              if (tempData[5].contains("6")) totalData[7][6]++;
              if (tempData[5].contains("7")) totalData[7][7]++;
              if (tempData[5].contains("8")) totalData[7][8]++;
              if (tempData[5].contains("9")) totalData[7][9]++;
          }


      } else {
          System.out.println("girl");
          totalData[8][0]++;
          if (tempData[5].contains("1")) totalData[8][1]++;
          if (tempData[5].contains("2")) totalData[8][2]++;
          if (tempData[5].contains("3")) totalData[8][3]++;
          if (tempData[5].contains("4")) totalData[8][4]++;
          if (tempData[5].contains("5")) totalData[8][5]++;
          if (tempData[5].contains("6")) totalData[8][6]++;
          if (tempData[5].contains("7")) totalData[8][7]++;
          if (tempData[5].contains("8")) totalData[8][8]++;
          if (tempData[5].contains("9")) totalData[8][9]++;


          if (Integer.parseInt(finalValue[i][userAge]) >= 70) {
              totalData[15][0]++;
              if (tempData[5].contains("1")) totalData[15][1]++;
              if (tempData[5].contains("2")) totalData[15][2]++;
              if (tempData[5].contains("3")) totalData[15][3]++;
              if (tempData[5].contains("4")) totalData[15][4]++;
              if (tempData[5].contains("5")) totalData[15][5]++;
              if (tempData[5].contains("6")) totalData[15][6]++;
              if (tempData[5].contains("7")) totalData[15][7]++;
              if (tempData[5].contains("8")) totalData[15][8]++;
              if (tempData[5].contains("9")) totalData[15][9]++;
          } else if (Integer.parseInt(finalValue[i][userAge]) >= 60) {
              totalData[14][0]++;
              if (tempData[5].contains("1")) totalData[14][1]++;
              if (tempData[5].contains("2")) totalData[14][2]++;
              if (tempData[5].contains("3")) totalData[14][3]++;
              if (tempData[5].contains("4")) totalData[14][4]++;
              if (tempData[5].contains("5")) totalData[14][5]++;
              if (tempData[5].contains("6")) totalData[14][6]++;
              if (tempData[5].contains("7")) totalData[14][7]++;
              if (tempData[5].contains("8")) totalData[14][8]++;
              if (tempData[5].contains("9")) totalData[14][9]++;
          } else if (Integer.parseInt(finalValue[i][userAge]) >= 50) {
              totalData[13][0]++;
              if (tempData[5].contains("1")) totalData[13][1]++;
              if (tempData[5].contains("2")) totalData[13][2]++;
              if (tempData[5].contains("3")) totalData[13][3]++;
              if (tempData[5].contains("4")) totalData[13][4]++;
              if (tempData[5].contains("5")) totalData[13][5]++;
              if (tempData[5].contains("6")) totalData[13][6]++;
              if (tempData[5].contains("7")) totalData[13][7]++;
              if (tempData[5].contains("8")) totalData[13][8]++;
              if (tempData[5].contains("9")) totalData[13][9]++;
          } else if (Integer.parseInt(finalValue[i][userAge]) >= 40) {
              totalData[12][0]++;
              if (tempData[5].contains("1")) totalData[12][1]++;
              if (tempData[5].contains("2")) totalData[12][2]++;
              if (tempData[5].contains("3")) totalData[12][3]++;
              if (tempData[5].contains("4")) totalData[12][4]++;
              if (tempData[5].contains("5")) totalData[12][5]++;
              if (tempData[5].contains("6")) totalData[12][6]++;
              if (tempData[5].contains("7")) totalData[12][7]++;
              if (tempData[5].contains("8")) totalData[12][8]++;
              if (tempData[5].contains("9")) totalData[12][9]++;
          } else if (Integer.parseInt(finalValue[i][userAge]) >= 30) {
              totalData[11][0]++;
              if (tempData[5].contains("1")) totalData[11][1]++;
              if (tempData[5].contains("2")) totalData[11][2]++;
              if (tempData[5].contains("3")) totalData[11][3]++;
              if (tempData[5].contains("4")) totalData[11][4]++;
              if (tempData[5].contains("5")) totalData[11][5]++;
              if (tempData[5].contains("6")) totalData[11][6]++;
              if (tempData[5].contains("7")) totalData[11][7]++;
              if (tempData[5].contains("8")) totalData[11][8]++;
              if (tempData[5].contains("9")) totalData[11][9]++;
          } else if (Integer.parseInt(finalValue[i][userAge]) >= 20) {
              totalData[10][0]++;
              if (tempData[5].contains("1")) totalData[10][1]++;
              if (tempData[5].contains("2")) totalData[10][2]++;
              if (tempData[5].contains("3")) totalData[10][3]++;
              if (tempData[5].contains("4")) totalData[10][4]++;
              if (tempData[5].contains("5")) totalData[10][5]++;
              if (tempData[5].contains("6")) totalData[10][6]++;
              if (tempData[5].contains("7")) totalData[10][7]++;
              if (tempData[5].contains("8")) totalData[10][8]++;
              if (tempData[5].contains("9")) totalData[10][9]++;
          } else if (Integer.parseInt(finalValue[i][userAge]) >= 10) {
              totalData[9][0]++;
              if (tempData[5].contains("1")) totalData[9][1]++;
              if (tempData[5].contains("2")) totalData[9][2]++;
              if (tempData[5].contains("3")) totalData[9][3]++;
              if (tempData[5].contains("4")) totalData[9][4]++;
              if (tempData[5].contains("5")) totalData[9][5]++;
              if (tempData[5].contains("6")) totalData[9][6]++;
              if (tempData[5].contains("7")) totalData[9][7]++;
              if (tempData[5].contains("8")) totalData[9][8]++;
              if (tempData[5].contains("9")) totalData[9][9]++;
          } else {
              totalData[15][0]++;
              if (tempData[5].contains("1")) totalData[15][1]++;
              if (tempData[5].contains("2")) totalData[15][2]++;
              if (tempData[5].contains("3")) totalData[15][3]++;
              if (tempData[5].contains("4")) totalData[15][4]++;
              if (tempData[5].contains("5")) totalData[15][5]++;
              if (tempData[5].contains("6")) totalData[15][6]++;
              if (tempData[5].contains("7")) totalData[15][7]++;
              if (tempData[5].contains("8")) totalData[15][8]++;
              if (tempData[5].contains("9")) totalData[15][9]++;
          }
      }
  }
}

//    public void setPieChart(){
//        pieChart = (PieChart)findViewById(R.id.piechart);
//
//        pieChart.invalidate();
//        pieChart.setUsePercentValues(true);
//        pieChart.getDescription().setEnabled(false);
//        pieChart.setExtraOffsets(5,10,5,5);
//
//        pieChart.setDragDecelerationFrictionCoef(0.95f);
//
//
//
//        //가운데 원형 뚫는거
//        pieChart.setDrawHoleEnabled(false);
//
//        pieChart.setHoleColor(Color.BLACK);
//
//
//        //원형차트 고정
//        pieChart.setRotationEnabled(false);
//
//        // 투명한 원반경 설정
//        pieChart.setTransparentCircleRadius(61f);
//
//        pieChart.setRotationAngle(  300f);
//
//        ArrayList<PieEntry> yValues = new ArrayList<PieEntry>();
//
//        for(int i =0;i<9;i++){
//            if(recycleCategory[i]!=0) {
//                switch (i){
//                    case 0:
//                        yValues.add(new PieEntry(recycleCategory[0],"플라스틱"));
//                        break;
//                    case 1:
//                        yValues.add(new PieEntry(recycleCategory[1],"종이"));
//                        break;
//                    case 2:
//                        yValues.add(new PieEntry(recycleCategory[2],"비닐"));
//                        break;
//                    case 3:
//                        yValues.add(new PieEntry(recycleCategory[3],"캔"));
//                        break;
//                    case 4:
//                        yValues.add(new PieEntry(recycleCategory[4],"스티로폼"));
//                        break;
//                    case 5:
//                        yValues.add(new PieEntry(recycleCategory[5],"페트병"));
//                        break;
//                    case 6:
//                        yValues.add(new PieEntry(recycleCategory[6],"유리"));
//                        break;
//                    case 7:
//                        yValues.add(new PieEntry(recycleCategory[7],"일반 쓰레기"));
//                        break;
//                    case 8:
//                        yValues.add(new PieEntry(recycleCategory[8],"전자제품"));
//                        break;
//
//                }
//            }
//        }
//
//
//        Description description = new Description();
//        description.setText("월간 배출량"); //라벨
//        description.setTextSize(32f);
//        pieChart.setDescription(description);
//
//        pieChart.animateXY(1000, 1000); //애니메이션
//
//        PieDataSet dataSet = new PieDataSet(yValues,"품목");
//        dataSet.setSliceSpace(3f);
//        dataSet.setSelectionShift(5f);
//        dataSet.setValueTextSize(32);
//        dataSet.setFormSize(32);
//        dataSet.setColors(ColorTemplate.JOYFUL_COLORS);
//
//        PieData data = new PieData((dataSet));
//        data.setValueTextSize(16f);
//        data.setValueTextColor(Color.GREEN);
//
//        pieChart.setData(data);
//    }

