package ch.bbw.obelix.common;

import static org.springframework.data.web.config.EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

@Configuration
@EnableSpringDataWebSupport(pageSerializationMode = VIA_DTO)
public class CommonConfiguration {

	@Bean
	public CommonsRequestLoggingFilter logFilter() {
		var filter = new CommonsRequestLoggingFilter();
		filter.setIncludeQueryString(true);
		filter.setBeforeMessagePrefix(">>>>> [");
		filter.setAfterMessagePrefix("<<<<< [");
		filter.setIncludePayload(true);
		filter.setMaxPayloadLength(10000);
		filter.setIncludeHeaders(false);
		return filter;
	}
}
