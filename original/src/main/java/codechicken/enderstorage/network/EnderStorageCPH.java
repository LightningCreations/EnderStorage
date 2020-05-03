package codechicken.enderstorage.network;

import codechicken.enderstorage.api.Frequency;
import codechicken.enderstorage.manager.EnderStorageManager;
import codechicken.enderstorage.storage.EnderItemStorage;
import codechicken.enderstorage.tile.TileEnderTank;
import codechicken.enderstorage.tile.TileFrequencyOwner;
import codechicken.lib.packet.ICustomPacketHandler.IClientPacketHandler;
import codechicken.lib.packet.PacketCustom;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.network.play.INetHandlerPlayClient;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;

public class EnderStorageCPH implements IClientPacketHandler {

    public static final String channel = "ES";

    @Override
    public void handlePacket(PacketCustom packet, Minecraft mc, INetHandlerPlayClient handler) {
        switch (packet.getType()) {
            case 1:
                handleTilePacket(mc.world, packet, packet.readPos());
                break;
            case 2:
                int windowID = packet.readUByte();
                ((EnderItemStorage) EnderStorageManager.instance(true).getStorage(Frequency.readFromPacket(packet), "item")).openClientGui(windowID, mc.player.inventory, packet.readString(), packet.readUByte());
                break;
            case 3:
                ((EnderItemStorage) EnderStorageManager.instance(true).getStorage(Frequency.readFromPacket(packet), "item")).setClientOpen(packet.readBoolean() ? 1 : 0);
                break;
            case 4:
                TankSynchroniser.syncClient(Frequency.readFromPacket(packet), packet.readFluidStack());
                break;
            case 5:
            case 6:
                handleTankTilePacket(mc.world, packet.readPos(), packet);
                break;
        }
    }

    private void handleTankTilePacket(WorldClient world, BlockPos pos, PacketCustom packet) {
        TileEntity tile = world.getTileEntity(pos);
        if (tile instanceof TileEnderTank) {
            ((TileEnderTank) tile).sync(packet);
        }
    }

    private void handleTilePacket(WorldClient world, PacketCustom packet, BlockPos pos) {
        TileEntity tile = world.getTileEntity(pos);

        if (tile instanceof TileFrequencyOwner) {
            ((TileFrequencyOwner) tile).readFromPacket(packet);
        }
    }
}
