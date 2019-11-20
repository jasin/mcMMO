package jasin.mcmmo.datatypes.interactions;

public enum NotificationType {

    ABILITY_COOLDOWN("AbilityCooldown");

    final String niceName;

    NotificationType(String niceName) {
        this.niceName = niceName;
    }

    @Override
    public String toString() {
        return niceName;
    }
}
