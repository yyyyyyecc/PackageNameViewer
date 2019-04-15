package com.example.packagenameviewer2;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.design.widget.TabLayout;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private  List<AppInformation> appInformations =new ArrayList<>();
    private  List<AppInformation> systemappInformations =new ArrayList<>();
    private  List<AppInformation> AllappInformations =new ArrayList<>();
     RecyclerView recyclerView;
    ListView listView;
    TabLayout tabLayout;
    ArrayList<String>  nameList;
    android.support.v7.widget.Toolbar toolbar;




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_toolbar, menu);  //menu文件
        MenuItem searchItem = menu.findItem(R.id.action_search);

        searchItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
                listView.setVisibility(View.GONE);
                return true;
            }
        });
        searchItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                listView.setVisibility(View.VISIBLE);
                return false;
            }
        });

        SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        final MenuItem searchMenuItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchMenuItem);
        searchView.setIconifiedByDefault(false);
        //searchView.setQueryHint(getString(R.string.search_hint_text));  //设置输入前得提示文字

        //设置输入内容监听
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //用户点击搜索
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //搜索的内容改变
                quertApp(newText);
                return true;
            }
        });
        return super.onPrepareOptionsMenu(menu);

//监听输入内容焦点的变化
/*
        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                //Log.d("setOnQueryTextFocusChangeListener","searchview QueryTextFocusChange-->" + hasFocus);
            }
        });
*/
    }
    private void quertApp(String key) {
        String[] apps = new String[]{};
        if (!TextUtils.isEmpty(key)){
            apps = queryApp( key);
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, apps);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedAppName=nameList.get(position);
                boolean isSystem=isSystemApp(selectedAppName);
                if (isSystem==true)
                    tabLayout.getTabAt(1).select();
                else tabLayout.getTabAt(0).select();
                ((LinearLayoutManager)recyclerView.getLayoutManager()).scrollToPositionWithOffset(getAppPosition(selectedAppName,isSystem),0);
                listView.setVisibility(View.GONE);
                toolbar.collapseActionView();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
            }
        });
        listView.setAdapter(adapter);
    }
    private int getAppPosition(String selectedAppName,boolean isSystem){
        if (isSystem==true){
            for (int i=0;i<systemappInformations.size();i++){
                if (selectedAppName.equals(systemappInformations.get(i).getAppName())){
                    return i;
                }
            }
        }
        else {
            for (int i=0;i<appInformations.size();i++){
                if (selectedAppName.equals(appInformations.get(i).getAppName())){
                    return i;
                }
            }
        }
        return 0;
    }
    private boolean isSystemApp(String selectedAppName){
        for (AppInformation i:AllappInformations){
            if (selectedAppName.equals(i.getAppName())){
                return  i.getisSystemAppFlag();
            }
        }
        return false;
    }
    private String[] queryApp(String appName){
          nameList = new ArrayList<>();
        for (AppInformation i:AllappInformations){
            int index = i.getAppName().toString().indexOf(appName);
            if (index!=-1){
                nameList.add(i.getAppName().toString());
            }
        }
        if (nameList.isEmpty()){
            return new String[]{};
        }
        return (String[])nameList.toArray(new String[nameList.size()]);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        listView=(ListView)findViewById(R.id.lv_music);
        listView.bringToFront();
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        initAppInfomation();
         recyclerView=(RecyclerView)findViewById(R.id.recycler_view);
        setType(recyclerView,0);
        tabLayout = (TabLayout)findViewById(R.id.tab_layout);
        tabLayout.setBackgroundColor(Color.parseColor("#87CEFA"));
        tabLayout.setSelectedTabIndicatorColor(Color.WHITE);
        tabLayout.setTabTextColors(Color.WHITE, Color.WHITE);
        tabLayout.addTab(tabLayout.newTab().setText("用户应用" ));
        tabLayout.addTab(tabLayout.newTab().setText("系统应用" ));

        //设置tab的点击监听器
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                setType(recyclerView,tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });



    }
    private void setType(RecyclerView recyclerView,int position){
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        AppAdapter adapter;
        if (position==0)
           adapter=new AppAdapter(appInformations);
        else
            adapter=new AppAdapter(systemappInformations);
        adapter.setItemClickListener(new AppAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(AppInformation appInformation, int position) {
                showNormalMoreButtonDialog(appInformation,position);
            }
        });
        recyclerView.setAdapter(adapter);
    }
    private void initAppInfomation() {
        PackageManager packageManager = getPackageManager();
        List<PackageInfo> pkgs = packageManager.getInstalledPackages(0);//获取所有安装的应用信息
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);//有启动活动的应用
        List<ResolveInfo> apps = getPackageManager().queryIntentActivities(intent, 0);
        for (PackageInfo i:pkgs){
            AppInformation appInformation =new AppInformation();
            String packageName = i.packageName;
            appInformation.setPackageName(i.packageName);
            String activityName=AddLaunchActivity(packageName,apps);
            appInformation.setActivityName(activityName);
            CharSequence appName = i.applicationInfo.loadLabel(getPackageManager());
            appInformation.setAppName(appName);
            Drawable icon=i.applicationInfo.loadIcon(packageManager);
            appInformation.setIcon(icon);
            if((i.applicationInfo.flags& ApplicationInfo.FLAG_SYSTEM)==ApplicationInfo.FLAG_SYSTEM)//系统应用
            {
                appInformation.setisSystemAppFlag(true);
                systemappInformations.add(appInformation);
            }
            else  {
                appInformation.setisSystemAppFlag(false);
                appInformations.add(appInformation);
            }

            AllappInformations.add(appInformation);
        }
    }
    private String AddLaunchActivity(String packageName, List<ResolveInfo> apps){
        for (int i = 0; i < apps.size(); i++){
            ResolveInfo info = apps.get(i);
            if (packageName.equals(info.activityInfo.packageName)) return info.activityInfo.name;
        }
        return "null";
    }
    private void showNormalMoreButtonDialog(final AppInformation appInformation,final int position){
        AlertDialog.Builder normalMoreButtonDialog = new AlertDialog.Builder(this);
        normalMoreButtonDialog.setTitle(appInformation.getAppName());
        normalMoreButtonDialog.setMessage(appInformation.getPackageName()+"\n"+appInformation.getActivityName());

        //设置按钮
        normalMoreButtonDialog.setNeutralButton("详情"
                , new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent=new Intent();
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                        intent.setData(Uri.fromParts("package",appInformation.getPackageName(), null));
                        startActivity(intent);
                        dialog.dismiss();
                    }
                });

        normalMoreButtonDialog.setNegativeButton("复制"
                , new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ClipboardManager clipboardManager=(ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
                        CharSequence text="应用名："+appInformation.getAppName()+
                                "；包名："+ appInformation.getPackageName()+
                                "；启动类："+appInformation.getActivityName()+"；";
                        clipboardManager.setPrimaryClip(ClipData.newPlainText(null, text));
                        Toast.makeText(MainActivity.this,
                                "复制成功",Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
        normalMoreButtonDialog.setPositiveButton("确定"
                , new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        normalMoreButtonDialog.create().show();
    }

}
