package com.example.firstapp;
import android.util.Log;

import com.example.firstapp.NetworkTask;


public class FF {
	static int tabCount = 0;
	String aURL;
	String hWnd;
	int aMake;
	NetworkTask ffSocket;
	
	FF(NetworkTask networktask){
		ffSocket = networktask;
		this.hWnd = "newTab" + Integer.toString(tabCount);
		tabCount++;
		if(ffSocket.nsocket.isConnected()){
			Log.i("ffSocket", "Setting Variable");
			ffSocket.SendData("var " + this.hWnd + " = window.content");
			
		}
	}
	
	FF(NetworkTask networktask, String aUrl, String hWnd, int aMake){
		this.aURL = aUrl;
		this.hWnd = hWnd;
		this.aMake = aMake;
	}
	
	public String Navigate(String newUrl){
		return ffSocket.SendData(this.hWnd + ".location.href = '" + newUrl + "'");
	}
	
	public String getWinTitle(){
		return ffSocket.SendData(this.hWnd + ".document.title");
	}
	public String FindTab(){
		String pwb = "newTab";
		return pwb;
	}
	
	
}
