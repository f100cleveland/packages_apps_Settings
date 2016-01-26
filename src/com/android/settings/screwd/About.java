/*
 * Copyright (C) 2015 Screw'd AOSP
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.settings.screwd;

import com.android.internal.logging.MetricsLogger;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemProperties;
import android.preference.Preference;
import android.preference.PreferenceGroup;
import android.preference.PreferenceScreen;

import com.android.settings.SettingsPreferenceFragment;
import com.android.settings.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

public class About extends SettingsPreferenceFragment {

    public static final String TAG = "About";

    private static final String KEY_SCREWD_SHARE = "share";
	private static final String KEY_DEVICE_MAINTAINER = "device_maintainer";

    Preference mSourceUrl;
    Preference mGoogleUrl;
	Preference mSiteUrl;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.about_screwd);
		
		setMaintainerSummary(KEY_DEVICE_MAINTAINER, "ro.screwd.maintainer");

        mSourceUrl = findPreference("screwd_source");
        mGoogleUrl = findPreference("screwd_google_plus");
		mSiteUrl = findPreference("screwd_website");

    }

    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen, Preference preference) {
        if (preference == mSourceUrl) {
            launchUrl("https://github.com/ScrewdAOSP");
		} else if (preference == mSiteUrl) {
			launchUrl("http://screwdandroid.com");
        } else if (preference == mGoogleUrl) {
            launchUrl("https://plus.google.com/u/0/communities/109777359391119326629");
        } else if (preference.getKey().equals(KEY_SCREWD_SHARE)) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, String.format(
                getActivity().getString(R.string.share_message), Build.MODEL));
        startActivity(Intent.createChooser(intent, getActivity().getString(R.string.share_chooser_title)));
        }
        return super.onPreferenceTreeClick(preferenceScreen, preference);
    }

    private void launchUrl(String url) {
        Uri uriUrl = Uri.parse(url);
        Intent donate = new Intent(Intent.ACTION_VIEW, uriUrl);
        getActivity().startActivity(donate);
    }
	
	private void setMaintainerSummary(String preference, String property) {
        try {
            String maintainers = SystemProperties.get(property,
                    getResources().getString(R.string.maintainer_default));
            findPreference(preference).setTitle(maintainers);
        } catch (RuntimeException e) {
            // No recovery
        }
    }

    private void setExplicitValueSummary(String preference, String value) {
        try {
            findPreference(preference).setSummary(value);
        } catch (RuntimeException e) {
            // No recovery
        }
    }
	
	@Override
    protected int getMetricsCategory() {
        return MetricsLogger.SCREWD_SETTINGS;
    }
}
