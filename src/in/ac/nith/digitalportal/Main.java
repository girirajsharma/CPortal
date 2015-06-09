package in.ac.nith.digitalportal;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Main extends Activity implements OnClickListener{
	
	Button noti,event,leader,gossip,fun,query,fb,g,tw;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.start);
		
		View title = getWindow().findViewById(android.R.id.title);
		View titleBar = (View) title.getParent();
		titleBar.setBackgroundColor(Color.BLACK);
		
		noti=(Button)this.findViewById(R.id.notifications);
		noti.setOnClickListener(this);
		event=(Button)this.findViewById(R.id.events);
		event.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Intent notif;
		switch(v.getId())
		{
		case R.id.notifications:
			if(ServerUtilities.registeredOnServer==1)
				notif=new Intent("in.ac.nith.digitalportal.DemoActivity");
			else
				notif=new Intent("in.ac.nith.digitalportal.Notifications");
			startActivity(notif);
			break;
		case R.id.events:
			Intent even=new Intent("in.ac.nith.digitalportal.TimeTabeView");
			startActivity(even);
			break;
		}
	}
	
	public void onBackPressed() {
		   Log.i("HA", "Finishing");
		   Intent intent = new Intent(Intent.ACTION_MAIN);
		   intent.addCategory(Intent.CATEGORY_HOME);
		   intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		   startActivity(intent);
		   finish();
		   System.exit(0);
		 }
}
