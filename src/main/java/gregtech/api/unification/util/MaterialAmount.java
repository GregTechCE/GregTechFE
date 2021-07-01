package gregtech.api.unification.util;

import java.util.Objects;

public class MaterialAmount {

    private static final long MATERIAL_UNIT = 3628800;

    public static final MaterialAmount ZERO = new MaterialAmount(0L);
    public static final MaterialAmount DUST = new MaterialAmount(MATERIAL_UNIT);
    public static final MaterialAmount INGOT = DUST;
    public static final MaterialAmount GEM = DUST;

    public static final MaterialAmount SMALL_DUST = DUST.div(4);
    public static final MaterialAmount TINY_DUST = DUST.div(9);
    public static final MaterialAmount NUGGET = DUST.div(9);

    public static final MaterialAmount PLATE = INGOT;
    public static final MaterialAmount DENSE_PLATE = INGOT.mul(9);
    public static final MaterialAmount ROD = INGOT.div(2);
    public static final MaterialAmount LONG_ROD = INGOT;
    public static final MaterialAmount GEAR = INGOT.mul(4);
    public static final MaterialAmount SMALL_GEAR = INGOT;
    public static final MaterialAmount SPRING = INGOT;
    public static final MaterialAmount SMALL_SPRING = INGOT.div(4);
    public static final MaterialAmount FINE_WIRE = INGOT.div(8);
    public static final MaterialAmount FOIL = INGOT.div(4);
    public static final MaterialAmount BOLT = INGOT.div(8);
    public static final MaterialAmount SCREW = INGOT.div(9);
    public static final MaterialAmount RING = INGOT.div(4);
    public static final MaterialAmount ROTOR = INGOT.mul(4);
    public static final MaterialAmount LENS = GEM.mul(3).div(4);

    public static final MaterialAmount CHIPPED_GEM = GEM.div(4);
    public static final MaterialAmount FLAWED_GEM = GEM.div(2);
    public static final MaterialAmount FLAWLESS_GEM = GEM.mul(2);
    public static final MaterialAmount EXQUISITE_GEM = GEM.mul(4);

    private final long amount;

    private MaterialAmount(long amount) {
        this.amount = amount;
    }

    public MaterialAmount mul(int multiplier) {
        return new MaterialAmount(this.amount * multiplier);
    }

    public MaterialAmount div(int denominator) {
        return new MaterialAmount(this.amount / denominator);
    }

    public MaterialAmount sub(MaterialAmount other) {
        return new MaterialAmount(Math.max(0L, this.amount - other.amount));
    }

    public MaterialAmount add(MaterialAmount other) {
        return new MaterialAmount(this.amount + other.amount);
    }

    public double div(MaterialAmount other) {
        return this.amount / (other.amount * 1.0);
    }

    public int divFloor(MaterialAmount other) {
        return (int) (this.amount / other.amount);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MaterialAmount that = (MaterialAmount) o;
        return amount == that.amount;
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount);
    }
}
