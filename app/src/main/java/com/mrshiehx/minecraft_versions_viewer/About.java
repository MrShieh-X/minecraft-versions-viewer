package com.mrshiehx.minecraft_versions_viewer;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;


public class About extends Activity implements AdapterView.OnItemClickListener {

    private ListView listView;
    private ArrayAdapter<String> listViewAdapter;
    TextView author;
    TextView copyright;
    TextView copyrightAndAllRightsReservedForChinese;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.initialization(this, R.string.activity_about_name);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.about);
        author = findViewById(R.id.author);
        copyright = findViewById(R.id.copyright);
        copyrightAndAllRightsReservedForChinese = findViewById(R.id.copyrightAndAllRightsReservedForChinese);
        try {
            String strJsondata = Utils.getJsonByAssets(this, "appURLs.json");
            JSONObject jsonObject = new JSONObject(strJsondata);
            String format = getResources().getString(R.string.text_layout_about_textview_author);
            String formatForCopyright = getResources().getString(R.string.text_layout_about_textview_copyright);
            String formatForCcopyrightAndAllRightsReservedForChinese = getResources().getString(R.string.text_layout_about_textview_copyrightForChinese);

            String authorText = jsonObject.getString("author");
            String authorTextviewText = String.format(format, authorText);
            String copyrightTextviewText = String.format(formatForCopyright, authorText);
            String copyrightAndAllRightsReservedForChineseTextviewText = String.format(formatForCcopyrightAndAllRightsReservedForChinese, authorText);
            author.setText(authorTextviewText);
            copyright.setText(copyrightTextviewText);
            copyrightAndAllRightsReservedForChinese.setText(copyrightAndAllRightsReservedForChineseTextviewText);
        } catch (JSONException e) {
            Utils.exceptionDialog(this, e, getResources().getString(R.string.dialog_exception_getjsonfailed));
        }
        //Utils.downloadFile(this,"https://gitee.com/MrShiehX/Repository/raw/master/mcvv/appURLs.json","appURLs.json",Utils.getDataFilesPath(this));

        //appName.setText(getResources().getString(R.string.app_name));

        TextView appVersionNameAndCode;
        appVersionNameAndCode = findViewById(R.id.versionNameAndCode);
        appVersionNameAndCode.setText(getResources().getString(R.string.text_layout_about_textview_version_partical) + " " + Utils.getVersionName(this) + "(" + Utils.getVersionCode(this) + ")");
        String strJsondata = Utils.getJsonByAssets(this, "appURLs.json");
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(strJsondata);
        } catch (JSONException e) {
            Utils.exceptionDialog(this, e, getResources().getString(R.string.dialog_exception_getjsonfailed));
        }
        String formatForContact = getResources().getString(R.string.text_layout_about_listview_users_can_do_item_contact);

        String formatForVisitMSXW = getResources().getString(R.string.text_layout_about_listview_users_can_do_item_visit_msxw);

        String formatForVisitAuthorGithub = getResources().getString(R.string.text_layout_about_listview_users_can_do_item_visit_author_github);

        String authorText = null;
        try {
            authorText = jsonObject.getString("author");
        } catch (JSONException e) {
            Utils.exceptionDialog(this, e, getResources().getString(R.string.dialog_exception_getjsonfailed));

        }
        String contactListitem = String.format(formatForContact, authorText);
        String visitMSXWListitem = String.format(formatForVisitMSXW, authorText);
        String visitAuthorGithubListitem = String.format(formatForVisitAuthorGithub, authorText);


        listView = (ListView) findViewById(R.id.usersCanDoListview);
        String[] arr_data = {
                contactListitem,
                getResources().getString(R.string.text_layout_about_listview_users_can_do_item_visit_mcversions_com),
                visitMSXWListitem,
                visitAuthorGithubListitem,
                getResources().getString(R.string.text_layout_about_listview_users_can_do_item_visit_github),
                getResources().getString(R.string.text_layout_about_listview_users_can_do_item_special_thanks)};// 创建的数据源
        listViewAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arr_data);
        listView.setAdapter(listViewAdapter);
        listView.setOnItemClickListener(this);
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


            /*String contactInformation = jsonObject.getString("contactInformation");
            String authorWebsiteURL = jsonObject.getString("authorWebsiteURL");
            String authorGithubURL = jsonObject.getString("authorGithubURL");
            String appGithubRepoURL = jsonObject.getString("appGithubRepoURL");
            String format = getResources().getString(R.string.text_layout_about_textview_author);

            String authorText = jsonObject.getString("author");
*/
        //String authorTextviewText= String.format(format ,authorText);

        switch (position) {
            case 0:
                String strJsondata1 = Utils.getJsonByAssets(this, "appURLs.json");
                JSONObject jsonObject1 = null;
                try {
                    jsonObject1 = new JSONObject(strJsondata1);
                    String contactInformation = jsonObject1.getString("contactInformation");
                    Utils.sendMail(this, contactInformation, "", "");
                } catch (JSONException e) {
                    Utils.exceptionDialog(this, e, getResources().getString(R.string.dialog_exception_getjsonfailed));
                }
                break;
            case 1:
                String strJsondata2 = Utils.getJsonByAssets(this, "appURLs.json");
                JSONObject jsonObject2 = null;
                try {
                    jsonObject2 = new JSONObject(strJsondata2);
                    String minecraftVersionsSupplierURL = jsonObject2.getString("minecraftVersionsSupplierURL");
                    Utils.goToWebsite(this, minecraftVersionsSupplierURL);
                } catch (JSONException e) {
                    Utils.exceptionDialog(this, e, getResources().getString(R.string.dialog_exception_getjsonfailed));
                }
                break;
            case 2:
                String strJsondata3 = Utils.getJsonByAssets(this, "appURLs.json");
                JSONObject jsonObject3 = null;
                try {
                    jsonObject3 = new JSONObject(strJsondata3);
                    String authorWebsiteURL = jsonObject3.getString("authorWebsiteURL");
                    Utils.goToWebsite(this, authorWebsiteURL);
                } catch (JSONException e) {
                    Utils.exceptionDialog(this, e, getResources().getString(R.string.dialog_exception_getjsonfailed));
                }
                break;
            case 3:
                String strJsondata4 = Utils.getJsonByAssets(this, "appURLs.json");
                JSONObject jsonObject4 = null;
                try {
                    jsonObject4 = new JSONObject(strJsondata4);
                    String authorGithubURL = jsonObject4.getString("authorGithubURL");
                    Utils.goToWebsite(this, authorGithubURL);
                } catch (JSONException e) {
                    Utils.exceptionDialog(this, e, getResources().getString(R.string.dialog_exception_getjsonfailed));
                }
                break;
            case 4:
                String strJsondata5 = Utils.getJsonByAssets(this, "appURLs.json");
                JSONObject jsonObject5 = null;
                try {
                    jsonObject5 = new JSONObject(strJsondata5);
                    String appGithubRepoURL = jsonObject5.getString("appGithubRepoURL");
                    Utils.goToWebsite(this, appGithubRepoURL);
                } catch (JSONException e) {
                    Utils.exceptionDialog(this, e, getResources().getString(R.string.dialog_exception_getjsonfailed));
                }
                break;
            case 5:
                Utils.showDialog(this, getResources().getString(R.string.dialog_special_thanks_title), getResources().getString(R.string.dialog_special_thanks_message));
                break;
        }
    }
}
