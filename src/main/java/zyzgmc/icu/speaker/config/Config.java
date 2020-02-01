package zyzgmc.icu.speaker.config;

import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import zyzgmc.icu.speaker.Speaker;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;


public class Config {

    public static File folder, config;
    public static ConfigurationLoader<CommentedConfigurationNode> loader;
    public static CommentedConfigurationNode rootNode;


    public static void setup(File myfolder){
        folder =myfolder;
    }

    public static void load() throws IOException {
        if(!folder.exists()){
            folder.mkdir();
        }
        config = new File(folder,"speaker.conf");
        loader = HoconConfigurationLoader.builder().setFile(config).build();

        if (!config.exists()){
            config.createNewFile();
            rootNode = loader.load();
            addValue();
            loader.save(rootNode);
        }
        rootNode = loader.load();
    }

    public static void save() throws IOException {
        loader.save(rootNode);
    }

    /*public static int modeCode_f = 1;
    public static int modeCode_i = 2;
    public static String name_1 = "欢迎";
    public static String name_2 = "帮助";
    public static String default_1 = "&e欢迎使用Speaker插件";
    public static String default_2 = "&a使用/speaker查看版本号";
    public static String[] realtime = {
            "09:00",
            "12:00",
            "18:00",
            "20:00"

    };

     */
    public static String[] realtime = {
            "09:00",
            "12:00"
    };

    public static void addValue(){
        rootNode.getNode("All","欢迎","Content").setValue("&e欢迎使用Speaker插件");
        rootNode.getNode("All","欢迎","Enable").setValue(true);
        rootNode.getNode("All","欢迎","ModeCode").setValue(2);
        rootNode.getNode("All","欢迎","Interval").setValue(10);
        rootNode.getNode("All","欢迎","FixTime").setValue(Arrays.asList(realtime));
    }



}
