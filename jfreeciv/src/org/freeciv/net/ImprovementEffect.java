package org.freeciv.net;
import org.freeciv.common.EffectType;
import org.freeciv.common.EffectRange;
import org.freeciv.common.UnitClass;

/**
 * This matches the impr_effect struct defined in freeciv/common/improvement.h
 *
 * @author Brian.Duff@dubh.org
 */
public class ImprovementEffect
{
  public EffectType type;
  
  /**
   * One of the EffectRange constants
   */
  public EffectRange range;
  public int amount;
  public int survives;
  public int cond_bldg; // improvement type id
  public int cond_gov;
  public int cond_adv; // tech type id
  public EffectType cond_eff;
  public UnitClass aff_unit; // UCL_LAST = all
  public int aff_terr; // terrain type
  public int aff_spec; // S_* bitmask of specials affected
}
