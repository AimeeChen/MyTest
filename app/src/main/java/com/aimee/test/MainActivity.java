package com.aimee.test;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    public static final String TAG = "MainActivity";
    public static final String PERMISSION = "android.Manifest.permission.WRITE_EXTERNAL_STORAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // 获取权限
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
            if (ContextCompat.checkSelfPermission(this, PERMISSION) !=
                    PackageManager.PERMISSION_GRANTED) {
                // 申请获取
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(MainActivity.this, "授权失败", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "授权成功", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 在Android7.0上使用此方法会抛出异常：
     * Caused by: android.os.FileUriExposedException: file:///storage/emulated/0/Pictures/Screenshots/imgtest.jpg exposed beyond app through Intent.getData()
     * @param view
     */
    public void openCrop(View view) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        String path = Environment.getExternalStorageDirectory() + "/Pictures/Screenshots/imgtest.jpg";
        Uri uri;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            /*
             * 在Android7.0上使用此方法会抛出异常：
             * Caused by: android.os.FileUriExposedException
             */
            uri = Uri.parse("file://" + path);
        } else {
            File file = new File("/storage/emulated/0/Pictures/Screenshots/imgtest.jpg");
            uri = FileProvider.getUriForFile(MainActivity.this, "com.aimee.test.fileprovider", file);
            Log.i(TAG, "uri = "+ uri.toString());
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        intent.setDataAndType(uri, "image/*");

        intent.putExtra("crop", "true");
        intent.putExtra("outputX", 80);
        intent.putExtra("outputY", 80);
        intent.putExtra("return-data", false);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
