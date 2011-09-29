package au.id.teda.volumeusage.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import au.id.teda.volumeusage.R;

public class SplashScreenActivity extends Activity {
	
	// TODO: Improve splash screen look
	// TODO: Do I really need a splash screen??
	
	private Thread splashThread; // The thread to process splash screen events
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.splash); // Splash screen view
		
		final SplashScreenActivity splashScreen = this;
		
		splashThread =  new Thread(){ // The thread to wait for splash screen events
			@Override
			public void run(){
				try {
					synchronized(this){
						wait(1000); // Wait given period of time or exit on touch
					}
				} catch(InterruptedException ex){
					
				}
				finish();
	                
				// Run next activity
				Intent intent = new Intent();
				intent.setClass(splashScreen, MainActivity.class);
				startActivity(intent);
				stop(); // Stop the thread
			}
		};
		splashThread.start();        
	}
	
	// Processes splash screen touch events
    @Override
    public boolean onTouchEvent(MotionEvent evt)
    {
        if(evt.getAction() == MotionEvent.ACTION_DOWN)
        {
            synchronized(splashThread){
                splashThread.notifyAll();
            }
        }
        return true;
    }
}
