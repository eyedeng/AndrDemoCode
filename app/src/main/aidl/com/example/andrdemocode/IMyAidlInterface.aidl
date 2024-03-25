// IMyAidlInterface.aidl
package com.example.andrdemocode;

// Declare any non-default types here with import statements
import com.example.andrdemocode.ipc.model.Car;

interface IMyAidlInterface {
    /**
     * Demonstrates some basic types that you can use as parameters
     * and return values in AIDL.
     */
    int getColor();

    void addCar(in Car car);

}