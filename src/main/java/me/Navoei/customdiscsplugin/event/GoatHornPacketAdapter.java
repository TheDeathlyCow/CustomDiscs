package me.Navoei.customdiscsplugin.event;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import me.Navoei.customdiscsplugin.CustomDiscs;
import org.bukkit.Sound;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

import java.util.List;

public class GoatHornPacketAdapter extends PacketAdapter {

    private static final List<Sound> GOAT_HORN_SOUNDS = List.of(
            Sound.ITEM_GOAT_HORN_PLAY,
            Sound.ITEM_GOAT_HORN_SOUND_0,
            Sound.ITEM_GOAT_HORN_SOUND_1,
            Sound.ITEM_GOAT_HORN_SOUND_2,
            Sound.ITEM_GOAT_HORN_SOUND_3,
            Sound.ITEM_GOAT_HORN_SOUND_4,
            Sound.ITEM_GOAT_HORN_SOUND_5,
            Sound.ITEM_GOAT_HORN_SOUND_6,
            Sound.ITEM_GOAT_HORN_SOUND_7
    );

    public GoatHornPacketAdapter(Plugin plugin, ListenerPriority listenerPriority) {
        super(plugin, listenerPriority, PacketType.Play.Server.ENTITY_SOUND);
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        PacketContainer packet = event.getPacket();
        Sound soundID = packet.getSoundEffects().read(0);
        if (GOAT_HORN_SOUNDS.contains(soundID)) {
            Entity user = packet.getEntityModifier(event).read(0);
            if (user instanceof Player player) {
                ItemStack heldItem = player.getInventory().getItemInMainHand();

                if (!GoatHornEvents.isGoatHorn(heldItem) || GoatHornEvents.getSoundPathFromItem(heldItem) == null) {
                    return;
                }

                event.setCancelled(true);
            }
        }

    }
}
