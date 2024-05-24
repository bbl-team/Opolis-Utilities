package com.benbenlaw.opolisutilities.capabillties;

public class CapabillitySyncronizer {

    /*
    public static class Types {
        public static final ResourceLocation BLOCKS = new ResourceLocation(MOD_ID, "blocks");
        public static final ResourceLocation ITEMS = new ResourceLocation(MOD_ID, "items");

        public static final SidedDataHandler BLOCK_DATA_HANDLER = SidedDataHandler.create(new BlockDataHandler.Server(), new BlockDataHandler.Client());
    }

    private static final HashMap<Capability<?>, ResourceLocation> CAPS = new HashMap<>();
    private static final HashMap<ResourceLocation, Capability<?>> REVERSE_CAPS = new HashMap<>();
    private static final HashMap<ResourceLocation, SidedDataHandler> DATA_HANDLERS = new HashMap<>();

    public static void register(ResourceLocation resourceLocation, Capability<?> capability) {
        CAPS.put(capability, resourceLocation);
        REVERSE_CAPS.put(resourceLocation, capability);
    }

    public static void registerDataHandler(ResourceLocation ID, SidedDataHandler handler) {
        if (DATA_HANDLERS.containsKey(ID)) throw new IllegalStateException("ID: %s already exists".formatted(ID));
        DATA_HANDLERS.put(ID, handler);
    }

    static {
        register(new ResourceLocation("forge", "item"), ForgeCapabilities.ITEM_HANDLER);
        register(new ResourceLocation("forge", "fluid"), ForgeCapabilities.FLUID_HANDLER);
        register(new ResourceLocation("forge", "energy"), ForgeCapabilities.ENERGY);
        register(new ResourceLocation("forge", "fluid_item"), ForgeCapabilities.FLUID_HANDLER_ITEM);

        registerDataHandler(Types.BLOCKS, Types.BLOCK_DATA_HANDLER);
    }

    public static Optional<SidedDataHandler> getDataHandler(ResourceLocation ID) {
        return DATA_HANDLERS.containsKey(ID) ? Optional.of(DATA_HANDLERS.get(ID)) : Optional.empty();
    }

    public static ResourceLocation get(Capability<?> capability) {
        return CAPS.getOrDefault(capability, new ResourceLocation("empty"));
    }

    public static Capability<?> get(ResourceLocation location) {
        return REVERSE_CAPS.getOrDefault(location, null);
    }

     */
}
