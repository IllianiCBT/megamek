/**
 * MegaMek - Copyright (C) 2004,2005 Ben Mazur (bmazur@sev.org)
 *
 *  This program is free software; you can redistribute it and/or modify it
 *  under the terms of the GNU General Public License as published by the Free
 *  Software Foundation; either version 2 of the License, or (at your option)
 *  any later version.
 *
 *  This program is distributed in the hope that it will be useful, but
 *  WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 *  or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 *  for more details.
 */
/*
 * Created on Sep 7, 2005
 *
 */
package megamek.common.weapons.infantry;

import megamek.common.AmmoType;

/**
 * @author Ben Grills
 */
public class InfantrySupportHeavyPPCWeapon extends InfantryWeapon {

	/**
	 *
	 */
	private static final long serialVersionUID = -3164871600230559641L;

	public InfantrySupportHeavyPPCWeapon() {
		super();

		name = "Particle Cannon (Support)";
		setInternalName(name);
		addLookupName("InfantrySupportPPC");
		addLookupName("InfantryHeavyPPC");
		addLookupName("Infantry Support PPC");
		ammoType = AmmoType.AmmoTypeEnum.INFANTRY;
		cost = 45000;
		bv = 11.32;
		flags = flags.or(F_NO_FIRES).or(F_DIRECT_FIRE).or(F_ENERGY).or(F_PPC).or(F_INF_SUPPORT);
		infantryDamage = 1.58;
		infantryRange = 3;
		crew = 5;
		ammoWeight = 0.0025;
		shots = 150;
		tonnage = 1.800;
		rulesRefs = "273, TM";
		techAdvancement.setTechBase(TechBase.ALL).setISAdvancement(2465, 2470, 2500, DATE_NONE, DATE_NONE)
		        .setISApproximate(true, false, false, false, false)
		        .setClanAdvancement(2465, 2470, 2500, DATE_NONE, DATE_NONE)
		        .setClanApproximate(true, false, false, false, false).setPrototypeFactions(Faction.TH)
		        .setProductionFactions(Faction.TH).setTechRating(TechRating.E)
		        .setAvailability(AvailabilityValue.C, AvailabilityValue.D, AvailabilityValue.C, AvailabilityValue.D);

	}
}
