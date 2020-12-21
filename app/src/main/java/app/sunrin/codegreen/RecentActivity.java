package app.sunrin.codegreen;


import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class RecentActivity extends AppCompatActivity {
    //메인화면(카메라로 바코드 스캔하는 곳)에서 설정 버튼 누르면 들어오는 화면임

//    PieChart pieChart;

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
        String dataAll = dataSave.getString("saveAll", "");
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

        int intPhotoLink = 0;
        int intProductName = 1;
        int intProductCategory = 2;
        int intScanDate = 3;
        int intKANcode = 4;
        int intRecycle = 5;



        //전체 상품 정보
        newData = new String[data.length][intRecycle+1];
        System.out.println("data.length");
        System.out.println(data.length);

        for(int i = 0; i<data.length; i++)
        {
            System.out.println("data[i]");
            System.out.println(data[i]);
            String[] tempData = data[i].split("@");
            System.out.println("newData[i][j]");
            for(int j = 0; j<6; j++)
            {
                newData[i][j] = tempData[j];

                System.out.println(newData[i][j]);
            }
        }

        Calendar today = Calendar.getInstance ( );
        SimpleDateFormat Format = new SimpleDateFormat("YYYY-MM");
        Date date = today.getTime ( );
        String thisMonth = Format.format(date);

        //전체 상품의 재활용 정보
        //혹시 필요할 수도 있을까봐 남겨둠
        for(int i = 0; i<data.length; i++)
        {
            if(newData[i][intScanDate].contains(thisMonth)) {
                for (int j = 1; j <= 9; j++) {
                    if (newData[i][intRecycle].contains(Integer.toString(j))) {
                        recycleCategory[j - 1]++;
                    }
                }
            }
        }



        //전체 재활용 정보(일자별)
        recycle = new int[data.length][12];
        for(int i = 0; i<data.length; i++)
        {
            String getYear = newData[i][intScanDate].substring(0, 4);
            String getMonth = newData[i][intScanDate].substring(5, 7);
            String getDay = newData[i][intScanDate].substring(8, 10);

            recycle[i][0] = Integer.parseInt(getYear);
            recycle[i][1] = Integer.parseInt(getMonth);
            recycle[i][2] = Integer.parseInt(getDay);

            System.out.println("recycle[i][0]");

            System.out.println(recycle[i][0]);
            System.out.println("recycle[i][1]");
            System.out.println(recycle[i][1]);
            System.out.println("recycle[i][2]");
            System.out.println(recycle[i][2]);

            System.out.println("recycle[i][j+2]");
            for(int j = 1; j<=9; j++)
            {
                if(newData[i][intRecycle].contains(Integer.toString(j)))
                {
                    recycle[i][j+2] = 1;

                }
                else
                {
                    recycle[i][j+2] = 0;
                }

                System.out.println(recycle[i][j+2]);


            }
        }

//        setPieChart();

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

}