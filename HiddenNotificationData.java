package android.content;/* ktekchan */

/*
 * This class saves data about hidden notifications in a map and methods to
 * access this information
 */

import android.app.ActivityManager;
import android.service.notification.StatusBarNotification;
import android.util.ArrayMap;
import android.util.Log;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class HiddenNotificationData{


   // Hash Map to save the information for easy access
   private ArrayMap<String, Object> mHiddenEntries = new ArrayMap<String, Object>();
   private ArrayMap<String, StatusBarNotification> mHiddenSbn = new ArrayMap<String, StatusBarNotification>();

   // Constructor
   public HiddenNotificationData(){
   }

   private static HiddenNotificationData hiddenNotificationData = null;

   // Gets the single shared instance
   public static synchronized HiddenNotificationData getSharedInstance(){
      if (hiddenNotificationData == null){
         hiddenNotificationData = new HiddenNotificationData();
         Log.d("YAAP", "get Shared instance - First Creation");
      }
//      Log.d("YAAP", "Class object - "+HiddenNotificationData.class);

      Log.d("YAAP", "get Shared instance NEW null case" + hiddenNotificationData);
      return hiddenNotificationData;
   }



   public Object get(String key) {
      return mHiddenEntries.get(key);
   }

   public void add(String key, Object entry, StatusBarNotification sbn) {
      Log.d("YAAP", "add  in Hidden Notification - " + key + "for app - "+sbn.getPackageName());
      mHiddenSbn.put(key,sbn);
      mHiddenEntries.put(key,entry);
      Log.d("YAAP", "Hidden Sbn "+mHiddenSbn);
      Log.d("YAAP", "Hidden Entries "+mHiddenSbn);

   }



   public Object remove(String key) {

      Object removed = mHiddenEntries.remove(key);
      StatusBarNotification sbn = mHiddenSbn.remove(key);
      Log.d("YAAP", "Remove  in Hidden Notification - " + key + "for app - "+sbn.getPackageName());

      if (removed == null) return null;
      return removed;
   }



   public HashSet<String> generateNotifAppNameMap(){

      HashSet<String> nameSet = new HashSet<>();
      for(String key : mHiddenEntries.keySet()) {
         String pkgName = key.split("|")[1];
         nameSet.add(pkgName);
      }
      Log.d("YAAP", "Current apps with notifs hidden - " + nameSet);

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
            Log.d("YAAP", "Entry removed for stopped app "+entry.getKey());

            iter.remove();
            mHiddenSbn.remove(entry.getKey());
         }
      }
   }

   public ArrayMap<String, StatusBarNotification> getDisplayMap(Context context){
      //filterEntries(context);
      Log.d("YAAP", "Size of display map ArrayMap is"+Integer.toString(mHiddenSbn.size()));
      return mHiddenSbn;
   }
}
