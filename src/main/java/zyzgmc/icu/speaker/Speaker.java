package zyzgmc.icu.speaker;

import com.google.inject.Inject;
import org.slf4j.Logger;
import org.spongepowered.api.Server;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.source.ConsoleSource;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.game.GameReloadEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColor;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.serializer.TextSerializers;
import zyzgmc.icu.speaker.command.*;
import zyzgmc.icu.speaker.command.Set;
import zyzgmc.icu.speaker.config.Config;
import zyzgmc.icu.speaker.tasks.intervalTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Plugin(
        id = "speaker",
        name = "Speaker",
        description = "Sponge端的自动广播插件",
        url = "https://zyzgmc.icu",
        authors = {
                "EricLiu"
        },
        dependencies = {}
)
public class Speaker {


    @Inject
    Logger logger;

    public static Speaker instance;

    public static Speaker getInstance() {
        Speaker instance = Speaker.instance;
        return instance;
    }

    public static ConsoleSource ConSrc;
    public static Server server;

    @Inject
    @ConfigDir(sharedRoot = false)
    public File folder;


    @Listener
    public void reload(GameReloadEvent event) throws IOException {
        Config.load();
    }


    @Listener
    public void onServerStart(GameStartedServerEvent event) {
        try {
            Config.setup(folder);
            Config.load();
        }catch (IOException e){
            e.printStackTrace();
        }
        try {
            checkTask();
        }finally {
            logger.info("任务初始化失败");
        }

        CommandSpec speaker = CommandSpec.builder()
                .description(Text.of("显示版本信息"))
                .permission("speaker.command.base")
                .child(Set.build(),"set")
                .child(Help.build(),"help")
                .child(Add.build(),"add")
                .child(ListCmd.build(), "list")
                .child(Show.build(),"show")
                .child(Reload.build(),"reload")
                .child(Remove.build(),"remove", "delete")
                .executor(new Vmessage())
                .build();


        Sponge.getCommandManager().register(this, speaker,"speaker","spk");
        logger.info("插件成功加载");


        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        logger.info("当前时间是"+format.format(date));
    }

    public static void checkTask(){
        for(String name:Config.nameCompletion){
            if(Config.rootNode.getNode("All",name,"ModeCode").getString() == "interval"){
                Speaker.ConSrc.sendMessage(TextSerializers.FORMATTING_CODE.deserialize(String.format("&a广播任务 &d%s &a已开始",name)));
                intervalTask inTask = new intervalTask();
                inTask.interTask(name,calSec(Config.rootNode.getNode("All",name,"Interval").getString()));
            }
        }

    }

    public static int calSec(String interval){
        Pattern h = Pattern.compile("^[0-9]*(h|H)+");
        Pattern m = Pattern.compile("^[0-9]*(m|M)+");
        Pattern s = Pattern.compile("^[0-9]*(s|S)+");
        int allSec = 0;
        if(Pattern.matches("^[0-9]*(h|H)+" ,interval)) {
            Matcher hM = h.matcher(interval);
            Integer hSec = Integer.parseInt(hM.group(0).substring(0,hM.group(0).length()-1)) * 3600;
            allSec = allSec+hSec;
        }
        if(Pattern.matches("^[0-9]*(m|M)+" ,interval)){
            Matcher mM = m.matcher(interval);
            Integer mSec = Integer.parseInt(mM.group(0).substring(0,mM.group(0).length()-1)) * 60;
            allSec = allSec+mSec;
        }
        if(Pattern.matches("^[0-9]*(s|S)+",interval)){
            Matcher sM = s.matcher(interval);
            Integer sSec = Integer.parseInt(sM.group(0).substring(0,sM.group(0).length()-1));
            allSec = allSec+sSec;
        }

        return allSec;
    }
}
