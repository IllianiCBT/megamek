/*
 * MegaMek - Copyright (C) 2000-2002 Ben Mazur (bmazur@sev.org)
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
package megamek.common.loaders;

import megamek.common.Engine;
import megamek.common.Entity;
import megamek.common.EntityMovementMode;
import megamek.common.EquipmentType;
import megamek.common.FuelType;
import megamek.common.SupportTank;
import megamek.common.Tank;
import megamek.common.util.BuildingBlock;
import megamek.logging.MMLogger;

/**
 * @author njrkrynn
 * @since April 6, 2002, 2:06 AM
 */
public class BLKSupportTankFile extends BLKFile implements IMekLoader {
    private static final MMLogger logger = MMLogger.create(BLKSupportTankFile.class);

    public BLKSupportTankFile(BuildingBlock bb) {
        dataFile = bb;
    }

    @Override
    protected int defaultVGLFacing(int location, boolean rearFacing) {
        switch (location) {
            case SupportTank.LOC_RIGHT:
                return rearFacing ? 2 : 1;
            case SupportTank.LOC_REAR:
                return 3;
            case SupportTank.LOC_LEFT:
                return rearFacing ? 4 : 5;
            case SupportTank.LOC_FRONT:
            case SupportTank.LOC_TURRET:
            default:
                return 0;
        }
    }

    @Override
    public Entity getEntity() throws EntityLoadingException {

        SupportTank t = new SupportTank();
        setBasicEntityData(t);

        if (!dataFile.exists("tonnage")) {
            throw new EntityLoadingException("Could not find weight block.");
        }
        t.setWeight(dataFile.getDataAsDouble("tonnage")[0]);

        if (!dataFile.exists("motion_type")) {
            throw new EntityLoadingException("Could not find movement block.");
        }
        String sMotion = dataFile.getDataAsString("motion_type")[0];
        EntityMovementMode nMotion = EntityMovementMode.parseFromString(sMotion);
        if (nMotion == EntityMovementMode.NONE) {
            throw new EntityLoadingException("Invalid movement type: " + sMotion);
        }
        t.setMovementMode(nMotion);

        addTransports(t);

        int engineCode = BLKFile.FUSION;
        if (dataFile.exists("engine_type")) {
            engineCode = dataFile.getDataAsInt("engine_type")[0];
        }
        // TODO: At some point fix this to throw an error if missing
        if (dataFile.exists("fuel")) {
            t.setFuelTonnage(dataFile.getDataAsDouble("fuel")[0]);
        }
        int engineFlags = Engine.TANK_ENGINE | Engine.SUPPORT_VEE_ENGINE;
        if (!dataFile.exists("cruiseMP")) {
            throw new EntityLoadingException("Could not find cruiseMP block.");
        }
        int engineRating = Math.max(10,
                (dataFile.getDataAsInt("cruiseMP")[0] * (int) t.getWeight()) - t.getSuspensionFactor());
        if (dataFile.getDataAsInt("cruiseMP")[0] == 0) {
            engineRating = engineCode == BLKFile.NONE ? 0 : 10;
        }
        if ((engineRating % 5) > 0) {
            engineRating += (5 - (engineRating % 5));
        }
        t.setEngine(new Engine(engineRating, BLKFile.translateEngineCode(engineCode), engineFlags));
        t.setOriginalWalkMP(dataFile.getDataAsInt("cruiseMP")[0]);

        loadSVArmor(t);

        if (dataFile.exists("internal_type")) {
            t.setStructureType(dataFile.getDataAsInt("internal_type")[0]);
        } else {
            t.setStructureType(EquipmentType.T_STRUCTURE_STANDARD);
        }

        if (!dataFile.exists("armor")) {
            throw new EntityLoadingException("Could not find armor block.");
        }

        int[] armor = dataFile.getDataAsInt("armor");

        if ((armor.length < 4) || (armor.length > 6)) {
            throw new EntityLoadingException("Incorrect armor array length");
        }

        t.setHasNoTurret(armor.length == 4);
        t.setHasNoDualTurret(armor.length == 4 || armor.length == 5);

        // add the body to the armor array
        int[] fullArmor = new int[armor.length + 1];
        fullArmor[0] = 0;
        System.arraycopy(armor, 0, fullArmor, 1, armor.length);
        for (int x = 0; x < fullArmor.length; x++) {
            t.initializeArmor(fullArmor[x], x);
        }

        // Set the structural tech rating
        if (!dataFile.exists("structural_tech_rating")) {
            throw new EntityLoadingException("Could not find " +
                    "structural_tech_rating block!");
        }
        t.setStructuralTechRating(dataFile
                .getDataAsInt("structural_tech_rating")[0]);
        // Set armor tech rating, if it exists (defaults to structural tr)
        // Allow use of armor_tech field if provided
        if (dataFile.exists("armor_tech_rating")) {
            t.setArmorTechRating(dataFile
                    .getDataAsInt("armor_tech_rating")[0]);
        } else if (dataFile.exists("armor_tech")) {
            t.setArmorTechRating(dataFile
                    .getDataAsInt("armor_tech")[0]);
        }
        // Set engine tech rating, if it exists (defaults to structural tr)
        if (dataFile.exists("engine_tech_rating")) {
            t.setEngineTechRating(dataFile
                    .getDataAsInt("engine_tech_rating")[0]);
        }

        t.autoSetInternal();
        t.recalculateTechAdvancement();

        loadEquipment(t, "Front", Tank.LOC_FRONT);
        loadEquipment(t, "Right", Tank.LOC_RIGHT);
        loadEquipment(t, "Left", Tank.LOC_LEFT);
        loadEquipment(t, "Rear", Tank.LOC_REAR);
        if (!t.hasNoTurret()) {
            loadEquipment(t, "Turret", Tank.LOC_TURRET);
        }
        loadEquipment(t, "Body", Tank.LOC_BODY);

        if (dataFile.exists("omni")) {
            t.setOmni(true);
        }
        t.setArmorTonnage(t.getArmorWeight());

        if (dataFile.exists("baseChassisTurretWeight")) {
            t.setBaseChassisTurretWeight(dataFile.getDataAsDouble("baseChassisTurretWeight")[0]);
        }

        if (dataFile.exists("baseChassisTurret2Weight")) {
            t.setBaseChassisTurret2Weight(dataFile.getDataAsDouble("baseChassisTurret2Weight")[0]);
        }

        if (dataFile.exists("baseChassisSponsonPintleWeight")) {
            t.setBaseChassisSponsonPintleWeight(dataFile.getDataAsDouble("baseChassisSponsonPintleWeight")[0]);
        }

        if (dataFile.exists("baseChassisFireConWeight")) {
            t.setBaseChassisFireConWeight((dataFile.getDataAsDouble("baseChassisFireConWeight")[0]));
        }

        if (dataFile.exists("fuelType")) {
            try {
                t.setICEFuelType(FuelType.valueOf(dataFile.getDataAsString("fuelType")[0]));
            } catch (IllegalArgumentException ex) {
                logger.error("While loading " + t.getShortNameRaw()
                        + ": Could not parse ICE fuel type "
                        + dataFile.getDataAsString("fuelType")[0]);
                t.setICEFuelType(FuelType.PETROCHEMICALS);
            }
        }
        loadQuirks(t);
        return t;
    }
}
