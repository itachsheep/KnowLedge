package com.example.bitmapleandemo2;

import java.io.ByteArrayOutputStream;
import java.io.File;

import android.graphics.Bitmap;
import android.util.Log;

public class NativeUtil {
	private static String TAG = "BitmapCompressActivity.NativeUtil";
	
	static {
		System.loadLibrary("jpegbither");
		System.loadLibrary("bitherjni");
	}
	
	
	public static void compressBitmap(Bitmap bitmap,String filePath){
		Log.i(TAG, "jpegCompressImage" );
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
        // ����ѹ������������100��ʾ��ѹ������ѹ��������ݴ�ŵ�baos��
        int options = 20;
        // JNI���ñ���ͼƬ��SD�� ����ؼ�
        NativeUtil.saveBitmap(bitmap, options, filePath, true);
	}
	
	  /**
     * ����native����
     *
     * @param bit
     * @param quality
     * @param fileName
     * @param optimize
     * @Description:��������
     */
    public static void saveBitmap(Bitmap bit, int quality, String fileName, boolean optimize) {
        compressBitmap(bit, bit.getWidth(), bit.getHeight(), quality, fileName.getBytes(), optimize);
    }
    
    /**
     * ���õײ� bitherlibjni.c�еķ���
     *
     * @param bit
     * @param w
     * @param h
     * @param quality
     * @param fileNameBytes
     * @param optimize
     * @return
     * @Description:��������
     */
    public static native String compressBitmap(Bitmap bit, int w, int h, int quality, byte[] fileNameBytes,
                                                boolean optimize);


}
