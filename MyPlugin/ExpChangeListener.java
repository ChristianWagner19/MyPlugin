package mingle.MyPlugin;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerLevelChangeEvent;

import com.j256.ormlite.dao.Dao;

import mingle.MyPlugin.entities.User;

public class ExpChangeListener implements Listener {
   private Dao<User, Integer> userDao;
   private MyPlugin plugin;

   public ExpChangeListener(MyPlugin plugin, Dao<User, Integer> userDao) {
      plugin.getServer().getPluginManager().registerEvents(this, plugin);
      this.userDao = userDao;
      this.plugin = plugin;
   }

   @EventHandler
   public void onExpChange(PlayerLevelChangeEvent event) {
      Player player = event.getPlayer();
      if (player.getLevel() >= 100 && !player.hasPermission("myplugin.backpack_levelup")) {
         Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + player.getDisplayName() + " permission set myplugin.backpack_levelup true");
         player.sendMessage(ChatColor.BLUE + "You may now use /backpack_levelup to spend 100 levels in exchange for 9 backpack slots.");
         player.sendMessage(ChatColor.GRAY + "This permission is only avaliable while you are level 100 or above.");
      }

      if (player.getLevel() < 100 && player.hasPermission("myplugin.backpack_levelup")) {
    	  player.sendMessage(ChatColor.GRAY + "You may " + ChatColor.RED + "NO LONGER " + ChatColor.GRAY + "use /backpack_levelup to spend 100 levels in exchange for 9 backpack slots.");
          player.sendMessage(ChatColor.RED + "This permission is only avaliable while you are level 100 or above.");
         Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + player.getDisplayName() + " permission set myplugin.backpack_levelup false");
      }

   }

   @EventHandler
   public void death(PlayerDeathEvent event) {
      Player player = event.getEntity();
      String message = String.format(ChatColor.RED + "You died! You lost %d EXP!", player.getTotalExperience());
      player.sendMessage(message);
      //player.sendMessage("DEBUG:\nX: " + Double.toString(player.getLocation().getX()) + "\nZ: " + Double.toString(player.getLocation().getZ()));
   }
}