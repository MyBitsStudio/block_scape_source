package com.alex.util.gzip;

import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPOutputStream;

public class GZipCompressor {

	@Nullable
	public static byte[] compress(byte[] data) {
		ByteArrayOutputStream compressedBytes = new ByteArrayOutputStream();
		try {
			GZIPOutputStream out = new GZIPOutputStream(compressedBytes);
			out.write(data);
			out.finish();
			out.close();
			return compressedBytes.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
