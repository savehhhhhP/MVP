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
 * Time: ����3:38
 * T
 */
public class LoginActivity extends Activity {
    public static final  String TAG = "LoginActivity";

    NavigationBar nbar;                                            //������
    SharedPreferences sp;

    DataBaseHelper myDbHelper;                                  //���ݿ�
    private void initDataBase() {
        myDbHelper = DataBaseHelper.getDataBaseHelper(LoginActivity.this);           //lxl��ȡ���ݿ�
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
         //btnLogin
        initUI();
        initDataBase();                            //��ʼ�����ݿ�
        initNavigationBar();
        initData();                               //��õ�ǰ�û����û�������
        initSpinner();
        setListener();
    }

    private void setListener() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent();
                intent.setClass(LoginActivity.this, FirstActivity.class);
                if (rdGroup.getCheckedRadioButtonId()==R.id.rbOldUser) {                                                               //���û���½
                    userId = myDbHelper.getUID(spOldUser.getSelectedItem().toString());
                    intent.putExtra("userid", userId);                                                    //�����û�ID������һ��activity��ȡ�û�������
                    sp.edit().putString("userData", spOldUser.getSelectedItem().toString()).commit();     //�洢��ǰ�û����û������´�����ʱȡ��
                }else if(rdGroup.getCheckedRadioButtonId()==R.id.rbNewUser){                                                                                    //���û���½
                    String newUser =textNewUser.getText().toString().trim();
                    if(newUser.equals("")){
                        privateToast("�������û���������Ϊ�գ�");
                        return;
                    }else{
                        creatNewUser(newUser);                                                            //����һ�����û�
                        sp.edit().putString("userData", newUser).commit();                                //�洢��ǰ�û����û������´�����ʱȡ��
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
        //�����µ��û�id
        String newUserId = GlobalUtil.getId();
        userId = newUserId;
        //�����µ��û�root_category
        String newUserCategory = GlobalUtil.getId();
        myDbHelper.createNewUser(newUser,newUserId,newUserCategory);
    }

    private void privateToast(String messg){
        Toast.makeText(LoginActivity.this,messg,Toast.LENGTH_SHORT).show();
    }

    private void initData() {
        sp = getSharedPreferences("xiaoyudi", 0);
        spidate = myDbHelper.getName();           //��õ�ǰ�е��û���
    }

    private String[] spidate;
    private ArrayAdapter<String> adapter;
    private String userId;

    private void initSpinner() {
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,spidate);
        //���������б�ķ��
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //��adapter ��ӵ�spinner��
        spOldUser.setAdapter(adapter);
        int i=0;
        for(;i<spidate.length;i++){
            if(spidate[i].equals(sp.getString("userData","Ĭ���û�"))){
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
                 if(checkedId == R.id.rbNewUser){                  //��������û�
                     spOldUser.setEnabled(false);
                     textNewUser.setEnabled(true);
                 }else{                                            //��������û�
                     spOldUser.setEnabled(true);
                     textNewUser.setEnabled(false);
                 }
             }
         });
    }
    public void initNavigationBar() {
        nbar.setTvTitle("С���");
        nbar.setBtnLeftVisble(false);
        nbar.setBtnRightVisble(false);
    }
}
