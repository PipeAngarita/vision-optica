package com.magmasoft.vision.api.filter;

import javax.servlet.*;
import java.io.IOException;

/**
 * Filtro de codificacion UTF-8 global.
 * MagmaSoft-Vision REST API | GA7-220501096-AA5-EV03
 */
public class EncodingFilter implements Filter {

    @Override
    public void init(FilterConfig config) throws ServletException {}

    @Override
    public void doFilter(ServletRequest req, ServletResponse res,
                         FilterChain chain) throws IOException, ServletException {
        req.setCharacterEncoding("UTF-8");
        res.setCharacterEncoding("UTF-8");
        chain.doFilter(req, res);
    }

    @Override
    public void destroy() {}
}
