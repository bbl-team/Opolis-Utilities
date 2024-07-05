package com.benbenlaw.opolisutilities.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public final class StartupConfigFile {

    public static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec SPEC;

    public static final ModConfigSpec.ConfigValue<Integer> totalGrowthAttempts;



    static {
        BUILDER.push("Opolis Utilities Config File");


        totalGrowthAttempts = BUILDER.comment("The number of attempts to grow a sapling per right click")
                .define("(WIP)Total Attempts for Sapling Grower", 128);

        BUILDER.pop();
        SPEC = BUILDER.build();

    }

}
