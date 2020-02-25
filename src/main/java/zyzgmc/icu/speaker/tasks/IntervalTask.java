package zyzgmc.icu.speaker.tasks;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.serializer.TextSerializers;
import zyzgmc.icu.speaker.config.Config;
import zyzgmc.icu.speaker.textBuild.TextBuilder;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;


public class IntervalTask {
    public static Timer thisTimer;

    public static Timer intervalTask(String name, double delay){
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if(Config.rootNode.getNode("All",name,"Enable").getBoolean()
                        && Objects.equals(Config.rootNode.getNode("All", name, "ModeCode").getString(), "interval")){

                    if (Objects.requireNonNull(Config.rootNode.getNode("All", name, "Display").getString()).equals("normal")) {
                        try {
                            TextBuilder.builderAndSender(name);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Sponge.getServer().getConsole().sendMessage(
                                    Text.of(
                                            TextColors.RED,String.format(
                                                    "公共任务 %s 发送失败",name
                                            )
                                    )
                            );
                        }
                    }else if (Objects.requireNonNull(Config.rootNode.getNode("All", name, "Display").getString()).equals("title")){
                        try {
                            TextBuilder.titleBuilder(name);
                        }catch (Exception e){
                            e.printStackTrace();
                            Sponge.getServer().getConsole().sendMessage(
                                    Text.of(
                                            TextColors.RED,String.format(
                                                    "公共任务 %s 发送失败",name
                                            )
                                    )
                            );
                        }
                    }else if (Objects.requireNonNull(Config.rootNode.getNode("All", name, "Display").getString()).equals("boss"))
                    {
                        try{
                            TextBuilder.bossBarBuilder(name);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                    Sponge.getServer().getConsole().sendMessage(
                            TextSerializers.FORMATTING_CODE.deserialize(
                                    Objects.requireNonNull(Config.rootNode.getNode("All", name, "Content").getString())
                            )
                    );

                }else {

                }


            }
        }, (long) (delay*1000), Config.rootNode.getNode("All",name,"Interval").getInt() * 1000);
        thisTimer = timer;
        return thisTimer;
    }
}
