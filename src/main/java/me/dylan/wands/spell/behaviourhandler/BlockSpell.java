package me.dylan.wands.spell.behaviourhandler;

import me.dylan.wands.pluginmeta.ListenerRegistry;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public class BlockSpell extends BaseBehaviour implements Listener {

    private final Material material;
    private final Consumer<Location> hitEffects;
    private final Set<Player> hasBlockSelected = new HashSet<>();

    private BlockSpell(AbstractBuilder.BaseMeta baseMeta, Material material, Consumer<Location> hitEffects) {
        super(baseMeta);
        this.material = material;
        this.hitEffects = hitEffects;
    }

    @Override
    public void cast(Player player) {
        ListenerRegistry.addListener(this);
    }

    public static Builder newBuilder(Material material) {
        return new Builder(material);
    }

    public static class Builder extends AbstractBuilder<Builder> {

        private final Material material;

        private Builder(Material material) {
            this.material = material;
        }

        @Override
        Builder self() {
            return this;
        }

        @Override
        public BaseBehaviour build() {
            return null;
        }
    }
}
