package zyzgmc.icu.speaker;

import com.google.inject.Inject;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.game.GameReloadEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.api.service.pagination.PaginationList;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.serializer.TextSerializers;
import zyzgmc.icu.speaker.command.*;
import zyzgmc.icu.speaker.command.Set;
import zyzgmc.icu.speaker.config.Config;
import zyzgmc.icu.speaker.tasks.FixTimeTask;
import zyzgmc.icu.speaker.tasks.InitialTimer;
import zyzgmc.icu.speaker.tasks.IntervalTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import static zyzgmc.icu.speaker.tasks.InitialTimer.fixTimerMap;
import static zyzgmc.icu.speaker.tasks.InitialTimer.timerMap;

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

    //构造一个实例
    public static Speaker instance;

    public static EconomyService economyService;



    @Inject
    private PluginContainer container;

    @Inject
    Logger logger;


    public static Logger getLogger(){
        return instance.logger;
    }

    public static PluginContainer getContainer(){
        return instance.container;
    }


    @Inject
    @ConfigDir(sharedRoot = false)
    public File folder;


    @Listener
    public void reload(GameReloadEvent event) throws IOException {
        try {
            Config.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    @Listener
    public void onServerStart(GameStartedServerEvent event) {

        try {
            Config.setup(folder);
            Config.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        instance = this;

        CommandSpec speaker = CommandSpec.builder()
                .description(Text.of("显示版本信息"))
                .permission("speaker.command.base")
                .child(Set.build(), "set")
                .child(Help.build(), "help")
                .child(Add.build(), "add")
                .child(ListCmd.build(), "list")
                .child(Show.build(), "show")
                .child(Reload.build(), "reload")
                .child(Remove.build(), "remove", "delete")
                .executor(new Vmessage())
                .build();


        Sponge.getCommandManager().register(this, speaker, "speaker", "spk");


        List<Text> welcome = new ArrayList<Text>();
        welcome.add(TextSerializers.FORMATTING_CODE.deserialize("&bSpeaker &6已加载"));
        PaginationList.builder()
                .title(Text.of(TextColors.LIGHT_PURPLE,"SPEAKER"))
                .padding(Text.of(TextColors.GREEN))
                .contents(welcome)
                .sendTo(Sponge.getServer().getConsole());

        InitialTimer.initialTask();


        Optional<EconomyService> serviceOpt = Sponge.getServiceManager().provide(EconomyService.class);
        if (!serviceOpt.isPresent()) {
            PaginationList.builder()
                    .title(Text.of(TextColors.YELLOW,"注意"))
                    .contents(Text.of(TextColors.RED,"Speaker未检测到经济插件，部分功能将无法使用"))
                    .padding(Text.of(TextColors.GREEN,"="))
                    .sendTo(Sponge.getServer().getConsole());

        }else {
            economyService = serviceOpt.get();
        }

        }

}
