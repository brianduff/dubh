package org.freeciv.common;

/**
 * This object is the "heart" of data storage in freeciv. In the c client, 
 * there are a large number of "globals", mostly initialized from ruleset
 * packets. In JFreeciv, we handle all these objects via factories. There 
 * are a number of factories in the game: the Factories object holds references
 * to all the factories. Usually the Client (or potentially the Server) will
 * instantiate a Factories object on startup and retrieve most of its data
 * through the relevant factories exposed in this class.
 *
 * @author Brian.Duff@dubh.org
 */
public final class Factories 
{
  private GameObjectFactory m_connectionFactory = 
    new AbstractGameObjectFactory(this)
    {
      protected GameObject doCreate()
      {
        return new Connection(this);
      } 
    };
  private GameObjectFactory m_advanceFactory =
    new AbstractGameObjectFactory(this)
    {
      protected GameObject doCreate()
      {
        return new Advance(this);
      } 
    };  
  private GameObjectFactory m_playerFactory =
    new AbstractGameObjectFactory(this)
    {
      protected GameObject doCreate()
      {
        return new Player(this);
      } 
    };
  private GameObjectFactory m_governmentFactory =
    new AbstractGameObjectFactory(this)
    {
      protected GameObject doCreate()
      {
        return new Government(this);
      } 
    };
  private GameObjectFactory m_unitTypeFactory =
    new AbstractGameObjectFactory(this)
    {
      protected GameObject doCreate()
      {
        return new UnitType(this);
      } 
    };
  private GameObjectFactory m_buildingFactory =
    new AbstractGameObjectFactory(this)
    {
      protected GameObject doCreate()
      {
        return new Building(this);
      } 
    };
  private GameObjectFactory m_terrainTypeFactory = 
    new AbstractGameObjectFactory(this)
    {
      protected GameObject doCreate()
      {
        return new TerrainType(this);
      } 
    };
  private GameObjectFactory m_nationFactory = 
    new AbstractGameObjectFactory(this)
    {
      protected GameObject doCreate()
      {
        return new Nation(this);
      } 
    };   

  private GameObjectFactory m_cityStyleFactory = 
    new AbstractGameObjectFactory(this)
    {
      protected GameObject doCreate()
      {
        return new CityStyle(this);
      } 
    };         
  /**
   * Construct a factories object
   */
  public Factories()
  {
  }

  public GameObjectFactory getConnectionFactory()
  {
    return m_connectionFactory;
  }

  public GameObjectFactory getAdvanceFactory()
  {
    return m_advanceFactory;
  }

  public GameObjectFactory getPlayerFactory()
  {
    return m_playerFactory;
  }

  public GameObjectFactory getGovernmentFactory()
  {
    return m_governmentFactory;
  }

  public GameObjectFactory getUnitTypeFactory()
  {
    return m_unitTypeFactory;
  }

  public GameObjectFactory getBuildingFactory()
  {
    return m_buildingFactory;
  }

  public GameObjectFactory getTerrainTypeFactory()
  {
    return m_terrainTypeFactory;
  }

  public GameObjectFactory getNationFactory()
  {
    return m_nationFactory;
  }

  public GameObjectFactory getCityStyleFactory()
  {
    return m_cityStyleFactory;
  }
}