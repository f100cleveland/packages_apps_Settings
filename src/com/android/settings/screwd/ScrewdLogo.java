/*
 * Copyright (C) 2015 The Dirty Unicorns project
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

import android.content.ContentResolver;
import android.content.res.Resources;
import android.database.ContentObserver;
import android.os.Bundle;
import android.os.Handler;
import android.os.UserHandle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceCategory;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;

import com.android.settings.R;
import com.android.internal.logging.MetricsLogger;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.Utils;

import net.margaritov.preference.colorpicker.ColorPickerPreference;

public class ScrewdLogo extends SettingsPreferenceFragment implements OnPreferenceChangeListener {

    private static final String TAG = "ScrewdLogo";

    private static final String KEY_SCREWD_LOGO = "status_bar_screwd_logo";
    private static final String KEY_SCREWD_LOGO_COLOR = "status_bar_screwd_logo_color";

    private ListPreference mScrewdLogo;
    private ColorPickerPreference mScrewdLogoColor;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addPreferencesFromResource(R.xml.screwd_logo);

        PreferenceScreen prefSet = getPreferenceScreen();
		
		//Screw'd logo type
        mScrewdLogo = (ListPreference) prefSet.findPreference(KEY_SCREWD_LOGO);
        mScrewdLogo.setOnPreferenceChangeListener(this);
        int screwdLogovalue = Settings.System.getIntForUser(getContentResolver(),
                Settings.System.STATUS_BAR_SCREWD_LOGO, 0, UserHandle.USER_CURRENT);
        mScrewdLogo.setValue(String.valueOf(screwdLogovalue));
        mScrewdLogo.setSummary(mScrewdLogo.getEntry());
		
		// SCREWD logo color
        mScrewdLogoColor =
            (ColorPickerPreference) prefSet.findPreference(KEY_SCREWD_LOGO_COLOR);
        mScrewdLogoColor.setOnPreferenceChangeListener(this);
        int intColor = Settings.System.getInt(getContentResolver(),
                Settings.System.STATUS_BAR_SCREWD_LOGO_COLOR, 0xffffffff);
        String hexColor = String.format("#%08x", (0xffffffff & intColor));
            mScrewdLogoColor.setSummary(hexColor);
            mScrewdLogoColor.setNewPreviewColor(intColor);

    }

    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (preference == mScrewdLogo) {
            Settings.System.putInt(getContentResolver(), Settings.System.STATUS_BAR_SCREWD_LOGO,
                    Integer.valueOf((String) newValue));
            mScrewdLogo.setValue(String.valueOf(newValue));
            mScrewdLogo.setSummary(mScrewdLogo.getEntry());
	} else if (preference == mScrewdLogoColor) {
            String hex = ColorPickerPreference.convertToARGB(
                    Integer.valueOf(String.valueOf(newValue)));
            preference.setSummary(hex);
            int intHex = ColorPickerPreference.convertToColorInt(hex);
            Settings.System.putInt(getContentResolver(),
                    Settings.System.STATUS_BAR_SCREWD_LOGO_COLOR, intHex);
            return true;
        }
        return false;
    }

    @Override
    protected int getMetricsCategory() {
        return MetricsLogger.SCREWD_SETTINGS;
    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

}
