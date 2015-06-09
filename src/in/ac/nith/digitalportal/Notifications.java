package in.ac.nith.digitalportal;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import static in.ac.nith.digitalportal.CommonUtilities.*;

public class Notifications extends Activity{

		
	Button register;
	EditText name,roll,email,branch,year,group;
	Button all;
	
	ConnectionDetector cd;
	AlertDialogManager alert = new AlertDialogManager();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.notifications);
		register=(Button)this.findViewById(R.id.register);
		name=(EditText)this.findViewById(R.id.name);
		roll=(EditText)this.findViewById(R.id.roll);
		email=(EditText)this.findViewById(R.id.email);
		branch=(EditText)this.findViewById(R.id.branch);
		year=(EditText)this.findViewById(R.id.year);
		group=(EditText)this.findViewById(R.id.group);
		
		cd = new ConnectionDetector(getApplicationContext());

		// Check if Internet present
		if (!cd.isConnectingToInternet()) {
			// Internet Connection is not present
			alert.showAlertDialog(Notifications.this,
					"Internet Connection Error",
					"Please connect to working Internet connection", false);
			// stop executing code by return
			return;
		}
		if (SERVER_URL == null || SENDER_ID == null || SERVER_URL.length() == 0
				|| SENDER_ID.length() == 0) {
			// GCM sernder id / server url is missing
			alert.showAlertDialog(Notifications.this, "Configuration Error!",
					"Please set your Server URL and GCM Sender ID", false);
			// stop executing code by return
			 return;
		}
			register.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// Read EditText dat
				String _name = name.getText().toString();
				String _email = email.getText().toString();
				String _roll = roll.getText().toString();
				String _branch = branch.getText().toString();
				String _year = year.getText().toString();
				String _group = group.getText().toString();
				
				// Check if user filled the form
				if(_name.trim().length() > 0){
					// Launch Main Activity
					Intent i = new Intent(getApplicationContext(), DemoActivity.class);
					
					// Registering user on our server					
					// Sending registraiton details to MainActivity
					i.putExtra("name", _name);
					i.putExtra("roll", _roll);
					i.putExtra("email", _email);
					i.putExtra("branch", _branch);
					i.putExtra("year", _year);
					i.putExtra("group", _group);
					startActivity(i);
					finish();
				}else{
					// user doen't filled that data
					// ask him to fill the form
					alert.showAlertDialog(Notifications.this, "Registration Error!", "Please enter your details", false);
				}
			}
		});
			
		all.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i=new Intent("com.example.exe.SQLVIEW");
				startActivity(i);
			}
		});
			
	}
	
	

}
