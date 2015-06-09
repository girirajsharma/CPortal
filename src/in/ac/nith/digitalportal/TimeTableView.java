package in.ac.nith.digitalportal;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import in.ac.nith.digitalportal.R;
 
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;
 
public class TimeTableView extends Activity implements OnClickListener{
	
	SharedPreferences preferences;
	SharedPreferences.Editor editor;
	AlertDialogManager alert = new AlertDialogManager();
	Spinner spinnerB,spinnerG,spinnerS;
	int sSelectedPosition,bSelectedPosition,gSelectedPosition;
	String semester,branch,group;
	String dayString="invalid";
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timetable);
        
        //sharedPreferences
        preferences=PreferenceManager.getDefaultSharedPreferences(this);
		editor=preferences.edit();
        
		//Retrieving values
		sSelectedPosition=preferences.getInt("sSelect", 5);
		System.out.println(sSelectedPosition+" sSelectedPosition_initial");
		bSelectedPosition=preferences.getInt("bSelect", 1);
		gSelectedPosition=preferences.getInt("gSelect", 2);
		
        // Spinner element
         spinnerS = (Spinner) findViewById(R.id.spinnerS);
         spinnerB = (Spinner) findViewById(R.id.spinnerB);
         spinnerG = (Spinner) findViewById(R.id.spinnerG);
        Button viewButton =(Button) findViewById(R.id.viewButton);
 
        //spinner & button click listener
        spinnerS.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View v, int pos, long row) {
                sSelectedPosition = pos;
                editor.putInt("sSelect",sSelectedPosition);
                System.out.println(sSelectedPosition+" sSelectedPosition_listener");
                editor.commit();
                semester=spinnerS.getSelectedItem().toString();
            }

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
        });
        spinnerB.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View v, int pos, long row) {
                bSelectedPosition = pos;
                editor.putInt("bSelect",bSelectedPosition);
                editor.commit();
                branch=spinnerB.getSelectedItem().toString();
                    
       }

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
        });
        spinnerG.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View v, int pos, long row) {
                gSelectedPosition = pos;
                editor.putInt("gSelect",gSelectedPosition);
                editor.commit();
                group=spinnerG.getSelectedItem().toString();
            }

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
        });
        viewButton.setOnClickListener((OnClickListener) this);
 
        // SpinnerS Drop down elements
        List<String> categories = new ArrayList<String>();
                    
        categories.add("1");categories.add("2");categories.add("3");categories.add("4");
        categories.add("5");categories.add("6");categories.add("7");categories.add("8");
        categories.add("9");categories.add("10");
        
        spinnerS.setSelection(sSelectedPosition);
        System.out.println(sSelectedPosition+" sSelectedPosition_final");
        // Creating adapter for spinnerS
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinnerS.setAdapter(dataAdapter);
        //--------------------------------------------------------------------------------------------
        
        // SpinnerS Drop down elements
        categories = new ArrayList<String>();

        categories.add("CSE");
        categories.add("CHEM");categories.add("CE");categories.add("ARCHI");
        categories.add("ECE");categories.add("EEE");categories.add("MECH");
        
        spinnerB.setSelection(bSelectedPosition);
        
        // Creating adapter for spinnerB
        dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinnerB.setAdapter(dataAdapter);
        
        //------------------------------------------------------------------------------------------
     
    // SpinnerS Drop down elements
    categories = new ArrayList<String>();
    
    categories.add("1");categories.add("2");categories.add("3");
    spinnerG.setSelection(gSelectedPosition);
    // Creating adapter for spinnerB
    dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
    // Drop down layout style - list view with radio button
    dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    // attaching data adapter to spinner
    spinnerG.setAdapter(dataAdapter);
    //----------------------------------------------------------------------------------------------
  }
 
	@Override
	public void onClick(View arg0) {
		Log.i("TAG","hellllo");
		// TODO Auto-generated method stub
		
		branch = spinnerB.getSelectedItem().toString().toLowerCase();
	    semester=spinnerS.getSelectedItem().toString();
	    group = spinnerG.getSelectedItem().toString();
	   
	    if((branch.equals("chem") && group.equals("3")) || (branch.equals("archi") && (group.equals("2") || group.equals("3"))) ){
	    	alert.showAlertDialog(TimeTableView.this,
					"Group Selection Error",
					"Please select a valid Group", false);
	    	return;
	    }
	    
	    if((!branch.equals("archi")) && (semester.equals("9")|| semester.equals("10"))){
	    	alert.showAlertDialog(TimeTableView.this,
					"Semester Selection Error",
					"Please select a valid Semester", false);
	    	return;
	    }
		
			 DatePicker datePicker = (DatePicker) findViewById(R.id.datePicker1);
			 int day = datePicker.getDayOfMonth();
			 int month = datePicker.getMonth();
			 int year = datePicker.getYear();
			 
			Calendar mycal = Calendar.getInstance();
			mycal.set(year, month, day);
			int dayOfWeek = mycal.get(mycal.DAY_OF_WEEK);
			
			switch(dayOfWeek) {
			case Calendar.MONDAY:
			    dayString = "2";
			    break;
			case Calendar.TUESDAY:
			    dayString = "3";
			    break;
			case Calendar.WEDNESDAY:
			    dayString = "4";
			    break;
			case Calendar.THURSDAY:
			    dayString = "5";
			    break;
			case Calendar.FRIDAY:
			    dayString = "6";
			    break;
			case Calendar.SATURDAY:
			    dayString = "invalid";
			    break;
			case Calendar.SUNDAY:
			    dayString = "invalid";
			    break;
			}

			
			if(dayString.equals("invalid"))
			{
				alert.showAlertDialog(TimeTableView.this,
						"Date Selection Error",
						"Please select a valid Date", false);
				return; //    Put here a valid statement
			}
			else
			{	
				Intent table=new Intent("in.ac.nith.digitalportal.AsyncTableView"); 
				table.putExtra("group",group);
				table.putExtra("branch",branch);
				table.putExtra("day",dayString);
				table.putExtra("semester", semester);
				startActivity(table);
			}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu);
		MenuInflater blowup = getMenuInflater();
		blowup.inflate(R.menu.activity_main, menu);
		return true;
		
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId())
		{
			case R.id.aboutus :
				Intent upg = new Intent("in.ac.nith.digitalportal.About");
				startActivity(upg);
				break;
				
			case R.id.contacts :
				Intent con=new Intent("in.ac.nith.digitalportal.Credits");
				startActivity(con);
				break;
		}
		return true;
	}
}