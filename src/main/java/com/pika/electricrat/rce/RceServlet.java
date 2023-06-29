package com.pika.electricrat.rce;

import com.pika.electricrat.web.Annotation.Api;
import com.pika.electricrat.web.Annotation.RequestMethodType;
import com.pika.electricrat.web.servlet.BaseServlet;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import org.junit.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

@WebServlet("/rce/*")
public class RceServlet extends BaseServlet {
    @Api({RequestMethodType.POST})
    public Map<?, ?> cmd(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, InterruptedException {
        String command = request.getParameter("cmd");
        HashMap<String, Object> data = new HashMap<>();
        data.put("data", execCmd(command));
        return data;
    }

    @Api({RequestMethodType.POST})
    public Map<?, ?> ping(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, InterruptedException {
        String ip = request.getParameter("ip");
        HashMap<String, Object> data = new HashMap<>();
        data.put("data", execCmd("ping -c 4 " + ip));
        return data;
    }

    public static String execCmd(String cmd) throws IOException, InterruptedException {
        List<String> bash_cmd1 = new ArrayList<>();
        bash_cmd1.add("/bin/sh");
        bash_cmd1.add("-c");
        bash_cmd1.add(cmd);
        Process p = Runtime.getRuntime().exec(bash_cmd1.toArray(new String[bash_cmd1.size()]));
        InputStream is = p.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
        String line;
        StringBuilder text = new StringBuilder();
        while((line = reader.readLine())!= null){
            text.append(line).append("\n");
        }
        p.waitFor();
        is.close();
        reader.close();
        p.destroy();
        return text.toString();
    }
}
