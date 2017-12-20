package com.reader.modulereader;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Application;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class SubPathActivity extends ActionBarActivity {
	 
	Button button_selectfile;
	ListView listview;
	public static String tag = "SaveFileDialog";  
   static final public String sRoot = "/";   
   static final public String sParent = "..";  
   static final public String sFolder = ".";  
   static final public String sEmpty = "";  
   static final private String sOnErrorMsg = "No rights to access!";  
 
   private String path = sRoot;  
   private List<Map<String, Object>> list = null;
   private Map<String, Integer> imagemap = new HashMap<String, Integer>(); ;  
   
   MyApplication myapp;
   
   private int getImageId(String s){  
       if(imagemap == null){  
           return 0;  
      }  
      else if(imagemap.containsKey(s)){  
           return imagemap.get(s);  
      }  
       else if(imagemap.containsKey(sEmpty)){  
           return imagemap.get(sEmpty);  
       }  
       else {  
           return 0;  
       }  
  }  
   private String getSuffix(String filename){  
       int dix = filename.lastIndexOf('.');  
       if(dix<0){  
           return "";  
       }  
       else{  
          return filename.substring(dix+1);  
       }  
  }  
   private int refreshFileList()  
   {  
       // ˢ���ļ��б�   
        File[] files = null;  
        try{  
           files = new File(path).listFiles();  
       }  
        catch(Exception e){  
            files = null;  
        }  
        if(files==null){  
           // ���ʳ���   
            Toast.makeText(this, sOnErrorMsg,Toast.LENGTH_SHORT).show();  
           return -1;  
       }  
       if(list != null){  
            list.clear();  
        }  
        else{  
           list = new ArrayList<Map<String, Object>>(files.length);  
        }  
         
      // �����ȱ����ļ��к��ļ��е������б�   
       ArrayList<Map<String, Object>> lfolders = new ArrayList<Map<String, Object>>();  
       ArrayList<Map<String, Object>> lfiles = new ArrayList<Map<String, Object>>();  
         
       if(!this.path.equals(sRoot)){  
           // ��Ӹ�Ŀ¼ �� ��һ��Ŀ¼   
            Map<String, Object> map = new HashMap<String, Object>();  
            map.put("name", sRoot);  
            map.put("path", sRoot);  
            map.put("img", getImageId(sRoot));  
            list.add(map);  
              
           map = new HashMap<String, Object>();  
           map.put("name", sParent);  
           map.put("path", path);  
            map.put("img", getImageId(sParent));  
            list.add(map);  
        }  
       String suffix=null;//".bin;"
       for(File file: files)  
       {  
           if(file.isDirectory() && file.listFiles()!=null){  
                // ����ļ���   
               Map<String, Object> map = new HashMap<String, Object>();  
               map.put("name", file.getName());  
                map.put("path", file.getPath());  
               map.put("img", getImageId(sFolder));  
                lfolders.add(map);  
            }  
            else if(file.isFile()){  
                // ����ļ�   
               String sf = getSuffix(file.getName()).toLowerCase();  
               if(suffix == null || suffix.length()==0 || (sf.length()>0 && suffix.indexOf("."+sf+";")>=0)){  
                    Map<String, Object> map = new HashMap<String, Object>();  
                    map.put("name", file.getName());  
                    map.put("path", file.getPath());  
                    map.put("img", getImageId(sf));  
                    lfiles.add(map);  
                }  
            } 
             
        }  
         
       list.addAll(lfolders); // ������ļ��У�ȷ���ļ�����ʾ������   
       list.addAll(lfiles);    //������ļ�   
          
          
        SimpleAdapter adapter = new SimpleAdapter(this, list, R.layout.filedialogitem, new String[]{"img", "name", "path"}, new int[]{R.id.filedialogitem_img, R.id.filedialogitem_name, R.id.filedialogitem_path});  
        listview.setAdapter(adapter);  
        return files.length;  
    }  
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.path_location);
		Application app=getApplication();
		myapp=(MyApplication) app; 
		 
		 // ���漸�����ø��ļ����͵�ͼ�꣬ ��Ҫ���Ȱ�ͼ����ӵ���Դ�ļ���  
		 
		imagemap.put(sRoot, R.drawable.filedialog_root);   // ��Ŀ¼ͼ��   
		imagemap.put(sParent, R.drawable.filedialog_folder_up);    //������һ���ͼ��   
		imagemap.put(sFolder, R.drawable.filedialog_folder);   //�ļ���ͼ��   
		imagemap.put("bin", R.drawable.filedialog_file);  
      //images.put("wav", R.drawable.filedialog_wavfile);   //wav�ļ�ͼ��   //wav�ļ�ͼ��   
		imagemap.put(sEmpty, R.drawable.filedialog_root);  
        
        
		listview=(ListView)this.findViewById(R.id.listView_list);

		button_selectfile=(Button)this.findViewById(R.id.button_select);
		button_selectfile.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Toast.makeText(SubPathActivity.this, path,
						Toast.LENGTH_SHORT).show();
				SubSystemActivity.et_path.setText(path+"/out.txt");
				finish();
			}
		});
		
		listview.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				 // ��Ŀѡ��   
	             String pt = (String) list.get(arg2).get("path");  
	            String fn = (String) list.get(arg2).get("name");  
	             if(fn.equals(sRoot) || fn.equals(sParent)){  
	                 // ����Ǹ�Ŀ¼������һ��   
	                 File fl = new File(pt);  
	                String ppt = fl.getParent();  
	                 if(ppt != null){  
	                     // ������һ��   
	                     path = ppt;  
	                 }  
	                else{  
	                     // ���ظ�Ŀ¼   
	                     path = sRoot;  
	                 }  
	             }  
	           else{  
	                File fl = new File(pt);  
	               if(fl.isFile()){  
	                   // ������ļ�   
	                  
	                    return;  
	               }  
	               else if(fl.isDirectory()){  
	                   // ������ļ���   
	                    // ��ô����ѡ�е��ļ���   
	                   path = pt;  
	                   
	                }  
	           }  
	            refreshFileList();
			}
		});
		
		 refreshFileList();  
	}
 
}
