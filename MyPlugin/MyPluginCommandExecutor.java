package mingle.MyPlugin;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MyPluginCommandExecutor implements CommandExecutor{
	@SuppressWarnings("unused")
	private final MyPlugin plugin;

	public MyPluginCommandExecutor(MyPlugin plugin) {
		this.plugin = plugin; // Store the plugin in situations where you need it.
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		// implementation exactly as before...
		if (cmd.getName().equalsIgnoreCase("sword")) { // If the player typed /basic then do the following...
			// do something...
			return true;
		} else if (cmd.getName().equalsIgnoreCase("basic2")) {
			if (!(sender instanceof Player)) {
				sender.sendMessage("This command can only be run by a player.");
			} else {
				Player player = (Player) sender;
				// do something
			}
			return true;
		}
		return false;
	}
}
