package mingle.MyPlugin.entities;

import java.util.UUID;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "users")
public class User {
  @DatabaseField(id = true)
  private UUID uniqueId;
  
  @DatabaseField
  private String username;
  
  @DatabaseField
  private int woodworking;
  
  @DatabaseField
  private int mining;
  
  @DatabaseField
  private int vitality;
  
  @DatabaseField
  private double woodworkingExp;
  
  @DatabaseField
  private double miningExp;
  
  @DatabaseField
  private double vitalityExp;
  
  @DatabaseField
  private int runregen;
  
  @DatabaseField
  private int regenTaskID;
  
  @DatabaseField
  private String mobsKilled;
  
  @DatabaseField
  private String oresMined;
  
  public void setUniqueId(UUID uuid) {
    this.uniqueId = uuid;
  }
  
  public UUID getUniqueId() {
    return this.uniqueId;
  }
  
  public void setUsername(String username) {
    this.username = username;
  }
  
  public String getUsername() {
    return this.username;
  }
  
  public void setWoodworking(int woodworking) {
    this.woodworking = woodworking;
  }
  
  public int getWoodworking() {
    return this.woodworking;
  }
  
  public void setMining(int mining) {
    this.mining = mining;
  }
  
  public int getMining() {
    return this.mining;
  }
  
  public void setVitality(int vitality) {
    this.vitality = vitality;
  }
  
  public int getVitality() {
    return this.vitality;
  }
  
  public void setRunRegen(int runregen) {
    this.runregen = runregen;
  }
  
  public int getRunRegen() {
    return this.runregen;
  }
  
  public void setRegenTaskID(int regenTaskID) {
    this.regenTaskID = regenTaskID;
  }
  
  public int getRegenTaskID() {
    return this.regenTaskID;
  }
  
  public void setWoodworkingExp(double woodworkingExp) {
    this.woodworkingExp = woodworkingExp;
  }
  
  public double getWoodworkingExp() {
    return this.woodworkingExp;
  }
  
  public void setMiningExp(double miningExp) {
    this.miningExp = miningExp;
  }
  
  public double getMiningExp() {
    return this.miningExp;
  }
  
  public void setVitalityExp(double vitalityExp) {
    this.vitalityExp = vitalityExp;
  }
  
  public double getVitalityExp() {
    return this.vitalityExp;
  }
  
  public void setMobsKilled(String mobsKilled) {
	  this.mobsKilled = mobsKilled;
  }
  
  public String getMobsKilled() {
	  return this.mobsKilled;
  }
  
  public void setOresMined(String oresMined) {
	  this.oresMined = oresMined;
  }
  
  public String getOresMined() {
	  return this.oresMined;
  }
}