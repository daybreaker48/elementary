package com.mhd.stard.util;

import android.content.Context;
import android.os.Environment;
import androidx.annotation.Nullable;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Comparator;


public class MHDLog {

    private static String mAppTag = MHDLog.class.getName();
    private static final String LOG_FILE_PATH = Environment.getExternalStorageDirectory() + "/MHDLOG";
    private static final int LOG_FILE_MAX_COUNT = 10;
    private static boolean mIsDebugMode = true;
    private static CrashReporter mCrashReporter;

    public static void init(String appTag, boolean debug, @Nullable CrashReporter crashReporter) {
        mIsDebugMode = debug;

        if(mAppTag != null)
            mAppTag = appTag;

        mCrashReporter = crashReporter;
    }

    /**
     * print debug
     */
    public static void d(String tag, Object... objects) {
        if (mIsDebugMode == true) Log.d("MHDLOG : " + tag, join(objects));
    }
    /**
     * print debug & 파일 저장
     */
    public static void d(String tag, boolean mIsTraceMode, Object... objects) {
        if (mIsDebugMode == true) Log.d("MHDLOG : " + tag, join(objects));
        if (mIsTraceMode) writeLog("D::" + tag, join(objects));
    }
    /**
     * print warning
     */
    public static void w(String tag, Object... objects) {
        if (mIsDebugMode == true) Log.w("MHDLOG : " + tag, join(objects));
    }
    /**
     * print warning & 파일 저장
     */
    public static void w(String tag, boolean mIsTraceMode, Object... objects) {
        if (mIsDebugMode == true) Log.w("MHDLOG : " + tag, join(objects));
        if (mIsTraceMode) writeLog("W::" + tag, join(objects));
    }
    /**
     * print info
     */
    public static void i(String tag, Object... objects) {
        if (mIsDebugMode == true) Log.i("MHDLOG : " + tag, join(objects));
    }
    /**
     * print info & 파일 저장
     */
    public static void i(String tag, boolean mIsTraceMode, Object... objects) {
        if (mIsDebugMode == true) Log.i("MHDLOG : " + tag, join(objects));
        if (mIsTraceMode) writeLog("I::" + tag, join(objects));
    }
    /**
     * print error
     */
    public static void e(String tag, Throwable throwable, Object... objects) {
        if (mIsDebugMode == true) Log.e("MHDLOG : " + tag, join(objects), throwable);
        else if(mCrashReporter != null)
            mCrashReporter.logException(new Exception(join(objects)));
    }
    /**
     * print error & throwable
     */
    public static void e(String tag, Object... objects) {
        if (mIsDebugMode == true) Log.e("MHDLOG : " + tag, join(objects));
        else if(mCrashReporter != null)
            mCrashReporter.logException(new Exception(join(objects)));
    }
    /**
     * print error & 파일 저장
     */
    public static void e(String tag, boolean mIsTraceMode, Object... objects) {
        if (mIsDebugMode == true) Log.e("MHDLOG : " + tag, join(objects));
        else if(mCrashReporter != null)
            mCrashReporter.logException(new Exception(join(objects)));

        if (mIsTraceMode) writeLog("E::" + tag, join(objects));
    }
    /**
     * print Exception
     */
    public static void printException(Exception e) {
        if(e != null) {
            String elog = Log.getStackTraceString(e);
            Log.e(mAppTag, "printException::" + elog);
        }
    }

    /**
     * 파일에 로그 저장
     */
    private static void writeLog(String tag, String log) {
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            String path = LOG_FILE_PATH;
            String fileName = Util.getInstance().getCurrentDate("yyyy-MM-dd") + ".txt";
            File file = new File(path , fileName);
            try {
                File dir = new File(path);
                if (dir.exists() == false) {
                    dir.mkdir();
                }
                if (file.exists() == false) {
                    file.createNewFile();
                    // 로그 파일 저장 개수 제한 7개
                    limitLogFileCount(file);
                }

                FileWriter fw = new FileWriter(file, true);
                BufferedWriter bw = new BufferedWriter(fw);
                final String currentDateAndTime = Util.getInstance().getCurrentDateAndTime(System.currentTimeMillis());

                bw.newLine();
                bw.append(currentDateAndTime + "    " + log);
                bw.newLine();
                bw.flush();
                bw.close();
            } catch (FileNotFoundException e) {
                printException(e);
            } catch (IOException e) {
                printException(e);
            }
        }
    }

    public static void writeAndroidRuntimeErrorLog(Context context) {
        String[] LOGCAT_CMD = new String[] {
                "logcat",
                "-d",
                "AndroidRuntime:E System.err:* APITask:* Utility:* ReTweetActivity:* ImagePreview:* TextPreview:* MovieView:* *:S" };
        Process logcatProc = null;

        try {
            logcatProc = Runtime.getRuntime().exec(LOGCAT_CMD);
        } catch (IOException e) {
            System.out.print("예외발생");
            return;
        }

        BufferedReader reader = null;
        String lineSeparatoer = System.getProperty("line.separator");
        StringBuilder strOutput = new StringBuilder();

        try {
            reader = new BufferedReader(new InputStreamReader(
                    logcatProc.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                strOutput.append(line);
                strOutput.append(lineSeparatoer);
            }
            reader.close();
        } catch (IOException e) {
            System.out.print("예외발생");
        }

        writeLog("AndroidRuntime:E", strOutput.toString());
    }

    private static void limitLogFileCount(File file) {
        File mLogFileInExternalMemory = new File(LOG_FILE_PATH);
        int mLogFileCount = mLogFileInExternalMemory.listFiles().length;

        if (mLogFileCount >= LOG_FILE_MAX_COUNT) {
            File[] mLogFileListInExternalMemory = mLogFileInExternalMemory
                    .listFiles();

            Arrays.sort(mLogFileListInExternalMemory, new Comparator<File>() {
                @Override
                public int compare(File o1, File o2) {
                    return o2.getName().compareTo(o1.getName());
                }
            });

            for (int i = 0; i < mLogFileCount; i++) {
                if (i >= LOG_FILE_MAX_COUNT) {
                    mLogFileListInExternalMemory[i].delete();

                    try {
                        FileWriter fw = new FileWriter(file, true);
                        BufferedWriter bw = new BufferedWriter(fw);

                        String log = "Deleted - Log file name : "
                                + mLogFileListInExternalMemory[i].getName();

                        bw.newLine();
                        bw.append(log);

                        bw.flush();
                        bw.close();
                    } catch (FileNotFoundException e) {
                        System.out.print("예외발생");
                    } catch (IOException e) {
                        System.out.print("예외발생");
                    }
                }
            }
        }
    }

    public static String join(Object... objects) {
        StringBuilder s = new StringBuilder();
        if(objects != null) {
            for(Object object : objects) {
                s.append(object).append(" ");
            }
        }
        else {
            s.append((String) null);
        }

        return s.toString();
    }

    public interface CrashReporter {
        void log(String message);
        void logException(Exception e);
    }
}


