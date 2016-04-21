package per.yrj.ui;


import java.util.ArrayList;
import java.util.List;

import per.yrj.clock.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;

public class MainActivity extends FragmentActivity implements OnClickListener {
	// 五大导航按钮
	private Button btMsg;
	private Button btPeople;
	private Button btClock;
	private Button btWeak;
	//五个fragment
	private MsgFragment msgFragment;
	private PeopleFragment peopleFragment;
	private ClockFragment clockFragment;
	private WeakFragment weakFragment;
	//将fragment集合在一起
	private List<Fragment> mFragments;
	
	private ViewPager viewPager;
	private ImageView ivUnderLine;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// 导航按钮初始化
		btMsg = (Button) findViewById(R.id.msg);
		btPeople = (Button) findViewById(R.id.people);
		btClock = (Button) findViewById(R.id.clock);
		btWeak = (Button) findViewById(R.id.weak);
		//fragment初始化
		msgFragment = new MsgFragment();
		peopleFragment = new PeopleFragment();
		clockFragment = new ClockFragment();
		weakFragment = new WeakFragment();
		//将五个fragment添加到fragments中去
		mFragments = new ArrayList<Fragment>();
		mFragments.add(msgFragment);
		mFragments.add(peopleFragment);
		mFragments.add(clockFragment);
		mFragments.add(weakFragment);
		viewPager = (ViewPager) findViewById(R.id.view_pager);
		ivUnderLine = (ImageView) findViewById(R.id.iv_underline);
		// 设置事件监听
		btMsg.setOnClickListener(this);
		btPeople.setOnClickListener(this);
		btClock.setOnClickListener(this);
		btWeak.setOnClickListener(this);
		viewPager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
		viewPager.addOnPageChangeListener(new OnPageChangeListener() {
			private int currentPage;
			@Override
			public void onPageSelected(int page) {
				currentPage = page;
			}
			
			@Override
			public void onPageScrolled(int page, float arg1, int arg2) {
				int width = btMsg.getWidth();
				//设置白色下划线的起始偏移量。在我手机上调试感觉偏右了一点，所以再-2
				int offset = (width - ivUnderLine.getWidth())/2-2;
				LayoutParams lp = (LayoutParams) ivUnderLine.getLayoutParams();
				if(currentPage == page){
					lp.leftMargin = (int) (currentPage*width+offset+arg1*width);
				}else if(currentPage - page == 1){
					lp.leftMargin = (int) (currentPage*width+offset-(1-arg1)*width);
				}
				ivUnderLine.setLayoutParams(lp);
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});
	}
	
	class MyPagerAdapter extends FragmentPagerAdapter{

		public MyPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public int getCount() {
			return mFragments.size();
		}

		@Override
		public Fragment getItem(int position) {
			return mFragments.get(position);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.msg:
			viewPager.setCurrentItem(0);
			break;
		case R.id.people:
			viewPager.setCurrentItem(1);
			break;
		case R.id.clock:
			viewPager.setCurrentItem(2);
			break;
		case R.id.weak:
			viewPager.setCurrentItem(3);
			break;
		default:
			break;
		}
	}
}
