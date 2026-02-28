package com.karmorak.lib.engine.graphic;

import java.util.concurrent.ConcurrentLinkedQueue;

public class GLTaskQueue {
    // Eine thread-sichere Queue für Aufgaben
    private static final ConcurrentLinkedQueue<Runnable> tasks = new ConcurrentLinkedQueue<>();

    public static void add(Runnable task) {
        tasks.add(task);
    }

    public static void executeAll() {
        while (!tasks.isEmpty()) {
            Runnable task = tasks.poll();
            if (task != null) {
                task.run();
            }
        }
    }
}