package com.alex.util.bzip2;

import com.alex.util.CBZip2OutputStream;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class BZip2Compressor {

	@Nullable
	public static byte[] compress(byte[] data) {
		ByteArrayOutputStream compressedBytes = new ByteArrayOutputStream();
		try {
			CBZip2OutputStream out = new CBZip2OutputStream(compressedBytes);
			out.write(data);
			out.close();
			return compressedBytes.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
