package edu.sdsu.cs645.server;

import edu.sdsu.cs645.client.WhiteboardService;
import edu.sdsu.cs645.shared.FieldVerifier;
import java.io.*;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server-side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class WhiteboardServiceImpl extends RemoteServiceServlet implements
    WhiteboardService {

    public String validateLogin(String input) {
      if(input.equals("sp2018")){
        return "OK";
      }
      return "INVALID";
    }

    public String save(String users, String contents){
      String path = getServletContext().getRealPath("/");
      String filename = path + "/data.txt";
      File file = new File(filename);
      PrintWriter out;
      try {
         if(file.exists()) {
           out = new PrintWriter(new FileOutputStream(new File(filename),true));
         }else{
           out = new PrintWriter(
          new FileWriter(filename));
         }
         if(contents.isEmpty()){
           return "Nothing";
         } else {
            out.append("<strong>"+users+":</strong>"+contents+"<br/>");
         }
            out.close();
  
      } catch (Exception e) {
        return "ERROR "+ e;
      }
      return "OK";
    }


    public String load(){
      String path = getServletContext().getRealPath("/");
      String filename = path + "/data.txt";
      StringBuffer contents = new StringBuffer();
      String line;
      try {
        BufferedReader in = new BufferedReader(
          new FileReader(filename));
          while((line=in.readLine())!=null){
            contents.append(line);
          }
          in.close();
        
      } catch (Exception e) {
          return "";
      }
      return contents.toString();
    }
}

     