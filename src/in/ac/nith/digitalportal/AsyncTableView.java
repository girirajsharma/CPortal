package in.ac.nith.digitalportal;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
 
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import in.ac.nith.digitalportal.R;
 
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
 
public class AsyncTableView extends ListActivity implements Serializable {
	
	
	String timeBlocks[]={"8:30 to 9:25","9:25 to 10:20","10:20 to 11:15","11:15 to 12:10","12:10 to 1:05","1:05 to 2:00","2:00 to 2:55","2:55 to 3:50","3:50 to 4:45"};
	
    ConnectionDetector cd;
    ProgressDialog pd;
     
    // JSON Node names
    private static final String TAG_TABLE = "table";
    private static final String TAG_SID = "sid";
    private static final String TAG_TID = "tid";
    private static final String TAG_ID = "id";
    private static final String TAG_NAME = "name";
    private static final String TAG_CODE = "code";
    private static final String TAG_EMAIL = "email";
    private static final String TAG_PHONE_MOBILE = "phone";
    private static final String TAG_BLOCK = "timeBlock";
    String teacher_name=null,subject_name=null,subject_code=null,teacher_phone=null,teacher_email=null;
    int blockIndex=-1;
    int urlError=0,fileError=0,internetError=0;

   
    //ProgressDialog Messages
    HashMap<Integer,String> messages=new HashMap<Integer,String>();
    
    // contacts JSONArray
    JSONArray table = null;
    JSONArray teacher = null;
    JSONArray subject = null;
    
