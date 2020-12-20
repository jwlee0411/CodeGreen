package app.sunrin.codegreen;


import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class RecentActivity extends AppCompatActivity {
    //메인화면(카메라로 바코드 스캔하는 곳)에서 설정 버튼 누르면 들어오는 화면임


    String[] data;
    String[][] newData;

    int[] recycleCategory = {0, 0, 0, 0, 0, 0, 0, 0, 0};
    //각 카테고리별로 몇 개를 버렸는지
    //플라스틱 1,종이 2,비닐 3,캔 4,스티로폼 5,페트병 6,유리 7,일반쓰레기 8, 전자제품 9

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent);

        //sharedpreference : 이름 saveAll, 키 saveAll에 String형으로 모든 데이터를 때려박음
        SharedPreferences dataSave = getSharedPreferences("saveAll", 0);
        String dataAll = dataSave.getString("saveAll", "");
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


         newData = new String[data.length][intRecycle+1];
         System.out.println(data.length);

        for(int i = 0; i<data.length; i++)
        {

            System.out.println(data[i]);
            String[] tempData = data[i].split("@");
            System.out.println(tempData[i]);
            System.out.println(tempData.length);
            for(int j = 0; j<6; j++)
            {
                newData[i][j] = tempData[j];
                System.out.println(newData[i][j]);
            }
        }

        for(int i = 0; i<data.length; i++)
        {
            for(int j = 1; j<=9; j++)
            {
                System.out.println("f" + newData[i][intRecycle]);
                if(newData[i][intRecycle].contains(Integer.toString(j)))
                {
                    recycleCategory[j-1]++;
                }
            }
        }

        System.out.println(recycleCategory[0]);
        System.out.println(recycleCategory[1]);
        System.out.println(recycleCategory[2]);







    }

}