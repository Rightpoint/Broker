package com.raizlabs.android.request.rest;

/**
 * Author: andrewgrosner
 * Contributors: { }
 * Description:
 */
public abstract class RestAdapter {


    public abstract <RestClass> RestClass getRestInterface(Class<RestClass> restClass);
}
