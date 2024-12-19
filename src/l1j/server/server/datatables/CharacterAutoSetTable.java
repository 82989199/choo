package l1j.server.server.datatables;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import l1j.server.L1DatabaseFactory;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.utils.L1QueryUtil;
import l1j.server.server.utils.SQLUtil;

public class CharacterAutoSetTable
{
  private static Logger _log = Logger.getLogger(CharacterAutoSetTable.class.getName());
  private static CharacterAutoSetTable _instance;
  
  public static CharacterAutoSetTable getInstance()
  {
    if (_instance == null) {
      _instance = new CharacterAutoSetTable();
    }
    return _instance;
  }
  
  public void load(L1PcInstance pc)
  {
    Connection con = null;
    PreparedStatement pstm = null;
    ResultSet rs = null;
    try
    {
      con = L1DatabaseFactory.getInstance().getConnection();
      pstm = con.prepareStatement("SELECT * FROM character_auto_set WHERE objid=?");
      pstm.setInt(1, pc.getId());
      rs = pstm.executeQuery();
      while (rs.next())
      {
        pc.set_자동물약퍼센트(rs.getInt("Auto_Potion_Percent"));
        String potions = rs.getString("Auto_Potion_List");
        if ((potions != null) && (!potions.isEmpty()))
        {
          StringTokenizer list = new StringTokenizer(potions, ",");
          while (list.hasMoreElements())
          {
            String s = list.nextToken();
            pc.get_자동물약리스트().add(Integer.valueOf(s));
          }
        }
        pc.set_자동물약사용(rs.getInt("Auto_Potion_Use") != 0);
        pc.set_자동버프사용(rs.getInt("Auto_Buff_Use") != 0);
        String buffs = rs.getString("Auto_Buff_List");
        if ((buffs != null) && (!buffs.isEmpty()))
        {
          StringTokenizer list = new StringTokenizer(buffs, ",");
          while (list.hasMoreElements())
          {
            String s = list.nextToken();
            pc.get_자동버프리스트().add(Integer.valueOf(s));
          }
        }
        pc.set_자동버프전투시사용(rs.getInt("Auto_Buff_Fight_Use") != 0);
        pc.set_자동버프세이프티존사용(rs.getInt("Auto_Buff_Safety_Use") != 0);
        
      }
    }
    catch (SQLException e)
    {
      _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
      System.out.println("■SQL접속 오류■ : 스크린샷을 찍어 주시길 바랍니다.");
    }
    catch (Exception e1)
    {
      System.out.println("■오류■ : 스크린샷을 찍어 주시길 바랍니다.");
    }
    finally
    {
      SQLUtil.close(rs);
      SQLUtil.close(pstm);
      SQLUtil.close(con);
    }
  }
  
  public void store(L1PcInstance pc)
  {
    Connection con = null;
    try
    {
      con = L1DatabaseFactory.getInstance().getConnection();
      String sql = "INSERT INTO character_auto_set (objid, char_name, Auto_Potion_Percent, Auto_Potion_List, Auto_Potion_Use, Auto_Buff_Use, Auto_Buff_List, Auto_Buff_Fight_Use, Auto_Buff_Safety_Use) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) ON DUPLICATE KEY UPDATE objid=?, char_name=?, Auto_Potion_Percent=?, Auto_Potion_List=?, Auto_Potion_Use=?, Auto_Buff_Use=?, Auto_Buff_List=?, Auto_Buff_Fight_Use=?, Auto_Buff_Safety_Use=?";
      
      ArrayList<Integer> 물약리스트 = pc.get_자동물약리스트();
      String 물약리스트1 = 물약리스트 == null ? null : 물약리스트.toString();
      if (물약리스트1 != null)
      {
    	  물약리스트1 = 물약리스트1.replace("[", "");
    	  물약리스트1 = 물약리스트1.replace("]", "");
    	  물약리스트1 = 물약리스트1.replace(" ", "");
      }
      ArrayList<Integer> 버프리스트 = pc.get_자동버프리스트();
      String 버프리스트1 = 버프리스트 == null ? null : 버프리스트.toString();
      if (버프리스트1 != null)
      {
    	  버프리스트1 = 버프리스트1.replace("[", "");
        버프리스트1 = 버프리스트1.replace("]", "");
        버프리스트1 = 버프리스트1.replace(" ", "");
      }
      L1QueryUtil.execute(con, sql, new Object[] {
    	        Integer.valueOf(pc.getId()), pc.getName(), Integer.valueOf(pc.get_자동물약퍼센트()), 물약리스트1, Integer.valueOf(pc.is_자동물약사용() ? 1 : 0), Integer.valueOf(pc.is_자동버프사용() ? 1 : 0), 버프리스트1, Integer.valueOf(pc.is_자동버프전투시사용() ? 1 : 0), Integer.valueOf(pc.is_자동버프세이프티존사용() ? 1 : 0), 
    	        Integer.valueOf(pc.getId()), pc.getName(), Integer.valueOf(pc.get_자동물약퍼센트()), 물약리스트1, Integer.valueOf(pc.is_자동물약사용() ? 1 : 0), Integer.valueOf(pc.is_자동버프사용() ? 1 : 0), 버프리스트1, Integer.valueOf(pc.is_자동버프전투시사용() ? 1 : 0), Integer.valueOf(pc.is_자동버프세이프티존사용() ? 1 : 0) });
    	    }
    catch (Exception e)
    {
      _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
    }
    finally
    {
      SQLUtil.close(con);
    }
  }
}