    AlertDialogManager alert = new AlertDialogManager();
    ArrayList<HashMap<String, String>> contactList;
    JSONObject json1,json2,json3;
    ObjectOutput out;
    ObjectInputStream in;
    File cacheFile;
    String jsonFileName,jsonTFileName,jsonSFileName;
    String sem,branch;int day,group;
    String url,url1,url2;
    int lowerLimit,upperLimit;
    JSONParser jParser;
 
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_listview);
         
        Intent myIntent= getIntent(); 
    	sem=myIntent.getStringExtra("semester");
    	branch = myIntent.getStringExtra("branch");
    	day = Integer.parseInt(myIntent.getStringExtra("day"));
    	group = Integer.parseInt(myIntent.getStringExtra("group"));
    	
    	jsonFileName=branch+sem+"_table.srl";
    	jsonTFileName=branch+"_teacher__table.srl";
    	jsonSFileName=branch+"_subject_table.srl";
    	
    	lowerLimit=45*(group-1)+(day-2)*9;upperLimit=lowerLimit+8;

        // url to make request

        url = "http://glug.nith.ac.in:3000/b"+branch+sem+"s.json";
        url1 ="http://glug.nith.ac.in:3000/"+branch+"teachers.json";
        url2 ="http://glug.nith.ac.in:3000/"+branch+"subjects.json";
        
        //ProgressDialog Messages
        messages.put(1,branch.toUpperCase()+" Sem("+sem+")   TimeTable");
        messages.put(2,branch.toUpperCase()+" Sem("+sem+")   Teachers");
        messages.put(3,branch.toUpperCase()+" Sem("+sem+")   Subjects");
        messages.put(4,"Parsing");
        messages.put(5,branch.toUpperCase()+" Sem("+sem+")   TimeTable");
        messages.put(6,branch.toUpperCase()+" Sem("+sem+")   Teachers");
        messages.put(7,branch.toUpperCase()+" Sem("+sem+")   Subjects");
        
        // Hashmap for ListView
        contactList = new ArrayList<HashMap<String, String>>();
 
        // Creating JSON Parser instance
        jParser = new JSONParser();
        cd = new ConnectionDetector(getApplicationContext());
        pd=ProgressDialog.show(AsyncTableView.this, "Loading",branch.toUpperCase()+" Sem("+sem+")   TimeTable",true,false,null);
        new MyTask().execute();
   }        
 //----------------------------------------------------------------------------------------------------------------------------------
        
        class MyTask extends AsyncTask<Void,Integer,Void>
        {

			@Override
			protected Void doInBackground(Void... arg0) {
				if(new File(new File(getCacheDir(),""),"/table/"+jsonFileName).exists())
		        {
		        	
		        		try{
		        		in = new ObjectInputStream(new FileInputStream(new File(new File(getCacheDir(),"")+"/table/"+jsonFileName)));
		        		String jsonString1 = (String) in.readObject();
		        		json1 = new JSONObject(jsonString1);
		        		in.close();
		        		}
		        		catch(Exception e)
		        		{
		        			fileError=1;
		        			return null;
		        		}
		        }
		        else if (cd.isConnectingToInternet())
		        {
		        	publishProgress(1);
		        		try 
		        		{
		        			json1 = jParser.getJSONFromUrl(url);
		        		    String jsonString1=json1.toString();
		            		
		            		cacheFile=new File(getCacheDir()+"/table/");
		        			cacheFile.mkdirs();
		        			out = new ObjectOutputStream(new FileOutputStream(new File(getCacheDir(),"")+"/table/"+jsonFileName));
		        			out.writeObject( jsonString1 );
		        	        out.close();
		        	        
		        		}
		        		catch (Exception e1) 
		        		{
		        			urlError=1;
		        			return null;
		        		}
		        }
		        else
		    	{
		        	internetError=1;
        			return null;
		    	}
//-----------------------------------------------------------------------------------------------------------------
				if(new File(new File(getCacheDir(),""),"/teachers/"+jsonTFileName).exists())
                {
                		try{
                		in = new ObjectInputStream(new FileInputStream(new File(new File(getCacheDir(),"")+"/teachers/"+jsonTFileName)));
                		String jsonString2 = (String) in.readObject();
                		json2 = new JSONObject(jsonString2);
                		in.close();
                		}
                		catch(Exception e)
                		{
                			fileError=1;
		        			return null;
                		}
                }
                else if (cd.isConnectingToInternet())
                {
                	publishProgress(2);
                		try 
                		{
                			json2 = jParser.getJSONFromUrl(url1);
                    		String jsonString2=json2.toString();
                    		
                			cacheFile=new File(getCacheDir()+"/teachers/");
                			cacheFile.mkdirs();
                			out = new ObjectOutputStream(new FileOutputStream(new File(getCacheDir(),"")+"/teachers/"+jsonTFileName));
                			out.writeObject( jsonString2 );
                	        out.close();
                		}
                		catch (Exception e1) 
                		{
                			urlError=1;
                			return null;
                		}
                }
                else
                {
                	internetError=1;
        			return null;
            	} 
//------------------------------------------------------------------------------------------------------------------
				if(new File(new File(getCacheDir(),""),"/subjects/"+jsonSFileName).exists())
                {
                    		try{
                    		in = new ObjectInputStream(new FileInputStream(new File(new File(getCacheDir(),"")+"/subjects/"+jsonSFileName)));
                    		String jsonString3 = (String) in.readObject();
                    		json3 = new JSONObject(jsonString3);
                    		in.close();
                    		}
                    		catch(Exception e)
                    		{                
                    			fileError=1;
                    			return null;
                    		}
                    }
                    else
                    if (cd.isConnectingToInternet())
                    {
                    	publishProgress(3);
                    		try 
                    		{              
                    			json3 = jParser.getJSONFromUrl(url2);
                        		String jsonString3=json3.toString();
                        		
                    			cacheFile=new File(getCacheDir()+"/subjects/");
                    			cacheFile.mkdirs();
                    			out = new ObjectOutputStream(new FileOutputStream(new File(getCacheDir(),"")+"/subjects/"+jsonSFileName));
                    			out.writeObject( jsonString3 );
                    	        out.close();
                    		}
                    		catch (Exception e1) 
                    		{
                    			urlError=1;
                    			return null;
                    		}
                    }
                    else
                	{
                    	internetError=1;
            			return null;
                	}
//----------------------------------------------------------------------------------------------------------------------------------- 	
				return null;
			}
//-----------------------------do in background over---------------------------------------------------------------------------------
			@Override
			protected void onProgressUpdate(Integer... values) {
				super.onProgressUpdate(values);
				if(values[0]==4)
					pd.setTitle(messages.get(values[0]));
				else
					pd.setMessage(messages.get(values[0]));
			}
//-----------------------------------------------------------------------------------------------------------------------------------
			protected void onPostExecute(Void v)
			{
				if(fileError==1)
				{
					pd.dismiss();
					// Internet Connection is not present,Json couldnt be fetched
		        	alert.showAlertDialog(AsyncTableView.this,
		        						"FileStream Error",
		        						 jsonFileName+" couldnt be read.Please refresh.", false);
		        	// stop executing code by return
		        	return;
				}
				else
				if(urlError==1)
				{
					pd.dismiss();
					//URL doesnt exists,Json couldnt be fetched
		        	alert.showAlertDialog(AsyncTableView.this,
		        						"URLStream Error",
		        						 "Web service will be updated soon.", false);
		        	// stop executing code by return
		        	return;
						
				}
				else
				if(internetError==1)
				{
					pd.dismiss();
					// Internet Connection is not present
		        	alert.showAlertDialog(AsyncTableView.this,
		        						"Connection Error",
		        						"Please connect to a working internet connection", false);
		        	// stop executing code by return
		        	return;
					
				}
				
				publishProgress(4);
				publishProgress(5);
				  try {
					
			        	
			            // Getting Array of table
			            table = json1.getJSONArray(TAG_TABLE);
			             
			            // looping through All table

			            for(int i = lowerLimit; i <=upperLimit ; i++){
			            	++blockIndex;
			                JSONObject c = table.getJSONObject(i);
			                 
			                
			                //making values null;
			                subject_name = null;
			                teacher_name = null;
			                teacher_email = null;
			                teacher_phone = null;
			                
			                
			                // Storing each json item in variable
			                String sid = c.getString(TAG_SID);
			                String tid = c.getString(TAG_TID);
//-----------------------------------------------------------------------------------------------------------------               
			            
			                publishProgress(6);              	
			                teacher = json2.getJSONArray("teachers");
			                
			                	for(int j = 0; j < teacher.length() ; j++)
			                	{
			                        	c = teacher.getJSONObject(j);
			                        	if(c.getString(TAG_ID).equals(tid))
			                        	{
			                        		teacher_name=c.getString(TAG_NAME);
			                        		teacher_email=c.getString(TAG_EMAIL);
			                        		teacher_phone=c.getString(TAG_PHONE_MOBILE);
			                        		break;
			                        	}
			                	}
			                	
//------------------------------------------------------------------------------------------------------------------               
			                publishProgress(7);
			                subject = json3.getJSONArray("subjects");
			                for(int j = 0; j < subject.length() ; j++)
			                {
			                    	c = subject.getJSONObject(j);
			                    	if(c.getString(TAG_ID).equals(sid))
			                    	{
			                    		subject_name=c.getString(TAG_NAME);
			                    		subject_code=c.getString(TAG_CODE);
			                    		break;
			                    	}
			                }
			                	
			                 
			                // creating new HashMap
			                HashMap<String, String> map = new HashMap<String, String>();
			                 
			                // adding each child node to HashMap key => value
			                map.put(TAG_NAME, subject_name);
			                map.put(TAG_ID, teacher_name);
			                map.put(TAG_EMAIL, teacher_email);
			                map.put(TAG_PHONE_MOBILE, teacher_phone);
			                map.put(TAG_BLOCK,timeBlocks[blockIndex]);
			                // adding HashList to ArrayList
			                contactList.add(map);
			                
			            }
			        } catch (Exception e) {                              
			        	// Internet Connection is not present,Json couldnt be fetched
			        	alert.showAlertDialog(AsyncTableView.this,
			        						"JSON Parsing Error",
			        						"Web Service will be updated soon", false);
			        	// stop executing code by return
			        			
			            e.printStackTrace();
			        }
				    
				    pd.dismiss();
				     /**
			         * Updating parsed JSON data into ListView
			         * */
			        ListAdapter adapter = new SimpleAdapter(AsyncTableView.this, contactList,R.layout.list_item,
			                new String[] { TAG_NAME, TAG_ID,TAG_BLOCK }, new int[] {
			                        R.id.subject, R.id.name, R.id.time });
			 
			        setListAdapter(adapter);
			 
			        // selecting single ListView item
			        ListView lv = getListView();
			 
			        // Launching new screen on Selecting Single ListItem
			        lv.setOnItemClickListener(new OnItemClickListener() {
			 
			            @Override
			            public void onItemClick(AdapterView<?> parent, View view,
			                    int position, long id) {
			                // getting values from selected ListItem
			               String name = contactList.get(position).get(TAG_NAME);
			               String email = contactList.get(position).get(TAG_EMAIL);
			               String phone = contactList.get(position).get(TAG_PHONE_MOBILE);

			                 
			                // Starting new intent
			                Intent in = new Intent(getApplicationContext(), SingleMenuItemContacts.class);
			                in.putExtra(TAG_NAME, name);
			                in.putExtra(TAG_EMAIL, email);
			                in.putExtra(TAG_PHONE_MOBILE, phone);
			                startActivity(in);
			            }
			        });
			}
        	
        }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu);
		MenuInflater blowup = getMenuInflater();
		blowup.inflate(R.menu.options_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch(item.getItemId())
		{
			case R.id.aboutus :
				Intent even1=new Intent("in.ac.nith.digitalportal.About");
				startActivity(even1);
				break;
			
			case R.id.refresh :
				if(new File(new File(getCacheDir(),""),"/table/"+jsonFileName).exists())
				{
					File file1=new File(getCacheDir()+"/table/"+jsonFileName);
					file1.delete();
				}
				if(new File(new File(getCacheDir(),""),"/subjects/"+jsonSFileName).exists())
				{
					File file2=new File(getCacheDir()+"/subjects/"+jsonSFileName);
					file2.delete();
				}
				if(new File(new File(getCacheDir(),""),"/teachers/"+jsonTFileName).exists())
				{
					File file3=new File(getCacheDir()+"/teachers/"+jsonTFileName);
					file3.delete();
				}
				Intent table=new Intent("in.ac.nith.digitalportal.AsyncTableView"); 
				table.putExtra("group", group+"");
				table.putExtra("branch",branch);
				table.putExtra("day",day+"");
				table.putExtra("semester", sem);
				startActivity(table);
				
				break;
				
			case R.id.contacts :
				Intent even3=new Intent("in.ac.nith.digitalportal.Credits");
				startActivity(even3);
				break;
		}
		return true;
	}
 
 
 
}

