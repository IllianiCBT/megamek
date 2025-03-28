/*
 * MegaMek - Copyright (C) 2004, 2005 Ben Mazur (bmazur@sev.org)
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 * for more details.
 */
package megamek.common.weapons;

import java.io.Serial;
import java.util.List;

import megamek.common.Aero;
import megamek.common.BombType;
import megamek.common.Entity;
import megamek.common.FighterSquadron;
import megamek.common.Game;
import megamek.common.HitData;
import megamek.common.Mounted;
import megamek.common.ToHitData;
import megamek.common.actions.WeaponAttackAction;
import megamek.logging.MMLogger;
import megamek.server.totalwarfare.TWGameManager;

/**
 * @author Jay Lawson
 * @since Sep 23, 2004
 */
public class SpaceBombAttackHandler extends WeaponHandler {
    private static final MMLogger LOGGER = MMLogger.create(SpaceBombAttackHandler.class);

    @Serial
    private static final long serialVersionUID = -2439937071168853215L;

    /**
     * @param toHit              {@link ToHitData} Object
     * @param weaponAttackAction {@link WeaponAttackAction} Object
     * @param game               {@link Game} Object
     * @param twGameManager      {@link TWGameManager} Object
     */
    public SpaceBombAttackHandler(ToHitData toHit, WeaponAttackAction weaponAttackAction, Game game,
                                  TWGameManager twGameManager) {
        super(toHit, weaponAttackAction, game, twGameManager);
        generalDamageType = HitData.DAMAGE_NONE;
    }

    /**
     * Calculate the attack value based on range
     *
     * @return an <code>int</code> representing the attack value at that range.
     */
    @Override
    protected int calcAttackValue() {
        int[] payload = waa.getBombPayload();
        if (null == payload) {
            return 0;
        }
        int numberOfBombs = 0;

        for (int j : payload) {
            numberOfBombs += j;
        }

        if (bDirect) {
            numberOfBombs = Math.min(numberOfBombs + (toHit.getMoS() / 3), numberOfBombs * 2);
        }

        numberOfBombs = applyGlancingBlowModifier(numberOfBombs, false);

        return numberOfBombs;
    }

    /**
     * Does this attack use the cluster hit table? necessary to determine how Aero damage should be applied
     */
    @Override
    protected boolean usesClusterTable() {
        return true;
    }

    @Override
    protected void useAmmo() {
        int[] payload = waa.getBombPayload();
        if (!(ae.isAero()) || null == payload) {
            return;
        }

        // Need to remove ammo from fighters within a squadron
        if (ae instanceof FighterSquadron) {
            // In a fighter squadron, we will have had dropped a salvo of bombs. The salvo consists of one bomb from
            // each fighter equipped with a bomb of the proper type.
            for (int type = 0; type < payload.length; type++) {
                List<Entity> activeFighters = ae.getActiveSubEntities();

                if (activeFighters.isEmpty()) {
                    break;
                }

                int fighterIndex = 0;
                for (int i = 0; i < payload[type]; i++) {
                    boolean bombRemoved = false;
                    int iterations = 0;
                    while (!bombRemoved && iterations <= activeFighters.size()) {
                        Aero fighter = (Aero) activeFighters.get(fighterIndex);
                        // find the first mounted bomb of this type and drop it
                        for (Mounted<?> bomb : fighter.getBombs()) {
                            if (((BombType) bomb.getType()).getBombType() == type &&
                                      !bomb.isDestroyed() &&
                                      bomb.getUsableShotsLeft() > 0) {
                                bomb.setShotsLeft(0);
                                bombRemoved = true;
                                break;
                            }
                        }
                        iterations++;
                        fighterIndex = (fighterIndex + 1) % activeFighters.size();
                    }

                    if (iterations > activeFighters.size()) {
                        LOGGER.error("Couldn't find ammo for a dropped bomb");
                    }
                }
                // Now remove a bomb from the squadron
                if (payload[type] > 0) {
                    int numSalvos = (int) Math.ceil((payload[type] + 0.0) / activeFighters.size());

                    for (int salvo = 0; salvo < numSalvos; salvo++) {
                        for (Mounted<?> bomb : ae.getBombs()) {
                            if (((BombType) bomb.getType()).getBombType() == type &&
                                      !bomb.isDestroyed() &&
                                      bomb.getUsableShotsLeft() > 0) {
                                bomb.setShotsLeft(0);
                                break;
                            }
                        }
                    }
                }
            }
        } else { // Ammo expenditure for a single fighter
            for (int type = 0; type < payload.length; type++) {
                for (int i = 0; i < payload[type]; i++) {
                    // find the first mounted bomb of this type and drop it
                    for (Mounted<?> bomb : ae.getBombs()) {
                        if (((BombType) bomb.getType()).getBombType() == type &&
                                  !bomb.isDestroyed() &&
                                  bomb.getUsableShotsLeft() > 0) {
                            bomb.setShotsLeft(0);
                            break;
                        }
                    }
                }
            }
        }

        super.useAmmo();
    }
}
