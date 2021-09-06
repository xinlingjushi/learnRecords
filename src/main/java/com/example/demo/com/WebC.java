package com.example.demo.com;

import jdk.internal.instrumentation.TypeMapping;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletRequest;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;

@Controller
public class WebC {

    public  String body(HttpServletRequest hsr) throws IOException {
            String contentLength = hsr.getHeader("Content-Length");
            byte[] buffer= new byte[Integer.valueOf(contentLength)];
            DataInputStream dis = new DataInputStream(hsr.getInputStream());
             InputStream in = hsr.getInputStream();
            dis.readFully(buffer,0,buffer.length);
            return  new String(buffer,"UTF-8");
    }

    public final int readFully(InputStream in, byte b[], int off, int len) throws IOException {
        if (len < 0)
            throw new IndexOutOfBoundsException();
        int n = 0;
        while (n < len) {
            int count = in.read(b, off + n, len - n);
            if (count < 0)
                break;
            n += count;
        }
        return n;
    }



    public static void main(String argv[]){

//道可道也，非恒道也；名可名也，非恒名也。
    }
}
