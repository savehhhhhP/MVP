package com.example.cameratest;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.*;

import com.example.customview.HelpDialog;
import com.example.customview.NavigationBar;
import com.example.po.Card;
import com.example.util.*;
import com.umeng.analytics.MobclickAgent;

public class FirstActivity extends Activity {
    public static final  String TAG = "FirstActivity";
    private static final int REQUEST_COVER = 10;


    NavigationBar nb;                                            //������
    ImageView iv1;
    ImageView iv2;
    ImageView iv3;
    ImageView iv4;
    ImageView iv5;
    ImageView iv6;
    ImageView iv1_t;
    ImageView iv2_t;
    ImageView iv3_t;
    ImageView iv4_t;
    ImageView iv5_t;
    ImageView iv6_t;


    TextView tv1;
    TextView tv2;
    TextView tv3;
    TextView tv4;
    TextView tv5;
    TextView tv6;
    Map<Integer, Card> cardMap;
    static useCardServer cardUsedserver=null;

    String parent;

    MyHandler myHandler;

    List<ImageView> ivList;
    List<ImageView> ivList_t;
    List<TextView> tvList;

    SharedPreferences sp;
    boolean firstTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first1);
        init();            //lxlϵͳ��ʼ��         �����Ƿ��һ�����������ж�
        initDataBase();    //��ʼ�����ݿ�
        initUI();          //lxl�����ʼ��
        initDir();         //lxlӦ�õ�һ��������ʱ��  ��ʼ�� ������Ҫ��·���ļ���      YY PIC
        initCards();
