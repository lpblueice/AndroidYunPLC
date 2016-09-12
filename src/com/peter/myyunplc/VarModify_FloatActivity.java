package com.peter.myyunplc;

import java.util.HashMap;

import com.peter.grm.GrmData;
import com.peter.http.HttpRequest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class VarModify_FloatActivity extends Activity
{
	
	
	Button btn_FloatOK=null;
	TextView txtview_varValue=null;
	TextView txtview_varName=null;
	TextView txtview_varDes=null;
	TextView txt_varFloatVauleModify=null;
	
	GrmData grmData =null;
	String grmEquAddr = null;
	String grmEquSID = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
        super.onCreate(savedInstanceState);
		setTitle("数据修改界面");
		
		setContentView(R.layout.activity_varmodify_float);         

    	btn_FloatOK=(Button)findViewById(R.id.btn_FloatOK);
    	txtview_varValue=(TextView)findViewById(R.id.txtview_varValue);
    	txtview_varName=(TextView)findViewById(R.id.txtview_varName);
    	txtview_varDes=(TextView)findViewById(R.id.txtview_varDes);
    	txt_varFloatVauleModify=(TextView)findViewById(R.id.txt_varFloatVauleModify);
		
		grmEquAddr = (String)getIntent().getSerializableExtra("GrmEquAddr");
		if(grmEquAddr == null)
		{
			try {
				Toast.makeText(getApplicationContext(), "Bundle grmData is null!", Toast.LENGTH_LONG).show();
				throw new Exception("Bundle grmEquAddr is null!");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		grmEquSID = (String)getIntent().getSerializableExtra("GrmEquSID");
		if(grmEquSID == null)
		{
			try {
				Toast.makeText(getApplicationContext(), "Bundle grmData is null!", Toast.LENGTH_LONG).show();
				throw new Exception("Bundle grmEquSID is null!");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		
		grmData = (GrmData)getIntent().getSerializableExtra("GrmData");
		if(grmData == null)
		{
			try {
				Toast.makeText(getApplicationContext(), "Bundle grmData is null!", Toast.LENGTH_LONG).show();
				throw new Exception("Bundle grmData is null!");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		txtview_varName.setText(grmData.varName);
		txtview_varValue.setText(grmData.varData);
		txtview_varDes.setText(grmData.webVarDes);
		
		
	}
	
	

	@Override
	public void setContentView(int layoutResID)
	{
		super.setContentView(layoutResID);
		OnClickListener onClickListener = new OnClickListener()
		{
			@SuppressLint("ShowToast") 
			@Override
			public void onClick(View v)
			{
				switch (v.getId())
				{
				case R.id.btn_FloatOK:				    			    		
			    	if(txt_varFloatVauleModify.getText().toString().isEmpty())
			    	{
			    		Toast.makeText(getApplicationContext(), "闇?瑕佽緭鍏ユ暟鎹?", 1).show();
			    		return;
			    	}
			    	
			    	float varModify = Float.parseFloat(txt_varFloatVauleModify.getText().toString().trim());
				    final String strRequest = "1\n" +
				    					grmData.varName+"\n"+
				    					varModify + "\n";	
				    Toast.makeText(
				    		VarModify_FloatActivity.this,
				    		strRequest, 
				    		Toast.LENGTH_SHORT);
				    new Thread(
				    		new Runnable() {									
								@Override
								public void run() {
									try {
										grmWriteVarStatic(grmEquAddr,grmEquSID,strRequest);
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}										
								}
							}).start();				    
					
				    Intent intent = new Intent();
        			intent.setClass(VarModify_FloatActivity.this, DataShowActivtiy.class);
        			startActivity(intent);  
				    
					break;				
				default:
					break;
				}
			}
		};

	
		btn_FloatOK.setOnClickListener(onClickListener);
	}
	
	
	public void grmWriteVarStatic(String strADDR, String strSID,String strRequest) throws Exception
	{
		
		String sr = HttpRequest.sendPost("http://"+strADDR+"/exdata?SID="+strSID+"&"+"OP=W", strRequest);		
		
		String[] strSplit = sr.split("\n");
		
		String isSuccessful = strSplit[0];
		
		if(isSuccessful.equals("ERROR"))
		{
			String errGetDataID  = (strSplit[1]).trim();
			(strSplit[2]).trim();
			
			throw new Exception("鍐欏叆鍙橀噺鍑洪敊:"+strSplit[1]+"--"+strSplit[2]);	
		}
		
		Integer.parseInt(strSplit[1].trim());
		
		//HashMap
		//key: index
		//value: 0 means Success; else means Error ID.
		HashMap<String, String> errorVar = new HashMap<String, String>(); 
		for(int i=2;i<strSplit.length;i++)
		{
			if(strSplit[i].trim().equals("0"))
			{
				continue;
			}
			else
			{
				errorVar.put(Integer.toString(i-2), strSplit[i].trim());
			}
		}
	}
}
