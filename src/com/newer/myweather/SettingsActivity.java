package com.newer.myweather;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class SettingsActivity extends Activity {

	private SettingsFragment fragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		if (null == savedInstanceState) {
			fragment = new SettingsFragment();
		}

		fragment = new SettingsFragment();
		getFragmentManager().beginTransaction()
				.add(android.R.id.content, fragment).commit();

	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		
		fragment.lp_temp.setSummary(fragment.lp_temp.getEntry());
		fragment.lp_windspace.setSummary(fragment.lp_windspace.getEntry());
		fragment.lp_rainfall.setSummary(fragment.lp_rainfall.getEntry());
		fragment.lp_update.setSummary(fragment.lp_update.getEntry());
	}

	public static class SettingsFragment extends PreferenceFragment implements OnPreferenceChangeListener, OnPreferenceClickListener, OnItemSelectedListener {

		private static final String TAG = "SettingsFragment";
		ListPreference lp_temp;
		ListPreference lp_windspace;
		ListPreference lp_rainfall;
		ListPreference lp_update;
		CheckBoxPreference cp_notification;
		CheckBoxPreference cp_language;
		Preference preference;
		
		private Spinner spinner1;
		private ArrayList<String> data1 = new ArrayList<String>();
		private ArrayAdapter<String> adapter1;
		
		private Spinner spinner2;
		private ArrayList<String> data2 = new ArrayList<String>();
		private ArrayAdapter<String> adapter2;
		
		private Spinner spinner3;
		private ArrayList<String> data3 = new ArrayList<String>();
		private ArrayAdapter<String> adapter3;
		
		private Spinner spinner4;
		private ArrayList<String> data4 = new ArrayList<String>();
		private ArrayAdapter<String> adapter4;
		
		private Spinner spinner5;
		private ArrayList<String> data5 = new ArrayList<String>();
		private ArrayAdapter<String> adapter5;
		
		@Override
		public void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			
			//加载资源
			addPreferencesFromResource(R.xml.settings);
			
			lp_temp = (ListPreference) findPreference("temp");
			lp_temp.setOnPreferenceChangeListener(this);
			
			lp_windspace = (ListPreference) findPreference("windSpeed");
			lp_windspace.setOnPreferenceChangeListener(this);
			
			lp_rainfall = (ListPreference) findPreference("rainfall_units");
			lp_rainfall.setOnPreferenceChangeListener(this);
			
			lp_update = (ListPreference) findPreference("update");
			lp_update.setOnPreferenceChangeListener(this);
			
			cp_notification = (CheckBoxPreference) findPreference("notification");
			cp_notification.setOnPreferenceChangeListener(this);
			
			cp_language = (CheckBoxPreference) findPreference("language");
			cp_language.setOnPreferenceChangeListener(this);
			
			preference = findPreference("cwn");
			preference.setOnPreferenceClickListener(this);
			
		}

		@Override
		public boolean onPreferenceChange(Preference preference, Object newValue) {
			// TODO Auto-generated method stub
			if (preference instanceof ListPreference) {
				ListPreference lp = (ListPreference) preference;
				 CharSequence [] ents = lp.getEntries();
				 int index = lp.findIndexOfValue((String)newValue);
				 lp.setSummary(ents[index]);
				 
				 return true;
			}else if(preference instanceof CheckBoxPreference){
				CheckBoxPreference cp = (CheckBoxPreference) preference;
				boolean ents = cp.getDisableDependentsState();
				cp.setChecked(ents);
				
				return true;
			}
			
			return false;
		}

		@Override
		public boolean onPreferenceClick(Preference preference) {
			// TODO Auto-generated method stub
			
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setTitle("Notication Configuration");
			View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_xxx, null);
			builder.setView(view);
			
			spinner1 = (Spinner) view.findViewById(R.id.sp_location);
			spinner1.setOnItemSelectedListener(this);
			
			data1.add("Current Location");
			data1.add("Changsha,China");
			adapter1 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, data1);
			adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinner1.setAdapter(adapter1);
			
			spinner2 = (Spinner) view.findViewById(R.id.sp_iconSet);
			spinner2.setOnItemSelectedListener(this);
			data2.add("Clean");data2.add("Climacons Dark");data2.add("Climacons Light");
			data2.add("DotVoid");data2.add("Fluffy");data2.add("Line");
			data2.add("Picons");data2.add("Realistic");data2.add("Sticky");
			data2.add("Symbolicons");data2.add("Symbolicons New");data2.add("Tab");
			data2.add("Tempus");data2.add("Tick");data2.add("Yow");
			adapter2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, data2);
			adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinner2.setAdapter(adapter2);
			
			spinner3 = (Spinner) view.findViewById(R.id.sp_status);
			spinner3.setOnItemSelectedListener(this);
			data3.add("Icon");data3.add("Temp");data3.add("Logo");
			adapter3 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, data3);
			adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinner3.setAdapter(adapter3);
			
			spinner4 = (Spinner) view.findViewById(R.id.sp_text);
			spinner4.setOnItemSelectedListener(this);
			data4.add("Black");data4.add("Blue");data4.add("White");
			adapter4 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, data4);
			adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinner4.setAdapter(adapter4);
			
			spinner5 = (Spinner) view.findViewById(R.id.sp_priority);
			spinner5.setOnItemSelectedListener(this);
			data5.add("Maximum Priority");data5.add("High Priority");data5.add("Default Priority");
			data5.add("Low Priority");data5.add("Minimum Priority");
			
			adapter5 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, data5);
			adapter5.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			spinner5.setAdapter(adapter5);
			
			builder.setPositiveButton("Ok", new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					
				}
			});
			builder.setNegativeButton("Cancel", null);
			builder.create().show();
			return false;
		}

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			// TODO Auto-generated method stub
			
		}
		
	}
}
