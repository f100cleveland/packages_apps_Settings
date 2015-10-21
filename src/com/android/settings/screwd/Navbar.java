/*

 * Copyright (C) 2014 Screw'd Android
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.settings.screwd;

import android.provider.SearchIndexableResource;
import com.android.settings.search.BaseSearchIndexProvider;
import com.android.settings.search.Indexable;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.UserHandle;
import android.os.SystemProperties;
import android.provider.Settings;
import android.preference.ListPreference;
import android.preference.CheckBoxPreference;
import android.preference.Preference;
import android.preference.PreferenceScreen;
import android.preference.PreferenceCategory;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.SwitchPreference;

import android.text.TextUtils;
import android.widget.Toast;


import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;

import java.util.ArrayList;
import java.util.List;

import com.android.internal.logging.MetricsLogger;

public class Navbar extends SettingsPreferenceFragment implements
        Preference.OnPreferenceChangeListener, Indexable {
	
	private static final String KILL_APP_LONGPRESS_BACK = "kill_app_longpress_back";

    private SwitchPreference mKillAppLongPressBack;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.screwd_navbar_settings);
		
		Activity activity = getActivity();
		
		ContentResolver resolver = getActivity().getContentResolver();
		PreferenceScreen prefSet = getPreferenceScreen();
			
		 // kill-app long press back
        mKillAppLongPressBack = (SwitchPreference) findPreference(KILL_APP_LONGPRESS_BACK);
        mKillAppLongPressBack.setOnPreferenceChangeListener(this);
        int killAppLongPressBack = Settings.Secure.getInt(getContentResolver(),
                KILL_APP_LONGPRESS_BACK, 0);
        mKillAppLongPressBack.setChecked(killAppLongPressBack != 0);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {
		if (preference == mKillAppLongPressBack) {
            boolean value = (Boolean) newValue;
            Settings.Secure.putInt(getContentResolver(), KILL_APP_LONGPRESS_BACK,
                    value ? 1 : 0);
        }
        return false;
    }

	
    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
		

        return false;
    }
	
	public static final Indexable.SearchIndexProvider SEARCH_INDEX_DATA_PROVIDER =
        new BaseSearchIndexProvider() {
        @Override
        public List<SearchIndexableResource> getXmlResourcesToIndex(Context context,
                                                                    boolean enabled) {
            ArrayList<SearchIndexableResource> result =
                new ArrayList<SearchIndexableResource>();

            SearchIndexableResource sir = new SearchIndexableResource(context);
            sir.xmlResId = R.xml.screwd_navbar_settings;
            result.add(sir);

            return result;
        }

        @Override
        public List<String> getNonIndexableKeys(Context context) {
            ArrayList<String> result = new ArrayList<String>();
            return result;
        }
    };
	
	@Override
	protected int getMetricsCategory() {
        return MetricsLogger.APPLICATION;
    }

}
