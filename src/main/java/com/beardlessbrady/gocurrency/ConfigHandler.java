package com.beardlessbrady.gocurrency;


import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

/**
 * Created by BeardlessBrady on 2021-02-23 for Currency-Mod
 * All Rights Reserved
 * https://github.com/Beardlessbrady/Currency-Mod
 */
@Mod.EventBusSubscriber(modid = "gocurrency", bus = Mod.EventBusSubscriber.Bus.MOD)
public class ConfigHandler {
    public static final CommonConfig COMMON;
    public static final ForgeConfigSpec COMMON_SPEC;

    static {
        final Pair<CommonConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(CommonConfig::new);
        COMMON_SPEC = specPair.getValue();
        COMMON = specPair.getKey();
    }

    public static ForgeConfigSpec.ConfigValue<List<? extends Double>> configCurrencyValue;
    public static ForgeConfigSpec.ConfigValue<List<? extends String>> configCurrencyName;

    public static void bakeConfig() {
        configCurrencyValue = COMMON.getConfigCurrencyValue();
        configCurrencyName = COMMON.getConfigCurrencyName();
    }

    @SubscribeEvent
    public static void onModConfigEvent(final ModConfig.ModConfigEvent configEvent) {
        if (configEvent.getConfig().getSpec() == ConfigHandler.COMMON_SPEC) {
            bakeConfig();
        }
    }

    public static class CommonConfig {
        public final ForgeConfigSpec.ConfigValue<List<? extends Double>> configCurrencyValue;
        Predicate<Object> currencyValueValidator = n -> ((Double)n > 0.0 && (Double)n < 999999.999);

        public final ForgeConfigSpec.ConfigValue<List<? extends String>> configCurrencyName;
        Predicate<Object> currencyNameValidator = n -> (true);

        public ForgeConfigSpec.ConfigValue<List<? extends Double>> getConfigCurrencyValue() {
            return configCurrencyValue;
        }

        public ForgeConfigSpec.ConfigValue<List<? extends String>> getConfigCurrencyName() {
            return configCurrencyName;
        }

        public CommonConfig (ForgeConfigSpec.Builder builder) {
            builder.push("Currency Configuration");

            configCurrencyName = builder
                    .comment("Currency Names - defining the names of each currency item in the mod.")
                    .translation(GOCurrency.MODID + ".config." + "configCurrencyName")
                    .defineList("currencyNames", Arrays.asList("One Dollar", "Five Dollars", "Ten Dollars", "Twenty Dollars", "Fifty Dollars", "One Hundred Dollars"), currencyNameValidator);

            configCurrencyValue = builder
                    .comment("Currency Values - defining the monetary values of each currency item in the mod (0.01 - 9999999.99)")
                    .translation(GOCurrency.MODID + ".config." + "configCurrencyValue")
                    .defineList("currencyValue", Arrays.asList(1.0, 5.0, 10.0, 20.0, 50.0, 100.0), currencyValueValidator);

            builder.pop();
        }
    }
}
