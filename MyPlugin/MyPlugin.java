    package mingle.MyPlugin;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import org.bukkit.plugin.java.JavaPlugin;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;

import mingle.MyPlugin.entities.User;



@SuppressWarnings("resource")
public final class MyPlugin extends JavaPlugin {


    private static final long RECREATE_INTERVAL_HOURS = 7;
    private static String databaseUrl;
    private ConnectionSource connectionSource;
    private Dao<User, Integer> userDao;
    private ScheduledExecutorService executorService;

    public MyPlugin() throws SQLException {
        loadDatabaseUrlFromFile();
        createConnectionSource();
        this.userDao = DaoManager.createDao(this.connectionSource, User.class);
        scheduleRecreateConnectionSourceTask();
    }

    private void loadDatabaseUrlFromFile() {
        Path path = Paths.get("/home/ubuntu/PluginTest/plugins/MyPluginConfig/jdbc.txt");
        try (BufferedReader reader = new BufferedReader(new FileReader(path.toFile(), StandardCharsets.UTF_8))) {
            String line = reader.readLine();
            databaseUrl = line + "?autoReconnect=true";
            System.out.println("Read line from file: " + line);
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
    }

    private void createConnectionSource() throws SQLException {
        this.connectionSource = new JdbcConnectionSource(MyPlugin.databaseUrl);
    }

    private void scheduleRecreateConnectionSourceTask() {
        executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleWithFixedDelay(this::recreateConnectionSource, RECREATE_INTERVAL_HOURS, RECREATE_INTERVAL_HOURS, TimeUnit.HOURS);
    }

    private synchronized void recreateConnectionSource() {
        try {
            this.connectionSource.close();
            createConnectionSource();
            this.userDao = DaoManager.createDao(this.connectionSource, User.class);
            System.out.println("Re-created connection source");
        } catch (Exception e) {
            System.err.println("Error re-creating connection source: " + e.getMessage());
        }
    }




   public void onEnable() {
      super.onEnable();
      this.getLogger().log(Level.INFO, "MyPlugin Sucessfully Loaded!");
      this.getCommand("mining_level5").setExecutor(new MiningCommands(this));
      this.getCommand("mining_level10").setExecutor(new MiningCommands(this));
      this.getCommand("stats").setExecutor(new SkillCommands(this, this.userDao));
      this.getCommand("backpack_levelup").setExecutor(new SkillCommands(this, this.userDao));
      this.getCommand("recipe_book").setExecutor(new MiscCommands(this, this.userDao));
      this.getCommand("kills").setExecutor(new MiscCommands(this, this.userDao));
      this.getCommand("ores").setExecutor(new MiscCommands(this, this.userDao));
      new MyEventListener(this, this.userDao);
      new LoginListener(this, this.userDao);
      new ExpChangeListener(this, this.userDao);
   }

   public void onDisable() {
   }
}