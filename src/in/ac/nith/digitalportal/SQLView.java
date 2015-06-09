package in.ac.nith.digitalportal;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

public class SQLView extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.notifdb);
		TextView tv=(TextView)findViewById(R.id.lblMessage);
		HotOrNot info = new HotOrNot(this);
		info.open();
		String data[] = info.getData();
		info.close();
		for(int i=0;i<data.length;i++)
		{
			data[i]+="\n";
			SpannableStringBuilder ssb=new SpannableStringBuilder(data[i]);
			if(i%2==0) ssb.setSpan(new ForegroundColorSpan(Color.rgb(209,209,209)), 0, data[i].length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			else ssb.setSpan(new ForegroundColorSpan(Color.rgb(30,146,182)), 0, data[i].length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			tv.append(ssb);
		}
	}
	

}
