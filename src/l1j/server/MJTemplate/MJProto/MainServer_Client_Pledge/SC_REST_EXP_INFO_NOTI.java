package l1j.server.MJTemplate.MJProto.MainServer_Client_Pledge;

import l1j.server.MJTemplate.MJProto.WireFormat;
import l1j.server.server.datatables.ClanBuffTable;
import l1j.server.server.datatables.IncreaseEinhasadMap;
import l1j.server.server.model.L1Clan;
import l1j.server.server.model.Instance.L1DollInstance;
import l1j.server.server.model.Instance.L1PcInstance;
import l1j.server.server.model.skill.L1SkillId;

import java.io.IOException;

import l1j.server.MJTemplate.MJProto.MJEProtoMessages;
import l1j.server.MJTemplate.MJProto.MJIProtoMessage;

// TODO : 자동으로 생성된 PROTO 코드입니다. made by MJSoft.
public class SC_REST_EXP_INFO_NOTI implements l1j.server.MJTemplate.MJProto.MJIProtoMessage {
	private static final int[] LEVEL_TO_BONUS_EFFECT = new int[] { 5, // 80
			6, // 81
			7, // 82
			8, // 83
			9, // 84
			10, // 85
			12, // 86
			14, // 87
			16, // 88
			18, // 89
			20, // 90
			23, // 91
			26, // 92
			29, // 93
			32, // 94
			35, // 95
			35, // 96
			35, // 97
			35, // 98
			35, // 99
			35, // 100
			35, // 101
			35, // 102
			35, // 103
			35, // 104
			35, // 105
			35, // 106
			35, // 107
	};

	public static double calcDecreaseCharacterEinhasad(L1PcInstance pc, double descrease) {
		double dec = descrease;
		double effect = calcEinhasadEffectToDouble(pc);
		if (effect > 0D) {
			dec -= (dec * effect);
		}
		
		dec = IncreaseEinhasadMap.getInstance().increaseEinhasadValue(pc.getMapId(), dec);
		
		if (pc.getAccount().getBlessOfAin() >= 2000001 && pc.getAccount().getBlessOfAin() <= 18000000) {
			dec *= 0.6;
		} else if (pc.getAccount().getBlessOfAin() >= 1800001 && pc.getAccount().getBlessOfAin() >= 34000000) {
			dec *= 0.8;
		}
		
		// System.out.println(effect);
		// System.out.println(String.format("src : %d / dst : %d / effect : %d",
		// (int)descrease, (int)dec, (int)(effect * 100)));
		return dec;
	}

	public static double calcEinhasadEffectToDouble(L1PcInstance pc) {
		double effect = calcEinhasadEffect(pc);
		return effect <= 0 ? effect : effect * 0.01D;
	}

	public static int calcEinhasadEffect(L1PcInstance pc) {
		int effect = 0;
		int level = pc.getLevel();
		if (level >= 80) {
			int idx = level - 80;
			effect = LEVEL_TO_BONUS_EFFECT[idx];
		}

		/** 2017-11-06 아인하사드 축복 감소 확률 리뉴얼 **/
		if (pc.getEinhasadBlessper() >= 0) {
			effect += pc.getEinhasadBlessper();
		}
		/** 2017-11-06 아인하사드 축복 감소 확률 리뉴얼 **/

		Object[] dollList1 = pc.getDollList().values().toArray();
		for (Object dollObject : dollList1) {
			L1DollInstance doll = (L1DollInstance) dollObject;
			effect += doll.getEinhasadEffect();
		}

		
		
		return effect;
	}
	
	public static void send(L1PcInstance pc) {
		if (pc == null || pc.getAccount() == null || pc.noPlayerCK)
			return;

		if (pc.getAccount().getGrangKinAngerTime() != 0) {
			return;
		}
		
		int hasad = pc.getAccount().getBlessOfAin();
		if (hasad > 0)
			hasad /= 10000;

		int ration = 0;
		if (pc.hasSkillEffect(L1SkillId.DRAGON_TOPAZ))
			ration += 81;
		if (pc.PC방_버프)
			ration += 40;

		int ein_level = get_ein_level(hasad);
		int extra = 0;
		if(ein_level == 4) {
			extra = 60;
			ration += 100;
		} else if(ein_level == 3) {
			extra = 50;
			ration += 80;
		} else if(ein_level == 2) {
			extra = 40;
			ration += 60;
		} else if (ein_level == 1) {
			ration += 100;
		}

		SC_REST_EXP_INFO_NOTI noti = newInstance();
		noti.set_rest_gauge(hasad);
		noti.set_default_ration(ration * 100);
		noti.set_reduce_efficiency(calcEinhasadEffect(pc));
		noti.set_extra_exp_ratio(extra);
		
		L1Clan c = pc.getClan();
		noti.set_bless_of_blood_pledge(pc.getClanBuffMap() != 0 && c != null && c.getEinhasadBlessBuff() != 0);
		pc.sendPackets(noti, MJEProtoMessages.SC_REST_EXP_INFO_NOTI, true);
	}

	public static SC_REST_EXP_INFO_NOTI newInstance() {
		return new SC_REST_EXP_INFO_NOTI();
	}
	
	public static int get_ein_level(int ein) {
		if(ein >= 3401)
			return 4;
		else if(ein >= 1801)
			return 3;
		else if(ein >= 201)
			return 2;
		else if(ein > 0)
			return 1;
		return 0;
	}

	private int _rest_gauge;
	private int _default_ration;
	private int _extra_exp_ratio;
	private int _reduce_efficiency;
	private boolean _bless_of_blood_pledge;
	private int _memorizedSerializedSize = -1;
	private byte _memorizedIsInitialized = -1;
	private int _bit;

