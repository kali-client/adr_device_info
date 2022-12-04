package com.android.device.software;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaCodecInfo;
import android.media.MediaCodecList;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.core.content.ContextCompat;

import com.android.UApplication;
import com.android.utils.MD5;
import com.android.utils.ULog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public final class Media {

    //获取媒体音量
    private static JSONObject getSoundInfo() {
        JSONObject jsonObject = new JSONObject();
        try {
            AudioManager mAudioManager = (AudioManager) UApplication.getContext().getSystemService(Context.AUDIO_SERVICE);
            //通话音量
            int max = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL);
            int current = mAudioManager.getStreamVolume(AudioManager.STREAM_VOICE_CALL);
            jsonObject.put("voice_call", max + "--" + current);
            //系统音量
            max = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_SYSTEM);
            current = mAudioManager.getStreamVolume(AudioManager.STREAM_SYSTEM);
            jsonObject.put("system", max + "--" + current);
            //铃声音量
            max = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_RING);
            current = mAudioManager.getStreamVolume(AudioManager.STREAM_RING);
            jsonObject.put("ring", max + "--" + current);
            //音乐音量
            max = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            current = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            jsonObject.put("music", max + "--" + current);
            //提示声音音量
            max = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_ALARM);
            current = mAudioManager.getStreamVolume(AudioManager.STREAM_ALARM);
            jsonObject.put("alarm", max + "--" + current);
        } catch (Throwable e) {
            ULog.e(e);
        }
        return jsonObject;
    }

    public static int getImageCount() {
        Cursor cursor = null;
        try {
            cursor = UApplication.getContext().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, (String[]) null, (String) null, (String[]) null, (String) null);
            if (cursor != null) {
                return cursor.getCount();
            }
        } catch (Exception e) {
            ULog.e(e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return 0;
    }

    public static String getVolume() {
        try {
            AudioManager audioManager = (AudioManager) UApplication.getContext().getSystemService(Context.AUDIO_SERVICE);
            return String.valueOf(audioManager.getStreamVolume(AudioManager.STREAM_VOICE_CALL) + audioManager.getStreamVolume(AudioManager.STREAM_SYSTEM) + audioManager.getStreamVolume(AudioManager.STREAM_RING) +
                    audioManager.getStreamVolume(AudioManager.STREAM_MUSIC) + audioManager.getStreamVolume(AudioManager.STREAM_ALARM));
        } catch (Throwable e) {
            ULog.e(e);
        }
        return "";
    }

    private static JSONObject getImageList() {
        JSONObject jsonObject = new JSONObject();
        try {
            JSONArray jarr = new JSONArray();
            ContentResolver mContentResolver = UApplication.getContext().getContentResolver();
            Cursor mCursor = mContentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
            if (mCursor == null) {
                return jsonObject;
            }
            int n = 0;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                jsonObject.put("total", mCursor.getCount());
            } catch (Throwable e) {
                ULog.e(e);
            }

            while (mCursor.moveToNext()) {
                if (n >= 10) break;
                StringBuilder sb = new StringBuilder();
                try {
                    sb.append(mCursor.getString(mCursor
                            .getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME))).append("#");
                } catch (Throwable e) {
                    sb.append("-").append("#");
                }

                try {
                    //获取图片的路径
                    sb.append(MD5.stringToMD5(mCursor.getString(mCursor
                            .getColumnIndexOrThrow(MediaStore.Images.Media.DATA)) + mCursor.getLong(mCursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)) + mCursor.getLong(mCursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_MODIFIED)))).append("#");
                } catch (Throwable e) {
                    sb.append("-").append("#");
                }
                try {
                    sb.append(mCursor.getString(mCursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_TAKEN))).append("#");
                } catch (Throwable e) {
                    sb.append("-").append("#");
                }


                try {
                    long updateTime = mCursor.getLong(mCursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATE_MODIFIED));
                    updateTime = updateTime * 1000;
                    Date date = new Date(updateTime);
                    sb.append(sdf.format(date)).append("#");
                } catch (Throwable e) {
                    sb.append("-").append("#");
                }

                try {
                    sb.append("id_").append(mCursor.getLong(mCursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID))).append("#");
                } catch (Throwable e) {
                    sb.append("-").append("#");
                }
                if (sb.length() > 0) {
                    sb.deleteCharAt(sb.length() - 1);
                }
                jarr.put(sb.toString());
                n++;
            }
            jsonObject.put("ls", jarr);
            mCursor.close();
        } catch (Throwable e) {

        }
        return jsonObject;
    }

    private static JSONObject getVideoList() {
        JSONObject jsonObject = new JSONObject();
        try {
            String[] projects = {MediaStore.Audio.Media.TITLE, MediaStore.Audio.Media.DATA, MediaStore.Audio.Media.DATE_MODIFIED, MediaStore.Audio.Media._ID};
            JSONArray jarr = new JSONArray();
            Cursor cursor = UApplication.getContext().getContentResolver().
                    query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projects, null, null,
                            MediaStore.Audio.Media.DEFAULT_SORT_ORDER + " limit 10");
            if (cursor == null) return jsonObject;
            try {
                jsonObject.put("total", cursor.getCount());
            } catch (Throwable e) {
                ULog.e(e);
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            while (cursor.moveToNext()) {
                StringBuilder sb = new StringBuilder();
                try {
                    //歌曲的名称 ：MediaStore.Audio.Media.TITLE
                    sb.append(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.TITLE))).append("#");
                } catch (Throwable e) {
                    sb.append("-").append("#");
                }

                try {
                    sb.append(MD5.stringToMD5(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)) + cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID)) + cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATE_MODIFIED)))).append("#");
                } catch (Throwable e) {
                    sb.append("-").append("#");
                }

                try {
                    long updateTime = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATE_MODIFIED));
                    updateTime = updateTime * 1000;
                    Date date = new Date(updateTime);
                    sb.append(sdf.format(date)).append("#");
                } catch (Throwable e) {
                    sb.append("-").append("#");
                }

                try {
                    sb.append("id_").append(cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media._ID))).append("#");
                } catch (Throwable e) {
                    sb.append("-").append("#");
                }
                if (sb.length() > 0) {
                    sb.deleteCharAt(sb.length() - 1);
                }
                jarr.put(sb.toString());
            }
            jsonObject.put("ls", jarr);
            cursor.close();
        } catch (Throwable e) {
            ULog.e(e);
        }
        return jsonObject;
    }

    public static Map<String, String> getPhotoInfo() {
        // 相册信息
        if (ContextCompat.checkSelfPermission(UApplication.getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }
        try {
            Uri imageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            String[] projection = {MediaStore.Images.Media._ID
                    , MediaStore.Images.Media.DATE_MODIFIED
            };
            Cursor cursor = UApplication.getContext().getContentResolver().query(imageUri,
                    projection,
                    MediaStore.Images.Media.MIME_TYPE + "=? or " + MediaStore.Images.Media.MIME_TYPE + "=?",
                    new String[]{"image/jpeg", "image/png"},
                    MediaStore.Images.Media.DATE_MODIFIED + " DESC");
            Map<String, String> photoMap = null;
            if (cursor != null) {
                photoMap = new HashMap<>();
                if (cursor.moveToFirst()) {
                    photoMap.put("photo_count", cursor.getCount() + "");
                    @SuppressLint("Range") long dateModified = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.DATE_MODIFIED)) * 1000;
                    String dateModifiedString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date(dateModified));
                    photoMap.put("photo_last_date", dateModifiedString);
                }
                cursor.close();
            }
            return photoMap;
        } catch (Throwable e) {
        }
        return null;
    }


    public static JSONObject getMediaCodec() {
        MediaCodecList mediaCodecList = new MediaCodecList(1);
        if (mediaCodecList.getCodecInfos() == null) {
            return null;
        }

        try {
            JSONArray jsonArray = new JSONArray();
            JSONArray jsonArray2 = new JSONArray();
            for (MediaCodecInfo mediaCodecInfo : mediaCodecList.getCodecInfos()) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("name", mediaCodecInfo.getName());
                if (android.os.Build.VERSION.SDK_INT >= 29) {
                    jsonObject.put("isHardwareAccelerated", Integer.valueOf(mediaCodecInfo.isHardwareAccelerated() ? 1 : 0));
                }
                if (mediaCodecInfo.isEncoder()) {
                    jsonArray.put(jsonObject);
                } else {
                    jsonArray2.put(jsonObject);
                }
            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("encoder", jsonArray);
            jsonObject.put("decoder", jsonArray2);
            return jsonObject;
        } catch (Exception e) {
            e.printStackTrace();
        }


        return null;
    }

    public static JSONObject getMediaInfo() {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("sound", getSoundInfo());
            jsonObject.put("volume", getVolume());
            jsonObject.put("imageCount", getImageCount());
            jsonObject.put("imageList", getImageList());
            jsonObject.put("photoInfo", getPhotoInfo());
            jsonObject.put("videoList", getVideoList());
            jsonObject.put("mediaCodec", getMediaCodec());
        } catch (Throwable e) {
            ULog.e(e);
        }
        return jsonObject;
    }
}
