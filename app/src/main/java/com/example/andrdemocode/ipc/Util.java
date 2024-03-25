package com.example.andrdemocode.ipc;

import android.content.Context;

import com.example.andrdemocode.base.XLog;
import com.example.andrdemocode.ipc.model.User;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * @author dengyan
 * @date 2024/3/20
 * @desc
 */
public class Util {

    public static void objectPersistence(Context context) {
        User user = new User(10, "ed", false);
        File file = new File(context.getFilesDir(), "object");
        try (ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream(file))) {
            outputStream.writeObject(user);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try (ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream(file))) {
            User newUser = (User) inputStream.readObject();
            newUser.userId++;
            XLog.i(newUser.toString());
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }


    }

}
