package org.freeciv.client;

import java.util.HashMap;
import java.util.Set;
import java.util.Iterator;
import java.util.ArrayList;

import org.freeciv.client.Client;
import org.freeciv.client.Constants;


import org.freeciv.net.AbstractPacket;
import org.freeciv.net.PktRulesetControl;
import org.freeciv.net.PktRulesetTech;
import org.freeciv.net.PktRulesetGovernment;
import org.freeciv.net.PktRulesetUnit;
import org.freeciv.net.PktRulesetBuilding;
import org.freeciv.net.PktRulesetTerrainControl;
import org.freeciv.net.PktRulesetTerrain;
import org.freeciv.net.PktRulesetCity;
import org.freeciv.net.PktRulesetNation;

/**
 * This manages rulesets for the Client
 */
public class RulesetManager implements Constants
{
   private static class RulesetType {}

   public static RulesetType
      TYPE_CONTROL      = new RulesetType(),
      TYPE_TECH         = new RulesetType(),
      TYPE_GOVERNMENT   = new RulesetType(),
      TYPE_UNIT         = new RulesetType(),
      TYPE_BUILDING     = new RulesetType(),
      TYPE_TERRAIN_CTRL = new RulesetType(),
      TYPE_TERRAIN      = new RulesetType(),
      TYPE_CITY         = new RulesetType(),
      TYPE_NATION       = new RulesetType();


   private ArrayList m_alListeners;

   private HashMap m_hmTypedListeners;

   public interface RulesetListener
   {
      void rulesetChanged(RulesetEvent rse);
   }

   public static class RulesetEvent
   {
      private AbstractPacket m_pkt;
      private int m_id;
      private RulesetType m_type;

      RulesetEvent(RulesetType type, AbstractPacket pkt, int id)
      {
         m_id = id;
         m_pkt = pkt;
         m_type = type;
      }

      public int getId()
      {
         return m_id;
      }

      public boolean isGlobal()
      {
         return (m_id < 0);
      }

      public AbstractPacket getRuleset()
      {
         return m_pkt;
      }

      public Object getType()
      {
         return m_type;
      }
   }

   private Client m_client;

   private PktRulesetControl m_rulesetControl;

   private PktRulesetTech[] m_rulesetTech;
   private HashMap m_rulesetTechHash;

   private PktRulesetGovernment[] m_rulesetGovernment;
   private HashMap m_rulesetGovernmentHash;

   private PktRulesetUnit[] m_rulesetUnit;
   private HashMap m_rulesetUnitHash;

   private PktRulesetBuilding[] m_rulesetBuilding;
   private HashMap m_rulesetBuildingHash;

   private PktRulesetTerrainControl m_rulesetTerrainControl;

   private PktRulesetTerrain[] m_rulesetTerrain;
   private HashMap m_rulesetTerrainHash;

   private PktRulesetCity[] m_rulesetCity;
   private HashMap m_rulesetCityHash;

   private PktRulesetNation[] m_rulesetNation;
   private HashMap m_rulesetNationHash;

   public RulesetManager(Client c)
   {
      m_client = c;

      m_rulesetTech = new PktRulesetTech[MAX_NUM_ITEMS];
      m_rulesetTechHash = new HashMap();

      m_rulesetGovernment = new PktRulesetGovernment[MAX_NUM_ITEMS];
      m_rulesetGovernmentHash = new HashMap();

      m_rulesetUnit =new PktRulesetUnit[MAX_NUM_ITEMS];
      m_rulesetUnitHash = new HashMap();

      m_rulesetBuilding =new PktRulesetBuilding[MAX_NUM_ITEMS];
      m_rulesetBuildingHash = new HashMap();

      m_rulesetTerrain =new PktRulesetTerrain[MAX_NUM_ITEMS];
      m_rulesetTerrainHash = new HashMap();

      m_rulesetCity =new PktRulesetCity[MAX_NUM_ITEMS];
      m_rulesetCityHash = new HashMap();

      m_rulesetNation =new PktRulesetNation[MAX_NUM_ITEMS];
      m_rulesetNationHash = new HashMap();

   }


   public void addRulesetListener(RulesetListener rl)
   {
      if (m_alListeners == null) m_alListeners = new ArrayList();
      m_alListeners.add(rl);
   }

   public void removeRulesetListener(RulesetListener rl)
   {
      if (m_alListeners != null)
         m_alListeners.remove(rl);

      if (m_hmTypedListeners != null)
      {
         // Check the typed listeners
         Set eKeys = m_hmTypedListeners.keySet();

         Iterator iKeys = eKeys.iterator();

         while(iKeys.hasNext())
         {
            RulesetType type = (RulesetType)iKeys.next();

            // Get the list for this type
            ArrayList al = (ArrayList)m_hmTypedListeners.get(type);
            if (al != null)
            {
               al.remove(rl);
            }
         }
      }
   }

   public void addRulesetListener(RulesetType type, RulesetListener rl)
   {
      if (m_hmTypedListeners == null) m_hmTypedListeners = new HashMap();
      // Get the list for the specified type
      ArrayList l = (ArrayList)m_hmTypedListeners.get(type);
      if (l == null)
      {
         l = new ArrayList();
         m_hmTypedListeners.put(type, l);
      }

      l.add(rl);
   }

   protected void fireRulesetEvent(RulesetType type, AbstractPacket rs)
   {
      fireRulesetEvent(type, rs, -1);
   }

