package com.example.helloworld.Activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.example.helloworld.Bean.PostBean;
import com.example.helloworld.Bean.RequestBean;
import com.example.helloworld.Bean.URLBean;
import com.example.helloworld.InternetInterface.FeedbackRequestInterface;
import com.example.helloworld.InternetInterface.ImageURLRequestInterface;
import com.example.helloworld.InternetInterface.PostInterface;
import com.example.helloworld.R;
import com.example.helloworld.adapter.BitmapAdapter;
import com.example.helloworld.config.Constract;
import com.example.helloworld.entity.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.phz.photopicker.config.ImagePickerConstract;
import com.phz.photopicker.config.SelectMode;
import com.phz.photopicker.intent.PickImageIntent;
import com.phz.photopicker.intent.PreViewImageIntent;
import com.phz.photopicker.util.UsageUtil;
import com.phz.photopicker.view.MyGridView;

import org.springframework.mock.web.MockMultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Observable;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class PhotoActivity extends AppCompatActivity {

    private MyGridView myGridView;

    private Context mContext;

    private static final int ACTION_REQUEST_PERMISSIONS = 0x001;
    /**
     * 需要的权限
     * 本来读写权限获取其中一只另外一个就也获取了（一个权限组的），随着安卓系统更新，只获取读不行了。
     */
    private static final String[] NEEDED_PERMISSIONS = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE};


    private int gridViewItemClickPosition;
    private String gridViewItemClickPath;

    /**
     * 允许上传照片最大数量
     */
    private static final int INT_MAXSIZE_IMG = 2;

    /**
     * 图片路径，和graidview的填充器有关 (可能包含plus加号)
     */
    private ArrayList<String> imagePathsList = new ArrayList<>();
    /**
     * 图片路径，不包含plus
     */
    private ArrayList<String> imagePathsListNew = new ArrayList<>();
    /**
     * 和gridView的填充器有关的填充器
     */
    private BitmapAdapter adapter;
    private Button submit_btn;
    private Button location;
    private String addr;
    private TextView title_tv,desc_tv;
    public LocationClient mLocationClient;
    TextView locationInfo;
    private ArrayList<RadioButton> qryType=new ArrayList<RadioButton>();
    private ArrayList<RadioButton> degree=new ArrayList<RadioButton>();

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //设置状态栏颜色
        Window window = getWindow();
        //After LOLLIPOP not translucent status bar
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //Then call setStatusBarColor.
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.parseColor("#FFFFFF"));
        //设置状态栏字体颜色
        changeStatusBarTextColor(true);

        //设置标题栏不可见
        if (getSupportActionBar() != null){
            getSupportActionBar().hide();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo);
        mContext = this;
        myGridView = findViewById(R.id.addPhoto);
        submit_btn=findViewById(R.id.submit);
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    getImageUrl();
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        title_tv=findViewById(R.id.photoTitle);
        desc_tv=findViewById(R.id.description);
        qryType.add(0,findViewById(R.id.questionTypeButton1));
        qryType.add(1,findViewById(R.id.questionTypeButton2));
        qryType.add(2,findViewById(R.id.questionTypeButton3));
        degree.add(0,findViewById(R.id.degreeButton1));
        degree.add(1,findViewById(R.id.degreeButton2));
        initData();

        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(new MyLocationListener());
        locationInfo = findViewById(R.id.locationtext);
        location = findViewById(R.id.locationBtn);
        location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationInfo.setText("位置获取中...");
                List<String> permissionList = new ArrayList<String>();

                if(ContextCompat.checkSelfPermission(PhotoActivity.this,Manifest.permission.ACCESS_FINE_LOCATION)!=PackageManager.PERMISSION_GRANTED){
                    permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
                }
                if(ContextCompat.checkSelfPermission(PhotoActivity.this,Manifest.permission.READ_PHONE_STATE)!=PackageManager.PERMISSION_GRANTED){
                    permissionList.add(Manifest.permission.READ_PHONE_STATE);
                }
                if(ContextCompat.checkSelfPermission(PhotoActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
                    permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
                }
                if(!permissionList.isEmpty()){
                    String [] permissions = permissionList.toArray(new String[permissionList.size()]);
                    ActivityCompat.requestPermissions(PhotoActivity.this,permissions,2);
                }
                else{
                    requestLocation();
                }
            }
        });
    }

    private void requestLocation(){
        initLocation();
        mLocationClient.start();
    }
    private void initLocation(){
        LocationClientOption option = new LocationClientOption();

        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        //可选，设置定位模式，默认高精度
        //LocationMode.Hight_Accuracy：高精度；
        //LocationMode. Battery_Saving：低功耗；
        //LocationMode. Device_Sensors：仅使用设备；

        option.setCoorType("bd09ll");
        //可选，设置返回经纬度坐标类型，默认GCJ02
        //GCJ02：国测局坐标；
        //BD09ll：百度经纬度坐标；
        //BD09：百度墨卡托坐标；
        //海外地区定位，无需设置坐标类型，统一返回WGS84类型坐标

        option.setScanSpan(1000);
        //可选，设置发起定位请求的间隔，int类型，单位ms
        //如果设置为0，则代表单次定位，即仅定位一次，默认为0
        //如果设置非0，需设置1000ms以上才有效

        option.setOpenGps(true);
        //可选，设置是否使用gps，默认false
        //使用高精度和仅用设备两种定位模式的，参数必须设置为true

        option.setLocationNotify(true);
        //可选，设置是否当GPS有效时按照1S/1次频率输出GPS结果，默认false

        option.setIgnoreKillProcess(false);
        //可选，定位SDK内部是一个service，并放到了独立进程。
        //设置是否在stop的时候杀死这个进程，默认（建议）不杀死，即setIgnoreKillProcess(true)

        option.SetIgnoreCacheException(false);
        //可选，设置是否收集Crash信息，默认收集，即参数为false

        option.setWifiCacheTimeOut(5*60*1000);
        //可选，V7.2版本新增能力
        //如果设置了该接口，首次启动定位时，会先判断当前Wi-Fi是否超出有效期，若超出有效期，会先重新扫描Wi-Fi，然后定位

        option.setEnableSimulateGps(false);
        //可选，设置是否需要过滤GPS仿真结果，默认需要，即参数为false

        option.setNeedNewVersionRgc(true);
        //可选，设置是否需要最新版本的地址信息。默认需要，即参数为true

        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);
    }

    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location){
            //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
            //以下只列举部分获取地址相关的结果信息
            //更多结果信息获取说明，请参照类参考中BDLocation类中的说明

            addr = location.getAddrStr();    //获取详细地址信息
            locationInfo.setText(addr);
            /*Toast.makeText(mContext,addr,Toast.LENGTH_SHORT).show();*/
        }
    }

    public boolean getNewsData(PostBean postBean,String ImageUrl) {
        postBean.setAccount(User.user.getAccount());
        postBean.setDesc(desc_tv.getText().toString());
        postBean.setTitle(title_tv.getText().toString());
        if(addr!=null){
            postBean.setAddress(addr);
        }
        else{
            postBean.setAddress("未知区域");
        }
        postBean.setImageUrl(ImageUrl);
        postBean.setProcess("已提交");
//        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        postBean.setTime(new Date());
        int index = -1;
        for(int i=0;i<qryType.size();i++) {
            if(qryType.get(i).isChecked()) {
                postBean.setCategory(qryType.get(i).getText().toString());
                index = i;
                break;
            }
        }
        if(index == -1){
            Toast.makeText(PhotoActivity.this,"请选择问题类型！",Toast.LENGTH_SHORT).show();
            return false;
        }
        index = -1;
        for(int i=0;i<degree.size();i++){
            if(degree.get(i).isChecked()){
                postBean.setDegree(1-i);
                index = i;
            }
        }
        if(index == -1){
            Toast.makeText(PhotoActivity.this,"请选择问题程度！",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void postNews(String ImageUrl){
        PostBean postBean=new PostBean();
        if(getNewsData(postBean,ImageUrl)) {
            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX").create();
            String feedBack = gson.toJson(postBean);//通过Gson将Bean转化为Json字符串形式
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(Constract.BASEURL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .build();
            PostInterface postInterface = retrofit.create(PostInterface.class);
            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), feedBack);
        /*Call<RequestBean> call=postInterface.postNews(body);
        call.enqueue(new Callback<RequestBean>() {
            @Override
            public void onResponse(Call<RequestBean> call, Response<RequestBean> response) {
                RequestBean requestBean = response.body();
                if(response.body().getCode()==Constract.REQUESTSUCCESS){
                    Toast.makeText(PhotoActivity.this,"上传成功",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(PhotoActivity.this,"上传失败",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RequestBean> call, Throwable t) {
                Toast.makeText(PhotoActivity.this,"上传失败",Toast.LENGTH_SHORT).show();
            }
        });*/
            Call<RequestBean> repos = postInterface.postNews(body);
            Runnable r = () -> {
                try {
                    RequestBean evaBean = repos.execute().body();
                    runOnUiThread(() -> {
                        if (evaBean.getCode() == 200) {
                            Toast.makeText(PhotoActivity.this, "发布成功！", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(PhotoActivity.this, "诶呀，数据好像出错了！", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            };
            Thread thread = new Thread(r);
            thread.start();
        }
    }

    public void getImageUrl() throws IOException {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constract.BASEURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        ImageURLRequestInterface imageURLRequestInterface = retrofit.create(ImageURLRequestInterface.class);

        if(imagePathsList.size()>1) {
            File image = new File(imagePathsList.get(0));
            MultipartBody.Part part = MultipartBody.Part.createFormData("file", image.getName(), RequestBody.create(MediaType.parse("file/image/upload"), image));
            Call<URLBean> call = imageURLRequestInterface.getURL(part);
            call.enqueue(new Callback<URLBean>() {
                @Override
                public void onResponse(Call<URLBean> call, Response<URLBean> response) {
                    if (response.body().getCode() == Constract.REQUESTSUCCESS) {
                        /* Toast.makeText(PhotoActivity.this,response.body().getData(),Toast.LENGTH_SHORT).show();*/
                        postNews(response.body().getData());
                    }
                }

                @Override
                public void onFailure(Call<URLBean> call, Throwable t) {

                    Toast.makeText(PhotoActivity.this, "上传失败，", Toast.LENGTH_SHORT).show();

                }
            });
        }
        else{
            postNews(" ");
        }
    }

    public void changeStatusBarTextColor(boolean b){
        if(b) getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        else getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
    }

    private void initData() {
        int cols = UsageUtil.getNumColnums(this);
        myGridView.setNumColumns(cols);
        imagePathsList.add(Constract.PLUS);
        adapter = new BitmapAdapter(imagePathsList, this);
        myGridView.setAdapter(adapter);
        myGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!PhotoActivity.this.checkPermissions(NEEDED_PERMISSIONS)) {
                    gridViewItemClickPosition = position;
                    gridViewItemClickPath = (String) parent.getItemAtPosition(position);
                    /**
                     * 请求一串权限
                     */
                    ActivityCompat.requestPermissions(PhotoActivity.this, NEEDED_PERMISSIONS, ACTION_REQUEST_PERMISSIONS);
                } else {
                    String string = (String) parent.getItemAtPosition(position);
                    PhotoActivity.this.toPickPhoto(position, string);
                }

            }
        });
    }

    /**
     * 跳转到图片选择器
     */
    private void toPickPhoto(int position, String string) {
        if (Constract.PLUS.equals(string)) {
            PickImageIntent intent = new PickImageIntent(mContext);
            //设置为多选模式
            intent.setSelectModel(SelectMode.MULTI);
            // 是否拍照
            intent.setIsShowCamera(true);
            //设置最多选择照片数量
            if (imagePathsList.size() > 0 && imagePathsList.size() < (INT_MAXSIZE_IMG + 1)) {
                // 最多选择照片数量
                intent.setSelectedCount(INT_MAXSIZE_IMG + 1 - imagePathsList.size());
            } else {
                intent.setSelectedCount(0);
            }
            /*// 已选中的照片地址， 用于回显选中状态
            intent.setSelectedPaths(imagePathsList);*/
            startActivityForResult(intent, REQUEST_CAMERA_CODE);
        } else {
            PreViewImageIntent intent = new PreViewImageIntent(mContext);
            intent.setCurrentItem(position);
            if (imagePathsList.contains(Constract.PLUS)) {
                imagePathsList.remove(Constract.PLUS);
            }
            intent.setPhotoPaths(imagePathsList);
            startActivityForResult(intent, REQUEST_PREVIEW_CODE);
        }
    }

    private static final int REQUEST_CAMERA_CODE = 10;
    private static final int REQUEST_PREVIEW_CODE = 20;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CAMERA_CODE:
                    if (data != null) {
                        ArrayList<String> list = data.getStringArrayListExtra(ImagePickerConstract.EXTRA_RESULT);
                        updateGridView(list);
                    }
                    break;
                case REQUEST_PREVIEW_CODE:
                    if (data != null) {
                        ArrayList<String> ListExtra = data.getStringArrayListExtra(ImagePickerConstract.EXTRA_RESULT);
                        if (imagePathsList != null) {
                            imagePathsList.clear();
                        }
                        imagePathsList.addAll(ListExtra);
                        if (imagePathsList.size() < INT_MAXSIZE_IMG) {
                            imagePathsList.add(Constract.PLUS);
                        }
                        adapter = new BitmapAdapter(imagePathsList, mContext);
                        myGridView.setAdapter(adapter);
                    }
                    break;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == ACTION_REQUEST_PERMISSIONS) {
            boolean isAllGranted = true;
            for (int grantResult : grantResults) {
                isAllGranted &= (grantResult == PackageManager.PERMISSION_GRANTED);
            }
            if (isAllGranted) {
                //全部获取了
                toPickPhoto(gridViewItemClickPosition, gridViewItemClickPath);
            } else {
                //至少有一个被拒绝
                Toast.makeText(mContext, "为确保应用正常运行，请允许相机和读取权限。", Toast.LENGTH_LONG).show();
            }
        }
        else if(requestCode == 2){
            if(grantResults.length>0){
                for (int result : grantResults){
                    if(result != PackageManager.PERMISSION_GRANTED){
                        Toast.makeText(mContext, "为确保应用正常运行，请允许位置权限。", Toast.LENGTH_LONG).show();
                        finish();
                        return;
                    }
                }
                requestLocation();
            }
            else{
                Toast.makeText(mContext, "发生未知异常!", Toast.LENGTH_LONG).show();
                finish();
            }
        }
    }

    /**
     * 遍历判断权限是否都有请求
     * @param neededPermissions
     * @return
     */
    private boolean checkPermissions(String[] neededPermissions) {
        if (neededPermissions == null || neededPermissions.length == 0) {
            return true;
        }
        boolean allGranted = true;
        for (String neededPermission : neededPermissions) {
            allGranted &= ContextCompat.checkSelfPermission(this, neededPermission) == PackageManager.PERMISSION_GRANTED;
        }
        return allGranted;
    }


    /**
     * 更新界面
     *
     * @param list 选择照片的路径列表
     */
    private void updateGridView(ArrayList<String> list) {
        if (imagePathsList.contains(Constract.PLUS)) {
            imagePathsList.remove(Constract.PLUS);
        }
        imagePathsList.addAll(list);
        /** 小于INT_MAXSIZE_IMG时显示添加图片item(也就是plus)*/
        if (imagePathsList.size() < INT_MAXSIZE_IMG) {
            imagePathsList.add(Constract.PLUS);
        }
        adapter = new BitmapAdapter(imagePathsList, mContext);
        myGridView.setAdapter(adapter);
    }
}