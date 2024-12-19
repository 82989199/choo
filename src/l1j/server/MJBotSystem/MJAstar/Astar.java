package l1j.server.MJBotSystem.MJAstar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import l1j.server.MJBotSystem.Loader.MJBotLoadManager;
import l1j.server.MJBotSystem.Pool.MJNodePool;
import l1j.server.server.model.map.L1Map;
import l1j.server.server.model.map.L1WorldMap;
import l1j.server.server.utils.MJCommons;

/**********************************
 * 
 * The class is only for L1(Lineage1) Ai Bot.
 * made by mjsoft, 2016.
 *  
 **********************************/
public class Astar {
	private static Comparator<Node> _comp;
	private ArrayList<Node> _opens;
	private ArrayList<Node> _closes;
	private int				_range;
	private int				_dx;
	private int				_dy;
	private int				_mid;
	public Astar(){
		if(_comp == null)
			_comp = new Ascending();
		
		_opens 	= new ArrayList<Node>(MJBotLoadManager.MBO_ASTAR_OPENS_SIZE);
		_closes = new ArrayList<Node>(MJBotLoadManager.MBO_ASTAR_CLOSES_SIZE);
		_range	= 0;
		_dx		= 0;
		_dy		= 0;
	}
	
	public void setRange(int i){
		_range = i;
	}
	
	public void setMapId(int i){
		_mid = i;
	}
	
	public void release(){
		MJNodePool pool = MJNodePool.getInstance();
		if(_opens.size() > 0){
			pool.push(_opens);
			_opens.clear();
		}
		if(_closes.size() > 0){
			pool.push(_closes);
			_closes.clear();
		}
	}
	
	public Node find(int sx, int sy, int dx, int dy){
		int count		= 0;
		Node best		= null;
		Node ret		= null;
		Node src		= MJNodePool.getInstance().pop();
		_dx				= dx;
		_dy				= dy;
		src.degree		= 0;
		src.distance	= MJCommons.getDistance(dx, dy, sx, sy);
		src.factor		= src.distance;
		src.x			= sx;
		src.y			= sy;
		_opens.add(src);
		
		if(_mid < 0)
			_mid = 4;
		
		if(_range < 1)
			_range = 1;
		
		if(equalsPos(src) != null)
			return src;
		
		int x = 0;
		int y = 0;
		while(count < MJBotLoadManager.MBO_ASTAR_LOOP){
			if(_opens.size() <= 0)
				break;
			
			Collections.sort(_opens, _comp);
			best = _opens.get(0);
			_opens.remove(0);
			if(best != null)
				_closes.add(best);
			
			for(int i=0; i<8; i++){
				x = best.x + MJCommons.HEADING_TABLE_X[i];
				y = best.y + MJCommons.HEADING_TABLE_Y[i];
				if(isAvailableTile(x, y, i)){
					ret = extendChildNode(best, x, y, i);
					if(ret != null)
						return ret;
				}
			}
			count++;
		}
		return best;
	}
	
	/** 자식 노드를 확장한다. **/
	private Node extendChildNode(Node node, int x, int y, int h){
		int i		= 0;
		int size	= 0;
		int degree	= node.degree + 1;
		Node old	= null;
		Node child	= null;
		
		size = _opens.size();
		for(i=0; i<size; i++){
			old = _opens.get(i);
			if(old == null)
				continue;
			
			if(old.x == x && old.y == y){
				if(degree < old.degree){
					old.parent 			= node;
					old.degree			= degree;
					old.factor			= old.distance + degree;
				}
				return null;
			}
		}
		
		size = _closes.size();
		for(i=0; i<size; i++){
			old = _closes.get(i);
			if(old == null)
				continue;
			
			if(old.x == x && old.y == y){
				if(degree < old.degree){
					old.parent 			= node;
					old.degree			= degree;
					old.factor			= old.distance + degree;
				}
				return null;
			}
		}
		child 				= MJNodePool.getInstance().pop();
		child.parent 		= node;
		child.degree 		= degree;
		child.distance 		= MJCommons.getDistance(x, y, _dx, _dy);
		child.factor		= child.distance + degree;
		child.x				= x;
		child.y				= y;
		_opens.add(child);
		
		return equalsPos(child);
	}
	
	/** 해당 노드가 목적지 허용치 안에 들어왔는가.? 비교를 줄이기 위해 node들어온 node형으로 반환 **/
	private Node equalsPos(Node node){
		int distanceX 	= Math.abs(node.x - _dx);
		int distanceY	= Math.abs(node.y - _dy);
		if(distanceX <= _range && distanceY <= _range){
			if(_range >= 2){
				if(!MJCommons.isPassableLine(node.x, node.y, _dx, _dy, (short)_mid))
					return null;
			}
			return node;
		}
		return null;
	}
	
	/** 해당 타일로 이동할 수 있는가. ver1.0**/
	public boolean isAvailableTile(int x, int y, int h){
		L1Map map = L1WorldMap.getInstance().getMap((short)_mid);
		if (!map.isPassable(x, y))
			return false;
		if (map.isExistDoor(x, y))
			return false;
		return true;
	}
//	/** 해당 타일로 이동할 수 있는가. ver2.0**/
//	public boolean isAvailableTile(int x, int y, int heading){
//	    L1Map map = L1WorldMap.getInstance().getMap((short)_mid);
//	    if (!map.isPassable(x, y))
//	        return false;
//	    if (map.isExistDoor(x, y))
//	        return false;
//	    // 대각선 이동의 경우, 장애물 우회 로직 추가
//	    if (heading % 2 != 0) { // 대각선 방향인 경우
//	        // 대각선 이동이 가능한지 추가 검사 필요
//	        // 예: 대각선 이동에 필요한 인접 타일도 이동 가능한지 확인
//	    }
//	    return true;
//	}
	
	/** 정렬을 지원하는 클래스. **/
	public class Ascending implements Comparator<Node>{

		@Override
		public int compare(Node node1, Node node2) {
			if(node1 == null)
				return 1;
			if(node2 == null)
				return -1;
			if(node1.degree < node2.degree)
				return 1;
			else if(node1.degree > node2.degree)
				return -1;
			else if(node2.factor < node1.factor)
				return 1;
			else if(node2.factor > node1.factor)
				return -1;
			return 0;
		}
	}
}
