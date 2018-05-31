package edu.sdsu.cs645.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>WhiteboardService</code>.
 */
public interface WhiteboardServiceAsync {
  // void whiteboardServer(String input, AsyncCallback<String> callback)
  void validateLogin(String input, AsyncCallback<String> callback)
      throws IllegalArgumentException;

  void save(String users, String input, AsyncCallback<String> callback)
      throws IllegalArgumentException;
  void load(AsyncCallback<String> callback)
      throws IllegalArgumentException;
}
