package com.xj.mainframe.netState;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * @Title NetWorkUtil
 * @Package com.ta.util.netstate
 * @Description 是检测网络的一个工具包
 */
public class NetWorkUtil
{
	public enum netType
	{
		wifi, CMNET, CMWAP, noneNet
	}

	/**
	 * 判断是否有网络连接
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isNetworkConnected(Context context)
	{
		if (context != null)
		{
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager
					.getActiveNetworkInfo();
			if (mNetworkInfo != null)
			{
				return mNetworkInfo.isAvailable();
			}
		}
		return false;
	}

	/**
	 * 判断WIFI网络是否可用
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isWifiConnected(Context context)
	{
		if (context != null)
			if (getConnectedType(context) == ConnectivityManager.TYPE_WIFI)
			{
				return true;
			}
		return false;
	}

	/**
	 * 判断MOBILE网络是否可用
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isMobileConnected(Context context)
	{
		if (context != null)
			if (getConnectedType(context) == ConnectivityManager.TYPE_MOBILE)
			{
				return true;
			}
		return false;
	}

	/**
	 * 获取当前网络连接的类型信息
	 * 
	 * @param context
	 * @return
	 */
	public static int getConnectedType(Context context)
	{
		if (context != null)
		{
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager
					.getActiveNetworkInfo();
			if (mNetworkInfo != null && mNetworkInfo.isAvailable())
			{
				return mNetworkInfo.getType();
			}
		}
		return -1;
	}

	/**
	 * 
	 * @author 白猫
	 * 
	 *         获取当前的网络状态 -1：没有网络 1：WIFI网络2：wap 网络3：net网络
	 * 
	 * @param context
	 * 
	 * @return
	 */
	public static netType getAPNType(Context context)
	{
		ConnectivityManager connMgr = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		if (networkInfo == null)
		{
			return netType.noneNet;
		}
		int nType = networkInfo.getType();
		if (nType == ConnectivityManager.TYPE_MOBILE)
		{
			if (networkInfo.getExtraInfo().toLowerCase().equals("cmnet"))
			{
				return netType.CMNET;
			}

			else
			{
				return netType.CMWAP;
			}
		} else if (nType == ConnectivityManager.TYPE_WIFI)
		{
			return netType.wifi;
		}
		return netType.noneNet;

	}
}
