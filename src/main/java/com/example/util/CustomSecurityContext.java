package com.example.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.thymeleaf.util.MapUtils;
import org.thymeleaf.util.StringUtils;

public class CustomSecurityContext {


    private static final Map<String, Collection<ConfigAttribute>> METADATA_SOURCE_MAP = new HashMap<String, Collection<ConfigAttribute>>();

    public static Map<String, Collection<ConfigAttribute>> getMetadataSource() {

        if (MapUtils.isEmpty(METADATA_SOURCE_MAP)) {

            synchronized (CustomSecurityContext.class) {

                loadMetadataSource();
            }
        }
        return new HashMap<String, Collection<ConfigAttribute>>(METADATA_SOURCE_MAP);
    }


    private static void loadMetadataSource() {

        if (!MapUtils.isEmpty(METADATA_SOURCE_MAP)) {
            return;
        }
        InputStream inputStream = null;
        try {
            ResourcePatternResolver resourcePatternResolver = new PathMatchingResourcePatternResolver();
            Resource resource = resourcePatternResolver.getResource("classpath:auth.properties");
            if (resource == null) {
                return;
            }

            Properties properties = new Properties();
            inputStream = resource.getInputStream();
            properties.load(inputStream);

            for (Map.Entry<Object, Object> entry : properties.entrySet()) {

                String key = (String) entry.getKey();
                String value = (String) entry.getValue();

                String[] values = StringUtils.split(value, ",");

                Collection<ConfigAttribute> configAttributes = new ArrayList<ConfigAttribute>();
                ConfigAttribute configAttribute = new SecurityConfig(key);
                configAttributes.add(configAttribute);

                for (String v : values) {
                    METADATA_SOURCE_MAP.put(StringUtils.trim(v), configAttributes);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("加载MetadataSource失败", e);
        } finally {
        	 try {
        		 if (inputStream != null)
				inputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
    }
}
