package pl.mirotcz.privatemessages.bungee.managers;

import pl.mirotcz.privatemessages.bungee.PrivateMessages;
import pl.mirotcz.privatemessages.bungee.storage.Storage;
import pl.mirotcz.privatemessages.bungee.storage.Storage_MySQL;

public class StorageManager {
   private Storage storage = null;
   private PrivateMessages instance = null;

   public StorageManager(PrivateMessages plugin) {
      this.instance = plugin;
   }

   public void setupStorage() {
      this.storage = new Storage_MySQL(this.instance);
   }

   public Storage getStorage() {
      return this.storage;
   }
}
