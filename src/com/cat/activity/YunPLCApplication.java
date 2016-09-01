package com.cat.activity;

import com.ta.TAApplication;

public class YunPLCApplication extends TAApplication{

		    // 程序退出标记
		
		    private static boolean isProgramExit = false;
		
		    public void setExit(boolean exit) {
		
		        isProgramExit= exit;
		
		    }
		
		     
		
		    public boolean isExit() {
		
		        return isProgramExit;
		
		    }

}
