package com.peter.grmmanager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.peter.grm.GrmData;
import com.peter.grm.GrmEqu;

public class GrmManager {

	/**
	 * list:
	 * 		hashmap1:
	 * 			"id"          ---- id
	 * 			"name"        ---- name
	 * 			"value"       ---- value
	 * 			"description" ---- description
	 * 		hashmap2:
	 * 			"id"          ---- id
	 * 			"name"        ---- name
	 * 			"value"       ---- value
	 * 			"description" ---- description
	 * 		hashmap3:
	 * 			"id"          ---- id
	 * 			"name"        ---- name
	 * 			"value"       ---- value
	 * 			"description" ---- description
	 * 		...
	 */
	private List<HashMap<String, Object>> data = new ArrayList<HashMap<String,Object>>();
	private Object syncData= new Object(); 
	
	/**
	 * the grms which need to get data.
	 */
	private GrmEqu grmEqu = null;
	
	private Timer freshDataTimer = new Timer();	
	/**
	 * the data getting thread 
	 */
	private TimerTask timerTask = new TimerTask() {
			
			@Override
			public void run() {

				if(grmEqu == null)
				{
					try {
						throw new Exception("GrmEqu is null");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				
				try {
					grmEqu.grmLogon();
					grmEqu.grmReadAllVar();
				} catch (Exception e) {
					e.printStackTrace();
				}
				
    			System.out.println("==============================" + grmEqu.getGrmName() + " data============================");
    			for(GrmData grmData : grmEqu.getGrmEquVars())
    			{
    				System.out.println(grmData.varName + "," + grmData.varData);
    			}
				
				synchronized (syncData) {
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
				}
			}
		};
		
	private static GrmManager grmManager = null;
	
	private GrmManager(){}
		
	public static GrmManager getInstance(){
		if(grmManager == null){
			grmManager = new GrmManager();
		}
		return grmManager;
	}
	
	/**
	 * start grm manager
	 * @param GrmEqu: the equ needs to get data
	 */
	public void start(GrmEqu equ){
		synchronized (syncData) {
			grmEqu = equ;
			
			data = new ArrayList<HashMap<String,Object>>();
			
			freshDataTimer.schedule(timerTask, 200 , 2000);
		}		
	}
	
	/**
	 * stop grm manager
	 */
	public void finish(){
		if(freshDataTimer != null)
		{
			freshDataTimer.cancel();
		}
		
		if(timerTask != null)
		{
			timerTask.cancel();
		}
	}
	
	/**
	 * return equ data
	 * @return
	 */
	public List<HashMap<String, Object>> getData(){
		synchronized (syncData) {
			List<HashMap<String, Object>> tempdata = new ArrayList<HashMap<String, Object>>();
			for (HashMap<String, Object> hashMap : data) {
				HashMap<String, Object> temphashmap = new HashMap<String, Object>();
				temphashmap.put("id", hashMap.get("id").toString());
				temphashmap.put("name", hashMap.get("name").toString());
				temphashmap.put("value", hashMap.get("value").toString());
				temphashmap.put("description", hashMap.get("description").toString());
				tempdata.add(temphashmap);				
			}
			System.out.println(this.getClass().toString() + ":   " + "======data changed! id: "+ data.get(0).get("id"));
			System.out.println(this.getClass().toString() + ":   " + "======data changed! id: "+ tempdata.get(0).get("id"));
			System.out.println(this.getClass().toString() + ":   " + "======data changed! name: "+ data.get(0).get("name"));
			System.out.println(this.getClass().toString() + ":   " + "======data changed! name: "+ tempdata.get(0).get("name"));
			System.out.println(this.getClass().toString() + ":   " + "======data changed! value: "+ data.get(0).get("value"));
			System.out.println(this.getClass().toString() + ":   " + "======data changed! value: "+ tempdata.get(0).get("value"));
			System.out.println(this.getClass().toString() + ":   " + "======data changed! description: "+ data.get(0).get("description"));
			System.out.println(this.getClass().toString() + ":   " + "======data changed! description: "+ tempdata.get(0).get("description"));
			return tempdata;
		}
	}
	
	/**
	 * return equ data
	 * @param tempdata: the collection which data should be stored in.
	 */
	public void getData(List<HashMap<String, Object>> tempdata){
		synchronized (syncData) {
			if(tempdata.size() <= 0){
				for (HashMap<String, Object> hashMap : data) {
					HashMap<String, Object> temphashmap = new HashMap<String, Object>();
					temphashmap.put("id", hashMap.get("id").toString());
					temphashmap.put("name", hashMap.get("name").toString());
					temphashmap.put("value", hashMap.get("value").toString());
					temphashmap.put("description", hashMap.get("description").toString());
					tempdata.add(temphashmap);				
				}
			}
			else{
				int i=0;
				for (HashMap<String, Object> hashMap : data) {;
					tempdata.get(i).put("id", hashMap.get("id").toString());
					tempdata.get(i).put("name", hashMap.get("name").toString());
					tempdata.get(i).put("value", hashMap.get("value").toString());
					tempdata.get(i).put("description", hashMap.get("description").toString());
					i++;
				}
			}
		}
		
	}
	
	/**
	 * retrun current grm equ
	 * @return
	 */
	public GrmEqu getEqu(){
		return grmEqu;
	}
	

	
	
}
