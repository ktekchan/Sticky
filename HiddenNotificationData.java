package android.content;/* ktekchan */

/*
 * This class saves data about hidden notifications in a map and methods to
 * access this information
 */

import android.support.v4.util.ArrayMap;
import com.android.systemui.statusbar.NotificationData.Entry;

import java.util.ArrayList;

public class HiddenNotificationData{

   private static HiddenNotificationData hiddenNotificationData;

   // Gets the single shared instance
   public static synchronized HiddenNotificationData getSharedInstance(){
      if (hiddenNotificationData == null){
         hiddenNotificationData = new HiddenNotificationData();
      }
      return hiddenNotificationData;
   }



   // Hash Map to save the information for easy access
   private final ArrayMap<String, Entry> mHiddenEntries = new ArrayMap<>();

   public Entry get(String key) {
      return mHiddenEntries.get(key);
   }

   public void add(Entry entry) {
      mHiddenEntries.put(entry.notification.getKey(), entry);
   }


   public Entry remove(String key) {
      Entry removed = mHiddenEntries.remove(key);
      if (removed == null) return null;
      return removed;
   }

   // remove notifications for particular package
   public void removePackage(String pkgName){
      for(ArrayMap.Entry<String, Entry> entry : mHiddenEntries.entrySet()) {
         String key = entry.getKey();
         if(key.contains(pkgName)){
            remove(key);
         }
      }
   }

   public Boolean isAppRunning(String appName,Context context){

      ActivityManager activityManager = (ActivityManager) context.getSystemService(ACTIVITY_SERVICE);
      List<RunningAppProcessInfo> procInfos = activityManager.getRunningAppProcesses();
      for(int i = 0; i < procInfos.size(); i++)
         {
            if(procInfos.get(i).processName.equals(appName)) 
            {
               return true;
            }
         }
      removePackage(appName);
      return false;
   }
}
