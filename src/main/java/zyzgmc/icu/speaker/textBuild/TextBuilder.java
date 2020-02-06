package zyzgmc.icu.speaker.textBuild;



import me.rojo8399.placeholderapi.PlaceholderService;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.service.economy.EconomyService;
import org.spongepowered.api.service.economy.account.UniqueAccount;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Text.Builder;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.serializer.TextSerializers;
import zyzgmc.icu.speaker.Speaker;
import zyzgmc.icu.speaker.config.Config;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import static zyzgmc.icu.speaker.Speaker.economyService;
import static zyzgmc.icu.speaker.Speaker.placeholderService;


public class TextBuilder {

    

    public static void builderAndSender(String name) throws Exception {
        String content = Config.rootNode.getNode("All", name, "Content").getString();
        String hoverText = Config.rootNode.getNode("All", name, "Hover").getString();
        String url = Config.rootNode.getNode().getNode("All", name, "Url").getString();
        String cmd = Config.rootNode.getNode("All", name, "Cmd").getString();


        if(!Sponge.getServer().getOnlinePlayers().isEmpty()) {
            for (Player player : Sponge.getServer().getOnlinePlayers()) {

                Builder build = Text.builder();
                build.append(
                        replaceDeal(content, player)
                );

                if (!Objects.equals(hoverText, "")) {
                    build.onHover(TextActions.showText(
                                    replaceDeal(hoverText, player)
                            )
                    );
                }

                if (!Objects.equals(url, "None")) {
                    URL thisUrl = null;

                    try {
                        thisUrl = new URL(url);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                        Sponge.getServer().getConsole().sendMessage(
                                TextSerializers.FORMATTING_CODE.deserialize(
                                        "&c公告 &e%s &c的 &eUrl &c有误，例：https://www.baidu.com"
                                )
                        );
                    }

                    build.onClick(TextActions.openUrl(thisUrl));

                }

                if (!Objects.equals(cmd, "None")) {
                    if (!cmd.startsWith("/")) {
                        build.onClick(TextActions.runCommand(replaceDeal(cmd, player).toPlain()));
                    } else {
                        build.onClick(TextActions.runCommand(replaceDeal(cmd, player).toPlain()));
                    }
                }

                player.sendMessage(build.build());

            }

        }

    }

    public static Text replaceDeal(String string, Player player) throws Exception {
        String str = string;
        str=str.replace("{Player}", player.getName());
        str=str.replace("{World}",player.getWorld().getName());
        str=str.replace("{GameMode}",player.getGameModeData().type().get().toString());
        str=str.replace("{Health}",player.getHealthData().health().get().toString());
        str=str.replace("{Food}",player.getFoodData().foodLevel().get().toString());
        Optional<EconomyService> serviceOpt = Sponge.getServiceManager().provide(EconomyService.class);
        if (serviceOpt.isPresent()) {
            Optional<UniqueAccount> uOpt = economyService.getOrCreateAccount(player.getUniqueId());
            UniqueAccount acc = uOpt.get();
            str=str.replace("{Balance}", acc.getDefaultBalance(economyService.getDefaultCurrency()).toString());
        }
        Text text = Text.of(str);
        try {
            text = placeholderService.replaceSourcePlaceholders(str, player);
        }catch (Exception e){

        }
        return text;
    }
}
