import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Scanner;

class Graph {
	public int[][] g;
	private boolean visited[];
	private int n;

	public Graph(int n) {
		g = new int[n + 1][n + 1];
		visited = new boolean[n + 1];
		this.n = n;
	}

	public void addEdge(int f, int l, int w) {
		g[f][l] = w;
		g[l][f] = w;
	}

	/**
	 * Fills array colors
	 * 
	 * @param colors
	 * @return
	 * @throws Exception
	 */
	public List<List<Integer>> cyclesSearch(List<List<Integer>> colors) throws Exception {
		List<List<Integer>> res = new LinkedList<List<Integer>>();
		for (int i = 1; i <= n; i++) {
			List<Integer> list = dfsCyclesSearch(i);
			if (list != null) {
				res.add(list);
				deleteCycle(list, colors);
			}
		}
		return res;
	}

	private List<Integer> dfsCyclesSearch(int start) {
		for (int i = 1; i <= n; i++) {
			visited[i] = false;
		}
		LinkedList<Integer> stack = new LinkedList<Integer>();
		stack.addLast(start);
		visited[start] = true;
		while (stack.size() > 0) {
			int cur = stack.getLast();
			if (g[cur][start] != 0 && stack.size() > 2) {
				return stack;
			}
			int next = findNext(cur);
			if (next != -1) { // остановка поиска в глубину
				stack.addLast(next);
				visited[next] = true;
			} else {
				stack.removeLast();
			}
		}
		return null;
	}

	private void deleteCycle(List<Integer> list, List<List<Integer>> colors) throws Exception {
		Iterator<Integer> it = list.iterator();
		List<Integer> colorList = new LinkedList<Integer>();
		colors.add(colorList);
		int prev = it.next();
		int next = it.next();
		colorList.add(g[prev][next]);
		deleteEdge(prev, next);
		while (it.hasNext()) {
			prev = next;
			next = it.next();
			colorList.add(g[prev][next]);
			deleteEdge(prev, next);
		}
		colorList.add(g[next][list.get(0)]);
		deleteEdge(next, list.get(0));
	}

	private void deleteEdge(int i, int j) throws Exception {
		if (g[i][j] == 0)
			throw new Exception("no edge");
		g[i][j] = 0;
		g[j][i] = 0;
	}

	private int findNext(int ver) {
		for (int i = 1; i <= n; i++) {
			if (g[ver][i] != 0 && !visited[i]) {
				return i;
			}
		}
		return -1;
	}

	public static List<int[]> ArrayColors(List<List<Integer>> colorsCycles, int m) {

		List<int[]> numbersColors = new LinkedList<int[]>();
		for (int i = 0; i < colorsCycles.size(); i++) {
			numbersColors.add(new int[m + 1]);
		}
		Iterator<List<Integer>> itCycles = colorsCycles.iterator();
		Iterator<int[]> itResCycles = numbersColors.iterator();
		for (int i = 0; i < colorsCycles.size(); i++) {
			Iterator<Integer> itEdges = itCycles.next().iterator();
			int[] numberColorsInCycle = itResCycles.next();
			while (itEdges.hasNext()) {
				numberColorsInCycle[itEdges.next()]++;
			}
		}
		return numbersColors;
		/*
		 * int[][] numberColors = new int[colorsCycles.size()][m];
		 * Iterator<List<Integer>> itCycles = colorsCycles.iterator(); for (int
		 * i = 0; i < colorsCycles.size(); i++) { Iterator<Integer> itEdges =
		 * itCycles.next().iterator(); while (itEdges.hasNext()) {
		 * numberColors[i][itEdges.next()]++; } } return numberColors;
		 */
	}
}

public class Codeforces_720B {
	private static int getFinalNumber(List<int[]> cycles) {
		HashSet<Integer> colors = new HashSet<>();
		Iterator<int[]> itCycles = cycles.iterator();
		outer: while (itCycles.hasNext()) {
			int[] arr = itCycles.next();
			for (int i = 1; i < arr.length; i++) {
				if (arr[i] > 1) {
					for (int j = 1; j < arr.length; j++) {
						if (arr[j] >= 1) {
							colors.add(j);
						}
					}
					itCycles.remove();
					continue outer;
				}
			}
		}
		itCycles = cycles.iterator();
		outer: while (itCycles.hasNext()) {
			int[] arr = itCycles.next();
			for (Integer i : colors) {
				if (arr[i] > 0) {
					for (int j = 1; j < arr.length; j++) {
						if (arr[j] >= 1) {
							colors.add(j);
						}
					}
					itCycles.remove();
					continue outer;
				}
			}
		}

		class MyPair implements Comparable<MyPair> {
			int key;
			int val;

			public MyPair(int key, int val) {
				this.key = key;
				this.val = val;
			}

			@Override
			public int compareTo(MyPair o) {
				if (this.key > o.key)
					return 1;
				else if (this.key < o.key)
					return -1;
				else
					return 0;
			}

		}

		HashMap<Integer, List<Integer>> mapColorCycle = new HashMap<>();
		itCycles = cycles.iterator();
		for (int i = 0; itCycles.hasNext(); i++) {
			int[] arr = itCycles.next();
			for (int j = 1; j < arr.length; j++) {
				if (arr[i] > 0) {
					List<Integer> list;
					if ((list = mapColorCycle.get(j)) == null) {
						list = new LinkedList<Integer>();
						mapColorCycle.put(j, list);
					}
					list.add(i);
				}
			}
		}
		LinkedList<MyPair> mapNumberCycle = new LinkedList<>();
		List<Integer> list;
		int[] mapCycleNumber = new int[cycles.size()];
		for (Integer i : mapColorCycle.keySet()){
			list = mapColorCycle.get(i);
			if(list.size()>1){
				for (int i : list)
					mapCycleNumber[i]++;
			}
		}
		for (int i = 0; i<mapCycleNumber.length; i++){
			if (mapCycleNumber[i] > 0)
				mapNumberCycle.add(new MyPair(mapCycleNumber[i], i));
		}
		Collections.sort(mapNumberCycle);
		
	}

	public static void main(String[] args) throws Exception {
		Scanner in = new Scanner(System.in);
		String[] s = in.nextLine().split(" ");
		int n = Integer.parseInt(s[0]);
		int m = Integer.parseInt(s[1]);
		Graph gr = new Graph(n), grUtil = new Graph(n);
		while (in.hasNext()) {
			s = in.nextLine().split(" ");
			int f = Integer.parseInt(s[0]), l = Integer.parseInt(s[1]), w = Integer.parseInt(s[2]);
			gr.addEdge(f, l, w);
			grUtil.addEdge(f, l, w);
		}
		in.close();
		List<List<Integer>> colorsCycles = new LinkedList<List<Integer>>();
		List<List<Integer>> cycles = grUtil.cyclesSearch(colorsCycles);
		List<int[]> numberColors = Graph.ArrayColors(colorsCycles, m); // [cycle][color]
																		// =
																		// number
		int res = getFinalNumber(numberColors);
		System.out.println(res);
	}

}
