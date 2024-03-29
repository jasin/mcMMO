package jasin.mcmmo.datatypes.experience;

public enum XPGainReason {
    
    PVP,
    PVE,
    VAMPIRISM,
    SHARED_PVP,
    SHARED_PVE,
    COMMAND,
    UNKNOWN;

    public static XPGainReason getXPGainReason(String reason) {
        for(XPGainReason type : values()) {
            if(type.name().equalsIgnoreCase(reason)) {
                return type;
            }
        }

        return null;
    }
}
