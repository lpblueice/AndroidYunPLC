package com.cat.activity;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.cat.activity.R;
import com.peter.grm.GrmEqu;
import com.peter.http.HttpRequest;
import com.ta.annotation.TAInject;
import com.ta.annotation.TAInjectView;
import com.ta.util.http.AsyncHttpClient;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ThinkAndroidLoginActivity extends ThinkAndroidBaseActivity {

	@TAInjectView(id = R.id.btn_EquGroupLogin)
	Button btn_EquGroupLogin;

	@TAInjectView(id = R.id.txt_EquGroupName)
	TextView txt_EquGroupName;

	@TAInjectView(id = R.id.txt_EquGroupPwd)
	TextView txt_EquGroupPwd;

	@TAInjectView(id = R.id.btn_Exit)
	Button btn_Exit;

	@TAInject
	private AsyncHttpClient asyncHttpClient;
	private String retContent;
	private String vaddr = "";
	private String param = "";

	private Handler mHandler = null;
	
	public String loginError = "";	


	// private final String equGroupURLOri =
	// "http://www.yunplc.com/qlog?X=570D0A67C34ABE9B9FB9F19717&Y=GP0042004a00490041004f0054004f004e0047";

	private final String equGroupURLOri = "http://www.yunplc.com";// /qlog?X=570D0A67C34ABE9B9FB9F19717&Y=GP0042004a00490041004f0054004f004e0047";

	@Override
	protected void onAfterOnCreate(Bundle savedInstanceState) {
		super.onAfterOnCreate(savedInstanceState);
		setTitle(R.string.login_activity_title);

		txt_EquGroupName.setText("BJIAOTONG");
		txt_EquGroupPwd.setText("dreamblue5598");
	}

	@Override
	protected void onAfterSetContentView() {
		super.onAfterSetContentView();

		try {
			mHandler = new Handler() {
				public void handleMessage(Message msg) {// 此方法在ui线程运行
					super.handleMessage(msg);
					if (msg.what == 1) {
						Toast.makeText(getApplicationContext(), "登录成功", 1)
								.show();
						//doActivity(R.string.thinkandroiddatashowactivity);
						Intent intent = new Intent();
			 			intent.setClass(ThinkAndroidLoginActivity.this,ThinkAndroidDataShowActivtiy.class);
			 			startActivity(intent); 
					} else if (msg.what == 2) {
						Toast.makeText(getApplicationContext(),
								"服务器中不存在此用户名对应的设备", 1).show();
					} else if (msg.what == 3) {
						Toast.makeText(getApplicationContext(), "服务器应答失败", 1)
								.show();
					}
				}
			};

		} catch (Exception e) {
			e.printStackTrace();
		}

		OnClickListener onClickListener = new OnClickListener() {

			@Override
			public void onClick(View v) {

				switch (v.getId()) {
				case R.id.btn_EquGroupLogin:
					String username = txt_EquGroupName.getText().toString();
					String pwd = txt_EquGroupPwd.getText().toString();
					try {
						DoLogin(username, pwd, true);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				case R.id.btn_Exit:
					finish();
					System.exit(0);
					break;
				default:
					break;
				}
			}
		};
		btn_EquGroupLogin.setOnClickListener(onClickListener);
		btn_Exit.setOnClickListener(onClickListener);
	}



	public void DoLogin(String name, String pwd, boolean isGrp)
			throws Exception {
		if (name == null || name.equals(""))
			throw new Exception("设备组名为空");

		if (pwd == null || pwd.equals("") || pwd.length() == 0)
			throw new Exception("密码为空");

		String strName = name.trim();
		String strPwd = pwd.trim();

		if (strPwd.length() < 6)
			throw new Exception("密码少于6位");

		String venc = "";
		if (isGrp) {
			if (strName == null || strName.equals(""))
				throw new Exception("设备组名为空");
			else
				venc = fStr2Hex(strName);
		} else {
			if (!isNumeric(strName) || strName.length() != 11)
				throw new Exception("设备序号非法,必须是11位数字");
			else
				venc = strName;
		}

		boolean isPC = false;
		vaddr = equGroupURLOri + "/qlog";// ?X=" + fEnc(strPwd) + "&Y=" + (isGrp
											// ? 'G' : 'D') + (isPC ? 'P' : 'M')
											// + venc;

		param = "X=" + fEnc(strPwd) + "&Y=" + (isGrp ? 'G' : 'D')
				+ (isPC ? 'P' : 'M') + venc;

		System.out.println("request address: " + vaddr);
		System.out.println("request parameter: " + param);

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					retContent = HttpRequest.sendGet(vaddr, param);
					if (!retContent.equals("")) {
						Pattern pattern = Pattern.compile("href=\"(/sg?.*)\"");
						Matcher m = pattern.matcher(retContent);
						if (m.find()) {
							System.out.println(retContent);
							Message message = new Message();
							message.what = 1;
							mHandler.sendMessage(message);
						} else {
							Message message = new Message();
							message.what = 2;
							mHandler.sendMessage(message);
						}
					} else {
						Message message = new Message();
						message.what = 3;
						mHandler.sendMessage(message);
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();

	}

	private boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		return pattern.matcher(str).matches();
	}

	private String fEnc(String s) {
		String r = "";
		String hexes = "83ED725A06C4BF91";// 8,3,14,13,7,2,5,10,0,6,12,4,11,15,9,1,
		for (int i = 0; i < s.length(); i++) {
			int ix = s.charAt(i);

			int i1 = hexes.charAt((((ix & 0xFF) >> 4) + i) & 0x0F);
			String str_i1 = String.format("%c", (char) i1);

			int i2 = hexes.charAt((ix + i) & 0x0F);
			String str_i2 = String.format("%c", (char) i2);

			r += str_i1 + str_i2;
		}
		return r;
	}

	private String Num2Hex4(int num) {
		String s = Integer.toHexString(num);
		while (s.length() < 4)
			s = "0" + s;
		return s;
	}

	private String fStr2Hex(String s) {
		String sret = "";
		for (int i = 0; i < s.length(); i++) {
			sret += Num2Hex4((int) s.toCharArray()[i]);
		}
		return sret;
	}

}
