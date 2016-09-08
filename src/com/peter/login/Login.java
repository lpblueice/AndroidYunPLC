package com.peter.login;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ta.TASyncHttpClient;
import com.ta.annotation.TAInject;
import com.ta.util.http.AsyncHttpClient;
import com.ta.util.http.AsyncHttpResponseHandler;

public class Login {

	@TAInject
	private TASyncHttpClient syncHttpClient;
	@TAInject
	private AsyncHttpClient asyncHttpClient;
	
	private final String equGroupURLOri = "http://www.yunplc.com/qlog?X=570D0A67C34ABE9B9FB9F19717&Y=GP0042004a00490041004f0054004f004e0047";
	
	private String retContent = "";
	public String loginError = "";
	public boolean isLoginFail = true;
	private Object syncObject = new Object();
	
	
	private int timeout = 5000; //ms
	
	public void LoginEquGroup(String name, String pwd, boolean isGrp) throws Exception
	{
//		if(name == null || name.equals(""))
//			throw new Exception("设备组名为空");
//		
//		if(pwd == null || pwd.equals("") || pwd.length() == 0)
//			throw new Exception("密码为空");
//		
//		String strName = name.trim();
//		String strPwd = pwd.trim();
//		
//		
//		if(strPwd.length() < 6)
//			throw new Exception("密码少于6位");
//		
//		if (isGrp) 
//		{
//			if(strName == null || strName.equals(""))
//				throw new Exception("设备组名为空");
//        }
//        else
//        {
//            if(!isNumeric(strName) || strName.length() != 11)
//            	throw new Exception("设备序号非法,必须是11位数字");
//        }
		
//		synchronized (syncObject) {
//			retContent = "";
//			loginError = null;
//			isLoginFail = true;
//		}
		
//		asyncHttpClient.get(equGroupURLOri, new AsyncHttpResponseHandler()
//		{
//			@Override
//			public void onSuccess(String content)
//			{
//				synchronized (syncObject) {
//					retContent = content;
//					syncObject.notify();
//				}				
//			}
//			
//			@Override
//			public void onFailure(Throwable error)
//			{
//				synchronized (syncObject) {
//					loginError = error.toString();
//					isLoginFail = true;
//					syncObject.notify();
//				}
//			}
//			
//		});
		
		
//		synchronized (syncObject) {
//			syncObject.wait();
//		}

//		if(!retContent.equals(""))
//		{
//			Pattern pattern = Pattern.compile("href=\"(/sg?SID=.*)\""); 
//			Matcher m=pattern.matcher(retContent); 
//			if(m.find())
//			{
//				isLoginFail = false;
//				loginError = "EquGroupName doesn't exist in Server";
//			}
//		};
		
	}
	
	public void DoLogin(String name, String pwd, boolean isGrp) throws Exception
	{
		if(name == null || name.equals(""))
		throw new Exception("设备组名为空");
		
		if(pwd == null || pwd.equals("") || pwd.length() == 0)
			throw new Exception("密码为空");
		
		String strName = name.trim();
		String strPwd = pwd.trim();
		
		
		if(strPwd.length() < 6)
			throw new Exception("密码少于6位");
		
		if (isGrp) 
		{
			if(strName == null || strName.equals(""))
				throw new Exception("设备组名为空");
	    }
	    else
	    {
	        if(!isNumeric(strName) || strName.length() != 11)
	        	throw new Exception("设备序号非法,必须是11位数字");
	    }
	
//		synchronized (syncObject) {
//			retContent = "";
//			loginError = null;
//			isLoginFail = true;
//		}
		
		asyncHttpClient.get(equGroupURLOri, new AsyncHttpResponseHandler()
		{
			@Override
			public void onSuccess(String content)
			{
				//synchronized (syncObject) {
					retContent = content;
				//	syncObject.notify();
				//}				
			}
			
//			@Override
//			public void onFailure(Throwable error)
//			{
//				synchronized (syncObject) {
//					loginError = error.toString();
//					isLoginFail = true;
//					syncObject.notify();
//				}
//			}
			
		});
		
		
//		synchronized (syncObject) {
//			syncObject.wait();
//		}
	
		if(!retContent.equals(""))
		{
			Pattern pattern = Pattern.compile("href=\"(/sg?SID=.*)\""); 
			Matcher m=pattern.matcher(retContent); 
			if(m.find())
			{
				isLoginFail = false;
				loginError = "EquGroupName doesn't exist in Server";
			}
		};
	}
	
	public void XX(){}
	
	
	private boolean isNumeric(String str)
	{ 
	    Pattern pattern = Pattern.compile("[0-9]*"); 
	    return pattern.matcher(str).matches();    
	 } 
	
	
}
