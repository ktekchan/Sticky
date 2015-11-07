package android.content;/* ktekchan */

/*
 * This class saves data about hidden notifications in a map and methods to
 * access this information
 */

import android.service.notification.StatusBarNotification;
import android.support.v4.util.ArrayMap;
import com.android.systemui.statusbar.StatusBarIconView;

import java.util.ArrayList;

public class HiddenNotificationData{

   // Entry object for the map
   public static final class HiddenEntry{
      public String key;
      public StatusBarNotification notification;
      public StatusBarIconView icon;

      public HiddenEntry(StatusBarNotification n, StatusBarIconView ic) {
         this.key = n.getKey();
         this.notification = n;
         this.icon = ic;
      }
   }


   private static HiddenNotificationData hiddenNotificationData;

   public static synchronized HiddenNotificationData getSharedInstance(){
      if (hiddenNotificationData == null){
         hiddenNotificationData = new HiddenNotificationData();
      }
      return hiddenNotificationData;
   }



   // Hash Map to save the information for easy access
   private final ArrayMap<String, HiddenEntry> mHiddenEntries = new ArrayMap<>();
   private final ArrayList<HiddenEntry> mHiddenSortedAndFiltered = new ArrayList<>();

   // All current active hidden notifications
   public ArrayList<HiddenEntry> getActiveHiddenNotifications() {
      return mHiddenSortedAndFiltered;
   }

   public HiddenEntry get(String key) {
      return mHiddenEntries.get(key);
   }

   public void add(HiddenEntry entry) {
      mHiddenEntries.put(entry.notification.getKey(), entry);
   }


   public HiddenEntry remove(String key) {
      HiddenEntry removed = mHiddenEntries.remove(key);
      if (removed == null) return null;
      return removed;
   }

}
