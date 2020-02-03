package zyzgmc.icu.speaker.tasks;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.text.serializer.TextSerializers;
import zyzgmc.icu.speaker.config.Config;

import java.util.Timer;
import java.util.TimerTask;


public class Test {
    public static Timer thisTimer;
    public static String timerName;

    public static void intervalTask(String name){
        Integer i = Config.rootNode.getNode("All",name,"Interval").getInt();
        String str = Config.rootNode.getNode("All",name,"Content").getString();
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if(Config.rootNode.getNode("All",name,"Enable").getBoolean()){

                    Sponge.getServer().getBroadcastChannel().send(TextSerializers.FORMATTING_CODE.deserialize(str));

                }else {

                }


            }
        }, 1000, i * 1000);
        thisTimer = timer;
    }
}
