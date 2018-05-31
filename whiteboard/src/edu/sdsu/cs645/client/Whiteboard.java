package edu.sdsu.cs645.client;

import edu.sdsu.cs645.shared.FieldVerifier;
import com.google.gwt.core.client.*;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Whiteboard implements EntryPoint {
    /**
    * The message displayed to the user when the server cannot be reached or
    * returns an error.
    */
    private static final String SERVER_ERROR = "An error occurred while "
        + "attempting to contact the server. Please check your network "
        + "connection and try again.";

    /**
    * Create a remote service proxy to talk to the server-side Whiteboard service.
    */
    private final WhiteboardServiceAsync whiteboardService = GWT.create(WhiteboardService.class);
    private HTML status;
    private RichTextArea board;
    private RichTextArea history;
    private TextBox user;
    
    public void onModuleLoad() {
        status = new HTML();
         status.getElement().setId("status_msg");
        buildLogin();
    }

    private void buildLogin(){
        FlowPanel loginPanel = new FlowPanel();
        // loginPanel.getElement().setId("log_panel"); class
        loginPanel.getElement().setId("log_panel"); // css -id
        user = new TextBox();
        final PasswordTextBox password = new PasswordTextBox();
        loginPanel.add(new HTML("<h1>LogIn </h1>"));
        loginPanel.add(new Label("Username"));
        loginPanel.add(user);
        loginPanel.add(new Label("Password"));
        loginPanel.add(password);
        FlowPanel buttonPanel = new FlowPanel();
        buttonPanel.setStyleName("btn-login-panel");
        Button loginButton = new Button("Login");
        Button clearButton = new Button("Clear");
        loginButton.setStyleName("log-btn");
        clearButton.setStyleName("clr-btn");
        
        clearButton.addClickHandler(new ClickHandler(){
            public void onClick(ClickEvent e){
                status.setText("");
                password.setText("");
                user.setText("");
                user.setFocus(true);
            }
        });

        loginButton.addClickHandler(new ClickHandler(){
            public void onClick(ClickEvent e){
                validateLogin(password.getText());
                password.setFocus(true);
            }
        });

        buttonPanel.add(clearButton);
        buttonPanel.add(loginButton);
        buttonPanel.add(new HTML("<br/><h3>Code by Sanchit Arora , Spring 2018. jadrn057</h3>"));
        loginPanel.add(buttonPanel);
        loginPanel.add(status);
        RootPanel.get().add(loginPanel);
    }
    
    private void validateLogin(String login){
      if(user.getText().isEmpty()){
        status.setStyleName("error");
        status.setText("Kindly enter username");
        user.setFocus(true);
      } else {
        AsyncCallback callback = new AsyncCallback(){
          public void onSuccess(Object results){
            String answer = (String) results;
            if(answer.equals("OK")){
              status.setText("");
              buildMainPanel();
            }
            else{
              status.setStyleName("error");
              status.setText("Error, Invalid password!");
            }
          }
          public void onFailure(Throwable err){
          status.setStyleName("error");
          status.setText("Failed"+err.getMessage());
          err.printStackTrace();
          }
        };
        whiteboardService.validateLogin(login, callback);
      }
    }

  private void buildMainPanel(){
    FlowPanel main = new FlowPanel();
    main.add(new HTML("<h1>Online Whiteboard</h1>"));
    main.add(new HTML("<h3>*Load button gives the username with their input</h3>"));
    main.getElement().setId("white_panel");
    main.add(getButtonPanel());
    board = new RichTextArea();
    history = new RichTextArea();
    history.getElement().setId("hist");
    loadPanel();
    main.add(history);
    history.setEnabled(false);
    main.add(board);
    main.add(new HTML("<br/><h3>Code by Sanchit Arora , Spring 2018. jadrn057</h3>"));
    main.add(status);
    RootPanel.get().clear();
    RootPanel.get().add(main);
    board.setFocus(true);

  }

  private FlowPanel getButtonPanel(){
    FlowPanel p = new FlowPanel();
    Button clr = new Button("Clear");
    Button save = new Button("Save");
    Button load = new Button("Load");
    clr.setStyleName("btn clr");
    save.setStyleName("btn save");
    load.setStyleName("btn load");
    clr.addClickHandler(new ClickHandler(){
      public void onClick(ClickEvent e){
        board.setHTML("");
        status.setHTML("");
        history.setVisible(false);
      }
    });
    save.addClickHandler(new ClickHandler(){
      public void onClick(ClickEvent e){
      savePanel();
      }
    });
    load.addClickHandler(new ClickHandler(){
      public void onClick(ClickEvent e){
        loadPanel();
      }
    });

    p.setStyleName("button-panel");
    p.add(clr);
    p.add(save);
    p.add(load);
    return p;
  }

  private void savePanel(){
      AsyncCallback callback = new AsyncCallback(){
        public void onSuccess(Object results){
          String answer = (String) results;
          if(answer.equals("OK")){
            status.setStyleName("success");
            status.setText("Whiteboard saved!");
          }
          else{
            status.setStyleName("error");
            status.setText("Error, could not save contents!");
          }
        }
          public void onFailure(Throwable err){
            status.setStyleName("error");
            status.setText("Failed"+err.getMessage());
            err.printStackTrace();
          }
        };
        whiteboardService.save(user.getText(), board.getHTML(), callback);
      board.setHTML("");
    }

    private void loadPanel(){
      AsyncCallback callback = new AsyncCallback(){
        public void onSuccess(Object results){
          String answer = (String) results;
          if (answer == ""){
              history.setHTML("");
              history.setVisible(false);
              status.setStyleName("");
             status.setText("");
          }else {
            history.setHTML(answer);
            history.setVisible(true);
            status.setStyleName("success");
            status.setText("Whiteboard loaded from disk!");
          }
        }
          public void onFailure(Throwable err){
            status.setStyleName("error");
            status.setText("Failed"+err.getMessage());
            err.printStackTrace();
          }
        };
        whiteboardService.load(callback);
      }
}


