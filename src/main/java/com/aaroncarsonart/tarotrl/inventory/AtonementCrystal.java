package com.aaroncarsonart.tarotrl.inventory;

import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class AtonementCrystal extends Item {

    // TODO replace TreeMap with custom TREE MAP implementation
    // TODO implement HarmonicTree<String, String> constructed with
    // TODO branches of Pair<String, TreeSet<String>>

    private TreeMap<String, String> harmonic_tree;

    public AtonementCrystal(String name, String description) {
        super(name, description);
        harmonic_tree = new TreeMap<>();
        harmonic_tree.put(name, description);
    }

    public AtonementCrystal(AtonementCrystal atonementCrystal) {
        super(atonementCrystal.getName(), atonementCrystal.getDescription());
        harmonic_tree = new TreeMap<>(atonementCrystal.harmonic_tree);
    }

    /**
     * Creates a sub crystal of this AtonementCrystal, using the given
     * fromKey and toKey strings.
     * @param fromKey The starting key from which to begin the sub crystal.
     * @param toKey The ending key at which to end the sub crystal.
     * @return A sub crystal of this AtonementCrystal.
     */
    public AtonementCrystal subCrystal(String fromKey, String toKey) {
        AtonementCrystal subCrystal = new AtonementCrystal(this);
        subCrystal.separate(this, fromKey, toKey);
        return subCrystal;
    }

    /**
     * Merge this AtonementCrystal with the given associated Atonement Crystal
     * given by the boundaries of the fromKey and toKey.
     * @param atonementCrystal The crystal to join with.
     * @param fromKey The starting key from which to begin the join.
     * @param toKey The ending key at which to end the join.
     */
    public void merge(AtonementCrystal atonementCrystal, String fromKey, String toKey) {
        SortedMap<String, String> treeToMerge = atonementCrystal.harmonic_tree.subMap(fromKey, toKey);
        for (Map.Entry<String, String> entry : treeToMerge.entrySet()) {
            this.harmonic_tree.merge(entry.getKey(), entry.getValue(), (v1, v2) -> v2);
        }
    }

    /**
     * Separate this AtonementCrystal from the given associated Atonement Crystal
     * given by the boundaries of the fromKey and toKey.
     * @param atonementCrystal The crystal to separate from.
     * @param fromKey The starting key from which to begin the separation.
     * @param toKey The ending key at which to end the separation.
     */
    public void separate(AtonementCrystal atonementCrystal, String fromKey, String toKey) {
        SortedMap<String, String> treeToSeparate = atonementCrystal.harmonic_tree.subMap(fromKey, toKey);
        for (Map.Entry<String, String> entry : treeToSeparate.entrySet()) {
            this.harmonic_tree.remove(entry.getKey(), entry.getValue());
        }
    }
}
