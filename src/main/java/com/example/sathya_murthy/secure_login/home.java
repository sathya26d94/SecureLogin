package com.example.sathya_murthy.secure_login;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Vibrator;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import org.apache.http.*;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;



public class home extends ActionBarActivity {


    ImageView image;
    int t,ct,ind,z;
    int imageArray[];
    String s1,s2;
    int token;

    public home() {
        image = null;
        z=0;
        ct=-1;
        ind=0;
        token=-1;
        s1=s2="";
        imageArray=new int[10];
        imageArray[0]=R.drawable.s1;
        imageArray[1]=R.drawable.s2;
        imageArray[2]=R.drawable.s3;
        imageArray[3]=R.drawable.s4;
        imageArray[4]=R.drawable.s5;
        imageArray[5]=R.drawable.s6;
        imageArray[6]=R.drawable.s7;
        imageArray[7]=R.drawable.s8;
        imageArray[8]=R.drawable.s9;
        imageArray[9]=R.drawable.s10;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        image = (ImageView) findViewById(R.id.imageView);
        Timer timer = new Timer();
        MyTimer mt = new MyTimer();
        timer.schedule(mt,2000,2000);
        Button b=(Button)findViewById(R.id.button);
        Button b1=(Button)findViewById(R.id.button2);
        if(!isNetworkConnected())
        {
            Toast.makeText(getApplicationContext(), "Pls Connect to Internet", Toast.LENGTH_LONG).show();
        }

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(t==1)
                {
                    ind++;
                    s1=s1+ct;
                    ct=0;
                    s2+="i";
                    if(ind==4)
                    {
                        login(""+token,s1,s2);
                        s1=s2="";
                        token=-1;
                        ind=0;
                        ct=-1;
                        z=0;
                        t=0;
                    }
                }


            }
        });
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(t==1)
                {
                    ind++;
                    ct=ct%10;
                    s1=s1+ct;
                    ct=0;
                    s2+="o";
                    if(ind==4)
                    {
                        login(""+token,s1,s2);
                        s1=s2="";
                        token=-1;
                        ind=0;
                        ct=-1;
                        z=0;
                        t=0;
                    }
                }

            }
        });
    }

        class MyTimer extends TimerTask {

            public void run() {
                        runOnUiThread(new Runnable() {

                    public void run() {

                        Random rand = new Random();

                        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                        ct++;
                        if (t==0)
                        {
                            token++;
                        }
                        if((rand.nextInt()%5==0 && t==0) || (token==9 && t==0))
                        {
                            v.vibrate(100);
                            t=1;
                            //Toast.makeText(getApplicationContext(), "token", Toast.LENGTH_LONG).show();
                            ct=0;
                        }

                        image.setImageResource(imageArray[z++]);

                        if(z==10)z=0;


                    }
                });
            }

        }
    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }

    private void login(String username, String password, String inout) {

        class LoginAsync extends AsyncTask<String, String, String>{

            private Dialog loadingDialog;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loadingDialog = ProgressDialog.show(home.this, "Please wait", "Logging in..");
            }

            @Override
            protected String doInBackground(String... params) {
                InputStream is = null;
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
                nameValuePairs.add(new BasicNameValuePair("token", params[0]));
                nameValuePairs.add(new BasicNameValuePair("pin", params[1]));
                nameValuePairs.add(new BasicNameValuePair("inout", params[2]));

                String result = null;

                try{
                    HttpClient httpClient = new DefaultHttpClient();
                    HttpPost httpPost = new HttpPost(
                            "http://securelogin.net16.net/ser.php");
                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    HttpResponse response = httpClient.execute(httpPost);

                    HttpEntity entity = response.getEntity();

                    is = entity.getContent();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(
                            is, "iso-8859-1"), 8);

                   if ((result = reader.readLine()) != null)
                   {}else
                   {
                       Toast.makeText(getApplicationContext(), "Network error", Toast.LENGTH_LONG).show();
                   }
                } catch (Exception e) {
                    e.printStackTrace();

                }
                return result;
            }

            @Override
            protected void onPostExecute(String result){
                String s = result.trim();
                loadingDialog.dismiss();
                if(s.equalsIgnoreCase("success")){

                    Toast.makeText(getApplicationContext(),"success",Toast.LENGTH_LONG).show();
                    Intent i = new Intent(getApplicationContext(), welcome.class);
                    finish();
                    startActivity(i);
                }else {
                    Toast.makeText(getApplicationContext(), "Failed,try again", Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(getIntent());

                }
            }
        }

        LoginAsync la = new LoginAsync();
        la.execute(username, password,inout);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();


        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