//		���� �Ѿ������ݿⷽ�����˸Ľ�  ��һ��������ʱ��� ��APK�е�DB�ļ������û������ݿ�  ���� ��ʱ���������ж�  �Ƿ��ǵ�һ�������������ݵĳ�ʼ������
//		if(!firstTime){initData();} 
    }
    DataBaseHelper myDbHelper;                                  //���ݿ�
    private void initDataBase() {
        myDbHelper = DataBaseHelper.getDataBaseHelper(FirstActivity.this);           //lxl��ȡ���ݿ�

    }

    public void init() {
        myHandler = new MyHandler();
        if(cardUsedserver==null){
            cardUsedserver = new useCardServer();
        }
        sp = getSharedPreferences("xiaoyudi", 0);
        firstTime = sp.getBoolean("firstTime", true);
        //��һ������ʱ������Դ���ֻ�XIAOYUDIĿ¼
        if(firstTime){
            images = this.getResources().getStringArray(R.array.images);
            audios = this.getResources().getStringArray(R.array.audios);
            try {
                String path_image = GlobalUtil.getExternalAbsolutePath(this) + "/" + "XIAOYUDI" + "/PIC/";
                String path_audio = GlobalUtil.getExternalAbsolutePath(this) + "/" + "XIAOYUDI" + "/YY/";
                copyAssets(images, path_image);  //������Դ ͼƬ
                copyAssets(audios, path_audio);  //������Դ ����
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initCards();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    public void initDir() {
        if (firstTime) {
            File dir_YY = new File(Constants.dir_path_yy);
            File dir_PIC = new File(Constants.dir_path_pic);
            if (!dir_YY.exists()) {
                dir_YY.mkdirs();                                       //lxl �ڴ��ڵ�Ŀ¼�д����ļ���  YY-����
            }
            if (!dir_PIC.exists()) {
                dir_PIC.mkdirs();                                      //PIC ͼƬ
            }
            Log.i(TAG, "·����ʼ���ɹ�");
        }
    }

    String[] images ;
    String[] audios ;

    public void initCards() {

        cardMap = myDbHelper.getChildsByParent(parent);                              //lxl���ݸ��ڵ�ȡ�ú��ӽڵ�����
//		begin ������ Ϊ���ڱ༭���淵�ص���ҳ���ʱ��  ˢ�³������ݲ�ͬ���� ��ʱ�������
        for (int i = 1; i < ivList.size(); i++) {
            ivList.get(i).setImageResource(R.drawable.ic_add);
        }
//		end by 2013 07 31
        Log.i(TAG, "parent id is :" + parent);
        if (cardMap != null) {
            Log.i(TAG, "cardlist.size is " + cardMap.size());
            Iterator it = cardMap.keySet().iterator();
            while (it.hasNext()) {
                Integer key = (Integer) it.next();
                Card cardItem = cardMap.get(key);
                cardUsedserver.addCard(cardItem,true);                                           //���ÿ�Ƭʹ��״̬Ϊ��ʹ��
                int position = cardItem.getPosition();
                Log.i(TAG, "��������ͼƬ");
                Bitmap mybitmap = GlobalUtil.preHandleImage(null, Constants.dir_path_pic + cardItem.getImage_filename());    //���ͼƬ�� bitmap ֮��ŵ�imageViewList  ��ѭ������������imageView����ͼƬ
                Log.i(TAG, "�������Դ");
                if(mybitmap!=null){
                    ivList.get(position).setImageBitmap(mybitmap);
                }
                Log.i(TAG,ivList.get(position).toString());
                if (cardItem.getType().equals(Constants.TYPE_CATEGORY)) {                            //lxl����Ŀ¼��һ��card�������ʽ
                    ivList_t.get(position).setImageResource(R.drawable.ic_category);
                } else {
                    ivList_t.get(position).setImageResource(R.drawable.ic_card);
                }
                tvList.get(position).setText(cardItem.getName());                                  //lxl��������
            }
        }
        sp.edit().putBoolean("firstTime", false).commit();
    }


    public void copyAssets(String[] resources, String path) throws IOException {

        for (int i = 0; i < resources.length; i++) {
            String outFileName = path + resources[i];
            File file = new File(outFileName);
            if (!file.exists()) {
                InputStream myInput = getAssets().open(resources[i]);
                OutputStream myOutput = new FileOutputStream(outFileName);
                byte[] buffer = new byte[1024];
                int length;
                while ((length = myInput.read(buffer)) > 0) {
                    myOutput.write(buffer, 0, length);
                }
                myOutput.flush();
                myOutput.close();
                myInput.close();
                Log.i(TAG, "��Դ�ļ���ʼ�����");
            }
        }
    }

    Intent intent;

    public void setOnClickListener() {                                //lxl���ò�ͬͼƬ�ĵ����ת�� ͬһ��Edit2Activity���Ǵ��벻ͬ��positionֵ��������ڣ����벻ͬ��parent�����ֲ�ͬ�ĸ��ڵ�
        for (int i = 0; i < ivList.size(); i++) {
            intent = new Intent();
            intent.putExtra("position", i);
            intent.setClass(FirstActivity.this, CoverCardActivity.class);
            intent.putExtra("parent", parent);
            if (!isLauchPage && i == 0) {                                  //lxl ��Ŀ¼�����κ��£�����Ŀ¼����ӳ�����Ӧ

            } else {
                ivList.get(i).setOnLongClickListener(new ImageViewLongClickListener(intent));
            }
            ivList.get(i).setOnClickListener(new ImageViewListener(i));    //lxl�˴���Ӱ�����Ӧ
        }
    }

    public Builder getADialog() {
        AlertDialog.Builder builder = new Builder(FirstActivity.this);
        //����
        builder.setTitle("ע�⣡");
        builder.setMessage("�������е�ͬ�����ᶪʧ��ǰ���ݣ�ȷ��ͬ����");
        builder.setPositiveButton("ȷ��",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                for(int i=0;i<ivList.size();i++){
//                    ivList.get(i).setImageBitmap(null);
//                }
                DataSyn.delFile(Constants.dir_path_pic);
                DataSyn.delFile(Constants.dir_path_yy);
                goSyn();
                Log.i(TAG,"ѡ����ȷ��");
            }
        });
        builder.setNegativeButton("ȡ��",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Log.i(TAG,"ѡ����ȡ��");
            }
        });
        return builder;
    }


    /**
     * Ŀ¼��ͼƬ����Ӧ�¼�
     */
    public class ImageViewListener implements OnClickListener {
        int position;

        public ImageViewListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            if (!isLauchPage && position == 0) {                                                //���Ǹ��ڵ㣨�����棩
                finish();
            } else {
                Card cardItem = cardMap.get(position);
//					�˴������� һ�����   ���磺 cardMap��size�� 3  ���ǵ����λ����2 ����ϵ�һ���ж�  ���� ʵ���ϵ����λ���ǿյ�
                if (cardItem != null) {
                    if (Constants.TYPE_CATEGORY.equals(cardMap.get(position).getType())) {
                        Intent intent = new Intent();
                        intent.putExtra("isLauchPage", false);
                        intent.putExtra("name", cardMap.get(position).getName());
                        intent.putExtra("parent", cardMap.get(position).getId());
                        intent.setClass(FirstActivity.this, FirstActivity.class);
                        startActivity(intent);
                    } else {
                        ivList.get(position).startAnimation(anim);
                        ivList_t.get(position).startAnimation(anim);
                        tvList.get(position).startAnimation(anim);
                        //tr1.startAnimation(anim);
                        Toast.makeText(FirstActivity.this, getString(R.string.FirstActivity_msg_cover), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(FirstActivity.this, getString(R.string.FirstActivity_msg_cover), Toast.LENGTH_SHORT).show();
                }
            }
//			}
        }
    }

    public class ImageViewLongClickListener implements OnLongClickListener {

        Intent intent;

        public ImageViewLongClickListener(Intent intent) {
            this.intent = intent;
        }

        @Override
        public boolean onLongClick(View v) {
//			��Ҫ���⴫���ڶ�������  ��ǰҳ���Ѿ��еĲ��� �Ͳ�Ҫ���û������ظ�ѡ����.. by sjl 2013 0726
            Log.i(TAG, "����¼��õ���position:" + intent.getIntExtra("position", 0));
            startActivityForResult(intent, REQUEST_COVER);
            return false;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_COVER) {                                              //��ѡȡ�滻��ҳ�淵��
            if (resultCode == 3) {
                int position = data.getIntExtra("position", 0);
                String name = data.getStringExtra("name");
                String image = data.getStringExtra("image");
                String audio = data.getStringExtra("audio");
                String _id = data.getStringExtra("_id");
                String type = data.getStringExtra("type");
                String parent_ = data.getStringExtra("parent");
                Card card = new Card();
                card.setName(name);
                card.setAudio(audio);
                card.setId(_id);
                card.setImage(image);
                card.setPosition(position);
                card.setType(type);
                //�˴�����˹��� ֻ�����һ��Ŀ¼���޸��ظ����Ŀ¼��ɵ�ѭ���쳣
                if(cardUsedserver.isCardUsed(card) && type.equals("category")){           //Ŀ¼�Ѿ���ӹ����ܾ����
                    Toast.makeText(FirstActivity.this,"��ѡ��ķ����Ѿ���ӹ��������ظ���ӣ�",Toast.LENGTH_LONG).show();
                    return;
                }
                cardUsedserver.addCard(card,true);                       //�����滻��ƬΪ��ʹ��
                if(cardMap.get(position)!=null){                         //����û�п�Ƭ���ʱ�����nullpointer�쳣
                    cardUsedserver.setCardUsed(cardMap.get(position),false);                            //���ñ��滻��ƬΪδʹ��
                }
                //----------------------------------------------

//				����ѡ���CARD�滻ԭ�е�Card
                cardMap.put(position, card);
                String filename = myDbHelper.queryFilename(image);
//				begin �����޸�  ѡȡͼƬ�����oom����   by sjl 2013 07 31
                Bitmap mybitmap = GlobalUtil.preHandleImage(ivList.get(position), Constants.dir_path_pic + filename);
                mybitmap = GlobalUtil.small(mybitmap);                        //lxl��ѡ���ͼƬ��������֮��ŵ���ѡ���Ҫ�滻��ͼƬ������
                ivList.get(position).setImageBitmap(mybitmap);
                mybitmap.recycle();
//				end
                Log.i(TAG, "������ȾͼƬ.." + position);
                if (type.equals(Constants.TYPE_CATEGORY)) {
                    setCategoryForResource(ivList_t,position,R.drawable.ic_category);
                } else {
                    setCategoryForResource(ivList_t, position, R.drawable.ic_card);
                }
                tvList.get(position).setText(name);
                myDbHelper.insertIntoCard_tree(_id,parent_, position);              //�滻�Ŀ�Ƭ�ύ�����ݿ�
            }
        }
    }

    private void setCategoryForResource(List<ImageView> iv,int position,int resId) {
        iv.get(position).setImageResource(resId);
    }

    int flag = 0;
    public void initNavigationBar() {
        nb.setTvTitle("С���");
        nb.setBtnRightClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new Builder(FirstActivity.this);
                builder.setTitle("����").setItems(new String[]{"�༭��Ƭ/Ŀ¼","ʹ�ð���","ͬ������"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:             //�༭��Ƭ/Ŀ¼
                                Intent intent = new Intent();
                                intent.setClass(FirstActivity.this, EditActivity.class);
                                startActivity(intent);
                                break;
                            case 1:            //ʹ�ð���
                                HelpDialog help = new HelpDialog(FirstActivity.this,getResources().getDrawable(R.drawable.first_help));
                                help.show();
                                break;
                            case 2:
                                //intent.putExtra("type",Constants.TYPE_SYN);
                                //begin�˴�ʵ��ͬ������ 2013 8 13
                                AlertDialog.Builder builder = getADialog();
                                builder.create().show();
                                return;
                            //end  8 16
                            default:
                                break;
                        }
                    }
                }).setNegativeButton("����", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
            }
        });
        nb.setBtnLeftClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new Builder(FirstActivity.this);
                builder.setTitle("�½�").setItems(new String[]{"��Ƭ", "Ŀ¼"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent();
                        intent.setClass(FirstActivity.this, CreatCardActivity.class);
                        switch (which) {
                            case 0:
                                intent.putExtra("type", Constants.TYPE_CARD);
                                break;
                            case 1:
                                intent.putExtra("type", Constants.TYPE_CATEGORY);
                                break;
                            default:
                                break;
                        }
                        startActivity(intent);
                    }
                }).setNegativeButton("����", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();

            }
        });
    }

    private void goSyn() {
        DataSyn.getdb(myDbHelper);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    String synData;
                    synData = DataSyn.getJsonString(Constants.GET_SQL);        //�˴������SQL����
                    Log.i(TAG,"get sql");
                    DataSyn.formatSql(synData);                                //�������ݱ�
                    String mainifestData = DataSyn.getJsonString(Constants.GET_MAINIFEST); //���mainfest����
                    Log.i(TAG,"get mainfest");
                    Map<String,String> mapMain = DataSyn.getMainifest(mainifestData);     //����mainfest����
                    int i=0,length=0;
                    length=Integer.parseInt(mapMain.get("length"));                                      //��ȡ�ļ���Դ�ĸ���
                    Log.i(TAG,""+length);
                    for(;i<length;i++){
                        String fileKey = "id_"+i;
                        DataSyn.getResourceString(Constants.GET_FILE_PATH+mapMain.get(fileKey),myDbHelper.getFilename(mapMain.get(fileKey)),Constants.dir_path_pic);
                    }
                    Log.i(TAG,"get file");
                    //DataSyn.getResourceString(Constants.GET_FILE_PATH+mapMain.get("id_1"),myDbHelper.getFilename(mapMain.get("id_1")),Constants.dir_path_yy);

                    Message msg = new Message();
                    Bundle b = new Bundle();//�������
                    b.putString("msg", getString(R.string.FirstActivity_syn_msg_ok));
                    msg.setData(b);
                    FirstActivity.this.myHandler.sendMessage(msg);

                }catch(Exception e){
                    Log.i("syn1",e.toString()  + "error");
                    Message msg = new Message();
                    Bundle b = new Bundle();//�������
                    b.putString("msg", getString(R.string.FirstActivity_syn_msg_error));
                    msg.setData(b);
                    FirstActivity.this.myHandler.sendMessage(msg);
                }
            }
        }).start();
    }

    public void initNavigationBar2(String name) {                                       //�Ե������ĳ�ʼ��������ť�����ã�����ͼƬ������
        nb.setTvTitle(name);
        nb.setBtnLeftBacground(R.drawable.ic_back);
        nb.setBtnRightVisble(false);
        nb.setBtnLeftClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    SQLiteDatabase db;

    boolean isLauchPage;
    Animation anim;
    RelativeLayout rl1;
    TableRow tr1;

    public void initUI() {
        //������Դ��ȡ
        anim = AnimationUtils.loadAnimation(this, R.anim.anim);
        rl1 = (RelativeLayout)findViewById(R.id.rl1);
        tr1 = (TableRow)findViewById(R.id.tr1);
        ivList = new ArrayList<ImageView>();
        ivList_t = new ArrayList<ImageView>();
        tvList = new ArrayList<TextView>();
        nb = (NavigationBar) findViewById(R.id.navigationBar_edit);
        iv1 = (ImageView) findViewById(R.id.imageView1);
        iv2 = (ImageView) findViewById(R.id.imageView2);
        iv3 = (ImageView) findViewById(R.id.imageView3);
        iv4 = (ImageView) findViewById(R.id.imageView4);
        iv5 = (ImageView) findViewById(R.id.imageView5);
        iv6 = (ImageView) findViewById(R.id.imageView6);
        iv1_t = (ImageView) findViewById(R.id.imageView1_t);
        iv2_t = (ImageView) findViewById(R.id.imageView2_t);
        iv3_t = (ImageView) findViewById(R.id.imageView3_t);
        iv4_t = (ImageView) findViewById(R.id.imageView4_t);
        iv5_t = (ImageView) findViewById(R.id.imageView5_t);
        iv6_t = (ImageView) findViewById(R.id.imageView6_t);
        ivList.add(iv1);
        ivList.add(iv2);
        ivList.add(iv3);
        ivList.add(iv4);
        ivList.add(iv5);
        ivList.add(iv6);
        ivList_t.add(iv1_t);
        ivList_t.add(iv2_t);
        ivList_t.add(iv3_t);
        ivList_t.add(iv4_t);
        ivList_t.add(iv5_t);
        ivList_t.add(iv6_t);
        initTextView();                                                //��ʼ��textView   ���tvList�ĳ�Ա���
        Intent intent = getIntent();
        isLauchPage = intent.getBooleanExtra("isLauchPage", true);       //ȡ���Ƿ�ΪĿ¼�ڵ����Ϣ������������ͬ����
        if (isLauchPage) {
            initNavigationBar();
//			��ʼ��  ��ҳ���ڵ�  �����ݿ��ȡ
            parent =myDbHelper.getParent(getString(R.string.firstParent));
            Log.i(TAG,parent+ "");
        } else {
            iv1.setImageResource(R.drawable.ic_return);
            tv1.setText("����");
            parent = intent.getStringExtra("parent");                    //ȡ�ø��ڵ���Ϣ��
            initNavigationBar2(intent.getStringExtra("name"));
        }
        setOnClickListener();
    }

    public void initTextView() {
        tv1 = (TextView) findViewById(R.id.textView1);
        tv2 = (TextView) findViewById(R.id.textView2);
        tv3 = (TextView) findViewById(R.id.textView3);
        tv4 = (TextView) findViewById(R.id.textView4);
        tv5 = (TextView) findViewById(R.id.textView5);
        tv6 = (TextView) findViewById(R.id.textView6);
        tvList.add(tv1);
        tvList.add(tv2);
        tvList.add(tv3);
        tvList.add(tv4);
        tvList.add(tv5);
        tvList.add(tv6);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_first, menu);
        return true;
    }

    class MyHandler extends Handler {

        public MyHandler() {

        }

        public MyHandler(Looper L) {

            super(L);

        }

        //���������д�˷���,��������

        @Override

        public void handleMessage(Message msg) {

            // TODO Auto-generated method stub
            super.handleMessage(msg);

            //�˴����Ը���UI

            Bundle b = msg.getData();

            String error = b.getString("msg");
            Toast.makeText(FirstActivity.this,error,Toast.LENGTH_LONG).show();
        }

    }

}
