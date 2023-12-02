package serverlet;
import javax.servlet.*;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * how tomcat work in chapter two
 * @author zhanyang
 */
public class PrimitiveServlet implements Servlet{
    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        System.out.println("init");
    }

    @Override
    public ServletConfig getServletConfig() {
        return null;
    }

    @Override
    public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
        System.out.println("from service");
        PrintWriter out = servletResponse.getWriter();
        out.println("hello. Roses are red");
        out.println("violets are blue");
    }

    @Override
    public String getServletInfo() {
        return null;
    }

    @Override
    public void destroy() {

    }
}
