package cn.com.ragnarok.elysion.common.web;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
/**
 * ±ძ¹�՚servletƤ׃"encoding"ʨ׃ת»»±ძ
 * @author Elysion
 *
 */
public class EncodingFilter implements Filter {
	private static  String encoding="utf-8";

	public void destroy() {
		// TODO Auto-generated method stub

	}

	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		
		request.setCharacterEncoding(encoding);
		response.setCharacterEncoding(encoding);
		chain.doFilter(request, response);

	}

	public void init(FilterConfig config) throws ServletException {
		String conf=config.getInitParameter("encoding");
		if(conf!=null){
			encoding=conf;
		}

	}

}
