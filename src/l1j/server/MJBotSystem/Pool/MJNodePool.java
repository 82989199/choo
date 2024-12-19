package l1j.server.MJBotSystem.Pool;

import java.util.ArrayDeque;
import java.util.ArrayList;

import l1j.server.MJBotSystem.Loader.MJBotLoadManager;
import l1j.server.MJBotSystem.MJAstar.Node;

/**
 * The class is only for L1(Lineage1) Ai Bot.
 * made by mjsoft, 2016.
 * **/

/** 과도한 메모리 사용을 자제시키기 위해 **/
/** 늘어나는 풀링이 부담스럽다면, 맥스 풀 사이즈를 지정하도록. **/
public class MJNodePool {
	
	private static MJNodePool _instance;
	public static MJNodePool getInstance(){
		if(_instance == null)
			_instance = new MJNodePool();
		return _instance;
	}
	
	private ArrayDeque<Node> 	_pool;
	private Object 				_lock;
	private MJNodePool(){
		_lock	= new Object();
		_pool 	= new ArrayDeque<Node>(MJBotLoadManager.MBO_ASTAR_NODEPOOL_SIZE);
		
		for(int i=0; i<MJBotLoadManager.MBO_ASTAR_NODEPOOL_SIZE; i++)
			_pool.push(new Node());
	}
	
	public void clear(){
		_pool.clear();
	}
	
	public Node pop(){
		synchronized(_lock){
			if(_pool.isEmpty())
				return new Node();
			else
				return _pool.pop();
		}
	}
	
	public void push(Node node){
		if(node == null) return;
		node.clear();
		synchronized(_lock){
			_pool.push(node);
		}
	}
	
	public void push(ArrayDeque<Node> nQ){
		if(nQ == null)
			return;
		
		Node tmp = null;
		synchronized(_lock){
			while(!nQ.isEmpty()){
				tmp = nQ.poll();
				if(tmp == null)
					continue;
				
				tmp.clear();
				_pool.push(tmp);
			}
		}
		
		nQ.clear();
	}
	
	public void push(ArrayList<Node> nodes){
		if(nodes == null)
			return;
		
		int size = nodes.size();
		Node tmp = null;
		synchronized(_lock){
			for(int i=0; i<size; i++){
				tmp = nodes.get(i);
				tmp.clear();
				_pool.push(tmp);
			}
		}
	}
}
