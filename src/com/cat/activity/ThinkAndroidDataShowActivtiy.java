package com.cat.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.peter.grm.GrmData;
import com.peter.grm.GrmEqu;
import com.ta.annotation.TAInjectView;

import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class ThinkAndroidDataShowActivtiy extends ThinkAndroidBaseActivity
{
	
	@TAInjectView(id = R.id.btn_equ1)
	Button btn_equ1;
	
	@TAInjectView(id = R.id.btn_equ2)
	Button btn_equ2;
	

	
	ListView listView;
	SimpleAdapter adapter;
	List<HashMap<String, Object>> data = new ArrayList<HashMap<String,Object>>();
	int mPosition;
	
	public static ArrayList<GrmEqu> grmEquArr = new ArrayList<GrmEqu>();
	Timer freshDataTimer = new Timer();
	
	private Handler mHandler;

	@Override
	protected void onAfterOnCreate(Bundle savedInstanceState)
	{
		super.onAfterOnCreate(savedInstanceState);
		setTitle(R.string.thinkandroid_datashow_title);

		
		listView = (ListView) this.findViewById(R.id.listView1); 
		listView.setOnItemClickListener(new ItemClickListener());

		
		try {
			mHandler = new Handler() 
			{
				public void handleMessage(Message msg) 
				{// 此方法在ui线程运行
					 super.handleMessage(msg);
		             if(msg.what == 1)
		             {     		    
		                 adapter.notifyDataSetChanged();
		             }
				}
			};
			
			
			//freshDataTimer.cancel();
			//创建SimpleAdapter适配器将数据绑定到item显示控件上  
		    adapter = new SimpleAdapter(
			    ThinkAndroidDataShowActivtiy.this, 
			    data, 
			    R.layout.var_item,   
	            new String[]{"id", "name", "value", "description"}, 
	            new int[]{R.id.varItem_ID, 
			    		R.id.varItem_Name,
			    		R.id.varItem_Value,
			    		R.id.varItem_Description}
			    );  
		    listView.setAdapter(adapter); 					
			grmEquArr.clear();
			grmEquArr.add(new GrmEqu("东餐厅","50102583211","dreamblue5598"));
			freshDataTimer.schedule(timerTask, 2000 , 2000);			
			Toast.makeText(getApplicationContext(), "切换至东餐厅", 1).show();	
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onAfterSetContentView()
	{
		super.onAfterSetContentView();
		OnClickListener onClickListener = new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				switch (v.getId())
				{
				case R.id.btn_equ1:
					try {
						//freshDataTimer.cancel();
						data = new ArrayList<HashMap<String,Object>>();
						//创建SimpleAdapter适配器将数据绑定到item显示控件上  
					    adapter = new SimpleAdapter(
						    ThinkAndroidDataShowActivtiy.this, 
						    data, 
						    R.layout.var_item,   
				            new String[]{"id", "name", "value", "description"}, 
				            new int[]{R.id.varItem_ID, 
						    		R.id.varItem_Name,
						    		R.id.varItem_Value,
						    		R.id.varItem_Description}
						    );  
					    listView.setAdapter(adapter); 					
						listView.setAdapter(adapter); 
						grmEquArr.clear();
						grmEquArr.add(new GrmEqu("东餐厅","50102583211","dreamblue5598"));
						freshDataTimer.schedule(timerTask, 2000 , 2000);
						Toast.makeText(getApplicationContext(), "切换至东餐厅", 1).show();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				case R.id.btn_equ2:
					try {
						//freshDataTimer.cancel();
						data = new ArrayList<HashMap<String,Object>>();
						//创建SimpleAdapter适配器将数据绑定到item显示控件上  
					    adapter = new SimpleAdapter(
						    ThinkAndroidDataShowActivtiy.this, 
						    data, 
						    R.layout.var_item,   
				            new String[]{"id", "name", "value", "description"}, 
				            new int[]{R.id.varItem_ID, 
						    		R.id.varItem_Name,
						    		R.id.varItem_Value,
						    		R.id.varItem_Description}
						    );  
					    listView.setAdapter(adapter); 					
						listView.setAdapter(adapter); 
						grmEquArr.clear();
						grmEquArr.add(new GrmEqu("西餐厅","20437182836","32345598"));
						freshDataTimer.schedule(timerTask, 2000 , 2000);						
						Toast.makeText(getApplicationContext(), "切换至西餐厅", 1).show();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;				
				default:
					break;
				}
			}
		};

	
		btn_equ1.setOnClickListener(onClickListener);
		btn_equ2.setOnClickListener(onClickListener);
	}
	
	@Override
	public void finish()
	{
		if(freshDataTimer != null)
		{
			freshDataTimer.cancel();
			freshDataTimer = null;
		}
		
		if(timerTask != null)
		{
			timerTask.cancel();
			timerTask = null;
		}
		
		System.exit(0);
	}
	

	//获取数据线程
	TimerTask timerTask = new TimerTask() {
		
		@Override
		public void run() {

			GrmEqu grmEqu = grmEquArr.get(0);
			
			try {
				grmEqu.grmLogon();
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				grmEqu.grmReadAllVar();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
//    			System.out.println("==============================" + grmEqu.getGrmName() + " data============================");
//    			for(GrmData grmData : grmEqu.getGrmEquVars())
//    			{
//    				System.out.println(grmData.varName + "," + grmData.varData);
//    			}
			  
		    int i=1;
		    if(data.size() <= 0)
			{
    			for(GrmData grmData : grmEqu.getGrmEquVars())
    		    {  		    				
    	            HashMap<String, Object> item = new HashMap<String, Object>();  
    	            item.put("id", i);  
    	            item.put("name", grmData.varName.trim());  
    	            item.put("value", grmData.varData.trim());  
    	            item.put("description", grmData.webVarDes.trim());  
    	            data.add(item);     				
    	            i++;
    		    }		    			
		    }  
			else
			{
				for(GrmData grmData : grmEqu.getGrmEquVars())
    		    { 	    			
	            	data.get(i-1).put("id", i);
	            	data.get(i-1).put("name", grmData.varName.trim());
	            	data.get(i-1).put("value", grmData.varData.trim());
	            	data.get(i-1).put("description", grmData.webVarDes.trim());
	            	i++;
    		    }
			}
				    			
			Message message = new Message();
            message.what = 1;
            mHandler.sendMessage(message);
		}
	};
	
	//获取点击事件      
    private final class ItemClickListener implements OnItemClickListener
    {  
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {  
            ListView listView = (ListView) parent;  
            HashMap<String, Object> data = (HashMap<String, Object>) listView.getItemAtPosition(position);  
            int varId =  Integer.parseInt(data.get("id").toString().trim()) - 1 ;  
            //Toast.makeText(getApplicationContext(), varId, Toast.LENGTH_SHORT).show();
            
            GrmEqu grmEqu = grmEquArr.get(0);
            if(grmEqu == null)
            {
            	try {
					throw new Exception("GrmEqu is null");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
            
            
            GrmData grmData = grmEqu.getGrmEquVars().get(varId);
        	if(grmData == null)
        	{
        		try {
					throw new Exception("GrmData is null");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	}
        	
        	
        	if(grmData.getVarRW() == GrmData.varRWTypeEnum.VAR_RW_YPE_UNKNOWN)
        	{
        		try {
					throw new Exception("GrmData RW type is unknown");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	}
        	else  if(grmData.getVarRW() == GrmData.varRWTypeEnum.VAR_RW_TYPE_R)
        	{
        		Toast.makeText(getApplicationContext(), "数据只读，不能修改。", Toast.LENGTH_SHORT).show();
        		return;
			}
        	else  if(grmData.getVarRW() == GrmData.varRWTypeEnum.VAR_RW_TYPE_RW)
        	{        		
        		if(grmData.getVarType() == GrmData.varTypeEnum.VAR_TYPE_UNKNOW)
        		{
        			try {
    					throw new Exception("GrmData var type is unknown");
    				} catch (Exception e) {
    					// TODO Auto-generated catch block
    					e.printStackTrace();
    				}
        		}
        		else if(grmData.getVarType() == GrmData.varTypeEnum.VAR_TYPE_B)
        		{
        			Intent intent = new Intent();
        			intent.setClass(ThinkAndroidDataShowActivtiy.this, ThinkAndroidVarModify_BooleanActivity.class);
        			Bundle bundle = new Bundle();
        			bundle.putSerializable("GrmData", grmData);
        			bundle.putSerializable("GrmEquAddr", grmEquArr.get(0).GetADDR());
        			bundle.putSerializable("GrmEquSID", grmEquArr.get(0).GetSID());
        			intent.putExtras(bundle);
        			startActivity(intent);      			
        		}
        		else if(grmData.getVarType() == GrmData.varTypeEnum.VAR_TYPE_I)
        		{
        			Intent intent = new Intent();
        			intent.setClass(ThinkAndroidDataShowActivtiy.this, ThinkAndroidVarModify_IntActivity.class);
        			Bundle bundle = new Bundle();
        			bundle.putSerializable("GrmData", grmData);
        			bundle.putSerializable("GrmEquAddr", grmEquArr.get(0).GetADDR());
        			bundle.putSerializable("GrmEquSID", grmEquArr.get(0).GetSID());
        			intent.putExtras(bundle);
        			startActivity(intent); 
				}
        		else if(grmData.getVarType() == GrmData.varTypeEnum.VAR_TYPE_F)
        		{
        			Intent intent = new Intent();
        			intent.setClass(ThinkAndroidDataShowActivtiy.this, ThinkAndroidVarModify_FloatActivity.class);
        			Bundle bundle = new Bundle();
        			bundle.putSerializable("GrmData", grmData);
        			bundle.putSerializable("GrmEquAddr", grmEquArr.get(0).GetADDR());
        			bundle.putSerializable("GrmEquSID", grmEquArr.get(0).GetSID());
        			intent.putExtras(bundle);
        			startActivity(intent);
				}
			}       	  
        }  
    } 

    
    
    
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        
    	 if(keyCode == KeyEvent.KEYCODE_BACK)
    	 {
    		Intent intent = new Intent();
 			intent.setClass(ThinkAndroidDataShowActivtiy.this, ThinkAndroidLoginActivity.class);
 			startActivity(intent);
         }
    	 
    	 return super.onKeyDown(keyCode, event);
    	
    }
    
    
    protected void onStart() 
    {    	
	    super.onStart();    	
	    YunPLCApplication mApp = (YunPLCApplication)getApplication();    	
	    if(mApp.isExit())
	    {    	
	        finish();    	
	    }    	
    }


	
	

}