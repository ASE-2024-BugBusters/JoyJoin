package com.joyjoin.postservice.Util;

import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class Util {

    /**
     * Is used to transform the Object into a JSON String
     * @param obj
     * @return
     */
    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param mvcResult
     * @param objClass
     * @return
     * @param <T>
     * @throws IOException
     */
    public static  <T> T stringToObj(MvcResult mvcResult, Class<T> objClass) throws IOException {
        String content = mvcResult.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(content, objClass);
    }
}
