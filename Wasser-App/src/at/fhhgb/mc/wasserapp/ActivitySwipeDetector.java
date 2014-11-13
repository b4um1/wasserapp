package at.fhhgb.mc.wasserapp;
import android.app.Activity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ViewFlipper;

public class ActivitySwipeDetector implements OnTouchListener {

	static final String logTag = "ActivitySwipeDetector";
	private ViewFlipper vf;
	private Activity activity;
	static final int MIN_DISTANCE = 150;
	static final int MAX_UP = 100;
	private float downX, downY, upX, upY;

	public ActivitySwipeDetector(ViewFlipper _vf, Activity _activity){
		vf = _vf;
		activity = _activity;
	}

	public void onRightToLeftSwipe(){
		Log.i(logTag, "RightToLeftSwipe!");
		if(vf.getDisplayedChild() == 0){
			vf.setInAnimation(activity, R.anim.in_right);
			vf.setOutAnimation(activity, R.anim.out_left);
			vf.setDisplayedChild(1);
		}
	}

	public void onLeftToRightSwipe(){
		Log.i(logTag, "LeftToRightSwipe!");
		if(vf.getDisplayedChild() == 1){
			vf.setInAnimation(activity, R.anim.in_left);
			vf.setOutAnimation(activity, R.anim.out_right);
			vf.setDisplayedChild(0);
		}
	}

	public boolean onTouch(View v, MotionEvent event) {
		switch(event.getAction()){
		case MotionEvent.ACTION_DOWN: 
			downX = event.getX();
			downY = event.getY();
			return true;

		case MotionEvent.ACTION_UP: 
			upX = event.getX();
			upY = event.getY();

			float deltaX = downX - upX;
			float deltaY = downY - upY;

			// swipe horizontal?
			if(Math.abs(deltaX) > MIN_DISTANCE){
				// left or right
				if(Math.abs(deltaY) > MAX_UP) { Log.i(logTag, "Horizontal swipe was to hight with " + Math.abs(deltaY) + ". Must be below" + MAX_UP); return false;}
				if(deltaX < 0) { this.onLeftToRightSwipe(); return true; }
				if(deltaX > 0) { this.onRightToLeftSwipe(); return true; }

			}
			else {
				Log.i(logTag, "Swipe was only " + Math.abs(deltaX) + " long, need at least " + MIN_DISTANCE);
				return false; // We don't consume the event
			}
		}
		return false;
	}

}