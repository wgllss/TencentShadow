package com.common.framework.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.text.TextUtils;
import android.util.Log;

import com.common.framework.Threadpool.ThreadPoolTool;
import com.common.framework.application.CommonApplication;
import com.common.framework.http.HttpRequest;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {

    public final static String FILE_EXTENSION_SEPARATOR = ".";

    /**
     * read file
     *
     * @param filePath
     * @param charsetName The name of a supported {@link java.nio.charset.Charset </code>charset<code>}
     * @return if file not exist, return null, else return content of file
     * @throws RuntimeException if an error occurs while operator BufferedReader
     */
    public static StringBuilder readFile(String filePath, String charsetName) {
        File file = new File(filePath);
        StringBuilder fileContent = new StringBuilder("");
        if (file == null || !file.isFile()) {
            return null;
        }

        BufferedReader reader = null;
        try {
            InputStreamReader is = new InputStreamReader(new FileInputStream(file), charsetName);
            reader = new BufferedReader(is);
            String line = null;
            while ((line = reader.readLine()) != null) {
                if (!fileContent.toString().equals("")) {
                    fileContent.append("\r\n");
                }
                fileContent.append(line);
            }
            reader.close();
            return fileContent;
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    throw new RuntimeException("IOException occurred. ", e);
                }
            }
        }
    }

    /**
     * write file
     *
     * @param filePath
     * @param content
     * @param append   is append, if true, write to the end of file, else clear content of file and write into it
     * @return return true
     * @throws RuntimeException if an error occurs while operator FileWriter
     */
    public static boolean writeFile(String filePath, String content, boolean append) {
        FileWriter fileWriter = null;
        try {
            makeDirs(filePath);
            fileWriter = new FileWriter(filePath, append);
            fileWriter.write(content);
            fileWriter.close();
            return true;
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            if (fileWriter != null) {
                try {
                    fileWriter.close();
                } catch (IOException e) {
                    throw new RuntimeException("IOException occurred. ", e);
                }
            }
        }
    }

    /**
     * write file
     *
     * @param filePath
     * @param stream
     * @return
     * @see {@link #writeFile(String, InputStream, boolean)}
     */
    public static boolean writeFile(String filePath, InputStream stream) {
        return writeFile(filePath, stream, false);
    }

    /**
     * write file
     *
     * @param filePath   the file to be opened for writing.
     * @param stream the input stream
     * @param append if <code>true</code>, then bytes will be written to the end of the file rather than the beginning
     * @return return true
     * @throws RuntimeException if an error occurs while operator FileOutputStream
     */
    public static boolean writeFile(String filePath, InputStream stream, boolean append) {
        return writeFile(filePath != null ? new File(filePath) : null, stream, append);
    }

    /**
     * write file
     *
     * @param file
     * @param stream
     * @return
     * @see {@link #writeFile(File, InputStream, boolean)}
     */
    public static boolean writeFile(File file, InputStream stream) {
        return writeFile(file, stream, false);
    }

    /**
     * write file
     *
     * @param file   the file to be opened for writing.
     * @param stream the input stream
     * @param append if <code>true</code>, then bytes will be written to the end of the file rather than the beginning
     * @return return true
     * @throws RuntimeException if an error occurs while operator FileOutputStream
     */
    public static boolean writeFile(File file, InputStream stream, boolean append) {
        OutputStream o = null;
        try {
            makeDirs(file.getAbsolutePath());
            o = new FileOutputStream(file, append);
            byte data[] = new byte[1024];
            int length = -1;
            while ((length = stream.read(data)) != -1) {
                o.write(data, 0, length);
            }
            o.flush();
            return true;
        } catch (FileNotFoundException e) {
            throw new RuntimeException("FileNotFoundException occurred. ", e);
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            if (o != null) {
                try {
                    o.close();
                    stream.close();
                } catch (IOException e) {
                    throw new RuntimeException("IOException occurred. ", e);
                }
            }
        }
    }

    /**
     * copy file
     *
     * @param sourceFilePath
     * @param destFilePath
     * @return
     * @throws RuntimeException if an error occurs while operator FileOutputStream
     */
    public static boolean copyFile(String sourceFilePath, String destFilePath) {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(sourceFilePath);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("FileNotFoundException occurred. ", e);
        }
        return writeFile(destFilePath, inputStream);
    }

    /**
     * read file to string list, a element of list is a line
     *
     * @param filePath
     * @param charsetName The name of a supported {@link java.nio.charset.Charset </code>charset<code>}
     * @return if file not exist, return null, else return content of file
     * @throws RuntimeException if an error occurs while operator BufferedReader
     */
    public static List<String> readFileToList(String filePath, String charsetName) {
        File file = new File(filePath);
        List<String> fileContent = new ArrayList<String>();
        if (file == null || !file.isFile()) {
            return null;
        }

        BufferedReader reader = null;
        try {
            InputStreamReader is = new InputStreamReader(new FileInputStream(file), charsetName);
            reader = new BufferedReader(is);
            String line = null;
            while ((line = reader.readLine()) != null) {
                fileContent.add(line);
            }
            reader.close();
            return fileContent;
        } catch (IOException e) {
            throw new RuntimeException("IOException occurred. ", e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    throw new RuntimeException("IOException occurred. ", e);
                }
            }
        }
    }

    /**
     * get file name from path, not include suffix
     *
     * <pre>
     *      getFileNameWithoutExtension(null)               =   null
     *      getFileNameWithoutExtension("")                 =   ""
     *      getFileNameWithoutExtension("   ")              =   "   "
     *      getFileNameWithoutExtension("abc")              =   "abc"
     *      getFileNameWithoutExtension("a.mp3")            =   "a"
     *      getFileNameWithoutExtension("a.b.rmvb")         =   "a.b"
     *      getFileNameWithoutExtension("c:\\")              =   ""
     *      getFileNameWithoutExtension("c:\\a")             =   "a"
     *      getFileNameWithoutExtension("c:\\a.b")           =   "a"
     *      getFileNameWithoutExtension("c:a.txt\\a")        =   "a"
     *      getFileNameWithoutExtension("/home/admin")      =   "admin"
     *      getFileNameWithoutExtension("/home/admin/a.txt/b.mp3")  =   "b"
     * </pre>
     *
     * @param filePath
     * @return file name from path, not include suffix
     * @see
     */
    public static String getFileNameWithoutExtension(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return filePath;
        }

        int extenPosi = filePath.lastIndexOf(FILE_EXTENSION_SEPARATOR);
        int filePosi = filePath.lastIndexOf(File.separator);
        if (filePosi == -1) {
            return (extenPosi == -1 ? filePath : filePath.substring(0, extenPosi));
        }
        if (extenPosi == -1) {
            return filePath.substring(filePosi + 1);
        }
        return (filePosi < extenPosi ? filePath.substring(filePosi + 1, extenPosi) : filePath.substring(filePosi + 1));
    }

    /**
     * get file name from path, include suffix
     *
     * <pre>
     *      getFileName(null)               =   null
     *      getFileName("")                 =   ""
     *      getFileName("   ")              =   "   "
     *      getFileName("a.mp3")            =   "a.mp3"
     *      getFileName("a.b.rmvb")         =   "a.b.rmvb"
     *      getFileName("abc")              =   "abc"
     *      getFileName("c:\\")              =   ""
     *      getFileName("c:\\a")             =   "a"
     *      getFileName("c:\\a.b")           =   "a.b"
     *      getFileName("c:a.txt\\a")        =   "a"
     *      getFileName("/home/admin")      =   "admin"
     *      getFileName("/home/admin/a.txt/b.mp3")  =   "b.mp3"
     * </pre>
     *
     * @param filePath
     * @return file name from path, include suffix
     */
    public static String getFileName(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return filePath;
        }

        int filePosi = filePath.lastIndexOf(File.separator);
        return (filePosi == -1) ? filePath : filePath.substring(filePosi + 1);
    }

    /**
     * get folder name from path
     *
     * <pre>
     *      getFolderName(null)               =   null
     *      getFolderName("")                 =   ""
     *      getFolderName("   ")              =   ""
     *      getFolderName("a.mp3")            =   ""
     *      getFolderName("a.b.rmvb")         =   ""
     *      getFolderName("abc")              =   ""
     *      getFolderName("c:\\")              =   "c:"
     *      getFolderName("c:\\a")             =   "c:"
     *      getFolderName("c:\\a.b")           =   "c:"
     *      getFolderName("c:a.txt\\a")        =   "c:a.txt"
     *      getFolderName("c:a\\b\\c\\d.txt")    =   "c:a\\b\\c"
     *      getFolderName("/home/admin")      =   "/home"
     *      getFolderName("/home/admin/a.txt/b.mp3")  =   "/home/admin/a.txt"
     * </pre>
     *
     * @param filePath
     * @return
     */
    public static String getFolderName(String filePath) {

        if (TextUtils.isEmpty(filePath)) {
            return filePath;
        }

        int filePosi = filePath.lastIndexOf(File.separator);
        return (filePosi == -1) ? "" : filePath.substring(0, filePosi);
    }

    /**
     * get suffix of file from path
     *
     * <pre>
     *      getFileExtension(null)               =   ""
     *      getFileExtension("")                 =   ""
     *      getFileExtension("   ")              =   "   "
     *      getFileExtension("a.mp3")            =   "mp3"
     *      getFileExtension("a.b.rmvb")         =   "rmvb"
     *      getFileExtension("abc")              =   ""
     *      getFileExtension("c:\\")              =   ""
     *      getFileExtension("c:\\a")             =   ""
     *      getFileExtension("c:\\a.b")           =   "b"
     *      getFileExtension("c:a.txt\\a")        =   ""
     *      getFileExtension("/home/admin")      =   ""
     *      getFileExtension("/home/admin/a.txt/b")  =   ""
     *      getFileExtension("/home/admin/a.txt/b.mp3")  =   "mp3"
     * </pre>
     *
     * @param filePath
     * @return
     */
    public static String getFileExtension(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return filePath;
        }

        int extenPosi = filePath.lastIndexOf(FILE_EXTENSION_SEPARATOR);
        int filePosi = filePath.lastIndexOf(File.separator);
        if (extenPosi == -1) {
            return "";
        }
        return (filePosi >= extenPosi) ? "" : filePath.substring(extenPosi + 1);
    }

    /**
     * Creates the directory named by the trailing filename of this file, including the complete directory path required
     * to create this directory. <br/>
     * <br/>
     * <ul>
     * <strong>Attentions:</strong>
     * <li>makeDirs("C:\\Users\\Trinea") can only create users folder</li>
     * <li>makeFolder("C:\\Users\\Trinea\\") can create Trinea folder</li>
     * </ul>
     *
     * @param filePath
     * @return true if the necessary directories have been created or the target directory already exists, false one of
     * the directories can not be created.
     * <ul>
     * <li>if {@link FileUtils#getFolderName(String)} return null, return false</li>
     * <li>if target directory already exists, return true</li>
     * <li>return {@link File#}</li>
     * </ul>
     */
    public static boolean makeDirs(String filePath) {
        String folderName = getFolderName(filePath);
        if (TextUtils.isEmpty(folderName)) {
            return false;
        }

        File folder = new File(folderName);
        return (folder.exists() && folder.isDirectory()) ? true : folder.mkdirs();
    }

    /**
     * @param filePath
     * @return
     * @see #makeDirs(String)
     */
    public static boolean makeFolders(String filePath) {
        return makeDirs(filePath);
    }

    /**
     * Indicates if this file represents a file on the underlying file system.
     *
     * @param filePath
     * @return
     */
    public static boolean isFileExist(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return false;
        }

        File file = new File(filePath);
        return (file.exists() && file.isFile());
    }

    /**
     * Indicates if this file represents a directory on the underlying file system.
     *
     * @param directoryPath
     * @return
     */
    public static boolean isFolderExist(String directoryPath) {
        if (TextUtils.isEmpty(directoryPath)) {
            return false;
        }

        File dire = new File(directoryPath);
        return (dire.exists() && dire.isDirectory());
    }

    /**
     * delete file or directory
     * <ul>
     * <li>if path is null or empty, return true</li>
     * <li>if path not exist, return true</li>
     * <li>if path exist, delete recursion. return true</li>
     * <ul>
     *
     * @param path
     * @return
     */
    public static boolean deleteFile(String path) {
        if (TextUtils.isEmpty(path)) {
            return true;
        }

        File file = new File(path);
        if (!file.exists()) {
            return true;
        }
        if (file.isFile()) {
            return file.delete();
        }
        if (!file.isDirectory()) {
            return false;
        }
        for (File f : file.listFiles()) {
            if (f.isFile()) {
                f.delete();
            } else if (f.isDirectory()) {
                deleteFile(f.getAbsolutePath());
            }
        }
        return file.delete();
    }

    /**
     * get file size
     * <ul>
     * <li>if path is null or empty, return -1</li>
     * <li>if path exist and it is a file, return file size, else return -1</li>
     * <ul>
     *
     * @param path
     * @return returns the length of this file in bytes. returns -1 if the file does not exist.
     */
    public static long getFileSize(String path) {
        if (TextUtils.isEmpty(path)) {
            return -1;
        }

        File file = new File(path);
        return (file.exists() && file.isFile() ? file.length() : -1);
    }

    private static final int BUF_SIZE = 2048;

    /**
     * ???assets???jar???????????????dex??????
     *
     * @param context
     * @param dexInternalStoragePath
     * @param dex_file
     * @return
     * @author :Atar
     * @createTime:2017-2-13??????2:18:03
     * @version:1.0.0
     * @modifyTime:
     * @modifyAuthor:
     * @description:
     */
    public static boolean prepareDex(Context context, File dexInternalStoragePath, String dex_file) {
        BufferedInputStream bis = null;
        OutputStream dexWriter = null;
        try {
            bis = new BufferedInputStream(context.getAssets().open(dex_file));
            dexWriter = new BufferedOutputStream(new FileOutputStream(dexInternalStoragePath));
            byte[] buf = new byte[BUF_SIZE];
            int len;
            while ((len = bis.read(buf, 0, BUF_SIZE)) > 0) {
                dexWriter.write(buf, 0, len);
            }
            dexWriter.close();
            bis.close();
            return true;
        } catch (IOException e) {
            if (dexWriter != null) {
                try {
                    dexWriter.close();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                }
            }
            return false;
        }
    }

    public static void openFile(File f, Context context) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);

        /* ??????getMIMEType()?????????MimeType */
        String type = getMIMEType(f);
        /* ??????intent???file???MimeType */
        intent.setDataAndType(Uri.fromFile(f), type);
        context.startActivity(intent);
    }

    /* ????????????MimeType???method */
    @SuppressLint("DefaultLocale")
    private static String getMIMEType(File f) {
        String type = "";
        String fName = f.getName();
        /* ??????????????? */
        String end = fName.substring(fName.lastIndexOf(".") + 1, fName.length()).toLowerCase();

        /* ???????????????????????????MimeType */
        if (end.equals("m4a") || end.equals("mp3") || end.equals("mid") || end.equals("xmf") || end.equals("ogg") || end.equals("wav")) {
            type = "audio";
        } else if (end.equals("3gp") || end.equals("mp4")) {
            type = "video";
        } else if (end.equals("jpg") || end.equals("gif") || end.equals("png") || end.equals("jpeg") || end.equals("bmp")) {
            type = "image";
        } else if (end.equals("apk")) {
            /* android.permission.INSTALL_PACKAGES */
            type = "application/vnd.android.package-archive";
        } else {
            type = "*";
        }
        /* ??????????????????????????????????????????????????????????????? */
        if (end.equals("apk")) {
        } else {
            type += "/*";
        }
        return type;
    }

    public static String getCacheDir() {
        String strHomeDir = "";// ?????????
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {// ???SD???
            String str = Environment.getExternalStorageDirectory().toString();
            if (!str.endsWith("/") && !str.endsWith("\\")) {
                str += '/';
            }
            strHomeDir = str;
        } else {
            strHomeDir = CommonApplication.getContext().getFilesDir().getAbsolutePath();
        }

        return strHomeDir;
    }

    private final static String TAG = FileUtils.class.getCanonicalName();

    /**
     * ????????????????????????
     *
     * @param path ????????????
     * @return
     */
    public static boolean exists(String path) {
        try {
            File file = new File(path);
            if (!file.exists() || file.isFile() == false)// ?????????
                return false;
            else
                return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * ??????????????????
     *
     * @param filePath
     * @return
     */
    public static boolean delFile(String filePath) {
        boolean ret = false;
        Log.d(TAG, "delFile:>>" + filePath);
        try {
            File file = new File(filePath);
            if (file.exists()) {
                file.delete();
                ret = true;
            }
        } catch (Exception e) {
            Log.e(TAG, "delFile()>>" + e);
            return false;
        }
        return ret;
    }

    /**
     * ???????????????
     *
     * @param dirPath
     * @return
     */
    public static boolean delDir(String dirPath) {
        boolean ret = false;
        Log.d(TAG, "delDir:>>" + dirPath);
        try {
            File file = new File(dirPath);
            if (file.exists()) {
                File[] files = file.listFiles();
                for (int i = 0; i < files.length; i++) {
                    if (files[i].isDirectory()) {
                        if (!delDir(files[i].getName())) {
                            return false;
                        }
                    } else {
                        files[i].delete();
                    }
                }
                file.delete(); // ??????????????????
                ret = true;
            } else
                return true;
        } catch (Exception e) {
            Log.e(TAG, "delDir()>>" + e);
            return false;
        }
        return ret;
    }

    /**
     * ??????????????????????????????????????????
     *
     * @param path
     * @return File??????
     */
    public static File createDir(String path) {
        File file = new File(path);
        // ???????????????????????????
        File parent = new File(file.getAbsolutePath().substring(0, file.getAbsolutePath().lastIndexOf(File.separator)));
        // ????????????????????????????????????????????????????????????
        if (!parent.exists()) {
            createDir(parent.getPath());
            // ???????????????
            file.mkdirs();
        } else {
            // ????????????????????????
            File self = new File(path);
            if (!self.exists())
                self.mkdirs();
        }

        return file;
    }

    //????????????
    public static void newFile(String _path, String _fileName) {
        File file = new File(_path + "/" + _fileName);
        try {
            if (!exists(file.getAbsolutePath())) {
                file.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * ????????????????????????drawable???,??????????????????????????????
     *
     * @param absolutePath ????????????
     * @return
     */
    @SuppressWarnings("deprecation")
    public static Drawable getDrawableByFilePath(String absolutePath) {
        System.out.println("getDrawableByFileName()");
        Bitmap bitmap = null;
        Drawable drawable = null;
        try {
            if (absolutePath != null && !absolutePath.equals("")) {
                if (FileUtils.exists(absolutePath)) {// ????????????
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    // options.inPreferredConfig= Bitmap.Config.ARGB_8888;
                    options.inJustDecodeBounds = true;
                    InputStream is = new FileInputStream(new File(absolutePath));
                    bitmap = BitmapFactory.decodeStream(is, null, options);
                    // bitmap = BitmapFactory.decodeFile(absolutePath, options);

                    options.inSampleSize = computeSampleSize(options, -1, 128 * 128);
                    options.inJustDecodeBounds = false;
                    try {
                        bitmap = BitmapFactory.decodeStream(is, null, options);
                        /*
                         * bitmap = BitmapFactory .decodeFile(absolutePath, options);
                         */
                        if (!bitmap.isRecycled()) {
                            bitmap.recycle();
                            System.gc();
                        }

                    } catch (OutOfMemoryError err) {
                        System.out.println("OutOfMemoryError" + err.toString());
                    }

                    drawable = new BitmapDrawable(bitmap);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return drawable;
    }

    public static int computeSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);

        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }

        return roundedSize;
    }

    private static int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;

        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(Math.floor(w / minSideLength), Math.floor(h / minSideLength));

        if (upperBound < lowerBound) {
            // return the larger one when there is no overlapping zone.
            return lowerBound;
        }

        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }

    /**
     * ???????????????????????????????????????????????????
     *
     * @param sPath ??????????????????????????????
     * @return ????????????????????????true???????????????false
     */
    public boolean deleteDirectory(String sPath) {
        // ??????sPath?????????????????????????????????????????????????????????
        if (!sPath.endsWith(File.separator)) {
            sPath = sPath + File.separator;
        }
        File dirFile = new File(sPath);
        // ??????dir???????????????????????????????????????????????????????????????
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        boolean flag = true;
        // ?????????????????????????????????(???????????????)
        File[] files = dirFile.listFiles();
        for (int i = 0; i < files.length; i++) {
            // ???????????????
            if (files[i].isFile()) {
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag)
                    break;
            } // ???????????????
            else {
                flag = deleteDirectory(files[i].getAbsolutePath());
                if (!flag)
                    break;
            }
        }
        if (!flag)
            return false;
        // ??????????????????
        if (dirFile.delete()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * ???????????????????????????????????????????????????????????????
     *
     * @param sPath ???????????????????????????
     * @return ?????????????????? true??????????????? false???
     */
    public boolean DeleteFolder(String sPath) {
        boolean flag = false;
        File file = new File(sPath);
        // ?????????????????????????????????
        if (!file.exists()) { // ??????????????? false
            return flag;
        } else {
            // ?????????????????????
            if (file.isFile()) { // ????????????????????????????????????
                return deleteFile(sPath);
            } else { // ????????????????????????????????????
                return deleteDirectory(sPath);
            }
        }
    }

    @SuppressWarnings("resource")
    public static long getFileSizes(File f) throws Exception {// ??????????????????
        long s = 0;
        if (f.exists()) {
            FileInputStream fis = null;
            fis = new FileInputStream(f);
            s = fis.available();
        } else {
            f.createNewFile();
            System.out.println("???????????????");
        }
        return s;
    }

    // ??????
    public long getFileSize(File f) throws Exception// ?????????????????????
    {
        long size = 0;
        File flist[] = f.listFiles();
        for (int i = 0; i < flist.length; i++) {
            if (flist[i].isDirectory()) {
                size = size + getFileSize(flist[i]);
            } else {
                size = size + flist[i].length();
            }
        }
        return size;
    }

    public static String FormetFileSize(long fileS) {// ??????????????????
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "K";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "M";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "G";
        }
        return fileSizeString;
    }

    public long getlist(File f) {// ??????????????????????????????
        long size = 0;
        File flist[] = f.listFiles();
        size = flist.length;
        for (int i = 0; i < flist.length; i++) {
            if (flist[i].isDirectory()) {
                size = size + getlist(flist[i]);
                size--;
            }
        }
        return size;
    }

    /**
     * SD????????????????????????M
     *
     * @return
     * @author :Atar
     * @createTime:2017-8-22??????10:33:55
     * @version:1.0.0
     * @modifyTime:
     * @modifyAuthor:
     * @description:
     */
    @SuppressLint("NewApi")
    public static long getAvailable() {
        File path = Environment.getExternalStorageDirectory();
        StatFs stat = new StatFs(path.toString());
        // ????????????????????????????????????2.3????????? getBlockSize()??????????????????
        // ??????????????????????????????????????????????????????
        long blocksize;
        // long totalblock;
        long availbleblocks;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            blocksize = stat.getBlockSizeLong();
            // totalblock = stat.getBlockCountLong();
            availbleblocks = stat.getAvailableBlocksLong();
        } else {
            blocksize = stat.getBlockSize();
            // totalblock = stat.getBlockCount();
            availbleblocks = stat.getAvailableBlocks();
        }
        long size = availbleblocks * blocksize;
        size = size / (1024 * 1024);
        return size;
    }

    /**
     * ??????????????????
     *
     * @param fileUrl
     * @return
     * @author :Atar
     * @createTime:2011-8-18??????4:12:55
     * @version:1.0.0
     * @modifyTime:
     * @modifyAuthor:
     * @description:
     */
    public static int getUrlFileSize(String fileUrl) {
        int fileLength = 0;
        HttpURLConnection httpConnection = null;
        try {
            URL url = new URL(fileUrl);
            httpConnection = HttpRequest.getHttpURLConnection(url, 10000);
            HttpRequest.setConHead(httpConnection);
            httpConnection.connect();
            int responseCode = httpConnection.getResponseCode();
            if (responseCode <= 400) {
                fileLength = httpConnection.getContentLength();// ??????????????????
            }

        } catch (Exception e) {
            ShowLog.e(TAG, e);
        } finally {
            if (httpConnection != null)
                httpConnection.disconnect();// ????????????
        }
        return fileLength;
    }

    /**
     * ?????????????????????????????????weex???js webview???html
     *
     * @param downloadUrl ????????????
     * @param postfixName ???????????????
     * @param savePath    ??????????????????
     * @author :Atar
     * @createTime:2017-9-15??????9:45:10
     * @version:1.0.0
     * @modifyTime:
     * @modifyAuthor:
     * @description:
     */
    public static void downLoadFileAndSaveFileToCache(final String downloadUrl, final String postfixName, final String savePath) {
        ThreadPoolTool.getInstance().execute(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection httpConnection = null;
                try {
                    if (downloadUrl.contains(postfixName)) {
                        URL url = new URL(downloadUrl);
                        httpConnection = HttpRequest.getHttpURLConnection(url, 10000);
                        HttpRequest.setConHead(httpConnection);
                        httpConnection.connect();
                        int responseCode = httpConnection.getResponseCode();
                        if (responseCode == HttpURLConnection.HTTP_OK || responseCode == HttpURLConnection.HTTP_PARTIAL) {
                            InputStream instream = httpConnection.getInputStream();

                            File file = new File(savePath);
                            if (!exists(savePath)) {
                                createDir(savePath);
                            }
                            String strLocalFileName = downloadUrl.substring(downloadUrl.lastIndexOf("/") + 1, downloadUrl.length()).replace(postfixName, "");
                            File downloadFile = new File(file.getAbsolutePath(), MDPassword.getPassword32(strLocalFileName));
                            downloadFile.deleteOnExit();
                            downloadFile.createNewFile();
                            FileOutputStream outStream = new FileOutputStream(downloadFile);
                            BufferedInputStream bis = new BufferedInputStream(instream);
                            try {
                                if (instream != null) {
                                    byte[] buffer = new byte[1024];
                                    int len;
                                    while ((len = bis.read(buffer)) != -1) {
                                        outStream.write(buffer, 0, len);
                                    }
                                }
                            } catch (Exception e) {

                            } finally {
                                outStream.close();
                                bis.close();
                                instream.close();
                            }
                        }
                    }
                } catch (Exception e) {
                    ShowLog.e(TAG, e);
                } finally {
                    if (httpConnection != null)
                        httpConnection.disconnect();// ????????????
                }
            }
        });
    }
}
