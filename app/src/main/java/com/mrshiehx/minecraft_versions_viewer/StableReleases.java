package com.mrshiehx.minecraft_versions_viewer;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class StableReleases extends Activity {
    public static List<VersionItem> versions = new ArrayList<>();
    public static VersionsAdapter versionsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.initialization(this, R.string.activity_stable_releases_name);
        //Utils.downloadAndInitListview(this,fileUrl,fileName,Utils.getDataFilesPath(this),getResources().getString(R.string.dialog_stable_releases_downloading_file_title),getResources().getString(R.string.dialog_stable_releases_downloading_file_message));

        //setContentView(R.layout.versions_comprehensive_name);
        //ListView listview = (ListView)findViewById(R.id.stable_releases_listview);


        LinearLayout mainLayout = new LinearLayout(this);
        //layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        //layoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        //layoutParams.addRule(RelativeLayout.VISIBLE);
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        setContentView(mainLayout);

        //LinearLayout ll=new LinearLayout(this);
        //ll.setOrientation(LinearLayout.HORIZONTAL);
        //ll.setGravity(Gravity.CENTER_VERTICAL);

        //ViewGroup.LayoutParams lp;
        //lp= ll.getLayoutParams();
        //ll.setMinimumHeight(60);
        //ll.setLayoutParams(lp);

        //TextView tv1=new TextView(this);
        //TextView tv2=new TextView(this);
        //TextView tv3=new TextView(this);
        //tv1.setText("dddddddddddddddddddddddd");
        //tv1.setTextAppearance(android.R.style.TextAppearance_Large);
        //ll.addView(tv1);
        //ll.addView(tv2);
        //ll.addView(tv3);


        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View view = layoutInflater.inflate(R.layout.versions_comprehensive_name, null, false);
        mainLayout.addView(view, 0);
        //Button b=new Button(this);

        ListView listview = new ListView(this);
        //mainLayout.addView(b,1);
        mainLayout.addView(listview, 1);
        versionsAdapter = new VersionsAdapter(this, R.layout.version_item, versions);

        listview.setAdapter(versionsAdapter);
        initListview();
    }

    DialogInterface.OnClickListener goToSettings = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            startActivity(new Intent().setClass(StableReleases.this, Settings.class));
        }
    };


    public void initListview() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        if (sharedPreferences.getString("stable_releases_versions_source", "default").equals("default")) {

            try {
                downloadAndInitListview();
            } catch (JSONException e) {
                Utils.exceptionDialog(this, e, getResources().getString(R.string.dialog_exception_getjsonorinitlistviewfailed));
            }

        } else if (sharedPreferences.getString("stable_releases_versions_source", "default").equals("custom_from_internet")) {

            if (TextUtils.isEmpty(sharedPreferences.getString("input_stable_releases_versions_source", null)) == false) {
                if (Utils.isUrlAUrl(sharedPreferences.getString("input_stable_releases_versions_source", null)) == true) {
                    String fileNameOfInputerVersionsSourceGetText = sharedPreferences.getString("input_stable_releases_versions_source", null).substring(sharedPreferences.getString("input_stable_releases_versions_source", null).lastIndexOf("/") + 1);
                    String fileExtensionOfInputerVersionsSourceGetText = fileNameOfInputerVersionsSourceGetText.substring(fileNameOfInputerVersionsSourceGetText.lastIndexOf(".") + 1);
                    if (fileExtensionOfInputerVersionsSourceGetText.equals("json") == true) {
                        try {
                            Utils.downloadAndInitListview(
                                    this,
                                    sharedPreferences.getString("input_stable_releases_versions_source", null),
                                    fileNameOfInputerVersionsSourceGetText,
                                    Utils.getDataFilesPath(this),
                                    versions,
                                    Utils.getDataFilesPath(this) + "/" + fileNameOfInputerVersionsSourceGetText,
                                    "minecraftStableReleases");
                        } catch (JSONException e) {
                            try {
                                downloadAndInitListview();
                            } catch (JSONException ex) {
                                Utils.exceptionDialog(this, ex, getResources().getString(R.string.dialog_exception_getjsonorinitlistviewfailed));
                            }
                            Utils.exceptionDialog(this, e, getResources().getString(R.string.dialog_exception_downloadorgetjsonorinitlistviewfailed));
                        }
                    } else {
                        try {
                            downloadAndInitListview();
                        } catch (JSONException xe) {
                            Utils.exceptionDialog(this, xe, getResources().getString(R.string.dialog_exception_getjsonorinitlistviewfailed));
                        }
                        Utils.showDialog(this, getResources().getString(R.string.dialog_versions_source_stable_releases_isnot_json_title), getResources().getString(R.string.dialog_versions_source_stable_releases_isnot_json_message) + getResources().getString(R.string.dialog_loaded_default_file), getResources().getString(R.string.dialog_go_to_settings_text_button), goToSettings);
                    }
                } else {
                    try {
                        downloadAndInitListview();
                    } catch (JSONException e) {
                        Utils.exceptionDialog(this, e, getResources().getString(R.string.dialog_exception_getjsonorinitlistviewfailed));
                    }
                    Utils.showDialog(this, getResources().getString(R.string.dialog_versions_source_stable_releases_isnot_url_title), getResources().getString(R.string.dialog_versions_source_stable_releases_isnot_url_message) + getResources().getString(R.string.dialog_loaded_default_file), getResources().getString(R.string.dialog_go_to_settings_text_button), goToSettings);
                }
            } else {
                try {
                    downloadAndInitListview();
                } catch (JSONException e) {
                    Utils.exceptionDialog(this, e, getResources().getString(R.string.dialog_exception_getjsonorinitlistviewfailed));
                }
                Utils.showDialog(this, getResources().getString(R.string.dialog_versions_source_stable_releases_ismpty_title), getResources().getString(R.string.dialog_versions_source_stable_releases_isempty_message) + getResources().getString(R.string.dialog_loaded_default_file), getResources().getString(R.string.dialog_go_to_settings_text_button), goToSettings);
            }


        } else if (sharedPreferences.getString("stable_releases_versions_source", "default").equals("custom_from_local")) {
            if (TextUtils.isEmpty(sharedPreferences.getString("input_stable_releases_versions_source", null)) == false) {
                String stableReleasesFileName = sharedPreferences.getString("input_stable_releases_versions_source", null).substring(sharedPreferences.getString("input_stable_releases_versions_source", null).lastIndexOf("/") + 1);
                if (Utils.fileIsExists(this, sharedPreferences.getString("input_stable_releases_versions_source", null)) == true) {
                    if (Utils.getFileExtension(stableReleasesFileName).equals("json") == true) {

                    } else {
                        try {
                            downloadAndInitListview();
                        } catch (JSONException e) {
                            Utils.exceptionDialog(this, e, getResources().getString(R.string.dialog_exception_getjsonorinitlistviewfailed));
                        }
                        Utils.showDialog(this, getResources().getString(R.string.dialog_versions_source_stable_releases_isnot_json_title), getResources().getString(R.string.dialog_versions_source_stable_releases_isnot_json_message) + getResources().getString(R.string.dialog_loaded_default_file), getResources().getString(R.string.dialog_go_to_settings_text_button), goToSettings);
                    }

                    if (Utils.getFileExtension(stableReleasesFileName).equals("json") == true) {
                        try {
                            //开始加载本地文件
                            Utils.initListview(
                                    this,
                                    versions,
                                    sharedPreferences.getString("input_stable_releases_versions_source", null),
                                    "minecraftStableReleases");
                        } catch (JSONException e) {
                            try {
                                downloadAndInitListview();
                            } catch (JSONException ex) {
                                Utils.exceptionDialog(this, ex, getResources().getString(R.string.dialog_exception_getjsonorinitlistviewfailed));
                            }
                            Utils.exceptionDialog(this, e, getResources().getString(R.string.dialog_exception_getjsonorinitlistviewfailed));
                        }
                    }

                } else {
                    try {
                        downloadAndInitListview();
                    } catch (JSONException e) {
                        Utils.exceptionDialog(this, e, getResources().getString(R.string.dialog_exception_getjsonorinitlistviewfailed));
                    }
                    Utils.showDialog(this, getResources().getString(R.string.dialog_versions_source_stable_releases_file_not_exist_title), getResources().getString(R.string.dialog_versions_source_stable_releases_file_not_exist_message) + getResources().getString(R.string.dialog_loaded_default_file), getResources().getString(R.string.dialog_go_to_settings_text_button), goToSettings);
                }
            } else {
                try {
                    downloadAndInitListview();
                } catch (JSONException e) {
                    Utils.exceptionDialog(this, e, getResources().getString(R.string.dialog_exception_getjsonorinitlistviewfailed));
                }
                Utils.showDialog(this, getResources().getString(R.string.dialog_versions_source_stable_releases_ismpty_title), getResources().getString(R.string.dialog_versions_source_stable_releases_isempty_message) + getResources().getString(R.string.dialog_loaded_default_file), getResources().getString(R.string.dialog_go_to_settings_text_button), goToSettings);
            }
        }
    }

    public void downloadAndInitListview() throws JSONException {
        String strJsondata = Utils.getJsonByAssets(this, "appURLs.json");
        JSONObject jsonObject = new JSONObject(strJsondata);
        String versionsSourceURL = jsonObject.getString("stableReleasesVersionsSourceURL");

        String fileName = versionsSourceURL.substring(versionsSourceURL.lastIndexOf("/") + 1);
        Utils.downloadAndInitListview(
                this,
                versionsSourceURL,
                fileName,
                Utils.getDataFilesPath(this),
                versions,
                Utils.getDataFilesPath(this) + "/" + fileName,
                "minecraftStableReleases");
    }

    private long firstTime = 0;

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                long secondTime = System.currentTimeMillis();
                if (secondTime - firstTime > 2000) {
                    Toast.makeText(this, getResources().getString(R.string.toast_press_again_exit_application), Toast.LENGTH_SHORT).show();
                    firstTime = secondTime;
                    return true;
                } else {
                    System.exit(0);
                }
                break;
        }
        return super.onKeyUp(keyCode, event);
    }
}
