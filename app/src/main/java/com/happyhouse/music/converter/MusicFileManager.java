package com.happyhouse.music.converter;

import android.content.Context;
import android.util.Log;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import javazoom.jl.converter.Converter;
import javazoom.jl.decoder.Decoder;
import javazoom.jl.decoder.JavaLayerException;

import static com.happyhouse.music.converter.MainActivity.PACKAGE_NAME_PATH;

class MusicFileManager {

    static String CACHE_FILE_PATH = null;
    public static String OUTPUT_FILE_PATH = "";

    //        MUSIC_PATH = "android.resource://" + PACKAGE_NAME + "/raw/";
//        musicFileManager = new MusicFileManager(getFilesDir().getParent() + File.separator + "cache_file"); //파일 매니저 로드

    public MusicFileManager(String path) { //기본 경로 설정 및 생성.
        CACHE_FILE_PATH = path;
        OUTPUT_FILE_PATH = "";
        File file = new File(path);
        if(!file.exists()){
            file.mkdirs();
        }
        CACHE_FILE_PATH = path;
    }

    public String convertMP3toWAV(Context context, int resource){
        //리턴을 할때는 파일의 이름이된 파일의 경로로 리턴을 하는게좋음.
        Converter converter = new Converter();
        //해당하는 이름을 맵에 추가해야함. //추가된 맵의 데이터를 불러와야함.
        //이걸로 input데이터를 만든다.
        try {
//            converter.convert(mp3.getAbsolutePath(), mp3.getParent() + File.separator + "test.wav"); //test ok
//            String outPath = MusicFileManager.CACHE_FILE_PATH + File.separator + "input.wav"; //input.wav에 숫자를 맥여야함.
            String outPath = MusicFileManager.CACHE_FILE_PATH + File.separator + context.getResources().getResourceEntryName(resource) + ".wav"; //input.wav에 숫자를 맥여야함.
            InputStream ins = context.getResources().openRawResource(resource);
            converter.convert(ins, outPath, null,  Decoder.getDefaultParams());

            return outPath;
        } catch (JavaLayerException e) {
            e.printStackTrace();
            return "ERROR";
        }
    }

    public static String convertMP3toWAV(String inputPath){
        Converter converter = new Converter();
        try {
            File file = new File(inputPath);
            String outFilePath = MusicFileManager.CACHE_FILE_PATH + File.separator + "input" + ".wav"; //input.wav에 숫자를 맥여야함.
//            String outFolderPath = MusicFileManager.CACHE_FILE_PATH + File.separator; //input.wav에 숫자를 맥여야함.
            InputStream targetStream = new FileInputStream(file);
            converter.convert(targetStream, outFilePath, null,  Decoder.getDefaultParams());
            return outFilePath;
        } catch (JavaLayerException e) {
            e.printStackTrace();
            return "ERROR";
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return "ERROR";
        }
    }

    public static String convertWAVtoMP3(String inputPath) {
        String outPath = "/storage/emulated/0/Music" + File.separator + "out.mp3"; //input.wav에 숫자를 맥여야함.
        String THE_REST_OF_YOUR_COMMAND = " -i " + inputPath + " -acodec libmp3lame " + outPath;
        String command = "data/user/0/" + PACKAGE_NAME_PATH + "/cache/ffmpeg" + THE_REST_OF_YOUR_COMMAND;
//        /data/user/0/com.happyhouse.music.converter/cache/ffmpeg
        try {
            Log.d("TEST", "Process Command : " + command);
            Process process = Runtime.getRuntime().exec("data/user/0/" + PACKAGE_NAME_PATH + "/cache/ffmpeg");
            process.waitFor();
            Log.d("TEST", "Process finished");
            return outPath;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "ERROR";
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "ERROR";
        }

    }

    /*public static String convertWAVtoMP3(String inputPath){
//        String outPath = MusicFileManager.CACHE_FILE_PATH + File.separator + "output" + ".mp3"; //input.wav에 숫자를 맥여야함.
        String outPath = "/storage/emulated/0/Music" + File.separator + "output.mp3"; //input.wav에 숫자를 맥여야함.
        try {
            File source = new File(inputPath);
            File target = new File(outPath);
            AudioAttributes audio = new AudioAttributes();
            audio.setCodec("libmp3lame");
            audio.setBitRate(new Integer(128000));
            audio.setChannels(new Integer(2));
            audio.setSamplingRate(new Integer(44100));

            EncodingAttributes attrs = new EncodingAttributes();
            attrs.setFormat("mp3");
            attrs.setAudioAttributes(audio);

            Encoder encoder = new Encoder(new MyFFMPEGExecutableLocator());
*//*            String[] a = encoder.getAudioDecoders();
            for(String data : a){
                Log.d("TEST", "format : " + data);
            }*//*
            encoder.encode(source, target, attrs);

            return outPath;
        } catch (Exception ex) {
            ex.printStackTrace();
            return "ERROR";
        }*/
/*        Converter converter = new Converter();
        try {
            File file = new File(inputPath);
            String outPath = MusicFileManager.CACHE_FILE_PATH + File.separator + "output" + ".mp3"; //input.wav에 숫자를 맥여야함.
            InputStream targetStream = new FileInputStream(file);
            converter.convert(targetStream, outPath, null,  Decoder.getDefaultParams());
            return outPath;
        } catch (JavaLayerException e) {
            e.printStackTrace();
            return "ERROR";
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return "ERROR";
        }

    }*/


    public static String convertMP3toWAV(InputStream ins, String outPath){
        Converter converter = new Converter();
        try {
            converter.convert(ins, outPath, null,  Decoder.getDefaultParams());
            return outPath;
        } catch (JavaLayerException e) {
            e.printStackTrace();
            return "ERROR";
        }
    }

    public static void directoryClear(){ //test ok;
        File file = new File(CACHE_FILE_PATH);
        if(file.isDirectory()){ //파일이 디렉토리인지 확인
            File[] files = file.listFiles();
            for( int i = 0; i < files.length; i++){
                files[i].delete();
            }
        }
    }
/*
    //목표는 기존의 path를 불러오는것.
    //output경로는 따로 설정해야함.
    protected String copyFile(int resource) { //리소스에 있는 파일복사해서 경로를 리턴함.
        try {
            InputStream in = getResources().openRawResource(resource); //재생하고자 하는 파일.
            FileOutputStream out = new FileOutputStream(getFilesDir().getParent() + "/files/input.wav");
            byte[] buff = new byte[1024];
            int read = 0;
            try {
                while ((read = in.read(buff)) > 0) {
                    out.write(buff, 0, read);
                }
                return getFilesDir().getParent() + "/files/input.wav";
            } finally {
                in.close();
                out.close();
            }
        } catch (Exception exp) {
            exp.printStackTrace();
        }
        return "ERROR";
    }*/
}