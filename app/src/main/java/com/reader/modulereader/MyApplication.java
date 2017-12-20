package com.reader.modulereader;

import android.app.Application;
import android.widget.TabHost;

import com.pow.api.cls.RfidPower;
import com.reader.modulereader.function.SPconfig;
import com.uhf.api.cls.Reader;
import com.uhf.api.cls.Reader.TAGINFO;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MyApplication extends Application{

	    //����
		//*
		public static String Constr_READ = "��";
		public static String Constr_CONNECT = "����";
		public static String Constr_INVENTORY = "�̵�";
		public static String Constr_RWLOP = "��д��";
		public static String Constr_set = "����";
		public static String Constr_SetFaill = "����ʧ�ܣ�";
		public static String Constr_GetFaill = "��ȡʧ�ܣ�";
		public static String Constr_SetOk="���óɹ�";
		public static String Constr_unsupport="��֧��";
		public static String Constr_Putandexit = "�ٰ�һ���˳�����";
		public static String[] Coname = new String[] { "���", "EPC ID", "����", "����",
				"Э��", "RSSI", "Ƶ��", "�������� " };
		public static String Constr_stopscan = "����ֹͣɨ��";
		public static String Constr_hadconnected = "�Ѿ�����";
		public static String Constr_plsetuuid = "�����ú�UUID:";
		public static String Constr_pwderror = "�������";
		public static String Constr_search = "����";
		public static String Constr_stop = "ֹͣ";

		public static String Constr_createreaderok = "��д������ʧ��";
		public static String[] pdaatpot = { "һ����", "˫����", "������", "������" };
		    
		public static String[] spibank={"������","EPC��","TID��","�û���"};
		public static String[] spifbank={"EPC��","TID��","�û���"}; 
		public static String[] spilockbank={"��������","��������","EPCbank","TIDbank","USERbank"}; 
		public static String[] spilocktype={"������","��ʱ����","��������"};
		public static String Constr_sub3readmem = "����ǩ";
		public static String Constr_sub3writemem = "д��ǩ";
		public static String Constr_sub3lockkill = "��������";
		public static String Constr_sub3readfail = "��ʧ��:";
		public static String Constr_sub3nodata = "������";
		public static String Constr_sub3wrtieok = "д�ɹ�";
		public static String Constr_sub3writefail = "дʧ��:";
		public static String Constr_sub3lockok = "���ɹ�";
		public static String Constr_sub3lockfail = "��ʧ��:";
		public static String Constr_sub3killok = "���ٳɹ�";
		public static String Constr_sub3killfial = "����ʧ��:";
		
		//String[] spireg={"�й�","����","�ձ�","����","ŷ��","ӡ��","���ô�","ȫƵ��"
		//		,"�й�2"};
		public static	String[] spireg = { "�й�", "����", "�ձ�", "����", "ŷ��", "ŷ��2", "ŷ��3", "ӡ��",
	    "���ô�", "ȫƵ��", "�й�2" };	
		public static String[] spinvmo={"��ͨģʽ","����ģʽ"};
		public static String[] spitari={"25΢��","12.5΢��","6.25΢��"};
		public static  String[] spiwmod={"��д","��д"};
		public static String Auto="�Զ�";
		public static String No="��";
	    public static String Constr_sub4invenpra="�̵����";
	    public static String Constr_sub4antpow = "���߹���";
	    public static String Constr_sub4regionfre = "����Ƶ��";
	    public static String Constr_sub4gen2opt = "Gen2��";
	    public static String Constr_sub4invenfil = "�̵����";
	    public static String Constr_sub4addidata = "��������";
	    public static String Constr_sub4others = "��������";
	    public static String Constr_sub4quickly = "����ģʽ";
	    public static String Constr_sub4setmodefail = "����ģʽʧ��";
	    public static String Constr_sub4setokresettoab = "���óɹ���������д����Ч";
	    public static String Constr_sub4ndsapow = "���豸��Ҫ����һ��";
	    public static String Constr_sub4unspreg = "��֧�ֵ�����";

	    public static String[] spiregbs = { "����", "�й�", "ŷƵ", "�й�2" };
		public static String Constr_subblmode = "ģʽ";
		public static String Constr_subblinven = "�̵�";
		public static String Constr_subblfil = "����";
		public static String Constr_subblfre = "Ƶ��";
		public static String Constr_subblnofre = "û��ѡ��Ƶ��";
		
		public static String[] cusreadwrite={"������","д����"};
		public static String[] cuslockunlock={"��","����"};
		
		public static String Constr_subcsalterpwd="������";
		public static String Constr_subcslockwpwd="��������";
		public static String Constr_subcslockwoutpwd="����������";
		public static String Constr_subcsplsetimeou="�����ó�ʱʱ��";
		public static String Constr_subcsputcnpwd="���뵱ǰ������������";
		public static String Constr_subcsplselreg="��ѡ������";
		public static String Constr_subcsopfail="����ʧ��:";
		public static String Constr_subcsputcurpwd="���뵱ǰ����";

		public static String Constr_subdbdisconnreconn = "�Ѿ��Ͽ�,������������";
		public static String Constr_subdbhadconnected = "�Ѿ�����";
		public static String Constr_subdbconnecting = "��������......";
		public static String Constr_subdbrev = "����";
		public static String Constr_subdbstop = "ֹͣ";
		public static String Constr_subdbdalennot = "���ݳ��Ȳ���";
		public static String Constr_subdbplpuhexchar = "������16�����ַ�";
		
		public static String Constr_subsysaveok = "����ɹ�";
		public static String Constr_subsysout = "����txt����csv";
		public static String Constr_subsysreavaid = "����������Ч";
		public static String Constr_sub1recfailed = "��������ʧ��";
		public static String Constr_subsysavefailed = "����ʧ��";
		public static String Constr_subsysexefin ="ִ�����";
		public static String Constr_sub1adrno="��ַû������";
		public static String Constr_sub1pdtsl="��ѡ��ƽ̨";
		public static String Constr_mainpu="�ϵ磺";
		public static String Constr_nostopstreadfailed="��ͣ���̵�����ʧ��";
		public static String Constr_nostopspreadfailed="��ͣ���̵�ֹͣʧ��";
		public static String Constr_nostopreadfailed="��ʼ�̵�ʧ�ܣ�";
		public static String Constr_connectok="���ӳɹ�";
		public static String Constr_connectfialed="����ʧ��";
		public static String Constr_disconpowdown="�Ͽ���д�����µ磺";
		public static String Constr_ok="�ɹ�:";
		public static String Constr_failed="ʧ��:";
		public static String Constr_excep="�쳣:";
		public static String Constr_setcep="�����쳣:";
		public static String Constr_getcep="��ȡ�쳣:";
		public static String Constr_killok="KILL�ɹ�";
		public static String Constr_killfailed="KILLʧ��";
		public static String Constr_psiant="��ѡ���̵�����";
		public static String Constr_selpro="��ѡ��Э��";
		public static String Constr_setpwd="���ù���:";
	    //*/

		 
	/*
	 * ��������   
	 */
	    public Map<String,TAGINFO> TagsMap=new LinkedHashMap<String,TAGINFO>();//����
		public String path;
	    public int ThreadMODE=0;
		public int refreshtime=1000;
		public int Mode;
		public Map<String, String> m;
		public TabHost tabHost;
		public long exittime;
		public boolean needreconnect;
	    public Reader Mreader;
	    public int antportc;
	    public String Curepc;
		public int Bank;
		public int BackResult;
		
	    public SPconfig spf;
		public RfidPower Rpower;
		
		public ReaderParams Rparams;
		public String Address;
		public boolean nostop=false;
		public boolean needlisen=false;
		public class ReaderParams
		{
 
			//save param
			public int opant;
			
			public List<String> invpro;
			public String opro;
			public int[] uants;
			public int readtime;
			public int sleep;

			public int checkant;
			public int[] rpow;
			public int[] wpow;
			
			public int region;
			public int[] frecys;
			public int frelen;
			
			public int session;
			public int qv;
			public int wmode;
			public int blf;
			public int maxlen;
			public int target;
			public int gen2code;
			public int gen2tari;
			
			public String fildata;
			public int filadr;
			public int filbank;
			public int filisinver;
			public int filenable;
			
			public int emdadr;
			public int emdbytec;
			public int emdbank;
			public int emdenable;
			
			public int antq;
			public int adataq;
			public int rhssi;
			public int invw;
			public int iso6bdeep;
			public int iso6bdel;
			public int iso6bblf;
			public int option;
			//other params
			
			public String password;
			public int optime;
			public ReaderParams()
			{
				opant=1;
				invpro=new ArrayList<String>();
				invpro.add("GEN2");
				uants=new int[1];
				uants[0]=1;
				sleep=0;
				readtime=50;
				optime=1000;
				opro="GEN2";
				checkant=1;
				rpow=new int[]{2700,2000,2000,2000};
				wpow=new int[]{2000,2000,2000,2000};
				region=1;
				frelen=0; 
				session=0; 
				qv=-1; 
				wmode=0; 
				blf=0; 
				maxlen=0; 
				target=0; 
				gen2code=2; 
				gen2tari=0; 
		 
				fildata="";
				filadr=32;
				filbank=1;
				filisinver=0;
				filenable=0; 
				
				emdadr=0;
				emdbytec=0; 
				emdbank=1;
				emdenable=0; 
				 
				adataq=0; 
				rhssi=1; 
				invw=0; 
				iso6bdeep=0; 
				iso6bdel=0; 
				iso6bblf=0;
				option=0;
			}
		}
}
