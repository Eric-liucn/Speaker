package zyzgmc.icu.speaker.command;

import org.spongepowered.api.boss.BossBarColors;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.serializer.TextSerializers;
import zyzgmc.icu.speaker.config.Config;
import zyzgmc.icu.speaker.tasks.IntervalTask;

import java.io.IOException;
import java.util.Arrays;

import static zyzgmc.icu.speaker.tasks.InitialTimer.timerMap;

public class Add implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        String Name = args.<String>getOne(Text.of("公告名称")).get();
        String Message = args.<String>getOne(Text.of("公告内容")).get();
        String[] Default = {"12:00"};
        if(!Config.rootNode.getNode("All",Name).isVirtual()){
            src.sendMessage(TextSerializers.FORMATTING_CODE.deserialize(String.format("名为 &c%s &r的公告已存在，使用&e/spk set content 名称 内容 &r来修改它",Name)));
        }else {
            src.sendMessage(TextSerializers.FORMATTING_CODE.deserialize(String.format("&a添加公告&e%s&a,内容为&r%s", Name, Message)));
            Config.rootNode.getNode("All", Name, "Content").setValue(Message);
            Config.rootNode.getNode("All", Name, "Enable").setValue(true);
            Config.rootNode.getNode("All", Name, "ModeCode").setValue("interval");
            Config.rootNode.getNode("All", Name, "Interval").setValue(45);
            Config.rootNode.getNode("All", Name, "FixTime").setValue(Arrays.asList(Default));
            Config.rootNode.getNode("All", Name, "Hover").setValue("使用 /spk set hover 公告名称 内容  来设置鼠标悬浮公告时显示的信息");
            Config.rootNode.getNode("All", Name, "Url").setValue("https://www.baidu.com");
            Config.rootNode.getNode("All", Name, "Cmd").setValue("/say 使用/spk set cmd 公告名称 指令  来设置点击公告时触发的指令");
            Config.rootNode.getNode("All", Name, "Display").setValue("normal");
            Config.rootNode.getNode("All", Name, "Setting","Title","持续时间").setValue(2);
            Config.rootNode.getNode("All", Name, "Setting","Title","淡入时间").setValue(1);
            Config.rootNode.getNode("All", Name, "Setting","Title","淡出时间").setValue(1);
            Config.rootNode.getNode("All", Name, "Setting","Boss","持续时间").setValue(10);
            Config.rootNode.getNode("All", Name, "Setting","Boss","颜色").setValue("BossBarColors.PURPLE");
            Config.rootNode.getNode("All",Name,"Join").setValue(false);
            try {
                Config.save();
                Config.load();
            } catch (IOException e) {
                e.printStackTrace();
            }
            timerMap.put(Name,IntervalTask.intervalTask(Name));
        }

        return CommandResult.success();
    }

    public static CommandSpec build(){
        return CommandSpec.builder()
                .description(Text.of("添加一条广播"))
                .permission("speaker.command.add")
                .arguments(GenericArguments.seq(
                        GenericArguments.onlyOne(GenericArguments.string(Text.of("公告名称"))),
                        GenericArguments.remainingJoinedStrings(Text.of("公告内容"))
                        )
                )
                .executor(new Add())
                .build();
    }
}
