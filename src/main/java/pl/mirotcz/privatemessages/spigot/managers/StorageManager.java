package pl.mirotcz.privatemessages.spigot.managers;

import pl.mirotcz.privatemessages.spigot.PrivateMessages;
import pl.mirotcz.privatemessages.spigot.storage.Storage;
import pl.mirotcz.privatemessages.spigot.storage.Storage_MySQL;
import pl.mirotcz.privatemessages.spigot.storage.Storage_SQLite;

public class StorageManager {
   private Storage storage = null;
   private final PrivateMessages instance;

   public StorageManager(PrivateMessages plugin) {
      this.instance = plugin;
   }

   public void setupStorage() {
      if (this.instance.getSettings().STORAGE_TYPE.equalsIgnoreCase("sqlite")) {
         this.storage = new Storage_SQLite(this.instance);
      } else {
         this.storage = new Storage_MySQL(this.instance);
      }

   }

   public Storage getStorage() {
      return this.storage;
   }
}
