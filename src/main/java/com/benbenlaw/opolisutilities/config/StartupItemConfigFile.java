package com.benbenlaw.opolisutilities.config;

import net.neoforged.neoforge.common.ModConfigSpec;

public final class StartupItemConfigFile {

    public static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec SPEC;
    public static final ModConfigSpec.ConfigValue<Integer> totalGrowthAttempts;
    public static final ModConfigSpec.ConfigValue<Integer> saplingGrowerDurability;
    public static final ModConfigSpec.ConfigValue<Boolean> saplingGrowerTakesDamage;

    public static final ModConfigSpec.ConfigValue<Boolean> animalNetHostileMobs;
    public static final ModConfigSpec.ConfigValue<Boolean> animalNetWaterMobs;
    public static final ModConfigSpec.ConfigValue<Boolean> animalNetAnimalMobs;
    public static final ModConfigSpec.ConfigValue<Boolean> animalNetVillagerMobs;
    public static final ModConfigSpec.ConfigValue<Boolean> animalNetTakesDamage;
    public static final ModConfigSpec.ConfigValue<Integer> animalNetDurability;

    public static final ModConfigSpec.ConfigValue<Integer> crookBoost;
    public static final ModConfigSpec.ConfigValue<Integer> crookDurability;
    public static final ModConfigSpec.ConfigValue<Boolean> crookTakesDamage;

    public static final ModConfigSpec.ConfigValue<Boolean> shouldPlayerGetDeathStoneOnDeath;

    public static final ModConfigSpec.ConfigValue<Integer> homeStoneCooldown;
    public static final ModConfigSpec.ConfigValue<Boolean> homeStoneTakesDamage;

    static {

        // Sapling Grower Configs
        BUILDER.comment("Sapling Grower Configs")
                .push("Sapling Grower");

        totalGrowthAttempts = BUILDER.comment("The number of attempts to grow a sapling per right click, default = 128")
                .define("Total Attempts for Sapling Grower", 128);

        saplingGrowerDurability = BUILDER.comment("The durability of the Sapling Grower, default = 128")
                .define("Sapling Grower Durability", 128);

        saplingGrowerTakesDamage = BUILDER.comment("Does the Sapling Grower take damage?, default = true")
                .define("Sapling Grower Takes Damage", true);

        BUILDER.pop();

        //Animal Net Configs
        BUILDER.comment("Animal Net Configs")
                .push("Animal Net");

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

        animalNetDurability = BUILDER.comment("The durability of the Animal Net, default = 8")
                .define("Animal Net Durability", 8);

        BUILDER.pop();


        //Crook Configs
        BUILDER.comment("Crook Configs")
                .push("Crook");

        crookBoost = BUILDER.comment("Additional loot table rolls when using the crook on leaves, default = 3")
                .define("Additional Crook Rolls", 3);

        crookDurability = BUILDER.comment("The durability of the Crook, default = 64")
                .define("Crook Durability", 64);

        crookTakesDamage = BUILDER.comment("Does the Crook take damage when breaking leaves, default = true")
                .define("Crook Takes Damage", true);

        BUILDER.pop();

        //Death Stone Configs
        BUILDER.comment("Death Stone Configs")
                .push("Death Stone");

        shouldPlayerGetDeathStoneOnDeath = BUILDER.comment("Should the player get a death stone on death, default = true")
                .define("Death Stone: Get on Death", true);

        BUILDER.pop();

        //Home Stone Configs
        BUILDER.comment("Home Stone Configs")
                .push("Home Stone");

        homeStoneCooldown = BUILDER.comment("Cooldown in ticks of the home stone, default = 6000")
                .define("Home Stone Cooldown", 6000);

        homeStoneTakesDamage = BUILDER.comment("Does the home stone take damge when teleporting, default = true")
                .define("Home Stone Durability", true);






        //LAST

        SPEC = BUILDER.build();

    }

}
