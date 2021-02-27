package com.cttricks.app;

import android.app.Activity;
import android.content.Context;
import com.google.appinventor.components.annotations.*;
import com.google.appinventor.components.common.*;
import com.google.appinventor.components.runtime.*;
import com.google.appinventor.components.runtime.util.*;
import java.io.*;
import java.net.*;

@SimpleObject(external = true)
@DesignerComponent(
  version = 1,
  description = "Extension Description",
  category = ComponentCategory.EXTENSION,
  nonVisible = true,
  iconName = ""
  )
@UsesPermissions(permissionNames = "android.permission.INTERNET")

public final class AddOnKDB extends AndroidNonvisibleComponent {
    private ComponentContainer container;
    private Context context;
    private Activity activity;
    private String main_url = "SERVER_URL";

    public AddOnKDB(ComponentContainer container) {
        super(container.$form());
        context = container.$context();
        activity = container.$context();
    }
  
  @SimpleFunction(description = "Make Server Call")
    public void Get() {
        final String urlString = this.main_url; 
        new Thread() {
            public void run(){
              try {
                    URL url = new URL(urlString);
                    URLConnection conn = url.openConnection();
                    InputStream is = conn.getInputStream();
                    final ByteArrayOutputStream into = new ByteArrayOutputStream();
                    byte[] buf = new byte[4096];
                    for (int n; 0 < (n = is.read(buf));) {
                        into.write(buf, 0, n);
                    }
                    into.close();
                    activity.runOnUiThread(new Runnable() {
                       public void run() {
                       	   try{
                               String final responseStr = new String(into.toByteArray(), "UTF-8");
                               GotData(responseStr);
                         } catch (Exception e){
                                GotData("Error Message");
                         }
                       }
                 });
                } catch(final Exception e){
                   activity.runOnUiThread(new Runnable() {
                       public void run() {
                           GotData("Error Message");
                       }
                   });
                }
            }
         }.start();	
    }
	
	@SimpleEvent(description = "Triggered when got response from server")
    public void GotData(String responseCode) {
        EventDispatcher.dispatchEvent(this, "GotData", responseCode);
	}
  
}
