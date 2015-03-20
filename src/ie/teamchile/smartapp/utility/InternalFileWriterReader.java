package ie.teamchile.smartapp.utility;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;
import android.util.Log;

public class InternalFileWriterReader {
	private Context context;
	private String fileName;
	private String note;
	private File file;
	
	/** 
	 * Barry Dempsey
	 * getFilesDir returns a file representing 
	 * the internal directory on the device. Writes text to
	 * this file and reads text back as required
	 * <uses-permission android:name="android.permission.WRITE_MEDIA_STORAGE" />
    	<uses-permission android:name="android.permission.READ_MEDIA_STORAGE" />
	 */

	public InternalFileWriterReader(Context context, 
			String fileName, String note) {
		this.context = context;
		this.fileName = fileName;
		this.note = note;
		file = new File(context.getFilesDir(), fileName);
	}
	
	public boolean writeToStorage() {
		try {
			FileOutputStream outputStream;
			outputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE);
			outputStream.write(note.getBytes());
			outputStream.close();
			return true;
		}catch(IOException ioe) {
			ioe.printStackTrace();			
		}	
		return false;
	}
	
	public String inputFromStorage() {
		FileInputStream inputStream;
		try {
			inputStream = context.openFileInput(fileName);
			InputStreamReader input = new InputStreamReader(inputStream);
			BufferedReader bsr = new BufferedReader(input);
			StringBuilder sb = new StringBuilder();
			String line = null;
			while((line = bsr.readLine()) != null) {
				sb.append(line);
			}
			return sb.toString();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "Error reading the file";
	}
	
	public boolean deleteTheFile(File file) {
		if(file.exists()) {
			file.delete();
			return true;
		}
		return false;
	}
	
	public File getFile() {
		return file;
	}
}
