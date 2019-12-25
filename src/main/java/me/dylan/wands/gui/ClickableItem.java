package me.dylan.wands.gui;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public abstract class ClickableItem<T> {
    private Consumer<T> submission;

    public abstract void callEvent(InventoryClickEvent event);

    public abstract ItemStack getItemStack();

    public ClickableItem<T> onSubmit(@Nullable Consumer<T> submission) {
        this.submission = submission;
        return this;
    }

    protected void submit(T t) {
        submission.accept(t);
    }
}
