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

public class CharacterAutoResolTable
{
  private static Logger _log = Logger.getLogger(CharacterAutoResolTable.class.getName());
  private static CharacterAutoResolTable _instance;
  
  public static CharacterAutoResolTable getInstance()
  {
    if (_instance == null) {
      _instance = new CharacterAutoResolTable();
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
      pstm = con.prepareStatement("SELECT * FROM character_auto_resol WHERE objid=?");
      pstm.setInt(1, pc.getId());
      rs = pstm.executeQuery();
      while (rs.next())
      {
        String resols = rs.getString("Auto_Resol_List");
        if ((resols != null) && (!resols.isEmpty()))
        {
          StringTokenizer list = new StringTokenizer(resols, ",");
          while (list.hasMoreElements())
          {
            String s = list.nextToken();
            pc.get_자동용해리스트().add(Integer.valueOf(s));
          }
        }
      }
    }
    catch (SQLException e)
    {
      _log.log(Level.SEVERE, e.getLocalizedMessage(), e);
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
      String sql = "INSERT INTO character_auto_resol (objid, char_name, Auto_Resol_List) VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE objid=?, char_name=?,Auto_Resol_List=?";
      
      ArrayList<Integer> 용해리스트 = pc.get_자동용해리스트();
      String 용해리스트1 = 용해리스트 == null ? null : 용해리스트.toString();
      if (용해리스트1 != null)
      {
    	  용해리스트1 = 용해리스트1.replace("[", "");
        용해리스트1 = 용해리스트1.replace("]", "");
        용해리스트1 = 용해리스트1.replace(" ", "");
      }
      L1QueryUtil.execute(con, sql, new Object[] {
        Integer.valueOf(pc.getId()), pc.getName(), 용해리스트1, 
        Integer.valueOf(pc.getId()), pc.getName(), 용해리스트1 });
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
