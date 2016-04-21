package per.yrj.ui;


import per.yrj.clock.R;
import per.yrj.model.Clock;
import per.yrj.utils.Utils;
import per.yrj.utils.WorldValues;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;

public class SetClockActivity extends Activity implements OnTimeSetListener {
	private Button btTimePicker;
	private Button btRepeat;
	private Button btOk;
	private Button btCancel;
	private EditText edClockTitle;
	private Clock clock;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.set_clock);
		super.onCreate(savedInstanceState);
		btTimePicker = (Button) findViewById(R.id.timePicker);
		btRepeat = (Button) findViewById(R.id.repeat);
		btOk = (Button) findViewById(R.id.ok);
		btCancel = (Button) findViewById(R.id.cancel);
		edClockTitle = (EditText) findViewById(R.id.clockName);
		// 获取启动页传过来的信息
		Intent intent = getIntent();
		clock = intent.getParcelableExtra(ClockFragment.EXTRA_CLOCK);
		if (clock == null)
			clock = new Clock();
		else {
			// 设置闹钟名称
			edClockTitle.setText(clock.getClockTitle());
		}
		btTimePicker.setText(clock.getTime());
		// 时间选择按钮的时间监听
		btTimePicker.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new TimePickerDialog(SetClockActivity.this,
						SetClockActivity.this, clock.getHour(), clock
								.getMinute(), true).show();
			}
		});

		// 重复频率选择按钮的监听
		btRepeat.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				createDialog();
			}
		});

		// 确定按钮时间监听
		btOk.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				// 设置闹钟标题，如果没有则设为闹钟
				clock.setClockTitle(edClockTitle.getText().length() == 0 ? "闹钟"
						: edClockTitle.getText().toString());
				Utils.setOnClock(SetClockActivity.this, clock);
				intent.putExtra(ClockFragment.EXTRA_CLOCK, clock);
				setResult(2, intent);
				finish();
			}
		});

		// 取消按钮时间监听
		btCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	/**
	 * 创建一个星期选择对话框
	 */
	private void createDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(
				SetClockActivity.this);
		builder.setMultiChoiceItems(WorldValues.WEAK_REPEAT, clock.getRepeatRate(),
				new DialogInterface.OnMultiChoiceClickListener() {
			boolean[] repeat = new boolean[7];

					@Override
					public void onClick(DialogInterface dialog, int which,
							boolean isChecked) {
							repeat[which] = isChecked;
						clock.setRepeatRate(repeat);
					}
				});
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String repeatString = "";
				boolean[] repeat = clock.getRepeatRate();
				for (int i = 0; i < repeat.length; i++) {
					if (repeat[i]) {
						repeatString += (WorldValues.WEAK_REPEAT[i] + ",");
					}
				}
				if (repeatString == "")
					repeatString = "仅一次";
				else
					repeatString = repeatString.substring(0,
							repeatString.length() - 1);
				btRepeat.setText(repeatString);
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		builder.show();
	}

	@Override
	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		// 设置按钮显示时间
		clock.setHour(hourOfDay);
		clock.setMinute(minute);
		btTimePicker.setText(clock.getTime());

	}
}
