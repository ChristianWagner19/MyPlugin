package mingle.MyPlugin;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class MiningCommands implements CommandExecutor {
  private Plugin plugin;
  
  public MiningCommands(MyPlugin plugin) {
    this.plugin = (Plugin)plugin;
  }
  
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (sender instanceof Player) {
      Player player = (Player)sender;
      if (command.getName().equalsIgnoreCase("mining_level5"))
        Bukkit.dispatchCommand((CommandSender)Bukkit.getConsoleSender(), "cc give " + player.getName() + " craft_mining:iron_duplicator"); 
      if (command.getName().equalsIgnoreCase("mining_level10"))
        Bukkit.dispatchCommand((CommandSender)Bukkit.getConsoleSender(), "cc give " + player.getName() + " craft_mining:diamond_duplicator"); 
      return true;
    } 
    return false;
  }
}
