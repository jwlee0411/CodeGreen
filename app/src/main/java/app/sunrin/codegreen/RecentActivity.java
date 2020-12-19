package app.sunrin.codegreen;


import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class RecentActivity extends AppCompatActivity {
    //메인화면(카메라로 바코드 스캔하는 곳)에서 설정 버튼 누르면 들어오는 화면임

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recent);

        //sharedpreference : 이름 saveAll, 키 saveAll에 String형으로 모든 데이터를 때려박음
        SharedPreferences dataSave = getSharedPreferences("saveAll", 0);
        String dataAll = dataSave.getString("saveAll", "");
        System.out.println(dataAll);


        //데이터를 차곡차곡 배열로 바꿔줄거에염♥
        String[] data = dataAll.split("★");


        //newData[i][0] => 상품 사진 링크
        //newData[i][1] => 상품명
        //newData[i][2] => 상품 카테고리
        //newData[i][3] => KAN코드
        //newData[i][4] => 분리배출방법(하이픈으로 연결되어 있어 우진이가 알아서 잘 처리해줄 필요 有)
        String[][] newData = new String[100][100];

        for(int i = 0; i<data.length; i++)
        {
            String[] tempData = data[i].split("☆");
            for(int j = 0; j<tempData.length; j++)
            {
                newData[i][j] = tempData[j];
                System.out.println(newData[i][j]);
            }
        }


    }

}