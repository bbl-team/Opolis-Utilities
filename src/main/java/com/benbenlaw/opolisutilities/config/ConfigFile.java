package com.benbenlaw.opolisutilities.config;

import net.minecraftforge.common.ForgeConfigSpec;
import org.w3c.dom.Text;

public final class ConfigFile {

    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Integer> homeStoneCooldown;
    public static final ForgeConfigSpec.ConfigValue<Boolean> homeStoneTakesDamage;

    public static final ForgeConfigSpec.ConfigValue<String> discordURL;
    public static final ForgeConfigSpec.ConfigValue<String> modpackVersion;
    public static final ForgeConfigSpec.ConfigValue<String> modpackName;

    static {
        BUILDER.push("Opolis Utilities Config File");

        homeStoneCooldown = BUILDER.comment("Cooldown in ticks of the home stone, default = 6000")
                .define("Home Stone Cooldown", 6000);

        homeStoneTakesDamage = BUILDER.comment("Does the home stone take damge when teleporting, default = true")
                .define("Home Stone Durability", true);

        discordURL = BUILDER.comment("URL for your discord")
                .define("Discord URL", "https://discord.gg/UpJknJ8");

        modpackName = BUILDER.comment("Modpack name")
                .define("Modpack name", "TESTMODPACK");

        modpackVersion = BUILDER.comment("Modpack Version")
                .define("Modpack Version", "VERSION");


        BUILDER.pop();
        SPEC = BUILDER.build();

    }
}
