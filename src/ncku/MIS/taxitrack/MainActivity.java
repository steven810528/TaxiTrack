package ncku.MIS.taxitrack;

//import java.util.Timer;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
//import android.graphics.Color;
//import android.location.Address;
import android.location.Criteria;
//import android.location.Geocoder;
import android.location.LocationListener;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;
//import java.io.*;

public class MainActivity extends Activity implements LocationListener {

	//宣告所需的物件
	private Button getCar;
	private Button stopCar;
	private Button showLoc;
	private TextView dollarLabel;
	private TextView meterLabel;
	private TextView statusLabel;
	private TextView gitLabel;
	private TextView titLabel;
	private boolean getService = false;	
	private int unitTime = 1000;		//GPS更新頻率，千分之一秒
	private boolean isShowLocation = true;
	
	Double longitude ;
	Double latitude ;	
	
	//LinearLayout background= (LinearLayout)findViewById(R.id.back);;
	
	private double distance;
	private int money; 
	//處理位置間距離的類別
	private TrackDistance td =new TrackDistance();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//物件和View連結
		getCar= (Button)findViewById(R.id.button1);
		stopCar= (Button)findViewById(R.id.button2);
		showLoc=(Button)findViewById(R.id.button3);
		
		dollarLabel=(TextView)findViewById(R.id.tvd);
		meterLabel=(TextView)findViewById(R.id.tvm);
		
		statusLabel=(TextView)findViewById(R.id.Tvs);
		//gitLabel = (TextView)findViewById(R.id.git);
		//titLabel=(TextView)findViewById(R.id.tit);
	
		getCar.setOnClickListener(handle);
		stopCar.setOnClickListener(handle);
		showLoc.setOnClickListener(handle);
		
		testLocationProvider();		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	@Override
	
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	/*
	private void Start()
	{
		
		//td.isActive=true;
		//td.total=0;
		

		//distance=td.total;
		//meterLabel.setText(Integer.toString(distance));
		//compute(distance);
		
		//titLabel.setText(Double.toString(td.longitude));
		//gitLabel.setText(Double.toString(td.latitude));
		//statusLabel.setText(Boolean.toString(td.isActive));
	}
	private void Stop()
	{
		//td.isActive=false;
		
		//statusLabel.setText(Boolean.toString(td.isActive));
	}
	*/
	//事件處理者
	private Button.OnClickListener handle = new Button.OnClickListener()
	{
		public void onClick(View v)
		{
			
			switch(v.getId())
			{
			case R.id.button1:
				//Start();
				td.isActive=true;
				showValue();
				td.setStart();
				//background.setBackgroundColor(Color.BLUE);
				
				break;
			case R.id.button2:
				//Stop();
				td.isActive=false;
				td.setEnd();
				showValue();
				showDialog();
				td.total=0;
				//background.setBackgroundColor(Color.GRAY);
				
				break;
				
			case R.id.button3:
				showLocDialog();
				break;
					
			}
		}
	};
	//計算票價,並顯示
	public void compute(double m)
	{
		
		if(m<=1250)
		{
			money=70;
		}
		else
		{
			double tmp =Math.ceil((m-1250)/250);
			money=(int)tmp*5+70;
		}
		dollarLabel.setText(Integer.toString((int)money));;
	}
	//更新所顯示的資訊
	public void showValue()
	{
		
		statusLabel.setText(Boolean.toString(td.isActive));
		distance=td.total;
		meterLabel.setText(Double.toString(distance));
		compute(distance);
	}
	//下車總結訊息視窗
	public void showDialog()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);	//main是class name
		builder.setTitle("乘車資訊總結");
		builder.setMessage("總距離為"+Double.toString(distance)+"公尺\n總金額為"+Integer.toString(money)+"元整\n總時間為"+td.totalTime+"秒\n平均速率為"+td.mpers+"m/s\n是否有繞路："+!td.isEffect);
		builder.show();
	}
	public void showLocDialog()
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);	//main是class name
		builder.setTitle("當前位置座標");
		builder.setMessage("經度為"+Double.toString(longitude)+"\n緯度為"+Double.toString(latitude));
		builder.show();
	}
	//GPS功能==========================================================================
	public void testLocationProvider() {
        
        LocationManager status = (LocationManager) (this.getSystemService(Context.LOCATION_SERVICE));
        if (status.isProviderEnabled(LocationManager.GPS_PROVIDER) || status.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
        	
        	getService = true;	
        	locationServiceInitial();
        } else {
        	Toast.makeText(this, "無法取得gps功能", Toast.LENGTH_LONG).show();
        	startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));	
        }
    }
    
    private LocationManager lms;
    private String bestProvider = LocationManager.GPS_PROVIDER;	
    private void locationServiceInitial() {
    	lms = (LocationManager) getSystemService(LOCATION_SERVICE);	
    	Criteria criteria = new Criteria();	
    	bestProvider = lms.getBestProvider(criteria, true);	
    	Location location = lms.getLastKnownLocation(bestProvider);
    	getLocation(location);
    }

    private void getLocation(Location location) {	
    	if(location != null) {
    		//TextView longitude_txt = (TextView) findViewById(R.id.git);
    		//TextView latitude_txt = (TextView) findViewById(R.id.tit);
    		//TextView address_txt = (TextView) findViewById(R.id.address);
   		
    		longitude = location.getLongitude();	
    		latitude = location.getLatitude();	
    		/*
    		if(isShowLocation==true)
    		{
    			gitLabel.setText(String.valueOf(longitude));
    			titLabel.setText(String.valueOf(latitude));
    		}
    		else 
    		{
    			gitLabel.setText("");
    			titLabel.setText("");
    		}
    		*/
    		//address_txt.setText(getAddressByLocation(location));
    		td.setLocation(location);
    		distance=td.total;
			meterLabel.setText(Double.toString(distance));
			compute(distance);
    	}
    	else {
    		Toast.makeText(this, "無法可能gps功能", Toast.LENGTH_LONG).show();
    	}	
    }    
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(getService) {
			lms.requestLocationUpdates(bestProvider, unitTime, 1, this);
			
		}
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if(getService) {
			lms.removeUpdates(this);	
		}
	}

	@Override
	protected void onRestart() {	
		// TODO Auto-generated method stub
		super.onRestart();
		testLocationProvider();
	}

	@Override
	public void onLocationChanged(Location location) {	
		// TODO Auto-generated method stub
		getLocation(location);
	}

	@Override
	public void onProviderDisabled(String arg0) {	
		// TODO Auto-generated method stub
	}

	@Override
	public void onProviderEnabled(String arg0) {	
		// TODO Auto-generated method stub
	}

	@Override
	public void onStatusChanged(String arg0, int arg1, Bundle arg2) {	
		// TODO Auto-generated method stub
	}
	/*
	public String getAddressByLocation(Location location) {
		String returnAddress = "";
		try {
				if (location != null) {
		    		Double longitude = location.getLongitude();	
		    		Double latitude = location.getLatitude();	

		    		
		    		Geocoder gc = new Geocoder(this, Locale.TRADITIONAL_CHINESE);	
		    		List<Address> lstAddress = gc.getFromLocation(latitude, longitude, 1);

		    	//	if (!Geocoder.isPresent()){ //Since: API Level 9
		    	//		returnAddress = "Sorry! Geocoder service not Present.";
		    	//	}
		    		returnAddress = lstAddress.get(0).getAddressLine(0);
				}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		return returnAddress;
	}
	*/
	
	
	
}
