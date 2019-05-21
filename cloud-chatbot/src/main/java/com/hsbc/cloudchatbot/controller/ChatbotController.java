package com.hsbc.cloudchatbot.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hsbc.cloudchatbot.utils.ReadFiles;
import com.hsbc.cloudchatbot.vo.PolicyVO;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;


/**
 * Created by nicole on 2019/5/18.
 */

@RestController
@SpringBootApplication
public class ChatbotController {
    private ResourceLoader resourceLoader = new DefaultResourceLoader();

    @RequestMapping("/")
    public String index() {
        return "Hello, I am Chatbot";
    }

    @RequestMapping(value = "/getPolicy", method = {RequestMethod.POST,RequestMethod.GET})
    public String getPolicy(@RequestParam(value = "policy" , required = false) String policy){
        PolicyVO policyVO;
        if (policy == null || Objects.equals(policy, "")) {
            policyVO = new PolicyVO();
        } else {
            policyVO = new PolicyVO(policy);
        }
        // Convert object to JSON string
        ObjectMapper mapper = new ObjectMapper();
        String jsonStr = "";
        try {
            jsonStr = mapper.writeValueAsString(policyVO);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return jsonStr;
    }


    @RequestMapping(value = "/getPolicy/{policy}", method = {RequestMethod.POST,RequestMethod.GET})
    public String policyQuery(@PathVariable(value = "policy", required = false) String policy) {
        return policy;
    }


//    @RequestMapping(value = "/getInfo/{q}", method = {RequestMethod.POST,RequestMethod.GET})
//    public String getInfo(@PathVariable String q) {
//        return "This is your question: " + q + "...... TBC";
//    }

    @RequestMapping(value = "/getInfo", method = {RequestMethod.POST,RequestMethod.GET})
    public String getInfo(@RequestParam(value = "question" , required = false) String question){
        String content = "";
        if(question == null || Objects.equals(question, "")) {
            return "Your question is ?";
        }
        try {
            ArrayList<String> cutWordsList = ReadFiles.cutWordsForQuestion(question);
            HashMap<String,HashMap<String, Float>> all_tf = ReadFiles.tfAllFiles(ReadFiles.fsName);
            HashMap<String, Float> idfs = ReadFiles.idf(all_tf);

            ReadFiles.tf_idf(all_tf, idfs);

            String maxMatchedFileName = ReadFiles.getMixMatchFileName(cutWordsList, all_tf);
            if (maxMatchedFileName != null && !Objects.equals(maxMatchedFileName, "")) {
                content = ReadFiles.readFile(maxMatchedFileName);
            } else {
                content = "Sorry, can not find the content, I will keep learning!";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }


}
