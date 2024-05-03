package com.benbenlaw.opolisutilities.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public final class ConfigFile {

    public static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec SPEC;

    public static final ModConfigSpec.ConfigValue<Integer> homeStoneCooldown;
    public static final ModConfigSpec.ConfigValue<Boolean> homeStoneTakesDamage;

    public static final ModConfigSpec.ConfigValue<String> discordURL;
    public static final ModConfigSpec.ConfigValue<String> modpackVersion;
    public static final ModConfigSpec.ConfigValue<String> modpackName;
    public static final ModConfigSpec.ConfigValue<Double> basicLootBoxDropChance;
    public static final ModConfigSpec.ConfigValue<Integer> crookBoost;
    public static final ModConfigSpec.ConfigValue<Integer> maxScramblerRange;
    public static final ModConfigSpec.ConfigValue<Integer> minScramblerRange;
    public static final ModConfigSpec.ConfigValue<Integer> totalGrowthAttempts;
    public static final ModConfigSpec.ConfigValue<Boolean> animalNetHostileMobs;
    public static final ModConfigSpec.ConfigValue<Boolean> animalNetWaterMobs;
    public static final ModConfigSpec.ConfigValue<Boolean> animalNetAnimalMobs;
    public static final ModConfigSpec.ConfigValue<Boolean> animalNetVillagerMobs;
    public static final ModConfigSpec.ConfigValue<Boolean> animalNetTakesDamage;
    public static final ModConfigSpec.ConfigValue<Boolean> woodenButtonsMakeDoorbellSound;


    static {
        BUILDER.push("Opolis Utilities Config File");

        homeStoneCooldown = BUILDER.comment("Cooldown in ticks of the home stone, default = 6000")
                .define("Home Stone Cooldown", 6000);

        homeStoneTakesDamage = BUILDER.comment("Does the home stone take damge when teleporting, default = true")
                .define("Home Stone Durability", true);

        discordURL = BUILDER.comment("URL for your discord")
                .define("Discord URL", "https://discord.gg/benbenlaw");

        modpackName = BUILDER.comment("Modpack name")
                .define("Modpack name", "TESTMODPACK");

        modpackVersion = BUILDER.comment("Modpack Version")
                .define("Modpack Version", "VERSION");

        basicLootBoxDropChance = BUILDER.comment("Mobs basic loot bag drop chance 0.0 = always, 1.0 = never, 0.5 = 50% chance, default = 0.75")
                .define("Mob Drop Chance", 0.95);

        crookBoost = BUILDER.comment("Additional rolls when using the crook on leaves")
                .define("Additional Crook Rolls", 3);

        maxScramblerRange = BUILDER.comment("The max range of the Ender Scrambler")
                .define("(WIP)Max Ender Scrambler Range", 8);

        minScramblerRange = BUILDER.comment("The min range of the Ender Scrambler")
                .define("(WIP)Min Ender Scrambler Range", 8);

        totalGrowthAttempts = BUILDER.comment("The number of attempts to grow a sapling per right click")
                .define("(WIP)Total Attempts for Sapling Grower", 128);

        animalNetHostileMobs = BUILDER.comment("Can the animal net capture hostile mobs, default = false")
                .define("Animal Net: Hostile Mobs", false);

        animalNetWaterMobs = BUILDER.comment("Can the animal net capture water mobs, default = true")
                .define("Animal Net: Water Mobs", true);

        animalNetAnimalMobs = BUILDER.comment("Can the animal net capture animal mobs, default = true")
                .define("Animal Net: Animal Mobs", true);

        animalNetVillagerMobs = BUILDER.comment("Can the animal net capture villager mobs, default = false")
                .define("Animal Net: Villager Mobs", false);

        animalNetTakesDamage = BUILDER.comment("Does the animal net take damage when capturing mobs, default = true")
                .define("Animal Net: Takes Damage?", true);

        woodenButtonsMakeDoorbellSound = BUILDER.comment("Do wooden buttons make a doorbell sound, default = false")
                .define("Wooden Buttons Make Doorbell Sound", false);


        BUILDER.pop();
        SPEC = BUILDER.build();

    }
}
