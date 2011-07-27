package de.c3t;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.Preference.OnPreferenceChangeListener;

public class Settings extends PreferenceActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.layout.settings);
		final Settings parent = this;
		findPreference("C2DM").setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

			public boolean onPreferenceChange(Preference preference, Object newValue) {
				boolean enableC2DM = (Boolean) newValue;
				if (enableC2DM) {
					C2DM.registerC2DM(parent);
				} else {
					C2DM.unRegisterC2DM(parent);
				}
				return true;
			}
		});
	}
}
