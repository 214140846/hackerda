package com.hackerda.platform.infrastructure;

import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;


public class AntiDuplicateLinkedBlockingQueue<E> extends LinkedBlockingQueue<E> {

    private final Set<E> set = new HashSet<>();

    public AntiDuplicateLinkedBlockingQueue(int size) {
        super(size);
    }

    @SneakyThrows
    @Override
    public synchronized void put(E e) {
        if (!set.contains(e)) {
            set.add(e);
            super.put(e);
        }
    }

    @SneakyThrows
    @Override
    public synchronized boolean offer(@NotNull E e) {
        if (!set.contains(e)) {
            set.add(e);
            super.offer(e);
        }
        return true;

    }

    @SneakyThrows
    @Override
    public synchronized E take() {
        E take = super.take();
        set.remove(take);
        return take;
    }

}
