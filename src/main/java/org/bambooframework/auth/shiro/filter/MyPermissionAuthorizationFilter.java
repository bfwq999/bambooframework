package org.bambooframework.auth.shiro.filter;

import java.io.IOException;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.web.filter.authz.PermissionsAuthorizationFilter;
import org.bambooframework.common.WebUtils;

public class MyPermissionAuthorizationFilter extends
		PermissionsAuthorizationFilter {

	@Override
	protected boolean onAccessDenied(ServletRequest request,
			ServletResponse response) throws IOException {
        if (WebUtils.isAjaxRequest((HttpServletRequest) request)) {
			((HttpServletResponse) response).sendError(HttpServletResponse.SC_UNAUTHORIZED);
		} else {
			super.onAccessDenied(request, response);
		}
        return false;
	}
}
