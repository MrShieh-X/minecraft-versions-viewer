package com.mrshiehx.minecraft_versions_viewer;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends TabActivity {
    TabHost tab;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.initialization(this,R.string.activity_main_name);
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //setContentView(R.layout.activity_main);
        tab = getTabHost();
        tab.setup(this.getLocalActivityManager());
        tab.addTab(tab.newTabSpec("stable_releases").setIndicator(getResources().getString(R.string.tab_stable_releases_name)).setContent(new Intent().setClass(this, StableReleases.class)));
        tab.addTab(tab.newTabSpec("snapshot_preview").setIndicator(getResources().getString(R.string.tab_snapshot_preview_name)).setContent(new Intent().setClass(this, SnapshotPreview.class)));
        tab.addTab(tab.newTabSpec("beta").setIndicator(getResources().getString(R.string.tab_beta_name)).setContent(new Intent().setClass(this, Beta.class)));
        tab.addTab(tab.newTabSpec("alpha").setIndicator(getResources().getString(R.string.tab_alpha_name)).setContent(new Intent().setClass(this, Alpha.class)));
        tab.setCurrentTab(1);
        tab.setCurrentTab(2);
        tab.setCurrentTab(3);
        tab.setCurrentTab(0);


        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        if (sharedPreferences.getString("launch_page", "stable_releases").equals("snapshot_preview")) {
            tab.setCurrentTab(1);
        } else if (sharedPreferences.getString("launch_page", "stable_releases").equals("beta")) {
            tab.setCurrentTab(2);
        } else if (sharedPreferences.getString("launch_page", "stable_releases").equals("alpha")) {
            tab.setCurrentTab(3);
        } else {
            tab.setCurrentTab(0);
        }


        if (Utils.isNetworkConnected(this) == false) {

            AlertDialog.Builder noNetworkDialog = new AlertDialog.Builder(this);
            noNetworkDialog.setTitle(getResources().getString(R.string.dialog_no_network_title))
                    .setMessage(getResources().getString(R.string.dialog_no_network_message));
            noNetworkDialog.setNegativeButton(getResources().getString(android.R.string.cancel), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            noNetworkDialog.setPositiveButton(getResources().getString(R.string.dialog_no_network_button_exit), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    System.exit(0);
                }
            });
            noNetworkDialog.show();

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh:
                /*try {
                    Utils.downloadFile(this, "https://gitee.com/MrShiehX/Repository/raw/master/mcvv/stableReleases.json", "stableReleases.json", Utils.getDataFilesPath(this));
                    Utils.downloadFile(this, "https://gitee.com/MrShiehX/Repository/raw/master/mcvv/snapshotPreview.json", "snapshotPreview.json", Utils.getDataFilesPath(this));
                    Utils.downloadFile(this, "https://gitee.com/MrShiehX/Repository/raw/master/mcvv/beta.json", "beta.json", Utils.getDataFilesPath(this));
                    Utils.downloadFile(this, "https://gitee.com/MrShiehX/Repository/raw/master/mcvv/alpha.json", "alpha.json", Utils.getDataFilesPath(this));
                    StableReleases.versionsAdapter.clear();
                    StableReleases.versionsAdapter.addAll(StableReleases.versions);
                    Utils.initListview(this, StableReleases.versions, Utils.getDataFilesPath(this) + "/stableReleases.json", "minecraftStableReleases");
                    StableReleases.versionsAdapter.notifyDataSetChanged();
                    SnapshotPreview.versionsAdapter.clear();
                    SnapshotPreview.versionsAdapter.addAll(SnapshotPreview.versions);
                    Utils.initListview(this, SnapshotPreview.versions, Utils.getDataFilesPath(this) + "/snapshotPreview.json", "minecraftSnapshotPreview");
                    SnapshotPreview.versionsAdapter.notifyDataSetChanged();
                    Beta.versionsAdapter.clear();
                    Beta.versionsAdapter.addAll(Beta.versions);
                    Utils.initListview(this, Beta.versions, Utils.getDataFilesPath(this) + "/beta.json", "minecraftBeta");
                    Beta.versionsAdapter.notifyDataSetChanged();
                    Alpha.versionsAdapter.clear();
                    Alpha.versionsAdapter.addAll(Alpha.versions);
                    Utils.initListview(this, Alpha.versions, Utils.getDataFilesPath(this) + "/alpha.json", "minecraftAlpha");
                    Alpha.versionsAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    Utils.exceptionDialog(this, e,getResources().getString(R.string.dialog_exception_getjsonorinitlistviewfailed));
                }*/
                refreshListviews();
                return true;
            case R.id.settings:
                Intent goToSettingsActivity = new Intent();
                goToSettingsActivity.setClass(this, Settings.class);
                startActivity(goToSettingsActivity);
                return true;
            case R.id.about:
                Intent goToAboutActivity = new Intent();
                goToAboutActivity.setClass(this, About.class);
                startActivity(goToAboutActivity);
                return true;
            case R.id.exit:
                AlertDialog.Builder exitDialog = new AlertDialog.Builder(this);
                exitDialog.setTitle(getResources().getString(R.string.dialog_exit_title));
                ;
                exitDialog.setMessage(getResources().getString(R.string.dialog_exit_message));
                exitDialog.setNegativeButton(getResources().getString(android.R.string.cancel), null);
                exitDialog.setPositiveButton(getResources().getString(R.string.dialog_exit_button_exit), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        System.exit(0);
                    }
                });
                exitDialog.show();
                return true;
            default:
                //do nothing
        }
        return super.onOptionsItemSelected(item);
    }

    public void refreshListviews(){
        refreshStableReleasesListview();
        refreshSnapshotPreviewListview();
        refreshBetaListview();
        refreshAlphaListview();
    }



    DialogInterface.OnClickListener goToSettings=new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            startActivity(new Intent().setClass(MainActivity.this,Settings.class));
        }
    };





    public void refreshStableReleasesListview(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        final String getString=sharedPreferences.getString("input_stable_releases_versions_source",null);
        if(sharedPreferences.getString("stable_releases_versions_source","default").equals("default")){

            try {
                StableReleases.versionsAdapter.clear();
                StableReleases.versionsAdapter.addAll(StableReleases.versions);
                refreshStableReleasesListviewByDefault();
                StableReleases.versionsAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                Utils.exceptionDialog(this,e,getResources().getString(R.string.dialog_exception_getjsonorinitlistviewfailed));
            }

        }else if(sharedPreferences.getString("stable_releases_versions_source","default").equals("custom_from_internet")) {

            if (TextUtils.isEmpty(getString) == false) {
                if (Utils.isUrlAUrl(getString) == true) {
                    final String fileNameOfInputerVersionsSourceGetText = getString.substring(getString.lastIndexOf("/") + 1);
                    String fileExtensionOfInputerVersionsSourceGetText = fileNameOfInputerVersionsSourceGetText.substring(fileNameOfInputerVersionsSourceGetText.lastIndexOf(".") + 1);
                    if (fileExtensionOfInputerVersionsSourceGetText.equals("json") == true) {
                        try {

                            if (Utils.isNetworkConnected(MainActivity.this) == true) {
                                File file = new File(Utils.getDataFilesPath(MainActivity.this));
                                if (!file.exists()) {
                                    file.mkdirs();
                                }
                                final ProgressDialog waitDownload = new ProgressDialog(MainActivity.this);
                                waitDownload.setTitle(getResources().getString(R.string.dialog_wait_download_and_initialization_title));
                                waitDownload.setMessage(getResources().getString(R.string.dialog_wait_download_and_initialization_message));
                                waitDownload.setCancelable(false);
                                waitDownload.show();
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            URL url = new URL(getString);
                                            HttpURLConnection con = (HttpURLConnection) url.openConnection();
                                            con.setReadTimeout(5000);
                                            con.setConnectTimeout(5000);
                                            con.setRequestProperty("Charset", "UTF-8");
                                            con.setRequestMethod("GET");
                                            if (con.getResponseCode() == 200) {
                                                InputStream is = con.getInputStream();
                                                FileOutputStream fileOutputStream = null;
                                                if (is != null) {
                                                    //FileUtils fileUtils = new FileUtils();
                                                    fileOutputStream = new FileOutputStream(Utils.createFile(Utils.getDataFilesPath(MainActivity.this), fileNameOfInputerVersionsSourceGetText));
                                                    byte[] buf = new byte[1024];
                                                    int ch;
                                                    while ((ch = is.read(buf)) != -1) {
                                                        fileOutputStream.write(buf, 0, ch);
                                                    }
                                                }
                                                if (fileOutputStream != null) {
                                                    fileOutputStream.flush();
                                                    fileOutputStream.close();
                                                }
                                            }
                                            final Timer timer = new Timer();
                                            timer.schedule(new TimerTask() {
                                                @Override
                                                public void run() {
                                                    if (Utils.fileIsExists(MainActivity.this, Utils.getDataFilesPath(MainActivity.this) + "/" + fileNameOfInputerVersionsSourceGetText) == true) {
                                                        waitDownload.dismiss();
                                                        timer.cancel();

                                                        try {
                                                            handler.sendEmptyMessage(0);
                                                            /*Looper.prepare();
                                                            StableReleases.versionsAdapter.clear();
                                                            Looper.loop();
                                                            StableReleases.versionsAdapter.addAll(StableReleases.versions);
                                                            Utils.initListview(MainActivity.this, StableReleases.versions, Utils.getDataFilesPath(MainActivity.this) + "/" + fileNameOfInputerVersionsSourceGetText, "minecraftStableReleases");
                                                            StableReleases.versionsAdapter.notifyDataSetChanged();*/
                                                        } catch (Exception E) {
                                                            try {
                                                                refreshStableReleasesListviewByDefault();
                                                            } catch (JSONException exe) {
                                                                Utils.exceptionDialog(MainActivity.this, exe, getResources().getString(R.string.dialog_exception_getjsonorinitlistviewfailed));
                                                            }
                                                            Utils.exceptionDialog(MainActivity.this, E, getResources().getString(R.string.dialog_exception_getjsonorinitlistviewfailed));
                                                        }


                                                    }
                                                }
                                            }, 0, 100);
                                        } catch (Exception e) {

                                            Looper.prepare();
                                            Utils.exceptionDialog(MainActivity.this,e,MainActivity.this.getResources().getString(R.string.dialog_exception_downloadfailed));
                                            Looper.loop();
                                        }
                                    }
                                }).start();
                            }else{
                                Toast.makeText(MainActivity.this, MainActivity.this.getResources().getString(R.string.toast_please_check_your_network), Toast.LENGTH_SHORT).show();
                            }
                        //Utils.downloadFile(this, getString, fileNameOfInputerVersionsSourceGetText, Utils.getDataFilesPath(this));
                            if(Utils.fileIsExists(this,Utils.getDataFilesPath(this)+fileNameOfInputerVersionsSourceGetText)==true) {


                                /*try {
                                    StableReleases.versionsAdapter.clear();
                                    StableReleases.versionsAdapter.addAll(StableReleases.versions);
                                    Utils.initListview(this, StableReleases.versions, Utils.getDataFilesPath(this) + "/" + fileNameOfInputerVersionsSourceGetText, "minecraftStableReleases");
                                    StableReleases.versionsAdapter.notifyDataSetChanged();
                                }catch (JSONException E){
                                    try {
                                        refreshStableReleasesListviewByDefault();
                                    } catch (JSONException exe) {
                                        Utils.exceptionDialog(this,exe,getResources().getString(R.string.dialog_exception_getjsonorinitlistviewfailed));
                                    }
                                    Utils.exceptionDialog(this,E,getResources().getString(R.string.dialog_exception_getjsonorinitlistviewfailed));
                                }
                            }else{*/

                                try {
                                    refreshStableReleasesListviewByDefault();
                                } catch (JSONException ex) {
                                    Utils.exceptionDialog(this,ex,getResources().getString(R.string.dialog_exception_getjsonorinitlistviewfailed));
                                }
                                Utils.showDialog(this,getResources().getString(R.string.dialog_versions_source_stable_releases_url_invaild_title),getResources().getString(R.string.dialog_versions_source_stable_releases_url_invaild_message)+getResources().getString(R.string.dialog_loaded_default_file),getResources().getString(R.string.dialog_go_to_settings_text_button),goToSettings);
                            }
                        } catch (Exception e){
                            try {
                                refreshStableReleasesListviewByDefault();
                            } catch (JSONException ex) {
                                Utils.exceptionDialog(this,ex,getResources().getString(R.string.dialog_exception_getjsonorinitlistviewfailed));
                            }
                            Utils.exceptionDialog(this,e,getResources().getString(R.string.dialog_exception_downloadfailed)+getResources().getString(R.string.dialog_loaded_default_file));
                        }


                    } else {
                        try {
                            refreshStableReleasesListviewByDefault();
                        } catch (JSONException xe) {
                            Utils.exceptionDialog(this,xe,getResources().getString(R.string.dialog_exception_getjsonorinitlistviewfailed));
                        }
                        Utils.showDialog(this, getResources().getString(R.string.dialog_versions_source_stable_releases_isnot_json_title), getResources().getString(R.string.dialog_versions_source_stable_releases_isnot_json_message)+getResources().getString(R.string.dialog_loaded_default_file),getResources().getString(R.string.dialog_go_to_settings_text_button),goToSettings);
                    }
                } else {
                    try {
                        refreshStableReleasesListviewByDefault();
                    } catch (JSONException e) {
                        Utils.exceptionDialog(this,e,getResources().getString(R.string.dialog_exception_getjsonorinitlistviewfailed));
                    }
                    Utils.showDialog(this, getResources().getString(R.string.dialog_versions_source_stable_releases_isnot_url_title), getResources().getString(R.string.dialog_versions_source_stable_releases_isnot_url_message)+getResources().getString(R.string.dialog_loaded_default_file),getResources().getString(R.string.dialog_go_to_settings_text_button),goToSettings);
                }
            } else {
                try {
                    refreshStableReleasesListviewByDefault();
                } catch (JSONException e) {
                    Utils.exceptionDialog(this,e,getResources().getString(R.string.dialog_exception_getjsonorinitlistviewfailed));
                }
                Utils.showDialog(this, getResources().getString(R.string.dialog_versions_source_stable_releases_ismpty_title), getResources().getString(R.string.dialog_versions_source_stable_releases_isempty_message)+getResources().getString(R.string.dialog_loaded_default_file),getResources().getString(R.string.dialog_go_to_settings_text_button),goToSettings);
            }



        }else if(sharedPreferences.getString("stable_releases_versions_source","default").equals("custom_from_local")) {
            if (TextUtils.isEmpty(getString) == false) {
                String stableReleasesFileName = getString.substring(getString.lastIndexOf("/") + 1);
                if (Utils.fileIsExists(this, getString) == true) {
                    if (Utils.getFileExtension(stableReleasesFileName).equals("json") == false) {
                        try {
                            refreshStableReleasesListviewByDefault();
                        } catch (JSONException e) {
                            Utils.exceptionDialog(this,e,getResources().getString(R.string.dialog_exception_getjsonorinitlistviewfailed));
                        }
                        Utils.showDialog(this, getResources().getString(R.string.dialog_versions_source_stable_releases_isnot_json_title), getResources().getString(R.string.dialog_versions_source_stable_releases_isnot_json_message)+getResources().getString(R.string.dialog_loaded_default_file),getResources().getString(R.string.dialog_go_to_settings_text_button),goToSettings);
                    }

                    if (Utils.getFileExtension(stableReleasesFileName).equals("json") == true) {
                        try {
                            //开始加载本地文件
                            StableReleases.versionsAdapter.clear();
                            StableReleases.versionsAdapter.addAll(StableReleases.versions);
                            Utils.initListview(
                                    this,
                                    StableReleases.versions,
                                    getString,
                                    "minecraftStableReleases");
                            StableReleases.versionsAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            try {refreshStableReleasesListviewByDefault();
                            } catch (JSONException ex) {
                                Utils.exceptionDialog(this,ex,getResources().getString(R.string.dialog_exception_getjsonorinitlistviewfailed));
                            }
                            Utils.exceptionDialog(this,e,getResources().getString(R.string.dialog_exception_getjsonorinitlistviewfailed));
                        }
                    }

                } else {
                    try {
                        refreshStableReleasesListviewByDefault();
                    } catch (JSONException e) {
                        Utils.exceptionDialog(this,e,getResources().getString(R.string.dialog_exception_getjsonorinitlistviewfailed));
                    }
                    Utils.showDialog(this, getResources().getString(R.string.dialog_versions_source_stable_releases_file_not_exist_title), getResources().getString(R.string.dialog_versions_source_stable_releases_file_not_exist_message)+getResources().getString(R.string.dialog_loaded_default_file),getResources().getString(R.string.dialog_go_to_settings_text_button),goToSettings);
                }
            } else {
                try {
                    refreshStableReleasesListviewByDefault();
                } catch (JSONException e) {
                    Utils.exceptionDialog(this,e,getResources().getString(R.string.dialog_exception_getjsonorinitlistviewfailed));
                }
                Utils.showDialog(this, getResources().getString(R.string.dialog_versions_source_stable_releases_ismpty_title), getResources().getString(R.string.dialog_versions_source_stable_releases_isempty_message)+getResources().getString(R.string.dialog_loaded_default_file),getResources().getString(R.string.dialog_go_to_settings_text_button),goToSettings);
            }
        }
    }




    public void refreshSnapshotPreviewListview(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        final String getString=sharedPreferences.getString("input_snapshot_preview_versions_source",null);
        if(sharedPreferences.getString("snapshot_preview_versions_source","default").equals("default")){

            try {
                SnapshotPreview.versionsAdapter.clear();
                SnapshotPreview.versionsAdapter.addAll(SnapshotPreview.versions);
                refreshSnapshotPreviewListviewByDefault();
                SnapshotPreview.versionsAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                Utils.exceptionDialog(this,e,getResources().getString(R.string.dialog_exception_getjsonorinitlistviewfailed));
            }

        }else if(sharedPreferences.getString("snapshot_preview_versions_source","default").equals("custom_from_internet")) {

            if (TextUtils.isEmpty(getString) == false) {
                if (Utils.isUrlAUrl(getString) == true) {
                    final String fileNameOfInputerVersionsSourceGetText = getString.substring(getString.lastIndexOf("/") + 1);
                    String fileExtensionOfInputerVersionsSourceGetText = fileNameOfInputerVersionsSourceGetText.substring(fileNameOfInputerVersionsSourceGetText.lastIndexOf(".") + 1);
                    if (fileExtensionOfInputerVersionsSourceGetText.equals("json") == true) {
                        try {

                            if (Utils.isNetworkConnected(MainActivity.this) == true) {
                                File file = new File(Utils.getDataFilesPath(MainActivity.this));
                                if (!file.exists()) {
                                    file.mkdirs();
                                }
                                final ProgressDialog waitDownload = new ProgressDialog(MainActivity.this);
                                waitDownload.setTitle(getResources().getString(R.string.dialog_wait_download_and_initialization_title));
                                waitDownload.setMessage(getResources().getString(R.string.dialog_wait_download_and_initialization_message));
                                waitDownload.setCancelable(false);
                                waitDownload.show();
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            URL url = new URL(getString);
                                            HttpURLConnection con = (HttpURLConnection) url.openConnection();
                                            con.setReadTimeout(5000);
                                            con.setConnectTimeout(5000);
                                            con.setRequestProperty("Charset", "UTF-8");
                                            con.setRequestMethod("GET");
                                            if (con.getResponseCode() == 200) {
                                                InputStream is = con.getInputStream();
                                                FileOutputStream fileOutputStream = null;
                                                if (is != null) {
                                                    //FileUtils fileUtils = new FileUtils();
                                                    fileOutputStream = new FileOutputStream(Utils.createFile(Utils.getDataFilesPath(MainActivity.this), fileNameOfInputerVersionsSourceGetText));
                                                    byte[] buf = new byte[1024];
                                                    int ch;
                                                    while ((ch = is.read(buf)) != -1) {
                                                        fileOutputStream.write(buf, 0, ch);
                                                    }
                                                }
                                                if (fileOutputStream != null) {
                                                    fileOutputStream.flush();
                                                    fileOutputStream.close();
                                                }
                                            }
                                            final Timer timer = new Timer();
                                            timer.schedule(new TimerTask() {
                                                @Override
                                                public void run() {
                                                    if (Utils.fileIsExists(MainActivity.this, Utils.getDataFilesPath(MainActivity.this) + "/" + fileNameOfInputerVersionsSourceGetText) == true) {
                                                        waitDownload.dismiss();
                                                        timer.cancel();

                                                        try {
                                                            handler.sendEmptyMessage(1);
                                                        } catch (Exception E) {
                                                            try {
                                                                refreshSnapshotPreviewListviewByDefault();
                                                            } catch (JSONException exe) {
                                                                Utils.exceptionDialog(MainActivity.this, exe, getResources().getString(R.string.dialog_exception_getjsonorinitlistviewfailed));
                                                            }
                                                            Utils.exceptionDialog(MainActivity.this, E, getResources().getString(R.string.dialog_exception_getjsonorinitlistviewfailed));
                                                        }


                                                    }
                                                }
                                            }, 0, 100);
                                        } catch (Exception e) {

                                            Looper.prepare();
                                            Utils.exceptionDialog(MainActivity.this,e,MainActivity.this.getResources().getString(R.string.dialog_exception_downloadfailed));
                                            Looper.loop();
                                        }
                                    }
                                }).start();
                            }else{
                                Toast.makeText(MainActivity.this, MainActivity.this.getResources().getString(R.string.toast_please_check_your_network), Toast.LENGTH_SHORT).show();
                            }
                            //Utils.downloadFile(this, getString, fileNameOfInputerVersionsSourceGetText, Utils.getDataFilesPath(this));
                            if(Utils.fileIsExists(this,Utils.getDataFilesPath(this)+fileNameOfInputerVersionsSourceGetText)==true) {
                                try {
                                    refreshSnapshotPreviewListviewByDefault();
                                } catch (JSONException ex) {
                                    Utils.exceptionDialog(this,ex,getResources().getString(R.string.dialog_exception_getjsonorinitlistviewfailed));
                                }
                                Utils.showDialog(this,getResources().getString(R.string.dialog_versions_source_snapshot_preview_url_invaild_title),getResources().getString(R.string.dialog_versions_source_snapshot_preview_url_invaild_message)+getResources().getString(R.string.dialog_loaded_default_file),getResources().getString(R.string.dialog_go_to_settings_text_button),goToSettings);
                            }
                        } catch (Exception e){
                            try {
                                refreshSnapshotPreviewListviewByDefault();
                            } catch (JSONException ex) {
                                Utils.exceptionDialog(this,ex,getResources().getString(R.string.dialog_exception_getjsonorinitlistviewfailed));
                            }
                            Utils.exceptionDialog(this,e,getResources().getString(R.string.dialog_exception_downloadfailed)+getResources().getString(R.string.dialog_loaded_default_file));
                        }


                    } else {
                        try {
                            refreshSnapshotPreviewListviewByDefault();
                        } catch (JSONException xe) {
                            Utils.exceptionDialog(this,xe,getResources().getString(R.string.dialog_exception_getjsonorinitlistviewfailed));
                        }
                        Utils.showDialog(this, getResources().getString(R.string.dialog_versions_source_snapshot_preview_isnot_json_title), getResources().getString(R.string.dialog_versions_source_snapshot_preview_isnot_json_message)+getResources().getString(R.string.dialog_loaded_default_file),getResources().getString(R.string.dialog_go_to_settings_text_button),goToSettings);
                    }
                } else {
                    try {
                        refreshSnapshotPreviewListviewByDefault();
                    } catch (JSONException e) {
                        Utils.exceptionDialog(this,e,getResources().getString(R.string.dialog_exception_getjsonorinitlistviewfailed));
                    }
                    Utils.showDialog(this, getResources().getString(R.string.dialog_versions_source_snapshot_preview_isnot_url_title), getResources().getString(R.string.dialog_versions_source_snapshot_preview_isnot_url_message)+getResources().getString(R.string.dialog_loaded_default_file),getResources().getString(R.string.dialog_go_to_settings_text_button),goToSettings);
                }
            } else {
                try {
                    refreshSnapshotPreviewListviewByDefault();
                } catch (JSONException e) {
                    Utils.exceptionDialog(this,e,getResources().getString(R.string.dialog_exception_getjsonorinitlistviewfailed));
                }
                Utils.showDialog(this, getResources().getString(R.string.dialog_versions_source_snapshot_preview_ismpty_title), getResources().getString(R.string.dialog_versions_source_snapshot_preview_isempty_message)+getResources().getString(R.string.dialog_loaded_default_file),getResources().getString(R.string.dialog_go_to_settings_text_button),goToSettings);
            }



        }else if(sharedPreferences.getString("snapshot_preview_versions_source","default").equals("custom_from_local")) {
            if (TextUtils.isEmpty(getString) == false) {
                String snapshotPreviewFileName = getString.substring(getString.lastIndexOf("/") + 1);
                if (Utils.fileIsExists(this, getString) == true) {
                    if (Utils.getFileExtension(snapshotPreviewFileName).equals("json") == false) {
                        try {
                            refreshSnapshotPreviewListviewByDefault();
                        } catch (JSONException e) {
                            Utils.exceptionDialog(this,e,getResources().getString(R.string.dialog_exception_getjsonorinitlistviewfailed));
                        }
                        Utils.showDialog(this, getResources().getString(R.string.dialog_versions_source_snapshot_preview_isnot_json_title), getResources().getString(R.string.dialog_versions_source_snapshot_preview_isnot_json_message)+getResources().getString(R.string.dialog_loaded_default_file),getResources().getString(R.string.dialog_go_to_settings_text_button),goToSettings);
                    }

                    if (Utils.getFileExtension(snapshotPreviewFileName).equals("json") == true) {
                        try {
                            //开始加载本地文件
                            SnapshotPreview.versionsAdapter.clear();
                            SnapshotPreview.versionsAdapter.addAll(SnapshotPreview.versions);
                            Utils.initListview(
                                    this,
                                    SnapshotPreview.versions,
                                    getString,
                                    "minecraftSnapshotPreview");
                            SnapshotPreview.versionsAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            try {refreshSnapshotPreviewListviewByDefault();
                            } catch (JSONException ex) {
                                Utils.exceptionDialog(this,ex,getResources().getString(R.string.dialog_exception_getjsonorinitlistviewfailed));
                            }
                            Utils.exceptionDialog(this,e,getResources().getString(R.string.dialog_exception_getjsonorinitlistviewfailed));
                        }
                    }

                } else {
                    try {
                        refreshSnapshotPreviewListviewByDefault();
                    } catch (JSONException e) {
                        Utils.exceptionDialog(this,e,getResources().getString(R.string.dialog_exception_getjsonorinitlistviewfailed));
                    }
                    Utils.showDialog(this, getResources().getString(R.string.dialog_versions_source_snapshot_preview_file_not_exist_title), getResources().getString(R.string.dialog_versions_source_snapshot_preview_file_not_exist_message)+getResources().getString(R.string.dialog_loaded_default_file),getResources().getString(R.string.dialog_go_to_settings_text_button),goToSettings);
                }
            } else {
                try {
                    refreshSnapshotPreviewListviewByDefault();
                } catch (JSONException e) {
                    Utils.exceptionDialog(this,e,getResources().getString(R.string.dialog_exception_getjsonorinitlistviewfailed));
                }
                Utils.showDialog(this, getResources().getString(R.string.dialog_versions_source_snapshot_preview_ismpty_title), getResources().getString(R.string.dialog_versions_source_snapshot_preview_isempty_message)+getResources().getString(R.string.dialog_loaded_default_file),getResources().getString(R.string.dialog_go_to_settings_text_button),goToSettings);
            }
        }
    }




    public void refreshBetaListview(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        final String getString=sharedPreferences.getString("input_beta_versions_source",null);
        if(sharedPreferences.getString("beta_versions_source","default").equals("default")){

            try {
                Beta.versionsAdapter.clear();
                Beta.versionsAdapter.addAll(Beta.versions);
                refreshBetaListviewByDefault();
                Beta.versionsAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                Utils.exceptionDialog(this,e,getResources().getString(R.string.dialog_exception_getjsonorinitlistviewfailed));
            }

        }else if(sharedPreferences.getString("beta_versions_source","default").equals("custom_from_internet")) {

            if (TextUtils.isEmpty(getString) == false) {
                if (Utils.isUrlAUrl(getString) == true) {
                    final String fileNameOfInputerVersionsSourceGetText = getString.substring(getString.lastIndexOf("/") + 1);
                    String fileExtensionOfInputerVersionsSourceGetText = fileNameOfInputerVersionsSourceGetText.substring(fileNameOfInputerVersionsSourceGetText.lastIndexOf(".") + 1);
                    if (fileExtensionOfInputerVersionsSourceGetText.equals("json") == true) {
                        try {

                            if (Utils.isNetworkConnected(MainActivity.this) == true) {
                                File file = new File(Utils.getDataFilesPath(MainActivity.this));
                                if (!file.exists()) {
                                    file.mkdirs();
                                }
                                final ProgressDialog waitDownload = new ProgressDialog(MainActivity.this);
                                waitDownload.setTitle(getResources().getString(R.string.dialog_wait_download_and_initialization_title));
                                waitDownload.setMessage(getResources().getString(R.string.dialog_wait_download_and_initialization_message));
                                waitDownload.setCancelable(false);
                                waitDownload.show();
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            URL url = new URL(getString);
                                            HttpURLConnection con = (HttpURLConnection) url.openConnection();
                                            con.setReadTimeout(5000);
                                            con.setConnectTimeout(5000);
                                            con.setRequestProperty("Charset", "UTF-8");
                                            con.setRequestMethod("GET");
                                            if (con.getResponseCode() == 200) {
                                                InputStream is = con.getInputStream();
                                                FileOutputStream fileOutputStream = null;
                                                if (is != null) {
                                                    //FileUtils fileUtils = new FileUtils();
                                                    fileOutputStream = new FileOutputStream(Utils.createFile(Utils.getDataFilesPath(MainActivity.this), fileNameOfInputerVersionsSourceGetText));
                                                    byte[] buf = new byte[1024];
                                                    int ch;
                                                    while ((ch = is.read(buf)) != -1) {
                                                        fileOutputStream.write(buf, 0, ch);
                                                    }
                                                }
                                                if (fileOutputStream != null) {
                                                    fileOutputStream.flush();
                                                    fileOutputStream.close();
                                                }
                                            }
                                            final Timer timer = new Timer();
                                            timer.schedule(new TimerTask() {
                                                @Override
                                                public void run() {
                                                    if (Utils.fileIsExists(MainActivity.this, Utils.getDataFilesPath(MainActivity.this) + "/" + fileNameOfInputerVersionsSourceGetText) == true) {
                                                        waitDownload.dismiss();
                                                        timer.cancel();

                                                        try {
                                                            handler.sendEmptyMessage(2);
                                                        } catch (Exception E) {
                                                            try {
                                                                refreshBetaListviewByDefault();
                                                            } catch (JSONException exe) {
                                                                Utils.exceptionDialog(MainActivity.this, exe, getResources().getString(R.string.dialog_exception_getjsonorinitlistviewfailed));
                                                            }
                                                            Utils.exceptionDialog(MainActivity.this, E, getResources().getString(R.string.dialog_exception_getjsonorinitlistviewfailed));
                                                        }


                                                    }
                                                }
                                            }, 0, 100);
                                        } catch (Exception e) {

                                            Looper.prepare();
                                            Utils.exceptionDialog(MainActivity.this,e,MainActivity.this.getResources().getString(R.string.dialog_exception_downloadfailed));
                                            Looper.loop();
                                        }
                                    }
                                }).start();
                            }else{
                                Toast.makeText(MainActivity.this, MainActivity.this.getResources().getString(R.string.toast_please_check_your_network), Toast.LENGTH_SHORT).show();
                            }
                            //Utils.downloadFile(this, getString, fileNameOfInputerVersionsSourceGetText, Utils.getDataFilesPath(this));
                            if(Utils.fileIsExists(this,Utils.getDataFilesPath(this)+fileNameOfInputerVersionsSourceGetText)==true) {
                                try {
                                    refreshBetaListviewByDefault();
                                } catch (JSONException ex) {
                                    Utils.exceptionDialog(this,ex,getResources().getString(R.string.dialog_exception_getjsonorinitlistviewfailed));
                                }
                                Utils.showDialog(this,getResources().getString(R.string.dialog_versions_source_beta_url_invaild_title),getResources().getString(R.string.dialog_versions_source_beta_url_invaild_message)+getResources().getString(R.string.dialog_loaded_default_file),getResources().getString(R.string.dialog_go_to_settings_text_button),goToSettings);
                            }
                        } catch (Exception e){
                            try {
                                refreshBetaListviewByDefault();
                            } catch (JSONException ex) {
                                Utils.exceptionDialog(this,ex,getResources().getString(R.string.dialog_exception_getjsonorinitlistviewfailed));
                            }
                            Utils.exceptionDialog(this,e,getResources().getString(R.string.dialog_exception_downloadfailed)+getResources().getString(R.string.dialog_loaded_default_file));
                        }


                    } else {
                        try {
                            refreshBetaListviewByDefault();
                        } catch (JSONException xe) {
                            Utils.exceptionDialog(this,xe,getResources().getString(R.string.dialog_exception_getjsonorinitlistviewfailed));
                        }
                        Utils.showDialog(this, getResources().getString(R.string.dialog_versions_source_beta_isnot_json_title), getResources().getString(R.string.dialog_versions_source_beta_isnot_json_message)+getResources().getString(R.string.dialog_loaded_default_file),getResources().getString(R.string.dialog_go_to_settings_text_button),goToSettings);
                    }
                } else {
                    try {
                        refreshBetaListviewByDefault();
                    } catch (JSONException e) {
                        Utils.exceptionDialog(this,e,getResources().getString(R.string.dialog_exception_getjsonorinitlistviewfailed));
                    }
                    Utils.showDialog(this, getResources().getString(R.string.dialog_versions_source_beta_isnot_url_title), getResources().getString(R.string.dialog_versions_source_beta_isnot_url_message)+getResources().getString(R.string.dialog_loaded_default_file),getResources().getString(R.string.dialog_go_to_settings_text_button),goToSettings);
                }
            } else {
                try {
                    refreshBetaListviewByDefault();
                } catch (JSONException e) {
                    Utils.exceptionDialog(this,e,getResources().getString(R.string.dialog_exception_getjsonorinitlistviewfailed));
                }
                Utils.showDialog(this, getResources().getString(R.string.dialog_versions_source_beta_ismpty_title), getResources().getString(R.string.dialog_versions_source_beta_isempty_message)+getResources().getString(R.string.dialog_loaded_default_file),getResources().getString(R.string.dialog_go_to_settings_text_button),goToSettings);
            }



        }else if(sharedPreferences.getString("beta_versions_source","default").equals("custom_from_local")) {
            if (TextUtils.isEmpty(getString) == false) {
                String betaFileName = getString.substring(getString.lastIndexOf("/") + 1);
                if (Utils.fileIsExists(this, getString) == true) {
                    if (Utils.getFileExtension(betaFileName).equals("json") == false) {
                        try {
                            refreshBetaListviewByDefault();
                        } catch (JSONException e) {
                            Utils.exceptionDialog(this,e,getResources().getString(R.string.dialog_exception_getjsonorinitlistviewfailed));
                        }
                        Utils.showDialog(this, getResources().getString(R.string.dialog_versions_source_beta_isnot_json_title), getResources().getString(R.string.dialog_versions_source_beta_isnot_json_message)+getResources().getString(R.string.dialog_loaded_default_file),getResources().getString(R.string.dialog_go_to_settings_text_button),goToSettings);
                    }

                    if (Utils.getFileExtension(betaFileName).equals("json") == true) {
                        try {
                            //开始加载本地文件
                            Beta.versionsAdapter.clear();
                            Beta.versionsAdapter.addAll(Beta.versions);
                            Utils.initListview(
                                    this,
                                    Beta.versions,
                                    getString,
                                    "minecraftBeta");
                            Beta.versionsAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            try {refreshBetaListviewByDefault();
                            } catch (JSONException ex) {
                                Utils.exceptionDialog(this,ex,getResources().getString(R.string.dialog_exception_getjsonorinitlistviewfailed));
                            }
                            Utils.exceptionDialog(this,e,getResources().getString(R.string.dialog_exception_getjsonorinitlistviewfailed));
                        }
                    }

                } else {
                    try {
                        refreshBetaListviewByDefault();
                    } catch (JSONException e) {
                        Utils.exceptionDialog(this,e,getResources().getString(R.string.dialog_exception_getjsonorinitlistviewfailed));
                    }
                    Utils.showDialog(this, getResources().getString(R.string.dialog_versions_source_beta_file_not_exist_title), getResources().getString(R.string.dialog_versions_source_beta_file_not_exist_message)+getResources().getString(R.string.dialog_loaded_default_file),getResources().getString(R.string.dialog_go_to_settings_text_button),goToSettings);
                }
            } else {
                try {
                    refreshBetaListviewByDefault();
                } catch (JSONException e) {
                    Utils.exceptionDialog(this,e,getResources().getString(R.string.dialog_exception_getjsonorinitlistviewfailed));
                }
                Utils.showDialog(this, getResources().getString(R.string.dialog_versions_source_beta_ismpty_title), getResources().getString(R.string.dialog_versions_source_beta_isempty_message)+getResources().getString(R.string.dialog_loaded_default_file),getResources().getString(R.string.dialog_go_to_settings_text_button),goToSettings);
            }
        }
    }



    public void refreshListviewByDefault(String versionsSourceURLFromJSONItemName, final int whatNumber) throws JSONException {
        String strJsondata = Utils.getJsonByAssets(this, "appURLs.json");
        JSONObject jsonObject = new JSONObject(strJsondata);
        String versionsSourceURL = jsonObject.getString(versionsSourceURLFromJSONItemName);

        final String fileName=versionsSourceURL.substring(versionsSourceURL.lastIndexOf("/") + 1);
        Utils.downloadFile(this, versionsSourceURL, fileName, Utils.getDataFilesPath(this));
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (Utils.fileIsExists(MainActivity.this, Utils.getDataFilesPath(MainActivity.this)+"/"+fileName) == true) {
                    try {
                        handler.sendEmptyMessage(whatNumber);
                    } catch (Exception e) {
                        Looper.prepare();
                        Utils.exceptionDialog(MainActivity.this,e,getResources().getString(R.string.dialog_exception_initlistviewfailed));
                        Looper.loop();
                    }
                    timer.cancel();
                }else{

                }
            }
        }, 0, 100);
    }




    public void refreshAlphaListview(){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        final String getString=sharedPreferences.getString("input_alpha_versions_source",null);
        if(sharedPreferences.getString("alpha_versions_source","default").equals("default")){

            try {
                Alpha.versionsAdapter.clear();
                Alpha.versionsAdapter.addAll(Alpha.versions);
                refreshAlphaListviewByDefault();
                Alpha.versionsAdapter.notifyDataSetChanged();
            } catch (JSONException e) {
                Utils.exceptionDialog(this,e,getResources().getString(R.string.dialog_exception_getjsonorinitlistviewfailed));
            }

        }else if(sharedPreferences.getString("alpha_versions_source","default").equals("custom_from_internet")) {

            if (TextUtils.isEmpty(getString) == false) {
                if (Utils.isUrlAUrl(getString) == true) {
                    final String fileNameOfInputerVersionsSourceGetText = getString.substring(getString.lastIndexOf("/") + 1);
                    String fileExtensionOfInputerVersionsSourceGetText = fileNameOfInputerVersionsSourceGetText.substring(fileNameOfInputerVersionsSourceGetText.lastIndexOf(".") + 1);
                    if (fileExtensionOfInputerVersionsSourceGetText.equals("json") == true) {
                        try {

                            if (Utils.isNetworkConnected(MainActivity.this) == true) {
                                File file = new File(Utils.getDataFilesPath(MainActivity.this));
                                if (!file.exists()) {
                                    file.mkdirs();
                                }
                                final ProgressDialog waitDownload = new ProgressDialog(MainActivity.this);
                                waitDownload.setTitle(getResources().getString(R.string.dialog_wait_download_and_initialization_title));
                                waitDownload.setMessage(getResources().getString(R.string.dialog_wait_download_and_initialization_message));
                                waitDownload.setCancelable(false);
                                waitDownload.show();
                                new Thread(new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            URL url = new URL(getString);
                                            HttpURLConnection con = (HttpURLConnection) url.openConnection();
                                            con.setReadTimeout(5000);
                                            con.setConnectTimeout(5000);
                                            con.setRequestProperty("Charset", "UTF-8");
                                            con.setRequestMethod("GET");
                                            if (con.getResponseCode() == 200) {
                                                InputStream is = con.getInputStream();
                                                FileOutputStream fileOutputStream = null;
                                                if (is != null) {
                                                    //FileUtils fileUtils = new FileUtils();
                                                    fileOutputStream = new FileOutputStream(Utils.createFile(Utils.getDataFilesPath(MainActivity.this), fileNameOfInputerVersionsSourceGetText));
                                                    byte[] buf = new byte[1024];
                                                    int ch;
                                                    while ((ch = is.read(buf)) != -1) {
                                                        fileOutputStream.write(buf, 0, ch);
                                                    }
                                                }
                                                if (fileOutputStream != null) {
                                                    fileOutputStream.flush();
                                                    fileOutputStream.close();
                                                }
                                            }
                                            final Timer timer = new Timer();
                                            timer.schedule(new TimerTask() {
                                                @Override
                                                public void run() {
                                                    if (Utils.fileIsExists(MainActivity.this, Utils.getDataFilesPath(MainActivity.this) + "/" + fileNameOfInputerVersionsSourceGetText) == true) {
                                                        waitDownload.dismiss();
                                                        timer.cancel();

                                                        try {
                                                            handler.sendEmptyMessage(3);
                                                        } catch (Exception E) {
                                                            try {
                                                                refreshAlphaListviewByDefault();
                                                            } catch (JSONException exe) {
                                                                Utils.exceptionDialog(MainActivity.this, exe, getResources().getString(R.string.dialog_exception_getjsonorinitlistviewfailed));
                                                            }
                                                            Utils.exceptionDialog(MainActivity.this, E, getResources().getString(R.string.dialog_exception_getjsonorinitlistviewfailed));
                                                        }


                                                    }
                                                }
                                            }, 0, 100);
                                        } catch (Exception e) {

                                            Looper.prepare();
                                            Utils.exceptionDialog(MainActivity.this,e,MainActivity.this.getResources().getString(R.string.dialog_exception_downloadfailed));
                                            Looper.loop();
                                        }
                                    }
                                }).start();
                            }else{
                                Toast.makeText(MainActivity.this, MainActivity.this.getResources().getString(R.string.toast_please_check_your_network), Toast.LENGTH_SHORT).show();
                            }
                            //Utils.downloadFile(this, getString, fileNameOfInputerVersionsSourceGetText, Utils.getDataFilesPath(this));
                            if(Utils.fileIsExists(this,Utils.getDataFilesPath(this)+fileNameOfInputerVersionsSourceGetText)==true) {
                                try {
                                    refreshAlphaListviewByDefault();
                                } catch (JSONException ex) {
                                    Utils.exceptionDialog(this,ex,getResources().getString(R.string.dialog_exception_getjsonorinitlistviewfailed));
                                }
                                Utils.showDialog(this,getResources().getString(R.string.dialog_versions_source_alpha_url_invaild_title),getResources().getString(R.string.dialog_versions_source_alpha_url_invaild_message)+getResources().getString(R.string.dialog_loaded_default_file),getResources().getString(R.string.dialog_go_to_settings_text_button),goToSettings);
                            }
                        } catch (Exception e){
                            try {
                                refreshAlphaListviewByDefault();
                            } catch (JSONException ex) {
                                Utils.exceptionDialog(this,ex,getResources().getString(R.string.dialog_exception_getjsonorinitlistviewfailed));
                            }
                            Utils.exceptionDialog(this,e,getResources().getString(R.string.dialog_exception_downloadfailed)+getResources().getString(R.string.dialog_loaded_default_file));
                        }


                    } else {
                        try {
                            refreshAlphaListviewByDefault();
                        } catch (JSONException xe) {
                            Utils.exceptionDialog(this,xe,getResources().getString(R.string.dialog_exception_getjsonorinitlistviewfailed));
                        }
                        Utils.showDialog(this, getResources().getString(R.string.dialog_versions_source_alpha_isnot_json_title), getResources().getString(R.string.dialog_versions_source_alpha_isnot_json_message)+getResources().getString(R.string.dialog_loaded_default_file),getResources().getString(R.string.dialog_go_to_settings_text_button),goToSettings);
                    }
                } else {
                    try {
                        refreshAlphaListviewByDefault();
                    } catch (JSONException e) {
                        Utils.exceptionDialog(this,e,getResources().getString(R.string.dialog_exception_getjsonorinitlistviewfailed));
                    }
                    Utils.showDialog(this, getResources().getString(R.string.dialog_versions_source_alpha_isnot_url_title), getResources().getString(R.string.dialog_versions_source_alpha_isnot_url_message)+getResources().getString(R.string.dialog_loaded_default_file),getResources().getString(R.string.dialog_go_to_settings_text_button),goToSettings);
                }
            } else {
                try {
                    refreshAlphaListviewByDefault();
                } catch (JSONException e) {
                    Utils.exceptionDialog(this,e,getResources().getString(R.string.dialog_exception_getjsonorinitlistviewfailed));
                }
                Utils.showDialog(this, getResources().getString(R.string.dialog_versions_source_alpha_ismpty_title), getResources().getString(R.string.dialog_versions_source_alpha_isempty_message)+getResources().getString(R.string.dialog_loaded_default_file),getResources().getString(R.string.dialog_go_to_settings_text_button),goToSettings);
            }



        }else if(sharedPreferences.getString("alpha_versions_source","default").equals("custom_from_local")) {
            if (TextUtils.isEmpty(getString) == false) {
                String alphaFileName = getString.substring(getString.lastIndexOf("/") + 1);
                if (Utils.fileIsExists(this, getString) == true) {
                    if (Utils.getFileExtension(alphaFileName).equals("json") == false) {
                        try {
                            refreshAlphaListviewByDefault();
                        } catch (JSONException e) {
                            Utils.exceptionDialog(this,e,getResources().getString(R.string.dialog_exception_getjsonorinitlistviewfailed));
                        }
                        Utils.showDialog(this, getResources().getString(R.string.dialog_versions_source_alpha_isnot_json_title), getResources().getString(R.string.dialog_versions_source_alpha_isnot_json_message)+getResources().getString(R.string.dialog_loaded_default_file),getResources().getString(R.string.dialog_go_to_settings_text_button),goToSettings);
                    }

                    if (Utils.getFileExtension(alphaFileName).equals("json") == true) {
                        try {
                            //开始加载本地文件
                            Alpha.versionsAdapter.clear();
                            Alpha.versionsAdapter.addAll(Alpha.versions);
                            Utils.initListview(
                                    this,
                                    Alpha.versions,
                                    getString,
                                    "minecraftAlpha");
                            Alpha.versionsAdapter.notifyDataSetChanged();
                        } catch (JSONException e) {
                            try {refreshAlphaListviewByDefault();
                            } catch (JSONException ex) {
                                Utils.exceptionDialog(this,ex,getResources().getString(R.string.dialog_exception_getjsonorinitlistviewfailed));
                            }
                            Utils.exceptionDialog(this,e,getResources().getString(R.string.dialog_exception_getjsonorinitlistviewfailed));
                        }
                    }

                } else {
                    try {
                        refreshAlphaListviewByDefault();
                    } catch (JSONException e) {
                        Utils.exceptionDialog(this,e,getResources().getString(R.string.dialog_exception_getjsonorinitlistviewfailed));
                    }
                    Utils.showDialog(this, getResources().getString(R.string.dialog_versions_source_alpha_file_not_exist_title), getResources().getString(R.string.dialog_versions_source_alpha_file_not_exist_message)+getResources().getString(R.string.dialog_loaded_default_file),getResources().getString(R.string.dialog_go_to_settings_text_button),goToSettings);
                }
            } else {
                try {
                    refreshAlphaListviewByDefault();
                } catch (JSONException e) {
                    Utils.exceptionDialog(this,e,getResources().getString(R.string.dialog_exception_getjsonorinitlistviewfailed));
                }
                Utils.showDialog(this, getResources().getString(R.string.dialog_versions_source_alpha_ismpty_title), getResources().getString(R.string.dialog_versions_source_alpha_isempty_message)+getResources().getString(R.string.dialog_loaded_default_file),getResources().getString(R.string.dialog_go_to_settings_text_button),goToSettings);
            }
        }
    }

    public void refreshStableReleasesListviewByDefault() throws JSONException {
        refreshListviewByDefault("stableReleasesVersionsSourceURL",4);
    }

    public void refreshSnapshotPreviewListviewByDefault() throws JSONException {
        refreshListviewByDefault("snapshotPreviewVersionsSourceURL",5);
    }


    public void refreshBetaListviewByDefault() throws JSONException {
        refreshListviewByDefault("betaVersionsSourceURL",6);
    }

    public void refreshAlphaListviewByDefault() throws JSONException {
        refreshListviewByDefault("alphaVersionsSourceURL",7);
    }


