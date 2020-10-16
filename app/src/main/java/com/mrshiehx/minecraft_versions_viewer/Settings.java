package com.mrshiehx.minecraft_versions_viewer;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.text.TextUtils;
import android.view.MenuItem;

import java.util.Locale;


public class Settings extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {

    public EditTextPreference input_stable_releases_versions_source;
    public EditTextPreference input_snapshot_preview_versions_source;
    public EditTextPreference input_beta_versions_source;
    public EditTextPreference input_alpha_versions_source;
    public ListPreference modify_theme;
    public ListPreference modify_language;
    public ListPreference launch_page;
    public ListPreference stable_releases_versions_source;
    public ListPreference snapshot_preview_versions_source;
    public ListPreference beta_versions_source;
    public ListPreference alpha_versions_source;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.initialization(this, R.string.activity_settings_name);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        addPreferencesFromResource(R.xml.activity_settings);
        input_stable_releases_versions_source = (EditTextPreference) getPreferenceScreen().findPreference("input_stable_releases_versions_source");
        input_snapshot_preview_versions_source = (EditTextPreference) getPreferenceScreen().findPreference("input_snapshot_preview_versions_source");
        input_beta_versions_source = (EditTextPreference) getPreferenceScreen().findPreference("input_beta_versions_source");
        input_alpha_versions_source = (EditTextPreference) getPreferenceScreen().findPreference("input_alpha_versions_source");
        modify_theme = (ListPreference) getPreferenceScreen().findPreference("modify_theme");
        modify_language = (ListPreference) getPreferenceScreen().findPreference("modify_language");
        launch_page = (ListPreference) getPreferenceScreen().findPreference("launch_page");
        stable_releases_versions_source = (ListPreference) getPreferenceScreen().findPreference("stable_releases_versions_source");
        snapshot_preview_versions_source = (ListPreference) getPreferenceScreen().findPreference("snapshot_preview_versions_source");
        beta_versions_source = (ListPreference) getPreferenceScreen().findPreference("beta_versions_source");
        alpha_versions_source = (ListPreference) getPreferenceScreen().findPreference("alpha_versions_source");
        initTextSummary1();
        initTextSummary2();
        initTextSummary3();
        initTextSummary4();
        initListSummary1();
        initListSummary2();
        initListSummary3();
        initListSummary4();
        initListSummary5();
        initListSummary6();
        initListSummary7();
        checkCustomVersionsSources();

