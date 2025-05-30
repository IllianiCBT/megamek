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
package megamek.common.weapons.autocannons;

import megamek.common.SimpleTechLevel;

import java.io.Serial;

/**
 * @author Jason Tighe
 * @since Oct 2, 2004
 */
public class CLProtoMekAC2 extends ProtoMekACWeapon {

    @Serial
    private static final long serialVersionUID = 4371171653960292873L;

    public CLProtoMekAC2() {
        super();
        // CHECKSTYLE IGNORE ForbiddenWords FOR 3 LINES
        name = "ProtoMech AC/2";
        setInternalName("CLProtoMechAC2");
        addLookupName("Clan ProtoMech AC/2");
        heat = 1;
        damage = 2;
        rackSize = 2;
        minimumRange = 0;
        shortRange = 7;
        mediumRange = 14;
        longRange = 20;
        extremeRange = 30;
        tonnage = 3.5;
        criticals = 2;
        bv = 34;
        cost = 95000;
        shortAV = 2;
        medAV = 2;
        longAV = 2;
        maxRange = RANGE_LONG;
        explosionDamage = damage;
        rulesRefs = "286, TO";
        // Tech Progression tweaked to combine IntOps with TRO Prototypes/3145 NTNU RS
        techAdvancement.setTechBase(TechBase.CLAN)
                .setTechRating(TechRating.F).setAvailability(AvailabilityValue.X, AvailabilityValue.X, AvailabilityValue.F, AvailabilityValue.E)
                .setClanAdvancement(DATE_NONE, 3070, 3073, DATE_NONE, DATE_NONE)
                .setClanApproximate(false, true, false, false, false)
                .setPrototypeFactions(Faction.CBS).setProductionFactions(Faction.CBS)
                .setStaticTechLevel(SimpleTechLevel.STANDARD);
    }
}
