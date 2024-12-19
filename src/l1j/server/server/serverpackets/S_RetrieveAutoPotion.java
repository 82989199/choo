package l1j.server.server.serverpackets;

import java.io.IOException;
import java.util.ArrayList;
import l1j.server.server.model.Instance.L1ItemInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.templates.L1Item;
import l1j.server.server.utils.BinaryOutputStream;

public class S_RetrieveAutoPotion
  extends ServerBasePacket
{

  public boolean NonValue = false;
  
  @SuppressWarnings("resource")
public S_RetrieveAutoPotion(L1PcInstance pc)
  {
    pc._자동물약초이스리스트 = new ArrayList<L1ItemInstance>();
    for (L1ItemInstance item : pc.getInventory().getItems()) {
      if ((item.getItemId() == 40010) || (item.getItemId() == 40011) || (item.getItemId() == 40012) || (item.getItemId() == 40019)
    		  || (item.getItemId() == 40020) || (item.getItemId() == 40021) || (item.getItemId() == 40022) || (item.getItemId() == 40023)
    		  || (item.getItemId() == 40024) || (item.getItemId() == 140010) || (item.getItemId() == 140011)
    		  || (item.getItemId() == 140012) || (item.getItemId() == 240010) || (item.getItemId() == 4100152) || (item.getItemId() == 4100153)
    		  || (item.getItemId() == 4100154)) {
        if (item.getItem().getType2() == 0) {
          if ((pc.getType() == 0) || (pc.getType() == 1) || (pc.getType() == 7))
          {
            if ((item.getItemId() == 40010) || (item.getItemId() == 40019) || (item.getItemId() == 40029) || (item.getItemId() == 40022) || 
              (item.getItemId() == 6342) || (item.getItemId() == 240010) || (item.getItemId() == 140010) || 
              (item.getItemId() == 40011) || (item.getItemId() == 400011) || (item.getItemId() == 40020) || 
              (item.getItemId() == 40023) || (item.getItemId() == 140011) || (item.getItemId() == 6343) || 
              (item.getItemId() == 40012) || (item.getItemId() == 40021) || (item.getItemId() == 40024) || (item.getItemId() == 400024)|| 
              (item.getItemId() == 7005001) || (item.getItemId() == 140012) || 
              (item.getItemId() == 40026) || (item.getItemId() == 40027) || (item.getItemId() == 40028) || 
              (item.getItemId() == 40013) || (item.getItemId() == 40018)|| (item.getItemId() == 440018) || (item.getItemId() == 40030) || 
              (item.getItemId() == 140013) || (item.getItemId() == 140018) || (item.getItemId() == 3000162) || 
              (item.getItemId() == 30073) || (item.getItemId() == 40014) || (item.getItemId() == 41415) || (item.getItemId() == 441415)|| (item.getItemId() == 140014) || 
              (item.getItemId() == 40015) || (item.getItemId() == 140015) || (item.getItemId() == 210114)  || (item.getItemId() == 2210114)|| (item.getItemId() == 41142) || 
              (item.getItemId() == 40017) || (item.getItemId() == 40507) || 
              (item.getItemId() == 40317) || 
              (item.getItemId() == 410063) || (item.getItemId() == 410137) || (item.getItemId() == 4100152) || (item.getItemId() == 4100153)
    		  || (item.getItemId() == 4100154)) {
              pc._자동물약초이스리스트.add(item);
            }
          }
          else if (pc.getType() == 2)
          {
            if ((item.getItemId() == 40010) || (item.getItemId() == 40019) || (item.getItemId() == 40029) || (item.getItemId() == 40022) || 
              (item.getItemId() == 6342) || (item.getItemId() == 240010) || (item.getItemId() == 140010) || 
              (item.getItemId() == 40011) || (item.getItemId() == 400011) || (item.getItemId() == 40020) || 
              (item.getItemId() == 40023) || (item.getItemId() == 140011) || (item.getItemId() == 6343) || 
              (item.getItemId() == 40012) || (item.getItemId() == 40021) || (item.getItemId() == 40024) || (item.getItemId() == 400024) || 
              (item.getItemId() == 7005001) || (item.getItemId() == 140012) || 
              (item.getItemId() == 40026) || (item.getItemId() == 40027) || (item.getItemId() == 40028) || 
              (item.getItemId() == 40013) || (item.getItemId() == 40018) || (item.getItemId() == 440018)|| (item.getItemId() == 40030) || 
              (item.getItemId() == 140013) || (item.getItemId() == 140018) || (item.getItemId() == 3000162) || 
              (item.getItemId() == 30076) || (item.getItemId() == 40068) || (item.getItemId() == 140068) || (item.getItemId() == 210110) || (item.getItemId() == 2210110)|| 
              (item.getItemId() == 40015) || (item.getItemId() == 140015) || (item.getItemId() == 210114) || (item.getItemId() == 2210114) || (item.getItemId() == 41142) || 
              (item.getItemId() == 40017) || (item.getItemId() == 40507) || 
              (item.getItemId() == 40317) || 
              (item.getItemId() == 410063) || (item.getItemId() == 410137) || (item.getItemId() == 4100152) || (item.getItemId() == 4100153)
    		  || (item.getItemId() == 4100154)) {
              pc._자동물약초이스리스트.add(item);
            }
          }
          else if (pc.getType() == 3)
          {
            if ((item.getItemId() == 40010) || (item.getItemId() == 40019) || (item.getItemId() == 40029) || (item.getItemId() == 40022) || 
              (item.getItemId() == 6342) || (item.getItemId() == 240010) || (item.getItemId() == 140010) || 
              (item.getItemId() == 40011) || (item.getItemId() == 400011) || (item.getItemId() == 40020) || 
              (item.getItemId() == 40023) || (item.getItemId() == 140011) || (item.getItemId() == 6343) || 
              (item.getItemId() == 40012) || (item.getItemId() == 40021) || (item.getItemId() == 40024) || (item.getItemId() == 400024) || 
              (item.getItemId() == 7005001) || (item.getItemId() == 140012) || 
              (item.getItemId() == 40026) || (item.getItemId() == 40027) || (item.getItemId() == 40028) || 
              (item.getItemId() == 40013) || (item.getItemId() == 40018)|| (item.getItemId() == 440018) || (item.getItemId() == 40030) || 
              (item.getItemId() == 140013) || (item.getItemId() == 140018) || (item.getItemId() == 3000162) || 
              (item.getItemId() == 40015) || (item.getItemId() == 140015) || (item.getItemId() == 210114)|| (item.getItemId() == 2210114) ||  (item.getItemId() == 41142) || 
              (item.getItemId() == 40017) || (item.getItemId() == 40507) || 
              (item.getItemId() == 40317) || 
              (item.getItemId() == 410063) || (item.getItemId() == 410137) || 
              (item.getItemId() == 30089) || (item.getItemId() == 40016) || (item.getItemId() == 140016) || (item.getItemId() == 210113) || (item.getItemId() == 2210113)
              || (item.getItemId() == 4100152) || (item.getItemId() == 4100153)
    		  || (item.getItemId() == 4100154)) {
              pc._자동물약초이스리스트.add(item);
            }
          }
          else if ((pc.getType() == 4) || (pc.getType() == 5))
          {
            if ((item.getItemId() == 40010) || (item.getItemId() == 40019) || (item.getItemId() == 40029) || (item.getItemId() == 40022) || 
              (item.getItemId() == 6342) || (item.getItemId() == 240010) || (item.getItemId() == 140010) || 
              (item.getItemId() == 40011) || (item.getItemId() == 400011) || (item.getItemId() == 40020) || 
              (item.getItemId() == 40023) || (item.getItemId() == 140011) || (item.getItemId() == 6343) || 
              (item.getItemId() == 40012) || (item.getItemId() == 40021) || (item.getItemId() == 40024) || (item.getItemId() == 400024) || 
              (item.getItemId() == 7005001) || (item.getItemId() == 140012) || 
              (item.getItemId() == 40026) || (item.getItemId() == 40027) || (item.getItemId() == 40028) || 
              (item.getItemId() == 40013) || (item.getItemId() == 40018) || (item.getItemId() == 440018)|| (item.getItemId() == 40030) || 
              (item.getItemId() == 140013) || (item.getItemId() == 140018) || (item.getItemId() == 3000162) || 
              (item.getItemId() == 40015) || (item.getItemId() == 140015) || (item.getItemId() == 210114) || (item.getItemId() == 2210114) || (item.getItemId() == 2210114) || (item.getItemId() == 41142) || 
              (item.getItemId() == 40017) || (item.getItemId() == 40507) || 
              (item.getItemId() == 40317) || 
              (item.getItemId() == 410063) || (item.getItemId() == 410137) || (item.getItemId() == 4100152) || (item.getItemId() == 4100153)
    		  || (item.getItemId() == 4100154)) {
              pc._자동물약초이스리스트.add(item);
            }
          }
          else if ((pc.getType() == 6) && (
            (item.getItemId() == 40010) || (item.getItemId() == 40019) || (item.getItemId() == 40029) || (item.getItemId() == 40022) || 
            (item.getItemId() == 6342) || (item.getItemId() == 240010) || (item.getItemId() == 140010) || 
            (item.getItemId() == 40011) || (item.getItemId() == 400011) || (item.getItemId() == 40020) || 
            (item.getItemId() == 40023) || (item.getItemId() == 140011) || (item.getItemId() == 6343) || 
            (item.getItemId() == 40012) || (item.getItemId() == 40021) || (item.getItemId() == 40024)  || (item.getItemId() == 400024)|| 
            (item.getItemId() == 7005001) || (item.getItemId() == 140012) || 
            (item.getItemId() == 40026) || (item.getItemId() == 40027) || (item.getItemId() == 40028) || 
            (item.getItemId() == 40013) || (item.getItemId() == 40018) || (item.getItemId() == 440018)|| (item.getItemId() == 40030) || 
            (item.getItemId() == 140013) || (item.getItemId() == 140018) || (item.getItemId() == 3000162) || 
            (item.getItemId() == 30077) || (item.getItemId() == 210036) ||  (item.getItemId() == 713) || (item.getItemId() == 7713) ||
            (item.getItemId() == 40015) || (item.getItemId() == 140015) || (item.getItemId() == 210114) || (item.getItemId() == 2210114) || (item.getItemId() == 41142) || 
            (item.getItemId() == 40017) || (item.getItemId() == 40507) || 
            (item.getItemId() == 40317) || 
            (item.getItemId() == 410063) || (item.getItemId() == 410137) || 
            (item.getItemId() == 30089) || (item.getItemId() == 40016) || (item.getItemId() == 140016) || (item.getItemId() == 210113) || (item.getItemId() == 2210113))
        		  || (item.getItemId() == 4100152) || (item.getItemId() == 4100153)
        		  || (item.getItemId() == 4100154)) {
            pc._자동물약초이스리스트.add(item);
          }
        }
      }
    }
//    if (pc._자동물약초이스리스트.size() > 0) // 물약이 인벤에 없을때 자동 물약 설정 들어가면 패킷 없어서 튕김.
    if (pc._자동물약초이스리스트.size() >= 0) // 이렇게 바꿔주면 해결된다.
    {
      writeC(180);
      writeC(8);
      writeC(4);
      writeC(8);
      writeBit(-1L);
      writeC(16);
      writeBit(pc._자동물약초이스리스트.size());
      writeC(24);
      writeC(5);
      writeC(32);
      writeBit(100L);
      byte[] status = null;
      int index = 0;
      for (L1ItemInstance item : pc._자동물약초이스리스트)
      {
        BinaryOutputStream detail = new BinaryOutputStream();
        L1Item temp = item.getItem();
        detail.writeC(8);
        detail.writeBit(item.getId());
        detail.writeC(16);
        detail.writeBit(temp.getItemDescId() == 0 ? -1L : temp.getItemDescId());
        detail.writeC(32);
        detail.writeBit(item.getCount());
        if (temp.getUseType() > 0)
        {
          detail.writeC(40);
          detail.writeBit(temp.getUseType());
        }
        detail.writeC(56);
        detail.writeBit(item.get_gfxid());
        detail.writeC(64);
        detail.writeC(item.getBless());
        detail.writeC(72);
        detail.writeBit(item.getStatusBit());
        detail.writeC(88);
        detail.writeBit(0L);
        detail.writeC(104);
        detail.writeBit(item.getEnchantLevel());
        detail.writeC(112);
        detail.writeBit(item.getTradeBit());
        int attrlevel;
        int attrtype;
        if ((temp.getType2() == 1) && (item.getAttrEnchantLevel() > 0))
        {
          attrlevel = item.getAttrEnchantLevel();
          attrtype = 0;
          if ((attrlevel >= 1) && (attrlevel <= 5)) {
            attrtype = 1;
          } else if ((attrlevel >= 6) && (attrlevel <= 10)) {
            attrtype = 2;
          } else if ((attrlevel >= 11) && (attrlevel <= 15)) {
            attrtype = 3;
          } else if ((attrlevel >= 16) && (attrlevel <= 20)) {
            attrtype = 4;
          }
          detail.writeBit(128L);
          detail.writeBit(attrtype);
          detail.writeBit(136L);
          detail.writeBit(attrtype == 1 ? attrlevel : attrlevel - 5 * (attrtype - 1));
        }
        detail.writeBit(146L);
        detail.writeS3(item.getViewName());
        if (item.isIdentified())
        {
          detail.writeBit(154L);
          status = item.getStatusBytes();
          detail.writeBit(status.length);
          for (byte b : status) {
            detail.writeC(b);
          }
        }
        int bsize = getBitSize(detail.getLength() - 2) + getBitSize(index) + detail.getBytes().length + 2;
        
        writeC(50);
        writeBit(bsize);
        writeC(8);
        writeBit(index++);
        writeC(18);
        writeBit(detail.getLength() - 2);
        writeByte(detail.getBytes());
      }
      writeC(56);
      writeC(1);
      writeH(0);
    }
    else
    {
      pc.sendPackets(new S_SystemMessage("선택 가능한 물약이 없습니다."));
      return;
    }
  }
  
  public byte[] getContent()
    throws IOException
  {
    return getBytes();
  }
  
  public String getType()
  {
    return "[S] S_RetrievePledgeList";
  }
}

