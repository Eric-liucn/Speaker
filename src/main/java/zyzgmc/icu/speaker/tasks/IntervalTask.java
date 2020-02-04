package zyzgmc.icu.speaker.tasks;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.text.serializer.TextSerializers;
import zyzgmc.icu.speaker.config.Config;

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
                        && Config.rootNode.getNode("All",name,"ModeCode").getString().equals("interval")){

                    Sponge.getServer().getBroadcastChannel().send(
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
