package com.king.ar_map;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import android.app.Activity;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.route.BusRouteResult;
import com.amap.api.services.route.DriveRouteResult;
import com.amap.api.services.route.RideRouteResult;
import com.amap.api.services.route.RouteSearch;
import com.amap.api.services.route.WalkRouteResult;

import java.util.HashMap;
import java.util.Map;
//下拉框
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity  {
    MapView mMapView = null;
    //下拉框,上
    private Spinner spinner_where;
    private List<String> data_list_where;
    private ArrayAdapter<String> arr_adapter_where;
    //文本框下一
    private TextView textView_classroomid;
    private TextView TextView_sp;
    //教室号输入框
    private EditText editText_classroomnum;
    private Button but_Loc;
    private Button bt_find;

    private LatLng centerpoint= new LatLng(106.469242,29.562272);// 一教经纬度
    private AMap aMap;


    private AMapLocationClient locationClient = null;
    private AMapLocationClientOption locationOption = null;
    private String[] strMsg;

    private List<MarketBean> marketList;
    private List<String> classroom_loc=new ArrayList<String>();
    private RouteSearch routeSearch = new RouteSearch(this);
    private int flag_turn_map=-1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //初始化界面
        inti_view();

        //获取地图控件引用
        mMapView = (MapView) findViewById(R.id.map);
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mMapView.onCreate(savedInstanceState);
//        mMapView.getMap().getUiSettings().setCompassEnabled(true);
//        mMapView.getMap().getUiSettings().setMyLocationButtonEnabled(true);
        aMap = mMapView.getMap();//获取地图实例
        initLocationStyle();
//        Location();
        initLocationListener();
        addMoreMarket(0);
        inti_classroom_loc();
    }
    private void inti_classroom_loc(){
        //01-05
        classroom_loc.add("106.30129,29.594933");
        classroom_loc.add("106.301376,29.594919");
        classroom_loc.add("106.301462,29.594947");
        classroom_loc.add("106.301526,29.594933");
        classroom_loc.add("106.301483,29.595064");
        //06-10
        classroom_loc.add("106.301612,29.595036");
        classroom_loc.add("106.301692,29.595092");
        classroom_loc.add("106.301662,29.595008");
        classroom_loc.add("106.301802,29.59498");
        classroom_loc.add("106.301796,29.595115");
        //11-15
        classroom_loc.add("106.301823,29.594989");
        classroom_loc.add("106.301893,29.59512");
        classroom_loc.add("106.301968,29.594989");
        classroom_loc.add("106.302048,29.595124");
        classroom_loc.add("106.302102,29.595003");
        //16-20
        classroom_loc.add("106.302204,29.595138");
        classroom_loc.add("106.302579,29.594984");
        classroom_loc.add("106.302585,29.595143");
        classroom_loc.add("106.302708,29.594989");
        classroom_loc.add("106.302853,29.595101");
        //21-25
        classroom_loc.add("106.30296,29.594975");
        classroom_loc.add("106.302558,29.595115");
        classroom_loc.add("106.302655,29.59498");
        classroom_loc.add("106.302778,29.595124");
        classroom_loc.add("106.302966,29.595245");
        //26-30
        classroom_loc.add("106.303078,29.595124");
        classroom_loc.add("106.303186,29.594924");
        classroom_loc.add("106.303293,29.594914");
        classroom_loc.add("106.303368,29.594891");
        classroom_loc.add("106.303406,29.59491");
        //31-35
        classroom_loc.add("106.303465,29.595437");
        classroom_loc.add("106.30317,29.595381");
        classroom_loc.add("106.303255,29.595497");
        classroom_loc.add("106.303003,29.595521");
        classroom_loc.add("106.302858,29.595465");
        //36-40
        classroom_loc.add("106.302322,29.595502");
        classroom_loc.add("106.302322,29.595502");
        classroom_loc.add("106.30214,29.595427");
        classroom_loc.add("106.301952,29.595409");
        classroom_loc.add("106.301694,29.595259");
        //41-45
        classroom_loc.add("106.301764,29.595516");
        classroom_loc.add("106.301651,29.595455");
        classroom_loc.add("106.301657,29.595567");
        classroom_loc.add("106.301474,29.595502");
        classroom_loc.add("106.301507,29.595661");
        //46-50
        classroom_loc.add("106.301362,29.595511");
        classroom_loc.add("106.301383,29.595675");
        classroom_loc.add("106.301254,29.595567");
        classroom_loc.add("106.301383,29.595675");
        classroom_loc.add("106.301126,29.595619");
        //51
        classroom_loc.add("106.301083,29.595796");

    }

    /**
     * 显示定位蓝点图标
     */
    private void initLocationStyle() {
        BitmapDescriptor descriptor = BitmapDescriptorFactory.fromResource(R.drawable.dingwei2);//自定义蓝点图标
        MyLocationStyle myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.myLocationIcon(descriptor);
        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW);//连续定位、且将视角移动到地图中心点，定位蓝点跟随设备移动。（1秒1次定位）
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE);//定位一次，且将视角移动到地图中心点。
        myLocationStyle.strokeColor(getResources().getColor(R.color.colorPrimary));// 设置圆形的边框颜色
        myLocationStyle.radiusFillColor(getResources().getColor(R.color.colorPrimaryDark));// 设置圆形的填充颜色
        mMapView.getMap().setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        mMapView.getMap().getUiSettings().setMyLocationButtonEnabled(true);//设置默认定位按钮是否显示，非必需设置。
        mMapView.getMap().setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        mMapView.getMap().moveCamera(CameraUpdateFactory.zoomTo(18));
        aMap.moveCamera(CameraUpdateFactory.changeBearing(0.0f));//动画
        if(flag_turn_map==1){
            flag_turn_map=flag_turn_map*-1;
            aMap.moveCamera(CameraUpdateFactory.changeTilt(45.0f));//无动画
        }
        else{
            flag_turn_map=flag_turn_map*-1;
            aMap.moveCamera(CameraUpdateFactory.changeTilt(0.0f));//无动画
        }

    }


    /**
     * 定位的监听回调
     */
    private void initLocationListener() {
//初始化定位
        AMapLocationClient mLocationClient = new AMapLocationClient(getApplicationContext());
        //声明定位回调监听器
        AMapLocationListener mLocationListener = new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation aMapLocation) {
//                TextView_sp.setText(aMapLocation.getAddress());//文本说明
                  TextView_sp.setText(aMapLocation.getPoiName());//文本说明
//                Log.i("dddd", "aMapLocation 地址:" + aMapLocation.getAddress());
//                //获取纬度
//                Log.i("dddd", "aMapLocation==null:是否为空" + (aMapLocation == null));
//                Log.i("dddd", "获取当前定位结果来源，如网络定位结果，详见定位类型表" + aMapLocation.getLocationType());
//                Log.i("dddd", "获取经度" + aMapLocation.getLatitude());
//                Log.i("dddd", "获取纬度" + aMapLocation.getLongitude());
//                Log.i("dddd", "获取精度信息" + aMapLocation.getAccuracy());
//                Log.i("dddd", "获取高度" + aMapLocation.getAltitude());
                //定位成功回调信息，设置相关消息
//                aMapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
//                aMapLocation.getLatitude();//获取经度
//                aMapLocation.getLongitude();//获取纬度;
//                aMapLocation.getAccuracy();//获取精度信息
//                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                Date date = new Date(aMapLocation.getTime());
//                df.format(date);//定位时间
//                aMapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果
//                aMapLocation.getCountry();//国家信息
//                aMapLocation.getProvince();//省信息
//                aMapLocation.getCity();//城市信息
//                aMapLocation.getDistrict();//城区信息
//                aMapLocation.getRoad();//街道信息
//                aMapLocation.getCityCode();//城市编码
//                aMapLocation.getAdCode();//地区编码
                if (aMapLocation != null) {
                    if (aMapLocation.getErrorCode() == 0) {
//可在其中解析amapLocation获取相应内容。
//                        Log.i("dddd", "aMapLocation:" + aMapLocation.getPoiName());
                        double current_latitude = aMapLocation.getLatitude();
                        double current_longitude = aMapLocation.getLongitude();
                    } else {
                        //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                        Log.i("dddd", "location Error, ErrCode:"
                                + aMapLocation.getErrorCode() + ", errInfo:"
                                + aMapLocation.getErrorInfo());
                    }
                }
            }
        };
