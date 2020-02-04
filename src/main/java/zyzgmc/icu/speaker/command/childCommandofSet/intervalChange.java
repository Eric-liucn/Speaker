package zyzgmc.icu.speaker.command.childCommandofSet;

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
import zyzgmc.icu.speaker.tasks.TimerTaskCancel;

import java.io.IOException;

import static zyzgmc.icu.speaker.tasks.InitialTimer.timerMap;

public class intervalChange implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) {

        String name = args.<String>getOne("公告名称").get();
        Integer newInterval = args.<Integer>getOne("间隔时间").get();


        if(!Config.rootNode.getNode("All",name).isVirtual()){

            Config.rootNode.getNode("All",name,"Interval").setValue(newInterval);

            try{
                Config.save();
                Config.load();
            }catch (IOException e){
                e.printStackTrace();
            }

            src.sendMessage(
                    TextSerializers.FORMATTING_CODE.deserialize(
                    String.format("&a已将公告 &e%s &a的间隔时间改为 &e%d 秒",name,newInterval)
                    )
            );

            TimerTaskCancel.cancelTask(name);
            timerMap.put(name,IntervalTask.intervalTask(name));

        }else {
            src.sendMessage(Text.of(TextColors.RED,"该公告不存在，请检查名称！"));
        }


        return CommandResult.success();
    }
    public static CommandSpec build(){
        return CommandSpec.builder()
                .description(Text.of("修改指定公告的间隔时间"))
                .permission("speaker.command.set.content")
                .arguments(
                        GenericArguments.seq(
                                GenericArguments.onlyOne(
                                        GenericArguments.withSuggestions(
                                                GenericArguments.string(Text.of("公告名称")),
                                                Config.nameCompletion
                                        )
                                ),
                                GenericArguments.onlyOne(GenericArguments.integer(Text.of("间隔时间")))
                        )
                )
                .executor(new intervalChange())
                .build();
    }
}
