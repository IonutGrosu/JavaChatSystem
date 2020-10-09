package chat.client.core;

import chat.client.model.Model;
import chat.client.model.ModelInterface;

public class ModelFactory
{
  private ClientFactory cf;
  ModelInterface model;

  public ModelFactory(ClientFactory cf)
  {
    this.cf = cf;
  }

  public ModelInterface getModel()
  {
    if (model == null) model = new Model(cf.getClient());
    return model;
  }
}
