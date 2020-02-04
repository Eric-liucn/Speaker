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
import zyzgmc.icu.speaker.tasks.IntervalTask;
import zyzgmc.icu.speaker.tasks.TimerTaskCancel;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

import static zyzgmc.icu.speaker.tasks.FixTimeTask.fixTask;
import static zyzgmc.icu.speaker.tasks.InitialTimer.fixTimerMap;

public class modeChange implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

       String name = args.<String>getOne("公告名称").get();
       String mode = args.<String>getOne("fix->固定模式/interval->间隔模式").get();
       if (!Config.rootNode.getNode("All",name).isVirtual()) {

           if(Config.rootNode.getNode("All",name,"ModeCode").getString().equals(mode)){

               src.sendMessage(
                       TextSerializers.FORMATTING_CODE.deserialize(
                               String.format(
                                       "&c 该公告已经是 &6%s &c模式",
                                       mode
                               )
                       )
               );

           }else {

               Config.rootNode.getNode("All", name, "ModeCode").setValue(mode);
               try {
                   Config.save();
                   Config.load();
               } catch (IOException e) {
                   e.printStackTrace();
               }
               src.sendMessage(TextSerializers.FORMATTING_CODE.deserialize(String.format("&a已将公告 &e%s &a的模式改为 &e%s",name,mode)));

               if(mode.equals("fix")){
                   TimerTaskCancel.cancelTask(name);
                   try {
                       fixTimerMap.put(name,FixTimeTask.fixTask(name));
                   } catch (ObjectMappingException e) {
                       e.printStackTrace();
                   }
               }else {
                   FixTimerCancel.cancelFixTimer(name);
                   IntervalTask.intervalTask(name);
               }

           }

       }else {
           src.sendMessage(Text.of(TextColors.RED,"该公告不存在，请检查名称！"));
       }

        return CommandResult.success();
    }

    public static CommandSpec build(){

        Map<String,String> choicesMap = new HashMap<String, String>();
        choicesMap.put("fix","fix");
        choicesMap.put("interval","interval");

        return CommandSpec.builder()
                .description(Text.of("修改指定公告的模式"))
                .permission("speaker.command.set.mode")
                .arguments(
                        GenericArguments.seq(
                                GenericArguments.onlyOne(
                                        GenericArguments.withSuggestions(
                                                GenericArguments.string(Text.of("公告名称")), Config.nameCompletion
                                        )
                                ),
                                GenericArguments.onlyOne(
                                        GenericArguments.choices(Text.of("fix->固定模式/interval->间隔模式"),choicesMap)
                                )
                        )
                )
                .executor(new modeChange())
                .build();
    }
}
