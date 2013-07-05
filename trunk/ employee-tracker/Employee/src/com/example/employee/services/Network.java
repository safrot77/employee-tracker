package com.example.employee.services;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


public class Network {

	/*public boolean ping() {
		
		Log.v("--------------Network--------", "---------");
		boolean reachable = false;
		Socket socket = null;
		try {
			socket = new Socket(IConstants.URL_PING, 80);
			Log.v("Service ", " available, yeah!");
			reachable = true;
		} catch (UnknownHostException e) {
			e.printStackTrace();
			Log.v("Service ", " unavailable, oh no!");
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (socket != null)
				try {
					socket.close();
				} catch (IOException e) {
					Log.v("","io exceeeptopn");
				}
		}

		return reachable;
	}*/

	public static boolean haveInternet(Context ctx) {

		NetworkInfo info = (NetworkInfo) ((ConnectivityManager) ctx
				.getSystemService(Context.CONNECTIVITY_SERVICE))
				.getActiveNetworkInfo();

		if (info == null || !info.isConnectedOrConnecting()) {
			return false;
		}
		return true;
	}

	

	

}
