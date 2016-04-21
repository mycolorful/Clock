package per.yrj.utils;

import java.util.Calendar;
import java.util.Date;

import per.yrj.model.Clock;
import per.yrj.receiver.ClockReceiver;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class Utils {
	/**
	 * 设置闹钟,并提示用户还多久响起
	 * @param context 上下文对象
	 * @param clock	一个闹钟对象
	 */
	public static void setOnClock(Context context,Clock clock){
		AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY, clock.getHour());
		c.set(Calendar.MINUTE, clock.getMinute());
		Intent intent = new Intent(context, ClockReceiver.class);
		PendingIntent operation = PendingIntent.getBroadcast(context, 0, intent, 0);
		Calendar now = Calendar.getInstance();
		if(clock.isRepeat()){
//			alarm.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis()
//					, intervalMillis, operation);
		}else{
			if(c.compareTo(now) < 0){
				c.add(Calendar.DAY_OF_MONTH, 1);
			}
			alarm.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), operation);
		}
		long delayTime = c.getTimeInMillis() - now.getTimeInMillis();
		int days = (int) (delayTime/(1000l*60*60*24));
		delayTime %= 1000l*60*60*24;
		int hours = (int) (delayTime/(1000l*60*60));
		delayTime %= 1000l*60*60;
		int minutes = (int) (delayTime/(1000l*60));
		//设置提示信息
		String s = "闹钟将在";
		s += days+"天";
		s += hours+"小时";
		s += minutes+"分钟";
		s += "后响起";
		Toast.makeText(context, s, Toast.LENGTH_LONG).show();
	}
}
