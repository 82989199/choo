/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * http://www.gnu.org/copyleft/gpl.html
 */
package l1j.server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import l1j.server.server.datatables.MapsTable;
import l1j.server.server.model.map.L1Map;
import l1j.server.server.model.map.L1V1Map;
import l1j.server.server.utils.FileUtil;


/**
 * 缓存文本地图以缩短读取时间
 */
public class CachedMapReader extends MapReader {

	/** 文本地图文件夹路径 */
	private static final String MAP_DIR = "./maps/";

	/** 缓存地图文件夹路径 */
	private static final String CACHE_DIR = "./data/mapcache/";

	/**
	 * 获取所有地图ID列表
	 *
	 * @return ArraryList
	 */
	private ArrayList<Integer> listMapIds() {

		File mapDir = new File(MAP_DIR); // 地图目录
		File mapFile = null; // 地图文件
		ArrayList<Integer> ids = new ArrayList<Integer>(mapDir.list().length);
		for (String name : mapDir.list()) {
			mapFile = new File(mapDir, name);
			if (!mapFile.exists()) {
				continue;
			}
			if (!FileUtil.getExtension(mapFile).toLowerCase().equals("txt")) {
				continue;
			}
			int id = 0; // 地图ID
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
	 * 将指定地图编号的文本地图转换为缓存地图
	 *
	 * @param mapId 地图编号
	 * @return L1V1Map
	 * @throws IOException
	 */
	private L1V1Map cacheMap(final int mapId) throws IOException {
		File file = new File(CACHE_DIR);
		if (!file.exists()) {
			file.mkdir();
		}

		L1V1Map map = (L1V1Map) new TextMapReader().read(mapId);

		DataOutputStream out = new DataOutputStream(new BufferedOutputStream(
				new FileOutputStream(CACHE_DIR + mapId + ".map")));

		out.writeInt(map.getId()); // 地图ID
		out.writeInt(map.getX()); // X坐标
		out.writeInt(map.getY()); // Y坐标  
		out.writeInt(map.getWidth()); // 宽度
		out.writeInt(map.getHeight()); // 高度

		for (byte[] line : map.getRawTiles()) {
			for (byte tile : line) {
				out.writeByte(tile);
			}
		}
		out.flush();
		out.close();

		return map;
	}

	/**
	 * 读取指定地图编号的缓存地图
	 *
	 * @param mapId 地图编号
	 * @return L1Map
	 * @throws IOException
	 */
	@Override
	public L1Map read(final int mapId) throws IOException {
		File file = new File(CACHE_DIR + mapId + ".map");
		if (!file.exists()) {
			return cacheMap(mapId);
		}

		DataInputStream in = new DataInputStream(new BufferedInputStream(
				new FileInputStream(CACHE_DIR + mapId + ".map")));

		int id = in.readInt(); // 地图ID
		if (mapId != id) {
			extracted();
		}

		int xLoc = in.readInt(); // X坐标起始位置
		int yLoc = in.readInt(); // Y坐标起始位置
		int width = in.readInt(); // 地图宽度
		int height = in.readInt(); // 地图高度

		byte[][] tiles = new byte[width][height]; // 地图数据
		for (byte[] line : tiles) {
			in.read(line);
		}

		in.close();
		L1V1Map map = new L1V1Map(id, tiles, xLoc, yLoc,
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

	private void extracted() throws FileNotFoundException {
		throw new FileNotFoundException();
	}

	/**
	 * 读取所有缓存地图
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
}