   protected void fireRulesetEvent(RulesetType type, AbstractPacket rs, int id)
   {
      RulesetEvent re = new RulesetEvent(type, rs, id);

      fireToAllListeners(re, m_alListeners);

      if (m_hmTypedListeners != null)
      {
         ArrayList typeLst = (ArrayList)m_hmTypedListeners.get(type);

         fireToAllListeners(re, typeLst);
      }
   }

   private void fireToAllListeners(RulesetEvent e, ArrayList lst)
   {
      if (lst != null)
      {
         Iterator i = lst.iterator();
         while (i.hasNext())
         {
            ((RulesetListener)i.next()).rulesetChanged(e);
         }
      }
   }


// Ruleset control
   public void setRulesetControl(PktRulesetControl pkt)
   {
      m_rulesetControl = pkt;
      fireRulesetEvent(TYPE_CONTROL, pkt);
   }

   public PktRulesetControl getRulesetControl()
   {
      return m_rulesetControl;
   }

// Tech Rulesets
   public void setRulesetTech(int id, PktRulesetTech pkt)
   {
      m_rulesetTech[id] = pkt;
      m_rulesetTechHash.put(pkt.name , pkt);
      fireRulesetEvent(TYPE_TECH, pkt, id);
   }

   public PktRulesetTech getRulesetTech(int id)
   {
      return m_rulesetTech[id];
   }

   public PktRulesetTech getRulesetTech(String name)
   {
      return (PktRulesetTech)m_rulesetTechHash.get(name);
   }



// Government Rulesets
   public void setRulesetGovernment(int id, PktRulesetGovernment pkt)
   {
      m_rulesetGovernment[id] = pkt;
      m_rulesetGovernmentHash.put(pkt.name , pkt);
      fireRulesetEvent(TYPE_GOVERNMENT, pkt, id);
   }

   public PktRulesetGovernment getRulesetGovernment(int id)
   {
      return m_rulesetGovernment[id];
   }

   public PktRulesetGovernment getRulesetGovernment(String name)
   {
      return (PktRulesetGovernment)m_rulesetGovernmentHash.get(name);
   }

// Unit Rulesets
   public void setRulesetUnit(int id, PktRulesetUnit pkt)
   {
      m_rulesetUnit[id] = pkt;
      m_rulesetUnitHash.put(pkt.name , pkt);
      fireRulesetEvent(TYPE_UNIT, pkt, id);
   }

   public PktRulesetUnit getRulesetUnit(int id)
   {
      return m_rulesetUnit[id];
   }

   public PktRulesetUnit getRulesetUnit(String name)
   {
      return (PktRulesetUnit)m_rulesetUnitHash.get(name);
   }

// Building Rulesets
   public void setRulesetBuilding(int id, PktRulesetBuilding pkt)
   {
      m_rulesetBuilding[id] = pkt;
      m_rulesetBuildingHash.put(pkt.name , pkt);
      fireRulesetEvent(TYPE_BUILDING, pkt, id);
   }

   public PktRulesetBuilding getRulesetBuilding(int id)
   {
      return m_rulesetBuilding[id];
   }

   public PktRulesetBuilding getRulesetBuilding(String name)
   {
      return (PktRulesetBuilding)m_rulesetBuildingHash.get(name);
   }

// Terrain Control
   public void setRulesetTerrainControl(PktRulesetTerrainControl rtc)
   {
      m_rulesetTerrainControl = rtc;
      fireRulesetEvent(TYPE_TERRAIN_CTRL, rtc);
   }

   public PktRulesetTerrainControl getRulesetTerrainControl()
   {
      return m_rulesetTerrainControl;
   }

// Terrain Rulesets
   public void setRulesetTerrain(int id, PktRulesetTerrain pkt)
   {
      m_rulesetTerrain[id] = pkt;
      m_rulesetTerrainHash.put(pkt.terrain_name , pkt);
      fireRulesetEvent(TYPE_TERRAIN, pkt, id);
   }

   public PktRulesetTerrain getRulesetTerrain(int id)
   {
      return m_rulesetTerrain[id];
   }

   public PktRulesetTerrain getRulesetTerrain(String name)
   {
      return (PktRulesetTerrain)m_rulesetTerrainHash.get(name);
   }

   public int getRulesetTerrainCount()
   {
      return m_rulesetTerrainHash.size();
   }


// City Rulesets
   public void setRulesetCity(int id, PktRulesetCity pkt)
   {
      m_rulesetCity[id] = pkt;
      m_rulesetCityHash.put(pkt.name , pkt);
      fireRulesetEvent(TYPE_CITY, pkt, id);
   }

   public PktRulesetCity getRulesetCity(int id)
   {
      return m_rulesetCity[id];
   }

   public PktRulesetCity getRulesetCity(String name)
   {
      return (PktRulesetCity)m_rulesetCityHash.get(name);
   }

// Nation Rulesets
   public void setRulesetNation(int id, PktRulesetNation pkt)
   {
      m_rulesetNation[id] = pkt;
      m_rulesetNationHash.put(pkt.name , pkt);
      fireRulesetEvent(TYPE_NATION, pkt, id);
   }

   public PktRulesetNation getRulesetNation(int id)
   {
      return m_rulesetNation[id];
   }

   public PktRulesetNation getRulesetNation(String name)
   {
      return (PktRulesetNation)m_rulesetNationHash.get(name);
   }

}