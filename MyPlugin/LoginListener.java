package mingle.MyPlugin;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

import mingle.MyPlugin.entities.User;

public final class LoginListener implements Listener {
   private Dao<User, Integer> userDao;
   private Plugin plugin;
   public static ArrayList<User> users = new ArrayList<>();

   public LoginListener(MyPlugin plugin, Dao<User, Integer> userDao) {
      this.plugin = plugin;
      plugin.getServer().getPluginManager().registerEvents(this, plugin);
      this.userDao = userDao;
   }

   @EventHandler
   public void userDisconnect(PlayerQuitEvent event) throws SQLException {
      User user = this.getUser(event);
      users.remove(user);
      user.setRunRegen(0);
      if (user.getRegenTaskID() != -1) {
         BukkitScheduler scheduler = Bukkit.getServer().getScheduler();
         System.out.println("BEFORE");
         System.out.println(scheduler.getPendingTasks());
         scheduler.cancelTask(user.getRegenTaskID());
         user.setRegenTaskID(-1);
         System.out.println("AFTER");
         System.out.println(scheduler.getPendingTasks());
      }

      this.userDao.update((User)user);
   }

   @EventHandler
   public void checkUserExists(PlayerJoinEvent event) throws SQLException {
      final User user = this.getUser(event);
      final Player player;
      if (user != null) {
         Bukkit.broadcastMessage("Welcome " + user.getUsername() + " back to the server!");
         player = event.getPlayer();
         player.sendMessage(ChatColor.RED + "Remember to use /stats to see your progress and use /help MyPlugin to see avaliable abilities/commands");
         player.sendMessage(ChatColor.RED + "Abilities/commands unlock at 5 level milestones");
         player.sendMessage(ChatColor.BLUE + "You can check the scoreboard at ChristianCNU.com");
         player.sendMessage(ChatColor.AQUA + "You may use /recipes to see recipes, or use the Recipe Book.");
         player.sendMessage(ChatColor.AQUA + "You may use /recipe_book to recieve a new book.");
         player.sendMessage(ChatColor.AQUA + "You may use /kills to see your mobs killed.");
         player.sendMessage(ChatColor.AQUA + "You may use /ores to see your ores mined.");
         
         
         if (player.hasPermission("myplugin.level10_effect_vitality")) {
            (new BukkitRunnable() {
               public void run() {
                  System.out.println("Starting regen task");
                  user.setRegenTaskID(this.getTaskId());
                  System.out.println("Starting Player health: " + Double.toString(player.getHealth()));
                  if (user.getRunRegen() == 1 && player.getHealth() < player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()) {
                     if (player.getHealth() < player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() && player.getHealth() + 1.0D > player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue()) {
                        player.setHealth(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
                     } else {
                        player.setHealth(player.getHealth() + 1.0D);
                     }
                  }

                  System.out.println("Ending Player health: " + Double.toString(player.getHealth()));
                  user.setRunRegen(1);

                  try {
                     LoginListener.this.userDao.update((User)user);
                  } catch (SQLException var2) {
                     var2.printStackTrace();
                  }

               }
            }).runTaskTimer(this.plugin, 0L, 2400L);
         }
      } else {
         player = event.getPlayer();
         UUID uniqueId = player.getUniqueId();
         Bukkit.broadcastMessage("Welcome " + player.getDisplayName() + " to the server!");
         player.sendMessage(ChatColor.RED + "Hey, Welcome to the server " + event.getPlayer().getDisplayName());
         player.sendMessage(ChatColor.RED + "Use /stats to see your progress and use /help to see avaliable abilities/commands");
         player.sendMessage(ChatColor.RED + "Abilities/commands unlock at 5 level milestones");
         player.sendMessage(ChatColor.AQUA + "You may use /recipes to see recipes, or use the Recipe Book.");
         player.sendMessage(ChatColor.AQUA + "You may use /recipe_book to recieve a new book.");
         player.sendMessage(ChatColor.AQUA + "You may use /kills to see your mobs killed.");
         player.sendMessage(ChatColor.AQUA + "You may use /ores to see your ores mined.");
         User newUser = new User();
         newUser.setUsername(player.getDisplayName());
         newUser.setUniqueId(uniqueId);
         newUser.setMining(1);
         newUser.setMiningExp(1.0D);
         newUser.setWoodworking(1);
         newUser.setWoodworkingExp(1.0D);
         newUser.setVitality(1);
         newUser.setVitalityExp(1.0D);
         if(newUser.getOresMined() == null) { // this is always true but works fine
  		   String oresMined = "Diamond Ore Mined:%d,Gold Ore Mined:%d,Iron Ore Mined:%d,Coal Ore Mined:%d,Copper Ore Mined:%d,Total Ores Mined:%d".formatted(0, 0, 0, 0, 0, 0);
  		   newUser.setOresMined(oresMined);
  		   userDao.update(newUser);
  	   		}
         if(newUser.getMobsKilled() == null) {
  		   String mobsKilled = "Zombies Killed:%d,Skeletons Killed:%d,Spiders Killed:%d,Cave Spiders Killed:%d,Creepers Killed:%d,Total Mobs Killed:%d".formatted(0, 0, 0, 0, 0, 0);
  		   newUser.setMobsKilled(mobsKilled);
  		   userDao.update(user);
  	   }
         
         
         this.userDao.create((User)newUser);
         System.out.println("Created new user");
         Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + player.getName() + " parent add recruit");
         Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + player.getName() + " parent add backpack1");
         Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "cc give " + player.getName() + " customcrafting:recipe_book");
         Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + player.getName() + " permission set myplugin.recipe_book true");
         Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + player.getName() + " permission set myplugin.kills true");
         Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + player.getName() + " permission set myplugin.ores true");
         
      }

      users.add(user);
      System.out.println(user);
   }

   public User getUser(PlayerEvent event) throws SQLException {
      Player player = event.getPlayer();
      UUID uniqueId = player.getUniqueId();
      QueryBuilder<User, Integer> queryBuilder = this.userDao.queryBuilder();
      queryBuilder.where().eq("uniqueId", uniqueId);
      PreparedQuery<User> preparedQuery = queryBuilder.prepare();
      User user = (User)this.userDao.queryForFirst(preparedQuery);
      return user;
   }
}