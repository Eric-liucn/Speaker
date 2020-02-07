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
        nameCompletion.clear();
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
        rootNode.getNode("All","欢迎","Content").setValue("&b[公告] &e玩家 &d{Player} &e当前所在的世界是 &d{World} &e， 他的余额是 &d{Balance}");
        rootNode.getNode("All","欢迎","Enable").setValue(true);
        rootNode.getNode("All","欢迎","ModeCode").setValue("interval").setComment("fix --> 固定时间模式\ninterval --> 间隔时间模式");
        rootNode.getNode("All","欢迎","Interval").setValue(45).setComment("单位：秒");
        rootNode.getNode("All","欢迎","FixTime").setValue(Arrays.asList(realtime));
        rootNode.getNode("All", "欢迎", "Hover").setValue("&a使用 &e/spk set hover 公告名称 内容  &a来设置鼠标悬浮公告时显示的信息");
        rootNode.getNode("All", "欢迎", "Url").setValue("None").setComment("例：https://www.baidu.com(仅输入域名无效，错误示例： baidu.com 或者 www.baidu.com) \n如果要启用点击公共打开链接，请将Cmd设置为None");
        rootNode.getNode("All", "欢迎", "Cmd").setValue("/say 使用/spk set cmd 公告名称 指令  来设置点击公告时触发的指令");
        rootNode.getNode("All", "欢迎", "Display").setValue("normal");
        rootNode.getNode("All", "欢迎", "Setting","Title","持续时间").setValue(40);
        rootNode.getNode("All", "欢迎", "Setting","Title","淡入时间").setValue(20);
        rootNode.getNode("All", "欢迎", "Setting","Title","淡出时间").setValue(20);
        rootNode.getNode("All", "欢迎", "Setting","Boss","持续时间").setValue(10);
        rootNode.getNode("All", "欢迎", "Setting","Boss","颜色").setValue("BossBarColors.PURPLE");
    }



}
