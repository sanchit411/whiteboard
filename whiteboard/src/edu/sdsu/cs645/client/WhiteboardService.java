package edu.sdsu.cs645.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

/**
 * The client-side stub for the RPC service.
 */
@RemoteServiceRelativePath("whiteboard")
public interface WhiteboardService extends RemoteService {
  String validateLogin(String name) throws IllegalArgumentException;

  String save(String users,String name) throws IllegalArgumentException;
  String load() throws IllegalArgumentException;
}
 