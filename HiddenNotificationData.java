package android.content;/* ktekchan */

/*
 * This class saves data about hidden notifications in a map and methods to
 * access this information
 */

import android.app.ActivityManager;
import android.service.notification.StatusBarNotification;
import android.util.ArrayMap;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
   private ArrayMap<String, Object> mHiddenEntries = new ArrayMap<>();
   private ArrayMap<String, StatusBarNotification> mHiddenSbn = new ArrayMap<>();

   public Object get(String key) {
      return mHiddenEntries.get(key);
   }

   public void add(String key, Object entry, StatusBarNotification sbn) {
      mHiddenEntries.put(key, entry);
      mHiddenSbn.put(key,sbn);
   }


   public ArrayMap<String,StatusBarNotification> getDisplayMap(){

      return mHiddenSbn;
   }

   public Object remove(String key) {
      Object removed = mHiddenEntries.remove(key);
      mHiddenSbn.remove(key);
      if (removed == null) return null;
      return removed;
   }



   public HashSet<String> generateNotifAppNameMap(){
      HashSet<String> nameSet = new HashSet<>();
      for(String key : mHiddenEntries.keySet()) {
         String pkgName = key.split("|")[1];
         nameSet.add(pkgName);
      }
      return nameSet;
   }

   public void filterEntries(Context context){

      ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
      List<ActivityManager.RunningAppProcessInfo> procInfos = activityManager.getRunningAppProcesses();

      HashSet<String> nameSet = generateNotifAppNameMap();
      for(int i = 0; i < procInfos.size(); i++) {
         if (nameSet.contains(procInfos.get(i).processName)) {
            nameSet.remove(procInfos.get(i).processName);
         }
      }

      Iterator<Map.Entry<String, Object>> iter = mHiddenEntries.entrySet().iterator();
      while(iter.hasNext()){
         Map.Entry<String,Object> entry = iter.next();
         if(nameSet.contains(entry.getKey().split("|")[1])){
            iter.remove();
            mHiddenSbn.remove(entry.getKey());
         }
      }
   }

   public ArrayMap<String, StatusBarNotification> getHiddenSbn(Context context){
      filterEntries(context);
      return mHiddenSbn;
   }
}
