package com.hardtech.bellapielpuntoscol;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class ApplicationLockUtil {

    private static final String PID_FILE_PATH = "application.pid";

    public static boolean isAppAlreadyRunning() {
        File pidFile = new File(PID_FILE_PATH);
        if (pidFile.exists()) {
            return true;
        } else {
            try {
                Files.write(pidFile.toPath(), String.valueOf(getCurrentPID()).getBytes());
            } catch (IOException e) {
                // Handle the exception
                e.printStackTrace();
            }
            return false;
        }
    }

    public static void releaseLock() {
        File pidFile = new File(PID_FILE_PATH);
        if (pidFile.exists()) {
            pidFile.delete();
        }
    }

    private static long getCurrentPID() {
        String processName = java.lang.management.ManagementFactory.getRuntimeMXBean().getName();
        return Long.parseLong(processName.split("@")[0]);
    }
}
