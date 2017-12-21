package com.reader.modulereader;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import com.reader.modulereader.function.MyAdapter;
import com.reader.modulereader.function.SPconfig;
import com.uhf.api.cls.ReadListener;
import com.uhf.api.cls.Reader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Main2Activity extends AppCompatActivity {

    public static final String BROADCAST_ACTION2 = "com.reader.modulereader";

    private MyApplication myapp;
    Map<String,Reader.TAGINFO> TagsMap=new LinkedHashMap<String,Reader.TAGINFO>();//有序
    List<Map<String, ?>> ListMs = new ArrayList<Map<String, ?>>();

    MyBroadcastReceiver mBroadcastReceiver = new MyBroadcastReceiver();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        myapp=(MyApplication) getApplication();
        myapp.Mreader=new Reader();

        myapp.spf=new SPconfig(this);

        mBroadcastReceiver = new MyBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BROADCAST_ACTION2);
        registerReceiver(mBroadcastReceiver, intentFilter);
        initData();
    }


    private void initData() {
        try{

            boolean bl = true;
            if(myapp.needreconnect)
            {
                int c=0;
                do{
                    bl=reconnect();
                    if(!bl)
                        Toast.makeText(Main2Activity.this, MyApplication.Constr_sub1recfailed,
                                Toast.LENGTH_SHORT).show();
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
                Reader.READER_ERR er=myapp.Mreader.AsyncStartReading(myapp.Rparams.uants,
                        myapp.Rparams.uants.length, myapp.Rparams.option);
                if(er!= Reader.READER_ERR.MT_OK_ERR)
                {	 Toast.makeText(Main2Activity.this, MyApplication.Constr_nostopreadfailed,
                        Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            if(myapp.ThreadMODE==0) {
//                handler.postDelayed(runnable_MainActivity,0);
            }else if(myapp.ThreadMODE==1)
            {
                if(myapp.needlisen==true)
                //设置盘存到标签时的回调处理函数
                { myapp.Mreader.addReadListener(RL);
                    //*
                    //设置读写器发生错误时的回调处理函数
//                    myapp.Mreader.addReadExceptionListener(REL);
                    myapp.needlisen=false;
                }

                //广播形式
//                if (StartReadTags()!= Reader.READER_ERR.MT_OK_ERR)
//                {
//                    Toast.makeText(Main2Activity.this, MyApplication.Constr_nostopreadfailed,
//                            Toast.LENGTH_SHORT).show();
//                    return;
//                }
            }

            myapp.TagsMap.clear();

        }catch(Exception ex)
        {
            Toast.makeText(Main2Activity.this, MyApplication.Constr_nostopreadfailed+ex.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }
    }

    private boolean reconnect()
    {
        Log.d("MYINFO", "reconenct");
        boolean blen=myapp.Rpower.PowerUp();
        if(!blen)
            return false;
        Toast.makeText(Main2Activity.this, MyApplication.Constr_mainpu+String.valueOf(blen),
                Toast.LENGTH_SHORT).show();

        Reader.READER_ERR er=myapp.Mreader.InitReader_Notype(myapp.Address, myapp.antportc);
        if(er== Reader.READER_ERR.MT_OK_ERR)
        {
//            tv_state.setText("");
            myapp.needreconnect=false;
            try
            {

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
                        rre = Reader.Region_Conf.RG_PRC;
                        break;
                    case 1:
                        rre = Reader.Region_Conf.RG_NA;
                        break;
                    case 2:
                        rre= Reader.Region_Conf.RG_NONE;
                        break;
                    case 3:
                        rre= Reader.Region_Conf.RG_KR;
                        break;
                    case 4:
                        rre= Reader.Region_Conf.RG_EU;
                        break;
                    case 5:
                    case 6:
                    case 7:
                    case 8:
                    default:
                        rre= Reader.Region_Conf.RG_NONE;
                        break;
                }
                if(rre!= Reader.Region_Conf.RG_NONE)
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

            } catch (Exception ex) {
                Log.d("MYINFO",
                        ex.getMessage() + ex.toString() + ex.getStackTrace());
            }
        }
        else
            return false;

        return true;
    }

    public class MyBroadcastReceiver extends BroadcastReceiver {
        public static final String TAG = "MyBroadcastReceiver";
        @Override
        public void onReceive(Context context, Intent intent) {

            Reader.TAGINFO tfs = myapp.Mreader.new TAGINFO();
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
//            tv_tags.setText(String.valueOf(cll));//总共读到的数量
//            Adapter.notifyDataSetChanged();
        }
    }


    private void TagsBufferResh(String key,Reader.TAGINFO tfs)
    {
        if (!TagsMap.containsKey(key))
        {
            TagsMap.put(key, tfs);

            //show
            Map<String, String> m = new HashMap<String, String>();
//            m.put(Coname[0], String.valueOf(TagsMap.size()));

            String epcstr = Reader
                    .bytes_Hexstr(tfs.EpcId);
            if (epcstr.length() < 24)
                epcstr = String.format("%-24s", epcstr);

//            m.put(Coname[1], epcstr);
            ListMs.add(m);
        }
    }

    //标签事件
    ReadListener RL=new ReadListener()
    {

        @Override
        public void tagRead(Reader r, final Reader.TAGINFO[] tag) {
            // TODO Auto-generated method stub

            Intent intent = new Intent();
            intent.setAction(BROADCAST_ACTION2);
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

    @Override
    protected void onDestroy() {
        unregisterReceiver(mBroadcastReceiver);
        System.exit(0);
        super.onDestroy();
    }
}
