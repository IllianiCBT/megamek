/*
 * MegaMek - Copyright (C) 2000-2005 Ben Mazur (bmazur@sev.org)
 * Copyright (c) 2018-2024 - The MegaMek Team. All Rights Reserved.
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 */

package megamek.common.weapons.infantry;

import megamek.common.AmmoType;


public class InfantryLaserPistolBrightStarL12 extends InfantryWeapon {

    private static final long serialVersionUID = 1L; // Update for each unique class

    public InfantryLaserPistolBrightStarL12() {
        super();

        name = "Laser Pistol (Brightstar L-12)";
        setInternalName(name);
        addLookupName("BRIGHTSTARL12");
        ammoType = AmmoType.AmmoTypeEnum.INFANTRY;
        cost = 1100;
        bv = 0.056;
        tonnage = 0.0012;
        infantryDamage = 0.19;
        infantryRange = 2;
        shots = 2;
        bursts = 1; // Bursts value is now always shown
        flags = flags.or(F_NO_FIRES).or(F_DIRECT_FIRE).or(F_LASER).or(F_ENERGY);
        rulesRefs = "Shrapnel #9";
        techAdvancement.setTechBase(TechBase.CLAN);
        techAdvancement.setClanAdvancement(DATE_NONE, DATE_NONE, 2800, DATE_NONE, DATE_NONE);
        techAdvancement.setTechRating(TechRating.E); 
        techAdvancement.setAvailability(AvailabilityValue.X, AvailabilityValue.E, AvailabilityValue.E, AvailabilityValue.E);
        techAdvancement.setClanApproximate(false, false, true, false, false);
        techAdvancement.setProductionFactions(Faction.CLAN);
    }
}
