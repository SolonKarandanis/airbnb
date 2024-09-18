package com.solon.airbnb.user.application.dto.serializer;

import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class CustomAuthorityDeserializer extends JsonDeserializer {
	
	private static final Logger log = LoggerFactory.getLogger(CustomAuthorityDeserializer.class);

	@Override
	public Object deserialize(JsonParser jp, DeserializationContext ctxt) 
			throws IOException, JacksonException {
		ObjectMapper mapper = (ObjectMapper) jp.getCodec();
        JsonNode jsonNode = mapper.readTree(jp);
        log.info("CustomAuthorityDeserializer ---> JsonNode :{}",jsonNode);
        List<GrantedAuthority> grantedAuthorities = new LinkedList<>();
        Iterator<JsonNode> elements = jsonNode.elements();
        log.info("CustomAuthorityDeserializer ---> elements :{}",elements);
        while (elements.hasNext()) {
        	JsonNode next = elements.next();
            JsonNode authority = next.get("authority");
            grantedAuthorities.add(new SimpleGrantedAuthority(authority.asText()));
        }
        
        return grantedAuthorities;
	}

}
