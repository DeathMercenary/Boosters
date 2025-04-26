package com.prismforge.boosters.tips;

import com.prismforge.boosters.booster.ActiveBooster;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Tracks who has tipped which boosters.
 */
public class TipTracker {

    private static final HashMap<UUID, Set<UUID>> tippedMap = new HashMap<>();
    // player UUID -> set of booster creator UUIDs tipped

    public static boolean hasTipped(UUID tipper, UUID creator) {
        return tippedMap.getOrDefault(tipper, new HashSet<>()).contains(creator);
    }

    public static void markTipped(UUID tipper, UUID creator) {
        tippedMap.computeIfAbsent(tipper, k -> new HashSet<>()).add(creator);
    }

    public static void clearTips(UUID creator) {
        // Called when booster expires
        for (Set<UUID> tippedSet : tippedMap.values()) {
            tippedSet.remove(creator);
        }
    }
}
