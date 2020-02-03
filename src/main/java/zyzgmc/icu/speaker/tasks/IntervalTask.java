package zyzgmc.icu.speaker.tasks;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.serializer.TextSerializers;
import zyzgmc.icu.speaker.Speaker;
import zyzgmc.icu.speaker.config.Config;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;


public class IntervalTask {


    public static String thisName;
    private static Task task;
    public static Long wait = 100L;

    public static void build(String name) throws InterruptedException {
        thisName = name;
        Integer intervalSec = Config.rootNode.getNode("All",thisName,"Interval").getInt();

        task = Task.builder()
                .name(thisName)
                .async()
                .interval(intervalSec, TimeUnit.SECONDS)
                .delay(wait,TimeUnit.MILLISECONDS)
                .execute(() ->TaskMethod())
                .submit(Speaker.instance);
    }

    private static void TaskMethod() {
        if(Config.rootNode.getNode("All",thisName,"Enable").getBoolean()){
            Sponge.getServer().getBroadcastChannel().send(
                    TextSerializers.FORMATTING_CODE.deserialize(
                            Config.rootNode.getNode("All",thisName,"Content").getString()
                    )
            );
        }else {
            task.cancel();
        }

    }
}
