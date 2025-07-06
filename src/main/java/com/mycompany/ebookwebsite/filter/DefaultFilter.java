/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Filter.java to edit this template
 */
package com.mycompany.ebookwebsite.filter;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Enhanced DefaultFilter with infinite loop protection
 * @author ADMIN
 */
@WebFilter(filterName = "DefaultFilter", urlPatterns = {"/*"})
public class DefaultFilter implements Filter {
    
    private static final boolean debug = false; // ✅ TẮTDEBUG để giảm noise
    private static final String LOOP_PREVENTION_ATTR = "DEFAULT_FILTER_PROCESSED";
    private static final int MAX_EXCEPTION_DEPTH = 3;

    // The filter configuration object we are associated with.  If
    // this value is null, this filter instance is not currently
    // configured.
    private FilterConfig filterConfig = null;
    
    public DefaultFilter() {
    }
    
    private void doBeforeProcessing(ServletRequest request, ServletResponse response)
            throws IOException, ServletException {
        if (debug) {
            log("DefaultFilter:DoBeforeProcessing");
        }
        
        // ✅ THÊM: Đánh dấu request đã được xử lý để tránh vòng lặp
        request.setAttribute(LOOP_PREVENTION_ATTR, Boolean.TRUE);
    }
    
    private void doAfterProcessing(ServletRequest request, ServletResponse response)
            throws IOException, ServletException {
        if (debug) {
            log("DefaultFilter:DoAfterProcessing");
        }
    }

    /**
     * ✅ ENHANCED: doFilter với cơ chế ngăn vòng lặp
     */
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain)
            throws IOException, ServletException {
        
        // ✅ KIỂM TRA: Ngăn vòng lặp filter
        if (request.getAttribute(LOOP_PREVENTION_ATTR) != null) {
            if (debug) {
                log("DefaultFilter: Loop detected, skipping processing");
            }
            chain.doFilter(request, response);
            return;
        }
        
        if (debug) {
            log("DefaultFilter:doFilter()");
        }
        
        doBeforeProcessing(request, response);
        
        Throwable problem = null;
        try {
            chain.doFilter(request, response);
        } catch (Throwable t) {
            // ✅ ENHANCED: Kiểm tra độ sâu exception để tránh vòng lặp
            int exceptionDepth = getExceptionDepth(t);
            if (exceptionDepth > MAX_EXCEPTION_DEPTH) {
                log("DefaultFilter: Exception depth exceeded (" + exceptionDepth + "), breaking loop");
                sendSimpleError(response, "Internal server error - loop detected");
                return;
            }
            
            problem = t;
            if (debug) {
                t.printStackTrace();
            }
        }
        
        doAfterProcessing(request, response);

        // ✅ ENHANCED: Xử lý exception an toàn hơn
        if (problem != null) {
            if (problem instanceof ServletException) {
                ServletException se = (ServletException) problem;
                // Kiểm tra nếu là vòng lặp ServletException
                if (isLoopingServletException(se)) {
                    log("DefaultFilter: Looping ServletException detected, sending simple error");
                    sendSimpleError(response, "Internal server error - servlet loop");
                    return;
                }
                throw se;
            }
            if (problem instanceof IOException) {
                throw (IOException) problem;
            }
            sendProcessingError(problem, response);
        }
    }

    /**
     * ✅ MỚI: Kiểm tra ServletException có đang lặp không
     */
    private boolean isLoopingServletException(ServletException se) {
        String message = se.getMessage();
        if (message != null && message.contains("jakarta.servlet.ServletException:")) {
            // Đếm số lần "ServletException" xuất hiện trong message
            int count = 0;
            int index = 0;
            while ((index = message.indexOf("ServletException", index)) != -1) {
                count++;
                index += "ServletException".length();
                if (count > 5) { // Nếu xuất hiện quá 5 lần = vòng lặp
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * ✅ MỚI: Đếm độ sâu exception
     */
    private int getExceptionDepth(Throwable t) {
        int depth = 0;
        Throwable current = t;
        while (current != null && depth < 50) { // Giới hạn để tránh vòng lặp vô hạn
            depth++;
            current = current.getCause();
        }
        return depth;
    }

    /**
     * ✅ MỚI: Gửi lỗi đơn giản để tránh vòng lặp
     */
    private void sendSimpleError(ServletResponse response, String message) {
        try {
            if (response instanceof HttpServletResponse) {
                HttpServletResponse httpResponse = (HttpServletResponse) response;
                if (!httpResponse.isCommitted()) {
                    httpResponse.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message);
                }
            }
        } catch (Exception e) {
            // Nếu không gửi được error response, chỉ log
            log("DefaultFilter: Failed to send simple error: " + e.getMessage());
        }
    }

    /**
     * Return the filter configuration object for this filter.
     */
    public FilterConfig getFilterConfig() {
        return (this.filterConfig);
    }

    /**
     * Set the filter configuration object for this filter.
     *
     * @param filterConfig The filter configuration object
     */
    public void setFilterConfig(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
    }

    /**
     * Destroy method for this filter
     */
    public void destroy() {
    }

    /**
     * Init method for this filter
     */
    public void init(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
        if (filterConfig != null) {
            if (debug) {
                log("DefaultFilter:Initializing filter");
            }
        }
    }

    /**
     * Return a String representation of this object.
     */
    @Override
    public String toString() {
        if (filterConfig == null) {
            return ("DefaultFilter()");
        }
        StringBuffer sb = new StringBuffer("DefaultFilter(");
        sb.append(filterConfig);
        sb.append(")");
        return (sb.toString());
    }
    
    private void sendProcessingError(Throwable t, ServletResponse response) {
        String stackTrace = getStackTrace(t);
        
        if (stackTrace != null && !stackTrace.equals("")) {
            try {
                if (!response.isCommitted()) {
                    response.setContentType("text/html");
                    PrintStream ps = new PrintStream(response.getOutputStream());
                    PrintWriter pw = new PrintWriter(ps);
                    pw.print("<html>\n<head>\n<title>Error</title>\n</head>\n<body>\n");

                    pw.print("<h1>The resource did not process correctly</h1>\n<pre>\n");
                    pw.print(stackTrace);
                    pw.print("</pre></body>\n</html>");
                    pw.close();
                    ps.close();
                    response.getOutputStream().close();
                }
            } catch (Exception ex) {
                log("DefaultFilter: Failed to send processing error: " + ex.getMessage());
            }
        } else {
            try {
                if (!response.isCommitted()) {
                    PrintStream ps = new PrintStream(response.getOutputStream());
                    t.printStackTrace(ps);
                    ps.close();
                    response.getOutputStream().close();
                }
            } catch (Exception ex) {
                log("DefaultFilter: Failed to send stack trace: " + ex.getMessage());
            }
        }
    }
    
    public static String getStackTrace(Throwable t) {
        String stackTrace = null;
        try {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            t.printStackTrace(pw);
            pw.close();
            sw.close();
            stackTrace = sw.getBuffer().toString();
        } catch (Exception ex) {
        }
        return stackTrace;
    }
    
    public void log(String msg) {
        if (filterConfig != null) {
            filterConfig.getServletContext().log(msg);
        }
    }
}