	private SC_REST_EXP_INFO_NOTI() {
		set_bless_of_blood_pledge(false);
	}

	public int get_rest_gauge() {
		return _rest_gauge;
	}

	public void set_rest_gauge(int val) {
		_bit |= 0x1;
		_rest_gauge = val;
	}

	public boolean has_rest_gauge() {
		return (_bit & 0x1) == 0x1;
	}

	public int get_default_ration() {
		return _default_ration;
	}

	public void set_default_ration(int val) {
		_bit |= 0x2;
		_default_ration = val;
	}

	public boolean has_default_ration() {
		return (_bit & 0x2) == 0x2;
	}

	public int get_extra_exp_ratio() {
		return _extra_exp_ratio;
	}

	public void set_extra_exp_ratio(int val) {
		_bit |= 0x4;
		_extra_exp_ratio = val;
	}

	public boolean has_extra_exp_ratio() {
		return (_bit & 0x4) == 0x4;
	}

	public int get_reduce_efficiency() {
		return _reduce_efficiency;
	}

	public void set_reduce_efficiency(int val) {
		_bit |= 0x8;
		_reduce_efficiency = val;
	}

	public boolean has_reduce_efficiency() {
		return (_bit & 0x8) == 0x8;
	}

	public boolean get_bless_of_blood_pledge() {
		return _bless_of_blood_pledge;
	}

	public void set_bless_of_blood_pledge(boolean val) {
		_bit |= 0x10;
		_bless_of_blood_pledge = val;
	}

	public boolean has_bless_of_blood_pledge() {
		return (_bit & 0x10) == 0x10;
	}

	@Override
	public long getInitializeBit() {
		return (long) _bit;
	}

	@Override
	public int getMemorizedSerializeSizedSize() {
		return _memorizedSerializedSize;
	}

	@Override
	public int getSerializedSize() {
		int size = 0;
		if (has_rest_gauge())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(1, _rest_gauge);
		if (has_default_ration())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(2, _default_ration);
		if (has_extra_exp_ratio())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(3, _extra_exp_ratio);
		if (has_reduce_efficiency())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeInt32Size(4, _reduce_efficiency);
		if (has_bless_of_blood_pledge())
			size += l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream.computeBoolSize(5, _bless_of_blood_pledge);
		_memorizedSerializedSize = size;
		return size;
	}

	@Override
	public boolean isInitialized() {
		if (_memorizedIsInitialized == 1)
			return true;
		if (!has_rest_gauge()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_default_ration()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_extra_exp_ratio()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		if (!has_reduce_efficiency()) {
			_memorizedIsInitialized = -1;
			return false;
		}
		_memorizedIsInitialized = 1;
		return true;
	}

	@Override
	public void writeTo(l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream output) throws IOException {
		if (has_rest_gauge()) {
			output.wirteInt32(1, _rest_gauge);
		}
		if (has_default_ration()) {
			output.wirteInt32(2, _default_ration);
		}
		if (has_extra_exp_ratio()) {
			output.wirteInt32(3, _extra_exp_ratio);
		}
		if (has_reduce_efficiency()) {
			output.wirteInt32(4, _reduce_efficiency);
		}
		if (has_bless_of_blood_pledge()) {
			output.writeBool(5, _bless_of_blood_pledge);
		}
	}

	@Override
	public l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream writeTo(
			l1j.server.MJTemplate.MJProto.MJEProtoMessages message) {
		l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream stream = l1j.server.MJTemplate.MJProto.IO.ProtoOutputStream
				.newInstance(getSerializedSize() + WireFormat.WRITE_EXTENDED_SIZE, message.toInt());
		try {
			writeTo(stream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return stream;
	}

	@Override
	public MJIProtoMessage readFrom(l1j.server.MJTemplate.MJProto.IO.ProtoInputStream input) throws IOException {
		while (!input.isAtEnd()) {
			int tag = input.readTag();
			switch (tag) {
			default: {
				return this;
			}
			case 0x00000008: {
				set_rest_gauge(input.readInt32());
				break;
			}
			case 0x00000010: {
				set_default_ration(input.readInt32());
				break;
			}
			case 0x00000018: {
				set_extra_exp_ratio(input.readInt32());
				break;
			}
			case 0x00000020: {
				set_reduce_efficiency(input.readInt32());
				break;
			}
			case 0x00000028: {
				set_bless_of_blood_pledge(input.readBool());
				break;
			}
			}
		}
		return this;
	}

	@Override
	public MJIProtoMessage readFrom(l1j.server.server.GameClient clnt, byte[] bytes) {
		l1j.server.MJTemplate.MJProto.IO.ProtoInputStream is = l1j.server.MJTemplate.MJProto.IO.ProtoInputStream
				.newInstance(bytes, l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE,
						((bytes[3] & 0xff) | (bytes[4] << 8 & 0xff00))
								+ l1j.server.MJTemplate.MJProto.WireFormat.READ_EXTENDED_SIZE);
		try {
			readFrom(is);

			if (!isInitialized())
				return this;
			// TODO : 아래부터 처리 코드를 삽입하십시오. made by MJSoft.

		} catch (Exception e) {
			e.printStackTrace();
		}
		return this;
	}

	@Override
	public MJIProtoMessage copyInstance() {
		return new SC_REST_EXP_INFO_NOTI();
	}

	@Override
	public MJIProtoMessage reloadInstance() {
		return newInstance();
	}

	@Override
	public void dispose() {
		_bit = 0;
		_memorizedIsInitialized = -1;
	}
}
