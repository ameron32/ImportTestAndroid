package com.ameron32.importtestandroid;

import com.ameron32.importtestandroid.Downloader;
import com.ameron32.testing.ImportTesting;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.View;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity implements OnClickListener {

    TextView tvMain;
    ImportTesting it;
    String downloadDir = "https://dl.dropboxusercontent.com/u/949753/GURPS/GURPSBuilder/156/";
    String sdDir = Environment.getExternalStorageDirectory()
            .getPath() + "/ameron32projects/GURPSBattleFlow/";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvMain = (TextView) findViewById(R.id.tvMain);
        tvMain.setOnClickListener(ocl);
        start();
    }
    
    private void start() {
        it = new ImportTesting(new String[] { sdDir });
        String[] fileNames = ImportTesting.getAllFiles();
        String[] downloadLocations = fileNames.clone();
        for (int i = 0; i < downloadLocations.length; i++) {
            downloadLocations[i] = downloadDir + downloadLocations[i];
        }
        downloadAssets(null, fileNames, true, downloadLocations);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    ProgressDialog mDownloadDialog;
    private void downloadAssets(String dlDir, String[] fileNames,
            boolean update, String[] sUrl) {
        b = new Button(this);
 //       b.setOnClickListener(this);
//        b.setText("Close");
    
        final ProgressDialog mDownloadDialog = new ProgressDialog(MainActivity.this);
        this.mDownloadDialog = mDownloadDialog;
        mDownloadDialog.setTitle("Downloading from Dropbox...");
        mDownloadDialog.setMessage("");
        mDownloadDialog.setIndeterminate(false);
        mDownloadDialog.setMax(100);
        mDownloadDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mDownloadDialog.setCancelable(false);
        mDownloadDialog.setButton(ProgressDialog.BUTTON_POSITIVE, "Close", (android.content.DialogInterface.OnClickListener) this);

        // execute this when the downloader must be fired
        final Downloader d = new Downloader(MainActivity.this, mDownloadDialog, b);
        if (dlDir != null)
            d.setDlDir(dlDir);
        d.setDlFiles(fileNames);
        if (update)
            d.setUpdate(update);
        d.execute(sUrl);
    }

    Button b;

    @Override
    public void onClick(DialogInterface dialog, int which) {

    }
    
    android.view.View.OnClickListener ocl = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            it.main();
            tvMain.setText(ImportTesting.getSB());
        }
    };
    
}
