package me.Navoei.customdiscsplugin.event;

import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import io.papermc.paper.event.player.PlayerStopUsingItemEvent;
import me.Navoei.customdiscsplugin.CustomDiscs;
import me.Navoei.customdiscsplugin.PlayerManager;
import me.Navoei.customdiscsplugin.VoicePlugin;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Jukebox;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.Nullable;

import java.nio.file.Path;

public class GoatHornEvents implements Listener {

    public static final NamespacedKey CUSTOM_DATA_KEY = new NamespacedKey(CustomDiscs.getInstance(), "customhorn");

    @EventHandler(
            priority = EventPriority.HIGHEST
    )
    public void onUse(PlayerInteractEvent event) {
        ItemStack item = event.getItem();

        if (item != null && isGoatHorn(item)) {
            if (this.playHorn(event.getPlayer(), item)) {
                event.setUseItemInHand(Event.Result.DENY);
            }
        }
    }

    private boolean playHorn(Player user, ItemStack horn) {

        if (user.getCooldown(horn.getType()) > 0) {
            return false;
        }

        Path soundFilePath = getSoundPathFromItem(horn);

        if (soundFilePath != null && soundFilePath.toFile().exists() && VoicePlugin.voicechatServerApi != null) {
            return PlayerManager.instance().playGoatHorn(VoicePlugin.voicechatServerApi, soundFilePath, user) != null;
        }

        return false;
    }

    @Nullable
    public static Path getSoundPathFromItem(ItemStack horn) {

        if (!horn.hasItemMeta()) {
            return null;
        }

        var data = horn.getItemMeta().getPersistentDataContainer();
        if (!data.has(CUSTOM_DATA_KEY, PersistentDataType.STRING)) {
            return null;
        }
        String soundFileName = data.get(CUSTOM_DATA_KEY, PersistentDataType.STRING);

        return Path.of(CustomDiscs.getInstance().getDataFolder().getPath(), "musicdata", soundFileName);
    }

    public static boolean isGoatHorn(ItemStack stack) {
        return stack.getType() == Material.GOAT_HORN;
    }
}
