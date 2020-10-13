package chat.client.core;

import chat.client.view.publicChat.PublicChatViewModel;
import chat.client.view.login.LoginViewModel;

public class ViewModelFactory
{
  private ModelFactory mf;
  private LoginViewModel loginVM;
  private PublicChatViewModel chatsVM;

  public ViewModelFactory(ModelFactory mf)
  {
    this.mf = mf;
  }

  public LoginViewModel getLoginVM()
  {
    if (loginVM == null) loginVM = new LoginViewModel(mf.getModel());
    return loginVM;
  }
  public PublicChatViewModel getChatsVM()
  {
    if (chatsVM == null) chatsVM = new PublicChatViewModel(mf.getModel());
    return chatsVM;
  }
}
