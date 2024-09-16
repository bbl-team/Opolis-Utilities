package com.benbenlaw.opolisutilities.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public final class ConfigFile {

    public static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec SPEC;


    public static final ModConfigSpec.ConfigValue<String> discordURL;
    public static final ModConfigSpec.ConfigValue<String> modpackVersion;
    public static final ModConfigSpec.ConfigValue<String> modpackName;
    public static final ModConfigSpec.ConfigValue<Double> basicLootBoxDropChance;
    public static final ModConfigSpec.ConfigValue<Boolean> woodenButtonsMakeDoorbellSound;
    public static final ModConfigSpec.ConfigValue<Boolean> summoningBlockCheckForSameEntityBeforeSpawningNewEntity;
    public static final ModConfigSpec.ConfigValue<Integer> summoningBlockRangeCheck;




    static {
        BUILDER.push("Opolis Utilities Config File");


        discordURL = BUILDER.comment("URL for your discord")
                .define("Discord URL", "https://discord.gg/benbenlaw");

        modpackName = BUILDER.comment("Modpack name")
                .define("Modpack name", "TESTMODPACK");

        modpackVersion = BUILDER.comment("Modpack Version")
                .define("Modpack Version", "VERSION");

        basicLootBoxDropChance = BUILDER.comment("Mobs basic loot bag drop chance 0.0 = always, 1.0 = never, 0.5 = 50% chance, default = 0.75")
                .define("Mob Drop Chance", 0.95);






        woodenButtonsMakeDoorbellSound = BUILDER.comment("Do wooden buttons make a doorbell sound, default = false")
                .define("Wooden Buttons Make Doorbell Sound", false);

        summoningBlockCheckForSameEntityBeforeSpawningNewEntity = BUILDER.comment("Does the summoning block check for the same entity before spawning a new entity, default = true")
                .define("Summoning Block Checks For Same Entity Before Spawning New Entity", true);

        summoningBlockRangeCheck = BUILDER.comment("The range the summoning block checks for entities, default = 2")
                .define("Summoning Block Range Check", 2);

        BUILDER.pop();
        SPEC = BUILDER.build();



    }


}
