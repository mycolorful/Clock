package per.yrj.receiver;

import per.yrj.ui.ClockActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class ClockReceiver extends BroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		Intent i = new Intent(context, ClockActivity.class);
		i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(i);
	}

}
