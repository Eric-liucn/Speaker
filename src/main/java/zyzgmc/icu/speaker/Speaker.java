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
import org.spongepowered.api.text.serializer.TextSerializers;
import zyzgmc.icu.speaker.command.*;
import zyzgmc.icu.speaker.config.Config;

import java.io.File;
import java.io.IOException;

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
    public Logger logger;

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

        CommandSpec speaker = CommandSpec.builder()
                .description(Text.of("显示版本信息"))
                .permission("speaker.command.base")
                .child(SetMessage.build(),"set","s")
                .child(Help.build(),"help")
                .child(ModeSwith.build(),"mode","m")
                .child(SetInterval.build(),"interval","i")
                .child(SetFixtime.build(),"fixtime","f")
                .child(Add.build(),"add")
                .child(ListCmd.build(), "list")
                .child(Show.build(),"show")
                .child(Reload.build(),"reload")
                .executor(new Vmessage())
                .build();

        Sponge.getCommandManager().register(this, speaker,"speaker","spk");
        logger.info("插件成功加载");
        System.out.println(Config.rootNode.getNode("All"));
    }
}
