package gregtech.api.util;

import gregtech.api.GTValues;
import net.minecraft.util.StringIdentifiable;

public enum VoltageTier implements StringIdentifiable {

    ULV("ultra_low_voltage", 8L, 0xDCDCDC),
    LV("low_voltage", 32L, 0xDCDCDC),
    MV("medium_voltage", 128L, 0xFF6400),
    HV("high_voltage", 512L, 0xFFFF1E),
    EV("extreme_voltage", 2048L, 0x808080),
    IV("insane_voltage", 8192L, 0xF0F0F5),
    LuV("ludicrous_voltage", 32768L, 0xF0F0F5),
    ZPM("zpm_voltage", 131072, 0xF0F0F5),
    UV("ultimate_voltage", 524288, 0xF0F0F5),
    MAX("maximum_voltage", Integer.MAX_VALUE, 0xF0F0F5);

    private final String name;
    private final long voltage;
    private final int voltageColor;

    VoltageTier(String name, long voltage, int voltageColor) {
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

    public long getVoltage() {
        return voltage;
    }

    public int getVoltageColor() {
        return voltageColor;
    }

    @Override
    public String asString() {
        return this.name;
    }
}
