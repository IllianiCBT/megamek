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
package megamek.common.weapons.battlearmor;

import megamek.common.AmmoType;
import megamek.common.weapons.ppc.PPCWeapon;

/**
 * @author Sebastian Brocks
 * @since Sep 24, 2004
 */
public class CLBASupportPPC extends PPCWeapon {
    private static final long serialVersionUID = 2062417699006705116L;

    public CLBASupportPPC() {
        super();
        name = "Support PPC";
        setInternalName("BA Support PPC");
        addLookupName("CLBASupportPPC");
        damage = 2;
        ammoType = AmmoType.AmmoTypeEnum.NA;
        shortRange = 2;
        mediumRange = 5;
        longRange = 7;
        extremeRange = 10;
        waterShortRange = 1;
        waterMediumRange = 3;
        waterLongRange = 5;
        waterExtremeRange = 7;
        bv = 14;
        tonnage = 0.240;
        criticals = 2;
        flags = flags.or(F_BA_WEAPON).andNot(F_MEK_WEAPON).andNot(F_TANK_WEAPON)
                .andNot(F_AERO_WEAPON).andNot(F_PROTO_WEAPON);
        cost = 14000;
        rulesRefs = "267, TM";
        techAdvancement.setTechBase(TechBase.CLAN).setTechRating(TechRating.D)
                .setAvailability(AvailabilityValue.X, AvailabilityValue.F, AvailabilityValue.D, AvailabilityValue.C)
                .setClanAdvancement(DATE_NONE, DATE_NONE, 2950, DATE_NONE, DATE_NONE);
    }
}
