package com.peter.grm;

import java.io.Serializable;

public class GrmData implements Serializable 
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public String varName = "";
	public String varType = "";
	public String varRWType = "";//可读写属性
	public String netPermission = ""; //网络权限 
	public String groupName = "";//变量组名
	public String webVarDes = "";//Web变量描述
	
	public String varData = "";
	
	public static enum varTypeEnum
	{
		VAR_TYPE_UNKNOW, //未知类型 
		VAR_TYPE_B, //开关量
		VAR_TYPE_I, //整数型
		VAR_TYPE_F, //浮点型
	}
	
	public static enum varRWTypeEnum
	{
		VAR_RW_YPE_UNKNOWN,
		VAR_RW_TYPE_R, //只读 
		VAR_RW_TYPE_RW, //可读写
	}
	
	public static enum netPermissionEnum
	{
		NET_PERM_UNKNOW,
		NET_PERM_LOW, //网络权限低
		NET_PERM_MID, //中 
		NET_PERM_HIGH, //高
	}
	
	//此变量可否读写
	public varRWTypeEnum getVarRW()
	{
		if(varRWType.trim().equals("W"))
		{
			return varRWTypeEnum.VAR_RW_TYPE_RW;
		}
		else if(varRWType.trim().equals("R"))
		{
			return varRWTypeEnum.VAR_RW_TYPE_R;
		}
		else 
		{
			return varRWTypeEnum.VAR_RW_YPE_UNKNOWN;
		}
	}
	
	//变量类型
	public varTypeEnum getVarType()
	{
		if(varType.trim().equals("B"))
		{
			return varTypeEnum.VAR_TYPE_B;
		}
		else if(varType.trim().equals("I"))
		{
			return varTypeEnum.VAR_TYPE_I;
		}
		else if(varType.trim().equals("F"))
		{
			return varTypeEnum.VAR_TYPE_F;
		}
		else 
		{
			return varTypeEnum.VAR_TYPE_UNKNOW;
		}
	}
	
		
}
