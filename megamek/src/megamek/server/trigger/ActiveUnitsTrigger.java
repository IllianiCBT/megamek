/*
 * Copyright (c) 2024 - The MegaMek Team. All Rights Reserved.
 *
 * This file is part of MegaMek.
 *
 * MegaMek is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * MegaMek is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with MegaMek. If not, see <http://www.gnu.org/licenses/>.
 */
package megamek.server.trigger;

import megamek.common.*;
import megamek.common.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This Trigger reacts when the count of active units is equal to the given count. When the
 * playerName is blank, units of all players are counted, otherwise only those of the given player. When a list
 * of unit IDs is given, only those units will be considered.
 * Note that immobile units or those with stunned crews or the like are still active. Only units that have
 * fled or are destroyed are no longer active.
 * Note that destroyed units can spawn pilots and some other objects are counted as in-game, so
 * providing an ID list can make sense.
 * Note that this trigger will react multiple times. Use {@link OnceTrigger} to limit it to one-time-only.
 */
public class ActiveUnitsTrigger implements Trigger {

    private final String playerName;
    private final int minUnitCount;
    private final int maxUnitCount;
    private final List<Integer> unitIds;

    public ActiveUnitsTrigger(@Nullable String playerName, List<Integer> unitIds, int minUnitCount, int maxUnitCount) {
        this.playerName = Objects.requireNonNullElse(playerName, "");
        this.minUnitCount = minUnitCount;
        this.maxUnitCount = maxUnitCount;
        this.unitIds = (unitIds == null) ? new ArrayList<>() : new ArrayList<>(unitIds);
    }

    public ActiveUnitsTrigger(@Nullable String playerName, List<Integer> unitIds, int fledUnitCount) {
        this(playerName, unitIds, fledUnitCount, fledUnitCount);
    }

    public ActiveUnitsTrigger(@Nullable String playerName, int fledUnitCount) {
        this(playerName, new ArrayList<>(), fledUnitCount);
    }

    public ActiveUnitsTrigger(@Nullable String playerName, int minUnitCount, int maxUnitCount) {
        this(playerName, new ArrayList<>(), minUnitCount, maxUnitCount);
    }

    @Override
    public boolean isTriggered(IGame game, TriggerSituation event) {
        long activeUnitCount = game.getInGameObjects().stream()
                .filter(this::matchesIdList)
                .filter(e -> matchesPlayerName(game.getPlayer(e.getOwnerId())))
                .count();

        return (activeUnitCount >= minUnitCount) && (activeUnitCount <= maxUnitCount);
    }

    private boolean matchesPlayerName(Player player) {
        return playerName.isBlank() || playerName.equals(player.getName());
    }

    private boolean matchesIdList(InGameObject unit) {
        return unitIds.isEmpty() || unitIds.contains(unit.getId());
    }

    @Override
    public String toString() {
        String result = "ActiveUnits: ";
        result += (minUnitCount >= 0 ? minUnitCount : "0") + (maxUnitCount < Integer.MAX_VALUE ? "-" + maxUnitCount : "+") + " of ";
        result += playerName.isBlank() ? "" : playerName + "/";
        result += unitIds.toString();
        return result;
    }
}
