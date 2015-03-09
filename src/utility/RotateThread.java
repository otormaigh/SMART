package utility;

import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

//private class to rotate the image on a separate thread
	public class RotateThread implements Runnable {
		private ImageView image;
		private static final float ROTATE_FROM = 0.0f;
	    private static final float ROTATE_TO = -10.0f * 180.0f;
		
		public RotateThread(ImageView image) {
			this.image = image;
		}

		@Override
		public void run() {
			RotateAnimation r; // = new RotateAnimation(ROTATE_FROM, ROTATE_TO);
			r = new RotateAnimation(ROTATE_FROM, ROTATE_TO, 
					Animation.RELATIVE_TO_SELF, 0.5f, 
					Animation.RELATIVE_TO_SELF, 0.5f);
			r.setDuration((long) 2*500);
			r.setRepeatCount(0);
			try {
				Thread.sleep(500);
				this.image.startAnimation(r);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
	}	