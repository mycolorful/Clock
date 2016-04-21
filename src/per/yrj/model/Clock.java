package per.yrj.model;

import java.util.Calendar;
import java.util.Date;

import android.os.Parcel;
import android.os.Parcelable;
import android.widget.CalendarView;

public class Clock implements Parcelable {
	/**
	 * 选择按钮是否可见
	 */
	private boolean isCheckBoxVisiable;
	/**
	 * 唤醒时间，小时
	 */
	private int mHour = 7;
	/**
	 * 唤醒时间，分钟数
	 */
	private int mMinute = 0;
	/**
	 * 闹钟标题
	 */
	private String clockTitle;
	/**
	 * 闹钟是否开启
	 */
	private boolean isOn;
	/**
	 * 闹钟是否重复
	 */
	private boolean isRepeat;
	/**
	 * 重复频率
	 */
	private boolean[] repeatRate = new boolean[7];
	/**
	 * 距离闹钟响起的时间
	 */
	private long weakMillis;
	/**
	 * 是否删除
	 */
	private boolean isDelItem;
	
	public Clock(){
		isCheckBoxVisiable = false;
		isDelItem = false;
		isOn = true;
	}
	
	public Clock(String clockTitle, int hour, int minute){
		this();
		this.clockTitle = clockTitle;
		this.mHour = hour;
		this.mMinute = minute;
	}
	
	public static final Parcelable.Creator<Clock> CREATOR  = new Creator<Clock>(){

		@Override
		public Clock createFromParcel(Parcel source) {
			Clock c = new Clock();
			c.setCheckBoxVisiable(source.readByte()!= 0);
			c.setHour(source.readInt());
			c.setMinute(source.readInt());
			c.setClockTitle(source.readString());
			c.setOn(source.readByte()!=0);
//			boolean[] a = new boolean [8];
//			for(int i = 0; i < c.getRepeatRate().length; i++){
//				a[i] = source.readB();
//			}
//			c.setRepeatRate(a);
			c.setRepeat(source.readByte()!=0);
			source.readBooleanArray(c.getRepeatRate());
			c.setWeakMillis(source.readLong());
			c.setDelItem(source.readByte()!=0);
			return c;
		}

		@Override
		public Clock[] newArray(int size) {
			return new Clock[size];
		}
		
	};
	
	/**
	 * 返回字符串类型的时间
	 * @return
	 */
	public String getTime(){
		String sHour = mHour<10?"0"+mHour:mHour+"";
		String sMinute = mMinute<10?"0"+mMinute:mMinute+"";
		return sHour+":"+sMinute;
	}
	
	public boolean isDelItem() {
		return isDelItem;
	}

	public void setDelItem(boolean isDelItem) {
		this.isDelItem = isDelItem;
	}

	public boolean isCheckBoxVisiable() {
		return isCheckBoxVisiable;
	}
	public void setCheckBoxVisiable(boolean isCheckBoxVisiable) {
		this.isCheckBoxVisiable = isCheckBoxVisiable;
	}
	public int getHour() {
		return mHour;
	}

	public void setHour(int Hour) {
		this.mHour = Hour;
	}

	public int getMinute() {
		return mMinute;
	}

	public void setMinute(int Minute) {
		this.mMinute = Minute;
	}

	public boolean isOn() {
		return isOn;
	}

	public void setOn(boolean isOn) {
		this.isOn = isOn;
	}

	public boolean isRepeat() {
		return isRepeat;
	}

	public void setRepeat(boolean isRepeat) {
		this.isRepeat = isRepeat;
	}

	public boolean[] getRepeatRate() {
		return repeatRate;
	}

	public void setRepeatRate(boolean[] repeatRate) {
		this.repeatRate = repeatRate;
	}

	public String getClockTitle() {
		return clockTitle;
	}
	public void setClockTitle(String clockTitle) {
		this.clockTitle = clockTitle;
	}

	public long getWeakMillis() {
		Calendar currentTime = Calendar.getInstance();
		currentTime.setTimeInMillis(System.currentTimeMillis());
		Calendar setTime = (Calendar) currentTime.clone();
		setTime.set(Calendar.HOUR_OF_DAY, mHour);
		setTime.set(Calendar.MINUTE, mMinute);
		if(currentTime.compareTo(setTime) != -1){
			setTime.roll(Calendar.DAY_OF_WEEK, true);
		}
		return setTime.getTimeInMillis() - currentTime.getTimeInMillis();
	}

	public void setWeakMillis(long weakMillis) {
		this.weakMillis = weakMillis;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeByte((byte) (isCheckBoxVisiable?1:0));
		dest.writeInt(mHour);
		dest.writeInt(mMinute);
		dest.writeString(clockTitle);
		dest.writeByte((byte) (isOn?1:0));
//		for(int i = 0; i < repeatRate.length; i++){
//			dest.writeInt(repeatRate[i]);
//		}
		dest.writeByte((byte) (isRepeat?1:0));
		dest.writeBooleanArray(repeatRate);
		dest.writeLong(weakMillis);
		dest.writeByte((byte) (isDelItem?1:0));
		
	}
	
	
	
}
