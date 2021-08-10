package com.cwi.cooperative.voting.helpers;

import java.io.IOException;
import java.util.Properties;

import org.springframework.core.io.support.PropertiesLoaderUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MessageProperties {
	
	private static MessageProperties messageProperties;
	private static Properties props;
	
	public static synchronized MessageProperties get() {
		
		if (messageProperties == null) {
			messageProperties=new MessageProperties();
			try {
				props = PropertiesLoaderUtils.loadAllProperties("messages_pt.properties");
			} catch (IOException err) {
				log.error("Erro ao carregar o arquivo 'messages_pt.properties'!", err);				
			}
		}
	    return messageProperties;
	}
	
	public String getMessage(String field) {
		if(field!=null && !field.trim().isEmpty()) {
			return props.getProperty(field);			
		}
		return field;
	}

}
