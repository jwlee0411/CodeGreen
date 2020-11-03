package app.sunrin.codegreen;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class AfterScanActivity extends AppCompatActivity {

    TextView productName, productCategory, textView3;
    String url, Shorturl;
    String Category;
    Bitmap bitmap;
    ImageView img;
    String result;
    RecyclerView recyclerView;
    ArrayList<setData> SetData = new ArrayList<setData>();
    ArrayList<Item> data = new ArrayList<>(); //분리배출 방법의 recyclerView에 넣는 arraylist

    SharedPreferences preferences, preferences1;
    // data arraylist에 데이터를 넣는 함수
    public void addPlastic()
    {
        data.add(addItem(getResources().getDrawable(R.drawable.ic_launcher_foreground),"플라스틱","플라스틱은 말이여,,,"));
    }

    public void addPaper()
    {
        data.add(addItem(getResources().getDrawable(R.drawable.ic_launcher_foreground),"종이","종이는 말이여,,,"));

    }
    public void addVinyl()
    {
        data.add(addItem(getResources().getDrawable(R.drawable.ic_launcher_foreground),"비닐","비닐은 말이여,,,"));

    }
    public void addCan()
    {
        data.add(addItem(getResources().getDrawable(R.drawable.ic_launcher_foreground),"캔","캔은 말이여,,,"));

    }
    public void addStrph()
    {
        data.add(addItem(getResources().getDrawable(R.drawable.ic_launcher_foreground),"스티로폼","스티로폼은 말이여,,,"));
    }
    public void addPet()
    {
        data.add(addItem(getResources().getDrawable(R.drawable.ic_launcher_foreground),"페트병","페트병은 말이여,,,"));
    }
    public void addTrash()
    {
        data.add(addItem(getResources().getDrawable(R.drawable.ic_launcher_foreground),"일반쓰레기","일반쓰레기는 말이여,,,"));
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_after_scan);



        preferences = getSharedPreferences("BarcodeResult", 0);
        result = preferences.getString("result", "");
        Shorturl = "http://gs1.koreannet.or.kr";
        url = Shorturl + "/pr/" + result; //바코드를 통한 url 가져오기

        preferences1 = getSharedPreferences("RecentData", 0);



        img = findViewById(R.id.product_img);




        // 네트워크 연결상태 체크
        if (NetworkConnection() == false)
        {
            NotConnected_showAlert();
        }

        initView();



    }

    public Item addItem(Drawable img, String title, String way)
    {
        Item item = new Item();
        item.setRecycle_img(img);
        item.setRecycle_class(title);
        item.setRecycle_how(way);
        return item;
    }
    private void initView() {

        productName = findViewById(R.id.product_name);
        productCategory = findViewById(R.id.pn);
        //textView3 = findViewById(R.id.textView3);


        new getName().execute();//상품명 가져오기
        new getCategory().execute();//상품 카테고리 가져오기
        new getPhoto1().execute();




        System.out.println(Category);
        switch(result) // 1차로 바코드 번호를 불러와서 분리수거 정보를 가져온다.
        {
            case "8808244201014":
                addVinyl();
                break;
            case "8809482500358":
               addPet();
                break;
            default: //등록된 바코드가 없을 경우
/*
                if(Category.contains("생수")) //카테고리를 기준으로 상품을 구분한다.
                {
                    data.add(addItem(getResources().getDrawable(R.drawable.ic_launcher_foreground),"페트병","페트병은 말이여,,,"));
                }
                else //해당되는 카테고리도 없을 경우
                {
                */

                    //분리수거에 관한 모든 정보를 띄워준다.
                    addCan();
                    addPaper();
                    addPet();
                    addPlastic();
                    addStrph();
                    addTrash();

               // }

        }



        recyclerView =findViewById(R.id.recycling);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        ReAdapter reAdapter = new ReAdapter(data);
        recyclerView.setAdapter(reAdapter);

    }

    private class getName extends AsyncTask<String, Void, String> //상품명 가져오기
    {

        @Override
        protected String doInBackground(String... params)
        {
            try {
                Connection.Response response = Jsoup.connect(url).method(Connection.Method.GET).execute();
                Document document = response.parse();


                Element category = document.select("h3").first();
                System.out.println(category);
                String categoryNew = category.toString();
                String categoryNew2 = categoryNew.replace("<h3>", "").replace("</h3>", "");
                return categoryNew2;


            }
            catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result)
        {// 결과값을 화면에 표시함.

            productName.setText(result);
            Category = result;
        }
    }

    private class getCategory extends AsyncTask<String, Void, String> //상품 카테고리 가져오기
    {

        @Override
        protected String doInBackground(String... params)
        {
            try {
                Connection.Response response = Jsoup.connect(url).method(Connection.Method.GET).execute();
                Document document = response.parse();



                    Element product = document.select("tr[class=b_line]").first();
                    System.out.println(product);
                    String productNew = product.toString();
                    String productNew2 = productNew.replace("<tr class=\"b_line\">", "").replace("<th>KAN 상품분류</th>", "").replace("&gt;", ">").replace("</tr>", "").replace("<td>", "").replace("</td", "");
                    return productNew2;


            }
            catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result)
        {// 결과값을 화면에 표시함.

                productCategory.setText(result);
        }
    }

    private class getPhoto1 extends AsyncTask<String, Void, String> //상품 사진 링크 가져오기
    {


        @Override
        protected String doInBackground(String... params)
        {
            try {
                Connection.Response response = Jsoup.connect(url).method(Connection.Method.GET).execute();
                Document document = response.parse();

                Element product = document.select("ul[class=btn_img]").first();
                System.out.println(product);
                String productNew = product.toString();

                String target = "/pr/";

                int target_num = productNew.indexOf(target);
                String result;
                result = Shorturl + productNew.substring(target_num,(productNew.substring(target_num).indexOf("\',\'")+target_num));
                result = result.replace("&amp;", "&");
                System.out.println(result);



                return result;


            }
            catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result)
        {
            TextView textView = findViewById(R.id.debug);
            textView.setText(result);
            new LoadImage().execute(result);

            //이미지가 띄워야 할 것 중 가장 마지막에 load되므로 image를 띄운 후 해당 데이터를 SharedPreference에 저장함
            //SharedPreference에 String형으로 저장하기 위해 String 3개 -> ArrayList -> JSON -> String으로 저장하는 코드임.
            // setData : 클래스, SetData : ArrayList
            setData setData = new setData(productName.getText().toString(), productCategory.getText().toString(), textView.getText().toString(), "추후 수정");
            //String 3개 -> ArrayList
            SetData.add(setData);


            // ArrayList -> JSON -> String -> 저장완료
            setStringArrayPref("settings_item_json", SetData);


        }
    }

    private class LoadImage extends AsyncTask<String, String, Bitmap> {
        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();

        }

        protected Bitmap doInBackground(String... args) {
            try
            {
                bitmap = BitmapFactory.decodeStream((InputStream) new URL(args[0]).getContent());
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap image) {
            if(image != null)
            {
                img.setImageBitmap(image);

            }


        }
    }


    private void NotConnected_showAlert() //네트워크 연결 오류 시 어플리케이션 종료
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(AfterScanActivity.this);
        builder.setTitle("네트워크 연결 오류");
        builder.setMessage("사용 가능한 무선네트워크가 없습니다.\n" + "먼저 무선네트워크 연결상태를 확인해 주세요.").setCancelable(false).setPositiveButton("확인", (dialog, id) -> { //확인 버튼 누르면 어플리케이션 종료
                    finish();
                    android.os.Process.killProcess(android.os.Process.myPid());
                });
        AlertDialog alert = builder.create();
        alert.show();

    }

    private boolean NetworkConnection() { //네트워크 연결 확인하는 코드
        int[] networkTypes = {ConnectivityManager.TYPE_MOBILE, ConnectivityManager.TYPE_WIFI};
        try {
            ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            for (int networkType : networkTypes) {
                NetworkInfo activeNetwork = manager.getActiveNetworkInfo();
                if (activeNetwork != null && activeNetwork.getType() == networkType) {
                    return true;
                }
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
    }






    private void setStringArrayPref(String key, ArrayList<setData> values) {

        SharedPreferences.Editor editor = preferences1.edit();
        JSONArray a = new JSONArray();
        for (int i = 0; i < values.size(); i++) {
            a.put(values.get(i));
        }
        if (!values.isEmpty()) {
            editor.putString(key, a.toString());
        } else {
            editor.putString(key, null);
        }
        editor.apply();
    }

    /*
    private ArrayList<setData> getStringArrayPref(Context context, String key) { // 연구중
        SharedPreferences prefs = getSharedPreferences("RecentData", 0);
        String json = prefs.getString(key, null);
        ArrayList<setData> urls = new ArrayList<setData>();
        if (json != null) {
            try {
                JSONArray a = new JSONArray(json);
                for (int i = 0; i < a.length(); i++) {
                    String url = a.optString(i);
                    urls.add(url);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return urls;
    }

    */

}