//设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
//启动定位
        mLocationClient.startLocation();
    }

    private void addMoreMarket(int num) {
        if (marketList == null) {
            marketList = new ArrayList<>();
        }
//        //模拟6个假数据
//        marketList.add(new MarketBean(29.595695f, 106.301057f, "D1200", " "));
//        marketList.add(new MarketBean(29.595695f, 106.301057f, "D1201", " "));
//        marketList.add(new MarketBean(22.694, 114.099, "标题2", "内容2"));
//        marketList.add(new MarketBean(22.667, 114.041, "标题3", "内容3"));
//        marketList.add(new MarketBean(22.647, 114.023, "标题4", "内容4"));
//        marketList.add(new MarketBean(22.688, 114.066, "标题5", "内容5"));
//        marketList.add(new MarketBean(22.635, 114.077, "标题6", "内容6"));
//
//        int i=num;
//        aMap.addMarker(new MarkerOptions().anchor(1.0f, 1.0f)
//                .alpha(0.5f)
//                .position(new LatLng(marketList.get(i).getLatitude(),//设置纬度
//                        marketList.get(i).getLongitude()))//设置经度
//                .title(marketList.get(i).getTitle())//设置标题
//                .snippet(marketList.get(i).getContent())//设置内容
//                // .setFlat(true) // 将Marker设置为贴地显示，可以双指下拉地图查看效果
//                .draggable(true) //设置Marker可拖动
//                .icon(BitmapDescriptorFactory.fromResource(R.drawable.dingwei)).infoWindowEnable(true)
//        );
        //设置自定义弹窗
        aMap.setInfoWindowAdapter(new WindowAdapter(this));
        //绑定信息窗点击事件
        aMap.setOnInfoWindowClickListener(new WindowAdapter(this));
        aMap.setOnMarkerClickListener(new WindowAdapter(this));
    }

    private void inti_view(){
        editText_classroomnum=findViewById(R.id.editText_classroomnum);
        //监听回车确认键
        editText_classroomnum.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

                //Toast.LENGTH_LONG表示显示时间长  Toast.LENGTH_SHORT表示显示时间短
                Log.i("dddd",editText_classroomnum.getText()+"");
                addMoreMarket(0);
                String classroomnum=textView_classroomid.getText().toString()+editText_classroomnum.getText().toString();
                String tmep=editText_classroomnum.getText().toString().substring(1,3);
                int classroom_num=Integer.parseInt(tmep);
                if(classroom_num>51 || classroom_num<1){
                    return false;
                }
                Log.i("dddd",classroom_num+"");
                String xys=classroom_loc.get(classroom_num-1);
                String[] xy=classroom_loc.get(classroom_num-1).split(",");
                float a=Float.parseFloat(xy[1]);
                float b=Float.parseFloat(xy[0]);

                double sa=aMap.getMyLocation().getLatitude();
                double sb=aMap.getMyLocation().getLongitude();
                LatLng start = new LatLng(sa, sb);
                LatLng end = new LatLng(a, b);
                float distance  = AMapUtils.calculateLineDistance(start, end);

                MarkerOptions markerOption = new MarkerOptions();
                markerOption.title(classroomnum).snippet(distance+" m");//标题+文字表述
                markerOption.draggable(true);//设置Marker可拖动
                markerOption.position(end);
                markerOption.icon(BitmapDescriptorFactory.fromResource(R.drawable.dingwei));
                //设置覆盖物比例
                markerOption.alpha(0.5f);
//                markerOption.setFlat(true);
                markerOption.anchor(1.0f, 1.0f);
                aMap.clear();
                Marker marker = mMapView.getMap().addMarker(markerOption);
                marker.showInfoWindow();

                movelocation(1);

                return true;
            }
        });
        //文本框
        TextView_sp=(TextView) findViewById(R.id.textView_sp);
        TextView_sp.setText("hPa");

        textView_classroomid=(TextView) findViewById(R.id.textView_classroomid);
        editText_classroomnum=(EditText) findViewById(R.id.editText_classroomnum);
        ///////////////////////////////////////////
        //下拉框1
        spinner_where = (Spinner) findViewById(R.id.spinner_where);
        //数据
        data_list_where = new ArrayList<String>();
        data_list_where.add("select building <..>");
        data_list_where.add("First school building（D1）");
        data_list_where.add("Comprehensive building（DZ）");
        //适配器
        arr_adapter_where= new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, data_list_where);
        //设置样式
        arr_adapter_where.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        spinner_where.setAdapter(arr_adapter_where);

        spinner_where.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {//选择item的选择点击监听事件
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
                // 将所选mySpinner 的值带入myTextView 中
//                 (String) spinner_where.getSelectedItem();
//                TextView_sp.setText("您选择的是：" + arg2+"个");//文本说明
                String[] crid = new String[]{"","D1","DZ"};
                if(arg2==0)
                {
                    textView_classroomid.setText(crid[arg2]);
                    editText_classroomnum.setVisibility(View.INVISIBLE);//view1处于不可见状态


                }
                if(arg2==1)
                {
                    textView_classroomid.setText(crid[arg2]);
                    editText_classroomnum.setVisibility(View.VISIBLE);//view1处于不可见状态

                    movelocation(arg2);

                }
                if(arg2==2)
                {
                    textView_classroomid.setText(crid[arg2]);
                    editText_classroomnum.setVisibility(View.VISIBLE);//view1处于不可见状态

                    movelocation(arg2);

                }
            }
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
                textView_classroomid.setText("");
            }
        });
        but_Loc = (Button) findViewById(R.id.but_Loc);

        //设置Button监听
        but_Loc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initLocationStyle();
                Log.i("dddd",""+aMap.getCameraPosition().target+""+aMap.getCameraPosition().zoom+"f,"+aMap.getCameraPosition().tilt+"f,"+aMap.getCameraPosition().bearing+"f");
            }
        });
        bt_find = (Button) findViewById(R.id.bt_find);
        bt_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(aMap.getMapScreenMarkers().get(0).getPosition().latitude==+aMap.getMyLocation().getLatitude()){
                    return;
                }
                Intent i1 = new Intent();
                String url="baidumap://map/walknavi?origin="
                        +aMap.getMyLocation().getLatitude()+","
                        +aMap.getMyLocation().getLongitude()
                        +"&destination="
                        +aMap.getMapScreenMarkers().get(0).getPosition().latitude+","
                        +aMap.getMapScreenMarkers().get(0).getPosition().longitude
                        +"&coord_type=wgs84&src=andr.baidu.openAPIdemo";
                Log.i("dddd",url);
                i1.setData(Uri.parse(url));
                startActivity(i1);


