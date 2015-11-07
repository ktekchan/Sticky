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

}
