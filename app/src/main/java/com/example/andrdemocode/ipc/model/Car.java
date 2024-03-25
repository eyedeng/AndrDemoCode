package com.example.andrdemocode.ipc.model;

import android.bluetooth.le.ScanResult;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author dengyan
 * @date 2024/3/20
 * @desc 序列化方式2：Android首选，内存序列化
 */
public class Car implements Parcelable {
    public int sn;
    public boolean isMax;
    public ScanResult scanResult;

    protected Car(Parcel in) {
        sn = in.readInt();
        isMax = in.readByte() != 0;
        scanResult = in.readParcelable(ScanResult.class.getClassLoader());
    }

    public static final Creator<Car> CREATOR = new Creator<Car>() {
        @Override
        public Car createFromParcel(Parcel in) {
            return new Car(in);
        }

        @Override
        public Car[] newArray(int size) {
            return new Car[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(sn);
        dest.writeByte((byte) (isMax ? 1 : 0));
        dest.writeParcelable(scanResult, flags);
    }


}
