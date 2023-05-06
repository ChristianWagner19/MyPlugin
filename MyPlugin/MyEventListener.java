package mingle.MyPlugin;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.PreparedQuery;
import com.j256.ormlite.stmt.QueryBuilder;

import mingle.MyPlugin.entities.User;

public class MyEventListener implements Listener {
   private Dao<User, Integer> userDao;
   private Plugin plugin;
   private Random rand = new Random();
   private HashMap<String, Double> miningValues = new HashMap<String, Double>();
   

   public MyEventListener(MyPlugin plugin, Dao<User, Integer> userDao) {
      this.userDao = userDao;
      this.plugin = plugin;
      plugin.getServer().getPluginManager().registerEvents(this, plugin);
      
      
      miningValues = initializeMiningValues(miningValues);
   }

   @EventHandler
   public void onPlayerSleep(PlayerBedLeaveEvent event) {
      if (Bukkit.getServer().getWorld("world").getTime() == 0L) {
         Player player = event.getPlayer();
         event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 1));
         player.sendMessage(ChatColor.GREEN + "You feel well rested... you recovered a little health and gained a small amount of vitality exp");
			addVitalityExp(player, 1.0);
         
      }

   }
   
   public User prepareUser(Player player){
	   try {
	   UUID uniqueId = player.getUniqueId();
       QueryBuilder<User, Integer> queryBuilder = this.userDao.queryBuilder();
       queryBuilder.where().eq("uniqueId", uniqueId);
       PreparedQuery<User> preparedQuery = queryBuilder.prepare();
       User user = (User)this.userDao.queryForFirst(preparedQuery);
       return user;
	   }
	   catch(SQLException e) {
		   e.printStackTrace();
	   }
	return null;
	   
   }
   
   public void addVitalityExp(Player player, double experienceGained){
	   try {
	   User user = prepareUser(player);
	   user.setVitalityExp(user.getVitalityExp() + experienceGained);
	   if (user.getVitalityExp() > (double)(user.getVitality() * 60 + 45)) {
           user.setVitality(user.getVitality() + 1);
           user.setVitalityExp(1.0D); 
           player.sendMessage(ChatColor.RED + "You have leveled up in vitality! You are now level " + Integer.toString(user.getVitality()) + "!");
	   }
	   this.userDao.update(user);
	   System.out.println("Vitality Called");
	   }
	   catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
   }
   
   
   @EventHandler
   public void onPlayerInteract(PlayerInteractEvent event) throws SQLException {
       Player player = event.getPlayer();
       Action action = event.getAction();
       ItemStack item = player.getInventory().getItemInMainHand();

       if (action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK)) {
           if (item.getType().equals(Material.GOLDEN_CARROT) && player.getHealth() < player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue() && player.getFoodLevel() >= 20) {
               // Check if the player is on cooldown
               if (player.getCooldown(Material.GOLDEN_CARROT) > 0) {
                   return;
               }

               // Consume the golden carrot
               item.setAmount(item.getAmount() - 1);

               player.setCooldown(Material.GOLDEN_CARROT, 20);

               // Health restoration
               double health = player.getHealth();
               double maxHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
               if (health < maxHealth) {
                   if (health >= maxHealth - 1.0D) {
                       player.setHealth(maxHealth);
                   } else {
                       player.setHealth(health + 1.0D);
                   }
               }

               // Add vitality experience
               addVitalityExp(player, 0.5);

               // Send the player a message
               player.sendMessage(ChatColor.YELLOW + "You have consumed a golden carrot!");
           }
       }
   }
   
   
   
   @EventHandler
   public void onPlayerEat(PlayerItemConsumeEvent event) throws SQLException {
      ItemStack item = event.getItem();
      String itemName = item.getItemMeta().getDisplayName();
      Player player = event.getPlayer();
      if (itemName.equals("Enchanted Golden Apple")) {
         event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 100, 3));
         addVitalityExp(player, 3.0);
      }

      if (item.containsEnchantment(Enchantment.MENDING) && itemName.contains("Apple of Revival")) {
         String reviveTarget = itemName.replace("Apple of Revival:", "");
         Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "spawn " + reviveTarget);
         Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "gamemode survival " + reviveTarget);
         addVitalityExp(player, 5.0);
      }

      if (item.getType().equals(Material.GOLDEN_CARROT)) {
         double health = player.getHealth();
         double maxHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
         //player.sendMessage("Health: " + Double.toString(player.getHealth()));
         if (health < maxHealth) {
            if (health >= maxHealth - 1.0D) {
               player.setHealth(maxHealth);
            } else {
               player.setHealth(health + 1.0D);
            }
         }
         addVitalityExp(player, 0.5);
         player.sendMessage(ChatColor.YELLOW + "You have consumed a golden carrot!");
         player.setCooldown(Material.GOLDEN_CARROT, 20);
         
         
      }

   }

   public HashMap<String, Double> initializeMiningValues(HashMap<String, Double> miningValues){
	   
		Path path = Paths.get("/home/ubuntu/PluginTest/plugins/MyPluginConfig/miningvalues.txt");
   try (BufferedReader reader = new BufferedReader(new FileReader(path.toFile(), StandardCharsets.UTF_8))) {
   	String line = reader.readLine();
       while(line != null) {
       	
       	String block = line.split(":")[0];
       	double blockvalue = Double.parseDouble(line.split(":")[1]);
       	miningValues.put(block, blockvalue);
       	line = reader.readLine();
       	}
   	} 
   catch (IOException e) {
       System.err.println("Error reading file: " + e.getMessage());
   }
 
return miningValues;
  
}
   
   public void editOresMined(Player player, String oretype) throws SQLException {
	   User user = prepareUser(player);
	   
	   String oresMined = user.getOresMined();
	   String[] oresMinedSplit = oresMined.split(",");
	   int diamondsMined = Integer.valueOf(oresMinedSplit[0].split(":")[1]);
	   int goldMined = Integer.valueOf(oresMinedSplit[1].split(":")[1]);
	   int ironMined = Integer.valueOf(oresMinedSplit[2].split(":")[1]);
	   int coalMined = Integer.valueOf(oresMinedSplit[3].split(":")[1]);
	   int copperMined = Integer.valueOf(oresMinedSplit[4].split(":")[1]);
		   
	   switch (oretype) {
	   		case "DIAMOND_ORE":  
        	   		diamondsMined += 1;
        	   		break;
           	case "GOLD_ORE":  
        	   		goldMined += 1;
        	   		break;
           	case "IRON_ORE":
           			ironMined += 1;
           			break;
           	case "COAL_ORE":
           			coalMined += 1;
           			break;
           	case "COPPER_ORE":
           			copperMined += 1;
           			break;
	   			}
		   	
         String updatedOresMined = "Diamond Ore Mined:%d,Gold Ore Mined:%d,Iron Ore Mined:%d,Coal Ore Mined:%d,Copper Ore Mined:%d,Total Ores Mined:%d".formatted(diamondsMined, goldMined, ironMined, coalMined, copperMined, diamondsMined + goldMined + ironMined + coalMined + copperMined);
		 user.setOresMined(updatedOresMined);
		 userDao.update(user);
	   	}
   
   
   
   
   @EventHandler
   public void onBlockBreak(BlockBreakEvent event) throws SQLException {
      double miningExpValue = 0.0D;
      Block block = event.getBlock();
      Player player = event.getPlayer();
      String blockType = block.getType().toString();
      //System.out.println("HERE IS THE BLOCK TYPE:");
      //System.out.println(blockType);
      
      boolean needsUpdate = false;
	   List<String> blockTypes = List.of("DIAMOND_ORE", "GOLD_ORE", "IRON_ORE", "COAL_ORE", "COPPER_ORE");
	   if(blockTypes.contains(blockType)) {
		   needsUpdate = true;
      		}
	   if(needsUpdate) {
		   editOresMined(player, blockType);
	   }
      
      
      if (miningValues.get(blockType) != null) {
    	  miningExpValue = miningValues.get(blockType);
      }
      if (miningExpValue > 0.0D) {
         User user = prepareUser(player);
         user.setMiningExp(user.getMiningExp() + miningExpValue);
         if (user.getMiningExp() > (double)(user.getMining() * 60 + 45)) {
            user.setMining(user.getMining() + 1);
            user.setMiningExp(1.0D);
            player.sendMessage(ChatColor.RED + "You have leveled up in mining! You are now level " + Integer.toString(user.getMining()) + "!");
            if (user.getMining() == 5) {
               Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + player.getName() + " permission set myplugin.mining_level5 true");
            }

            if (user.getMining() == 10) {
               Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + player.getName() + " permission set myplugin.mining_level10 true");
            }
         }

         this.userDao.update((User)user);
      }

      double woodexpValue = 0.0D;
      if (event.getBlock().getType().name().contains("LOG")) {
         woodexpValue = 1.0D;
      }

      if (woodexpValue > 0.0D) {
         User user = prepareUser(player);
         user.setWoodworkingExp(user.getWoodworkingExp() + woodexpValue);
         if (user.getWoodworkingExp() > (double)(user.getWoodworking() * 60 + 45)) {
            user.setWoodworking(user.getWoodworking() + 1);
            user.setWoodworkingExp(1.0D);
            player.sendMessage(ChatColor.RED + "You have leveled up in woodworking! You are now level " + Integer.toString(user.getWoodworking()) + "!");
            if (user.getWoodworking() == 5) {
               Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + player.getName() + " permission set myplugin.level5_craft_woodworking true");
            }

            if (user.getWoodworking() == 10) {
               Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + player.getName() + " permission unset myplugin.level5_craft_woodworking");
               Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + player.getName() + " permission set myplugin.level10_craft_woodworking true");
            }
         }
         this.userDao.update((User)user);
      }

   }
   
   public void editMobsKilled(Player player, String mobtype) throws SQLException {
	   User user = prepareUser(player);
	   if(user.getMobsKilled() == null) {
		   String mobsKilled = "Zombies Killed:%d,Skeletons Killed:%d,Spiders Killed:%d,Cave Spiders Killed:%d,Creepers Killed:%d,Total Mobs Killed:%d".formatted(0, 0, 0, 0, 0, 0);
		   user.setMobsKilled(mobsKilled);
		   userDao.update(user);
	   }
	   
	   String mobsKilled = user.getMobsKilled();
	   String[] mobsKilledSplit = mobsKilled.split(",");
	   int zombiesKilled = Integer.valueOf(mobsKilledSplit[0].split(":")[1]);
	   int skeletonsKilled = Integer.valueOf(mobsKilledSplit[1].split(":")[1]);
	   int spidersKilled = Integer.valueOf(mobsKilledSplit[2].split(":")[1]);
	   int caveSpidersKilled = Integer.valueOf(mobsKilledSplit[3].split(":")[1]);
	   int creepersKilled = Integer.valueOf(mobsKilledSplit[4].split(":")[1]);
		   
	   switch (mobtype) {
	   		case "ZOMBIE":  
        	   		zombiesKilled += 1;
        	   		break;
           	case "SKELETON":  
        	   		skeletonsKilled += 1;
        	   		break;
           	case "SPIDER":
           			spidersKilled += 1;
           			break;
           	case "CAVE_SPIDER":
           			caveSpidersKilled += 1;
           			break;
           	case "CREEPER":
           			creepersKilled += 1;
           			break;
	   			}
		   	
         String updatedMobsKilled = "Zombies Killed:%d,Skeletons Killed:%d,Spiders Killed:%d,Cave Spiders Killed:%d,Creepers Killed:%d,Total Mobs Killed:%d".formatted(zombiesKilled, skeletonsKilled, spidersKilled, caveSpidersKilled, creepersKilled, zombiesKilled + skeletonsKilled + spidersKilled + caveSpidersKilled + creepersKilled);
		 user.setMobsKilled(updatedMobsKilled);
		 userDao.update(user);
	   	}
   
   
   @EventHandler
   public void onEntityDeath(EntityDeathEvent event) throws SQLException {
   if (event.getEntity().getKiller() != null) {
	   Player player = event.getEntity().getKiller();
	   Entity entityKilled = event.getEntity();
	   String entityName = entityKilled.getType().toString();
	   boolean needsUpdate = false;
	   List<String> entityNames = List.of("ZOMBIE", "SKELETON", "SPIDER", "CAVE_SPIDER", "CREEPER");
	   if(entityNames.contains(entityName)) {
		   needsUpdate = true;
       		}
	   if(needsUpdate) {
		   editMobsKilled(player, entityName);
	   }
   		}
   }
   @EventHandler
   public void onPlayerDamage(EntityDamageEvent event) {
       try {
           if (event.getEntity() instanceof Player) {
               Player player = (Player) event.getEntity();
               User user = prepareUser(player);
               int random;

               if (player.hasPermission("myplugin.level5_effect_vitality") && event.getDamage() >= 1.0D) {
                   random = this.rand.nextInt(10);
                   if (random <= 1) {
                       player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 20, 2));
                   }
               }

               if (player.hasPermission("myplugin.level10_effect_vitality") && event.getDamage() >= 1.0D) {
                   random = this.rand.nextInt(10);
                   if (random <= 1) {
                       player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 30, 2));
                   }

                   random = this.rand.nextInt(10);
                   if (random <= 1) {
                       event.setDamage(event.getDamage() / 2.0D);
                   }
               }
                this.userDao.update(user);
                
               // Call addVitalityExp no matter the damage taken
                System.out.println(event.getDamage()/2);
               addVitalityExp(player, event.getDamage()/2);
               if (user.getVitality() == 5) {
                   Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + player.getName() + " permission set myplugin.level5_effect_vitality true");
                   Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "atrribute " + player.getName() + " minecraft:generic.max_health base set 22.0");
               }

               if (user.getVitality() == 10) {
                   Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + player.getName() + " permission unset myplugin.level5_effect_vitality");
                   Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "lp user " + player.getName() + " permission set myplugin.level10_effect_vitality true");
                   Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "atrribute " + player.getName() + " minecraft:generic.max_health base set 24.0");
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
                       }
                   }).runTaskTimer(this.plugin, 0L, 2400L);
               }
               
           }
       } catch (SQLException e) {
           e.printStackTrace();
       }
   }
}
    
