package com.raizlabs.android.broker.rest;

/**
 * Author: andrewgrosner
 * Description: Used internally to connect the {@link com.raizlabs.android.broker.RequestManager}
 * to the annotated {@link BaseRestInterface} classes.
 */
public abstract class RestAdapter {

    /**
     * Returns the specified REST interface. The interface does not need to implement any other interface,
     * so this will return null if the specified class is not a valid interface.
     * @param restClass The class that is annotated with RestInterface annotation.
     * @param <RestClass>
     * @return The interface that exists for the class.
     */
    public abstract <RestClass> RestClass getRestInterface(Class<RestClass> restClass);
}
