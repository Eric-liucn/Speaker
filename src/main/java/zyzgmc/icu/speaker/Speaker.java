package zyzgmc.icu.speaker;

import com.google.inject.Inject;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.plugin.Dependency;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Text;

@Plugin(
        id = "speaker",
        name = "Speaker",
        description = "Sponge端的自动广播插件",
        url = "zyzgmc.icu",
        authors = {
                "EricLiu"
        },
        dependencies = {
                @Dependency(id = "None")
        }
)
public class Speaker {

    public static Speaker instance;

    @Inject
    private Logger logger;

    @Listener
    public void onServerStart(GameStartedServerEvent event) {

        instance = this;

        CommandSpec fixtime = CommandSpec.builder()
                .description(Text.of("设置固定模式的广播时间点"))
                .permission("speaker.command.fixtime")
                .arguments()
                .executor(new SetFixtime())
                .build();

        CommandSpec interval = CommandSpec.builder()
                .description(Text.of("设置间隔模式的间隔时间"))
                .permission("speaker.command.interval")
                .arguments()
                .executor(new SetInterval())
                .build();

        CommandSpec mode = CommandSpec.builder()
                .description(Text.of("设置插件工作模式：定时/间隔"))
                .permission("speaker.command.mode")
                .arguments()
                .executor(new ModeSwith())
                .build();

        CommandSpec set = CommandSpec.builder()
                .description(Text.of("设置公告信息"))
                .permission("speaker.command.set")
                .arguments(
                        GenericArguments.onlyOne(GenericArguments.integer(Text.of("<序号>"))),
                        GenericArguments.remainingJoinedStrings(Text.of("<公告内容>"))
                )
                .executor(new SetMessage())
                .build();

        CommandSpec help = CommandSpec.builder()
                .description(Text.of("显示帮助信息"))
                .permission("speaker.command.base")
                .executor(new Help())
                .build();

        CommandSpec speaker = CommandSpec.builder()
                .description(Text.of("显示版本信息"))
                .permission("speaker.command.base")
                .child(set,"s")
                .child(help)
                .child(mode,"m")
                .child(interval,"i")
                .child(fixtime,"f")
                .executor(new Vmessage())
                .build();


        Sponge.getCommandManager().register(instance, speaker,"spk")
    }
}
