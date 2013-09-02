package com.example.cameratest;

import android.app.TabActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import android.widget.TabHost;
import android.widget.TabWidget;
import com.example.customview.NavigationBar;
import com.example.util.Constants;
import com.example.util.DataBaseHelper;
import com.umeng.analytics.MobclickAgent;

public class CoverCardActivity extends TabActivity {

    NavigationBar nb;
    ListView lv;
    ListView catLv;
    Cursor datasource4card;
    Cursor datasource4cato;
    //	Ԥ���滻�� λ�õ� �������   ����ҳ���е����λ��position  ��parent
    int replacePosition;
    String parent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cover1);
        Intent intent = getIntent();
        replacePosition = intent.getIntExtra("position", 0);
        parent = intent.getStringExtra("parent");
        init();
        initTab();
    }

    private TabWidget tabWidget;
    private TabHost tabHost;
    private void initTab() {
        //��ȡ��TabHost���
        tabHost = getTabHost();
        TabHost.TabSpec tab1 = tabHost.newTabSpec("tab1")
                .setIndicator("��Ƭ")
                .setContent(R.id.tabCard);
        TabHost.TabSpec tab2 = tabHost.newTabSpec("tab2")
                .setIndicator("Ŀ¼")
                .setContent(R.id.tabCategory);
        tabWidget = getTabWidget();
        //��ӱ�ǩҳ
        tabHost.addTab(tab1);
        tabHost.addTab(tab2);

        for (int i =0; i <tabWidget.getChildCount(); i++) {

            View vvv = tabWidget.getChildAt(i);
            if(tabHost.getCurrentTab()==i){
                vvv.setBackgroundDrawable(getResources().getDrawable(R.drawable.focus));
            }
            else {
                vvv.setBackgroundDrawable(getResources().getDrawable(R.drawable.unfocus));
            }
        }

        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener(){

            @Override
            public void onTabChanged(String tabId) {
                for (int i =0; i < tabWidget.getChildCount(); i++) {
                    View vvv = tabWidget.getChildAt(i);
                    if(tabHost.getCurrentTab()==i){
                        vvv.setBackgroundDrawable(getResources().getDrawable(R.drawable.focus));
                    }
                    else {
                        vvv.setBackgroundDrawable(getResources().getDrawable(R.drawable.unfocus));
                    }
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    public void init() {
        setupViews();
        initNavigationBar();
    }

    public void initNavigationBar() {
        nb.setTvTitle("�滻");
        nb.setBtnRightVisble(false);

        nb.setBtnLeftBacground(R.drawable.ic_back);
        nb.setBtnLeftClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    DataBaseHelper myDbHelper;

    public void setupViews() {
        nb = (NavigationBar) findViewById(R.id.nb_edit);
        lv = (ListView) findViewById(R.id.listView);
        catLv = (ListView) findViewById(R.id.listView2);
        myDbHelper = DataBaseHelper.getDataBaseHelper(CoverCardActivity.this);
        datasource4card = myDbHelper.getDataSource(Constants.TYPE_CARD);                           //���ؿ�Ƭ��Ϣ
        SimpleCursorAdapter adapter4card = new SimpleCursorAdapter(CoverCardActivity.this, R.layout.listitem, datasource4card, new String[]{"name"}, new int[]{R.id.listitem});
        lv.setAdapter(adapter4card);

        datasource4cato = myDbHelper.getDataSource(Constants.TYPE_CATEGORY);                       //����Ŀ¼��Ϣ
        SimpleCursorAdapter adapter4cato = new SimpleCursorAdapter(CoverCardActivity.this, R.layout.listitem, datasource4cato, new String[]{"name"}, new int[]{R.id.listitem});
        catLv.setAdapter(adapter4cato);

        lv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int location, long id) {
                replayBack(datasource4card, location, 3);
            }
        });

        catLv.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int location, long id) {
                replayBack(datasource4cato, location, 3);
            }
        });
    }

    public void replayBack(Cursor datasource, int location, int resultCode) {
        Cursor c = datasource;
        if (c.moveToPosition(location)) {
            String _id = c.getString(c.getColumnIndex("_id"));
            String cardname = c.getString(c.getColumnIndex("name"));
            String type = c.getString(c.getColumnIndex("type"));
            String image = c.getString(c.getColumnIndex("image"));
            String audio = c.getString(c.getColumnIndex("audio"));
            Intent data = new Intent();
//			��Ҫ���⽫image��idת��Ϊ·����Ϣ
            data.putExtra("image", image);
            data.putExtra("type", type);
            data.putExtra("name", cardname);
            data.putExtra("audio", audio);
            data.putExtra("_id", _id);
            data.putExtra("position", replacePosition);
            data.putExtra("parent",parent);
            //myDbHelper.insertIntoCard_tree(_id, parent, replacePosition);  �ύ�����ݿ�ɾ����firstActivity
//			����ǰһ��ҳ�洫�ݹ�����   position parent ������¼
            setResult(resultCode, data);
            Log.i("lxl", "���ڸ������ݿ� ����2*8ҳ��");
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_edit, menu);
        return true;
    }

}
