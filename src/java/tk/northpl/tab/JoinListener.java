package tk.northpl.tab;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.PlayerInfoData;
import com.comphenix.protocol.wrappers.WrappedChatComponent;

import pl.za.xvacuum.guilds.Main;

import com.comphenix.protocol.wrappers.EnumWrappers;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public final class JoinListener implements Listener
{
    @EventHandler
    public void onJoin(final PlayerJoinEvent e)
    {
        final PacketContainer writeTab = Main.getInstance().getTabListHandler().getProtocol().createPacket(PacketType.Play.Server.PLAYER_INFO);
        writeTab.getPlayerInfoAction().write(0, EnumWrappers.PlayerInfoAction.ADD_PLAYER);

        final List<PlayerInfoData> virtualPlayersToWrite = new ArrayList<>(80);
        for (final TablistSlot wgp : Main.getInstance().getTabListHandler().getSlots().values())
        {
            virtualPlayersToWrite.add(new PlayerInfoData(wgp.getVirtualPlayer(), Main.getInstance().TABLIST_PING, EnumWrappers.NativeGameMode.NOT_SET, WrappedChatComponent.fromText(wgp.getTextForPlayer(e.getPlayer().getName()))));
        }
        writeTab.getPlayerInfoDataLists().write(0, virtualPlayersToWrite);
        try
        {
            Main.getInstance().getTabListHandler().getProtocol().sendServerPacket(e.getPlayer(), writeTab);
        }
        catch (final InvocationTargetException e1)
        {
           e1.printStackTrace();
        }
    }
}