/*

    public void refreshSnapshotPreviewListviewByDefault() throws JSONException {
        String strJsondata = Utils.getJsonByAssets(this, "appURLs.json");
        JSONObject jsonObject = new JSONObject(strJsondata);
        String versionsSourceURL = jsonObject.getString("snapshotPreviewVersionsSourceURL");

        final String fileName=versionsSourceURL.substring(versionsSourceURL.lastIndexOf("/") + 1);
        Utils.downloadFile(this, versionsSourceURL, fileName, Utils.getDataFilesPath(this));
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (Utils.fileIsExists(MainActivity.this, Utils.getDataFilesPath(MainActivity.this)+"/"+fileName) == true) {
                    try {
                        handler.sendEmptyMessage(4);
                    } catch (Exception e) {
                        Looper.prepare();
                        Utils.exceptionDialog(MainActivity.this,e,getResources().getString(R.string.dialog_exception_initlistviewfailed));
                        Looper.loop();
                    }
                    timer.cancel();
                }else{

                }
            }
        }, 0, 100);
    }



    public void refreshBetaListviewByDefault() throws JSONException {
        String strJsondata = Utils.getJsonByAssets(this, "appURLs.json");
        JSONObject jsonObject = new JSONObject(strJsondata);
        String versionsSourceURL = jsonObject.getString("betaVersionsSourceURL");

        final String fileName=versionsSourceURL.substring(versionsSourceURL.lastIndexOf("/") + 1);
        Utils.downloadFile(this, versionsSourceURL, fileName, Utils.getDataFilesPath(this));
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (Utils.fileIsExists(MainActivity.this, Utils.getDataFilesPath(MainActivity.this)+"/"+fileName) == true) {
                    try {
                        handler.sendEmptyMessage(4);
                    } catch (Exception e) {
                        Looper.prepare();
                        Utils.exceptionDialog(MainActivity.this,e,getResources().getString(R.string.dialog_exception_initlistviewfailed));
                        Looper.loop();
                    }
                    timer.cancel();
                }else{

                }
            }
        }, 0, 100);
    }
*/

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch(msg.what){
                case 0:
                    try {
                        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);

                        final String getString=sharedPreferences.getString("input_stable_releases_versions_source",null);

                        final String fileNameOfInputerVersionsSourceGetText = getString.substring(getString.lastIndexOf("/") + 1);

                        StableReleases.versionsAdapter.clear();
                        StableReleases.versionsAdapter.addAll(StableReleases.versions);
                        Utils.initListview(MainActivity.this, StableReleases.versions, Utils.getDataFilesPath(MainActivity.this) + "/" + fileNameOfInputerVersionsSourceGetText, "minecraftStableReleases");
                        StableReleases.versionsAdapter.notifyDataSetChanged();
                    } catch (JSONException E) {
                        try {
                            refreshStableReleasesListviewByDefault();
                        } catch (JSONException exe) {
                            Utils.exceptionDialog(MainActivity.this, exe, getResources().getString(R.string.dialog_exception_getjsonorinitlistviewfailed));
                        }
                        Utils.exceptionDialog(MainActivity.this, E, getResources().getString(R.string.dialog_exception_getjsonorinitlistviewfailed));
                    }


                    //StableReleases.versionsAdapter.notifyDataSetChanged(); //发送消息通知ListView更新
                    //StableReleases.lis.setAdapter(StableReleases.versionsAdapter); // 重新设置ListView的数据适配器
                    break;
                case 1:
                    try {
                        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);

                        final String getString=sharedPreferences.getString("input_snapshot_preview_versions_source",null);

                        final String fileNameOfInputerVersionsSourceGetText = getString.substring(getString.lastIndexOf("/") + 1);

                        SnapshotPreview.versionsAdapter.clear();
                        SnapshotPreview.versionsAdapter.addAll(SnapshotPreview.versions);
                        Utils.initListview(MainActivity.this, SnapshotPreview.versions, Utils.getDataFilesPath(MainActivity.this) + "/" + fileNameOfInputerVersionsSourceGetText, "minecraftSnapshotPreview");
                        SnapshotPreview.versionsAdapter.notifyDataSetChanged();
                    } catch (JSONException E) {
                        try {
                            refreshSnapshotPreviewListviewByDefault();
                        } catch (Exception exe) {
                            Utils.exceptionDialog(MainActivity.this, exe, getResources().getString(R.string.dialog_exception_getjsonorinitlistviewfailed));
                        }
                        Utils.exceptionDialog(MainActivity.this, E, getResources().getString(R.string.dialog_exception_getjsonorinitlistviewfailed));
                    }
                    break;
                case 2:
                    try {
                        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);

                        final String getString=sharedPreferences.getString("input_beta_versions_source",null);

                        final String fileNameOfInputerVersionsSourceGetText = getString.substring(getString.lastIndexOf("/") + 1);

                        Beta.versionsAdapter.clear();
                        Beta.versionsAdapter.addAll(Beta.versions);
                        Utils.initListview(MainActivity.this, Beta.versions, Utils.getDataFilesPath(MainActivity.this) + "/" + fileNameOfInputerVersionsSourceGetText, "minecraftBeta");
                        Beta.versionsAdapter.notifyDataSetChanged();
                    } catch (JSONException E) {
                        try {
                            refreshBetaListviewByDefault();
                        } catch (Exception exe) {
                            Utils.exceptionDialog(MainActivity.this, exe, getResources().getString(R.string.dialog_exception_getjsonorinitlistviewfailed));
                        }
                        Utils.exceptionDialog(MainActivity.this, E, getResources().getString(R.string.dialog_exception_getjsonorinitlistviewfailed));
                    }
                    break;
                case 3:
                    try {
                        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);

                        final String getString=sharedPreferences.getString("input_alpha_versions_source",null);

                        final String fileNameOfInputerVersionsSourceGetText = getString.substring(getString.lastIndexOf("/") + 1);

                        Alpha.versionsAdapter.clear();
                        Alpha.versionsAdapter.addAll(Alpha.versions);
                        Utils.initListview(MainActivity.this, Alpha.versions, Utils.getDataFilesPath(MainActivity.this) + "/" + fileNameOfInputerVersionsSourceGetText, "minecraftAlpha");
                        Alpha.versionsAdapter.notifyDataSetChanged();
                    } catch (JSONException E) {
                        try {
                            refreshAlphaListviewByDefault();
                        } catch (Exception exe) {
                            Utils.exceptionDialog(MainActivity.this, exe, getResources().getString(R.string.dialog_exception_getjsonorinitlistviewfailed));
                        }
                        Utils.exceptionDialog(MainActivity.this, E, getResources().getString(R.string.dialog_exception_getjsonorinitlistviewfailed));
                    }
                case 4:
                    try {
                        String strJsondata = Utils.getJsonByAssets(MainActivity.this, "appURLs.json");
                        JSONObject jsonObject = new JSONObject(strJsondata);
                        String versionsSourceURL = jsonObject.getString("stableReleasesVersionsSourceURL");

                        final String fileName=versionsSourceURL.substring(versionsSourceURL.lastIndexOf("/") + 1);

                        StableReleases.versionsAdapter.clear();
                        StableReleases.versionsAdapter.addAll(StableReleases.versions);
                        Utils.initListview(MainActivity.this, StableReleases.versions, Utils.getDataFilesPath(MainActivity.this) + "/" + fileName, "minecraftStableReleases");
                        StableReleases.versionsAdapter.notifyDataSetChanged();
                    } catch (JSONException E) {
                        try {
                            refreshStableReleasesListviewByDefault();
                        } catch (JSONException exe) {
                            Utils.exceptionDialog(MainActivity.this, exe, getResources().getString(R.string.dialog_exception_getjsonorinitlistviewfailed));
                        }
                        Utils.exceptionDialog(MainActivity.this, E, getResources().getString(R.string.dialog_exception_getjsonorinitlistviewfailed));
                    }

                case 5:
                    try {
                        String strJsondata = Utils.getJsonByAssets(MainActivity.this, "appURLs.json");
                        JSONObject jsonObject = new JSONObject(strJsondata);
                        String versionsSourceURL = jsonObject.getString("snapshotPreviewVersionsSourceURL");

                        final String fileName=versionsSourceURL.substring(versionsSourceURL.lastIndexOf("/") + 1);

                        SnapshotPreview.versionsAdapter.clear();
                        SnapshotPreview.versionsAdapter.addAll(SnapshotPreview.versions);
                        Utils.initListview(MainActivity.this, SnapshotPreview.versions, Utils.getDataFilesPath(MainActivity.this) + "/" + fileName, "minecraftSnapshotPreview");
                        SnapshotPreview.versionsAdapter.notifyDataSetChanged();
                    } catch (JSONException E) {
                        try {
                            refreshSnapshotPreviewListviewByDefault();
                        } catch (JSONException exe) {
                            Utils.exceptionDialog(MainActivity.this, exe, getResources().getString(R.string.dialog_exception_getjsonorinitlistviewfailed));
                        }
                        Utils.exceptionDialog(MainActivity.this, E, getResources().getString(R.string.dialog_exception_getjsonorinitlistviewfailed));
                    }
                    break;

                case 6:
                    try {
                        String strJsondata = Utils.getJsonByAssets(MainActivity.this, "appURLs.json");
                        JSONObject jsonObject = new JSONObject(strJsondata);
                        String versionsSourceURL = jsonObject.getString("betaVersionsSourceURL");

                        final String fileName=versionsSourceURL.substring(versionsSourceURL.lastIndexOf("/") + 1);

                        Beta.versionsAdapter.clear();
                        Beta.versionsAdapter.addAll(Beta.versions);
                        Utils.initListview(MainActivity.this, Beta.versions, Utils.getDataFilesPath(MainActivity.this) + "/" + fileName, "minecraftBeta");
                        Beta.versionsAdapter.notifyDataSetChanged();
                    } catch (JSONException E) {
                        try {
                            refreshBetaListviewByDefault();
                        } catch (JSONException exe) {
                            Utils.exceptionDialog(MainActivity.this, exe, getResources().getString(R.string.dialog_exception_getjsonorinitlistviewfailed));
                        }
                        Utils.exceptionDialog(MainActivity.this, E, getResources().getString(R.string.dialog_exception_getjsonorinitlistviewfailed));
                    }
                    break;

                case 7:
                    try {
                        String strJsondata = Utils.getJsonByAssets(MainActivity.this, "appURLs.json");
                        JSONObject jsonObject = new JSONObject(strJsondata);
                        String versionsSourceURL = jsonObject.getString("alphaVersionsSourceURL");

                        final String fileName=versionsSourceURL.substring(versionsSourceURL.lastIndexOf("/") + 1);

                        Alpha.versionsAdapter.clear();
                        Alpha.versionsAdapter.addAll(Alpha.versions);
                            Utils.initListview(MainActivity.this, Alpha.versions, Utils.getDataFilesPath(MainActivity.this) + "/" + fileName, "minecraftAlpha");
                        Alpha.versionsAdapter.notifyDataSetChanged();
                    } catch (JSONException E) {
                        try {
                            refreshAlphaListviewByDefault();
                        } catch (JSONException exe) {
                            Utils.exceptionDialog(MainActivity.this, exe, getResources().getString(R.string.dialog_exception_getjsonorinitlistviewfailed));
                        }
                        Utils.exceptionDialog(MainActivity.this, E, getResources().getString(R.string.dialog_exception_getjsonorinitlistviewfailed));
                    }
                    break;
                    //do something
            }
        }
    };
}
