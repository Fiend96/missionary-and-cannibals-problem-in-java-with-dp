package darkness;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Main {

    /**
     * 已经获取的该状态下的最短路径集合
     */
    private Map<State, List<Drive>> visState;
    /**
     * 合法操作表
     */
    private List<Drive> drives;


    // 直接在Java程序中运行
//	public static void main(String[] args) {
//		// 初始化
//		Scanner s = new Scanner(System.in);
//		int n = s.nextInt();
//		int k = s.nextInt();
//		List<Drive> driveList = new ArrayList<>();
//		State.N = n;
//		State.B = k;
//		State state1 = new State(n, n, 1);
//		new Main().execute(n, k, driveList);
//		if (driveList == null || !checkSafe(driveList, state1.clone())) {
//			System.out.println("不合法");
//			return;
//		}
//		for (Drive drive : driveList) {
//			String first = null;
//			String second = null;
//			State left = null;
//			if (state1.bl == 1) {
//				// 左岸
//				first = "左岸";
//				second = "右岸";
//				left = state1.out(drive.ml, drive.cl);
//			} else if (state1.bl == 2) {
//				// 右岸
//				first = "右岸";
//				second = "左岸";
//				left = state1.in(drive.ml, drive.cl);
//			} else {
//				System.out.println("出错");
//				break;
//			}
//			if (left == null || !left.isSafe()) {
//				System.out.println("被吃了");
//				break;
//			}
//			System.out.println(drive.ml + "个传教士和" + drive.cl + "个野人从" + first + "到了" + second);
//			System.out.println("右岸有：" + (State.N - left.ml) + "个传道士和" + (State.N - left.cl) + "个野人");
//			System.out.println();
//			state1 = left;
//		}
//	}

    /**
     * 查找一条可执行的路径
     *
     * @param n         人数
     * @param k         船载量
     * @param driveList 返回路径，若无解则返回一个空List
     */
    public void execute(int n, int k, List<Drive> driveList) {
        State.N = n;
        State.B = k;
        State state = new State(State.N, State.N, 1);
        drives = getSafeDrive(State.B);
        visState = new HashMap<>();
        Map<State, Boolean> checkMap = new HashMap<>();
        List<Drive> drives = findMinRoad(state, checkMap);
        if (drives != null && drives.size() != 0) {
            driveList.addAll(drives);
        }
    }

    /**
     * 找到一条该状态下最短的路径
     *
     * @param state       当前状态
     * @param checkStates 该路径下已经遍历过的状态，避免循环
     * @return 该状态下最短路径集合
     */
    private List<Drive> findMinRoad(State state, Map<State, Boolean> checkStates) {
        List<Drive> driveList = null;
        if (state.isSuccess()) {
            return new ArrayList<>();
        }
        if (visState.containsKey(state)) {
            // 已经存在最短路径
            return new ArrayList<>(visState.get(state));
        }
        for (Drive drive : drives) {
            // 查找一个最短的路径
            State target = null;
            if (state.bl == 1) {
                target = state.out(drive.ml, drive.cl);
            } else if (state.bl == 2) {
                target = state.in(drive.ml, drive.cl);
            }
            if (target == null || !target.isSafe() || checkStates.containsKey(target))// 无法进行
                continue;
            Map<State, Boolean> map = new HashMap<>(checkStates);
            map.put(state, true);
            List<Drive> targets = findMinRoad(target, map);
            if (targets == null) // 无法找到最短路径
                continue;
            targets.add(0, drive);
            if (driveList == null) {
                driveList = targets;
            } else if (targets.size() < driveList.size()) {
                driveList = targets;
            }
        }
        // 保存最短路径
        if (driveList != null && driveList.size() != 0 && checkSafe(driveList, state.clone())) {
            visState.put(state, driveList);
        }
        return driveList == null ? null : new ArrayList<>(driveList);

    }

    /**
     * 检测这条路径在该状态下是否合法
     *
     * @param drives 路径集合
     * @param target 状态空间
     * @return 是否合法
     */
    private static boolean checkSafe(List<Drive> drives, State target) {
        for (Drive drive : drives) {
            State left = null;
            if (target.bl == 1) {
                left = target.out(drive.ml, drive.cl);
            } else if (target.bl == 2) {
                left = target.in(drive.ml, drive.cl);
            }
            if (left == null || !left.isSafe())
                return false;
            target = left;
        }
        if (!target.isSuccess())
            return false;
        return true;
    }

    /**
     * 计算合法的操作集合
     *
     * @param k 船载量
     * @return 合法操作集合
     */
    private List<Drive> getSafeDrive(int k) {
        List<Drive> drives = new ArrayList<>();
        // 野人为n，传教士为0的情况
        for (int i = 0; i <= k; i++) {
            // i 表示传教士的数量

            for (int j = 0; j <= k; j++) {
                if ((i + j) == 0 || (i + j) > k)
                    continue;
                if (i >= j || i == 0 || j == 0) {
                    // j 表示野人的数量
                    Drive drive = new Drive();
                    drive.ml = i;
                    drive.cl = j;
                    drives.add(drive);
                }
            }
        }
        return drives;
    }
}
