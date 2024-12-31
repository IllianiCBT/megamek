/*
 * Copyright (c) 2021-2024 - The MegaMek Team. All Rights Reserved.
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
package megamek.client.generator.skillGenerators;

import megamek.client.generator.enums.SkillGeneratorMethod;
import megamek.common.Compute;
import megamek.common.Entity;
import megamek.common.Mek;
import megamek.common.Tank;
import megamek.common.enums.SkillLevel;
import megamek.logging.MMLogger;

public class TotalWarfareSkillGenerator extends AbstractSkillGenerator {
    private final static MMLogger logger = MMLogger.create(TotalWarfareSkillGenerator.class);

    // region Variable Declarations
    protected static final int[][] SKILL_LEVELS = new int[][] {
            { 7, 6, 5, 4, 4, 3, 2, 1, 0, 0 },
            { 7, 7, 6, 6, 5, 4, 3, 2, 1, 0 } };
    // endregion Variable Declarations

    // region Constructors
    public TotalWarfareSkillGenerator() {
        this(SkillGeneratorMethod.TOTAL_WARFARE);
    }

    protected TotalWarfareSkillGenerator(final SkillGeneratorMethod method) {
        super(method);
    }
    // endregion Constructors

    @Override
    public int[] generateRandomSkills(final Entity entity, final boolean clanPilot, final boolean forceClan) {
        return generateRandomSkills(getLevel(), entity, clanPilot, forceClan);
    }

    /**
     * Generates random pilot skills (gunnery and piloting levels) for an entity based on the provided skill level,
     * pilot type, and clan affiliation.
     * The skill levels are determined by rolling dice, applying bonuses, and then adjusting the results based on
     * the specified skill level. The final skills are bounded by the predefined skill arrays.
     *
     * @param level      the {@link SkillLevel} representing the desired skill level of the pilot (e.g., ULTRA_GREEN,
     *                   VETERAN, LEGENDARY). This influences the gunnery and piloting levels.
     * @param entity     the {@link Entity} for which the skills are being generated. This entity may influence bonuses.
     * @param clanPilot  a boolean flag indicating whether the pilot is affiliated with a clan:
     *                   <ul>
     *                     <li>{@code true}: Treats the pilot as a clan member, granting potential bonuses.</li>
     *                     <li>{@code false}: Treats the pilot as non-clan.</li>
     *                   </ul>
     * @param forceClan  a boolean flag that forces the pilot to be considered as a clan pilot for skill calculation
     *                   regardless of other factors. This may override default behavior.
     * @return an array of integers, where:
     *         <ul>
     *           <li>Index {@code 0}: The calculated gunnery skill level.</li>
     *           <li>Index {@code 1}: The calculated piloting skill level.</li>
     *         </ul>
     *
     * <p>Behavior and Process:</p>
     * <ol>
     *   <li>Calculates a skill bonus using the {@link #determineBonus(Entity, boolean, boolean)} method based on
     *       the entity's properties and pilot types.</li>
     *   <li>Determines random base rolls for gunnery and piloting using {@code Compute.d6(1)}.</li>
     *   <li>Adjusts the rolls based on the calculated bonus.</li>
     *   <li>Uses the provided {@link SkillLevel} to further adjust the gunnery and piloting levels (e.g., adding
     *       fixed offsets for VETERAN, HEROIC, etc.).</li>
     *   <li>Ensures the resulting gunnery and piloting values stay within the predefined {@code SKILL_LEVELS} array
     *       bounds using the {@link Math#min(int, int)} method.</li>
     *   <li>Returns the cleaned and bounded results as an integer array using the {@link #cleanReturn(Entity, int, int)} method.</li>
     * </ol>
     */
    protected int[] generateRandomSkills(final SkillLevel level, final Entity entity, final boolean clanPilot,
                                         final boolean forceClan) {
        final int bonus = determineBonus(entity, clanPilot, forceClan);

        final int gunneryRoll = Compute.d6(1) + bonus;
        final int pilotingRoll = Compute.d6(1) + bonus;

        final int gunneryLevel;
        final int pilotingLevel;
        switch (level) {
            case ULTRA_GREEN:
                gunneryLevel = (int) Math.ceil(gunneryRoll / 2.0);
                pilotingLevel = (int) Math.ceil(pilotingRoll / 2.0);
                break;
            case GREEN:
                gunneryLevel = (int) Math.ceil((gunneryRoll + 0.5) / 2.0);
                pilotingLevel = (int) Math.ceil((pilotingRoll + 0.5) / 2.0);
                break;
            case REGULAR:
                gunneryLevel = (int) Math.ceil(gunneryRoll / 2.0) + 2;
                pilotingLevel = (int) Math.ceil(pilotingRoll / 2.0) + 2;
                break;
            case VETERAN:
                gunneryLevel = (int) Math.ceil(gunneryRoll / 2.0) + 3;
                pilotingLevel = (int) Math.ceil(pilotingRoll / 2.0) + 3;
                break;
            case ELITE:
                gunneryLevel = (int) Math.ceil(gunneryRoll / 2.0) + 4;
                pilotingLevel = (int) Math.ceil(pilotingRoll / 2.0) + 4;
                break;
            case HEROIC:
                gunneryLevel = (int) Math.ceil(gunneryRoll / 2.0) + 5;
                pilotingLevel = (int) Math.ceil(pilotingRoll / 2.0) + 5;
                break;
            case LEGENDARY:
                gunneryLevel = (int) Math.ceil(gunneryRoll / 2.0) + 6;
                pilotingLevel = (int) Math.ceil(pilotingRoll / 2.0) + 6;
                break;
            default:
                logger.error("Attempting to generate skills for unknown skill level of " + level);
                gunneryLevel = 0;
                pilotingLevel = 0;
                break;
        }

        // Apply bounds for array access
        return cleanReturn(
            entity,
            SKILL_LEVELS[0][Math.min(gunneryLevel, SKILL_LEVELS[0].length - 1)],
            SKILL_LEVELS[1][Math.min(pilotingLevel, SKILL_LEVELS[1].length - 1)]
        );
    }

    /**
     * @param entity    the entity whose crew skill is being rolled
     * @param clanPilot if the crew is led by a clan pilot
     * @param forceClan forces the type to be clan if the crew are led by a
     *                  clanPilot
     * @return the bonus to use on the Random Skills Table (Expanded) roll
     */
    protected int determineBonus(final Entity entity, final boolean clanPilot,
            final boolean forceClan) {
        if (getType().isClan() || (forceClan && clanPilot)) {
            if (entity instanceof Mek) {
                return 2;
            } else if (entity instanceof Tank) {
                return -2;
            }
        }

        return 0;
    }
}
