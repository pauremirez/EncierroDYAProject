package com.navarra.dya.encierro;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by
 * @author Paula Remirez Ruiz
 * @version 2.0 (beta)
 */
public class ConnectionDetector {
 
    private Context _context;
    public ConnectionDetector(Context context){
        this._context = context;
    }
 
    /**
     * Checking for all possible internet providers
     * @return boolean
     * **/
    public boolean isConnectingToInternet(){
        ConnectivityManager connectivity = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
          if (connectivity != null)
          {
              NetworkInfo[] info = connectivity.getAllNetworkInfo();
              if (info != null) {
                  for (NetworkInfo anInfo : info)
                      if (anInfo.getState() == NetworkInfo.State.CONNECTED) {
                          return true;
                      }
              }
 
          }
          return false;
    }
}
