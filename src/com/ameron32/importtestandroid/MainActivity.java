package com.ameron32.importtestandroid;

import java.util.ArrayList;

import com.ameron32.importtestandroid.Downloader;
import com.ameron32.testing.ImportTesting;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {

    TextView tvMain;
    ImportTesting it;
    private final String downloadDir = "https://dl.dropboxusercontent.com/u/949753/GURPS/GURPSBuilder/158/";
    private final String sdDir = Environment.getExternalStorageDirectory()
            .getPath() + "/ameron32projects/GURPSBattleFlow/";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        start();
    }
    
    private void init() {
        tvMain = (TextView) findViewById(R.id.tvMain);
        tvMain.setOnClickListener(ocl);
    }
    
    private void start() {
        it = new ImportTesting(new String[] { sdDir });
        download();
//        String[] fileNames = ImportTesting.getAllFilenames();
//        String[] downloadLocations = fileNames.clone();
//        for (int i = 0; i < downloadLocations.length; i++) {
//            downloadLocations[i] = downloadDir + downloadLocations[i];
//        }
//        downloadAssets(null, fileNames, true, downloadLocations);
    }
    
    private void download() {
        String[][] fileNames = ImportTesting.getAllFilenames();
        
		// cheat to convert the fileNames for references on the file to the
		// right fileName
//        for (int n = 0; n < fileNames.length; n++) {
//        	for (String[] file : References.getReferences())
//        		if (fileNames[n].equalsIgnoreCase(file[2])) {
//        			fileNames[n] = file[3];
//        		}
//        }

        ArrayList<String> updateFileNames = new ArrayList<String>();
        ArrayList<String> noUpdateFileNames = new ArrayList<String>();
        ArrayList<String> updateDownloadLocations = new ArrayList<String>();
        ArrayList<String> noUpdateDownloadLocations = new ArrayList<String>();
        
        for (String[] fileInfo : fileNames) {
    		String fileName = fileInfo[0];
    		String update = fileInfo[1];
        	String downloadLocation = fileInfo[0];
        	// add "http" etc, if needed
        	if (!fileInfo[0].substring(0,3).equalsIgnoreCase("http")) {
        		downloadLocation = downloadDir + fileName;
        	} else {
        		downloadLocation = fileName;
        	}
        	
        	// determine update
        	if (fileInfo[1].equalsIgnoreCase("false")) {
        		noUpdateFileNames.add(fileName);
        		noUpdateDownloadLocations.add(downloadLocation);
        	} else if (fileInfo[1].equalsIgnoreCase("true")){
        		updateFileNames.add(fileName);
        		updateDownloadLocations.add(downloadLocation);
        	} else {
        		Log.e("UpdateStatusUnknown","Could not determine update status. Defaulting to YES.");
        		updateFileNames.add(fileName);
        		updateDownloadLocations.add(downloadLocation);
        	}
        }

//    	// add download directory to standalone filenames, if needed
//      for (int i = 0; i < downloadLocations.length; i++) {
//      	if (!downloadLocations[i][0].substring(0,3).equalsIgnoreCase("http"))
//      		downloadLocations[i][0] = downloadDir + downloadLocations[i][0];
//      }
        downloadAssets(null, updateFileNames, true, updateDownloadLocations);
        downloadAssets(null, noUpdateFileNames, false, noUpdateDownloadLocations);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    private void downloadAssets(
    		String dlDir, 
    		ArrayList<String> fileNames,
            boolean update, 
            ArrayList<String> sUrl) {
        // execute this when the downloader must be fired
        final Downloader d = new Downloader(MainActivity.this, null);
        if (dlDir != null)
            d.setDlDir(dlDir);
        d.setDlFiles(fileNames.toArray(new String[0]));
        if (update)
            d.setUpdate(update);
        d.execute(sUrl.toArray(new String[0]));
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {

    }
    
    android.view.View.OnClickListener ocl = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };
    
    Runnable updateText = new Runnable() {
		@Override
		public void run() {
			tvMain.setText(ImportTesting.getSB());
		}
	};
	Runnable importAndLoad = new Runnable() {
		@Override
		public void run() {
        	new ProgressMonitor(MainActivity.this, it, updateText).execute();
		}
	};
    
}
