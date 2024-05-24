package com.benbenlaw.opolisutilities.networking.packets;

public class PacketItemRepairerOnOffButton {

    /*

    private BlockPos pos;

    public PacketItemRepairerOnOffButton(BlockPos pos) {
        this.pos = pos;
    }

    public PacketItemRepairerOnOffButton(FriendlyByteBuf buf) {
        this.pos = buf.readBlockPos();
    }

    public void toBytes(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
    }

    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayer player = context.getSender();
            if (player != null) {
                ServerLevel level = player.serverLevel();
                if (pos != null) {

                    if (level.getBlockState(pos).getValue(ItemRepairerBlock.POWERED)) {
                        level.setBlockAndUpdate(pos, ModBlocks.ITEM_REPAIRER.get().defaultBlockState().setValue(ItemRepairerBlock.POWERED, false));
                    } else
                        level.setBlockAndUpdate(pos, ModBlocks.ITEM_REPAIRER.get().defaultBlockState().setValue(ItemRepairerBlock.POWERED, true));


                }
            }
        });
        return true;
    }

     */
}