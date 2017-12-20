package com.reader.modulereader;


import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import com.pow.api.cls.RfidPower;
import com.reader.modulereader.function.SPconfig;
import com.uhf.api.cls.Reader;

import java.util.ArrayList;
import java.util.List;

public class Sub1TabActivity<OpeListActivity> extends Activity {

	private Button button_connect,button_disconnect;
	TabHost tabHost_connect;
	 
	private Spinner spinner_pdatype,spinner_antports;
	@SuppressWarnings("rawtypes")
	private ArrayAdapter arradp_pdatype,arradp_antports;
	
	String[] pdatypev={"��","CW","trinea","alps-konka77_cu_ics",
			"alps-kt45","hd508","IDAT","JIEB","ekemp","alps-konka77_cu_ics_test",
			"senter-st308w", "senter-st907","alps-g86","HANDH_12","armor","CZ8800","XISI","XIBA","SENTER907PDA","alps-kt45Q",
			"Urovo_SQ31","Urovo_SQ31Q","K06SS_A","HANDH_13","SENTER907_PDA_T8939"}; 
 
	String[] baudrate={"9600","19200","38400","57600","115200"};
	 
	MyApplication myapp;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab1_tablelayout_connect);
		
		Application app=getApplication();
		myapp=(MyApplication) app; 
		myapp.Mreader=new Reader();

		myapp.spf=new SPconfig(this);
		
		 pdatypev[0]=MyApplication.No;
		 
				spinner_antports = (Spinner) findViewById(R.id.spinner_antports);       	 
				arradp_antports = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,MyApplication.pdaatpot); 
				arradp_antports.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);    
				spinner_antports.setAdapter(arradp_antports);
				spinner_pdatype = (Spinner) findViewById(R.id.spinner_pdatype); 
				 //����ѡ������ArrayAdapter��������         	 
				arradp_pdatype = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,pdatypev); 
				 //���������б�ķ��       
				arradp_pdatype.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				 //��adapter ��ӵ�spinner��       
				spinner_pdatype.setAdapter(arradp_pdatype);

				button_connect=(Button)findViewById(R.id.button_connect);
				button_disconnect=(Button)findViewById(R.id.button_disconnect);
				 
				String pdatypestr=myapp.spf.GetString("PDATYPE");
				String addressstr=myapp.spf.GetString("ADDRESS");
				String antportstr=myapp.spf.GetString("ANTPORT");
				 
				if(!pdatypestr.equals(""))
				{
					spinner_pdatype.setSelection(Integer.valueOf(pdatypestr));
					if(spinner_pdatype.getSelectedItemPosition()==0)
					{
						EditText et1=(EditText)findViewById(R.id.editText_adr);
						et1.setText(addressstr);
					}
				}
				 
				if(!antportstr.equals(""))
				{
					spinner_antports.setSelection(Integer.valueOf(antportstr));
				}
				
				//�����¼�
				button_connect.setOnClickListener(new OnClickListener()
				{
					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						EditText et1=(EditText)findViewById(R.id.editText_adr);
					    String  ip=et1.getText().toString();
					    if(ip=="") 
					    {
					    	Toast.makeText(Sub1TabActivity.this, MyApplication.Constr_sub1adrno,
							Toast.LENGTH_SHORT).show();
					    }
					    if(myapp.Rpower==null)
					    {
					    	Toast.makeText(Sub1TabActivity.this, MyApplication.Constr_sub1pdtsl,
									Toast.LENGTH_SHORT).show();
					    }
		 
						boolean blen=myapp.Rpower.PowerUp();
					  
						Toast.makeText(Sub1TabActivity.this, MyApplication.Constr_mainpu+String.valueOf(blen),
									Toast.LENGTH_SHORT).show();
						if(!blen)
						return;

						Reader.READER_ERR er=myapp.Mreader.InitReader_Notype(ip, spinner_antports.getSelectedItemPosition()+1);
 
						if(er== Reader.READER_ERR.MT_OK_ERR)
						{	
							myapp.needlisen=true;
							myapp.needreconnect=false;
							myapp.spf.SaveString("PDATYPE", String.valueOf(spinner_pdatype.getSelectedItemPosition()));
							myapp.spf.SaveString("ADDRESS", et1.getText().toString());
							myapp.spf.SaveString("ANTPORT", String.valueOf(spinner_antports.getSelectedItemPosition()));
							 
							Toast.makeText(Sub1TabActivity.this, MyApplication.Constr_connectok,
							Toast.LENGTH_SHORT).show();
							myapp.antportc=spinner_antports.getSelectedItemPosition()+1;  
							ConnectHandleUI();
							myapp.Address=ip;
						 
						}
							else
							{
								 
								Toast.makeText(Sub1TabActivity.this, MyApplication.Constr_connectfialed+
							    er.toString(),Toast.LENGTH_SHORT).show();
							}
					}
				});
				
				button_disconnect.setOnClickListener(new OnClickListener()
				{

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						
						if(myapp.Mreader!=null)
						{
							myapp.Mreader.CloseReader();
							myapp.needlisen=true;
						}
						
						boolean blen=myapp.Rpower.PowerDown();
					 
						Toast.makeText(Sub1TabActivity.this, MyApplication.Constr_disconpowdown+String.valueOf(blen),
								Toast.LENGTH_SHORT).show();
						DisConnectHandleUI();
					}
					
				});
	 
				spinner_pdatype.setOnItemSelectedListener(new OnItemSelectedListener()
				{

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						// TODO Auto-generated method stub
					 
						RfidPower.PDATYPE PT= RfidPower.PDATYPE.valueOf(arg2);
						myapp.Rpower=new RfidPower(PT);
						if(PT!= RfidPower.PDATYPE.NONE)
						{EditText et1=(EditText)findViewById(R.id.editText_adr);
					    et1.setText(myapp.Rpower.GetDevPath());
 
						}
						
						//*
						String  mod = android.os.Build.MODEL;
						Toast.makeText(Sub1TabActivity.this, mod,
								Toast.LENGTH_SHORT).show();
						/*if (mod.equals("h901"))
							myapp.Rpower = new RfidPower(PDATYPE.HANDEHUOER);
						else if (mod.equals("SHT37"))
							myapp.Rpower = new RfidPower(PDATYPE.XBANG_4G);
							//*/
					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub
						
					}
					
				});
				InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);  
				  
				inputMethodManager.hideSoftInputFromWindow(Sub1TabActivity.this.getCurrentFocus().getWindowToken(),  
				  
				InputMethodManager.HIDE_NOT_ALWAYS);
	}
	private void ConnectHandleUI()
	{
		try
		{
			Reader.READER_ERR er;
			myapp.Rparams=myapp.spf.ReadReaderParams();
			
			 if(myapp.Rparams.invpro.size()<1)
				 myapp.Rparams.invpro.add("GEN2");
		   
		    	 List<Reader.SL_TagProtocol> ltp=new ArrayList<Reader.SL_TagProtocol>();
				   for(int i=0;i<myapp.Rparams.invpro.size();i++)
				   {  if(myapp.Rparams.invpro.get(i).equals("GEN2"))
				    	{ ltp.add(Reader.SL_TagProtocol.SL_TAG_PROTOCOL_GEN2);
				    	 
				    	}
				   else if(myapp.Rparams.invpro.get(i).equals("6B"))
				    	{
				    	  ltp.add(Reader.SL_TagProtocol.SL_TAG_PROTOCOL_ISO180006B);
				    	 
				    	}
				   else if(myapp.Rparams.invpro.get(i).equals("IPX64"))
				    	{
				    	   ltp.add(Reader.SL_TagProtocol.SL_TAG_PROTOCOL_IPX64);
				    	   
				    	}
				   else if(myapp.Rparams.invpro.get(i).equals("IPX256"))
				    	{
				    	  ltp.add(Reader.SL_TagProtocol.SL_TAG_PROTOCOL_IPX256);
				    	  
				    	}
				   }

			Reader.Inv_Potls_ST ipst=myapp.Mreader.new Inv_Potls_ST();
					ipst.potlcnt=ltp.size();
					ipst.potls=new Reader.Inv_Potl[ipst.potlcnt];
			Reader.SL_TagProtocol[] stp=ltp.toArray(new Reader.SL_TagProtocol[ipst.potlcnt]);
					for(int i=0;i<ipst.potlcnt;i++)
					{
						Reader.Inv_Potl ipl=myapp.Mreader.new Inv_Potl();
						ipl.weight=30;
						ipl.potl=stp[i];
						ipst.potls[0]=ipl;
					}
					
				  er=myapp.Mreader.ParamSet(Reader.Mtr_Param.MTR_PARAM_TAG_INVPOTL, ipst);
			      Log.d("MYINFO", "Connected set pro:"+er.toString());
	 
			 er=myapp.Mreader.ParamSet(Reader.Mtr_Param.MTR_PARAM_READER_IS_CHK_ANT,
					 new int[]{myapp.Rparams.checkant});
			 Log.d("MYINFO", "Connected set checkant:"+er.toString());


			Reader.AntPowerConf apcf=myapp.Mreader.new AntPowerConf();
				apcf.antcnt=myapp.antportc;
				for(int i=0;i<apcf.antcnt;i++)
				{
					Reader.AntPower jaap=myapp.Mreader.new AntPower();
					jaap.antid=i+1;
					jaap.readPower =(short)myapp.Rparams.rpow[i];
					jaap.writePower=(short)myapp.Rparams.wpow[i];
					apcf.Powers[i]=jaap; 
				}
				
			myapp.Mreader.ParamSet(Reader.Mtr_Param.MTR_PARAM_RF_ANTPOWER, apcf);

			Reader.Region_Conf rre;
			 switch(myapp.Rparams.region)
			 {
			 case 0:
				 rre =Reader.Region_Conf.RG_PRC;
				 break;
			 case 1:
				 rre = Reader.Region_Conf.RG_NA;
				 break;
			 case 2:
				 rre=Reader.Region_Conf.RG_NONE;
				 break;
			 case 3:
				 rre=Reader.Region_Conf.RG_KR;
				 break;
			 case 4:
				 rre=Reader.Region_Conf.RG_EU;
				 break;
			 case 5:
			 case 6:
			 case 7:
			 case 8:
			 default:
				 rre=Reader.Region_Conf.RG_NONE;
				 break;
			 }
			 if(rre!=Reader.Region_Conf.RG_NONE)
			 {
				 er=myapp.Mreader.ParamSet(Reader.Mtr_Param.MTR_PARAM_FREQUENCY_REGION,rre);
			 }
  
			if(myapp.Rparams.frelen>0)
			{

				Reader.HoptableData_ST hdst=myapp.Mreader.new HoptableData_ST();
					hdst.lenhtb=myapp.Rparams.frelen;
					hdst.htb=myapp.Rparams.frecys;
					  er=myapp.Mreader.ParamSet
							(Reader.Mtr_Param.MTR_PARAM_FREQUENCY_HOPTABLE,hdst);
			}
			 
			er=myapp.Mreader.ParamSet(Reader.Mtr_Param.MTR_PARAM_POTL_GEN2_SESSION,
							new int[]{myapp.Rparams.session});
			er=myapp.Mreader.ParamSet(Reader.Mtr_Param.MTR_PARAM_POTL_GEN2_Q,
					new int[]{myapp.Rparams.qv});
			er=myapp.Mreader.ParamSet(Reader.Mtr_Param.MTR_PARAM_POTL_GEN2_WRITEMODE,
					new int[]{myapp.Rparams.wmode});
			er=myapp.Mreader.ParamSet(Reader.Mtr_Param.MTR_PARAM_POTL_GEN2_MAXEPCLEN,
					new int[]{myapp.Rparams.maxlen});
			er=myapp.Mreader.ParamSet(Reader.Mtr_Param.MTR_PARAM_POTL_GEN2_TARGET,
					new int[]{myapp.Rparams.target});
			 
			if(myapp.Rparams.filenable==1)
			{
				Reader.TagFilter_ST tfst=myapp.Mreader.new TagFilter_ST();
				 tfst.bank=myapp.Rparams.filbank;
				 tfst.fdata=new byte[myapp.Rparams.fildata.length()/2];
				 myapp.Mreader.Str2Hex(myapp.Rparams.fildata,
						 myapp.Rparams.fildata.length(), tfst.fdata);
				 tfst.flen=tfst.fdata.length*8;
				 tfst.startaddr=myapp.Rparams.filadr;
				 tfst.isInvert=myapp.Rparams.filisinver;
		       
				 myapp.Mreader.ParamSet(Reader.Mtr_Param.MTR_PARAM_TAG_FILTER, tfst);
			}
		 
			if(myapp.Rparams.emdenable==1)
			{
				Reader.EmbededData_ST edst = myapp.Mreader.new EmbededData_ST();
				 
				edst.accesspwd=null;
				edst.bank=myapp.Rparams.emdbank;
				edst.startaddr=myapp.Rparams.emdadr;
				edst.bytecnt=myapp.Rparams.emdbytec;
				edst.accesspwd=null;
			 
				er=myapp.Mreader.ParamSet(Reader.Mtr_Param.MTR_PARAM_TAG_EMBEDEDDATA,
						edst);
			}
			 
			er=myapp.Mreader.ParamSet
					(Reader.Mtr_Param.MTR_PARAM_TAGDATA_UNIQUEBYEMDDATA,
							new int[]{myapp.Rparams.adataq});
			er=myapp.Mreader.ParamSet
					(Reader.Mtr_Param.MTR_PARAM_TAGDATA_RECORDHIGHESTRSSI,
							new int[]{myapp.Rparams.rhssi});
			er=myapp.Mreader.ParamSet
					(Reader.Mtr_Param.MTR_PARAM_TAG_SEARCH_MODE,
							new int[]{myapp.Rparams.invw});
			
			TextView tv_module=(TextView)findViewById(R.id.textView_module);

			Reader.HardwareDetails val=myapp.Mreader.new HardwareDetails();
			er=myapp.Mreader.GetHardwareDetails(val);
			if(er==Reader.READER_ERR.MT_OK_ERR)
			{
				tv_module.setText(val.module.toString());
			}
		 
		}catch(Exception ex)
		{
			Log.d("MYINFO", ex.getMessage()+ex.toString()+ex.getStackTrace());
		}
		 this.button_connect.setEnabled(false);
	        this.button_disconnect.setEnabled(true);
	        TabWidget tw=myapp.tabHost.getTabWidget();
			 tw.getChildAt(1).setVisibility(View.VISIBLE);
			 tw.getChildAt(2).setVisibility(View.VISIBLE);
			 tw.getChildAt(3).setVisibility(View.VISIBLE);
			 myapp.tabHost.setCurrentTab(1);
	}
	private void DisConnectHandleUI()
	{
		button_disconnect.setEnabled(false);
		button_connect.setEnabled(true);
		 TabWidget tw=myapp.tabHost.getTabWidget();
		 tw.getChildAt(1).setVisibility(View.INVISIBLE);
		 tw.getChildAt(2).setVisibility(View.INVISIBLE);
		 tw.getChildAt(3).setVisibility(View.INVISIBLE);
		 TextView tv_module=(TextView)findViewById(R.id.textView_module);
		 tv_module.setText("");
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){   
	        if((System.currentTimeMillis()-myapp.exittime) > 2000){  
	            Toast.makeText(getApplicationContext(), MyApplication.Constr_Putandexit, Toast.LENGTH_SHORT).show();                                
	            myapp.exittime = System.currentTimeMillis();   
	        } else {
	            finish();
	           // System.exit(0);
	        }
	        return true;   
	    }
	    return super.onKeyDown(keyCode, event);
	}

}
