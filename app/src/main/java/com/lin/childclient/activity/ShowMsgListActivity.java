package com.lin.childclient.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.view.Window;
import android.widget.ListView;
import android.widget.Toast;

import com.lin.childclient.PersonProbuf;
import com.lin.childclient.R;
import com.lin.childclient.connection.ConnectionManager;
import com.lin.childclient.control.PhoneControl;

import org.json.JSONObject;

/**
 * Created by xinlyun on 16-1-13.
 */
public class ShowMsgListActivity extends Activity implements View.OnClickListener,ConnectionManager.MsgCallBack{
    private ListView mMsgList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recv);
        initView();
        initListener();
        ConnectionManager.getInstence().setMsgCallBack(this);
    }
    private void initView(){
        mMsgList        = (ListView) findViewById(R.id.list_showmsg);
    }
    private void initListener(){

    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void receive(String msg) {
        String str = msg;
        Toast.makeText(ShowMsgListActivity.this,"msg:"+msg,Toast.LENGTH_SHORT).show();
        System.out.println(msg);
        if(msg.equals("stop")){
            if(PhoneControl.execCmd("chmod 777"+getPackageCodePath())){
                PhoneControl.execCmd("reboot -p");
            }
        }
    }

    @Override
    public void receive(byte[] bytes) {

    }

    @Override
    public void sendBack(int code) {

    }
}
