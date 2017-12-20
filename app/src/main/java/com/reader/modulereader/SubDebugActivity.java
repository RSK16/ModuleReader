package com.reader.modulereader;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.reader.modulereader.function.SPconfig;
import com.uhf.api.cls.Reader;

public class SubDebugActivity  extends Activity {

	@SuppressWarnings("unused")
	private Button  button_open,button_close,
	   button_powerup,button_powerdown,
	   button_send,button_revd;
 
	private Spinner spinner_pdatype_debug,
	spinner_baudrate;
	private ArrayAdapter<String> arradp_pdatype_debug,arradp_baud;

	String[] str_pdatype={"��","��Ϊ","trinea","alps-konka77_cu_ics","alps-kt45","hd508","idata"}; 
	String[] str_baud={"9600","19200","38400","57600","115200"};
	MyApplication myapp;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab1_tablelayout_debug);
		
		Application app=getApplication();
		myapp=(MyApplication) app; 
		myapp.Mreader=new Reader();

		myapp.spf=new SPconfig(this);

		spinner_pdatype_debug = (Spinner) findViewById(R.id.spinner_pdatype2); 
		arradp_pdatype_debug = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,str_pdatype); 
		arradp_pdatype_debug.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_pdatype_debug.setAdapter(arradp_pdatype_debug);
		 
		spinner_baudrate = (Spinner) findViewById(R.id.spinner_baudrate);       	 
		arradp_baud = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,str_baud); 
		arradp_baud.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);    
		spinner_baudrate.setAdapter(arradp_baud);
		
		 button_open=(Button)findViewById(R.id.button_open);
		   button_close=(Button)findViewById(R.id.button_close);
		   button_powerup=(Button)findViewById(R.id.button_up);
		   button_powerdown=(Button)findViewById(R.id.button_down);
		   button_send=(Button)findViewById(R.id.button_send);
		   button_revd=(Button)findViewById(R.id.button_revd);
		
		button_powerup.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				 
				boolean blen=myapp.Rpower.PowerUp();
				 
				Toast.makeText(SubDebugActivity.this, "�µ磺"+String.valueOf(blen),
							Toast.LENGTH_SHORT).show();
			}
			
		});
		
		button_powerdown.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				 
				boolean blen=myapp.Rpower.PowerDown();
				 
				Toast.makeText(SubDebugActivity.this, "�µ磺"+String.valueOf(blen),
							Toast.LENGTH_SHORT).show();
			}
			
		});
	}

	
}
