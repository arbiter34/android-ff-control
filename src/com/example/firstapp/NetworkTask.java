package com.example.firstapp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import android.util.Log;

public class NetworkTask {
    Socket nsocket; //Network Socket
    InputStream nis; //Network Input Stream
    OutputStream nos; //Network Output Stream
    String received;
    Boolean firstConnected = true;
    Boolean lastmessage = false;

    NetworkTask(String address, int port) { 
        try {
            //if(!nsocket.isConnected()){
            	SocketAddress sockaddr = new InetSocketAddress(address, port);
            	nsocket = new Socket();
            	nsocket.connect(sockaddr, 5000); //10 second connection timeout
                nis = nsocket.getInputStream();
                nos = nsocket.getOutputStream();
                Log.i("ConnectTask", "doInBackground: Socket created, streams assigned");
                Log.i("ConnectTask", "doInBackground: Waiting for inital data...");
            //}
            if (nsocket.isConnected()) { 
                byte[] buffer = new byte[4096];
                int read = nis.read(buffer, 0, 4096); //This is blocking
                while(read != -1){
                    byte[] tempdata = new byte[read];
                    System.arraycopy(buffer, 0, tempdata, 0, read);
                    String tempString = new String(tempdata);
                    if(tempString.matches(".*repl\\d?>.*")){
                    	Log.i("Connection Completed", received);
                    	return;
                    }
                    received += tempString;
                    Log.i("AsyncTask", "doInBackground: Got some data");
                    if(nis.available() > 0){
                    	read = nis.read(buffer, 0, 4096); //This is blocking
                    }
                    else{
                    	return;
                    }
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("AsyncTask", "doInBackground: IOException");
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("AsyncTask", "doInBackground: Exception");
            Log.i("Exception", e.toString());
        }
    }

    public String SendData(String cmd) { //You run this from the main thread.
        try {
        	cmd += ";\n";
            if (nsocket.isConnected()) {
            	lastmessage = false;
            	received = "";
                Log.i("AsyncTask", "SendDataToNetwork: Writing received message to socket");
                Log.d("SendData", cmd);
                nos.write(cmd.getBytes());
                return this.RecvData();
            } else {
                Log.i("AsyncTask", "SendDataToNetwork: Cannot send message. Socket is closed");
            }
        } catch (Exception e) {
            Log.i("AsyncTask", "SendDataToNetwork: Message send failed. Caught an exception");
        }
        return "";
    }
    
    public String RecvData(){
    	try{
    	 byte[] buffer = new byte[4096];
         int read = nis.read(buffer, 0, 4096); //This is blocking
         while(read > 0){
             Log.i("RecvTask", "doInBackground: Got some data");
             byte[] tempdata = new byte[read];
             System.arraycopy(buffer, 0, tempdata, 0, read);
             String tempString = new String(tempdata);
             received += tempString;
             Log.i("Received:", received);
             if(tempString.matches(".*repl\\d?>.*")){
            	 Log.i("Breaking", "Break");
             	break;
             }
             read = nis.read(buffer, 0, 4096);
         }
    	} catch (Exception e){
    		Log.i("Error", e.getLocalizedMessage());
    		return "failed";
    	}
    	if(firstConnected){
    		firstConnected = false;
    		return "Connected";
    	}
    	Log.i("RecvData:", received);
        received = received.replaceAll("repl\\d?>", "");
        received = received.replaceAll(" $", "");
        String response = received;
    	received = "";
    	return response;
    	}
}