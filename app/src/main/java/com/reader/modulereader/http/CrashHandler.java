package com.reader.modulereader.http;

import android.content.Context;
import android.os.Environment;

import com.reader.modulereader.Constants;
import com.reader.modulereader.utils.DateUtil;
import com.reader.modulereader.utils.LogUtil;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * 创建者：     金国栋      <br/><br/>
 * 创建时间:   2016/10/9 16:51   <br/><br/>
 * 描述:	      处理未捕获异常
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {

	private static CrashHandler instance;
	private Context mContext;

	public static CrashHandler getInstance() {
		if (instance == null) {
			synchronized (CrashHandler.class) {
				if (instance == null) {
					instance = new CrashHandler();
				}
			}
		}
		return instance;
	}

	public void init(Context context) {
		mContext = context;
		Thread.setDefaultUncaughtExceptionHandler(this);
	}

	@Override
	public void uncaughtException(Thread thread, Throwable error) {
		String logPath;
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			logPath = Environment.getExternalStorageDirectory()
					.getAbsolutePath()
					+ File.separator
					+ File.separator
					+ Constants.LOG_DIR_NAME;

			File file = new File(logPath);
			if (!file.exists()) {
				file.mkdirs();
			}

			//对文件修改时间进行排序
			List<File> files = Arrays.asList(file.listFiles());
			Collections.sort(files, (o1, o2) -> {
				long time_1 = o1.lastModified();
				long time_2 = o2.lastModified();
				int value = time_1 > time_2 ? 1 : -1;
				return value;
			});

			//如果日志文件数量大于50就删除一部分，只保留最近10条
			if (files.size() >= 50) {
				for (int i = 0; i < files.size() - 10; i++) {
					files.get(i).delete();
				}
			}
			//写入错误日志
			try {
				long date = System.currentTimeMillis();
				String dateStr = String.valueOf(date);
				FileWriter fw = new FileWriter(logPath + File.separator
						+ String.format("errorlog_%s.log", dateStr.substring(0, dateStr.length() - 5)), true);
				fw.write(DateUtil.defDateFormat(date) + "\n");
				// 错误信息
				fw.write(error.getMessage() + "\n");
				fw.write("\n");
				fw.close();
				LogUtil.i("------>>>application crash save log... <<<--------");
			} catch (IOException e) {
				LogUtil.e(e.toString());
			}
		}
		error.printStackTrace();
		//结束进程
		android.os.Process.killProcess(android.os.Process.myPid());
	}
}
