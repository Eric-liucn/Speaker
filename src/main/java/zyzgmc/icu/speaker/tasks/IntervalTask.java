package zyzgmc.icu.speaker.tasks;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.text.serializer.TextSerializers;
import zyzgmc.icu.speaker.config.Config;
import zyzgmc.icu.speaker.textBuild.TextBuilder;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;


public class IntervalTask {
    public static Timer thisTimer;

    public static Timer intervalTask(String name){
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if(Config.rootNode.getNode("All",name,"Enable").getBoolean()
                        && Objects.equals(Config.rootNode.getNode("All", name, "ModeCode").getString(), "interval")){

                    try {
                        TextBuilder.builderAndSender(name);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    Sponge.getServer().getConsole().sendMessage(
                            TextSerializers.FORMATTING_CODE.deserialize(
                                    Config.rootNode.getNode("All",name,"Content").getString()
                            )
                    );

                }else {

                }


            }
        }, 1000, Config.rootNode.getNode("All",name,"Interval").getInt() * 1000);
        thisTimer = timer;
        return thisTimer;
    }
}
