package mingle.MyPlugin;

import java.sql.SQLException;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

import mingle.MyPlugin.entities.User;

public class MiscCommands implements CommandExecutor {
  private final MyPlugin plugin;
  private Dao<User, Integer> userDao;
  
  public MiscCommands(MyPlugin plugin, Dao<User, Integer> userDao) {
      this.plugin = plugin;
      this.userDao = userDao;
   }
  
  
  public User prepareUser(Player player) throws SQLException {
	   UUID uniqueId = player.getUniqueId();
      QueryBuilder<User, Integer> queryBuilder = this.userDao.queryBuilder();
      queryBuilder.where().eq("uniqueId", uniqueId);
      PreparedQuery<User> preparedQuery = queryBuilder.prepare();
      User user = (User)this.userDao.queryForFirst(preparedQuery);
	return user;
	   
  }
  
  
  
  
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (sender instanceof Player) {
      if (command.getName().equalsIgnoreCase("recipe_book")) {
        Player player = (Player)sender;
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "cc give " + player.getName() + " customcrafting:recipe_book");
        return true;
      }
      if (command.getName().equalsIgnoreCase("kills")) {
    	  Player player = (Player)sender;
    	  try {
			User user = prepareUser(player);
			String mobsKilled = user.getMobsKilled();
			String[] mobsKilledSplit = mobsKilled.split(",");
			player.sendMessage(ChatColor.RED + "%s: %d".formatted(mobsKilledSplit[0].split(":")[0], Integer.valueOf(mobsKilledSplit[0].split(":")[1])));
			player.sendMessage(ChatColor.RED + "%s: %d".formatted(mobsKilledSplit[1].split(":")[0], Integer.valueOf(mobsKilledSplit[1].split(":")[1])));
			player.sendMessage(ChatColor.RED + "%s: %d".formatted(mobsKilledSplit[2].split(":")[0], Integer.valueOf(mobsKilledSplit[2].split(":")[1])));
			player.sendMessage(ChatColor.RED + "%s: %d".formatted(mobsKilledSplit[3].split(":")[0], Integer.valueOf(mobsKilledSplit[3].split(":")[1])));
			player.sendMessage(ChatColor.RED + "%s: %d".formatted(mobsKilledSplit[4].split(":")[0], Integer.valueOf(mobsKilledSplit[4].split(":")[1])));
			String totalMessage = ChatColor.RED + "%s: %d".formatted(mobsKilledSplit[5].split(":")[0], Integer.valueOf(mobsKilledSplit[5].split(":")[1]));
			player.sendMessage(totalMessage);
			String repeated = new String(new char[totalMessage.length()]). replace("\0", "-");
			player.sendMessage(ChatColor.RED + "%s".formatted(repeated));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
      }
      if(command.getName().equalsIgnoreCase("ores")) {
    	  Player player = (Player)sender;
    	  try {
			User user = prepareUser(player);
			String oresMined = user.getOresMined();
			String[] oresMinedSplit = oresMined.split(",");
			player.sendMessage(ChatColor.RED + "%s: %d".formatted(oresMinedSplit[0].split(":")[0], Integer.valueOf(oresMinedSplit[0].split(":")[1])));
			player.sendMessage(ChatColor.RED + "%s: %d".formatted(oresMinedSplit[1].split(":")[0], Integer.valueOf(oresMinedSplit[1].split(":")[1])));
			player.sendMessage(ChatColor.RED + "%s: %d".formatted(oresMinedSplit[2].split(":")[0], Integer.valueOf(oresMinedSplit[2].split(":")[1])));
			player.sendMessage(ChatColor.RED + "%s: %d".formatted(oresMinedSplit[3].split(":")[0], Integer.valueOf(oresMinedSplit[3].split(":")[1])));
			player.sendMessage(ChatColor.RED + "%s: %d".formatted(oresMinedSplit[4].split(":")[0], Integer.valueOf(oresMinedSplit[4].split(":")[1])));
			String totalMessage = ChatColor.RED + "%s: %d".formatted(oresMinedSplit[5].split(":")[0], Integer.valueOf(oresMinedSplit[5].split(":")[1]));
			player.sendMessage(totalMessage);
			String repeated = new String(new char[totalMessage.length() + 1]). replace("\0", "-");
			player.sendMessage(ChatColor.RED + "%s".formatted(repeated));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
      }
      
      return true;
    }
    return false;
  }
}
