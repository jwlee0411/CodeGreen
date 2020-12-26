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


import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
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


    }; //0번째 인덱스 => 총 쓰레기 수
    // 1~9번째 인덱스 => 재활용 관련 정보(1~9)


    //메인화면(카메라로 바코드 스캔하는 곳)에서 설정 버튼 누르면 들어오는 화면임

//    PieChart pieChart;


    String[] data;
    String[][] newData;
    String[][] myData;

    int[] monthTotal= {0,0,0,0,0,0,0,0,0,0,0,0};

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



        if(dataAll.equals("") || dataAll.equals(" ")||dataAll.equals(null))
        {
            Toast.makeText(this, "조회 내역이 없습니다. 바코드 인식 후 다시 시도해주세요!", Toast.LENGTH_SHORT).show();
            finish();
        }
        else
        {
            dataAll = dataAll.substring(dataAll.indexOf("★")+1);



            //데이터를 차곡차곡 배열로 바꿔줄거에염♥
            //처음에 있는 별을 없애야 제대로 작동되므로 위에서 substring을 해줬어영
            data = dataAll.split("★");




            //myData[i][0] => 상품 사진 링크
            //myData[i][1] => 상품명
            //myData[i][2] => 상품 카테고리
            //myData[i][3] => 스캔한 날짜
            //myData[i][4] => KAN코드
            //myData[i][5] => 분리배출방법(하이픈으로 연결되어 있음)

            //내 상품 정보
            myData = new String[data.length][intRecycle + 1];
            System.out.println("data.length");
            System.out.println(data.length);
            System.out.println(data[0]);



            for(int i = 0; i<data.length; i++)
            {
                myData[i] = data[i].split("@");
                myData[i][3] = myData[i][3].substring(5, 7);
                System.out.println("12345" + myData[i][3]);
                System.out.println("@" + Arrays.toString(myData[i]));

            }




            //모든 유저의 재활용 정보 불러오기


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

        checkMonth();

        lineChart();

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

    private void checkMonth(){
        for(int i=0; i<myData.length;i++){
            int pl=Integer.parseInt(myData[i][3]);
            monthTotal[pl-1]++;
            System.out.println("myData[i][3]");
            System.out.println(myData[i][3]);
        }
    }

    private void lineChart(){

        ArrayList<Entry> entry_chart = new ArrayList<>();

        LineChart lineChart = findViewById(R.id.linechart);
        LineData chartData = new LineData();

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("MM");
        String monthPl = sdf.format(cal.getTime());
        int nowMonth=Integer.parseInt(monthPl);

        for(int i=nowMonth; i<12;i++) {
            entry_chart.add(new Entry(i+1, monthTotal[i]));
            System.out.println(i);
            System.out.println(monthTotal[i]);
        }
        for(int i=0;i<nowMonth;i++) {
            entry_chart.add(new Entry(i+1, monthTotal[i]));
            System.out.println(i);
            System.out.println(monthTotal[i]);
        }

        LineDataSet lineDataSet = new LineDataSet(entry_chart, "월별 누적량");
        chartData.addDataSet(lineDataSet);



        lineDataSet.setColor(Color.BLACK);
        lineDataSet.setCircleColor(Color.BLACK);
        lineDataSet.setMode(LineDataSet.Mode.LINEAR);
        lineChart.animateXY(1000,1000);

        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        YAxis yRAxis = lineChart.getAxisRight();
        yRAxis.setDrawLabels(false);
        yRAxis.setDrawAxisLine(false);
        yRAxis.setDrawGridLines(false);


        lineChart.setData(chartData);

        lineChart.invalidate();
    }

    //TODO

}


