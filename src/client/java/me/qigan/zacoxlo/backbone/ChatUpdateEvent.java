package me.qigan.zacoxlo.backbone;

import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class ChatUpdateEvent {
    public static List<Function<Component, Void>> chatUpdateEvents = new ArrayList<>();

    public static void register(Function<Component, Void> task) {
        chatUpdateEvents.add(task);
    }

    public static void post(Component component) {
        chatUpdateEvents.forEach((e) -> e.apply(component));
    }
}
