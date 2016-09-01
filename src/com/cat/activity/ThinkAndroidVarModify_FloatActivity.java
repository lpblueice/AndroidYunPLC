package com.cat.activity;

import java.util.HashMap;

import com.peter.grm.GrmData;
import com.peter.grm.GrmEqu;
import com.peter.http.HttpRequest;
import com.ta.annotation.TAInjectView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class ThinkAndroidVarModify_FloatActivity extends ThinkAndroidBaseActivity
{
	
	@TAInjectView(id = R.id.btn_FloatOK)
	Button btn_FloatOK;
	
	
	@TAInjectView(id = R.id.txtview_varValue)
	TextView txtview_varValue;
	
	@TAInjectView(id = R.id.txtview_varName)
	TextView txtview_varName;
	
	@TAInjectView(id = R.id.txtview_varDes)
	TextView txtview_varDes;
	
	@TAInjectView(id = R.id.txt_varFloatVauleModify)
	TextView txt_varFloatVauleModify;
	
	
	GrmData grmData =null;
	String grmEquAddr = null;
	String grmEquSID = null;
	
	@Override
	protected void onAfterOnCreate(Bundle savedInstanceState)
	{
		super.onAfterOnCreate(savedInstanceState);
		setTitle(R.string.thinkandroid_modifyvar_title);
		
		
		grmEquAddr = (String)getIntent().getSerializableExtra("GrmEquAddr");
		if(grmEquAddr == null)
		{
			try {
				Toast.makeText(getApplicationContext(),"Bundle grmEquAddr is null!", Toast.LENGTH_SHORT);
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
				Toast.makeText(getApplicationContext(),"Bundle grmEquSID is null!", Toast.LENGTH_SHORT);
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
				Toast.makeText(getApplicationContext(),"Bundle grmData is null!", Toast.LENGTH_SHORT);
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
	protected void onAfterSetContentView()
	{
		super.onAfterSetContentView();
		OnClickListener onClickListener = new OnClickListener()
		{
			@SuppressLint("ShowToast") 
			@Override
			public void onClick(View v)
			{
				switch (v.getId())
				{
				case R.id.btn_FloatOK:				    			    		
			    	if(txt_varFloatVauleModify.getText().toString() == "")
			    	{
			    		 Toast.makeText(
						    		ThinkAndroidVarModify_FloatActivity.this,
						    		"需要输入数据", 
						    		Toast.LENGTH_SHORT);
			    		return;
			    	}
			    	
			    	float varModify = Float.parseFloat(txt_varFloatVauleModify.getText().toString().trim());
				    final String strRequest = "1\n" +
				    					grmData.varName+"\n"+
				    					varModify + "\n";	
				    Toast.makeText(
				    		ThinkAndroidVarModify_FloatActivity.this,
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
        			intent.setClass(ThinkAndroidVarModify_FloatActivity.this, ThinkAndroidDataShowActivtiy.class);
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
			
			throw new Exception("写入变量出错:"+strSplit[1]+"--"+strSplit[2]);	
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
