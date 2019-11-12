package jasin.mcmmo.utils.metadata;

import cn.nukkit.plugin.Plugin;
import cn.nukkit.metadata.MetadataValue;

public class FixedMetadataValue extends MetadataValue {

    private final Object value;

    public FixedMetadataValue(Plugin owningPlugin, final Object value) {
        super(owningPlugin);
        this.value = value;
    }

    // Invalidate does nothing making this value fixed.
    @Override
    public void invalidate() { }

    @Override
    public Object value() {
        return this.value;
    }
}
