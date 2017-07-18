package rp.edu.sg.c302.addressbook;

import java.util.ArrayList;

//import com.example.p11authentication.R;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import rp.edu.sg.c302.addressbook.R;

public class PersonAdapter extends ArrayAdapter<Person> {
	
	Context context;
	int layoutResourceId;   
	ArrayList<Person> personList = null;
	

	public PersonAdapter(Context context, int resource, ArrayList<Person> objects) {
		super(context, resource, objects);
		this.context = context;
		this.layoutResourceId = resource;
		this.personList = objects;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		PersonHolder holder = null;

		if(row == null)
		{
			LayoutInflater inflater = ((Activity)context).getLayoutInflater();
			row = inflater.inflate(layoutResourceId, parent, false);

			holder = new PersonHolder();
			holder.fullName = (TextView)row.findViewById(R.id.txtFullName);
			holder.mobile = (TextView)row.findViewById(R.id.txtMobile);

			row.setTag(holder);
		}
		else
		{
			holder = (PersonHolder)row.getTag();
		}

		Person person = personList.get(position);
		holder.fullName.setText(person.getFirstName() + " " + person.getLastName());
		holder.mobile.setText(person.getMobileNumber());
		return row;
	}

	static class PersonHolder
	{
		TextView fullName;
		TextView mobile;
	}


		
	
}
