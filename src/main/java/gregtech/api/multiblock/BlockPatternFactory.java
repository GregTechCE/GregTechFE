package gregtech.api.multiblock;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import gregtech.api.multiblock.util.AisleRepetition;
import gregtech.api.multiblock.util.BlockCountPredicate;
import gregtech.api.multiblock.util.RelativeDirection;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.tuple.Pair;

import java.lang.reflect.Array;
import java.util.*;
import java.util.function.Predicate;

import static gregtech.api.multiblock.util.RelativeDirection.*;

public class BlockPatternFactory {

    private static final Joiner COMMA_JOIN = Joiner.on(",");

    private final List<String[]> depth = new ArrayList<>();
    private final Int2ObjectMap<AisleRepetition> aisleRepetitions = new Int2ObjectOpenHashMap<>();
    private final RelativeDirection[] structureDir = new RelativeDirection[3];

    private int aisleHeight;
    private int rowWidth;

    private final Map<Character, Pair<Integer, Integer>> blockCountValidators = new HashMap<>();
    private final Map<Character, Predicate<BlockWorldState>> symbolMap = new HashMap<>();
    private final Set<Character> referencedSymbols = new HashSet<>();

    private final Int2ObjectMap<Predicate<PatternMatchContext>> layerValidators = new Int2ObjectOpenHashMap<>();
    private final List<Predicate<PatternMatchContext>> contextValidators = new ArrayList<>();

    private BlockPatternFactory(RelativeDirection charDir, RelativeDirection stringDir, RelativeDirection aisleDir) {
        this.structureDir[0] = charDir;
        this.structureDir[1] = stringDir;
        this.structureDir[2] = aisleDir;
        this.symbolMap.put(' ', blockWorldState -> true);
        validateStructureDir();
    }

    private void validateStructureDir() {
        int flags = 0;
        for (int i = 0; i < 3; i++) {
            switch (structureDir[i]) {
                case UP, DOWN -> flags |= 0x1;
                case LEFT, RIGHT -> flags |= 0x2;
                case FRONT, BACK -> flags |= 0x4;
            }
        }
        if (flags != 0x7) {
            throw new IllegalArgumentException("Must have 3 different axes!");
        }
    }

    /**
     * Adds a repeatable aisle to this pattern
     */
    public BlockPatternFactory aisleRepeatable(int minRepeat, int maxRepeat, String... aisle) {
        return aisle(aisle).setRepeatable(minRepeat, maxRepeat);
    }

    /**
     * Adds a single aisle to this pattern. (so multiple calls to this will increase the aisleDir by 1)
     */
    public BlockPatternFactory aisle(String... aisle) {
        Preconditions.checkArgument(ArrayUtils.isNotEmpty(aisle), "Empty aisle pattern");

        if (this.depth.isEmpty()) {
            this.aisleHeight = aisle.length;
            this.rowWidth = aisle[0].length();
        }

        if (aisle.length != this.aisleHeight) {
            throw new IllegalArgumentException("Expected aisle with height of " + this.aisleHeight + ", but was given one with a height of " + aisle.length + ")");
        }

        for (String s : aisle) {
            if (s.length() != this.rowWidth) {
                throw new IllegalArgumentException("Not all rows in the given aisle are the correct width (expected " + this.rowWidth + ", found one with " + s.length() + ")");
            }
            for (char c0 : s.toCharArray()) {
                this.referencedSymbols.add(c0);
            }
        }

        this.depth.add(aisle);
        return this;
    }

    /**
     * Set last aisle repeatable
     */
    public BlockPatternFactory setRepeatable(int minRepeat, int maxRepeat) {
        Preconditions.checkArgument(minRepeat <= maxRepeat, "Lower bound of repeat counting must smaller than or equal to the upper bound");
        this.aisleRepetitions.put(this.depth.size() - 1, new AisleRepetition(minRepeat, maxRepeat));
        return this;
    }

    public BlockPatternFactory limitBlockCount(char symbol, int minAmount, int maxAmount) {
        Preconditions.checkArgument(minAmount <= maxAmount, "Minimum amount should be less or equal to the maximum one");
        this.referencedSymbols.add(symbol);
        this.blockCountValidators.put(symbol, Pair.of(minAmount, maxAmount));
        return this;
    }

    public BlockPatternFactory limitMinimumBlockCount(char symbol, int minValue) {
        return limitBlockCount(symbol, minValue, Integer.MAX_VALUE);
    }

    public BlockPatternFactory limitMaximumBlockCount(char symbol, int maxValue) {
        return limitBlockCount(symbol, 0, maxValue);
    }

    public static BlockPatternFactory start() {
        return new BlockPatternFactory(RIGHT, UP, BACK);
    }

    public static BlockPatternFactory start(RelativeDirection charDir, RelativeDirection stringDir, RelativeDirection aisleDir) {
        return new BlockPatternFactory(charDir, stringDir, aisleDir);
    }

    public BlockPatternFactory where(char symbol, Predicate<BlockWorldState> blockMatcher) {
        this.symbolMap.put(symbol, blockMatcher);
        return this;
    }

    /**
     * Adds predicate to be run after multiblock checking to validate
     * pattern matching context before succeeding match sequence
     */
    public BlockPatternFactory validateContext(Predicate<PatternMatchContext> validator) {
        this.contextValidators.add(validator);
        return this;
    }

    /**
     * Adds predicate to validate given layer using given validator
     * Given context is layer-local and can be accessed via {@link BlockWorldState#getLayerContext()}
     */
    public BlockPatternFactory validateLayer(int layerIndex, Predicate<PatternMatchContext> layerValidator) {
        this.layerValidators.put(layerIndex, layerValidator);
        return this;
    }

    public BlockPattern build() {
        checkMissingPredicates();

        return new BlockPattern(this.structureDir,
                this.aisleRepetitions,
                makePredicateArray(),
                this.layerValidators,
                this.contextValidators,
                makeBlockCountLimits());
    }

    @SuppressWarnings("unchecked")
    private Predicate<BlockWorldState>[][][] makePredicateArray() {
        Predicate<BlockWorldState>[][][] predicate = (Predicate<BlockWorldState>[][][]) Array.newInstance(Predicate.class, this.depth.size(), this.aisleHeight, this.rowWidth);

        for (int i = 0; i < this.depth.size(); ++i) {
            for (int j = 0; j < this.aisleHeight; ++j) {
                for (int k = 0; k < this.rowWidth; ++k) {
                    predicate[i][j][k] = this.symbolMap.get(this.depth.get(i)[j].charAt(k));
                }
            }
        }

        return predicate;
    }

    private List<BlockCountPredicate> makeBlockCountLimits() {
        ArrayList<BlockCountPredicate> predicates = new ArrayList<>();

        for (Character predicateChar : this.blockCountValidators.keySet()) {
            Pair<Integer, Integer> limits = this.blockCountValidators.get(predicateChar);
            Predicate<BlockWorldState> predicate = this.symbolMap.get(predicateChar);

            predicates.add(new BlockCountPredicate(predicate, limits.getLeft(), limits.getRight()));
        }

        return predicates;
    }

    private void checkMissingPredicates() {
        List<Character> missingCharacters = new ArrayList<>();

        for (Character usedCharacter : this.referencedSymbols) {
            if (!this.symbolMap.containsKey(usedCharacter)) {
                missingCharacters.add(usedCharacter);
            }
        }

        if (!missingCharacters.isEmpty()) {
            throw new IllegalStateException("Predicates for character(s) " + COMMA_JOIN.join(missingCharacters) + " are missing");
        }
    }
}