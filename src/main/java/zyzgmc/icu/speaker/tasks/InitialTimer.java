package zyzgmc.icu.speaker.tasks;

import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.text.serializer.TextSerializers;
import zyzgmc.icu.speaker.config.Config;
import zyzgmc.icu.speaker.tasks.IntervalTask;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;

public class InitialTimer {

    //构建一个Map储存所有interval timer
    public static Map<String,Timer> timerMap=new HashMap<String, Timer>();

    //构建一个Map储存所有fix timer
    public static Map<String, Map<String,Timer>> fixTimerMap = new HashMap<String, Map<String, Timer>>();

    public static void initialTask() {
        for (String name : Config.nameCompletion) {
            // 开始间隔模式任务
            if (Config.rootNode.getNode("All", name, "ModeCode").getString().equals("interval")) {
                Sponge.getServer().getConsole().sendMessage(
                        TextSerializers.FORMATTING_CODE.deserialize(
                        String.format("&a找到间隔模式广播任务 &e%s &a状态为： &c%b", name,
                                Config.rootNode.getNode("All",name,"Enable").getBoolean())
                        )
                );
                IntervalTask test = new IntervalTask();
                double delay = Config.rootNode.getNode("All",name,"DelayOnStart").getDouble();
                timerMap.put(name,test.intervalTask(name,delay));
            } else if (Config.rootNode.getNode("All",name,"ModeCode").getString().equals("fix")){
                Sponge.getServer().getConsole().sendMessage(
                        TextSerializers.FORMATTING_CODE.deserialize(
                                String.format("&a找到固定模式广播任务 &e%s &a状态为： &c%b", name,
                                        Config.rootNode.getNode("All",name,"Enable").getBoolean())
                        )
                );
                try {
                    fixTimerMap.put(name,FixTimeTask.fixTask(name));
                }catch (ObjectMappingException e){
                    e.printStackTrace();
                }
            }
        }
    }
}
