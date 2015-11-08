package android.content;/* ktekchan */

/*
 * This class saves data about hidden notifications in a map and methods to
 * access this information
 */

import android.util.ArrayMap;

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
   private final ArrayMap<String, Object> mHiddenEntries = new ArrayMap<>();

   public Object get(String key) {
      return mHiddenEntries.get(key);
   }

   public void add(String key, Object entry) {
      mHiddenEntries.put(key, entry);
   }


   public Object remove(String key) {
      Object removed = mHiddenEntries.remove(key);
      if (removed == null) return null;
      return removed;
   }


   // remove notifications for particular package
   public void removePackage(String pkgName){
      for(ArrayMap.Entry<String, Object> entry : mHiddenEntries.entrySet()) {
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
