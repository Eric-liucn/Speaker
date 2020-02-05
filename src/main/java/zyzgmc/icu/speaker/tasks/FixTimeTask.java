package zyzgmc.icu.speaker.tasks;

import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.text.serializer.TextSerializers;
import zyzgmc.icu.speaker.config.Config;
import zyzgmc.icu.speaker.textBuild.TextBuilder;

import java.time.LocalDateTime;
import java.util.*;

public class FixTimeTask {

    public static Map<String, Timer> fixTask(String name) throws ObjectMappingException {
        Map<String,Timer> timePointMap = new HashMap<String, Timer>();

        for (Object timePointO: Config.rootNode.getNode("All",name,"FixTime").getList(TypeToken.of(String.class))
             ) {
            String timePointS = (String) timePointO;
            int delay = getDelay(timePointS);

            if(delay>0)
            {
                Timer timer = new Timer();
                timer.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        if(
                                Config.rootNode.getNode("All",name,"Enable").getBoolean()
                                && Objects.equals(Config.rootNode.getNode("All", name, "ModeCode").getString(), "fix")
                        ){
                          TextBuilder.builderAndSender(name);
                          Sponge.getServer().getConsole().sendMessage(
                                  TextSerializers.FORMATTING_CODE.deserialize(
                                          Objects.requireNonNull(Config.rootNode.getNode("All", name, "Content").getString())
                                  )
                          );

                        }
                    }
                },delay*1000,24*60*60*1000);

                timePointMap.put(timePointS,timer);
            }else {
                String[] notTodaySplit = timePointS.split(":");
                int notTodayDelay = getDelay("24:00")+ Integer.parseInt(notTodaySplit[0])*3600+Integer.parseInt(notTodaySplit[1])*60;
                Timer timer = new Timer();
                timer.scheduleAtFixedRate(new TimerTask() {
                    @Override
                    public void run() {
                        if(
                                Config.rootNode.getNode("All",name,"Enable").getBoolean()
                                        && Objects.equals(Config.rootNode.getNode("All", name, "ModeCode").getString(), "fix")
                        ){
                            TextBuilder.builderAndSender(name);
                            Sponge.getServer().getConsole().sendMessage(
                                    TextSerializers.FORMATTING_CODE.deserialize(
                                            Objects.requireNonNull(Config.rootNode.getNode("All", name, "Content").getString())
                                    )
                            );
                        }
                    }
                },notTodayDelay*1000,24*60*60*1000);

            }
        }
        return timePointMap;

    }

    public static Integer getDelay(String timePointS){
        LocalDateTime currentTime = LocalDateTime.now();
        String[] timePointSplit = timePointS.split(":");
        Integer timePointSecs = Integer.parseInt(timePointSplit[0])*3600 + Integer.parseInt(timePointSplit[1])*60;
        Integer CurrentSecs = currentTime.getHour()*3600+currentTime.getMinute()*60+currentTime.getSecond();

        return timePointSecs - CurrentSecs;
    }

}
