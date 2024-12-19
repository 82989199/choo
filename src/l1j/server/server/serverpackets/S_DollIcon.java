package l1j.server.server.serverpackets;

import l1j.server.server.Opcodes;
import l1j.server.server.model.Instance.L1ItemInstance;

public class S_DollIcon extends ServerBasePacket {

 private byte[] _byte = null;
 private static final String S_DollIcon = "[S] S_DollIcon";

 //소환할때 패킷
 public S_DollIcon(L1ItemInstance item, int time) {
  writeC(Opcodes.S_EXTENDED_PROTOBUF);
  writeC(0x11);
  writeC(0x09);

  writeC(0x08);
  writeBit(time);

  writeC(0x10);
  writeBit(item.getItem().getItemDescId());

  writeC(0x18);
  writeBit(item.getId());

  writeC(0x22);
  byte[] status = item.getStatusBytes();
  writeBit(status.length);
  writeByte(status);

  writeH(0);
 }

 // 아이콘 해제할때 패킷
 public S_DollIcon() {
  writeC(Opcodes.S_EXTENDED_PROTOBUF);
  writeC(0x11);
  writeC(0x09);

  writeC(0x08);
  writeBit(0);

  writeC(0x10);
  writeBit(0);

  writeC(0x18);
  writeBit(0);

  writeH(0);
 }

 @Override
 public byte[] getContent() {
  if (_byte == null) {
   _byte = getBytes();
  }
  return _byte;
 }

 @Override
 public String getType() {
  return S_DollIcon;
 }
}
