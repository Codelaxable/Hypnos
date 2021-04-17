package re.cod.hypnos.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import re.cod.hypnos.HypnosMod;

import java.io.*;

public class Config {
    private static Config instance;
    final File configFolder = new File("config");
    final File configFile = new File(configFolder ,"hypnos.json");
    final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    public Data data = new Config.Data();

    public static class Data {
        public int playerPercentage = 50;
        public String notEnoughPlayerMessage = "§9{player} entered bed, not enought to skip night [{sleeping}/{required}]§r";
        public String nightSkipMessage = "§9Enough players are sleeping, skipping night§r";
        public String wakeUpMessage = "§9{player} leaved bed§r";
        public boolean enableWakeUp = false;

        public void validate() {
            if (this.playerPercentage < 0 || this.playerPercentage > 100) {
                HypnosMod.LOGGER.error("Config: PlayerPercentage is invalid: using default");
                this.playerPercentage = 50;
            }
        }
    }

    Config() {
        try {
            this.loadConfig();
        } catch (IOException e) {
            if (e instanceof FileNotFoundException) {
                HypnosMod.LOGGER.info("Config file not found: creating default in " + configFile);
                this.saveConfig();
            } else {
                HypnosMod.LOGGER.error("Failed to load config file: " + configFile);
                e.printStackTrace();
            }
        }
    }

    public void loadConfig() throws IOException {
        FileReader fw = new FileReader(this.configFile);
        this.data = gson.fromJson(fw, Data.class);
        fw.close();
        this.data.validate();
    }

    public void saveConfig() {
        try {
            if (!configFolder.exists())
                configFolder.mkdirs();
            if (!configFile.exists())
                configFile.createNewFile();
            FileWriter fw = new FileWriter(this.configFile, false);
            gson.toJson(this.data, fw);
            fw.close();
        } catch (IOException e) {
            HypnosMod.LOGGER.error("Failed to create or write config file: " + configFile);
            e.printStackTrace();
        }
    }

    public static Config get() {
        if (Config.instance == null) {
            Config.instance = new Config();
        }
        return Config.instance;
    }
}
