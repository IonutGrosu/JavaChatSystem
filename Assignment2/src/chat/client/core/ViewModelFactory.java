package chat.client.core;

import chat.client.view.chats.ChatsViewModel;
import chat.client.view.login.LoginViewModel;

public class ViewModelFactory
{
  private ModelFactory mf;
  private LoginViewModel loginVM;
  private ChatsViewModel chatsVM;

  public ViewModelFactory(ModelFactory mf)
  {
    this.mf = mf;
  }

  public LoginViewModel getLoginVM()
  {
    if (loginVM == null) loginVM = new LoginViewModel(mf.getModel());
    return loginVM;
  }
  public ChatsViewModel getChatsVM()
  {
    if (chatsVM == null) chatsVM = new ChatsViewModel(mf.getModel());
    return chatsVM;
  }
}
