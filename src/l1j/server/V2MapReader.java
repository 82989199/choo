package l1j.server;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.InflaterInputStream;

import l1j.server.server.datatables.MapsTable;
import l1j.server.server.model.map.L1Map;
import l1j.server.server.model.map.L1V2Map;
import l1j.server.server.utils.BinaryInputStream;
import l1j.server.server.utils.FileUtil;

/**
 * 读取二进制地图文件(v2maps/\d*.txt)
 */
public class V2MapReader extends MapReader {

	/** 地图文件夹路径 */
	private static final String MAP_DIR = "./v2maps/";

	/**
	 * 获取所有地图ID列表
	 *
	 * @return ArraryList
	 */
	private ArrayList<Integer> listMapIds() {
		ArrayList<Integer> ids = new ArrayList<Integer>();

		File mapDir = new File(MAP_DIR);
		File mapFile = null;
		for (String name : mapDir.list()) {
			mapFile = new File(mapDir, name);
			if (!mapFile.exists()) {
				continue;
			}
			if (!FileUtil.getExtension(mapFile).toLowerCase().equals("md")) {
				continue;
			}
			int id = 0;
			try {
				String idStr = FileUtil.getNameWithoutExtension(mapFile);
				id = Integer.parseInt(idStr);
			} catch (NumberFormatException e) {
				continue;
			}
			ids.add(id);
		}
		return ids;
	}

	/**
	 * 读取所有二进制地图
	 *
	 * @return Map
	 * @throws IOException
	 */
	@Override
	public Map<Integer, L1Map> read() throws IOException {
		Map<Integer, L1Map> maps = new HashMap<Integer, L1Map>();
		for (int id : listMapIds()) {
			maps.put(id, read(id));
		}
		return maps;
	}

	/**
	 * 读取指定地图编号的二进制地图
	 *
	 * @param mapId 地图编号
	 * @return L1Map
	 * @throws IOException
	 */
	@Override
	public L1Map read(final int mapId) throws IOException {
		File file = new File(MAP_DIR + mapId + ".md");
		if (!file.exists()) {
			extracted(mapId);
		}

		BinaryInputStream in = new BinaryInputStream(new BufferedInputStream(
				new InflaterInputStream(new FileInputStream(file))));

		int id = in.readInt(); // 地图ID
		if (mapId != id) {
			extracted(mapId);
		}

		int xLoc = in.readInt(); // X坐标起始位置
		int yLoc = in.readInt(); // Y坐标起始位置
		int width = in.readInt(); // 地图宽度
		int height = in.readInt(); // 地图高度

		byte[] tiles = new byte[width * height * 2];
		for (int i = 0; i < width * height * 2; i++) {
			tiles[i] = (byte) in.readByte();
		}
		in.close();

		L1V2Map map = new L1V2Map(id, tiles, xLoc, yLoc, width, height,
				MapsTable.getInstance().isUnderwater(mapId), // 是否为水下地图
				MapsTable.getInstance().isMarkable(mapId), // 是否可标记
				MapsTable.getInstance().isTeleportable(mapId), // 是否可传送
				MapsTable.getInstance().isEscapable(mapId), // 是否可使用回城卷轴
				MapsTable.getInstance().isUseResurrection(mapId), // 是否可使用复活术
				MapsTable.getInstance().isUsePainwand(mapId), // 是否可使用疼痛魔杖
				MapsTable.getInstance().isEnabledDeathPenalty(mapId), // 是否有死亡惩罚
				MapsTable.getInstance().isTakePets(mapId), // 是否可携带宠物
				MapsTable.getInstance().isRecallPets(mapId), // 是否可召唤宠物
				MapsTable.getInstance().isUsableItem(mapId), // 是否可使用物品
				MapsTable.getInstance().isUsableSkill(mapId), // 是否可使用技能
				MapsTable.getInstance().isRuler(mapId)); // 是否为统治区域
		return map;
	}

	private void extracted(final int mapId) throws FileNotFoundException {
		throw new FileNotFoundException("MapId: " + mapId);
	}
}
