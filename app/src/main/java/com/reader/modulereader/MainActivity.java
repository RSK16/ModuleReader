package com.reader.modulereader;

import android.app.Application;
import android.app.TabActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TabWidget;
import android.widget.TextView;

import com.pow.api.cls.RfidPower;
import com.pow.api.cls.RfidPower.PDATYPE;
import com.reader.modulereader.entity.BaseCourse;
import com.reader.modulereader.entity.Bean;
import com.reader.modulereader.entity.Course;
import com.reader.modulereader.entity.LocationBean;
import com.reader.modulereader.entity.Notice;
import com.reader.modulereader.exception.ApiHttpException;
import com.reader.modulereader.function.AndroidWakeLock;
import com.reader.modulereader.function.MyAdapter;
import com.reader.modulereader.function.MyEpListAdapter;
import com.reader.modulereader.function.SPconfig;
import com.reader.modulereader.function.ScreenListener;
import com.reader.modulereader.mvp.MainContract;
import com.reader.modulereader.mvp.MainPresenter;
import com.reader.modulereader.utils.BaiduMapUtilByRacer;
import com.reader.modulereader.utils.DateUtil;
import com.reader.modulereader.utils.EventBusUtils;
import com.reader.modulereader.utils.MessageEvent;
import com.reader.modulereader.utils.StringUtil;
import com.reader.modulereader.utils.ToastUtil;
import com.uhf.api.cls.BackReadOption;
import com.uhf.api.cls.ErrInfo;
import com.uhf.api.cls.ReadExceptionListener;
import com.uhf.api.cls.ReadListener;
import com.uhf.api.cls.Reader;
import com.uhf.api.cls.Reader.AntPower;
import com.uhf.api.cls.Reader.AntPowerConf;
import com.uhf.api.cls.Reader.EmbededData_ST;
import com.uhf.api.cls.Reader.HardwareDetails;
import com.uhf.api.cls.Reader.HoptableData_ST;
import com.uhf.api.cls.Reader.Inv_Potl;
import com.uhf.api.cls.Reader.Inv_Potls_ST;
import com.uhf.api.cls.Reader.Mtr_Param;
import com.uhf.api.cls.Reader.READER_ERR;
import com.uhf.api.cls.Reader.Region_Conf;
import com.uhf.api.cls.Reader.SL_TagProtocol;
import com.uhf.api.cls.Reader.TAGINFO;
import com.uhf.api.cls.Reader.TagFilter_ST;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.DataOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

@SuppressWarnings("deprecation")
public class MainActivity<P extends MainContract.IMainPresenter> extends TabActivity implements MainContract.IMainView {

	TextView tv_once,tv_state,tv_tags,tv_costt,mTvNotice;
	Button button_read,button_stop,button_clear;
	private ListView listView;

	Map<String,TAGINFO> TagsMap=new LinkedHashMap<String,TAGINFO>();//有序
	private Handler handler = new Handler( );
	private MyApplication myapp;
	private SoundPool soundPool;
	TabHost tabHost;
	ScreenListener l;
	AndroidWakeLock Awl;
	int Test_count=0;
	boolean autostop;

	//RULE 默认平台
	boolean RULE_NOSELPT=false;
	PDATYPE PT=PDATYPE.CZ880;

	List<Map<String, ?>> ListMs = new ArrayList<Map<String, ?>>();
	MyAdapter Adapter;
	Map<String, String> h = new HashMap<String, String>();
	private GridView glList1;
    private BookAdapter bookAdapter1;
	private ArrayList<Course.CourseBean> todaylist = new ArrayList<>();
	private MainContract.IMainPresenter presenter;
	private Timer timer;
	private List<Course.BasecourseBean> mBasecourse = new ArrayList<>();
	private Bean mBean = new Bean();
	@Override
	public void showView(int viewState) {

	}

	protected void setPresenter() {
		presenter = new MainPresenter(this);
	}

	public MainContract.IMainPresenter getPresenter() {
		return presenter;
	}

	@Override
	public void showView(int viewState, ApiHttpException e) {

	}

