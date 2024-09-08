package lych.worldmodifiers.client.screen.entry;

import com.google.common.collect.Lists;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.util.FormattedCharSequence;

import javax.annotation.Nullable;
import java.util.List;

public abstract class BaseEntry extends ContainerObjectSelectionList.Entry<BaseEntry> {
    protected static final int DEPTH_OFFSET = 10;
    @Nullable
    private final List<FormattedCharSequence> tooltip;
    protected final List<AbstractWidget> children = Lists.newArrayList();
    protected final int entryDepth;

    public BaseEntry(@Nullable List<FormattedCharSequence> tooltip, int entryDepth) {
        this.tooltip = tooltip;
        this.entryDepth = entryDepth;
    }

    @Override
    public List<? extends GuiEventListener> children() {
        return children;
    }

    @Override
    public List<? extends NarratableEntry> narratables() {
        return children;
    }

    @Nullable
    public List<FormattedCharSequence> getTooltip() {
        return tooltip;
    }
}
