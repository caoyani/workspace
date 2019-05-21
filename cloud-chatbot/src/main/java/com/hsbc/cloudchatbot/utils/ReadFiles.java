package com.hsbc.cloudchatbot.utils;
import java.io.*;
import java.util.*;
import org.wltea.analyzer.lucene.IKAnalyzer;

/**
 * Created by nicole on 2019/5/20.
 */
public class ReadFiles {

    public static String fsName;
    public static String[] nonimportantWords;

    static {
        Properties properties = new Properties();
        try {
            properties.load(new InputStreamReader(Thread.currentThread().getContextClassLoader().getResourceAsStream("config.properties"), "gbk"));
        } catch (IOException e) {
            System.out.println("Error loading the file config.properties"+ e);
        }
        fsName = properties.getProperty("file.system.path");
        nonimportantWords = properties.getProperty("nonimportant.words").split(",");
    }

    private static ArrayList<String> FileList = new ArrayList<String>(); // the list of file

    //get list of file for the directory, including sub-directory of it
    public static List<String> readDirs(String filepath) throws IOException
    {
        try
        {
            File file = new File(filepath);
            if(!file.isDirectory()) {
                System.out.println("filepath:" + file.getAbsolutePath() +". Please enter a folder! ");
            } else {
                String[] flist = file.list();
                assert flist != null;
                for (String aFlist : flist) {
                    File newfile = new File(filepath + "\\" + aFlist);
                    if (!newfile.isDirectory()) {
                        FileList.add(newfile.getAbsolutePath());
                    } else {
                        if (newfile.isDirectory()) //if file is a directory, call ReadDirs
                        {
                            readDirs(filepath + "\\" + aFlist);
                        }
                    }
                }
            }
        } catch(FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
        return FileList;
    }

    //read file
    public static String readFile(String file) throws IOException
    {
        StringBuffer strSb = new StringBuffer(); //String is constant， StringBuffer can be changed.
        InputStreamReader inStrR = new InputStreamReader(new FileInputStream(file), "gbk"); //byte streams to character streams
        BufferedReader br = new BufferedReader(inStrR);
        String line = br.readLine();
        while(line != null){
            strSb.append(line).append("\r\n");
            line = br.readLine();
        }
        return strSb.toString();
    }

    //word segmentation from file
    public static ArrayList<String> cutWords(String file) throws IOException{
        String text = ReadFiles.readFile(file);
        IKAnalyzer analyzer = new IKAnalyzer();
        return  analyzer.split(text);
    }

    //word segmentation from text
    public static ArrayList<String> cutWordsForQuestion(String text) throws IOException{
        IKAnalyzer analyzer = new IKAnalyzer();
        return analyzer.split(text);
    }

    //term frequency in a file, times for each word
    public static HashMap<String, Integer> normalTF(ArrayList<String> cutwords){
        HashMap<String, Integer> resTF = new HashMap<String, Integer>();
        for(String word : cutwords){
            if(resTF.get(word) == null){
                resTF.put(word, 1);
            } else {
                resTF.put(word, resTF.get(word) + 1);
            }
//            System.out.println(word);
        }
        return resTF;
    }

    //term frequency in a file, frequency of each word
    public static HashMap<String, Float> tf(ArrayList<String> cutwords){
        HashMap<String, Float> resTF = new HashMap<String, Float>();

        int wordLen = cutwords.size();
        HashMap<String, Integer> intTF = ReadFiles.normalTF(cutwords);

        Iterator iter = intTF.entrySet().iterator(); //iterator for that get from TF
        while(iter.hasNext()){
            Map.Entry entry = (Map.Entry)iter.next();
            resTF.put(entry.getKey().toString(), Float.parseFloat(entry.getValue().toString()) / wordLen);
//            System.out.println(entry.getKey().toString() + " = "+  Float.parseFloat(entry.getValue().toString()) / wordLen);
        }
        return resTF;
    }

    //tf times for file
    public static HashMap<String, HashMap<String, Integer>> normalTFAllFiles(String dirc) throws IOException{
        HashMap<String, HashMap<String, Integer>> allNormalTF = new HashMap<String, HashMap<String,Integer>>();

        List<String> filelist = ReadFiles.readDirs(dirc);
        for(String file : filelist){
            ArrayList<String> cutwords = ReadFiles.cutWords(file); //get cut word for one file

            HashMap<String, Integer> dict = ReadFiles.normalTF(cutwords);
            allNormalTF.put(file, dict);
        }
        return allNormalTF;
    }

    //tf for all file
    public static HashMap<String,HashMap<String, Float>> tfAllFiles(String dirc) throws IOException{
        HashMap<String, HashMap<String, Float>> allTF = new HashMap<String, HashMap<String, Float>>();
        List<String> filelist = ReadFiles.readDirs(dirc);

        for(String file : filelist){
            ArrayList<String> cutwords = ReadFiles.cutWords(file); //get cut words for one file

            HashMap<String, Float> dict = ReadFiles.tf(cutwords);
            allTF.put(file, dict);
        }
        return allTF;
    }
    public static HashMap<String, Float> idf(HashMap<String,HashMap<String, Float>> all_tf){
        HashMap<String, Float> resIdf = new HashMap<String, Float>();
        HashMap<String, Integer> dict = new HashMap<String, Integer>();
        int docNum = FileList.size();

        for(int i = 0; i < docNum; i++){
            HashMap<String, Float> temp = all_tf.get(FileList.get(i));
            Iterator iter = temp.entrySet().iterator();
            while(iter.hasNext()){
                Map.Entry entry = (Map.Entry)iter.next();
                String word = entry.getKey().toString();
                if(dict.get(word) == null){
                    dict.put(word, 1);
                }else {
                    dict.put(word, dict.get(word) + 1);
                }
            }
        }
//        System.out.println("IDF for every word is:");
        Iterator iter_dict = dict.entrySet().iterator();
        while(iter_dict.hasNext()){
            Map.Entry entry = (Map.Entry)iter_dict.next();
            float value = (float)Math.log(docNum / Float.parseFloat(entry.getValue().toString()));
            resIdf.put(entry.getKey().toString(), value);
//            System.out.println(entry.getKey().toString() + " = " + value);
        }
        return resIdf;
    }
    public static void tf_idf(HashMap<String,HashMap<String, Float>> all_tf,HashMap<String, Float> idfs){
        HashMap<String, HashMap<String, Float>> resTfIdf = new HashMap<String, HashMap<String, Float>>();

        int docNum = FileList.size();
        for(int i = 0; i < docNum; i++){
            String filepath = FileList.get(i);
            HashMap<String, Float> tfidf = new HashMap<String, Float>();
            HashMap<String, Float> temp = all_tf.get(filepath);
            Iterator iter = temp.entrySet().iterator();
            while(iter.hasNext()){
                Map.Entry entry = (Map.Entry)iter.next();
                String word = entry.getKey().toString();
                Float value = (float)Float.parseFloat(entry.getValue().toString()) * idfs.get(word);
                tfidf.put(word, value);
            }
            resTfIdf.put(filepath, tfidf);
        }
//        System.out.println("TF-IDF for Every file is :");
        DisTfIdf(resTfIdf);
    }
    public static void DisTfIdf(HashMap<String, HashMap<String, Float>> tfidf){
        Iterator iter1 = tfidf.entrySet().iterator();
        while(iter1.hasNext()){
            Map.Entry entrys = (Map.Entry)iter1.next();
//            System.out.println("FileName: " + entrys.getKey().toString());
//            System.out.print("{");
            HashMap<String, Float> temp = (HashMap<String, Float>) entrys.getValue();
            Iterator iter2 = temp.entrySet().iterator();
            while(iter2.hasNext()){
                Map.Entry entry = (Map.Entry)iter2.next();
//                System.out.print(entry.getKey().toString() + " = " + entry.getValue().toString() + ", ");
            }
//            System.out.println("}");
        }
    }

    //get max matched file name
    public static String getMixMatchFileName(ArrayList<String> cutWordsList, HashMap<String, HashMap<String, Float>> tfidf){
        String matchedFile = "";
        Float maxMatchedValue = 0f;
        for (String word : cutWordsList) {
            boolean skip = false;
//            System.out.println("000000000 " + word );
            for (int i = 0; i < nonimportantWords.length; i++) {
                if (word.equals(nonimportantWords[i])) {
                    skip = true;
                    break;
                }
            }
            //枚举
//            NonimportantWords[] arr = NonimportantWords.values();
//            for(int i=0;i<arr.length; i++){
//                if (word.equals(arr[i].getWord())) {
//                    skip = true;
//                    break;
//                }
//            }
            if (skip) {
                continue;
            }
            for (Map.Entry entrys : tfidf.entrySet()) {
                String filename = entrys.getKey().toString();
                HashMap<String, Float> temp = (HashMap<String, Float>) entrys.getValue();
                for (Map.Entry entry : temp.entrySet()) {
                    if (word.equals(entry.getKey().toString())) {
                        Float tfidfValue = (Float) entry.getValue();
//                        System.out.println("+++++++++" + filename + "  =  " + entry.getKey().toString() + "  =  " + tfidfValue);
                        if (tfidfValue > maxMatchedValue) {
                            maxMatchedValue = tfidfValue;
                            matchedFile = filename;
                        }
                    }
                }
            }
        }
        return matchedFile;
    }

//    public static void main(String[] args) throws IOException {
//        try {
//            String question = "什么是金融";
//            ArrayList<String> cutWordsList = ReadFiles.cutWordsForQuestion(question);
//
//            HashMap<String,HashMap<String, Float>> all_tf = ReadFiles.tfAllFiles(fsName);
//            HashMap<String, Float> idfs = ReadFiles.idf(all_tf);
//
//            ReadFiles.tf_idf(all_tf, idfs);
//
//            String maxMatchedFileName = ReadFiles.getMixMatchFileName(cutWordsList, all_tf);
//            System.out.println("----------------------"+maxMatchedFileName);
//            if (maxMatchedFileName != null && !Objects.equals(maxMatchedFileName, "")) {
//                String content = ReadFiles.readFile(maxMatchedFileName);
//                System.out.println(content);
//            } else {
//                System.out.println("Sorry, can not find the content, I will keep learning!");
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

}