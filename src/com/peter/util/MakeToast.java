package com.peter.util;

import android.content.Context;
import android.widget.Toast;

public class MakeToast {

	public static void makeToast(Context ctx, String content)
	{
		Toast t3=Toast.makeText(ctx, content, Toast.LENGTH_SHORT);    
		t3.show();
	}
}
