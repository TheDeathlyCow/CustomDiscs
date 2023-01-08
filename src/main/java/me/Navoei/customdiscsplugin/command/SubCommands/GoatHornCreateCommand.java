package me.Navoei.customdiscsplugin.command.SubCommands;

import me.Navoei.customdiscsplugin.CustomDiscs;
import me.Navoei.customdiscsplugin.command.CommandHelper;
import me.Navoei.customdiscsplugin.command.SubCommand;
import me.Navoei.customdiscsplugin.event.GoatHornEvents;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GoatHornCreateCommand extends SubCommand {

    @Override
    public String getName() {
        return "createHorn";
    }

    @Override
    public String getDescription() {
        return ChatColor.GRAY + "Creates a custom goat horn";
    }

    @Override
    public String getSyntax() {
        return ChatColor.GREEN + "/customdisc createHorn <filename> \"Custom Lore\"";
    }

    @Override
    public void perform(Player player, String[] args) {
        ItemStack mainHandStack = player.getInventory().getItemInMainHand();
        if (GoatHornEvents.isGoatHorn(mainHandStack)) {
            if (args.length >= 3) {

                if (!player.hasPermission("customdiscs.createhorn")) {
                    player.sendMessage(ChatColor.RED + "You do not have permission to execute this command!");
                    return;
                }

                // /cd create test.mp3 "test"
                //      [0]     [1]     [2]
                //Find file, if file not there then say "file not there"
                String songname = "";
                String filename = args[1];
                if (filename.contains("../")) {
                    player.sendMessage(ChatColor.RED + "This is an invalid filename!");
                    return;
                }

                String customName = this.readCustomName(CommandHelper.readQuotes(args));

                if (customName.isEmpty()) {
                    player.sendMessage(ChatColor.RED + "You must provide a name for your horn");
                    return;
                }

                File getDirectory = new File(CustomDiscs.getInstance().getDataFolder(), "musicdata");
                File songFile = new File(getDirectory.getPath(), filename);
                if (songFile.exists()) {
                    String fileExtention = this.getFileExtension(filename);
                    if (fileExtention.equals("wav") || fileExtention.equals("mp3")) {
                        songname = args[1];
                    } else {
                        player.sendMessage(ChatColor.RED + "File is not in wav or mp3 format!");
                        return;
                    }
                } else {
                    player.sendMessage(ChatColor.RED + "File not found!");
                    return;
                }

                // Sets the lore of the item to the specified name
                ItemMeta itemMeta = mainHandStack.getItemMeta();
                @Nullable List<Component> itemLore = new ArrayList<>();
                final TextComponent customLoreSong = Component.text()
                        .decoration(TextDecoration.ITALIC, false)
                        .content(customName)
                        .color(NamedTextColor.GRAY)
                        .build();
                itemLore.add(customLoreSong);
                itemMeta.addItemFlags(ItemFlag.values());
                itemMeta.lore(itemLore);

                PersistentDataContainer data = itemMeta.getPersistentDataContainer();
                data.set(GoatHornEvents.CUSTOM_DATA_KEY, PersistentDataType.STRING, filename);

                mainHandStack.setItemMeta(itemMeta);

                player.sendMessage("Your filename is: " + ChatColor.GRAY + songname);
                player.sendMessage("Your custom name is: " + ChatColor.GRAY + customName);

            } else {
                player.sendMessage(ChatColor.RED + "Insufficient arguments! ( /customdisc createhorn <filename> \"Custom Lore\" )");
            }
        } else {
            player.sendMessage(ChatColor.RED + "You are not holding a goat horn in your main hand!");
        }
    }

    private String getFileExtension(String s) {
        int index = s.lastIndexOf(".");
        if (index > 0) {
            return s.substring(index + 1);
        } else {
            return "";
        }
    }

    private String readCustomName(ArrayList<String> q) {

        StringBuffer sb = new StringBuffer();

        for (String s : q) {
            sb.append(s);
            sb.append(" ");
        }

        if (sb.isEmpty()) {
            return sb.toString();
        } else {
            return sb.toString().substring(0, sb.length() - 1);
        }
    }

}
