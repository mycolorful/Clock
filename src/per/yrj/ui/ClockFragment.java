package per.yrj.ui;

import java.util.ArrayList;
import java.util.ListIterator;

import per.yrj.clock.R;
import per.yrj.model.Clock;

import android.app.Activity;
import android.app.AlarmManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

public class ClockFragment extends ListFragment implements OnClickListener{
	public static final int ADD_COLCK = 0X11;
	public static final int EDIT_CLOCK = 0X22;
	public static final String EXTRA_CLOCK = "clock";
	private Context context;
	private Button btAddClock;
	private Button btDelClock;
	private TextView tvTime;
	private TextView tvTitle;
	private ArrayList<Clock> data;
	private MyAdapter myAdapter;
	private boolean isSelecting = false;
	private int mClickItemPosition;
	
	@Override
	public void onAttach(Activity activity) {
		context = activity;
		data = new ArrayList<Clock>();
		myAdapter = new MyAdapter();
		super.onAttach(activity);
	}
	

	@Override
	@Nullable
	public View onCreateView(LayoutInflater inflater, 
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.clock_fra, null);
		btAddClock = (Button) view.findViewById(R.id.addClock);
		btAddClock.setOnClickListener(this);
		btDelClock = (Button) view.findViewById(R.id.delClock);	
		btDelClock.setOnClickListener(this);
		setListAdapter(myAdapter);
		return view;
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		if(isSelecting){
			//如果在选择删除item的页面
			CheckBox checkBox = (CheckBox) v.findViewById(R.id.checkBox);
			checkBox.setChecked(!checkBox.isChecked());
			myAdapter.notifyDataSetChanged();
		}else{
			//将被选中的item的position记录下来
			mClickItemPosition = position;
			//否则就在正常页面
			//跳转值闹钟设置页面
			Intent intent = new Intent(context, SetClockActivity.class);
			//获取当前item的clock对象并装入intent中
			Clock clock = (Clock) myAdapter.getItem(position);
			intent.putExtra(EXTRA_CLOCK,clock);
			startActivityForResult(intent, EDIT_CLOCK);
		}
	}
	
	private class MyAdapter extends BaseAdapter{
			
			@Override
			public View getView(int position, View convertView, ViewGroup parent) {
				ViewGroup layout = null;
				if(convertView == null){
					layout = (ViewGroup) LayoutInflater.from(context).
							inflate(R.layout.clock_list_item, parent, false);
				}else{
					layout = (ViewGroup) convertView;
				}
				final Clock clockListItem = data.get(position);
				
				//设置checkBox的业务逻辑
				CheckBox cbSelect = (CheckBox) layout.findViewById(R.id.checkBox);
				cbSelect.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					
					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						clockListItem.setDelItem(isChecked);
					}
				});
				//同步isDelItem与cbSelect的值
				cbSelect.setChecked(clockListItem.isDelItem());

				//设置Switch的业务逻辑
				Switch clockSwitch = (Switch) layout.findViewById(R.id.clockSwitch);
				clockSwitch.setChecked(clockListItem.isOn());
				clockSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					
					@Override
					public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
						clockListItem.setOn(!isChecked);
						if(isChecked){
							setOnClock(clockListItem);
						}else{
							setOffClock(clockListItem);
						}
						
					}
					
					/**
					 * 取消闹钟
					 * @param clockListItem
					 */
					private void setOffClock(Clock clockListItem) {
						// TODO Auto-generated method stub
						
					}

					/**
					 * 开启闹钟
					 * @param clockListItem
					 */
					private void setOnClock(Clock clockListItem) {
						AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
						long weakMillis = clockListItem.getWeakMillis();
						Toast.makeText(context, weakMillis/(1000*60)+"",Toast.LENGTH_LONG).show();
					}
				});
				
				tvTime = (TextView) layout.findViewById(R.id.weakTime);
				tvTitle = (TextView) layout.findViewById(R.id.clockTitle);
				//选择框的设置
				if(clockListItem.isCheckBoxVisiable()){
					cbSelect.setVisibility(View.VISIBLE);
				}else{
					cbSelect.setVisibility(View.GONE);
				}
				
				//闹钟时间的设置
				tvTime.setText(clockListItem.getTime());
				//闹钟标题的设置
				tvTitle.setText(clockListItem.getClockTitle());
				return layout;
			}
			
			@Override
			public long getItemId(int position) {
				return position;
			}
			
			@Override
			public Object getItem(int position) {
				return data.get(position);
			}
			
			@Override
			public int getCount() {
				return data.size();
			}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode ==2){
			Clock clock = data.getParcelableExtra(EXTRA_CLOCK);
			if(requestCode == ADD_COLCK){
				this.data.add(clock);
			}
			if(requestCode == EDIT_CLOCK){
				this.data.set(mClickItemPosition, clock);
			}
			myAdapter.notifyDataSetChanged();
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.addClock){
			//判断是否是在选择页面
			if(isSelecting){
				//如果在的话隐藏选择框
				hideChechBox();
				//并且将已选择要删除的全部重置为false
				for(ListIterator<Clock> iterator = data.listIterator(0); iterator.hasNext();){
					Clock c = iterator.next();
					if(c.isDelItem()){
						c.setDelItem(false);
					}
				}
			}else{
				Intent intent = new Intent(context, SetClockActivity.class);
				//启动闹钟设置页面，添加闹钟
				startActivityForResult(intent, ADD_COLCK);
			}
		}
		if(v.getId() == R.id.delClock){
			//判断是否是在选择页面
			if(isSelecting){
				for(ListIterator<Clock> iterator = data.listIterator(0); iterator.hasNext();){
					if(iterator.next().isDelItem()){
						iterator.remove();
					}
				}
				
				hideChechBox();
			}else{
				//如果不在选择页面，则显示选择框，并将btAddClock设为取消
				for(Clock item: data){
					System.out.println(item.isDelItem());
					item.setCheckBoxVisiable(true);
				}
				btAddClock.setText("取消");
				isSelecting = !isSelecting;
			} 
		}
		myAdapter.notifyDataSetChanged();
	}
	
	/**
	 * 隐藏选择框，并且将btAddClock按钮改回添加
	 */
	private void hideChechBox(){
		for(Clock item: data){
			item.setCheckBoxVisiable(false);
		}
		btAddClock.setText("添加");
		isSelecting = !isSelecting;
	}
	
}
