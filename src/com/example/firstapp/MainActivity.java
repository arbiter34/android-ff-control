package com.example.firstapp;

import java.net.Socket;
import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity {

	Handler handler = new Handler() {
		  @Override
		  public void handleMessage(Message msg) {
			  TextView textStatus = 
                        (TextView)findViewById(R.id.textStatus);
			  textStatus.setText("Button Pressed");
		     }
		 };
	 
    Button btnStart, btnSend, btnTest, btnTest1, submitText;
    TextView textStatus;
    EditText urlText;
    static NetworkTask networktask = new NetworkTask("192.168.1.132", 4242);
    static String aUrl;
    Socket nsocket;
    FF stuff;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnStart = (Button)findViewById(R.id.btnStart);
        btnSend = (Button)findViewById(R.id.btnSend);
        btnTest = (Button)findViewById(R.id.btnTest);
        btnTest1 = (Button)findViewById(R.id.btnTest1);
        submitText = (Button)findViewById(R.id.submitText);
        textStatus = (TextView)findViewById(R.id.textStatus);
        urlText = (EditText)findViewById(R.id.urlText);
        btnStart.setOnClickListener(btnStartListener);
        btnSend.setOnClickListener(btnSendListener);
        btnTest.setOnClickListener(btnTestListener);
        btnTest1.setOnClickListener(btnTest1Listener);
        submitText.setOnClickListener(submitTextListener);
    }

    private OnClickListener submitTextListener = new OnClickListener() {
    	public void onClick(View v){
    		aUrl = urlText.getText().toString();
    	}
    };
    
    private OnClickListener btnStartListener = new OnClickListener() {
        public void onClick(View v){    	
        	Runnable runnable = new Runnable() {
    	        public void run() {    
    	        	Message msg = handler.obtainMessage();
        			Bundle bundle = new Bundle();
        			networktask = new NetworkTask("192.168.1.132", 4242); //Create initial instance so SendDataToNetwork doesn't throw an error.
        			networktask.SendData("");
        	}
        };
        Thread mythread = new Thread(runnable);
        mythread.start();
       }
    };
    
    private OnClickListener btnTestListener = new OnClickListener() {
    	public void onClick(View v){    	
        	Runnable runnable = new Runnable() {
    	        public void run() {     	
            stuff = new FF(networktask);
            stuff.Navigate("http://www.google.com");
        	}
        };
        Thread mythread = new Thread(runnable);
        mythread.start();
       }
    };
    
    private OnClickListener btnTest1Listener = new OnClickListener() {
    	public void onClick(View v){    	
        	Runnable runnable = new Runnable() {
    	        public void run() {     	
            stuff = new FF(networktask); 
        	}
        };
        Thread mythread = new Thread(runnable);
        mythread.start();
       }
    };
    
    private OnClickListener btnSendListener = new OnClickListener() {
        public void onClick(View v){    	
        	Runnable runnable = new Runnable() {
    	        public void run() {    
    	        	stuff = new FF(networktask);
    	        	stuff.Navigate(aUrl);
        	}
        };
        Thread mythread = new Thread(runnable);
        mythread.start();
       }
    };
}
