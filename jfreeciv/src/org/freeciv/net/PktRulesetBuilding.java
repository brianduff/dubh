package org.freeciv.net;

import org.freeciv.common.EffectType;
import org.freeciv.common.EffectRange;
import org.freeciv.common.UnitClass;
public class PktRulesetBuilding extends AbstractPacket 
{
  public int id; /* index for improvement_types[] */
  public String name;
  public int tech_req;
  public int bldg_req;
  public int[] terr_gate;
  public int[] spec_gate;
  public int equiv_range;
  public int[] equiv_dupl;
  public int[] equiv_repl;
  public int obsolete_by;
  public boolean is_wonder;
  public int build_cost;
  public int upkeep;
  public int sabotage;
  public ImprovementEffect[] effect;
  public int variant;
  public String helptext;
  public PktRulesetBuilding() 
  {
    super();
  }
  public PktRulesetBuilding( InStream in )  throws NetworkProtocolException
  {
    super( in );
  }
  public void receive( InStream in ) throws NetworkProtocolException
  {
    id = in.readUnsignedByte();
    tech_req = in.readUnsignedByte();
    bldg_req = in.readUnsignedByte();
    terr_gate = in.readUnsignedByteVector();
    spec_gate = in.readShortVector();
    equiv_range = in.readUnsignedByte();
    equiv_dupl = in.readUnsignedByteVector();
    equiv_repl = in.readUnsignedByteVector();
    obsolete_by = in.readUnsignedByte();
    is_wonder = ( in.readUnsignedByte() != 0 );
    build_cost = in.readShort();
    upkeep = in.readUnsignedByte();
    sabotage = in.readUnsignedByte();
    int count = in.readUnsignedByte();
    effect = new ImprovementEffect[ count ];
    for( int i = 0;i < count;i++ )
    {
      ImprovementEffect eff = new ImprovementEffect();
      effect[ i ] = eff;
      eff.type = EffectType.fromInt( in.readUnsignedByte() );
      eff.range = EffectRange.fromInt( in.readUnsignedByte() );
      eff.amount = in.readShort();
      eff.survives = in.readUnsignedByte();
      eff.cond_bldg = in.readUnsignedByte();
      eff.cond_gov = in.readUnsignedByte();
      eff.cond_adv = in.readUnsignedByte();
      eff.cond_eff = EffectType.fromInt( in.readUnsignedByte() );
      eff.aff_unit = UnitClass.fromInt( in.readUnsignedByte() );
      eff.aff_terr = in.readUnsignedByte();
      eff.aff_spec = in.readShort();
    }
    variant = in.readUnsignedByte();
    name = in.readZeroString();
    if( in.packIterRemaining() > 0 )
    {
      helptext = in.readZeroString();
    }
  }
  public void send( OutStream out )
               throws java.io.IOException
  {
    
  }
  public String toString()
  {
    return name;
  }
  String helpText;
  public void setHelpText( String txt )
  {
    helpText = txt;
  }


}
