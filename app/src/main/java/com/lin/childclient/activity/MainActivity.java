package com.lin.childclient.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.lin.childclient.R;
import com.lin.childclient.connection.ConnectionManager;


public class MainActivity extends Activity implements View.OnClickListener{
    private SharedPreferences sh ;
    private Button mSignON,mSetIp ;
    private String serverIp;
    private ConnectionManager.TryConnectCall connectCall;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initListener();
        sh              = getSharedPreferences("myown", Context.MODE_PRIVATE);


    }
    private void initView(){
        mSignON         = (Button) findViewById(R.id.sign_on);
        mSetIp          = (Button) findViewById(R.id.setip_enter);
    }
    private void initListener(){
        mSignON         .setOnClickListener(this);
        mSetIp          .setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        serverIp        = sh.getString("ip","192.168.0.25");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.sign_on:
                ConnectionManager.getInstence().createConnection(serverIp,65430,getConnectCall());
                break;
            case R.id.setip_enter:
                startActivity(new Intent(MainActivity.this,SettingIp.class));
                break;
            default:
                break;
        }
    }

    private ConnectionManager.TryConnectCall getConnectCall(){
        if(connectCall==null){
            connectCall = new ConnectionManager.TryConnectCall() {
                @Override
                public void connectSucceful(String host) {
                    startActivity(new Intent(MainActivity.this,ShowMsgListActivity.class));
                }
                @Override
                public void connectFail(int err) {
                    Toast.makeText(MainActivity.this,"登陆失败，请稍后再试",Toast.LENGTH_SHORT).show();
                }
            };
        }
        return connectCall;
    }


}
