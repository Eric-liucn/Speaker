package zyzgmc.icu.speaker.command.childCommandofSet;

import ninja.leaping.configurate.objectmapping.ObjectMappingException;
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
import zyzgmc.icu.speaker.tasks.FixTimeTask;
import zyzgmc.icu.speaker.tasks.FixTimerCancel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

import static zyzgmc.icu.speaker.tasks.InitialTimer.fixTimerMap;

public class fixTimeChange implements CommandExecutor {

    public static boolean timeIsValid(String timePoint){
        return Pattern.matches("^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$", timePoint);
    }

    public static boolean isValid(String FixTime) {
        String[] testList = FixTime.split("/");
        for (String i:testList
             ) {
            if (timeIsValid(i)) {

            }else {
                return false;
            }
        }
        return true;
    }

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        String name = args.<String>getOne("公告名称").get();
        String newFixTime = args.<String>getOne("固定时间点").get();

        if(isValid(newFixTime)) {

            String[] timeList = newFixTime.split("/");

            if (!Config.rootNode.getNode("All", name).isVirtual()) {
                Config.rootNode.getNode("All", name, "FixTime").setValue(Arrays.asList(timeList));
                try {
                    Config.save();
                    Config.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if(Objects.equals(Config.rootNode.getNode("All", name, "ModeCode").getString(), "fix"))
                {
                    FixTimerCancel.cancelFixTimer(name);
                    try {
                        fixTimerMap.put(name,FixTimeTask.fixTask(name));
                    } catch (ObjectMappingException e) {
                        e.printStackTrace();
                    }
                }else {
                    src.sendMessage(Text.of(TextColors.RED,"该公告目前是间隔时间模式,改动将不会影响当前公告任务"));
                }

                src.sendMessage(TextSerializers.FORMATTING_CODE.deserialize(String.format("&a已将公告 &e%s &a的广播时间点改为 &e%s", name, newFixTime)));
            } else {
                src.sendMessage(Text.of(TextColors.RED, "该公告不存在，请检查名称！"));
            }
        }else {
            src.sendMessage(Text.of(TextColors.RED,"时间输入格式有误，请检查格式以及符号是否为英文冒号，例：09:00/12:00/18:00"));
        }


        return CommandResult.success();
    }




    public static CommandSpec build(){
        return CommandSpec.builder()
                .description(Text.of("修改指定公告的间隔时间"))
                .permission("speaker.command.set.fix")
                .arguments(
                        GenericArguments.seq(
                                GenericArguments.onlyOne(
                                        GenericArguments.withSuggestions(
                                                GenericArguments.string(Text.of("公告名称")), Config.nameCompletion
                                        )
                                ),
                                GenericArguments.remainingJoinedStrings(Text.of("固定时间点"))
                        )
                )
                .executor(new fixTimeChange())
                .build();
    }
}
