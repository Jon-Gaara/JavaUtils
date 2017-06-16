package com.yumaolin.util.FileResolve;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

public class FileUploadUtil {
    //private static final int BUFFER_SIZE = 131072;
    
    public static void fileCopy(final File src,final File dst) throws IOException {
	/*try {
		InputStream in = null;
		OutputStream out = null;
		try {
			in = new BufferedInputStream(new FileInputStream(src), BUFFER_SIZE);
			out = new BufferedOutputStream(new FileOutputStream(dst), BUFFER_SIZE);
			byte[] buffer = new byte[BUFFER_SIZE];
			while (in.read(buffer) > 0) {
				out.write(buffer);
			}
		} finally {
			if (in != null) {
				in.close();
			}
			if (out != null) {
				out.close();
			}
		}
	} catch (Exception e) {
		e.printStackTrace();
	}*/
	try(final RandomAccessFile inFile = new RandomAccessFile(src, "r" )){
	        try(final RandomAccessFile outFile = new RandomAccessFile(dst, "rw" );){
	            final FileChannel inChannel = inFile.getChannel();
	            final FileChannel outChannel = outFile.getChannel();
	            long pos = 0;
	            long toCopy = inFile.length();
	            while ( toCopy > 0 ){
	                final long bytes = inChannel.transferTo(pos,toCopy,outChannel);
	                pos += bytes;
	                toCopy -= bytes;
	            }
	        }
		}
    }

}
