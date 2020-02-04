package zyzgmc.icu.speaker.config;

import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.spongepowered.api.Sponge;
import zyzgmc.icu.speaker.Speaker;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;


public class Config {

    public static File folder, config;
    public static ConfigurationLoader<CommentedConfigurationNode> loader;
    public static CommentedConfigurationNode rootNode;
    public static List<String> nameCompletion =new ArrayList<String>();


    public static void setup(File myfolder){
        folder =myfolder;
    }

    public static void load() throws IOException {
        if(!folder.exists()){
            folder.mkdir();
        }
        config = new File(folder,"speaker.conf");
        loader = HoconConfigurationLoader.builder().setFile(config).build();

        //判断Config文件是否存在，不存在的划创建文件，并添加值
        if (!config.exists()){
            config.createNewFile();
            rootNode = loader.load();
            addValue();
            loader.save(rootNode);
        }
        rootNode = loader.load();
        if(Config.rootNode.getNode("All").isVirtual()){
            addValue();
        }else {}
        // 构建一个包含所有公共名称的List用于指令补全
        for (Object name:rootNode.getNode("All").getChildrenMap().keySet()
        ) {String nameString = (String) name;
            nameCompletion.add(nameString);
        }

    }

    //保存配置
    public static void save() throws IOException {
        loader.save(rootNode);
    }

    //声明一个数组用于初始值
    public static String[] realtime = {
            "09:00",
            "12:00"
    };

    //设置初始值
    public static void addValue(){
        rootNode.getNode("All","欢迎","Content").setValue("&e欢迎使用Speaker插件");
        rootNode.getNode("All","欢迎","Enable").setValue(true);
        rootNode.getNode("All","欢迎","ModeCode").setValue("fix").setComment("fix --> 固定时间模式\ninterval --> 间隔时间模式");
        rootNode.getNode("All","欢迎","Interval").setValue(45).setComment("单位：秒");
        rootNode.getNode("All","欢迎","FixTime").setValue(Arrays.asList(realtime));
    }



}
