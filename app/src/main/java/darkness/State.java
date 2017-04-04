package darkness;

/**
 * 状态空间
 *
 * @author lihanguang
 *
 */
public class State implements Cloneable {
	// 最大传教士或野人数量
	public static int N;
	// 船的最大承载量
	public static int B;
	// 传教士人数
	public int ml;
	// 野人人数
	public int cl;
	// 船,1表示有 2表示无
	public int bl;

	public State(int ml, int cl, int bl) {
		this.ml = ml;
		this.cl = cl;
		this.bl = bl;
	}

	public boolean isSafe() {
		return ml == 0 || ml == N || ml == cl;
	}

	public boolean isSuccess() {
		return ml == 0 && cl == 0 && bl == 2;
	}

	public State in(int ml, int cl) {
		if (this.bl == 2) {
			ml = this.ml + ml;
			cl = this.cl + cl;
			if (ml > N || cl > N)
				return null;
			int bl = 1;
			return new State(ml, cl, bl);
		}
		return null;
	}

	public State out(int ml, int cl) {
		if (this.bl == 1) {
			ml = this.ml - ml;
			cl = this.cl - cl;
			if (ml < 0 || cl < 0)
				return null;
			int bl = 2;
			return new State(ml, cl, bl);
		}
		return null;
	}

	public String toString() {
		return "传道士：" + ml + "，野人：" + cl + "，船：" + bl + "\n";
	};

	@Override
	public int hashCode() {
		return ("ml:" + ml + ",cl:" + cl + ",bl:" + bl).hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof State) {
			State state = (State) obj;
			if (state.ml == ml && state.cl == cl && state.bl == bl)
				return true;
		}
		return false;
	}

	@Override
	protected State clone() {
		return new State(ml, cl, bl);
	}
}
