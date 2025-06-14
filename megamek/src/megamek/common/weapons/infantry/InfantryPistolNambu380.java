/*
 * Copyright (c) - 2004-2022 - The MegaMek Team. All Rights Reserved.
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
package megamek.common.weapons.infantry;

import megamek.common.AmmoType;

/**
 * @since March 20, 2022
 * @author Hammer
 */
public class InfantryPistolNambu380 extends InfantryWeapon {
   private static final long serialVersionUID = -3164871600230559641L;

   public InfantryPistolNambu380() {
       super();

       name = "Pistol (Nambu 380)";
       setInternalName(name);
       addLookupName("Nambu 380");
       ammoType = AmmoType.AmmoTypeEnum.INFANTRY;
       bv = 0.21;
       tonnage = 0.00095;
       infantryDamage = 0.21;
       infantryRange = 1;
       ammoWeight = 0.00004;
       cost = 350;
       ammoCost = 20;
       shots = 18;
       bursts = 1;
       flags = flags.or(F_NO_FIRES).or(F_DIRECT_FIRE).or(F_BALLISTIC);
       rulesRefs = "Shrapnel #3";
       techAdvancement
               .setTechBase(TechBase.IS)
               .setTechRating(TechRating.C)
               .setAvailability(AvailabilityValue.D,AvailabilityValue.D,AvailabilityValue.D,AvailabilityValue.D)
               .setISAdvancement(DATE_NONE, DATE_NONE,2100,DATE_NONE,DATE_NONE)
               .setISApproximate(false, false, true, false, false)
               .setProductionFactions(Faction.DC);
   }
}
