package org.bambooframework.web.servlet.resource;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.util.StreamUtils;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;
import org.springframework.web.util.UrlPathHelper;

/**
 * 处理html模板的内容
 * @author lei
 * @date 2015年9月16日
 * @Description:
 * 	在每个内容前面加上跳转,当浏览器单独打开页面时,进行跳转
 */
public class HtmlResourceHttpRequestHandler extends ResourceHttpRequestHandler {
	private static final Log logger = LogFactory.getLog(HtmlResourceHttpRequestHandler.class);

	protected void writeContent(HttpServletRequest request,HttpServletResponse response, Resource resource)
			throws IOException {
		String path =  new UrlPathHelper().getLookupPathForRequest(request);
		String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
		String pre = "<script type=\"text/javascript\">if(typeof(angular) == \"undefined\"){window.location.href = \""+
				basePath+"main.html?url="+path
				+"\";}</script>\r\n";
		//以/views开头的模板
		if(path.length()>6 && "/views".equals(path.substring(0, 6))){
			//html 模板
			byte[] bytes = pre.getBytes();
			long length = resource.contentLength()+bytes.length;
			if (length > Integer.MAX_VALUE) {
				throw new IOException("Resource content too long (beyond Integer.MAX_VALUE): " + resource);
			}
			response.setContentLength((int) length);
			response.getOutputStream().write(bytes);
			response.getOutputStream().flush();
		}
		super.writeContent(response, resource);
	}

	@Override
	public void handleRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		checkAndPrepare(request, response, true);

		// check whether a matching resource exists
		Resource resource = getResource(request);
		if (resource == null) {
			logger.trace("No matching resource found - returning 404");
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			return;
		}

		// check the resource's media type
		MediaType mediaType = getMediaType(resource);
		if (mediaType != null) {
			if (logger.isTraceEnabled()) {
				logger.trace("Determined media type '" + mediaType + "' for " + resource);
			}
		}
		else {
			if (logger.isTraceEnabled()) {
				logger.trace("No media type found for " + resource + " - not sending a content-type header");
			}
		}

		// header phase
		if (new ServletWebRequest(request, response).checkNotModified(resource.lastModified())) {
			logger.trace("Resource not modified - returning 304");
			return;
		}
		setHeaders(response, resource, mediaType);

		// content phase
		if (METHOD_HEAD.equals(request.getMethod())) {
			logger.trace("HEAD request - skipping content");
			return;
		}
		writeContent(request,response, resource);;
	}

}
