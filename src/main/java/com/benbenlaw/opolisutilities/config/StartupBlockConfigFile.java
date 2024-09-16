package com.benbenlaw.opolisutilities.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public final class StartupBlockConfigFile {

    public static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec SPEC;

    public static final ModConfigSpec.ConfigValue<Integer> maxScramblerRange;





    static {

        // Ender Scrambler Configs
        BUILDER.comment("Ender Scrambler Configs")
                .push("Ender Scrambler");

        maxScramblerRange = BUILDER.comment("The max range of the Ender Scrambler, default = 8")
                .define("Max Ender Scrambler Range", 8);

        BUILDER.pop();








        //LAST

        SPEC = BUILDER.build();

    }

}
