/*
 * Copyright (c) 2015-present, Parse, LLC.
 * All rights reserved.
 *
 * This source code is licensed under the BSD-style license found in the
 * LICENSE file in the root directory of this source tree. An additional grant
 * of patent rights can be found in the PATENTS file in the same directory.
 */
package com.parse.starter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Switch;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;


public class MainActivity extends AppCompatActivity {

  public void redirectActivity(){
    if(ParseUser.getCurrentUser().get("riderOrDriver").equals("rider")){
      Intent intent = new Intent(getApplicationContext(), RiderActivity.class);
      startActivity(intent);
    }
  }

  @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
  public void getStarted(View view){
    Switch userSwitch = (Switch) findViewById(R.id.userSwitch);
    Log.i("Switch Value", String.valueOf(userSwitch.isChecked()));

    String userType = "rider";
    if(userSwitch.isChecked()){
      userType="driver";
    }
     ParseUser.getCurrentUser().put("riderOrDriver",userType);
      ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
        @Override
        public void done(ParseException e) {
          redirectActivity();
        }
      });
     //Log.i("Info","Redirecting as" + userType);
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    getSupportActionBar().hide();

    if(ParseUser.getCurrentUser() == null){
      ParseAnonymousUtils.logIn(new LogInCallback() {
        @Override
        public void done(ParseUser parseUser, ParseException e) {
          if(e==null){
            Log.i("Info","Anonymous Login Successful");
          }
          else {
            Log.i("Info", "Anonymous Login Failed");
          }
        }
      });
    } else{
      if(ParseUser.getCurrentUser().get("riderOrDriver")!=null){
        Log.i("Info","Redirecting as" + ParseUser.getCurrentUser().get("riderOrDriver"));
        redirectActivity();
      }
    }

    ParseAnalytics.trackAppOpenedInBackground(getIntent());
  }

}