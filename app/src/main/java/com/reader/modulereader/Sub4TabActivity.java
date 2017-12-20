package com.reader.modulereader;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import com.reader.modulereader.function.MyAdapter2;
import com.reader.modulereader.function.commfun;
import com.uhf.api.cls.Reader.AntPower;
import com.uhf.api.cls.Reader.AntPowerConf;
import com.uhf.api.cls.Reader.EmbededData_ST;
import com.uhf.api.cls.Reader.HoptableData_ST;
import com.uhf.api.cls.Reader.Inv_Potl;
import com.uhf.api.cls.Reader.Inv_Potls_ST;
import com.uhf.api.cls.Reader.Mtr_Param;
import com.uhf.api.cls.Reader.READER_ERR;
import com.uhf.api.cls.Reader.Region_Conf;
import com.uhf.api.cls.Reader.SL_TagProtocol;
import com.uhf.api.cls.Reader.TagFilter_ST;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Sub4TabActivity extends Activity {

	String[] spises = { "S0", "S1", "S2", "S3" };
	String[] spipow = { "500", "600", "700", "800", "900", "1000", "1100",
			"1200", "1300", "1400", "1500", "1600", "1700", "1800", "1900",
			"2000", "2100", "2200", "2300", "2400", "2500", "2600", "2700",
			"2800", "2900", "3000","3100","3200","3300" };// ����ʱ���
	String[] spiq = { "�Զ�", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9",
			"10", "11", "12", "13", "14", "15" };
	//String[] spinvmo = { "��ͨģʽ", "����ģʽ" };
	String[] spiblf = { "40", "250", "400", "640" };
	String[] spimlen = { "96", "496" };
	String[] spitget = { "A", "B", "A-B", "B-A" };
	String[] spigcod = { "FM0", "M2", "M4", "M8" };

	String[] spi6btzsd = { "99percent", "11percent" };
	String[] spidelm = { "Delimiter1", "Delimiter4" };
	String[] spiperst = { "0%", "5%", "10%", "15%", "20%", "25%", "30%", "35%",
			"40%", "45%", "50%" };

	CheckBox cb_gpo1, cb_gpo2, cb_gpo3, cb_gpo4, cb_gpi1, cb_gpi2, cb_gpi3,
			cb_gpi4, cb_oant, cb_odata, cb_hrssi, cb_gen2, cb_6b, cb_ipx64,
			cb_ipx256, cb_ant1, cb_ant2, cb_ant3, cb_ant4, cb_allsel,
			cb_nostop, cbmf_readcount, cbmf_rssi, cbmf_ant, cbmf_fre,
			cbmf_time, cbmf_rfu, cbmf_pro, cbmf_dl, cb_fre;
	RadioGroup rg_emdenable, rg_antcheckenable, rg_invfilenable,
			rg_invfilmatch;

	private ArrayAdapter<String> arrdp_bank, arrdp_fbank, arrdp_ses,
			arradp_pow, arrdp_q, arrdp_invmo, arrdp_blf, arrdp_mlen,
			arrdp_tget, arrdp_g2cod, arrdp_tari, arrdp_wmod, arrdp_6btzsd,
			arrdp_delm, arradp_reg, arrdp_per;
	Spinner spinner_ant1rpow, spinner_ant1wpow, spinner_ant2rpow,
			spinner_ant2wpow, spinner_ant3rpow, spinner_ant3wpow,
			spinner_ant4rpow, spinner_ant4wpow, spinner_sesion, spinner_q,
			spinner_wmode, spinner_blf, spinner_maxlen, spinner_target,
			spinner_g2code, spinner_tari, spinner_emdbank, spinner_filbank,
			spinner_region, spinner_invmode, spinner_6btzsd, spinner_delmi,
			spinner_persen;
	TabHost tabHost_set;

	Button button_getantpower, button_setantpower, button_getantcheck,
			button_setantcheck, button_getgen2ses, button_setgen2ses,
			button_getgen2q, button_setgen2q, button_getwmode, button_setwmode,
			button_getgen2blf, button_setgenblf, button_getgen2maxl,
			button_setgen2maxl, button_getgen2targ, button_setgen2targ,
			button_getgen2code, button_setgen2code, button_getgen2tari,
			button_setgen2tari, button_setgpo, button_getgpi, button_getemd,
			button_setemd, button_getfil, button_setfil, button_getreg,
			button_setreg, button_getfre, button_setfre, button_getusl,
			button_setusl, button_invproset, button_opproget, button_opproset,
			button_invantsset,

			button_oantuqget, button_oantuqset, button_odatauqget,
			button_odatauqset, button_hrssiget, button_hrssiset,
			button_invmodeget, button_invmodeset, button_6bdpget,
			button_6bdpset, button_6bdltget, button_6bdltset, button_6bblfget,
			button_6bblfset, button_gettempture, button_nostop,button_q11200,button_q21200;
	ListView elist;
	MyApplication myapp;

	private View createIndicatorView(Context context, TabHost tabHost,
			String title) {
		LayoutInflater inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View tabIndicator = inflater.inflate(R.layout.tab_indicator_vertical,
				tabHost.getTabWidget(), false);
		final TextView titleView = (TextView) tabIndicator
				.findViewById(R.id.tv_indicator);
		titleView.setText(title);
		return tabIndicator;
	}

	private void showlist(String[] fres) {
		ArrayList<String> list = new ArrayList<String>();
		list.addAll(Arrays.asList(fres));
		MyAdapter2 mAdapter = new MyAdapter2(list, this);
		// ��Adapter

		elist.setAdapter(mAdapter);
	}
 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab4_tablelayout);

		Application app = getApplication();
		myapp = (MyApplication) app;
		// ��ȡTabHost����
		// �õ�TabActivity�е�TabHost����
		tabHost_set = (TabHost) findViewById(R.id.tabhost4);
		// ���û�м̳�TabActivityʱ��ͨ�����ַ�����������tabHost
		tabHost_set.setup();
		tabHost_set.getTabWidget().setOrientation(LinearLayout.VERTICAL);
	 
		tabHost_set.addTab(tabHost_set.newTabSpec("tab1")
				.setIndicator(createIndicatorView(this, tabHost_set, MyApplication.Constr_sub4invenpra))
				.setContent(R.id.tab4_sub1_invusl));
		tabHost_set.addTab(tabHost_set.newTabSpec("tab2")
				.setIndicator(createIndicatorView(this, tabHost_set, MyApplication.Constr_sub4antpow))
				.setContent(R.id.tab4_sub2_antpow));
		tabHost_set.addTab(tabHost_set.newTabSpec("tab3")
				.setIndicator(createIndicatorView(this, tabHost_set, MyApplication.Constr_sub4regionfre))
				.setContent(R.id.tab4_sub3_invfre));
		tabHost_set.addTab(tabHost_set.newTabSpec("tab4")
				.setIndicator(createIndicatorView(this, tabHost_set, MyApplication.Constr_sub4gen2opt))
				.setContent(R.id.tab4_sub4_gen2));
		tabHost_set.addTab(tabHost_set.newTabSpec("tab5")
				.setIndicator(createIndicatorView(this, tabHost_set, MyApplication.Constr_sub4invenfil))
				.setContent(R.id.tab4_sub5_invfil));
		tabHost_set.addTab(tabHost_set.newTabSpec("tab6")
				.setIndicator(createIndicatorView(this, tabHost_set, MyApplication.Constr_sub4addidata))
				.setContent(R.id.tab4_sub6_emd));
		tabHost_set.addTab(tabHost_set.newTabSpec("tab7")
				.setIndicator(createIndicatorView(this, tabHost_set, "GPIO"))
				.setContent(R.id.tab4_sub7_gpio));
		tabHost_set.addTab(tabHost_set.newTabSpec("tab8")
				.setIndicator(createIndicatorView(this, tabHost_set, MyApplication.Constr_sub4others))
				.setContent(R.id.tab4_sub8_others));
		tabHost_set.addTab(tabHost_set.newTabSpec("tab9")
				.setIndicator(createIndicatorView(this, tabHost_set,  MyApplication.Constr_sub4quickly))
				.setContent(R.id.tab4_sub9_quickly));
		TabWidget tw = tabHost_set.getTabWidget();
		tw.getChildAt(0).setBackgroundColor(Color.BLUE);
		// tabHost2.setCurrentTab(2);
		spiq[0]=MyApplication.Auto; 
		
		spinner_ant1rpow = (Spinner) findViewById(R.id.spinner_ant1rpow);
		spinner_ant1wpow = (Spinner) findViewById(R.id.spinner_ant1wpow);
		spinner_ant2rpow = (Spinner) findViewById(R.id.spinner_ant2rpow);
		spinner_ant2wpow = (Spinner) findViewById(R.id.spinner_ant2wpow);
		spinner_ant3rpow = (Spinner) findViewById(R.id.spinner_ant3rpow);
		spinner_ant3wpow = (Spinner) findViewById(R.id.spinner_ant3wpow);
		spinner_ant4rpow = (Spinner) findViewById(R.id.spinner_ant4rpow);
		spinner_ant4wpow = (Spinner) findViewById(R.id.spinner_ant4wpow);

		// /*
		arradp_pow = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, spipow);
		arradp_pow
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_ant1rpow.setAdapter(arradp_pow);
		spinner_ant1wpow.setAdapter(arradp_pow);
		spinner_ant2rpow.setAdapter(arradp_pow);
		spinner_ant2wpow.setAdapter(arradp_pow);
		spinner_ant3rpow.setAdapter(arradp_pow);
		spinner_ant3wpow.setAdapter(arradp_pow);
		spinner_ant4rpow.setAdapter(arradp_pow);
		spinner_ant4wpow.setAdapter(arradp_pow);

		spinner_ant2rpow.setEnabled(false);
		spinner_ant2wpow.setEnabled(false);
		spinner_ant3rpow.setEnabled(false);
		spinner_ant3wpow.setEnabled(false);
		spinner_ant4rpow.setEnabled(false);
		spinner_ant4wpow.setEnabled(false);
		// */

		spinner_sesion = (Spinner) findViewById(R.id.spinner_gen2session);
		arrdp_ses = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, spises);
		arrdp_ses
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_sesion.setAdapter(arrdp_ses);

		spinner_q = (Spinner) findViewById(R.id.spinner_gen2q);
		arrdp_q = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, spiq);
		arrdp_q.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_q.setAdapter(arrdp_q);

		spinner_wmode = (Spinner) findViewById(R.id.spinner_gen2wmode);
		arrdp_wmod = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, MyApplication.spiwmod);
		arrdp_wmod
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_wmode.setAdapter(arrdp_wmod);

		spinner_blf = (Spinner) findViewById(R.id.spinner_gen2blf);
		arrdp_blf = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, spiblf);
		arrdp_blf
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_blf.setAdapter(arrdp_blf);

		spinner_maxlen = (Spinner) findViewById(R.id.spinner_gen2maxl);
		arrdp_mlen = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, spimlen);
		arrdp_mlen
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_maxlen.setAdapter(arrdp_mlen);

		spinner_target = (Spinner) findViewById(R.id.spinner_target);
		arrdp_tget = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, spitget);
		arrdp_tget
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_target.setAdapter(arrdp_tget);

		spinner_g2code = (Spinner) findViewById(R.id.spinner_gen2code);
		arrdp_g2cod = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, spigcod);
		arrdp_g2cod
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_g2code.setAdapter(arrdp_g2cod);

		spinner_tari = (Spinner) findViewById(R.id.spinner_gen2tari);
		arrdp_tari = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, MyApplication.spitari);
		arrdp_tari
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_tari.setAdapter(arrdp_tari);

		spinner_emdbank = (Spinner) findViewById(R.id.spinner_emdbank);
		arrdp_bank = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, MyApplication.spibank);
		arrdp_bank
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_emdbank.setAdapter(arrdp_bank);

		spinner_filbank = (Spinner) findViewById(R.id.spinner_invfbank);
		arrdp_fbank = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, MyApplication.spifbank);
		arrdp_fbank
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_filbank.setAdapter(arrdp_fbank);

		spinner_invmode = (Spinner) findViewById(R.id.spinner_invmode);
		arrdp_invmo = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, MyApplication.spinvmo);
		arrdp_invmo
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_invmode.setAdapter(arrdp_invmo);

		spinner_6btzsd = (Spinner) findViewById(R.id.spinner_6bdlt);
		arrdp_6btzsd = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, spi6btzsd);
		arrdp_6btzsd
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_6btzsd.setAdapter(arrdp_6btzsd);

		spinner_delmi = (Spinner) findViewById(R.id.spinner_6bdp);
		arrdp_delm = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, spidelm);
		arrdp_delm
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_delmi.setAdapter(arrdp_delm);

		spinner_region = (Spinner) findViewById(R.id.spinner_region);
		arradp_reg = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, MyApplication.spireg);
		arradp_reg
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_region.setAdapter(arradp_reg);

		spinner_persen = (Spinner) findViewById(R.id.spinner_per);
		arrdp_per = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_item, spiperst);
		arrdp_per
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner_persen.setAdapter(arrdp_per);

		rg_antcheckenable = (RadioGroup) findViewById(R.id.radioGroup_antcheck);
		button_getantpower = (Button) findViewById(R.id.button_antpowget);
		button_setantpower = (Button) findViewById(R.id.button_antpowset);
		button_getantcheck = (Button) findViewById(R.id.button_checkantget);
		button_setantcheck = (Button) findViewById(R.id.button_checkantset);

		button_getgen2ses = (Button) findViewById(R.id.button_gen2sesget);
		button_setgen2ses = (Button) findViewById(R.id.button_gen2sesset);
		button_getgen2q = (Button) findViewById(R.id.button_gen2qget);
		button_setgen2q = (Button) findViewById(R.id.button_gen2qset);
		button_getwmode = (Button) findViewById(R.id.button_gen2wmodeget);
		button_setwmode = (Button) findViewById(R.id.button_gen2wmodeset);
		button_getgen2blf = (Button) findViewById(R.id.button_gen2blfget);
		button_setgenblf = (Button) findViewById(R.id.button_gen2blfset);
		button_getgen2maxl = (Button) findViewById(R.id.button_gen2mlget);
		button_setgen2maxl = (Button) findViewById(R.id.button_gen2mlset);
		button_getgen2targ = (Button) findViewById(R.id.button_target);
		button_setgen2targ = (Button) findViewById(R.id.button_targetset);
		button_getgen2code = (Button) findViewById(R.id.button_codeget);
		button_setgen2code = (Button) findViewById(R.id.button_codeset);
		button_getgen2tari = (Button) findViewById(R.id.button_gen2tariget);
		button_setgen2tari = (Button) findViewById(R.id.button_gen2tariset);

		button_setgpo = (Button) findViewById(R.id.button_gposet);
		button_getgpi = (Button) findViewById(R.id.button_gpiget);

		button_gettempture = (Button) findViewById(R.id.button_tempure);
		button_nostop = (Button) findViewById(R.id.button_nostop);
		button_q11200 = (Button) findViewById(R.id.button_q1enable1200);
		button_q21200 = (Button) findViewById(R.id.button_q2enable1200);

		cb_gpo1 = (CheckBox) findViewById(R.id.checkBox_gpo1);
		cb_gpo2 = (CheckBox) findViewById(R.id.checkBox_gpo2);
		cb_gpo3 = (CheckBox) findViewById(R.id.checkBox_gpo3);
		cb_gpo4 = (CheckBox) findViewById(R.id.checkBox_gpo4);
		cb_gpi1 = (CheckBox) findViewById(R.id.checkBox_gpi1);
		cb_gpi2 = (CheckBox) findViewById(R.id.checkBox_gpi2);
		cb_gpi3 = (CheckBox) findViewById(R.id.checkBox_gpi3);
		cb_gpi4 = (CheckBox) findViewById(R.id.checkBox_gpi4);

		cb_gen2 = (CheckBox) findViewById(R.id.checkBox_invgen2);
		cb_6b = (CheckBox) findViewById(R.id.checkBox_inv6b);
		cb_ipx64 = (CheckBox) findViewById(R.id.checkBox_invipx64);
		cb_ipx256 = (CheckBox) findViewById(R.id.checkBox_invipx256);

		cb_ant1 = (CheckBox) findViewById(R.id.checkBox_ant1);
		cb_ant2 = (CheckBox) findViewById(R.id.checkBox_ant2);
		cb_ant3 = (CheckBox) findViewById(R.id.checkBox_ant3);
		cb_ant4 = (CheckBox) findViewById(R.id.checkBox_ant4);
		cb_allsel = (CheckBox) findViewById(R.id.checkBox_allselect);

		cbmf_readcount = (CheckBox) this.findViewById(R.id.checkBox_readcount);
		cbmf_rssi = (CheckBox) this.findViewById(R.id.checkBox_rssi);
		cbmf_ant = (CheckBox) this.findViewById(R.id.checkBox_ant);
		cbmf_fre = (CheckBox) this.findViewById(R.id.checkBox_frequency);
		cbmf_time = (CheckBox) this.findViewById(R.id.checkBox_time);
		cbmf_rfu = (CheckBox) this.findViewById(R.id.checkBox_rfu);
		cbmf_pro = (CheckBox) this.findViewById(R.id.checkBox_pro);
		cbmf_dl = (CheckBox) this.findViewById(R.id.checkBox_tagdatalen);
		cb_nostop = (CheckBox) findViewById(R.id.checkBox_nostop);

		button_getemd = (Button) findViewById(R.id.button_getemd);
		button_setemd = (Button) findViewById(R.id.button_setemd);
		rg_emdenable = (RadioGroup) findViewById(R.id.radioGroup_emdenable);
		button_getfil = (Button) findViewById(R.id.button_getfil);
		button_setfil = (Button) findViewById(R.id.button_setfil);
		button_getreg = (Button) findViewById(R.id.button_getregion);
		button_setreg = (Button) findViewById(R.id.button_setregion);
		button_getfre = (Button) findViewById(R.id.button_getfre);
		button_setfre = (Button) findViewById(R.id.button_setfre);
		button_getusl = (Button) findViewById(R.id.button_invuslget);
		button_setusl = (Button) findViewById(R.id.button_invuslset);

		button_invproset = (Button) findViewById(R.id.button_invproset);
		button_opproget = (Button) findViewById(R.id.button_opproget);
		button_opproset = (Button) findViewById(R.id.button_opproset);

		button_invantsset = (Button) findViewById(R.id.button_invantsset);

		rg_invfilenable = (RadioGroup) findViewById(R.id.radioGroup_enablefil);
		rg_invfilmatch = (RadioGroup) findViewById(R.id.radioGroup_invmatch);
		elist = (ListView) this.findViewById(R.id.listView_frequency);

		button_oantuqget = (Button) findViewById(R.id.button_oantuqget);
		button_oantuqset = (Button) findViewById(R.id.button_oantuqset);
		button_odatauqget = (Button) findViewById(R.id.button_odatauqget);
		button_odatauqset = (Button) findViewById(R.id.button_odatauqset);
		button_hrssiget = (Button) findViewById(R.id.button_hrssiget);
		button_hrssiset = (Button) findViewById(R.id.button_hrssiset);
		button_invmodeget = (Button) findViewById(R.id.button_invmodeget);
		button_invmodeset = (Button) findViewById(R.id.button_invmodeset);
		button_6bdpget = (Button) findViewById(R.id.button_6bdpget);
		button_6bdpset = (Button) findViewById(R.id.button_6bdpset);
		button_6bdltget = (Button) findViewById(R.id.button_6bdltget);
		button_6bdltset = (Button) findViewById(R.id.button_6bdltset);
		button_6bblfget = (Button) findViewById(R.id.button_6bblfget);
		button_6bblfset = (Button) findViewById(R.id.button_6bblfset);
		cb_oant = (CheckBox) findViewById(R.id.checkBox_oantuq);
		cb_odata = (CheckBox) findViewById(R.id.checkBox_odatauq);
		cb_hrssi = (CheckBox) findViewById(R.id.checkBox_hrssi);
		if (myapp.antportc >= 2) {
			spinner_ant2rpow.setEnabled(true);
			spinner_ant2wpow.setEnabled(true);
		}
		if (myapp.antportc >= 3) {
			spinner_ant3rpow.setEnabled(true);
			spinner_ant3wpow.setEnabled(true);
		}
		if (myapp.antportc >= 4) {
			spinner_ant4rpow.setEnabled(true);
			spinner_ant4wpow.setEnabled(true);
		}
		// ��listView�ļ�����
		elist.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				// ȡ��ViewHolder����������ʡȥ��ͨ������findViewByIdȥʵ����������Ҫ��cbʵ���Ĳ���
				MyAdapter2.ViewHolder holder = (MyAdapter2.ViewHolder) arg1.getTag();
				// �ı�CheckBox��״̬
				holder.cb.toggle();
				// ��CheckBox��ѡ��״����¼����
				MyAdapter2.getIsSelected().put(arg2, holder.cb.isChecked());
			}

		});
		button_getantcheck.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try {
					int[] val2 = new int[] { -1 };
					READER_ERR er = myapp.Mreader.ParamGet(
							Mtr_Param.MTR_PARAM_READER_IS_CHK_ANT, val2);

					if (er == READER_ERR.MT_OK_ERR) {
						if (val2[0] == 0)
							rg_antcheckenable.check(rg_antcheckenable
									.getChildAt(0).getId());
						else
							rg_antcheckenable.check(rg_antcheckenable
									.getChildAt(1).getId());
					} else
						Toast.makeText(Sub4TabActivity.this,
								MyApplication.Constr_GetFaill + er.toString(), Toast.LENGTH_SHORT)
								.show();

				} catch (Exception e) {
					// TODO Auto-generated catch block
					Toast.makeText(Sub4TabActivity.this,
							MyApplication.Constr_excep + e.getMessage(), Toast.LENGTH_SHORT).show();
				}

			}

		});
		button_setantcheck.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try {
					READER_ERR er;
					if (commfun.SortGroup(rg_antcheckenable) == 0)
						er = myapp.Mreader.ParamSet(
								Mtr_Param.MTR_PARAM_READER_IS_CHK_ANT,
								new int[] { 0 });
					else
						er = myapp.Mreader.ParamSet(
								Mtr_Param.MTR_PARAM_READER_IS_CHK_ANT,
								new int[] { 1 });
					if (er == READER_ERR.MT_OK_ERR) {
						myapp.Rparams.checkant = commfun.SortGroup(rg_antcheckenable);
						Toast.makeText(Sub4TabActivity.this, MyApplication.Constr_SetOk,
								Toast.LENGTH_SHORT).show();
					} else
						Toast.makeText(Sub4TabActivity.this,
								MyApplication.Constr_SetFaill + er.toString(), Toast.LENGTH_SHORT)
								.show();

				} catch (Exception e) {
					// TODO Auto-generated catch block
					Toast.makeText(Sub4TabActivity.this,
							MyApplication.Constr_setcep + e.getMessage(), Toast.LENGTH_SHORT)
							.show();
					return;
				}

			}

		});
		button_getantpower.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try {
					AntPowerConf apcf2 = myapp.Mreader.new AntPowerConf();

					READER_ERR er = myapp.Mreader.ParamGet(
							Mtr_Param.MTR_PARAM_RF_ANTPOWER, apcf2);

					if (er == READER_ERR.MT_OK_ERR) {
						for (int i = 0; i < apcf2.antcnt; i++) {
							if (i == 0) {
								spinner_ant1rpow
										.setSelection((apcf2.Powers[i].readPower - 500) / 100);
								spinner_ant1wpow
										.setSelection((apcf2.Powers[i].writePower - 500) / 100);
							} else if (i == 1) {
								spinner_ant2rpow
										.setSelection((apcf2.Powers[i].readPower - 500) / 100);
								spinner_ant2wpow
										.setSelection((apcf2.Powers[i].writePower - 500) / 100);
							} else if (i == 2) {
								spinner_ant3rpow
										.setSelection((apcf2.Powers[i].readPower - 500) / 100);
								spinner_ant3wpow
										.setSelection((apcf2.Powers[i].writePower - 500) / 100);
							} else if (i == 3) {
								spinner_ant4rpow
										.setSelection((apcf2.Powers[i].readPower - 500) / 100);
								spinner_ant4wpow
										.setSelection((apcf2.Powers[i].writePower - 500) / 100);
							}
						}

					} else
						Toast.makeText(Sub4TabActivity.this,
								MyApplication.Constr_GetFaill + er.toString(), Toast.LENGTH_SHORT)
								.show();

				} catch (Exception e) {
					// TODO Auto-generated catch block
					Toast.makeText(Sub4TabActivity.this,
							MyApplication.Constr_getcep + e.getMessage(), Toast.LENGTH_SHORT)
							.show();
				}

			}

		});
		button_setantpower.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				int[] rp = new int[4];
				int[] wp = new int[4];

				rp[0] = spinner_ant1rpow.getSelectedItemPosition();
				rp[1] = spinner_ant2rpow.getSelectedItemPosition();
				rp[2] = spinner_ant3rpow.getSelectedItemPosition();
				rp[3] = spinner_ant4rpow.getSelectedItemPosition();

				wp[0] = spinner_ant1wpow.getSelectedItemPosition();
				wp[1] = spinner_ant2wpow.getSelectedItemPosition();
				wp[2] = spinner_ant3wpow.getSelectedItemPosition();
				wp[3] = spinner_ant4wpow.getSelectedItemPosition();

				AntPowerConf apcf = myapp.Mreader.new AntPowerConf();
				apcf.antcnt = myapp.antportc;
				int[] rpow = new int[apcf.antcnt];
				int[] wpow = new int[apcf.antcnt];
				for (int i = 0; i < apcf.antcnt; i++) {
					AntPower jaap = myapp.Mreader.new AntPower();
					jaap.antid = i + 1;
					jaap.readPower = (short) (500 + 100 * rp[i]);
					rpow[i] = jaap.readPower;

					jaap.writePower = (short) (500 + 100 * wp[i]);
					wpow[i] = jaap.writePower;
					apcf.Powers[i] = jaap;
				}

				try {
					READER_ERR er = myapp.Mreader.ParamSet(
							Mtr_Param.MTR_PARAM_RF_ANTPOWER, apcf);
					if (er == READER_ERR.MT_OK_ERR) {
						myapp.Rparams.rpow = rpow;
						myapp.Rparams.wpow = wpow;
						Toast.makeText(Sub4TabActivity.this, MyApplication.Constr_SetOk,
								Toast.LENGTH_SHORT).show();
					} else
						Toast.makeText(Sub4TabActivity.this,
								MyApplication.Constr_SetFaill + er.toString(), Toast.LENGTH_SHORT)
								.show();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					Toast.makeText(Sub4TabActivity.this,
							MyApplication.Constr_setcep + e.getMessage(), Toast.LENGTH_SHORT)
							.show();
					return;
				}

			}

		});

		button_getgen2ses.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try {
					int[] val2 = new int[] { -1 };
					READER_ERR er = myapp.Mreader.ParamGet(
							Mtr_Param.MTR_PARAM_POTL_GEN2_SESSION, val2);

					if (er == READER_ERR.MT_OK_ERR) {
						spinner_sesion.setSelection(val2[0]);
					} else
						Toast.makeText(Sub4TabActivity.this,
								MyApplication.Constr_GetFaill + er.toString(), Toast.LENGTH_SHORT)
								.show();

				} catch (Exception e) {
					// TODO Auto-generated catch block
					Toast.makeText(Sub4TabActivity.this,
							MyApplication.Constr_setcep + e.getMessage(), Toast.LENGTH_SHORT)
							.show();
				}

			}

		});
		button_setgen2ses.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try {
					int[] val = new int[] { -1 };
					val[0] = spinner_sesion.getSelectedItemPosition();

					READER_ERR er = myapp.Mreader.ParamSet(
							Mtr_Param.MTR_PARAM_POTL_GEN2_SESSION, val);
					if (er == READER_ERR.MT_OK_ERR) {
						myapp.Rparams.session = val[0];
						Toast.makeText(Sub4TabActivity.this, MyApplication.Constr_SetOk,
								Toast.LENGTH_SHORT).show();
					} else
						Toast.makeText(Sub4TabActivity.this,
								MyApplication.Constr_SetFaill + er.toString(), Toast.LENGTH_SHORT)
								.show();

				} catch (Exception e) {
					// TODO Auto-generated catch block
					Toast.makeText(Sub4TabActivity.this,
							MyApplication.Constr_setcep + e.getMessage(), Toast.LENGTH_SHORT)
							.show();
					return;
				}

			}

		});
		button_getgen2q.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try {
					int[] val = new int[] { -1 };
					READER_ERR er = myapp.Mreader.ParamGet(
							Mtr_Param.MTR_PARAM_POTL_GEN2_Q, val);

					if (er == READER_ERR.MT_OK_ERR) {
						spinner_q.setSelection(val[0] + 1);
					} else
						Toast.makeText(Sub4TabActivity.this,
								MyApplication.Constr_GetFaill + er.toString(), Toast.LENGTH_SHORT)
								.show();

				} catch (Exception e) {
					// TODO Auto-generated catch block
					Toast.makeText(Sub4TabActivity.this,
							MyApplication.Constr_setcep + e.getMessage(), Toast.LENGTH_SHORT)
							.show();
				}

			}

		});
		button_setgen2q.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try {
					int[] val = new int[] { -1 };
					val[0] = spinner_q.getSelectedItemPosition() - 1;
					READER_ERR er = myapp.Mreader.ParamSet(
							Mtr_Param.MTR_PARAM_POTL_GEN2_Q, val);
					if (er == READER_ERR.MT_OK_ERR) {
						myapp.Rparams.qv = val[0];
						Toast.makeText(Sub4TabActivity.this, MyApplication.Constr_SetOk,
								Toast.LENGTH_SHORT).show();
					} else
						Toast.makeText(Sub4TabActivity.this,
								MyApplication.Constr_SetFaill + er.toString(), Toast.LENGTH_SHORT)
								.show();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					Toast.makeText(Sub4TabActivity.this,
							MyApplication.Constr_setcep + e.getMessage(), Toast.LENGTH_SHORT)
							.show();
					return;
				}

			}

		});

		button_getwmode.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try {
					int[] val = new int[] { -1 };
					READER_ERR er = myapp.Mreader.ParamGet(
							Mtr_Param.MTR_PARAM_POTL_GEN2_WRITEMODE, val);

					if (er == READER_ERR.MT_OK_ERR) {
						if (val[0] == 0)
							spinner_wmode.setSelection(0);
						else if (val[0] == 1)
							spinner_wmode.setSelection(1);
					} else
						Toast.makeText(Sub4TabActivity.this,
								MyApplication.Constr_GetFaill + er.toString(), Toast.LENGTH_SHORT)
								.show();

				} catch (Exception e) {
					// TODO Auto-generated catch block
					Toast.makeText(Sub4TabActivity.this,
							MyApplication.Constr_setcep + e.getMessage(), Toast.LENGTH_SHORT)
							.show();
				}

			}

		});
		button_setwmode.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try {
					int[] val = new int[] { spinner_wmode
							.getSelectedItemPosition() };

					READER_ERR er = myapp.Mreader.ParamSet(
							Mtr_Param.MTR_PARAM_POTL_GEN2_WRITEMODE, val);
					if (er == READER_ERR.MT_OK_ERR) {
						myapp.Rparams.wmode = val[0];
						Toast.makeText(Sub4TabActivity.this, MyApplication.Constr_SetOk,
								Toast.LENGTH_SHORT).show();
					} else
						Toast.makeText(Sub4TabActivity.this,
								MyApplication.Constr_SetFaill + er.toString(), Toast.LENGTH_SHORT)
								.show();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					Toast.makeText(Sub4TabActivity.this,
							MyApplication.Constr_setcep + e.getMessage(), Toast.LENGTH_SHORT)
							.show();
					return;
				}

			}

		});
		button_getgen2blf.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try {
					int[] val = new int[] { -1 };
					READER_ERR er = myapp.Mreader.ParamGet(
							Mtr_Param.MTR_PARAM_POTL_GEN2_BLF, val);

					if (er == READER_ERR.MT_OK_ERR) {
						switch (val[0]) {
						case 40:
							spinner_blf.setSelection(0);
							break;
						case 250:
							spinner_blf.setSelection(1);
							break;
						case 400:
							spinner_blf.setSelection(2);
							break;
						case 640:
							spinner_blf.setSelection(3);
							break;
						}
					} else
						Toast.makeText(Sub4TabActivity.this,
								MyApplication.Constr_GetFaill + er.toString(), Toast.LENGTH_SHORT)
								.show();

				} catch (Exception e) {
					// TODO Auto-generated catch block
					Toast.makeText(Sub4TabActivity.this,
							MyApplication.Constr_setcep + e.getMessage(), Toast.LENGTH_SHORT)
							.show();
				}

			}

		});
		button_setgenblf.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try {
					int[] val = new int[] { -1 };

					switch (spinner_blf.getSelectedItemPosition()) {
					case 0:
						val[0] = 40;
						break;
					case 1:
						val[0] = 250;
						break;
					case 2:
						val[0] = 400;
						break;
					case 3:
						val[0] = 640;
						break;
					}
					READER_ERR er = myapp.Mreader.ParamSet(
							Mtr_Param.MTR_PARAM_POTL_GEN2_BLF, val);
					if (er == READER_ERR.MT_OK_ERR) {
						myapp.Rparams.blf = val[0];
						Toast.makeText(Sub4TabActivity.this, MyApplication.Constr_SetOk,
								Toast.LENGTH_SHORT).show();
					} else
						Toast.makeText(Sub4TabActivity.this,
								MyApplication.Constr_SetFaill + er.toString(), Toast.LENGTH_SHORT)
								.show();

				} catch (Exception e) {
					// TODO Auto-generated catch block
					Toast.makeText(Sub4TabActivity.this,
							MyApplication.Constr_setcep + e.getMessage(), Toast.LENGTH_SHORT)
							.show();
					return;
				}
			}

		});
		button_getgen2maxl.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				int[] val = new int[] { -1 };
				READER_ERR er = myapp.Mreader.ParamGet(
						Mtr_Param.MTR_PARAM_POTL_GEN2_MAXEPCLEN, val);
				if (er == READER_ERR.MT_OK_ERR) {
					if (val[0] == 96)
						spinner_maxlen.setSelection(0);
					else
						spinner_maxlen.setSelection(1);
				} else
					Toast.makeText(Sub4TabActivity.this,
							MyApplication.Constr_GetFaill + er.toString(), Toast.LENGTH_SHORT).show();
			}

		});
		button_setgen2maxl.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				int[] val = new int[] { spinner_maxlen
						.getSelectedItemPosition() == 0 ? 96 : 496 };
				READER_ERR er = myapp.Mreader.ParamSet(
						Mtr_Param.MTR_PARAM_POTL_GEN2_MAXEPCLEN, val);
				if (er == READER_ERR.MT_OK_ERR) {
					myapp.Rparams.maxlen = val[0];
					Toast.makeText(Sub4TabActivity.this, MyApplication.Constr_SetOk,
							Toast.LENGTH_SHORT).show();
				} else
					Toast.makeText(Sub4TabActivity.this,
							MyApplication.Constr_SetFaill + er.toString(), Toast.LENGTH_SHORT).show();

			}

		});
		button_getgen2targ.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try {
					int[] val = new int[] { -1 };
					READER_ERR er = myapp.Mreader.ParamGet(
							Mtr_Param.MTR_PARAM_POTL_GEN2_TARGET, val);
					if (er == READER_ERR.MT_OK_ERR) {
						spinner_target.setSelection(val[0]);
					} else
						Toast.makeText(Sub4TabActivity.this,
								MyApplication.Constr_GetFaill + er.toString(), Toast.LENGTH_SHORT)
								.show();

				} catch (Exception e) {
					// TODO Auto-generated catch block
					Toast.makeText(Sub4TabActivity.this,
							MyApplication.Constr_setcep + e.getMessage(), Toast.LENGTH_SHORT)
							.show();
				}

			}

		});
		button_setgen2targ.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				int[] val = new int[] { spinner_target
						.getSelectedItemPosition() };
				READER_ERR er = myapp.Mreader.ParamSet(
						Mtr_Param.MTR_PARAM_POTL_GEN2_TARGET, val);

				if (er == READER_ERR.MT_OK_ERR) {
					myapp.Rparams.gen2tari = val[0];
					Toast.makeText(Sub4TabActivity.this, MyApplication.Constr_SetOk,
							Toast.LENGTH_SHORT).show();
				} else
					Toast.makeText(Sub4TabActivity.this,
							MyApplication.Constr_SetFaill + er.toString(), Toast.LENGTH_SHORT).show();

			}

		});
		button_getgen2code.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				int[] val = new int[] { -1 };
				READER_ERR er = myapp.Mreader.ParamGet(
						Mtr_Param.MTR_PARAM_POTL_GEN2_TAGENCODING, val);
				if (er == READER_ERR.MT_OK_ERR) {
					spinner_g2code.setSelection(val[0]);
				} else
					Toast.makeText(Sub4TabActivity.this,
							MyApplication.Constr_GetFaill + er.toString(), Toast.LENGTH_SHORT).show();

			}

		});
		button_setgen2code.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				int[] val = new int[] { spinner_g2code
						.getSelectedItemPosition() };
				READER_ERR er = myapp.Mreader.ParamSet(
						Mtr_Param.MTR_PARAM_POTL_GEN2_TAGENCODING, val);

				if (er == READER_ERR.MT_OK_ERR) {
					myapp.Rparams.gen2code = val[0];
					Toast.makeText(Sub4TabActivity.this, MyApplication.Constr_SetOk,
							Toast.LENGTH_SHORT).show();
				} else
					Toast.makeText(Sub4TabActivity.this,
							MyApplication.Constr_SetFaill + er.toString(), Toast.LENGTH_SHORT).show();

			}
		});
		button_getgen2tari.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try {

					int[] val = new int[] { -1 };
					READER_ERR er = myapp.Mreader.ParamGet(
							Mtr_Param.MTR_PARAM_POTL_GEN2_TARI, val);
					if (er == READER_ERR.MT_OK_ERR) {
						spinner_tari.setSelection(val[0]);
					} else
						Toast.makeText(Sub4TabActivity.this,
								MyApplication.Constr_GetFaill + er.toString(), Toast.LENGTH_SHORT)
								.show();

				} catch (Exception e) {
					// TODO Auto-generated catch block
					Toast.makeText(Sub4TabActivity.this,
							MyApplication.Constr_setcep + e.getMessage(), Toast.LENGTH_SHORT)
							.show();
				}

			}

		});
		button_setgen2tari.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				int[] val = new int[] { spinner_tari.getSelectedItemPosition() };
				READER_ERR er = myapp.Mreader.ParamSet(
						Mtr_Param.MTR_PARAM_POTL_GEN2_TARI, val);
				if (er == READER_ERR.MT_OK_ERR) {
					myapp.Rparams.gen2tari = val[0];
					Toast.makeText(Sub4TabActivity.this, MyApplication.Constr_SetOk,
							Toast.LENGTH_SHORT).show();
				} else
					Toast.makeText(Sub4TabActivity.this,
							MyApplication.Constr_SetFaill + er.toString(), Toast.LENGTH_SHORT).show();

			}

		});

		button_getgpi.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				int[][] gpi = new int[myapp.antportc][1];

				for (int i = 0; i < gpi.length; i++) {
					READER_ERR er = myapp.Mreader.GetGPI(i + 1, gpi[i]);

					if (er == READER_ERR.MT_OK_ERR) {
						if (i == 1) {
							if (gpi[i][0] == 1)
								cb_gpi1.setChecked(true);
							else
								cb_gpi1.setChecked(false);
						} else if (i == 2) {
							if (gpi[i][0] == 1)
								cb_gpi2.setChecked(true);
							else
								cb_gpi2.setChecked(false);
						} else if (i == 3) {
							if (gpi[i][0] == 1)
								cb_gpi3.setChecked(true);
							else
								cb_gpi3.setChecked(false);
						} else if (i == 4) {
							if (gpi[i][0] == 1)
								cb_gpi4.setChecked(true);
							else
								cb_gpi4.setChecked(false);
						}
					} else
						Toast.makeText(Sub4TabActivity.this,
								MyApplication.Constr_GetFaill + er.toString(), Toast.LENGTH_SHORT)
								.show();
				}

			}

		});
		button_setgpo.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				int[][] gpo = new int[myapp.antportc][1];
				for (int i = 0; i < gpo.length; i++) {
					if (i == 0)
						gpo[0][0] = cb_gpo1.isChecked() ? 1 : 0;
					else if (i == 1)
						gpo[1][0] = cb_gpo2.isChecked() ? 1 : 0;
					else if (i == 2)
						gpo[2][0] = cb_gpo3.isChecked() ? 1 : 0;
					else if (i == 3)
						gpo[3][0] = cb_gpo4.isChecked() ? 1 : 0;
				}
				for (int i = 0; i < gpo.length; i++) {
					READER_ERR er = myapp.Mreader.SetGPO(i + 1, gpo[i][0]);
					if (er == READER_ERR.MT_OK_ERR)
						Toast.makeText(Sub4TabActivity.this, MyApplication.Constr_SetOk,
								Toast.LENGTH_SHORT).show();
					else
						Toast.makeText(Sub4TabActivity.this,
								MyApplication.Constr_SetFaill + er.toString(), Toast.LENGTH_SHORT)
								.show();
				}

			}

		});
		button_getemd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				EditText etst = (EditText) findViewById(R.id.editText_emdsadr);
				EditText etapwd = (EditText) findViewById(R.id.editText_emdacspwd);
				EditText etct = (EditText) findViewById(R.id.editText_emdcount);

				EmbededData_ST edst2 = myapp.Mreader.new EmbededData_ST();
				READER_ERR er = myapp.Mreader.ParamGet(
						Mtr_Param.MTR_PARAM_TAG_EMBEDEDDATA, edst2);
				if (er == READER_ERR.MT_OK_ERR) {
					if (edst2 == null || edst2.bytecnt == 0) {
						etst.setText("");
						etapwd.setText("");
						etct.setText("");
						rg_emdenable.check(rg_emdenable.getChildAt(0).getId());
						spinner_emdbank.setSelection(0);
					} else {
						if (edst2.accesspwd != null) {
							char[] out = new char[edst2.accesspwd.length * 2];
							myapp.Mreader.Hex2Str(edst2.accesspwd,
									edst2.accesspwd.length, out);
							etapwd.setText(String.valueOf(out));
						}
						etst.setText(String.valueOf(edst2.startaddr));
						etct.setText(String.valueOf(edst2.bytecnt));
						rg_emdenable.check(rg_emdenable.getChildAt(1).getId());

						spinner_emdbank.setSelection(edst2.bank);

					}
				} else
					Toast.makeText(Sub4TabActivity.this,
							MyApplication.Constr_GetFaill + er.toString(), Toast.LENGTH_SHORT).show();

			}

		});
		button_setemd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try {
					EmbededData_ST edst = myapp.Mreader.new EmbededData_ST();

					edst.accesspwd = null;

					if (commfun.SortGroup(rg_emdenable) == 1) {
						EditText etst = (EditText) findViewById(R.id.editText_emdsadr);
						EditText etapwd = (EditText) findViewById(R.id.editText_emdacspwd);
						EditText etct = (EditText) findViewById(R.id.editText_emdcount);
						edst.bank = spinner_emdbank.getSelectedItemPosition();
						edst.startaddr = Integer.valueOf(etst.getText()
								.toString());
						edst.bytecnt = Byte
								.parseByte(etct.getText().toString());
						if (!etapwd.getText().toString().equals("")) {
							edst.accesspwd = new byte[etapwd.getText().length() / 2];
							myapp.Mreader.Str2Hex(etapwd.getText().toString(),
									etapwd.getText().length(), edst.accesspwd);
						}
						edst.accesspwd = null;
					} else
						edst.bytecnt = 0;

					READER_ERR er = myapp.Mreader.ParamSet(
							Mtr_Param.MTR_PARAM_TAG_EMBEDEDDATA, edst);
					if (er == READER_ERR.MT_OK_ERR) {
						myapp.Rparams.emdadr = edst.startaddr;
						myapp.Rparams.emdbank = edst.bank;
						myapp.Rparams.emdbytec = edst.bytecnt;
						myapp.Rparams.emdenable = 1;
						Toast.makeText(Sub4TabActivity.this, MyApplication.Constr_SetOk,
								Toast.LENGTH_SHORT).show();
					} else
						Toast.makeText(Sub4TabActivity.this,
								MyApplication.Constr_SetFaill + er.toString(), Toast.LENGTH_SHORT)
								.show();

				} catch (Exception ex) {
					Toast.makeText(Sub4TabActivity.this,
							MyApplication.Constr_setcep + ex.toString(), Toast.LENGTH_SHORT).show();
				}
			}
		});

		button_getfil.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				TagFilter_ST tfst2 = myapp.Mreader.new TagFilter_ST();

				READER_ERR er = myapp.Mreader.ParamGet(
						Mtr_Param.MTR_PARAM_TAG_FILTER, tfst2);
				if (er == READER_ERR.MT_OK_ERR) {
					EditText et = (EditText) findViewById(R.id.editText_filterdata);
					EditText etadr = (EditText) findViewById(R.id.editText_invfilsadr);

					if (tfst2.flen == 0) {
						rg_invfilenable.check(rg_invfilenable.getChildAt(0)
								.getId());
						et.setText("");
						etadr.setText("");
						spinner_filbank.setSelection(0);
					} else {
						rg_invfilenable.check(rg_invfilenable.getChildAt(1)
								.getId());
						char[] fd = new char[tfst2.flen /8*2];
						myapp.Mreader.Hex2Str(tfst2.fdata, tfst2.flen/8, fd);
						et.setText(String.valueOf(fd));
						etadr.setText(String.valueOf(tfst2.startaddr));
						spinner_filbank.setSelection(tfst2.bank-1);

						if (tfst2.isInvert == 1)
							rg_invfilmatch.check(rg_invfilmatch.getChildAt(1)
									.getId());
						else
							rg_invfilmatch.check(rg_invfilmatch.getChildAt(0)
									.getId());
					}

				} else
					Toast.makeText(Sub4TabActivity.this,
							MyApplication.Constr_GetFaill + er.toString(), Toast.LENGTH_SHORT).show();
			}
		});

		button_setfil.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try {
					TagFilter_ST tfst = myapp.Mreader.new TagFilter_ST();

					// flen 0 Ϊȡ������ ����nullֵ

					if (commfun.SortGroup(rg_invfilenable) == 1) {

						EditText et = (EditText) findViewById(R.id.editText_filterdata);
						int ln=et.getText().toString().length();
						 if(ln==1||ln%2==1)
							 ln++;
						tfst.fdata = new byte[ln / 2];
						myapp.Mreader.Str2Hex(et.getText().toString(), et
								.getText().toString().length(), tfst.fdata);
						tfst.bank = spinner_filbank.getSelectedItemPosition() + 1;
						EditText etadr = (EditText) findViewById(R.id.editText_invfilsadr);
						tfst.flen = tfst.fdata.length * 8;
						tfst.startaddr = Integer.valueOf(etadr.getText()
								.toString());

						int ma = commfun.SortGroup(rg_invfilmatch);
						if (ma == 1)
							tfst.isInvert = 1;
						else
							tfst.isInvert = 0;
					} else
						tfst = null;

				READER_ERR er = myapp.Mreader.ParamSet(
							Mtr_Param.MTR_PARAM_TAG_FILTER, tfst);
					if (er == READER_ERR.MT_OK_ERR) {
						if(tfst!=null)
						{myapp.Rparams.filadr = tfst.startaddr;
						myapp.Rparams.filbank = tfst.bank;
						EditText et = (EditText) findViewById(R.id.editText_filterdata);
						myapp.Rparams.fildata = et.getText().toString();
						myapp.Rparams.filenable = 1;
						myapp.Rparams.filisinver = tfst.isInvert;
						}
						else
						{
							myapp.Rparams.filadr = 0;
							myapp.Rparams.filbank =1;
							 
							myapp.Rparams.fildata = "";
							myapp.Rparams.filenable = 0;
							myapp.Rparams.filisinver = 0;
						}

						Toast.makeText(Sub4TabActivity.this, MyApplication.Constr_SetOk,
								Toast.LENGTH_SHORT).show();
					} else
						Toast.makeText(Sub4TabActivity.this,
								MyApplication.Constr_SetFaill + er.toString(), Toast.LENGTH_SHORT)
								.show();

				} catch (Exception ex) {
					Toast.makeText(Sub4TabActivity.this,
							MyApplication.Constr_setcep + ex.toString(), Toast.LENGTH_SHORT).show();
				}
			}
		});
		button_getreg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				spinner_region.setSelection(-1);
				Region_Conf[] rcf2 = new Region_Conf[1];
				READER_ERR er = myapp.Mreader.ParamGet(
						Mtr_Param.MTR_PARAM_FREQUENCY_REGION, rcf2);
				if (er == READER_ERR.MT_OK_ERR) {
					switch (rcf2[0]) {
					case RG_PRC:
						spinner_region.setSelection(0);
						break;
					case RG_EU:
						spinner_region.setSelection(4);
						break;
					case RG_EU2:
						spinner_region.setSelection(5);
						break;
					case RG_EU3:
						spinner_region.setSelection(6);
						break;
					case RG_KR:
						spinner_region.setSelection(3);
						break;
					case RG_NA:
						spinner_region.setSelection(1);
						break;
					default:
						spinner_region.setSelection(7);
						break;
					}
				} else
					Toast.makeText(Sub4TabActivity.this,
							MyApplication.Constr_GetFaill + er.toString(), Toast.LENGTH_SHORT).show();
			}
		});
		button_setreg.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Region_Conf rre;
				switch (spinner_region.getSelectedItemPosition()) {
				case 0:
					rre = Region_Conf.RG_PRC;
					break;
				case 1:
					rre = Region_Conf.RG_NA;
					break;
				case 2:
					rre = Region_Conf.RG_NONE;
					break;
				case 3:
					rre = Region_Conf.RG_KR;
					break;
				case 4:
					rre = Region_Conf.RG_EU;
					break;
				case 5:
					rre = Region_Conf.RG_EU2;
					break;
				case 6:
					rre = Region_Conf.RG_EU3;
					break;
				case 7:
				case 8:
				case 9:
				case 10:
				default:
					rre = Region_Conf.RG_NONE;
					break;
				}
				if (rre == Region_Conf.RG_NONE) {
					Toast.makeText(Sub4TabActivity.this, MyApplication.Constr_sub4unspreg,
							Toast.LENGTH_SHORT).show();
					return;
				}

				READER_ERR er = myapp.Mreader.ParamSet(
						Mtr_Param.MTR_PARAM_FREQUENCY_REGION, rre);
				if (er == READER_ERR.MT_OK_ERR) {
					myapp.Rparams.region = spinner_region
							.getSelectedItemPosition();
					myapp.Rparams.frelen = 0;
					Toast.makeText(Sub4TabActivity.this, MyApplication.Constr_SetOk,
							Toast.LENGTH_SHORT).show();
				} else
					Toast.makeText(Sub4TabActivity.this,
							MyApplication.Constr_SetFaill + er.toString(), Toast.LENGTH_SHORT).show();
			}

		});

		button_getfre.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				HoptableData_ST hdst2 = myapp.Mreader.new HoptableData_ST();
				READER_ERR er = myapp.Mreader.ParamGet(
						Mtr_Param.MTR_PARAM_FREQUENCY_HOPTABLE, hdst2);

				int[] tablefre;
				if (er == READER_ERR.MT_OK_ERR) {

					tablefre = commfun.Sort(hdst2.htb, hdst2.lenhtb);
					String[] ssf = new String[hdst2.lenhtb];
					for (int i = 0; i < hdst2.lenhtb; i++) {
						ssf[i] = String.valueOf(tablefre[i]);
					}
					showlist(ssf);

				} else
					Toast.makeText(Sub4TabActivity.this,
							MyApplication.Constr_GetFaill + er.toString(), Toast.LENGTH_SHORT).show();

			}

		});

		button_setfre.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				ArrayList<Integer> lit = new ArrayList<Integer>();
				for (int i = 0; i < elist.getCount(); i++) {
					String temp = (String) elist.getItemAtPosition(i);
					if (MyAdapter2.getIsSelected().get(i)) {
						lit.add(Integer.valueOf(temp));
					}

				}
				if (lit.size() > 0) {
					int[] vls = commfun.CollectionTointArray(lit);
					HoptableData_ST hdst = myapp.Mreader.new HoptableData_ST();
					hdst.lenhtb = vls.length;
					hdst.htb = vls;
					READER_ERR er = myapp.Mreader.ParamSet(
							Mtr_Param.MTR_PARAM_FREQUENCY_HOPTABLE, hdst);
					if (er == READER_ERR.MT_OK_ERR) {
						myapp.Rparams.frecys = hdst.htb;
						myapp.Rparams.frelen = hdst.lenhtb;
						Toast.makeText(Sub4TabActivity.this, MyApplication.Constr_SetOk,
								Toast.LENGTH_SHORT).show();
					} else
						Toast.makeText(Sub4TabActivity.this,
								MyApplication.Constr_SetFaill + er.toString(), Toast.LENGTH_SHORT)
								.show();
				}

			}

		});

		button_getusl.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				EditText ettime = (EditText) findViewById(R.id.editText_invtime);
				EditText etsleep = (EditText) findViewById(R.id.editText_invsleep);

				ettime.setText(String.valueOf((myapp.Rparams.readtime)));
				etsleep.setText(String.valueOf((myapp.Rparams.sleep)));
			}

		});
		button_setusl.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try {
					EditText ettime = (EditText) findViewById(R.id.editText_invtime);
					EditText etsleep = (EditText) findViewById(R.id.editText_invsleep);
					myapp.Rparams.readtime = Integer.valueOf(ettime.getText()
							.toString());
					myapp.Rparams.sleep = Integer.valueOf(etsleep.getText()
							.toString());

				} catch (Exception e) {
					// TODO Auto-generated catch block
					Toast.makeText(Sub4TabActivity.this,
							MyApplication.Constr_SetFaill + e.getMessage(), Toast.LENGTH_SHORT)
							.show();
					return;
				}
				Toast.makeText(Sub4TabActivity.this, MyApplication.Constr_SetOk, Toast.LENGTH_SHORT)
						.show();
			}

		});

		button_invproset.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				List<SL_TagProtocol> ltp = new ArrayList<SL_TagProtocol>();
				List<String> ls = new ArrayList<String>();
				if (cb_gen2.isChecked()) {
					ltp.add(SL_TagProtocol.SL_TAG_PROTOCOL_GEN2);
					ls.add("GEN2");

				}
				if (cb_6b.isChecked()) {
					ltp.add(SL_TagProtocol.SL_TAG_PROTOCOL_ISO180006B);
					ls.add("6B");
				}
				if (cb_ipx64.isChecked()) {
					ltp.add(SL_TagProtocol.SL_TAG_PROTOCOL_IPX64);
					ls.add("IPX64");
				}
				if (cb_ipx256.isChecked()) {
					ltp.add(SL_TagProtocol.SL_TAG_PROTOCOL_IPX256);
					ls.add("IPX256");
				}

				if (ltp.size() < 1) {
					Toast.makeText(Sub4TabActivity.this, MyApplication.Constr_selpro,
							Toast.LENGTH_SHORT).show();
					return;
				}
				Inv_Potls_ST ipst = myapp.Mreader.new Inv_Potls_ST();
				ipst.potlcnt = ltp.size();
				ipst.potls = new Inv_Potl[ipst.potlcnt];
				SL_TagProtocol[] stp = ltp
						.toArray(new SL_TagProtocol[ipst.potlcnt]);
				for (int i = 0; i < ipst.potlcnt; i++) {
					Inv_Potl ipl = myapp.Mreader.new Inv_Potl();
					ipl.weight = 30;
					ipl.potl = stp[i];
					ipst.potls[0] = ipl;
				}

				READER_ERR er = myapp.Mreader.ParamSet(
						Mtr_Param.MTR_PARAM_TAG_INVPOTL, ipst);
				if (er == READER_ERR.MT_OK_ERR) {
					myapp.Rparams.invpro.addAll(ls);
					Toast.makeText(Sub4TabActivity.this, MyApplication.Constr_SetOk,
							Toast.LENGTH_SHORT).show();
				} else
					Toast.makeText(Sub4TabActivity.this,
							MyApplication.Constr_SetFaill + er.toString(), Toast.LENGTH_SHORT).show();
			}

		});
		button_opproget.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

			}

		});
		button_opproset.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

			}

		});

		button_invantsset.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				List<Integer> ltp = new ArrayList<Integer>();

				if (cb_ant1.isChecked())
					ltp.add(1);
				if (cb_ant2.isChecked())
					ltp.add(2);
				if (cb_ant3.isChecked())
					ltp.add(3);
				if (cb_ant4.isChecked())
					ltp.add(4);

				if (ltp.size() < 1) {
					Toast.makeText(Sub4TabActivity.this, MyApplication.Constr_psiant,
							Toast.LENGTH_SHORT).show();
					return;
				}

				Integer[] ants = ltp.toArray(new Integer[ltp.size()]);
				myapp.Rparams.uants = new int[ants.length];
				for (int i = 0; i < ants.length; i++)
					myapp.Rparams.uants[i] = ants[i];

				Toast.makeText(Sub4TabActivity.this, MyApplication.Constr_SetOk, Toast.LENGTH_SHORT)
						.show();
			}

		});

		button_oantuqget.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				int[] val = new int[] { -1 };
				READER_ERR er = myapp.Mreader.ParamGet(
						Mtr_Param.MTR_PARAM_TAGDATA_UNIQUEBYANT, val);

				if (er == READER_ERR.MT_OK_ERR) {
					cb_oant.setChecked(val[0] == 1 ? true : false);
				} else
					Toast.makeText(Sub4TabActivity.this,
							MyApplication.Constr_GetFaill + er.toString(), Toast.LENGTH_SHORT).show();

			}

		});
		button_oantuqset.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				int[] val = new int[] { -1 };
				val[0] = cb_oant.isChecked() ? 1 : 0;
				READER_ERR er = myapp.Mreader.ParamSet(
						Mtr_Param.MTR_PARAM_TAGDATA_UNIQUEBYANT, val);
				if (er == READER_ERR.MT_OK_ERR) {
					myapp.Rparams.antq = val[0];
					Toast.makeText(Sub4TabActivity.this, MyApplication.Constr_SetOk,
							Toast.LENGTH_SHORT).show();
				} else
					Toast.makeText(Sub4TabActivity.this,
							MyApplication.Constr_SetFaill + er.toString(), Toast.LENGTH_SHORT).show();
			}

		});
		button_odatauqget.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				int[] val = new int[] { -1 };
				READER_ERR er = myapp.Mreader.ParamGet(
						Mtr_Param.MTR_PARAM_TAGDATA_UNIQUEBYEMDDATA, val);

				if (er == READER_ERR.MT_OK_ERR) {
					cb_odata.setChecked(val[0] == 1 ? true : false);
				} else
					Toast.makeText(Sub4TabActivity.this,
							MyApplication.Constr_GetFaill + er.toString(), Toast.LENGTH_SHORT).show();
			}

		});
		button_odatauqset.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				int[] val = new int[] { -1 };
				val[0] = cb_odata.isChecked() ? 1 : 0;
				READER_ERR er = myapp.Mreader.ParamSet(
						Mtr_Param.MTR_PARAM_TAGDATA_UNIQUEBYEMDDATA, val);
				if (er == READER_ERR.MT_OK_ERR) {
					myapp.Rparams.adataq = val[0];
					Toast.makeText(Sub4TabActivity.this, MyApplication.Constr_SetOk,
							Toast.LENGTH_SHORT).show();
				} else
					Toast.makeText(Sub4TabActivity.this,
							MyApplication.Constr_SetFaill + er.toString(), Toast.LENGTH_SHORT).show();
			}

		});

		button_hrssiget.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				int[] val = new int[] { -1 };
				READER_ERR er = myapp.Mreader.ParamGet(
						Mtr_Param.MTR_PARAM_TAGDATA_RECORDHIGHESTRSSI, val);

				if (er == READER_ERR.MT_OK_ERR) {
					cb_hrssi.setChecked(val[0] == 1 ? true : false);
				} else
					Toast.makeText(Sub4TabActivity.this,
							MyApplication.Constr_GetFaill + er.toString(), Toast.LENGTH_SHORT).show();

			}

		});
		button_hrssiset.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				int[] val = new int[] { -1 };
				val[0] = cb_hrssi.isChecked() ? 1 : 0;
				READER_ERR er = myapp.Mreader.ParamSet(
						Mtr_Param.MTR_PARAM_TAGDATA_RECORDHIGHESTRSSI, val);
				if (er == READER_ERR.MT_OK_ERR) {
					myapp.Rparams.rhssi = val[0];
					Toast.makeText(Sub4TabActivity.this, MyApplication.Constr_SetOk,
							Toast.LENGTH_SHORT).show();
				} else
					Toast.makeText(Sub4TabActivity.this,
							MyApplication.Constr_SetFaill + er.toString(), Toast.LENGTH_SHORT).show();
			}

		});
		button_invmodeget.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				int[] val = new int[] { -1 };

				READER_ERR er = myapp.Mreader.ParamGet(
						Mtr_Param.MTR_PARAM_TAG_SEARCH_MODE, val);

				if (er == READER_ERR.MT_OK_ERR) {
					spinner_invmode.setSelection(val[0]);
				} else
					Toast.makeText(Sub4TabActivity.this,
							MyApplication.Constr_GetFaill + er.toString(), Toast.LENGTH_SHORT).show();
			}

		});
		button_invmodeset.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				int[] val = new int[] { spinner_invmode
						.getSelectedItemPosition() };
				READER_ERR er = myapp.Mreader.ParamSet(
						Mtr_Param.MTR_PARAM_TAG_SEARCH_MODE, val);
				if (er == READER_ERR.MT_OK_ERR) {
					myapp.Rparams.invw = spinner_invmode
							.getSelectedItemPosition();
					Toast.makeText(Sub4TabActivity.this, MyApplication.Constr_SetOk,
							Toast.LENGTH_SHORT).show();
				} else
					Toast.makeText(Sub4TabActivity.this,
							MyApplication.Constr_SetFaill + er.toString(), Toast.LENGTH_SHORT).show();

			}

		});

		button_6bdpget.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Toast.makeText(Sub4TabActivity.this, MyApplication.Constr_unsupport, Toast.LENGTH_SHORT)
						.show();

			}

		});
		button_6bdpset.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Toast.makeText(Sub4TabActivity.this, MyApplication.Constr_unsupport, Toast.LENGTH_SHORT)
						.show();
			}

		});

		button_6bdltget.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Toast.makeText(Sub4TabActivity.this, MyApplication.Constr_unsupport, Toast.LENGTH_SHORT)
						.show();

			}

		});
		button_6bdltset.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Toast.makeText(Sub4TabActivity.this, MyApplication.Constr_unsupport, Toast.LENGTH_SHORT)
						.show();
			}

		});

		button_6bblfget.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				int[] val = new int[] { -1 };

				READER_ERR er = myapp.Mreader.ParamGet(
						Mtr_Param.MTR_PARAM_POTL_ISO180006B_BLF, val);

				if (er == READER_ERR.MT_OK_ERR) {
					EditText et = (EditText) findViewById(R.id.editText_6bblf);
					et.setText(String.valueOf(val[0]));
				} else
					Toast.makeText(Sub4TabActivity.this,
							MyApplication.Constr_GetFaill + er.toString(), Toast.LENGTH_SHORT).show();

			}

		});
		button_6bblfset.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try {
					EditText et = (EditText) findViewById(R.id.editText_6bblf);

					int[] val = new int[] { Integer.valueOf(et.getText()
							.toString()) };
					READER_ERR er = myapp.Mreader.ParamSet(
							Mtr_Param.MTR_PARAM_POTL_ISO180006B_BLF, val);
					if (er == READER_ERR.MT_OK_ERR)
						Toast.makeText(Sub4TabActivity.this, MyApplication.Constr_SetOk,
								Toast.LENGTH_SHORT).show();
					else
						Toast.makeText(Sub4TabActivity.this,
								MyApplication.Constr_SetFaill + er.toString(), Toast.LENGTH_SHORT)
								.show();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					Toast.makeText(Sub4TabActivity.this,
							MyApplication.Constr_setcep + e.getMessage(), Toast.LENGTH_SHORT)
							.show();
					return;
				}
			}

		});
		button_gettempture.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try {
					EditText et = (EditText) findViewById(R.id.editText_tempure);

					int[] val = new int[1];
					val[0] = 0;
					READER_ERR er = myapp.Mreader.ParamGet(
							Mtr_Param.MTR_PARAM_RF_TEMPERATURE, val);
					if (er == READER_ERR.MT_OK_ERR) {
						et.setText(String.valueOf(val[0]));
						 
					} else
						Toast.makeText(Sub4TabActivity.this,
								MyApplication.Constr_GetFaill + er.toString(), Toast.LENGTH_SHORT)
								.show();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					Toast.makeText(Sub4TabActivity.this,
							MyApplication.Constr_setcep + e.getMessage(), Toast.LENGTH_SHORT)
							.show();
					return;
				}
			}

		});
		button_nostop.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				int metaflag = 0;
				if (cbmf_readcount.isChecked())
					metaflag |= 0X0001;
				if (cbmf_rssi.isChecked())
					metaflag |= 0X0002;
				if (cbmf_ant.isChecked())
					metaflag |= 0X0004;
				if (cbmf_fre.isChecked())
					metaflag |= 0X0008;
				if (cbmf_time.isChecked())
					metaflag |= 0X0010;
				if (cbmf_rfu.isChecked())
					metaflag |= 0X0020;
				if (cbmf_pro.isChecked())
					metaflag |= 0X0040;
				if (cbmf_dl.isChecked())
					metaflag |= 0X0080;

				myapp.Rparams.option = (metaflag<<8)
						| spinner_persen.getSelectedItemPosition();

				if (cb_nostop.isChecked())
					myapp.nostop = true;
				else
					myapp.nostop = false;
			}
		});
		
		button_q11200.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
 
				myapp.Rparams.option = 0; 
				myapp.nostop = true;
				int[] mp=new int[1];
				 
				READER_ERR er=myapp.Mreader.ParamGet(
						Mtr_Param.MTR_PARAM_RF_MAXPOWER, mp);
				if(er==READER_ERR.MT_OK_ERR)
				{AntPowerConf apcf = myapp.Mreader.new AntPowerConf();
				apcf.antcnt = myapp.antportc;
				int[] rpow = new int[apcf.antcnt];
				int[] wpow = new int[apcf.antcnt];
				for (int i = 0; i < apcf.antcnt; i++) {
					AntPower jaap = myapp.Mreader.new AntPower();
					jaap.antid = i + 1;
					jaap.readPower = (short) (mp[0]);
					rpow[i] = jaap.readPower;

					jaap.writePower = (short) (mp[0]);
					wpow[i] = jaap.writePower;
					apcf.Powers[i] = jaap;
				}
				er = myapp.Mreader.ParamSet(
						Mtr_Param.MTR_PARAM_RF_ANTPOWER, apcf);
				if(er==READER_ERR.MT_OK_ERR)
					Toast.makeText(Sub4TabActivity.this,
							MyApplication.Constr_setpwd+String.valueOf((short)mp[0]), Toast.LENGTH_SHORT)
							.show();
				}
				
				
				er = myapp.Mreader.ParamSet(
						Mtr_Param.MTR_PARAM_POTL_GEN2_SESSION, new int[]{1});
				if(er==READER_ERR.MT_OK_ERR)
					Toast.makeText(Sub4TabActivity.this,
							MyApplication.Constr_set+"Session 1", Toast.LENGTH_SHORT)
							.show();
				myapp.Rparams.sleep=0;
				cb_nostop.setChecked(true);
			}
		});
		
		button_q21200.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
 
				myapp.Rparams.option = 0; 
				myapp.nostop = true;
				int[] mp=new int[1];
				 
				READER_ERR er=myapp.Mreader.ParamGet(
						Mtr_Param.MTR_PARAM_RF_MAXPOWER, mp);
				if(er==READER_ERR.MT_OK_ERR)
				{AntPowerConf apcf = myapp.Mreader.new AntPowerConf();
				apcf.antcnt = myapp.antportc;
				int[] rpow = new int[apcf.antcnt];
				int[] wpow = new int[apcf.antcnt];
				for (int i = 0; i < apcf.antcnt; i++) {
					AntPower jaap = myapp.Mreader.new AntPower();
					jaap.antid = i + 1;
					jaap.readPower = (short) (mp[0]);
					rpow[i] = jaap.readPower;

					jaap.writePower = (short) (mp[0]);
					wpow[i] = jaap.writePower;
					apcf.Powers[i] = jaap;
				}
				er = myapp.Mreader.ParamSet(
						Mtr_Param.MTR_PARAM_RF_ANTPOWER, apcf);
				if(er==READER_ERR.MT_OK_ERR)
					Toast.makeText(Sub4TabActivity.this,
							MyApplication.Constr_setpwd+String.valueOf((short)mp[0]), Toast.LENGTH_SHORT)
							.show();
				}
				
				
				er = myapp.Mreader.ParamSet(
						Mtr_Param.MTR_PARAM_POTL_GEN2_SESSION, new int[]{0});
				if(er==READER_ERR.MT_OK_ERR)
					Toast.makeText(Sub4TabActivity.this,
							MyApplication.Constr_set+"Session 0", Toast.LENGTH_SHORT)
							.show();
				myapp.Rparams.sleep=0;
				cb_nostop.setChecked(true);
			}
		});
		
		cb_allsel.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@SuppressWarnings("static-access")
			@Override
			public void onCheckedChanged(CompoundButton arg0, boolean arg1) {
				// TODO Auto-generated method stub
				MyAdapter2 m2 = (MyAdapter2) elist.getAdapter();
				if (arg1 == true) {
					HashMap<Integer, Boolean> isSelected = new HashMap<Integer, Boolean>();

					for (int m = 0; m < m2.getCount(); m++)
						isSelected.put(m, true);
					m2.setIsSelected(isSelected);

				} else {
					HashMap<Integer, Boolean> isSelected = new HashMap<Integer, Boolean>();

					for (int m = 0; m < m2.getCount(); m++)
						isSelected.put(m, false);
					m2.setIsSelected(isSelected);
				}
				elist.setAdapter(m2);
			}

		});
		tabHost_set.setOnTabChangedListener(new OnTabChangeListener() {

			@Override
			public void onTabChanged(String arg0) {
				// TODO Auto-generated method stub
				int j = tabHost_set.getCurrentTab();
				TabWidget tabIndicator = tabHost_set.getTabWidget();
				View vw = tabIndicator.getChildAt(j);
				vw.setBackgroundColor(Color.BLUE);
				int tc = tabHost_set.getTabContentView().getChildCount();
				for (int i = 0; i < tc; i++) {
					if (i != j) {
						View vw2 = tabIndicator.getChildAt(i);
						vw2.setBackgroundColor(Color.TRANSPARENT);
					} else {
						switch (i) {
						case 0:

							button_getusl.performClick();
							button_opproget.performClick();
							break;
						case 1:
							button_getantcheck.performClick();
							button_getantpower.performClick();
							break;
						case 2:
							button_getreg.performClick();
							button_getfre.performClick();
							break;
						case 3:
							button_getgen2ses.performClick();
							button_getgen2q.performClick();
							button_getwmode.performClick();
							// button_getgen2blf.performClick();
							button_getgen2maxl.performClick();
							button_getgen2targ.performClick();
							button_getgen2code.performClick();
							// button_getgen2tari.performClick();
							break;
						case 4:
							button_getfil.performClick();
							break;
						case 5:
							button_getemd.performClick();
							break;
						case 7:
							button_oantuqget.performClick();
							button_odatauqget.performClick();
							button_hrssiget.performClick();
							break;
						}
					}
				}

			}

		});

		cb_gen2.setChecked(true);
		cb_6b.setChecked(false);
		cb_ipx64.setChecked(false);
		cb_ipx256.setChecked(false);
		for (int k = 0; k < myapp.Rparams.invpro.size(); k++) {
			if (myapp.Rparams.invpro.get(k).equals("GEN2"))
				cb_gen2.setChecked(true);
			else if (myapp.Rparams.invpro.get(k).equals("6B"))
				cb_6b.setChecked(true);
			else if (myapp.Rparams.invpro.get(k).equals("IPX64"))
				cb_ipx64.setChecked(true);
			else if (myapp.Rparams.invpro.get(k).equals("IPX256"))
				cb_ipx256.setChecked(true);
		}

		cb_ant1.setChecked(false);
		cb_ant2.setChecked(false);
		cb_ant3.setChecked(false);
		cb_ant4.setChecked(false);
		for (int k = 0; k < myapp.Rparams.uants.length; k++) {
			if (myapp.Rparams.uants[k] == 1) {
				cb_ant1.setChecked(true);
			} else if (myapp.Rparams.uants[k] == 2) {
				cb_ant2.setChecked(true);
			} else if (myapp.Rparams.uants[k] == 3) {
				cb_ant3.setChecked(true);
			} else if (myapp.Rparams.uants[k] == 4) {
				cb_ant4.setChecked(true);
			}
		}
		this.button_getusl.performClick();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK
				&& event.getAction() == KeyEvent.ACTION_DOWN) {
			if ((System.currentTimeMillis() - myapp.exittime) > 2000) {
				Toast.makeText(getApplicationContext(), MyApplication.Constr_Putandexit,
						Toast.LENGTH_SHORT).show();
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
