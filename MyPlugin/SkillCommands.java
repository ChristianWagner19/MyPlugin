package mingle.MyPlugin;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

import mingle.MyPlugin.entities.User;
import net.md_5.bungee.api.ChatColor;

public class SkillCommands implements CommandExecutor {
   private Plugin plugin;
   private Dao<User, Integer> userDao;

   public SkillCommands(MyPlugin plugin, Dao<User, Integer> userDao) {
      this.plugin = plugin;
      this.userDao = userDao;
   }

   public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
      if (sender instanceof Player) {
         Player player = (Player)sender;
         User user = null;
         if (command.getName().equalsIgnoreCase("stats")) {
            UUID uniqueId = player.getUniqueId();
            QueryBuilder queryBuilder = this.userDao.queryBuilder();

            try {
               queryBuilder.where().eq("uniqueId", uniqueId);
               PreparedQuery<User> preparedQuery = queryBuilder.prepare();
               user = (User)this.userDao.queryForFirst(preparedQuery);
            } catch (Exception var25) {
               System.out.println(var25);
            }

            int Mining = user.getMining();
            double miningExp = user.getMiningExp();
            int Woodworking = user.getWoodworking();
            double woodworkingExp = user.getWoodworkingExp();
            int Vitality = user.getVitality();
            double vitalityExp = user.getVitalityExp();
            double miningExpToLevel = (double)(Mining * 60 + 45);
            double woodworkingExpToLevel = (double)(Woodworking * 60 + 45);
            double vitalityExpToLevel = (double)(Vitality * 60 + 45);
            int Backpack = 1;
            if (player.hasPermission("backpack.size.2")) {
               Backpack = 2;
            }

            if (player.hasPermission("backpack.size.3")) {
               Backpack = 2;
            }

            if (player.hasPermission("backpack.size.4")) {
               Backpack = 4;
            }

            if (player.hasPermission("backpack.size.5")) {
               Backpack = 5;
            }

            if (player.hasPermission("backpack.size.6")) {
               Backpack = 6;
            }

            player.sendMessage(ChatColor.DARK_GRAY + "Here are your current stats: ");
            player.sendMessage("");
            player.sendMessage(ChatColor.GREEN + "Mining Level: " + Integer.toString(Mining) + " || EXP: " + Double.toString(miningExp) + " / " + Double.toString(miningExpToLevel));
            player.sendMessage(ChatColor.BLUE + "Woodworking Level: " + Integer.toString(Woodworking) + " || EXP: " + Double.toString(woodworkingExp) + " / " + Double.toString(woodworkingExpToLevel));
            player.sendMessage(ChatColor.RED + "Vitality Level: " + Integer.toString(Vitality) + " || EXP: " + Double.toString(vitalityExp) + " / " + Double.toString(vitalityExpToLevel));
            player.sendMessage(ChatColor.GRAY + "Backpack Level: " + Integer.toString(Backpack) + " / 6");
            player.sendMessage(ChatColor.DARK_RED + "use /help MyPlugin to see avaliable skills");
         }

         if (command.getName().equalsIgnoreCase("backpack_levelup")) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + player.getName() + " promote backpacks");
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + player.getName() + " permission set myplugin.backpack_levelup false");
            player.setLevel(player.getLevel() - 100);
         }

         return true;
      } else {
         return false;
      }
   }
}