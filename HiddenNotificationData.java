/* ktekchan */

/*
 * This class saves data about hidden notifications in a map and methods to
 * access this information
 */

public class HiddenNotificationData{

   // Entry object for the map
   public static final class HiddenEntry{
      public String key;
      public StatusBarNotification notification;
      public StatusBarIconView icon;

      public Entry(StatusBarNotification n, StatusBarIconView ic) {
         this.key = n.getKey();
         this.notification = n;
         this.icon = ic;
      }
   }

   // Hash Map to save the information for easy access
   private final ArrayMap<String, HiddenEntry> mHiddenEntries = new ArrayMap<>();
   private final ArrayList<HiddenEntry> mHiddenSortedAndFiltered = new ArrayList<>();

   // All current active hidden notifications
   public ArrayList<HiddenEntry> getActiveHiddenNotifications() {
      return mSortedAndFiltered;
   }

   public HiddenEntry get(String key) {
      return mHiddenEntries.get(key);
   }

   public HiddenEntry remove(String key) {
      HiddenEntry removed = mHiddenEntries.remove(key);
      if (removed == null) return null;
      return removed;
   }

}
