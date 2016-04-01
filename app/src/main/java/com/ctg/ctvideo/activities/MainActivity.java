package com.ctg.ctvideo.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import com.ctg.ctvideo.R;
import com.ctg.ctvideo.services.CtVideoService;
import com.ctg.ctvideo.services.NetworkService;
import com.ctg.ctvideo.services.PasswordUtils;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void toHomepage(View view) {
        Intent intent = new Intent(this, HomepageActivity.class);
        startActivity(intent);
    }

    public void toFileExplorer(View view) {
        Intent intent = new Intent(this, FileExplorerActivity.class);
        startActivity(intent);
    }

    public void throwVideo(View view) {
        new Thread() {
            public void run() {
                String result = CtVideoService.throwVideo("e3bf8f2fa62911e5b690a5558c170839");
                System.out.println(result);
            }
        }.start();
    }

    public void test(View view) {
        String s = new String("abcdefghijklmnopqrstuvwyxzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890~!@#$%^&*()_+");
        System.out.println("原始：" + s);
        System.out.println("MD5后：" + PasswordUtils.encryptMD5(s));
        System.out.println("加密的：" + PasswordUtils.encryptDES(s));
        System.out.println("解密的：" + PasswordUtils.decryptDES(PasswordUtils.encryptDES(s)));
        System.out.println(s.equals(PasswordUtils.decryptDES(PasswordUtils.encryptDES(s))));
        System.out.println("加密的：" + PasswordUtils.encrypt3DES(s));
        System.out.println("解密的：" + PasswordUtils.decrypt3DES(PasswordUtils.encrypt3DES(s)));
        System.out.println(s.equals(PasswordUtils.decrypt3DES(PasswordUtils.encrypt3DES(s))));

        byte[] b1 = s.getBytes();
        byte[] b2 = PasswordUtils.hexToByte(PasswordUtils.toHexString(b1));
        System.out.println(Arrays.equals(b1, b2));
    }
}
