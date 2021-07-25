package gregtech.api.util;

import gregtech.api.GTValues;
import net.minecraft.util.StringIdentifiable;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public enum VoltageTier implements StringIdentifiable {

    ULV("ultra_low_voltage", 8, 0xDCDCDC),
    LV("low_voltage", 32, 0xDCDCDC),
    MV("medium_voltage", 128, 0xFF6400),
    HV("high_voltage", 512, 0xFFFF1E),
    EV("extreme_voltage", 2048, 0x808080),
    IV("insane_voltage", 8192, 0xF0F0F5),
    LuV("ludicrous_voltage", 32768, 0xF0F0F5),
    ZPM("zpm_voltage", 131072, 0xF0F0F5),
    UV("ultimate_voltage", 524288, 0xF0F0F5),
    MAX("maximum_voltage", Integer.MAX_VALUE, 0xF0F0F5);

    private final String name;
    private final int voltage;
    private final int voltageColor;

    VoltageTier(String name, int voltage, int voltageColor) {
        this.name = name;
        this.voltage = voltage;
        this.voltageColor = voltageColor;
    }

    public String getName() {
        return name;
    }

    public String getTranslationKey() {
        return GTValues.MODID + ".voltage." + this.name;
    }

    public int getVoltage() {
        return voltage;
    }

    public int getVoltageColor() {
        return voltageColor;
    }

    public VoltageTier getPreviousTier() {
        VoltageTier[] values = values();
        return values[Math.max(0, ordinal() - 1)];
    }

    public VoltageTier getNextTier() {
        VoltageTier[] values = values();
        return values[Math.min(ordinal() + 1, values.length - 1)];
    }

    @Override
    public String asString() {
        return this.name;
    }

    @Nullable
    public static VoltageTier byName(String name) {
        return TIER_BY_NAME.get(name);
    }

    private static final Map<String, VoltageTier> TIER_BY_NAME = new HashMap<>();

    static {
        for (VoltageTier voltageTier : values()) {
            TIER_BY_NAME.put(voltageTier.getName(), voltageTier);
        }
    }
}