        Locale locale = Locale.getDefault();
        System.out.println(locale.getLanguage());
        System.out.println(locale.getCountry());
        if (TextUtils.isEmpty(modify_language.getValue())) {
            if ((locale.getLanguage() + "_" + locale.getCountry()).equals("zh_CN")) {
                modify_language.setValue("zh_CN");
            } else if ((locale.getLanguage() + "_" + locale.getCountry()).equals("zh_TW")) {
                modify_language.setValue("zh_TW");
            } else {
                modify_language.setValue("en_US");
            }
        }
        //Toast.makeText(this, locale.getLanguage()+"_"+locale.getCountry()+"", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals("input_stable_releases_versions_source")) {
            initTextSummary1();
            checkCustomVersionsSources();
        }
        if (key.equals("input_snapshot_preview_versions_source")) {
            initTextSummary2();
            checkCustomVersionsSources();
        }
        if (key.equals("input_beta_versions_source")) {
            initTextSummary3();
            checkCustomVersionsSources();
        }
        if (key.equals("input_alpha_versions_source")) {
            initTextSummary4();
            checkCustomVersionsSources();
        }
        if (key.equals("modify_theme")) {
            initListSummary1();
            checkCustomVersionsSources();
        }
        if (key.equals("modify_language")) {
            initListSummary2();
            checkCustomVersionsSources();
        }
        if (key.equals("launch_page")) {
            initListSummary3();
            checkCustomVersionsSources();
        }
        if (key.equals("stable_releases_versions_source")) {
            initListSummary4();
            checkCustomVersionsSources();
        }
        if (key.equals("snapshot_preview_versions_source")) {
            initListSummary5();
            checkCustomVersionsSources();
        }
        if (key.equals("beta_versions_source")) {
            initListSummary6();
            checkCustomVersionsSources();
        }
        if (key.equals("alpha_versions_source")) {
            initListSummary7();
            checkCustomVersionsSources();
        }
    }

    public void initTextSummary1() {
        if (TextUtils.isEmpty(input_stable_releases_versions_source.getText())) {
            input_stable_releases_versions_source.setSummary(getResources().getString(R.string.preference_settings_edittexts_input_versions_source_hint));
        } else {
            input_stable_releases_versions_source.setSummary(input_stable_releases_versions_source.getText());
        }
    }

    public void initTextSummary2() {
        if (TextUtils.isEmpty(input_snapshot_preview_versions_source.getText())) {
            input_snapshot_preview_versions_source.setSummary(getResources().getString(R.string.preference_settings_edittexts_input_versions_source_hint));
        } else {
            input_snapshot_preview_versions_source.setSummary(input_snapshot_preview_versions_source.getText());
        }
    }


    public void initTextSummary3() {
        if (TextUtils.isEmpty(input_beta_versions_source.getText())) {
            input_beta_versions_source.setSummary(getResources().getString(R.string.preference_settings_edittexts_input_versions_source_hint));
        } else {
            input_beta_versions_source.setSummary(input_beta_versions_source.getText());
        }
    }


    public void initTextSummary4() {
        if (TextUtils.isEmpty(input_alpha_versions_source.getText())) {
            input_alpha_versions_source.setSummary(getResources().getString(R.string.preference_settings_edittexts_input_versions_source_hint));
        } else {
            input_alpha_versions_source.setSummary(input_alpha_versions_source.getText());
        }
    }


    public void initListSummary1() {
        if (TextUtils.isEmpty(modify_theme.getValue())) {
            //modify_theme.setSummary(getResources().getString(R.string.preference_settings_edittexts_input_versions_source_hint));
        } else {
            modify_theme.setSummary(modify_theme.getEntry());
        }
    }


    public void initListSummary2() {
        if (TextUtils.isEmpty(modify_language.getValue())) {
            //modify_language.setSummary(getResources().getString(R.string.preference_settings_edittexts_input_versions_source_hint));
        } else {
            modify_language.setSummary(modify_language.getEntry());
        }
    }


    public void initListSummary3() {
        if (TextUtils.isEmpty(launch_page.getValue())) {
            //modify_theme.setSummary(getResources().getString(R.string.preference_settings_edittexts_input_versions_source_hint));
        } else {
            launch_page.setSummary(launch_page.getEntry());
        }
    }


    public void initListSummary4() {
        if (TextUtils.isEmpty(stable_releases_versions_source.getValue())) {
            //modify_theme.setSummary(getResources().getString(R.string.preference_settings_edittexts_input_versions_source_hint));
        } else {
            stable_releases_versions_source.setSummary(stable_releases_versions_source.getEntry());
        }
        if (stable_releases_versions_source.getValue().equals("default")) {
            input_stable_releases_versions_source.setEnabled(false);
        } else {
            input_stable_releases_versions_source.setEnabled(true);
        }
    }


    public void initListSummary5() {
        if (TextUtils.isEmpty(snapshot_preview_versions_source.getValue())) {
            //modify_theme.setSummary(getResources().getString(R.string.preference_settings_edittexts_input_versions_source_hint));
        } else {
            snapshot_preview_versions_source.setSummary(snapshot_preview_versions_source.getEntry());
        }

        if (snapshot_preview_versions_source.getValue().equals("default")) {
            input_snapshot_preview_versions_source.setEnabled(false);
        } else {
            input_snapshot_preview_versions_source.setEnabled(true);
        }
    }


    public void initListSummary6() {
        if (TextUtils.isEmpty(beta_versions_source.getValue())) {
            //modify_theme.setSummary(getResources().getString(R.string.preference_settings_edittexts_input_versions_source_hint));
        } else {
            beta_versions_source.setSummary(beta_versions_source.getEntry());
        }
        if (beta_versions_source.getValue().equals("default")) {
            input_beta_versions_source.setEnabled(false);
        } else {
            input_beta_versions_source.setEnabled(true);
        }
    }


    public void initListSummary7() {
        if (TextUtils.isEmpty(alpha_versions_source.getValue())) {
            //modify_theme.setSummary(getResources().getString(R.string.preference_settings_edittexts_input_versions_source_hint));
        } else {
            alpha_versions_source.setSummary(alpha_versions_source.getEntry());
        }
        if (alpha_versions_source.getValue().equals("default")) {
            input_alpha_versions_source.setEnabled(false);
        } else {
            input_alpha_versions_source.setEnabled(true);
        }
    }

    public void checkCustomVersionsSources() {
        checkStableReleasesCustomVersionsSource();
        checkSnapshotPreviewCustomVersionsSource();
        checkBetaCustomVersionsSource();
        checkAlphaCustomVersionsSource();
    }

    public void checkStableReleasesCustomVersionsSource() {

        if (stable_releases_versions_source.getValue().equals("custom_from_internet")) {

            if (TextUtils.isEmpty(input_stable_releases_versions_source.getText()) == false) {
                if (Utils.isUrlAUrl(input_stable_releases_versions_source.getText()) == true) {
                    String fileNameOfInputStableReleasesVersionsSourceGetText = input_stable_releases_versions_source.getText().substring(input_stable_releases_versions_source.getText().lastIndexOf("/") + 1);
                    String fileExtensionOfInputStableReleasesVersionsSourceGetText = fileNameOfInputStableReleasesVersionsSourceGetText.substring(fileNameOfInputStableReleasesVersionsSourceGetText.lastIndexOf(".") + 1);
                    if (fileExtensionOfInputStableReleasesVersionsSourceGetText.equals("json") == true) {
                        Utils.downloadFileForSettings(this, input_stable_releases_versions_source.getText(), "test_stableReleases.json", Utils.getDataCachePath(this), VersionsType.STABLE_RELEASES);
                    } else {
                        Utils.showDialog(this, getResources().getString(R.string.dialog_versions_source_stable_releases_isnot_json_title), getResources().getString(R.string.dialog_versions_source_stable_releases_isnot_json_message));
                    }
                } else {
                    Utils.showDialog(this, getResources().getString(R.string.dialog_versions_source_stable_releases_isnot_url_title), getResources().getString(R.string.dialog_versions_source_stable_releases_isnot_url_message));
                }
            } else {
                Utils.showDialog(this, getResources().getString(R.string.dialog_versions_source_stable_releases_ismpty_title), getResources().getString(R.string.dialog_versions_source_stable_releases_isempty_message));
            }

        } else if (stable_releases_versions_source.getValue().equals("custom_from_local")) {
            if (TextUtils.isEmpty(input_stable_releases_versions_source.getText()) == false) {
                String stableReleasesFileName = input_stable_releases_versions_source.getText().substring(input_stable_releases_versions_source.getText().lastIndexOf("/") + 1);
                if (Utils.fileIsExists(this, input_stable_releases_versions_source.getText()) == true) {
                    if (Utils.getFileExtension(stableReleasesFileName).equals("json") == false) {
                        Utils.showDialog(this, getResources().getString(R.string.dialog_versions_source_stable_releases_isnot_json_title), getResources().getString(R.string.dialog_versions_source_stable_releases_isnot_json_message));
                    }
                } else {
                    Utils.showDialog(this, getResources().getString(R.string.dialog_versions_source_stable_releases_file_not_exist_title), getResources().getString(R.string.dialog_versions_source_stable_releases_file_not_exist_message));
                }
            } else {
                Utils.showDialog(this, getResources().getString(R.string.dialog_versions_source_stable_releases_ismpty_title), getResources().getString(R.string.dialog_versions_source_stable_releases_isempty_message));
            }
        }
    }


    public void checkSnapshotPreviewCustomVersionsSource() {

        if (snapshot_preview_versions_source.getValue().equals("custom_from_internet")) {

            if (TextUtils.isEmpty(input_snapshot_preview_versions_source.getText()) == false) {
                if (Utils.isUrlAUrl(input_snapshot_preview_versions_source.getText()) == true) {
                    String fileNameOfInputStableReleasesVersionsSourceGetText = input_snapshot_preview_versions_source.getText().substring(input_snapshot_preview_versions_source.getText().lastIndexOf("/") + 1);
                    String fileExtensionOfInputStableReleasesVersionsSourceGetText = fileNameOfInputStableReleasesVersionsSourceGetText.substring(fileNameOfInputStableReleasesVersionsSourceGetText.lastIndexOf(".") + 1);
                    if (fileExtensionOfInputStableReleasesVersionsSourceGetText.equals("json") == true) {
                        Utils.downloadFileForSettings(this, input_snapshot_preview_versions_source.getText(), "test_snapshotPreview.json", Utils.getDataCachePath(this), VersionsType.SNAPSHOT_PREVIEW);
                    } else {
                        Utils.showDialog(this, getResources().getString(R.string.dialog_versions_source_snapshot_preview_isnot_json_title), getResources().getString(R.string.dialog_versions_source_snapshot_preview_isnot_json_message));
                    }
                } else {
                    Utils.showDialog(this, getResources().getString(R.string.dialog_versions_source_snapshot_preview_isnot_url_title), getResources().getString(R.string.dialog_versions_source_snapshot_preview_isnot_url_message));
                }
            } else {
                Utils.showDialog(this, getResources().getString(R.string.dialog_versions_source_snapshot_preview_ismpty_title), getResources().getString(R.string.dialog_versions_source_snapshot_preview_isempty_message));
            }

        } else if (snapshot_preview_versions_source.getValue().equals("custom_from_local")) {
            if (TextUtils.isEmpty(input_snapshot_preview_versions_source.getText()) == false) {
                String stableReleasesFileName = input_snapshot_preview_versions_source.getText().substring(input_snapshot_preview_versions_source.getText().lastIndexOf("/") + 1);
                if (Utils.fileIsExists(this, input_snapshot_preview_versions_source.getText()) == true) {
                    if (Utils.getFileExtension(stableReleasesFileName).equals("json") == false) {
                        Utils.showDialog(this, getResources().getString(R.string.dialog_versions_source_snapshot_preview_isnot_json_title), getResources().getString(R.string.dialog_versions_source_snapshot_preview_isnot_json_message));
                    }
                } else {
                    Utils.showDialog(this, getResources().getString(R.string.dialog_versions_source_snapshot_preview_file_not_exist_title), getResources().getString(R.string.dialog_versions_source_snapshot_preview_file_not_exist_message));
                }
            } else {
                Utils.showDialog(this, getResources().getString(R.string.dialog_versions_source_snapshot_preview_ismpty_title), getResources().getString(R.string.dialog_versions_source_snapshot_preview_isempty_message));
            }
        }
    }


    public void checkBetaCustomVersionsSource() {

        if (beta_versions_source.getValue().equals("custom_from_internet")) {

            if (TextUtils.isEmpty(input_beta_versions_source.getText()) == false) {
                if (Utils.isUrlAUrl(input_beta_versions_source.getText()) == true) {
                    String fileNameOfInputStableReleasesVersionsSourceGetText = input_beta_versions_source.getText().substring(input_beta_versions_source.getText().lastIndexOf("/") + 1);
                    String fileExtensionOfInputStableReleasesVersionsSourceGetText = fileNameOfInputStableReleasesVersionsSourceGetText.substring(fileNameOfInputStableReleasesVersionsSourceGetText.lastIndexOf(".") + 1);
                    if (fileExtensionOfInputStableReleasesVersionsSourceGetText.equals("json") == true) {
                        Utils.downloadFileForSettings(this, input_beta_versions_source.getText(), "test_beta.json", Utils.getDataCachePath(this), VersionsType.BETA);
                    } else {
                        Utils.showDialog(this, getResources().getString(R.string.dialog_versions_source_beta_isnot_json_title), getResources().getString(R.string.dialog_versions_source_beta_isnot_json_message));
                    }
                } else {
                    Utils.showDialog(this, getResources().getString(R.string.dialog_versions_source_beta_isnot_url_title), getResources().getString(R.string.dialog_versions_source_beta_isnot_url_message));
                }
            } else {
                Utils.showDialog(this, getResources().getString(R.string.dialog_versions_source_beta_ismpty_title), getResources().getString(R.string.dialog_versions_source_beta_isempty_message));
            }

        } else if (beta_versions_source.getValue().equals("custom_from_local")) {
            if (TextUtils.isEmpty(input_beta_versions_source.getText()) == false) {
                String stableReleasesFileName = input_beta_versions_source.getText().substring(input_beta_versions_source.getText().lastIndexOf("/") + 1);
                if (Utils.fileIsExists(this, input_beta_versions_source.getText()) == true) {
                    if (Utils.getFileExtension(stableReleasesFileName).equals("json") == false) {
                        Utils.showDialog(this, getResources().getString(R.string.dialog_versions_source_beta_isnot_json_title), getResources().getString(R.string.dialog_versions_source_beta_isnot_json_message));
                    }
                } else {
                    Utils.showDialog(this, getResources().getString(R.string.dialog_versions_source_beta_file_not_exist_title), getResources().getString(R.string.dialog_versions_source_beta_file_not_exist_message));
                }
            } else {
                Utils.showDialog(this, getResources().getString(R.string.dialog_versions_source_beta_ismpty_title), getResources().getString(R.string.dialog_versions_source_beta_isempty_message));
            }
        }
    }


    public void checkAlphaCustomVersionsSource() {

        if (alpha_versions_source.getValue().equals("custom_from_internet")) {

            if (TextUtils.isEmpty(input_alpha_versions_source.getText()) == false) {
                if (Utils.isUrlAUrl(input_alpha_versions_source.getText()) == true) {
                    String fileNameOfInputStableReleasesVersionsSourceGetText = input_alpha_versions_source.getText().substring(input_alpha_versions_source.getText().lastIndexOf("/") + 1);
                    String fileExtensionOfInputStableReleasesVersionsSourceGetText = fileNameOfInputStableReleasesVersionsSourceGetText.substring(fileNameOfInputStableReleasesVersionsSourceGetText.lastIndexOf(".") + 1);
                    if (fileExtensionOfInputStableReleasesVersionsSourceGetText.equals("json") == true) {
                        Utils.downloadFileForSettings(this, input_alpha_versions_source.getText(), "test_alpha.json", Utils.getDataCachePath(this), VersionsType.ALPHA);
                    } else {
                        Utils.showDialog(this, getResources().getString(R.string.dialog_versions_source_alpha_isnot_json_title), getResources().getString(R.string.dialog_versions_source_alpha_isnot_json_message));
                    }
                } else {
                    Utils.showDialog(this, getResources().getString(R.string.dialog_versions_source_alpha_isnot_url_title), getResources().getString(R.string.dialog_versions_source_alpha_isnot_url_message));
                }
            } else {
                Utils.showDialog(this, getResources().getString(R.string.dialog_versions_source_alpha_ismpty_title), getResources().getString(R.string.dialog_versions_source_alpha_isempty_message));
            }

        } else if (alpha_versions_source.getValue().equals("custom_from_local")) {
            if (TextUtils.isEmpty(input_alpha_versions_source.getText()) == false) {
                String stableReleasesFileName = input_alpha_versions_source.getText().substring(input_alpha_versions_source.getText().lastIndexOf("/") + 1);
                if (Utils.fileIsExists(this, input_alpha_versions_source.getText()) == true) {
                    if (Utils.getFileExtension(stableReleasesFileName).equals("json") == false) {
                        Utils.showDialog(this, getResources().getString(R.string.dialog_versions_source_alpha_isnot_json_title), getResources().getString(R.string.dialog_versions_source_alpha_isnot_json_message));
                    }
                } else {
                    Utils.showDialog(this, getResources().getString(R.string.dialog_versions_source_alpha_file_not_exist_title), getResources().getString(R.string.dialog_versions_source_alpha_file_not_exist_message));
                }
            } else {
                Utils.showDialog(this, getResources().getString(R.string.dialog_versions_source_alpha_ismpty_title), getResources().getString(R.string.dialog_versions_source_alpha_isempty_message));
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initTextSummary1();
        initTextSummary2();
        initTextSummary3();
        initTextSummary4();
        initListSummary1();
        initListSummary2();
        initListSummary3();
        initListSummary4();
        initListSummary5();
        initListSummary6();
        initListSummary7();
        checkCustomVersionsSources();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }
}