	@Override
	public void getNoticeJsonServletSuccess(Notice notice) {
		if (notice != null && notice.notices != null && notice.notices.size() > 0) {
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < notice.notices.size(); i++) {
				if (i == 3) {
					break;
				}
				sb.append("【通知】 ："+notice.notices.get(i).name+" "+notice.notices.get(i).description+"     date: "+notice.notices.get(i).createTime+"\n");
			}
			mTvNotice.setText(sb.toString());
		}
	}

	@Override
	public void getNoticeJsonServletError(ApiHttpException e) {
		ToastUtil.toastS(e.getMessage());
	}

	@Override
	public void getCourseJsonServletSuccess(Course course) {
		todaylist.clear();
		if (course != null && course.course != null && course.course.size() > 0) {
			if (mBasecourse.size() == 0) {
				mBasecourse.addAll(course.basecourse);
			}
			for (Course.CourseBean courseBean : course.course) {
				if (courseBean.end.contains(DateUtil.getCurrentDate())) {
					todaylist.add(courseBean);
					for (Course.BasecourseBean basecourseBean : mBasecourse) {
						if (basecourseBean.COURSE.equals(courseBean.title)&&basecourseBean.readed==0) {
							basecourseBean.readed =1;
						}
					}
				}
			}
		}
		bookAdapter1.updateAll();
		bookAdapter1.notifyDataSetChanged();
	}

	@Override
	public void getCourseJsonServletError(ApiHttpException e) {
		ToastUtil.toastS(e.getMessage());
	}

	@Override
	public void addlnglatJsonServletSuccess() {

	}

	@Override
	public void addlnglatJsonServletError(ApiHttpException e) {
//		ToastUtil.toastS(e.getMessage());
	}

	@Override
	public void GetBaseCourseJsonServletSuccess(BaseCourse baseCourse) {

	}

	@Override
	public void GetBaseCourseJsonServletError(ApiHttpException e) {
		ToastUtil.toastS(e.getMessage());
	}


	@Override
	protected void onStart() {
		super.onStart();
		EventBusUtils.register(this);
	}

	@Subscribe(threadMode = ThreadMode.MAIN)
	public void MessageEvent(MessageEvent event) {
		if (!StringUtil.isEmpty(event.getlat())) {
			getPresenter().addlnglatJsonServlet(event.getlng(),event.getlat());
		}
	}

	TimerTask task= new TimerTask() {
		@Override
		public void run() {
			getPresenter().getNoticeJsonServlet();
			getPresenter().getCourseJsonServlet();
		}
	};

	public void locate() {
		BaiduMapUtilByRacer.locateByBaiduMap(getBaseContext(), 50*1000,
				new BaiduMapUtilByRacer.LocateListener() {

					@Override
					public void onLocateSucceed(LocationBean locationBean) {
						if (locationBean == null) {
							return;
						}
//						ToastUtil.toastS(locationBean.getLatitude()+","+locationBean.getLongitude());
						getPresenter().addlnglatJsonServlet(locationBean.getLongitude()+"",locationBean.getLatitude()+"");
					}

					@Override
					public void onLocateFiled() {
					}

					@Override
					public void onLocating() {
					}
				});
	}


	public class MyBroadcastReceiver extends BroadcastReceiver {
		public static final String TAG = "MyBroadcastReceiver";
		@Override
		public void onReceive(Context context, Intent intent) {

			TAGINFO tfs = myapp.Mreader.new TAGINFO();
			tfs.AntennaID=intent.getByteExtra("ANT",(byte) 1);
			tfs.CRC=intent.getByteArrayExtra("CRC");
			tfs.EmbededData=intent.getByteArrayExtra("EMD");
			tfs.EmbededDatalen=intent.getShortExtra("EML",(short) 0);

			tfs.EpcId=intent.getByteArrayExtra("EPC");
			tfs.Epclen=intent.getShortExtra("EPL",(short) 0);
			tfs.Frequency=intent.getIntExtra("FRQ",0);
			tfs.PC=intent.getByteArrayExtra("PC");
			tfs.Phase=intent.getIntExtra("PHA",0);
			tfs.ReadCnt=intent.getIntExtra("RDC",0);
			tfs.Res=intent.getByteArrayExtra("RES");
			tfs.RSSI=intent.getIntExtra("RSI",0);
			tfs.TimeStamp=intent.getIntExtra("TSP",0);


			TagsBufferResh(Reader.bytes_Hexstr(tfs.EpcId), tfs);
			int cll = TagsMap.size();
			if (cll < 0)
				cll = 0;
			tv_tags.setText(String.valueOf(cll));
			Adapter.notifyDataSetChanged();
		}
	}

	public Handler handler2 = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			Bundle bd=msg.getData();
			switch (msg.what)
			{
				case 0:
				{
					String count=bd.get("Msg_cnt").toString();
					tv_once.setText(count);
					tv_tags.setText(bd.get("Msg_all").toString());
					break;

				}
				case 1:
				{	  button_read.setText(MyApplication.Constr_READ);
					tv_state.setText(bd.get("Msg_error_1").toString());
					// button_stop.performClick();
					break;
				}
				case 2:
				{
					tv_state.setText(bd.get("Msg_error_2").toString());

					break;
				}
				case 3:
				{

					StopHandleUI();
					break;
				}
                case 4:
                {

                    startRead();
                    break;
                }
			}
		}
	};
	public static boolean runRootCommand(String command) {
		Process process = null;
		DataOutputStream os = null;

		try {
			process = Runtime.getRuntime().exec("sh");//Runtime.getRuntime().exec("su");
			os = new DataOutputStream(process.getOutputStream());
			os.writeBytes(command + "\n");
			os.writeBytes("exit\n");
			os.flush();
			process.waitFor();
		} catch (Exception e) {
			Log.d("Phone Link",  "su root - the device is not rooted,  error message： " + e.getMessage());
			return false;
		} finally {
			try {
				if(null != os) {
					os.close();
				}
				if(null != process) {
					process.destroy();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return true;
	}


	MyEpListAdapter mttab1adp;

	private boolean reconnect()
	{
		Log.d("MYINFO", "reconenct");
		boolean blen=myapp.Rpower.PowerUp();
		if(!blen)
			return false;
//		Toast.makeText(MainActivity.this, MyApplication.Constr_mainpu+String.valueOf(blen),
//				Toast.LENGTH_SHORT).show();

		READER_ERR er=myapp.Mreader.InitReader_Notype(myapp.Address, myapp.antportc);
		if(er==READER_ERR.MT_OK_ERR)
		{
			tv_state.setText("");
			myapp.needreconnect=false;
			try
			{

				myapp.Rparams=myapp.spf.ReadReaderParams();

				if(myapp.Rparams.invpro.size()<1)
					myapp.Rparams.invpro.add("GEN2");

				List<SL_TagProtocol> ltp=new ArrayList<SL_TagProtocol>();
				for(int i=0;i<myapp.Rparams.invpro.size();i++)
				{  if(myapp.Rparams.invpro.get(i).equals("GEN2"))
				{ ltp.add(SL_TagProtocol.SL_TAG_PROTOCOL_GEN2);

				}
				else if(myapp.Rparams.invpro.get(i).equals("6B"))
				{
					ltp.add(SL_TagProtocol.SL_TAG_PROTOCOL_ISO180006B);

				}
				else if(myapp.Rparams.invpro.get(i).equals("IPX64"))
				{
					ltp.add(SL_TagProtocol.SL_TAG_PROTOCOL_IPX64);

				}
				else if(myapp.Rparams.invpro.get(i).equals("IPX256"))
				{
					ltp.add(SL_TagProtocol.SL_TAG_PROTOCOL_IPX256);

				}
				}

				Inv_Potls_ST ipst=myapp.Mreader.new Inv_Potls_ST();
				ipst.potlcnt=ltp.size();
				ipst.potls=new Inv_Potl[ipst.potlcnt];
				SL_TagProtocol[] stp=ltp.toArray(new SL_TagProtocol[ipst.potlcnt]);
				for(int i=0;i<ipst.potlcnt;i++)
				{
					Inv_Potl ipl=myapp.Mreader.new Inv_Potl();
					ipl.weight=30;
					ipl.potl=stp[i];
					ipst.potls[0]=ipl;
				}

				er=myapp.Mreader.ParamSet(Mtr_Param.MTR_PARAM_TAG_INVPOTL, ipst);
				Log.d("MYINFO", "Connected set pro:"+er.toString());

				er=myapp.Mreader.ParamSet(Mtr_Param.MTR_PARAM_READER_IS_CHK_ANT,
						new int[]{myapp.Rparams.checkant});
				Log.d("MYINFO", "Connected set checkant:"+er.toString());


				AntPowerConf apcf=myapp.Mreader.new AntPowerConf();
				apcf.antcnt=myapp.antportc;
				for(int i=0;i<apcf.antcnt;i++)
				{
					AntPower jaap=myapp.Mreader.new AntPower();
					jaap.antid=i+1;
					jaap.readPower =(short)myapp.Rparams.rpow[i];
					jaap.writePower=(short)myapp.Rparams.wpow[i];
					apcf.Powers[i]=jaap;
				}

				myapp.Mreader.ParamSet(Mtr_Param.MTR_PARAM_RF_ANTPOWER, apcf);

				Region_Conf rre;
				switch(myapp.Rparams.region)
				{
					case 0:
						rre =Region_Conf.RG_PRC;
						break;
					case 1:
						rre = Region_Conf.RG_NA;
						break;
					case 2:
						rre=Region_Conf.RG_NONE;
						break;
					case 3:
						rre=Region_Conf.RG_KR;
						break;
					case 4:
						rre=Region_Conf.RG_EU;
						break;
					case 5:
					case 6:
					case 7:
					case 8:
					default:
						rre=Region_Conf.RG_NONE;
						break;
				}
				if(rre!=Region_Conf.RG_NONE)
				{
					er=myapp.Mreader.ParamSet(Mtr_Param.MTR_PARAM_FREQUENCY_REGION,rre);
				}

				if(myapp.Rparams.frelen>0)
				{

					HoptableData_ST hdst=myapp.Mreader.new HoptableData_ST();
					hdst.lenhtb=myapp.Rparams.frelen;
					hdst.htb=myapp.Rparams.frecys;
					er=myapp.Mreader.ParamSet
							(Mtr_Param.MTR_PARAM_FREQUENCY_HOPTABLE,hdst);
				}

				er=myapp.Mreader.ParamSet(Mtr_Param.MTR_PARAM_POTL_GEN2_SESSION,
						new int[]{myapp.Rparams.session});
				er=myapp.Mreader.ParamSet(Mtr_Param.MTR_PARAM_POTL_GEN2_Q,
						new int[]{myapp.Rparams.qv});
				er=myapp.Mreader.ParamSet(Mtr_Param.MTR_PARAM_POTL_GEN2_WRITEMODE,
						new int[]{myapp.Rparams.wmode});
				er=myapp.Mreader.ParamSet(Mtr_Param.MTR_PARAM_POTL_GEN2_MAXEPCLEN,
						new int[]{myapp.Rparams.maxlen});
				er=myapp.Mreader.ParamSet(Mtr_Param.MTR_PARAM_POTL_GEN2_TARGET,
						new int[]{myapp.Rparams.target});

				if(myapp.Rparams.filenable==1)
				{
					TagFilter_ST tfst=myapp.Mreader.new TagFilter_ST();
					tfst.bank=myapp.Rparams.filbank;
					tfst.fdata=new byte[myapp.Rparams.fildata.length()/2];
					myapp.Mreader.Str2Hex(myapp.Rparams.fildata,
							myapp.Rparams.fildata.length(), tfst.fdata);
					tfst.flen=tfst.fdata.length*8;
					tfst.startaddr=myapp.Rparams.filadr;
					tfst.isInvert=myapp.Rparams.filisinver;

					myapp.Mreader.ParamSet(Mtr_Param.MTR_PARAM_TAG_FILTER, tfst);
				}

				if(myapp.Rparams.emdenable==1)
				{
					EmbededData_ST edst = myapp.Mreader.new EmbededData_ST();

					edst.accesspwd=null;
					edst.bank=myapp.Rparams.emdbank;
					edst.startaddr=myapp.Rparams.emdadr;
					edst.bytecnt=myapp.Rparams.emdbytec;
					edst.accesspwd=null;

					er=myapp.Mreader.ParamSet(Mtr_Param.MTR_PARAM_TAG_EMBEDEDDATA,
							edst);
				}

				er=myapp.Mreader.ParamSet
						(Mtr_Param.MTR_PARAM_TAGDATA_UNIQUEBYEMDDATA,
								new int[]{myapp.Rparams.adataq});
				er=myapp.Mreader.ParamSet
						(Mtr_Param.MTR_PARAM_TAGDATA_RECORDHIGHESTRSSI,
								new int[]{myapp.Rparams.rhssi});
				er=myapp.Mreader.ParamSet
						(Mtr_Param.MTR_PARAM_TAG_SEARCH_MODE,
								new int[]{myapp.Rparams.invw});

			} catch (Exception ex) {
				Log.d("MYINFO",
						ex.getMessage() + ex.toString() + ex.getStackTrace());
			}
		}
		else
			return false;

		return true;
	}

	//****************************************事件方式读取标签
	MyBroadcastReceiver mBroadcastReceiver = new MyBroadcastReceiver();
	public static final String BROADCAST_ACTION = "com.reader.modulereader";
	READER_ERR StartReadTags()
	{
		//初始化结BackReadOption
		BackReadOption m_BROption=new BackReadOption();
		//本例只使用天线1进行盘存，如果要使用多个天线则只需要将使用的天线编
		//号赋值到数组ants中，例如同时使用天线1和2，则代码应该改为ants[0] = 1;
		//ants[1] = 2;antcnt = 2;
		int antcnt=1;
		int[] ants=new int[antcnt];
		ants[0]=1;
	     /*是否采用高速模式（目前只有slr11xx和slr12xx系列读写器才支持）,对于
	      *一般标签数量不大，速度不快的应用没有必要使用高速模式,本例没有设置
	      *使用高速模式
	      * */
		m_BROption.IsFastRead = false;//采用非高速模式盘存标签

		///非高速盘存模式下必须要设置的参数*******************************************
		//盘存周期,单位为ms，可根据实际使用的天线个数按照每个天线需要200ms
		//的方式计算得出,如果启用高速模式则此选项没有任何意义，可以设置为
		//任意值，或者干脆不设置
		m_BROption.ReadDuration = (short)(200 * antcnt);
		//盘存周期间的设备不工作时间,单位为ms,一般可设置为0，增加设备不工作
		//时间有利于节电和减少设备发热（针对某些使用电池供电或空间结构不利
		//于散热的情况会有帮助）
		m_BROption.ReadInterval = 0;
		//****************************************************************************

		//高速盘存模式参数设置********************************************************
		//以下为选择使用高速模式才起作用的选项参,照如下设置即可,如果使能高速
		//模式，即把IsFastRead设置为true则,只需取消以下被注释的代码即可
	 	/*
	 	//高速模式下为取得最佳效果设置为0即可
	 	m_BROption.FastReadDutyRation = 0;
	     //标签信息是否携带识别天线的编号
	     m_BROption.TMFlags.IsAntennaID = true;
	     //标签信息是否携带标签识别次数
	     m_BROption.TMFlags.IsReadCnt = false;
	     //标签信息是否携带识别标签时的信号强度
	     m_BROption.TMFlags.IsRSSI = false;
	     //标签信息是否携带时间戳
	     m_BROption.TMFlags.IsTimestamp = false;
	     //标签信息是否携带识别标签时的工作频点
	     m_BROption.TMFlags.IsFrequency = false;
	     //标签信息是否携带识别标签时同时读取的其它bank数据信息,如果要获取在
	     //盘存时同时读取其它bank的信息还必须设置MTR_PARAM_TAG_EMBEDEDDATA参数,
	     //（目前只有slr11xx和slr12xx系列读写器才支持）
	     m_BROption.TMFlags.IsEmdData = false;
	     //保留字段，可始终设置为0
	     m_BROption.TMFlags.IsRFU = false;
	     //*/

		return myapp.Mreader.StartReading(ants, antcnt, m_BROption);
	}
	//标签事件
	ReadListener RL=new ReadListener()
	{

		@Override
		public void tagRead(Reader r, final TAGINFO[] tag) {
			// TODO Auto-generated method stub

			Intent intent = new Intent();
			intent.setAction(BROADCAST_ACTION);
			for(int i=0;i<tag.length;i++)
			{
				intent.putExtra("ANT", tag[i].AntennaID);
				intent.putExtra("CRC", tag[i].CRC);
				intent.putExtra("EMD", tag[i].EmbededData);
				intent.putExtra("EML", tag[i].EmbededDatalen);

				intent.putExtra("EPC", tag[i].EpcId);
				intent.putExtra("EPL", tag[i].Epclen);
				intent.putExtra("FRQ", tag[i].Frequency);
				intent.putExtra("PC", tag[i].PC);
				intent.putExtra("PHA", tag[i].Phase);
				intent.putExtra("RDC", tag[i].ReadCnt);
				intent.putExtra("RES", tag[i].Res);
				intent.putExtra("RSI", tag[i].RSSI);
				intent.putExtra("TSP", tag[i].TimeStamp);

				sendBroadcast(intent);
			}

		}

	};
	ReadExceptionListener REL=new ReadExceptionListener()
	{

		@Override
		public void tagReadException(Reader r, final READER_ERR re) {
			// TODO Auto-generated method stub
			Message mes=new Message();
			mes.what=2;
			Bundle bd=new Bundle();
			bd.putString("Msg_error_2", re.toString());
			mes.setData(bd);
			handler2.sendMessage(mes);
		}

	};
	//************************************************//

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setLange();
		String apkRoot="chmod 777 "+getPackageCodePath();

		runRootCommand(apkRoot);
		soundPool= new SoundPool(10,AudioManager.STREAM_SYSTEM,5);
		soundPool.load(this,R.raw.beep333,1);
		Awl=new AndroidWakeLock((PowerManager) getSystemService(Context.POWER_SERVICE));

		Awl.WakeLock();
		tabHost = (TabHost) findViewById(android.R.id.tabhost);
        bookAdapter1 = new BookAdapter(mBasecourse, getBaseContext());
        glList1 = (GridView) findViewById(R.id.gl_list1);
		glList1.setAdapter(bookAdapter1);
		tabHost.setup();
		setPresenter();

		if(RULE_NOSELPT)
			;
		else
			tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator(MyApplication.Constr_CONNECT,
					getResources().getDrawable(R.drawable.ic_launcher)).setContent(
					new Intent(this, Sub1TabActivity.class)));

		TabSpec tp=tabHost.newTabSpec("tab2").setIndicator(MyApplication.Constr_INVENTORY)
				.setContent(R.id.tab2);

		tabHost.addTab(tp);
//		tabHost.addTab(tabHost.newTabSpec("tab3").setIndicator(MyApplication.Constr_RWLOP,
//				getResources().getDrawable(android.R.drawable.arrow_down_float)).setContent(
//				new Intent(this, Sub3TabActivity.class)));
//
//		tabHost.addTab(tabHost.newTabSpec("tab4").setIndicator(
//				MyApplication.Constr_set,
//				getResources().getDrawable(android.R.drawable.arrow_down_float)).setContent(
//				new Intent(this, Sub4TabActivity.class)));

		TabWidget tw=tabHost.getTabWidget();
		if(RULE_NOSELPT)
			;
		else
		{
			tw.getChildAt(1).setVisibility(View.INVISIBLE);
//			tw.getChildAt(2).setVisibility(View.INVISIBLE);
//			tw.getChildAt(3).setVisibility(View.INVISIBLE);
		}

		Application app=getApplication();
		myapp=(MyApplication)app;
		myapp.Mreader=new Reader();
		myapp.Rparams=myapp.new ReaderParams();
		Coname=MyApplication.Coname;

		mBroadcastReceiver = new MyBroadcastReceiver();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(BROADCAST_ACTION);
		registerReceiver(mBroadcastReceiver, intentFilter);

		myapp.tabHost=tabHost;
		button_read=(Button)findViewById(R.id.button_start);
		button_stop=(Button)findViewById(R.id.button_stop);
		button_stop.setEnabled(false);
		button_clear=(Button)findViewById(R.id.button_readclear);
		listView = (ListView) findViewById(R.id.listView_epclist);

		tv_once=(TextView)findViewById(R.id.textView_readoncecnt);
		tv_state=(TextView)findViewById(R.id.textView_invstate);
		tv_tags=(TextView)findViewById(R.id.textView_readallcnt);
		tv_costt=(TextView)findViewById(R.id.textView_costtime);
		mTvNotice=(TextView)findViewById(R.id.notice_text);
//		mTvNotice.setOnClickListener(v -> locate());
		locate();
		timer = new Timer();
		timer.schedule(task, 0,6*1000);
		for (int i = 0; i < Coname.length; i++)
			h.put(Coname[i], Coname[i]);
		myapp.needreconnect=false;
		l = new ScreenListener(this);

		if(RULE_NOSELPT)
		{
			myapp.spf=new SPconfig(this);
			myapp.Rpower=new RfidPower(PT);
			String path=myapp.Rpower.GetDevPath();//链接读写器地址
			if(myapp.Rpower.PowerUp())
			{
				READER_ERR er=myapp.Mreader.InitReader_Notype(path, 1);
				myapp.antportc=1;
				myapp.Address=path;
				if(er==READER_ERR.MT_OK_ERR)
				{
					ConnectHandleUI();
				}
			}
		}

		l.begin(new ScreenListener.ScreenStateListener() {

			@Override
			public void onScreenOn() {

			}

			@Override
			public void onScreenOff() {

				Log.d("MYINFO", "onScreenoff");
				if(button_stop.isEnabled())
				{   button_stop.performClick();}

				if(myapp.Mreader!=null)
				{	myapp.Mreader.CloseReader();
					myapp.needlisen=true;
				}

				if(myapp.Rpower!=null)
				{
					myapp.Rpower.PowerDown();
					myapp.needreconnect=true;
				}
			}
		});

//		button_read.setOnClickListener(new OnClickListener()
//		{
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
////                startRead();
//            }
//
//		});

//		button_stop.setOnClickListener(new OnClickListener()
//		{
//
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//
//				if (myapp.nostop) {
//					Log.d("MYINFO", "stop---");
//					READER_ERR er = myapp.Mreader.AsyncStopReading();
//					if (er != READER_ERR.MT_OK_ERR) {
//						Toast.makeText(MainActivity.this, MyApplication.Constr_nostopspreadfailed,
//								Toast.LENGTH_SHORT).show();
//						return;
//					}
//				}
//
//				if (myapp.ThreadMODE == 0)
//					handler.removeCallbacks(runnable_MainActivity);
//				else if (myapp.ThreadMODE == 1){
//					if (myapp.Mreader.StopReading() != READER_ERR.MT_OK_ERR)
//					{
//						Toast.makeText(MainActivity.this, MyApplication.Constr_nostopspreadfailed,
//								Toast.LENGTH_SHORT).show();
//						return;
//					}
//				}
//
//				if (myapp.Rpower.GetType() == PDATYPE.SCAN_ALPS_ANDROID_CUIUS2) {
//					try {
//						Thread.sleep(500);
//					} catch (InterruptedException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				}
//
//				Awl.ReleaseWakeLock();
//				autostop = false;
//
//				myapp.TagsMap.putAll(TagsMap);
//				StopHandleUI();
//
//			}
//
//		});

//		button_clear.setOnClickListener(new OnClickListener()
//		{
//
//			@Override
//			public void onClick(View arg0) {
//				// TODO Auto-generated method stub
//				if(Adapter!=null)
//				{
//					TagsMap.clear();
//					myapp.TagsMap.clear();
//					ListMs.clear();
//					// showlist();
//
//					ListMs.add(h);
//					Adapter.notifyDataSetChanged();
//				}
//
//				TextView et=(TextView)findViewById(R.id.textView_readoncecnt);
//				et.setText("0");
//
//				TextView et2=(TextView)findViewById(R.id.textView_readallcnt);
//				et2.setText("0");
//
//				TextView et3=(TextView)findViewById(R.id.textView_invstate);
//				et3.setText("...");
//
//				myapp.Curepc="";
//			}
//		});

		this.listView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
									long arg3) {
				// TODO Auto-generated method stub
				arg1.setBackgroundColor(Color.YELLOW);

				@SuppressWarnings("unchecked")
				HashMap<String,String> hm=(HashMap<String,String>)listView.getItemAtPosition(arg2);
				String epc=hm.get("EPC ID");
				myapp.Curepc=epc;

				for(int i=0;i<listView.getCount();i++)
				{
					if(i!=arg2)
					{
						View v=listView.getChildAt(i);
						if(v!=null)
						{ColorDrawable cd=(ColorDrawable) v.getBackground();
							if(Color.YELLOW==cd.getColor())
							{
								int[] colors = {Color.WHITE, Color.rgb(219, 238, 244) };//RGB颜色
								v.setBackgroundColor(colors[i % 2]);// 每隔item之间颜色不同
							}
						}
					}
				}
			}

		});

		tabHost.setOnTabChangedListener(new OnTabChangeListener(){

			@Override
			public void onTabChanged(String arg0) {
				int j=tabHost.getCurrentTab();
				if(RULE_NOSELPT)
				{
					if(j==1)
					{
						Sub3TabActivity.EditText_sub3fildata.setText(myapp.Curepc);
						Sub3TabActivity.EditText_sub3wdata.setText(myapp.Curepc);
					}
				}
				else{
					if(j==2)
					{
						Sub3TabActivity.EditText_sub3fildata.setText(myapp.Curepc);
						Sub3TabActivity.EditText_sub3wdata.setText(myapp.Curepc);
					}
				}
			}
		});

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                    EventBusUtils.post(new MessageEvent("sa"));
                    Thread.sleep(1000);
                    Message mes=new Message();
                    mes.what=4;
                    handler2.sendMessage(mes);
                } catch (InterruptedException e) {
                }
            }
        }).start();
	}

    public void startRead() {
        try{

            if(Adapter==null)
            {
                ListMs.add(h);
                Adapter  = new MyAdapter(getApplicationContext(),ListMs,
                        R.layout.listitemview_inv, Coname,new int[] { R.id.textView_readsort,
                        R.id.textView_readepc, R.id.textView_readcnt,R.id.textView_readant,
                        R.id.textView_readpro,R.id.textView_readrssi,R.id.textView_readfre,
                        R.id.textView_reademd});

                listView.setAdapter(Adapter);
            }

            boolean bl = true;
            if(myapp.needreconnect)
            {
                int c=0;
                do{
                    bl=reconnect();
                    if(!bl)
//                        Toast.makeText(MainActivity.this, MyApplication.Constr_sub1recfailed,
//                                Toast.LENGTH_SHORT).show();
                    c++;
                    if(c>0)
                        break;
                }
                while(true);
            }
            if(!bl)
                return;

            if(myapp.nostop)
            {
                READER_ERR er=myapp.Mreader.AsyncStartReading(myapp.Rparams.uants,
                        myapp.Rparams.uants.length, myapp.Rparams.option);
                if(er!= READER_ERR.MT_OK_ERR)
                {
//					Toast.makeText(MainActivity.this, MyApplication.Constr_nostopreadfailed,
//                        Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            if(myapp.ThreadMODE==0)
                handler.postDelayed(runnable_MainActivity,0);
            else if(myapp.ThreadMODE==1)
            {
                if(myapp.needlisen==true)
                //设置盘存到标签时的回调处理函数
                { myapp.Mreader.addReadListener(RL);
                    //*
                    //设置读写器发生错误时的回调处理函数
                    myapp.Mreader.addReadExceptionListener(REL);
                    myapp.needlisen=false;
                }

                //广播形式
                if (StartReadTags()!= READER_ERR.MT_OK_ERR)
                {
//                    Toast.makeText(MainActivity.this, MyApplication.Constr_nostopreadfailed,
//                            Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            myapp.TagsMap.clear();
            ReadHandleUI();

        }catch(Exception ex)
        {
//            Toast.makeText(MainActivity.this, MyApplication.Constr_nostopreadfailed+ex.getMessage(),
//                    Toast.LENGTH_SHORT).show();
        }
    }


    String[] Coname;//=new String[]{"序号","EPC ID","次数","天线","协议","RSSI","频率","附加数据"};

	private void TagsBufferResh(String key,TAGINFO tfs)
	{
		if (!TagsMap.containsKey(key))
		{
			TagsMap.put(key, tfs);

			//show
			Map<String, String> m = new HashMap<String, String>();
			m.put(Coname[0], String.valueOf(TagsMap.size()));

			String epcstr = Reader
					.bytes_Hexstr(tfs.EpcId);
			if (epcstr.length() < 24)
				epcstr = String.format("%-24s", epcstr);

			m.put(Coname[1], epcstr);

			String cs = m.get("次数");
			if (cs == null)
				cs = "0";
			int isc = Integer.parseInt(cs) + tfs.ReadCnt;

			m.put(Coname[2], String.valueOf(isc));
			m.put(Coname[3],
					String.valueOf(tfs.AntennaID));
			m.put(Coname[4], "");
			m.put(Coname[5], String.valueOf(tfs.RSSI));
			m.put(Coname[6],
					String.valueOf(tfs.Frequency));

			if (tfs.EmbededDatalen > 0) {
				char[] out = new char[tfs.EmbededDatalen * 2];
				myapp.Mreader.Hex2Str(tfs.EmbededData,
						tfs.EmbededDatalen, out);
				m.put(Coname[7], String.valueOf(out));

			} else
				m.put(Coname[7], "                 ");

			ListMs.add(m);
		}
		else {
			TAGINFO tf = TagsMap.get(key);

			String epcstr = key;
			if (epcstr.length() < 24)
				epcstr = String.format("%-24s", epcstr);

			for (int k = 0; k < ListMs.size(); k++) {
				@SuppressWarnings("unchecked")
				Map<String, String> m = (Map<String, String>) ListMs
						.get(k);
				if (m.get(Coname[1]).equals(epcstr)) {
					tf.ReadCnt += tfs.ReadCnt;
					tf.RSSI = tfs.RSSI;
					tf.Frequency = tfs.Frequency;

					m.put(Coname[2],
							String.valueOf(tf.ReadCnt));
					m.put(Coname[5],
							String.valueOf(tf.RSSI));
					m.put(Coname[6],
							String.valueOf(tf.Frequency));
					break;
				}
			}
		}
	}

	private boolean re;
	private int numAll=0;
	private Runnable runnable_MainActivity = new Runnable() {
		public void run() {

			String[] tag = null;
			int[] tagcnt = new int[1];
			tagcnt[0] = 0;

			int streadt = 0, enreadt = 0;
			synchronized (this) {
				// Log.d("MYINFO", "ManActivity..1");
				READER_ERR er;
				streadt = (int) System.currentTimeMillis();
				if (myapp.nostop) {
					er = myapp.Mreader.AsyncGetTagCount(tagcnt);
				} else {
					er = myapp.Mreader.TagInventory_Raw(myapp.Rparams.uants,
							myapp.Rparams.uants.length,
							(short) myapp.Rparams.readtime, tagcnt);
				}
				//Log.d("MYINFO","read:" + er.toString() + " cnt:"+ String.valueOf(tagcnt[0]));

				if (er == READER_ERR.MT_OK_ERR) {
					boolean be = false;
					if (tagcnt[0] > 0) {
						tv_once.setText(String.valueOf(tagcnt[0]));

						soundPool.play(1, 1, 1, 0, 0, 1);
						tag = new String[tagcnt[0]];
						for (int i = 0; i < tagcnt[0]; i++) {
							TAGINFO tfs = myapp.Mreader.new TAGINFO();
							if (myapp.Rpower.GetType() == PDATYPE.SCAN_ALPS_ANDROID_CUIUS2) {
								try {
									Thread.sleep(10);
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
							if (myapp.nostop)
								er = myapp.Mreader.AsyncGetNextTag(tfs);
							else
								er = myapp.Mreader.GetNextTag(tfs);

							//Log.d("MYINFO","get tag index:" + String.valueOf(i)+ " er:" + er.toString());
							if (er == READER_ERR.MT_HARDWARE_ALERT_ERR_BY_TOO_MANY_RESET) {

								tv_state.setText("error:"
										+ String.valueOf(er.value())
										+ er.toString());
								myapp.needreconnect = true;
								button_stop.performClick();
								autostop = true;
							}

							// Log.d("MYINFO","debug gettag:"+er.toString());
							// Log.d("MYINFO","debug tag:"+Reader.bytes_Hexstr(tfs.EpcId));

							if (er == READER_ERR.MT_OK_ERR) {
								tag[i] = Reader.bytes_Hexstr(tfs.EpcId);

								//刷新标签缓存
								TagsBufferResh(tag[i], tfs);

								mBean.count++;
								if (mBean.list == null) {
									mBean.list = new ArrayList<>();
								}
								if (mBean.list.size() > 0) {
									re = false;
									for (String s : mBean.list) {
										if (s.equals(tag[i])) {
											re = true;
										}
									}
									if (!re) {
										mBean.list.add(tag[i]);
									}
								} else {
									mBean.list.add(tag[i]);
								}
								if (mBean.count == 60) {
									for (Course.BasecourseBean basecourseBean : mBasecourse) {
										basecourseBean.current_read = false;
									}
									for (String s : mBean.list) {
										if (mBasecourse != null && mBasecourse.size() > 0) {
											for (Course.BasecourseBean basecourseBean : mBasecourse) {
												if (s.equals(basecourseBean.EPCID)) {//扫描到了标签
													basecourseBean.current_read = true;
													boolean isToday = false;
													for (Course.CourseBean courseBean : todaylist) {
														if (courseBean.title.equals(basecourseBean.COURSE)) {//扫描到的标签 是今天的
															isToday = true;
														}
													}
													if (isToday) {
														basecourseBean.readed = 2;
													} else {
														basecourseBean.readed = 3;
													}
												}
											}

										}
									}
									for (Course.BasecourseBean basecourseBean : mBasecourse) {
										if (!basecourseBean.current_read) {
											boolean b = false;
											for (Course.CourseBean courseBean : todaylist) {
												if (courseBean.title.equals(basecourseBean.COURSE)) {//扫描到的标签 是今天的
													b = true;
												}
											}
											if (b) {
												basecourseBean.readed = 1;
											} else {
												basecourseBean.readed = 0;
											}

										}
									}
									mBean.count = 0;
									mBean.list.clear();
									bookAdapter1.updateAll();
									bookAdapter1.notifyDataSetChanged();
								}
							}
						}

						enreadt = (int) System.currentTimeMillis();
						tv_costt.setText("  "
								+ String.valueOf(enreadt - streadt));
					} else {
						be = true;
					}
					if (be) {
						numAll++;
					} else {
						numAll = 0;
					}
					if (numAll == 6) {
						numAll = 0;
						for (Course.BasecourseBean basecourseBean : mBasecourse) {
							basecourseBean.current_read = false;
							boolean b = false;
							for (Course.CourseBean courseBean : todaylist) {
								if (courseBean.title.equals(basecourseBean.COURSE)) {//扫描到的标签 是今天的
									b = true;
								}

							}
							if (b) {
								basecourseBean.readed = 1;
							} else {
								basecourseBean.readed = 0;
							}
						}
						bookAdapter1.updateAll();
						bookAdapter1.notifyDataSetChanged();
					}
				} else {
					tv_state.setText("error:" + String.valueOf(er.value())
							+ " " + er.toString());

					if(myapp.nostop&&er!=READER_ERR.MT_OK_ERR)
					{
						tv_state.setText("error:" + String.valueOf(er.value())
								+ er.toString());
						//button_stop.performClick();
						handler.removeCallbacks(runnable_MainActivity);
						myapp.TagsMap.putAll(TagsMap);
						StopHandleUI();
						autostop = true;
					}

					if (er == READER_ERR.MT_HARDWARE_ALERT_ERR_BY_TOO_MANY_RESET) {
						tv_state.setText("error:" + String.valueOf(er.value())
								+ er.toString());
						myapp.needreconnect = true;
						button_stop.performClick();
						autostop = true;
					} else
						handler.postDelayed(this, myapp.Rparams.sleep);
					return;

				}
			}

			if (tag == null) {
				tag = new String[0];
			} else {
				Adapter.notifyDataSetChanged();
			}
			int cll = TagsMap.size();
			if (cll < 0)
				cll = 0;
			tv_tags.setText(String.valueOf(cll));

			handler.postDelayed(this, myapp.Rparams.sleep);
		}
	};

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if(!button_read.isEnabled())
		{
//			Toast.makeText(MainActivity.this, MyApplication.Constr_stopscan,
//					Toast.LENGTH_SHORT).show();
		}
		else{

			if (id == R.id.action_system) {

				Intent intent = new Intent(MainActivity.this, SubSystemActivity.class);
				startActivityForResult(intent, 0);
				return true;
			}
			else if(id==R.id.action_qt)
			{
				Intent intent = new Intent(MainActivity.this, SubQTActivity.class);
				startActivityForResult(intent, 0);
				return true;
			}
			else if(id==R.id.action_lasterr)
			{
				ErrInfo ei=new ErrInfo();
				myapp.Mreader.GetLastDetailError(ei);
//				Toast.makeText(MainActivity.this, MyApplication.Constr_connectfialed+
//						"last:"+String.valueOf(ei.derrcode)+" "+ei.errstr,Toast.LENGTH_SHORT).show();
			}
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
								 Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}

	private void ReadHandleUI()
	{
		this.button_read.setEnabled(false);
		this.button_stop.setEnabled(true);
		TabWidget tw=myapp.tabHost.getTabWidget();
		if(RULE_NOSELPT)
		{
			tw.getChildAt(1).setEnabled(false);
//			tw.getChildAt(2).setEnabled(false);
		}
		else{
			tw.getChildAt(0).setEnabled(false);
//			tw.getChildAt(2).setEnabled(false);
//			tw.getChildAt(3).setEnabled(false);
		}
	}
	private void StopHandleUI()
	{
		button_read.setEnabled(true);
		button_stop.setEnabled(false);
		TabWidget tw=myapp.tabHost.getTabWidget();
		if(RULE_NOSELPT)
		{
			tw.getChildAt(1).setEnabled(true);
//			tw.getChildAt(2).setEnabled(true);
		}
		else
		{tw.getChildAt(0).setEnabled(true);
//			tw.getChildAt(2).setEnabled(true);
//			tw.getChildAt(3).setEnabled(true);
		}
	}

	@Override
	public void onResume() {

		super.onResume();

	}

	@Override
	protected void onDestroy() {
		EventBusUtils.unregister(this);
		timer.cancel();
		Awl.ReleaseWakeLock();
		unregisterReceiver(mBroadcastReceiver);
		System.exit(0);
		super.onDestroy();
		if (presenter != null)
			presenter.detachView();
		BaiduMapUtilByRacer.stopAndDestroyLocate();
	}

	@Override
	public void onPause(){
		super.onPause();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN){
			if((System.currentTimeMillis()-myapp.exittime) > 2000){
//				Toast.makeText(getApplicationContext(), MyApplication.Constr_Putandexit, Toast.LENGTH_SHORT).show();
				myapp.exittime = System.currentTimeMillis();
			} else {
				if(handler!=null)
					handler.removeCallbacks(runnable_MainActivity);
				if(myapp.Mreader!=null)
				{myapp.Mreader.CloseReader();
					myapp.needlisen=true;
				}

				myapp.Rpower.PowerDown();
				finish();
				// System.exit(0);
			}
			return true;
		}
		else if(keyCode==139&&myapp.Rpower.GetType()==PDATYPE.CHAINWAY)
		{
			if(button_read.isEnabled())
			{
				button_read.performClick();
			}
			else
				button_stop.performClick();
		}

		return super.onKeyDown(keyCode, event);
	}


	private void ConnectHandleUI()
	{
		try
		{
			READER_ERR er;
			myapp.Rparams=myapp.spf.ReadReaderParams();

			if(myapp.Rparams.invpro.size()<1)
				myapp.Rparams.invpro.add("GEN2");

			List<SL_TagProtocol> ltp=new ArrayList<SL_TagProtocol>();
			for(int i=0;i<myapp.Rparams.invpro.size();i++)
			{  if(myapp.Rparams.invpro.get(i).equals("GEN2"))
			{ ltp.add(SL_TagProtocol.SL_TAG_PROTOCOL_GEN2);

			}
			else if(myapp.Rparams.invpro.get(i).equals("6B"))
			{
				ltp.add(SL_TagProtocol.SL_TAG_PROTOCOL_ISO180006B);

			}
			else if(myapp.Rparams.invpro.get(i).equals("IPX64"))
			{
				ltp.add(SL_TagProtocol.SL_TAG_PROTOCOL_IPX64);

			}
			else if(myapp.Rparams.invpro.get(i).equals("IPX256"))
			{
				ltp.add(SL_TagProtocol.SL_TAG_PROTOCOL_IPX256);

			}
			}

			Inv_Potls_ST ipst=myapp.Mreader.new Inv_Potls_ST();
			ipst.potlcnt=ltp.size();
			ipst.potls=new Inv_Potl[ipst.potlcnt];
			SL_TagProtocol[] stp=ltp.toArray(new SL_TagProtocol[ipst.potlcnt]);
			for(int i=0;i<ipst.potlcnt;i++)
			{
				Inv_Potl ipl=myapp.Mreader.new Inv_Potl();
				ipl.weight=30;
				ipl.potl=stp[i];
				ipst.potls[0]=ipl;
			}

			er=myapp.Mreader.ParamSet(Mtr_Param.MTR_PARAM_TAG_INVPOTL, ipst);
			Log.d("MYINFO", "Connected set pro:"+er.toString());

			er=myapp.Mreader.ParamSet(Mtr_Param.MTR_PARAM_READER_IS_CHK_ANT,
					new int[]{myapp.Rparams.checkant});
			Log.d("MYINFO", "Connected set checkant:"+er.toString());


			AntPowerConf apcf=myapp.Mreader.new AntPowerConf();
			apcf.antcnt=myapp.antportc;
			for(int i=0;i<apcf.antcnt;i++)
			{
				AntPower jaap=myapp.Mreader.new AntPower();
				jaap.antid=i+1;
				jaap.readPower =(short)myapp.Rparams.rpow[i];
				jaap.writePower=(short)myapp.Rparams.wpow[i];
				apcf.Powers[i]=jaap;
			}

			myapp.Mreader.ParamSet(Mtr_Param.MTR_PARAM_RF_ANTPOWER, apcf);

			Region_Conf rre;
			switch(myapp.Rparams.region)
			{
				case 0:
					rre =Region_Conf.RG_PRC;
					break;
				case 1:
					rre = Region_Conf.RG_NA;
					break;
				case 2:
					rre=Region_Conf.RG_NONE;
					break;
				case 3:
					rre=Region_Conf.RG_KR;
					break;
				case 4:
					rre=Region_Conf.RG_EU;
					break;
				case 5:
				case 6:
				case 7:
				case 8:
				default:
					rre=Region_Conf.RG_NONE;
					break;
			}
			if(rre!=Region_Conf.RG_NONE)
			{
				er=myapp.Mreader.ParamSet(Mtr_Param.MTR_PARAM_FREQUENCY_REGION,rre);
			}

			if(myapp.Rparams.frelen>0)
			{

				HoptableData_ST hdst=myapp.Mreader.new HoptableData_ST();
				hdst.lenhtb=myapp.Rparams.frelen;
				hdst.htb=myapp.Rparams.frecys;
				er=myapp.Mreader.ParamSet
						(Mtr_Param.MTR_PARAM_FREQUENCY_HOPTABLE,hdst);
			}

			er=myapp.Mreader.ParamSet(Mtr_Param.MTR_PARAM_POTL_GEN2_SESSION,
					new int[]{myapp.Rparams.session});
			er=myapp.Mreader.ParamSet(Mtr_Param.MTR_PARAM_POTL_GEN2_Q,
					new int[]{myapp.Rparams.qv});
			er=myapp.Mreader.ParamSet(Mtr_Param.MTR_PARAM_POTL_GEN2_WRITEMODE,
					new int[]{myapp.Rparams.wmode});
			er=myapp.Mreader.ParamSet(Mtr_Param.MTR_PARAM_POTL_GEN2_MAXEPCLEN,
					new int[]{myapp.Rparams.maxlen});
			er=myapp.Mreader.ParamSet(Mtr_Param.MTR_PARAM_POTL_GEN2_TARGET,
					new int[]{myapp.Rparams.target});

			if(myapp.Rparams.filenable==1)
			{
				TagFilter_ST tfst=myapp.Mreader.new TagFilter_ST();
				tfst.bank=myapp.Rparams.filbank;
				tfst.fdata=new byte[myapp.Rparams.fildata.length()/2];
				myapp.Mreader.Str2Hex(myapp.Rparams.fildata,
						myapp.Rparams.fildata.length(), tfst.fdata);
				tfst.flen=tfst.fdata.length*8;
				tfst.startaddr=myapp.Rparams.filadr;
				tfst.isInvert=myapp.Rparams.filisinver;

				myapp.Mreader.ParamSet(Mtr_Param.MTR_PARAM_TAG_FILTER, tfst);
			}

			if(myapp.Rparams.emdenable==1)
			{
				EmbededData_ST edst = myapp.Mreader.new EmbededData_ST();

				edst.accesspwd=null;
				edst.bank=myapp.Rparams.emdbank;
				edst.startaddr=myapp.Rparams.emdadr;
				edst.bytecnt=myapp.Rparams.emdbytec;
				edst.accesspwd=null;

				er=myapp.Mreader.ParamSet(Mtr_Param.MTR_PARAM_TAG_EMBEDEDDATA,
						edst);
			}

			er=myapp.Mreader.ParamSet
					(Mtr_Param.MTR_PARAM_TAGDATA_UNIQUEBYEMDDATA,
							new int[]{myapp.Rparams.adataq});
			er=myapp.Mreader.ParamSet
					(Mtr_Param.MTR_PARAM_TAGDATA_RECORDHIGHESTRSSI,
							new int[]{myapp.Rparams.rhssi});
			er=myapp.Mreader.ParamSet
					(Mtr_Param.MTR_PARAM_TAG_SEARCH_MODE,
							new int[]{myapp.Rparams.invw});

			TextView tv_module=(TextView)findViewById(R.id.textView_module);
			HardwareDetails val=myapp.Mreader.new HardwareDetails();
			er=myapp.Mreader.GetHardwareDetails(val);
			if(er==READER_ERR.MT_OK_ERR)
			{
				tv_module.setText(val.module.toString());
			}

		}catch(Exception ex)
		{
			Log.d("MYINFO", ex.getMessage()+ex.toString()+ex.getStackTrace());
		}

	}

	private void setLange()
	{
		//Toast.makeText(getApplicationContext(), "setl1", Toast.LENGTH_SHORT).show();                           
		Locale locale =getApplicationContext().getResources().getConfiguration().locale;
		String language = locale.getLanguage();
		MyApplication.Constr_READ =this.getString(R.string.Constr_READ);
		MyApplication.Constr_CONNECT = this.getString(R.string.Constr_CONNECT);
		MyApplication.Constr_INVENTORY = this.getString(R.string.Constr_INVENTORY);
		MyApplication.Constr_RWLOP =this.getString(R.string.Constr_RWLOP);
		MyApplication.Constr_set = this.getString(R.string.Constr_set);
		MyApplication.Constr_SetFaill =this.getString(R.string.Constr_SetFaill);
		MyApplication.Constr_GetFaill = this.getString(R.string.Constr_GetFaill);
		MyApplication.Constr_SetOk=this.getString(R.string.Constr_SetOk);
		MyApplication.Constr_unsupport=this.getString(R.string.Constr_unsupport);
		MyApplication.Constr_Putandexit = this.getString(R.string.Constr_Putandexit);

		MyApplication.Constr_stopscan = this.getString(R.string.Constr_stopscan);
		MyApplication.Constr_hadconnected = this.getString(R.string.Constr_hadconnected);
		MyApplication.Constr_plsetuuid = this.getString(R.string.Constr_plsetuuid);
		MyApplication.Constr_pwderror = this.getString(R.string.Constr_pwderror);
		MyApplication.Constr_search =this.getString(R.string.Constr_search);
		MyApplication.Constr_stop = this.getString(R.string.Constr_stop);

		MyApplication.Constr_createreaderok =this.getString(R.string.Constr_createreaderok);

		MyApplication.Constr_sub3readmem = this.getString(R.string.Constr_sub3readmem);
		MyApplication.Constr_sub3writemem = this.getString(R.string.Constr_sub3writemem);
		MyApplication.Constr_sub3lockkill = this.getString(R.string.Constr_sub3lockkill);
		MyApplication.Constr_sub3readfail = this.getString(R.string.Constr_sub3readfail);
		MyApplication.Constr_sub3nodata = this.getString(R.string.Constr_sub3nodata);
		MyApplication.Constr_sub3wrtieok = this.getString(R.string.Constr_sub3wrtieok);
		MyApplication.Constr_sub3writefail = this.getString(R.string.Constr_sub3writefail);
		MyApplication.Constr_sub3lockok = this.getString(R.string.Constr_sub3lockok);
		MyApplication.Constr_sub3lockfail =this.getString(R.string.Constr_sub3lockfail);
		MyApplication.Constr_sub3killok = this.getString(R.string.Constr_sub3killok);
		MyApplication.Constr_sub3killfial = this.getString(R.string.Constr_sub3killfial);

		MyApplication.Auto=this.getString(R.string.Auto);
		MyApplication.No=this.getString(R.string.No);
		MyApplication.Constr_sub4invenpra=this.getString(R.string.Constr_sub4invenpra);
		MyApplication.Constr_sub4antpow=this.getString(R.string.Constr_sub4antpow);
		MyApplication.Constr_sub4regionfre=this.getString(R.string.Constr_sub4regionfre);
		MyApplication.Constr_sub4gen2opt=this.getString(R.string.Constr_sub4gen2opt);
		MyApplication.Constr_sub4invenfil=this.getString(R.string.Constr_sub4invenfil);
		MyApplication.Constr_sub4addidata=this.getString(R.string.Constr_sub4addidata);
		MyApplication.Constr_sub4others=this.getString(R.string.Constr_sub4others);
		MyApplication.Constr_sub4quickly=this.getString(R.string.Constr_sub4quickly);
		MyApplication.Constr_sub4setmodefail=this.getString(R.string.Constr_sub4setmodefail);
		MyApplication.Constr_sub4setokresettoab=this.getString(R.string.Constr_sub4setokresettoab);
		MyApplication.Constr_sub4ndsapow=this.getString(R.string.Constr_sub4ndsapow);
		MyApplication.Constr_sub4unspreg=this.getString(R.string.Constr_sub4unspreg);

		MyApplication.Constr_subblmode = this.getString(R.string.Constr_subblmode);
		MyApplication.Constr_subblinven = this.getString(R.string.Constr_subblinven);
		MyApplication.Constr_subblfil = this.getString(R.string.Constr_subblfil);
		MyApplication.Constr_subblfre = this.getString(R.string.Constr_subblfre);
		MyApplication.Constr_subblnofre = this.getString(R.string.Constr_subblnofre);

		MyApplication.Constr_subcsalterpwd=this.getString(R.string.Constr_subcsalterpwd);
		MyApplication.Constr_subcslockwpwd=this.getString(R.string.Constr_subcslockwpwd);
		MyApplication.Constr_subcslockwoutpwd=this.getString(R.string.Constr_subcslockwoutpwd);
		MyApplication.Constr_subcsplsetimeou=this.getString(R.string.Constr_subcsplsetimeou);
		MyApplication.Constr_subcsputcnpwd=this.getString(R.string.Constr_subcsputcnpwd);
		MyApplication.Constr_subcsplselreg=this.getString(R.string.Constr_subcsplselreg);
		MyApplication.Constr_subcsopfail=this.getString(R.string.Constr_subcsopfail);
		MyApplication.Constr_subcsputcurpwd=this.getString(R.string.Constr_subcsputcurpwd);

		MyApplication.Constr_subdbdisconnreconn = this.getString(R.string.Constr_subdbdisconnreconn);
		MyApplication.Constr_subdbhadconnected = this.getString(R.string.Constr_subdbhadconnected);
		MyApplication.Constr_subdbconnecting = this.getString(R.string.Constr_subdbconnecting);
		MyApplication.Constr_subdbrev = this.getString(R.string.Constr_subdbrev);
		MyApplication.Constr_subdbstop =this.getString(R.string.Constr_subdbstop);
		MyApplication.Constr_subdbdalennot = this.getString(R.string.Constr_subdbdalennot);
		MyApplication.Constr_subdbplpuhexchar = this.getString(R.string.Constr_subdbplpuhexchar);

		MyApplication.Constr_subsysaveok = this.getString(R.string.Constr_subsysaveok);
		MyApplication.Constr_subsysout = this.getString(R.string.Constr_subsysout);
		MyApplication.Constr_subsysreavaid =this.getString(R.string.Constr_subsysreavaid);
		MyApplication.Constr_sub1recfailed = this.getString(R.string.Constr_sub1recfailed);
		MyApplication.Constr_subsysavefailed =this.getString(R.string.Constr_subsysavefailed);
		MyApplication.Constr_subsysexefin =this.getString(R.string.Constr_subsysexefin);
		MyApplication.Constr_sub1adrno=this.getString(R.string.Constr_sub1adrno);
		MyApplication.Constr_sub1pdtsl=this.getString(R.string.Constr_sub1pdtsl);
		MyApplication.Constr_mainpu=this.getString(R.string.Constr_mainpu);
		MyApplication.Constr_nostopstreadfailed=this.getString(R.string.Constr_nostopstreadfailed);
		MyApplication.Constr_nostopspreadfailed=this.getString(R.string.Constr_nostopspreadfailed);
		MyApplication.Constr_nostopreadfailed=this.getString(R.string.Constr_nostopreadfailed);
		MyApplication.Constr_connectok=this.getString(R.string.Constr_connectok);
		MyApplication.Constr_connectfialed=this.getString(R.string.Constr_connectfialed);
		MyApplication.Constr_disconpowdown=this.getString(R.string.Constr_disconpowdown);
		MyApplication.Constr_ok=this.getString(R.string.Constr_ok);
		MyApplication.Constr_failed=this.getString(R.string.Constr_failed);
		MyApplication.Constr_excep=this.getString(R.string.Constr_excep);
		MyApplication.Constr_setcep=this.getString(R.string.Constr_setcep);
		MyApplication.Constr_getcep=this.getString(R.string.Constr_getcep);
		MyApplication.Constr_killok=this.getString(R.string.Constr_killok);
		MyApplication.Constr_killfailed=this.getString(R.string.Constr_killfailed);
		MyApplication.Constr_psiant=this.getString(R.string.Constr_psiant);
		MyApplication.Constr_selpro=this.getString(R.string.Constr_selpro);
		if (language.endsWith("zh"))
		{

		}
		else
		{
			MyApplication.Coname = new String[] { "Sort", "EPC ID", "Count", "Ant",
					"Protocol", "RSSI", "Frequency", "Addition " };

			MyApplication.pdaatpot = new String[]{ "one ant", "two ants", "three ants", "fours ants" };

			MyApplication.spibank=new String[]{"reserver bank","EPC bank","TID bank","user bank"};
			MyApplication.spifbank=new String[]{"EPC bank","TID bank","User bank"};
			MyApplication.spilockbank=new String[]{"Access password","Kill password","EPCbank","TIDbank","USERbank"};
			MyApplication.spilocktype=new String[]{"Unlock","Temporary lock","Permanent lock"};

			MyApplication.spireg =new String[] { "China", "NA", "Japan", "Korea", "Europe", "Europe2", "Europe3", "India",
					"Canada", "ALL", "China2" };
			MyApplication.spinvmo=new String[]{"General mode","High speed mode"};
			MyApplication.spitari=new String[]{"25microsecond","12.5microsecond","6.25microsecond"};
			MyApplication.spiwmod=new String[]{"word write","block write"};

			MyApplication.cusreadwrite=new String[]{"read opration","wirte opration"};
			MyApplication.cuslockunlock=new String[]{"lock","unlock"};

		}

	}
}
