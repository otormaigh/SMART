package ie.teamchile.smartapp.util;

import ie.teamchile.smartapp.R;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class ToastAlert{
	private TextView tv;
	private View layout;
	private boolean isWarning;

	public ToastAlert(Context context, String message, boolean isWarning) {
		this.isWarning = isWarning;
		Toast ImageToast = new Toast(context);
	    LinearLayout toastLayout = new LinearLayout(context);
	    toastLayout.setOrientation(LinearLayout.HORIZONTAL);
	    ImageView image = new ImageView(context);
	    TextView tv = new TextView(context);
	    ProgressBar pb = new ProgressBar(context);
	    if(isWarning) {
	    	tv.setTextColor(Color.RED);
	    	toastLayout.setBackgroundColor(Color.WHITE);
	    }else {
	    	tv.setTextColor(Color.WHITE);
	    	toastLayout.setBackgroundColor(Color.BLUE);
	    }
	    tv.setText(message);
	    tv.setTypeface(null, Typeface.BOLD);
	    image.setImageResource(R.drawable.ic_launcher);
	    ImageToast.setGravity(Gravity.TOP, 0, 0);
	    toastLayout.addView(pb);
	    toastLayout.addView(tv);
	    toastLayout.addView(image);
	    ImageToast.setView(toastLayout);
	    ImageToast.setDuration(Toast.LENGTH_SHORT);
	    ImageToast.show();
	}
}
