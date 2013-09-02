package com.example.cameratest;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import com.example.customview.NavigationBar;
import com.example.util.DataBaseHelper;
import com.example.util.GlobalUtil;

/**
 * Created with IntelliJ IDEA.
 * User: Appchina
 * Date: 13-9-2
 * Time: 下午3:38
 * T
 */
public class LoginActivity extends Activity {
    public static final  String TAG = "LoginActivity";

    NavigationBar nbar;                                            //导航条
    SharedPreferences sp;

    DataBaseHelper myDbHelper;                                  //数据库
    private void initDataBase() {
        myDbHelper = DataBaseHelper.getDataBaseHelper(LoginActivity.this);           //lxl获取数据库
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
         //btnLogin
        initUI();
        initDataBase();                            //初始化数据库
        initNavigationBar();
        initData();                               //获得当前用户的用户名数据
        initSpinner();
        setListener();
    }

    private void setListener() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent();
                intent.setClass(LoginActivity.this, FirstActivity.class);
                if (rdGroup.getCheckedRadioButtonId()==R.id.rbOldUser) {                                                               //老用户登陆
                    userId = myDbHelper.getUID(spOldUser.getSelectedItem().toString());
                    intent.putExtra("userid", userId);                                                    //传入用户ID，在下一个activity读取用户的配置
                    sp.edit().putString("userData", spOldUser.getSelectedItem().toString()).commit();     //存储当前用户的用户名，下次载入时取用
                }else if(rdGroup.getCheckedRadioButtonId()==R.id.rbNewUser){                                                                                    //新用户登陆
                    String newUser =textNewUser.getText().toString().trim();
                    if(newUser.equals("")){
                        privateToast("请输入用户名（不能为空）");
                        return;
                    }else{
                        creatNewUser(newUser);                                                            //创建一个新用户
                        sp.edit().putString("userData", newUser).commit();                                //存储当前用户的用户名，下次载入时取用
                        intent.putExtra("userid", userId);
                    }
                }
                startActivity(intent);
            }
        });
        setGroupListener(rdGroup);
        rdGroup.clearCheck();
    }

    private void creatNewUser(String newUser) {
        //生成新的用户id
        String newUserId = GlobalUtil.getId();
        userId = newUserId;
        //生成新的用户root_category
        String newUserCategory = GlobalUtil.getId();
        myDbHelper.createNewUser(newUser,newUserId,newUserCategory);
    }

    private void privateToast(String messg){
        Toast.makeText(LoginActivity.this,messg,Toast.LENGTH_SHORT).show();
    }

    private void initData() {
        sp = getSharedPreferences("xiaoyudi", 0);
        spidate = myDbHelper.getName();           //获得当前有的用户名
    }

    private String[] spidate;
    private ArrayAdapter<String> adapter;
    private String userId;

    private void initSpinner() {
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,spidate);
        //设置下拉列表的风格
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //将adapter 添加到spinner中
        spOldUser.setAdapter(adapter);
        int i=0;
        for(;i<spidate.length;i++){
            if(spidate[i].equals(sp.getString("userData","默认用户"))){
                spOldUser.setSelection(i);
            }
        }
    }

    Intent intent;
    RadioButton newUser;
    RadioButton oldUser;
    Button btnLogin;
    EditText textNewUser;
    Spinner spOldUser;
    RadioGroup rdGroup;
    LinearLayout ll;
    private void initUI() {
        btnLogin = (Button)findViewById(R.id.btnLogin);
        nbar = (NavigationBar)findViewById(R.id.navigationBar_Login);
        newUser =(RadioButton)findViewById(R.id.rbNewUser);
        oldUser =(RadioButton)findViewById(R.id.rbOldUser);

        rdGroup = (RadioGroup)findViewById(R.id.radioGroup);
        spOldUser =(Spinner)findViewById(R.id.spOldUserName);
        textNewUser = (EditText)findViewById(R.id.newUserName);
        ll = (LinearLayout)findViewById(R.id.lLayout);
    }
    private void setGroupListener(RadioGroup rd){
         rd.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
             @Override
             public void onCheckedChanged(RadioGroup group, int checkedId) {
                 if(checkedId == R.id.rbNewUser){                  //点击了新用户
                     spOldUser.setEnabled(false);
                     textNewUser.setEnabled(true);
                 }else{                                            //点击了老用户
                     spOldUser.setEnabled(true);
                     textNewUser.setEnabled(false);
                 }
             }
         });
    }
    public void initNavigationBar() {
        nbar.setTvTitle("小雨滴");
        nbar.setBtnLeftVisble(false);
        nbar.setBtnRightVisble(false);
    }
}
