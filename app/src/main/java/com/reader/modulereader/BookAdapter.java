package com.reader.modulereader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.reader.modulereader.entity.Course;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 赵康 on 2017/12/22.
 */

public class BookAdapter extends BaseAdapter {

    private List<Course.BasecourseBean> titles;
    private List<Course.BasecourseBean> titlesAll;
    private final Context context;

    public BookAdapter(List<Course.BasecourseBean> titles, Context context) {
        super();
        this.titles = titles;
        this.context = context;
        titlesAll = new ArrayList<>();
        for (Course.BasecourseBean basecourseBean : titles) {
            if (basecourseBean.readed>0) {
                titlesAll.add(basecourseBean);
            }
        }
    }

    public void updateAll(){
        titlesAll.clear();
        for (Course.BasecourseBean basecourseBean : titles) {
            if (basecourseBean.readed>0) {
                titlesAll.add(basecourseBean);
            }
        }
    }

    @Override
    public int getCount() {
        return titlesAll.size();
    }

    @Override
    public Object getItem(int i) {
        return titlesAll.get(i);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;

        if (convertView == null) {

            viewHolder = new ViewHolder();
            // 获得容器
            convertView = LayoutInflater.from(this.context).inflate(R.layout.book, null);

            // 初始化组件
            viewHolder.title = (TextView) convertView.findViewById(R.id.title);
            // 给converHolder附加一个对象
            convertView.setTag(viewHolder);
        } else {
            // 取得converHolder附加的对象
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.title.setText(titlesAll.get(position).COURSE);
        if (titlesAll.get(position).readed==1) {
            viewHolder.title.setBackgroundResource(R.drawable.textview_round_border_red);
        } else if (titlesAll.get(position).readed==2){
            viewHolder.title.setBackgroundResource(R.drawable.textview_round_border_green);
        } else if (titlesAll.get(position).readed == 3) {
            viewHolder.title.setBackgroundResource(R.drawable.textview_round_border_orange);
        }
        return convertView;
    }

    class ViewHolder {
        public TextView title;
    }
}
