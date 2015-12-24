package ie.teamchile.smartapp.util;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import timber.log.Timber;

public class FileReadWrite {
	private Context context;
	private String fileName;
	private String note;
	private File file;
	
	public FileReadWrite(Context context,
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
		}catch(IOException e) {
			Timber.e(Log.getStackTraceString(e));
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
			Timber.e(Log.getStackTraceString(e));
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

