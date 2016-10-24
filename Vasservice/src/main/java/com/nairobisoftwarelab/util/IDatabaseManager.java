package com.nairobisoftwarelab.util;

import java.util.List;

/**
 * Created by pc on 10/18/2016.
 */
public interface IDatabaseManager<T> {
    List<T> getAll(String query);

    T get(String query);
}
