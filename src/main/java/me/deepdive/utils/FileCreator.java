package me.deepdive.utils;

import lombok.Getter;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;


/**
 * This API class is used to create configuration files for any Minecraft plugin.
 * Simply make a class, extend it, and fill in the super constructor parameters, and you're all set.
 * Automatically supports folders by adding it to the path.
 */
public abstract class FileCreator {

    @Getter
    private final String configName;

    @Getter
    protected YamlConfiguration config = new YamlConfiguration();

    private final JavaPlugin instance;

    /**
     * The constructor to create a new file. Everything is handled automatically, just put in the right constructor parameters and you're all set
     * @param path The path of the file, this can include folders. Use /'s to add folders, ability to stack them
     * @param configName The name of the configuration file, exclude the extension!
     * @param instance The plugin instance
     */
    public FileCreator(String path, String configName, JavaPlugin instance){
        this.configName = configName;
        this.instance = instance;

        try{
            createFiles(path);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    @Getter private File configF;

    public void saveFile(){
        try {
            config.save(configF);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void createFiles(String path) throws IOException {
        configF = new File(instance.getDataFolder().getAbsolutePath() + path, configName + ".yml");

        if(!configF.exists()){
            configF.getParentFile().mkdirs();
            String replaced = path.replaceFirst("/", "");
            instance.saveResource(replaced + "/" + configName + ".yml", false);
        }

        try{
            config.load(configF);
        }catch(IOException | InvalidConfigurationException e){
            e.printStackTrace();
        }
    }

    /**
     * Reload a configuration file, useful for /reload commands.
     * @param path The file path of the file, exclude the configuration file name itself.
     */
    public void reload(String path) {
        saveFile();
        configF = new File(instance.getDataFolder().getAbsolutePath() + path, configName + ".yml");
        try {
            config.load(configF);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
    }

}
