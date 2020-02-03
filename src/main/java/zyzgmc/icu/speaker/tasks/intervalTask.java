package zyzgmc.icu.speaker.tasks;

import org.spongepowered.api.scheduler.Task;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;
import zyzgmc.icu.speaker.Speaker;
import zyzgmc.icu.speaker.config.Config;

import java.util.concurrent.TimeUnit;

public class intervalTask {

    public Task task;

    public void interTask(String name, Integer sec){
        task = Task.builder()
                .name(name)
                .interval(sec, TimeUnit.SECONDS)
                .async()
                .execute(() -> taskMethod())
                .submit(Speaker.getInstance());
    }

    public void taskMethod(){
        String tName = task.getName();
        String text = Config.rootNode.getNode("All",tName,"Content").getString();
        if(Config.rootNode.getNode("All",tName,"Enable").getBoolean()){
            Speaker.server.getBroadcastChannel().send(TextSerializers.FORMATTING_CODE.deserialize(text));
        }else {
            task.cancel();
        }
    }

}
