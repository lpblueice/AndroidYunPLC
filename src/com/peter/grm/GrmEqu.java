package com.peter.grm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import com.peter.http.HttpRequest;



public class GrmEqu implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	private String strGrmName = "";
	public String getGrmName(){return strGrmName;}
	
	private String strGrmID = ""; 
	public String getGrmID(){return strGrmID;}
	private String strPasswd = "";
	

	private String strADDR = "";
	public String GetADDR(){return strADDR;}
	private String strSID = "";
	public String GetSID(){return strSID;}
	
	private String errGetDataID ="";
	private ArrayList<GrmData> varArray = new ArrayList<GrmData>();
	public ArrayList<GrmData> getGrmEquVars(){return varArray;}
	
	private GrmLocationInfo grmLocationInfo = new GrmLocationInfo();
	public GrmLocationInfo getGrmLocationInfo() {return grmLocationInfo;}
	
	
	public GrmEqu(String GrmName,String GrmID, String Passwd) throws Exception
	{
		strGrmName = GrmName;
		
		if(GrmID.equals(""))
		{
			throw new Exception("GRM 设备ID不能为空");
		}
		else
		{
			strGrmID = GrmID;
		}
		
		strPasswd = Passwd;
	}
	
	
	public void grmLogon() throws Exception
	{
		
		//If SID exists, we can reuse it. If it is not available, we should log in again.
		if(!strSID.equals(""))
		{
			return;
		}
		
		
		//String sr = HttpRequest.sendPost("http://www.yunplc.com:7080/exlog", "GRM="+strGrmID+"&"+"PASS="+strPasswd);
		String sr = HttpRequest.sendPost("http://www.yunplc.com:7080/exlog", "GRM="+strGrmID+"&"+"PASS="+strPasswd);
		System.out.println(sr);
		
		String[] strSplit = sr.split("\n");
		
		String isSuccessful = strSplit[0];
		
		if(isSuccessful.equals("ERROR"))
		{
			throw new Exception("登陆设备出错:"+strSplit[1]+"--"+strSplit[2]);			
		}
		else if(isSuccessful.equals("OK"))
		{
			strADDR = ((strSplit[1]).split("="))[1];
			strSID  = ((strSplit[2]).split("="))[1];		
			
			System.out.println("strADDR:"+strADDR);
			System.out.println("strSID:"+strSID);
		}	
	}
	
	
	private void GetAvailableSID() throws Exception
	{
		String sr = HttpRequest.sendPost("http://"+strADDR+"/exdata?SID="+strSID+"&"+"OP=E", "NTRPGC");
		
		String[] strSplit = sr.split("\n");
		
		String isSuccessful = strSplit[0];
		
		if(isSuccessful.equals("ERROR"))
		{
			errGetDataID  = (strSplit[1]).trim();
			(strSplit[2]).trim();		
			
			if(Integer.parseInt(errGetDataID) == 8)//If errorcode is 8, it means the SID is not available. We should log in again.
			{
				System.out.println(strGrmName + "'s current SID is not available, log in again!");
				grmLogon();
			}
			else
			{
				throw new Exception("登陆设备出错:"+strSplit[1]+"--"+strSplit[2]);	
			}
		}
		else
		{
			System.out.println(strGrmName + "'s current SID is available!");
		}
	}
	
	
	private void grmEnumVarInfo() throws Exception
	{
		GetAvailableSID();
		
		String sr = HttpRequest.sendPost("http://"+strADDR+"/exdata?SID="+strSID+"&"+"OP=E", "NTRPGC");
		
		
		System.out.println("=========================Enum all var info==========================");
		System.out.println(strGrmName);
		System.out.println(sr);
		
		String[] strSplit = sr.split("\n");
		
		String isSuccessful = strSplit[0];
		
		if(isSuccessful.equals("ERROR"))
		{
			errGetDataID  = (strSplit[1]).trim();
			(strSplit[2]).trim();
			
			throw new Exception("枚举设备变量 信息出错:"+strSplit[1]+"--"+strSplit[2]);	
		}
		
		
	
		Integer.parseInt(strSplit[1].trim());
		
		varArray.clear();//clear var list
		for(int i=2;i<strSplit.length;i++)
		{
			ArrayList<String> varPro = new ArrayList<String>();
			String[] str = strSplit[i].split(",");
			for(int j=0;j<str.length;j++)
			{
				varPro.add(str[j]);
			}
			for(int k=varPro.size();k<6;k++)//If there is no elements, it means there are all empty string.
			{
				varPro.add("");
			}
			
			GrmData grmData = new GrmData();				
			grmData.varName =       (varPro.get(0) == null) ?  "" : varPro.get(0).trim();
			grmData.varType =       (varPro.get(1) == null) ?  "" : varPro.get(1).trim();
			grmData.varRWType =     (varPro.get(2) == null) ?  "" : varPro.get(2).trim();
			grmData.netPermission = (varPro.get(3) == null) ?  "" : varPro.get(3).trim();
			grmData.groupName =     (varPro.get(4) == null) ?  "" : varPro.get(4).trim();
			grmData.webVarDes =     (varPro.get(5) == null) ?  "" : varPro.get(5).trim();
			
			
			varArray.add(grmData);
		}
		
		
		return;

	}
	
	
	private void grmReadVar(ArrayList<String> varList) throws Exception
	{
		GetAvailableSID();
		
		String strRequest = "";
		strRequest += varList.size() + "\n";
		for(String varName : varList)
		{
			strRequest += varName + "\n";
		}
		
		String sr = HttpRequest.sendPost("http://"+strADDR+"/exdata?SID="+strSID+"&"+"OP=R", strRequest);
		
		//System.out.println(strSID);
		System.out.println("=========================Read var==========================");
		System.out.println(strGrmName);
		System.out.println(sr);
		
		String[] strSplit = sr.split("\n");
		
		String isSuccessful = strSplit[0];
		
		if(isSuccessful.equals("ERROR"))
		{
			errGetDataID  = (strSplit[1]).trim();
			(strSplit[2]).trim();
			
			throw new Exception("读取变量出错:"+strSplit[1]+"--"+strSplit[2]);	
		}
		
		Integer.parseInt(strSplit[1].trim());
		
		for(int i=2;i<strSplit.length;i++)
		{
			varArray.get(i-2).varData = (strSplit[i].trim() == null) ?  "" : strSplit[i].trim();
		}
		
	}
	
	
	private ArrayList<String> grmGetVarNameList() throws Exception
	{
		ArrayList<String> equVarNameList = new ArrayList<String>();
		
		for(GrmData var : varArray)
		{
			equVarNameList.add(var.varName);
		}
		
		return equVarNameList;
	}
	
	public void grmReadAllVar() throws Exception
	{
		grmEnumVarInfo();
		
		ArrayList<String> equVarNameList = grmGetVarNameList();
		
		grmReadVar(equVarNameList);
	}
	
	//pattern:
	//2
	//变量名 1
	//变量值 1
	//变量名 2
	//变量值 2
	//.....
	public void grmWriteVar(String strRequest) throws Exception
	{
		GetAvailableSID();
		
		String sr = HttpRequest.sendPost("http://"+strADDR+"/exdata?SID="+strSID+"&"+"OP=W", strRequest);
		
		//System.out.println(strSID);
		System.out.println("=========================Write var==========================");
		System.out.println(strGrmName);
		System.out.println(sr);
		
		String[] strSplit = sr.split("\n");
		
		String isSuccessful = strSplit[0];
		
		if(isSuccessful.equals("ERROR"))
		{
			errGetDataID  = (strSplit[1]).trim();
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
	
	
	
	
	
	public void grmGetLocation() throws Exception
	{
		GetAvailableSID();
		
		String sr = HttpRequest.sendPost("http://"+strADDR+"/exdata?SID="+strSID+"&"+"OP=L", "");
		
		//System.out.println(strSID);
		System.out.println("=========================Get Grm Location==========================");
		System.out.println(strGrmName);
		System.out.println(sr);
		
		String[] strSplit = sr.split("\n");
		
		String isSuccessful = strSplit[0];
		
		if(isSuccessful.equals("ERROR"))
		{
			errGetDataID  = (strSplit[1]).trim();
			(strSplit[2]).trim();
			
			throw new Exception("获取设备地理位置出错:"+strSplit[1]+"--"+strSplit[2]);	
		}
		
		int num = Integer.parseInt(strSplit[1].trim());
		
		if(num == 9)
		{
			grmLocationInfo.originData =    (strSplit[2] == null) ?  "" : strSplit[2].trim(); //2;460;0; 2508;6F19;20;2508;0A83;10; （基站原始数据）
			grmLocationInfo.longtitude=     (strSplit[3] == null) ?  "" : strSplit[3].trim();//113.4009 （坐标：经度）
			grmLocationInfo.lantitude =     (strSplit[4] == null) ?  "" : strSplit[4].trim();//23.1310 （坐标：纬度）
			grmLocationInfo.province =      (strSplit[5] == null) ?  "" : strSplit[5].trim();//广东省 （位置：省/自治区/直辖市）
			grmLocationInfo.city =          (strSplit[6] == null) ?  "" : strSplit[6].trim();//广州市 （位置：地/市）
			grmLocationInfo.district =      (strSplit[7] == null) ?  "" : strSplit[7].trim();//天河区 （位置：区/县）
			grmLocationInfo.street =        (strSplit[8] == null) ?  "" : strSplit[8].trim();//车陂路 300 号 （位置：乡镇/街道）
			grmLocationInfo.fullLocation =  (strSplit[9] == null) ?  "" : strSplit[9].trim();//广东省广州市天河区车陂路 362 号 （完整的位置字符串）
			grmLocationInfo.nearMarkPoint = (strSplit[10] == null) ?  "" : strSplit[10].trim();//时尚明苑 （临近标志点）
		}
		else if(num == 1)
		{
			grmLocationInfo.originData =    (strSplit[2] == null) ?  "" : strSplit[2].trim();
		}
		else if(num == 0) 
		{
			
		}		
	}
	
	public void grmGetGrmHistory() throws Exception
	{
		GetAvailableSID();
		
		 String sbegin = "20160528000000";//"yyyyMMddhhmmss";
		 String send = "20160529000000";
		 String inc = "+"; //isinc ? "+" : "-";
		 String val = sbegin + "\r\n" + send + "\r\n" + inc + "\r\n";
		 String valname = "$NetState";
		 int maxcnt = 10;
		 val += valname + "\r\n" + maxcnt;

		String sr = HttpRequest.sendPost("http://"+strADDR+"/exdata?SID="+strSID+"&"+"OP=H", val);
		
		//System.out.println(strSID);
		System.out.println("=========================Get Grm History==========================");
		System.out.println(strGrmName);
		System.out.println(sr);
	}

}
