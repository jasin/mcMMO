package jasin.mcmmo.utils.experience;

import jasin.mcmmo.config.experience.ConfigExperience;

import java.lang.Math;

public final class FormulaManager {
    
    public int calculateXpNeeded(int lvl) {
        int base = ConfigExperience.getInstance().getBase();
        double multiplier = ConfigExperience.getInstance().getMultiplier();

        // We'll only concern ourselves with Linear XP
        return (int) Math.floor(base + lvl * multiplier);
    }
}
