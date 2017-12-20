package com.reader.modulereader.function;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;

import com.reader.modulereader.R;

import java.util.ArrayList;
import java.util.HashMap;

public class MyAdapter2 extends BaseAdapter {  
   // ������ݵ�list   
    private ArrayList<String> list;  
    // ��������CheckBox��ѡ��״��   
    private static HashMap<Integer, Boolean> isSelected;  
    // �������벼��   
    private LayoutInflater inflater = null;  
  
    // ������   
    @SuppressLint("UseSparseArrays")
	public MyAdapter2(ArrayList<String> list, Context context) {  
        this.list = list;  
       inflater = LayoutInflater.from(context);  
       isSelected = new HashMap<Integer, Boolean>();  
       // ��ʼ������   
       initDate();  
    }  
  
   // ��ʼ��isSelected������   
   private void initDate() {  
       for (int i = 0; i < list.size(); i++) {  
           getIsSelected().put(i, false);  
       }  
   }  
 
    @Override  
    public int getCount() {  
        return list.size();  
   }  
  
   @Override  
    public Object getItem(int position) {  
        return list.get(position);  
    }  
  
    @Override  
    public long getItemId(int position) {  
        return position;  
   }  
  
   @Override  
    public View getView(int position, View convertView, ViewGroup parent) {  
        ViewHolder holder = null;  
        if (convertView == null) {  
           // ���ViewHolder����   
           holder = new ViewHolder();  
           // ���벼�ֲ���ֵ��convertview   
            convertView = inflater.inflate(R.layout.listitemview_fre, null);
  
            holder.cb = (CheckBox) convertView.findViewById(R.id.item_cb); 
           // Log.d("SLB4.0", (String)getItem(position));
            
           // Ϊview���ñ�ǩ   
           convertView.setTag(holder);  
        } else {  
           // ȡ��holder   
            holder = (ViewHolder) convertView.getTag();  
        }  
        holder.cb.setText((String)getItem(position));
        // ����isSelected������checkbox��ѡ��״��   
       holder.cb.setChecked(getIsSelected().get(position));  
       return convertView;  
    }  
  
   public static HashMap<Integer, Boolean> getIsSelected() {  
       return isSelected;  
    }  
  
    public static void setIsSelected(HashMap<Integer, Boolean> isSelected) {  
        MyAdapter2.isSelected = isSelected;  
    }  
  
    public static class ViewHolder {    
       public CheckBox cb;  
    }  
}  