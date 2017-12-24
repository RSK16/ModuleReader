package com.reader.modulereader;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.reader.modulereader.entity.Course;

import java.util.List;

/**
 * Created by 赵康 on 2017/12/22.
 */

public class BookAdapter extends BaseAdapter {

    private List<Course.CourseBean> titles;
    private final Context context;

    public BookAdapter(List<Course.CourseBean> titles, Context context) {
        super();
        this.titles = titles;
        this.context = context;

    }

    @Override
    public int getCount() {
        return titles.size();
    }

    @Override
    public Object getItem(int i) {
        return titles.get(i);
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
            if (titles.get(position).readed) {
                viewHolder.title.setBackgroundResource(R.drawable.textview_round_border_green);
                viewHolder.title.setTextColor(MyApplication.getInstance().getResources().getColor(R.color.green));
            } else {
                viewHolder.title.setBackgroundResource(R.drawable.textview_round_border_red);
                viewHolder.title.setTextColor(MyApplication.getInstance().getResources().getColor(R.color.red));
            }
            // 给converHolder附加一个对象
            convertView.setTag(viewHolder);
        } else {
            // 取得converHolder附加的对象
            viewHolder = (ViewHolder) convertView.getTag();
        }


        viewHolder.title.setText(titles.get(position).title);

        return convertView;
    }

    class ViewHolder {
        public TextView title;
    }
}
