package ncku.MIS.taxitrack;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
//import android.content.Context;
import android.location.Location;
//import android.location.LocationListener;
//import android.location.LocationManager;
import android.os.Bundle;


public class TrackDistance extends Activity {
	
	public double total;
	double distanceOf2;
	double directDistance;
	static Location a;
	static Location b;
	static Location from;
	static Location to;
	public boolean isActive =false;
	public int totalTime; 
	Date startDate = new Date();
	Date endDate = new Date();
	
	boolean isEffect = true;
	double mpers = 0;
	
	//靜態變數
	static private final double EARTH_RADIUS = 6378137.0;
	public void setLocation(Location l)
	{
		if(a==null&&b==null)
		{
			a=l;
			from=l;
		}
		else if(a!=null&&b==null)
		{
			b=l;
		}
		else
		{
			a=b;
			b=l;
			gps2m(a,b);
		}
		
		
	}
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		
	}
	public void setLocTo()
	{
		to=b;
		directDistance=direct2p(from,to);
		double a = Math.pow(directDistance,2);
		if(total>a*0.1)
			isEffect=false;
		else
			isEffect=true;
	}
	
	public void setStart()
	{
		startDate = new java.util.Date();
	}
	public void setEnd()
	{
		endDate = new java.util.Date();
		totalTime = (int)TimeUnit.MILLISECONDS.toSeconds(endDate.getTime() - startDate.getTime());
		if(totalTime!=0)
		mpers=total/totalTime;
	}
	private void gps2m(Location l1,Location l2)
	{
		double lat_a=l1.getLatitude();
		double lng_a=l1.getLongitude();
		double lat_b=l2.getLatitude();
		double lng_b=l2.getLongitude();
		
		double radLat1 = (lat_a * Math.PI / 180.0);
		double radLat2 = (lat_b * Math.PI / 180.0);
		double a = radLat1 - radLat2;
		double b = (lng_a - lng_b) * Math.PI / 180.0;
		double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
		+ Math.cos(radLat1) * Math.cos(radLat2)
		* Math.pow(Math.sin(b / 2), 2)));
		s = s * EARTH_RADIUS;
		s = Math.round(s * 10000) / 10000;
		if(isActive==true)
		total+=s;
	}
	
	private double direct2p(Location l1,Location l2) 
	{
		double lat_a=l1.getLatitude();
		double lng_a=l1.getLongitude();
		double lat_b=l2.getLatitude();
		double lng_b=l2.getLongitude();
		
		double radLat1 = (lat_a * Math.PI / 180.0);
		double radLat2 = (lat_b * Math.PI / 180.0);
		double a = radLat1 - radLat2;
		double b = (lng_a - lng_b) * Math.PI / 180.0;
		double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2)
		+ Math.cos(radLat1) * Math.cos(radLat2)
		* Math.pow(Math.sin(b / 2), 2)));
		s = s * EARTH_RADIUS;
		s = Math.round(s * 10000) / 10000;
		return s;
	}
	
	
}