//                //当前经纬度
//                LatLonPoint pointFrom = new LatLonPoint(aMap.getMyLocation().getLatitude(),aMap.getMyLocation().getLongitude());
//                //目的地的经纬度
//                LatLonPoint pointTo = new LatLonPoint(aMap.getMapScreenMarkers().get(0).getPosition().latitude, aMap.getMapScreenMarkers().get(0).getPosition().longitude);
//
//                RouteSearch.FromAndTo fromAndTo = new RouteSearch.FromAndTo(pointFrom, pointTo);
//                //初始化query对象，fromAndTo是包含起终点信息，walkMode是步行路径规划的模式 mode，计算路径的模式。SDK提供两种模式：RouteSearch.WALK_DEFAULT 和 RouteSearch.WALK_MULTI_PATH。
//                RouteSearch.WalkRouteQuery query = new RouteSearch.WalkRouteQuery(fromAndTo, RouteSearch.WALK_DEFAULT);
//                routeSearch.calculateWalkRouteAsyn(query);//开始算路
//                routeSearch.setRouteSearchListener(new RouteSearch.OnRouteSearchListener() {
//                    @Override
//                    public void onBusRouteSearched(BusRouteResult busRouteResult, int i) {
//
//                    }
//
//                    @Override
//                    public void onDriveRouteSearched(DriveRouteResult driveRouteResult, int i) {
//
//                    }
//
//                    @Override
//                    public void onWalkRouteSearched(WalkRouteResult walkRouteResult, int i) {
////                        if (i == 1000) {//1000代表成功
////                            //在地图上绘制路径：
////                            WalkRouteOverlay walkRouteOverlay = new WalkRouteOverlay(getBaseContext(), mMapView.getMap(), walkRouteResult.getPaths().get(0), walkRouteResult.getStartPos(), walkRouteResult.getTargetPos());
//////                            walkRouteOverlay.setNodeIconVisibility(false);
//////                    mMapView.getMap().clear();
//////                            walkRouteOverlay.removeFromMap();
////                            walkRouteOverlay.addToMap();//将Overlay添加到地图上显示
//////                            walkRouteOverlay.zoomToSpan();//调整地图能看到起点和终点
//////                            lastWalkRouteOverlay = walkRouteOverlay;
////                        }
//                    }
//
//
//                    @Override
//                    public void onRideRouteSearched(RideRouteResult rideRouteResult, int i) {
//
//                    }
//                });
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
            }
        });

    }




    private void movelocation(int flag){
        // 2019-03-26 03:51:29.975 14483-14483/com.king.ar_map I/dddd: 倾斜59.641415缩放18.749186旋转96.26513经纬lat/lng: (29.595224939673244,106.30220305858607)
        if(flag==0){
            float new_position[]={17.411993f,0.0f,0.0f};
            aMap.moveCamera(CameraUpdateFactory.changeTilt(new_position[3]));//无动画
            aMap.animateCamera(CameraUpdateFactory.changeBearing(new_position[4]));//动画
        }
        if(flag==1){
            float new_position[]={29.595224939673244f,106.30220305858607f,18.749186f,59.641415f,96.26513f};//一教
            LatLng latLng = new LatLng(new_position[0],new_position[1]);//构造一个位置
            aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,new_position[2]));
            aMap.moveCamera(CameraUpdateFactory.changeTilt(new_position[3]));//无动画
            aMap.animateCamera(CameraUpdateFactory.changeBearing(new_position[4]));//动画
        }
        else if(flag==2){//综合楼
//            2019-03-26 04:10:06.768 18580-18580/com.king.ar_map I/dddd: 倾斜63.497784缩放19.184656旋转293.72305经纬lat/lng: (29.595776521795063,106.29937064591479)
            float new_position[]={29.595776521795063f,106.29937064591479f,18.749186f,59.641415f,293.72305f};
            LatLng latLng = new LatLng(new_position[0],new_position[1]);//构造一个位置
            aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,new_position[2]));
            aMap.moveCamera(CameraUpdateFactory.changeTilt(new_position[3]));//无动画
            aMap.animateCamera(CameraUpdateFactory.changeBearing(new_position[4]));//动画
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();
    }
    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }


}
